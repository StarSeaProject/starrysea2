package top.starrysea.service.impl;

import static top.starrysea.common.Common.isNotNull;
import static top.starrysea.common.ResultKey.USER;
import static top.starrysea.common.ResultKey.LIST_1;
import static top.starrysea.common.ResultKey.LIST_2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import top.starrysea.common.Common;
import top.starrysea.common.ServiceResult;
import top.starrysea.dao.IFundingDao;
import top.starrysea.dao.IOrderDetailDao;
import top.starrysea.dao.IUserDao;
import top.starrysea.dao.IWorkDao;
import top.starrysea.kql.facede.KumaRedisDao;
import top.starrysea.object.dto.Funding;
import top.starrysea.object.dto.OrderDetail;
import top.starrysea.object.dto.User;
import top.starrysea.object.dto.Work;
import top.starrysea.object.view.in.UserForSave;
import top.starrysea.object.view.out.UserFundingInfo;
import top.starrysea.object.view.out.UserFundingWorkInfo;
import top.starrysea.object.view.out.UserOrderInfo;
import top.starrysea.service.IUserService;

@Service("userService")
public class UserServiceImpl implements IUserService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IUserDao userDao;
	@Autowired
	private KumaRedisDao kumaRedisDao;
	@Autowired
	private IFundingDao fundingDao;
	@Autowired
	private IWorkDao workDao;
	@Autowired
	private IOrderDetailDao orderDetailDao;

	@Override
	public ServiceResult registerService(User user) {
		try {
			user.setUserId(Common.getCharId("U-", 20));
			List<User> userList = new ArrayList<>();
			userList.add(user);
			String activateCode = Common.getCharId(30);
			kumaRedisDao.set(activateCode, Common.toJson(userList), 600);
			user.setUserId(activateCode);
			// 由于发送邮件服务只能传一个对象所以使用userId来存储激活码了,真正的user还是存在redis中的
			return ServiceResult.of(true).setResult(USER, user);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ServiceResult.of(false).setErrInfo(e.getMessage());
		}
	}

	@Override
	public ServiceResult checkUserAvailabilityService(User user) {
		boolean checkResult = userDao.checkUserAvailabilityDao(user);
		return ServiceResult.of(checkResult).setErrInfo(checkResult ? null : "该邮箱已被注册!");
	}

	@Override
	public ServiceResult userLogin(User user) {
		User user1 = userDao.getUserDao(user);
		ServiceResult serviceResult = ServiceResult.of();
		if (isNotNull(user1)) {
			serviceResult.setSuccessed(true).setResult(USER, user1);
		} else {
			serviceResult.setSuccessed(false).setErrInfo("用户名或密码错误");
		}
		return serviceResult;
	}

	@Override
	public ServiceResult activateService(String redisKey) {
		ServiceResult serviceResult = ServiceResult.of();
		try {
			List<UserForSave> userList = kumaRedisDao.getList(redisKey, UserForSave.class);
			if (userList.isEmpty()) {
				serviceResult.setSuccessed(false).setErrInfo("无效的激活码");
				logger.info("激活失败:激活码 {} 无效", redisKey);
			} else {
				UserForSave userForSave = userList.get(0);
				userDao.saveUserDao(userForSave.toDTO());
				kumaRedisDao.delete(redisKey);
				serviceResult.setSuccessed(true).setResult(USER, userForSave.toDTO());
				logger.info("用户 {} 的激活成功", userForSave.getUserEmail());
			}
		} catch (Exception e) {
			logger.info("激活失败:使用 {} 激活时出现异常", redisKey);
			logger.error(e.getMessage(), e);
			serviceResult.setSuccessed(false).setErrInfo(e.getMessage());
		}
		return serviceResult;
	}

	@Override
	public ServiceResult getUserInfoService(String userId) {
		ServiceResult serviceResult = ServiceResult.of(true);
		User user = userDao.getUserInfoDao(userId);
		List<Funding> fundings = fundingDao.getFundingByUserDao(userId);
		// 一次性查出所有该用户参与众筹关联的作品
		List<Work> works = workDao.getWorkByActivityDao(fundings.stream()
				.map(funding -> funding.getActivity().getActivityId()).distinct().collect(Collectors.toList()));
		// 根据活动id进行分组
		Map<Integer, List<Work>> workGroupByActivity = works.stream()
				.collect(Collectors.groupingBy(work -> work.getActivity().getActivityId()));
		// 将不同活动id的作品加入到众筹记录中并按参与众筹时间排序
		List<UserFundingInfo> userFundingInfos = fundings.stream()
				.map(funding -> new UserFundingInfo(funding,
						workGroupByActivity.get(funding.getActivity().getActivityId()).stream()
								.map(UserFundingWorkInfo::new).collect(Collectors.toList())))
				.sorted((userFundingInfo1,
						userFundingInfo2) -> Common.string2Time(userFundingInfo1.getFundingTime())
								.before(Common.string2Time(userFundingInfo2.getFundingTime())) ? 1 : -1)
				.collect(Collectors.toList());

		// 一次性查出所有用户的下单记录
		List<OrderDetail> orderDetails = orderDetailDao.getOrderDetailByUser(userId);
		// 根据orderId进行分组(已按照下单时间排序)
		Map<String, List<OrderDetail>> orderDetailGroupByOrder = orderDetails.stream()
				.collect(Collectors.groupingBy(orderDetail -> orderDetail.getOrder().getOrderId()));
		// 将不同的订单详情加到对应的订单记录中
		List<UserOrderInfo> userOrderInfos = orderDetails.stream().map(orderDetail -> {
			List<OrderDetail> _orderDetail = orderDetailGroupByOrder.get(orderDetail.getOrder().getOrderId());
			UserOrderInfo userOrderInfo = new UserOrderInfo(_orderDetail.get(0));
			userOrderInfo.addUserOrderDetailInfos(_orderDetail);
			return userOrderInfo;
		}).collect(Collectors.toList());

		serviceResult.setResult(USER, user);
		serviceResult.setResult(LIST_1, userFundingInfos);
		serviceResult.setResult(LIST_2, userOrderInfos);
		return serviceResult;
	}

	@Override
	public ServiceResult editUserInfoService(User user) {
		userDao.updateUserDao(user);
		return ServiceResult.of(true);
	}

	@Override
	public ServiceResult changeUserPasswordService(User user, String newPassword) {
		User userToValidate = userDao.getUserInfoDao(user.getUserId());
		// 用用户id获取邮箱
		userToValidate.setUserId(user.getUserId());
		userToValidate.setUserPassword(user.getUserPassword());
		// 用邮箱和输入的旧密码检验后者是否正确(相当于登录)
		if (userDao.getUserDao(userToValidate) == null) {
			return ServiceResult.of(false).setErrInfo("修改密码失败: 旧密码错误");
		} else {
			if (userToValidate.getUserPassword().equals(newPassword)) {
				return ServiceResult.of(false).setErrInfo("修改密码失败: 新密码不可与旧密码相同");
			} else {
				try {
					userToValidate.setUserPassword(newPassword);
					userDao.updateUserPasswordDao(userToValidate);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					return ServiceResult.of(false).setErrInfo(e.getMessage());
				}
				return ServiceResult.of(true);
			}
		}
	}
}