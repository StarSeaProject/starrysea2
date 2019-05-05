package top.starrysea.service.impl;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.starrysea.common.Common;
import top.starrysea.common.Condition;
import top.starrysea.common.ServiceResult;
import top.starrysea.dao.*;
import top.starrysea.exception.EmptyResultException;
import top.starrysea.exception.LogicException;
import top.starrysea.exception.UpdateException;
import top.starrysea.kql.facede.KumaRedisDao;
import top.starrysea.mq.MessageSender;
import top.starrysea.object.dto.*;
import top.starrysea.object.view.in.ExportXlsCondition;
import top.starrysea.object.view.in.OrderDetailForAddOrder;
import top.starrysea.object.view.out.AreaForAddOrder;
import top.starrysea.object.view.out.CityForAddOrder;
import top.starrysea.object.view.out.ProvinceForAddOrder;
import top.starrysea.service.IOrderService;
import top.starrysea.service.mail.IMailService;

import javax.annotation.Resource;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static top.starrysea.common.Const.*;
import static top.starrysea.common.ResultKey.*;
import static top.starrysea.common.ServiceResult.FAIL_SERVICE_RESULT;
import static top.starrysea.common.ServiceResult.SUCCESS_SERVICE_RESULT;
import static top.starrysea.dao.impl.OrderDaoImpl.PAGE_LIMIT;

@Service("orderService")
public class OrderServiceImpl implements IOrderService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private IOrderDao orderDao;
	@Autowired
	private IWorkTypeDao workTypeDao;
	@Autowired
	private IProvinceDao provinceDao;
	@Resource(name = "orderMailService")
	private IMailService orderMailService;
	@Resource(name = "sendOrderMailService")
	private IMailService sendOrderMailService;
	@Resource(name = "modifyOrderMailService")
	private IMailService modifyOrderMailService;
	@Autowired
	private IOrderDetailDao orderDetailDao;
	@Autowired
	private KumaRedisDao kumaRedisDao;
	@Autowired
	private MessageSender messageSender;
	@Autowired
	private IPostageDao postageDao;

	@Override
	public ServiceResult queryAllOrderService(Condition condition, Orders order) {
		List<Orders> ordersList = orderDao.getAllOrderDao(condition, order);
		int totalPage = 0;
		int count = orderDao.getOrderCountDao(condition, order);
		if (count % PAGE_LIMIT == 0) {
			totalPage = count / PAGE_LIMIT;
		} else {
			totalPage = (count / PAGE_LIMIT) + 1;
		}

		return ServiceResult.of(true).setResult(LIST_1, ordersList).setNowPage(condition.getPage())
				.setTotalPage(totalPage);
	}

	@Override
	// 根据订单号查询一个订单的具体信息以及发货情况
	public ServiceResult queryOrderService(Orders order) {
		Orders o = orderDao.getOrderDao(order);
		List<OrderDetail> ods = orderDetailDao.getAllOrderDetailDao(new OrderDetail.Builder().order(order).build());
		return ServiceResult.of(true).setResult(ORDER, o).setResult(LIST_1, ods);
	}

	@Override
	// 用户对一个作品进行下单，同时减少该作品的库存
	@Transactional
	public ServiceResult addOrderService(Orders order, List<OrderDetail> orderDetails) {
		try {
			for (OrderDetail orderDetail : orderDetails) {
				orderDetail.setOrder(order);
				if (orderDetailDao.isOrderDetailExistDao(orderDetail))
					throw new LogicException("购物车中有已经领取过的应援物,不能重复领取");
				WorkType workType = orderDetail.getWorkType();
				workType.setStock(1);
				int stock = workTypeDao.getWorkTypeStockDao(workType);
				if (stock == 0) {
					throw new EmptyResultException("购物车中有作品已被全部领取");
				} else if (stock - workType.getStock() < 0) {
					throw new LogicException("购物车中有作品库存不足");
				}
				workTypeDao.reduceWorkTypeStockDao(workType);
				orderDetail.setId(Common.getCharId("OD-", 10));
			}
			order.setOrderId(Common.getCharId("O-", 10));
			order.setOrderNum(Common.getCharId(30));
			// 通过邮费表查询出应付金额
			order.setOrderMoney(postageDao.getPostage(new Postage.Builder()
					.province(provinceDao.getProvinceByArea(order.getOrderArea().getAreaId()).getProvinceId()).build())
					.getPostageMoney() * 100);
			// 如果应付金额为0,说明是邮费表中没有记录,那么就走到付
			if (order.getOrderMoney() == 0) {
				order.setOrderStatus((short) 1);
			} else {
				order.setOrderStatus((short) 0);
				messageSender.sendMessage(ORDERS_EXCHANGE, ORIGINAL_ORDER_QUEUE, Common.toJson(order), QUEUE_TIMEOUT);
			}
			orderDao.saveOrderDao(order);
			orderDetailDao.saveOrderDetailsDao(orderDetails);
			return ServiceResult.of(true).setResult(LIST_1, orderDetails).setResult(ORDER, order);
		} catch (EmptyResultException | LogicException e) {
			logger.error(e.getMessage(), e);
			return ServiceResult.of(false).setErrInfo(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new UpdateException(e);
		}
	}

	@Override
	// 修改一个订单的状态
	public ServiceResult modifyOrderService(Orders order) {
		order.setOrderStatus((short) 2);
		orderDao.updateOrderDao(order);
		return ServiceResult.of(true).setResult(ORDER, orderDao.getOrderDao(order));
	}

	@Override
	// 删除一个订单
	@Transactional
	public ServiceResult removeOrderService(Orders order) {
		workTypeDao.updateWorkTypeStockDao(order);
		orderDao.deleteOrderDao(order);
		return SUCCESS_SERVICE_RESULT;
	}

	@Override
	@Cacheable(value = "provinces", cacheManager = "defaultCacheManager")
	public ServiceResult queryAllProvinceService() {
		List<Area> areas = provinceDao.getAllProvinceDao();
		Map<Integer, ProvinceForAddOrder> provinceVos = new HashMap<>();
		for (Area area : areas) {
			int provinceId = area.getCity().getProvince().getProvinceId();
			if (!provinceVos.containsKey(provinceId)) {
				ProvinceForAddOrder province = new ProvinceForAddOrder(provinceId,
						area.getCity().getProvince().getProvinceName());
				province.setCitys(new HashMap<>());
				provinceVos.put(provinceId, province);
			}
			ProvinceForAddOrder provinceVo = provinceVos.get(provinceId);
			int cityId = area.getCity().getCityId();
			Map<Integer, CityForAddOrder> cityVos = provinceVo.getCitys();
			if (!cityVos.containsKey(cityId)) {
				CityForAddOrder city = new CityForAddOrder(cityId, area.getCity().getCityName());
				city.setAreas(new ArrayList<>());
				cityVos.put(cityId, city);
			}
			CityForAddOrder cityVo = cityVos.get(cityId);
			cityVo.getAreas().add(new AreaForAddOrder(area.getAreaId(), area.getAreaName()));
		}
		return ServiceResult.of(true).setResult(MAP, provinceVos);
	}

	@Override
	public ServiceResult queryWorkTypeStock(List<WorkType> workTypes) {
		try {
			for (WorkType workType : workTypes) {
				Integer stock = workTypeDao.getWorkTypeStockDao(workType);
				if (stock <= 0)
					throw new LogicException("库存不足");
			}
			return SUCCESS_SERVICE_RESULT;
		} catch (EmptyResultDataAccessException e) {
			return ServiceResult.of(false).setErrInfo("该作品下没有这样的类型");
		} catch (Exception e) {
			return ServiceResult.of(false).setErrInfo(e.getMessage());
		}
	}

	@Override
	public ServiceResult exportOrderToXlsService(ExportXlsCondition exportXlsCondition) {
		if (Common.isNotNull(exportXlsCondition.getStartTime())) {
			exportXlsCondition.setStartTime(Common.string2Date(exportXlsCondition.getStartTime()).getTime() + "");
		}
		List<OrderDetail> result = orderDetailDao.getAllOrderDetailForXls(exportXlsCondition);
		HSSFWorkbook excel = new HSSFWorkbook();
		HSSFSheet sheet = excel.createSheet("发货名单");
		HSSFRow row = sheet.createRow(0);
		row.createCell(0).setCellValue("收货人姓名");
		row.createCell(1).setCellValue("作品名称");
		row.createCell(2).setCellValue("作品类型");
		row.createCell(3).setCellValue("收货地址");
		row.createCell(4).setCellValue("收货人手机");
		row.createCell(5).setCellValue("备注");
		for (int i = 0; i < result.size(); i++) {
			OrderDetail orderDetail = result.get(i);
			HSSFRow dataRow = sheet.createRow(i + 1);
			dataRow.createCell(0).setCellValue(orderDetail.getOrder().getOrderName());
			dataRow.createCell(1).setCellValue(orderDetail.getWorkType().getWork().getWorkName());
			dataRow.createCell(2).setCellValue(orderDetail.getWorkType().getName());
			dataRow.createCell(3)
					.setCellValue(orderDetail.getOrder().getOrderArea().getCity().getProvince().getProvinceName()
							+ orderDetail.getOrder().getOrderArea().getCity().getCityName()
							+ orderDetail.getOrder().getOrderArea().getAreaName()
							+ orderDetail.getOrder().getOrderAddress());
			dataRow.createCell(4).setCellValue(orderDetail.getOrder().getOrderPhone());
			dataRow.createCell(5).setCellValue(orderDetail.getOrder().getOrderRemark());
		}
		try (FileOutputStream fout = new FileOutputStream("/result.xls")) {
			excel.write(fout);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public ServiceResult resendEmailService(Orders order) {
		Orders result = orderDao.getOrderDao(order);
		if (result.getOrderStatus() == 1) {
			List<OrderDetail> ods = orderDetailDao
					.getAllResendOrderDetailDao(new OrderDetail.Builder().order(order).build());
			orderMailService.sendMailService(ods);
		} else if (result.getOrderStatus() == 2) {
			sendOrderMailService.sendMailService(result);
		}
		return SUCCESS_SERVICE_RESULT;
	}

	@Override
	public ServiceResult queryAllWorkTypeForShoppingCarService(List<WorkType> workTypes) {
		if (workTypes.isEmpty()) {
			return ServiceResult.of(false).setResult(LIST_1, new ArrayList<>());
		}
		return ServiceResult.of(true).setResult(LIST_1, workTypeDao.getAllWorkTypeForShoppingCarDao(workTypes));
	}

	@Override
	public ServiceResult modifyAddressService(Orders order) {
		orderDao.updateAddressDao(order);
		return SUCCESS_SERVICE_RESULT;
	}

	@Override
	public ServiceResult modifyAddressEmailService(Orders order) {
		Orders result = orderDao.getOrderDao(order);
		if (result.getOrderStatus() != 2) {
			modifyOrderMailService.sendMailService(result);
			return SUCCESS_SERVICE_RESULT;
		}
		return FAIL_SERVICE_RESULT;
	}

	@Override
	public ServiceResult queryShoppingCarListService(String redisKey) {
		ServiceResult serviceResult = ServiceResult.of();
		List<OrderDetailForAddOrder> orderDetailForAddOrders = kumaRedisDao.getList(redisKey,
				OrderDetailForAddOrder.class);
		if (!orderDetailForAddOrders.isEmpty()) {
			serviceResult.setResult(LIST_1, orderDetailForAddOrders);
			serviceResult.setSuccessed(true);
		} else {
			serviceResult.setResult(LIST_1, new ArrayList<OrderDetailForAddOrder>());
			serviceResult.setSuccessed(true);
		}
		return serviceResult;
	}

	@Override
	public ServiceResult addorModifyWorkToShoppingCarService(String redisKey,
	                                                         List<OrderDetailForAddOrder> orderDetailForAddOrders) {
		kumaRedisDao.set(redisKey, Common.toJson(orderDetailForAddOrders));
		return SUCCESS_SERVICE_RESULT;
	}

	@Override
	public ServiceResult removeShoppingCarListService(String redisKey) {
		kumaRedisDao.delete(redisKey);
		return SUCCESS_SERVICE_RESULT;
	}

	@Override
	/***
	 * 订单回调处理 目前没有考虑到订单自动取消的同时用户付款
	 */
	public ServiceResult notifyOrderService(Orders orders) {
		try {
			Orders o = orderDao.getOrderDao(orders);
			o.setOrderId(orders.getOrderId());
			List<OrderDetail> ods = new ArrayList<>();
			if (o.getOrderStatus() == 0) {// 排除已被支付的订单，防止多次回调
				o.setOrderStatus((short) 1);
				orderDao.updateOrderDao(o);
				ods = orderDetailDao.getAllOrderDetailDao(new OrderDetail.Builder().order(o).build());
				ods.forEach(orderDetail -> {
					orderDetail.setOrder(o);
				});
			}
			return ServiceResult.of(true).setResult(LIST_1, ods);
		} catch (Exception e) {
			return ServiceResult.FAIL_SERVICE_RESULT;
		}
	}

	@Override
	@RabbitListener(queues = CANCEL_ORDER_QUEUE)
	@Transactional
	public void cancelOrderService(String message) {
		logger.info("收到取消订单消息：" + message);
		try {
			Orders orders = Common.toObject(message, Orders.class);
			Orders o = orderDao.getOrderDao(orders);
			if (o.getOrderStatus() == 0) {
				o.setOrderStatus((short) 3);
				orderDao.updateOrderDao(o);
				List<OrderDetail> orderDetails = orderDetailDao
						.getAllOrderDetailDao(new OrderDetail.Builder().order(o).build());
				orderDetails.forEach(od -> {
					od.getWorkType().setStock(1);
					workTypeDao.increaseWorkTypeStockDao(od.getWorkType());
				});
			}
		} catch (Exception e) {
			logger.info(message + "发生了错误,原因是" + e.getMessage());
		}
	}

	@Override
	public ServiceResult getPostageMoney(int provinceId) {
		return ServiceResult.of(true).setResult(INTEGER,
				postageDao.getPostage(new Postage.Builder().province(provinceId).build()).getPostageMoney());
	}

}
