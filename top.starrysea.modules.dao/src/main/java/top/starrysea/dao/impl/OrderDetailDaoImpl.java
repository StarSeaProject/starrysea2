package top.starrysea.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import top.starrysea.dao.IOrderDetailDao;
import top.starrysea.kql.clause.OrderByType;
import top.starrysea.kql.clause.SelectClause;
import top.starrysea.kql.clause.WhereType;
import top.starrysea.kql.facede.IntegerSqlResult;
import top.starrysea.kql.facede.KumaSqlDao;
import top.starrysea.kql.facede.ListSqlResult;
import top.starrysea.object.dto.Area;
import top.starrysea.object.dto.City;
import top.starrysea.object.dto.OrderDetail;
import top.starrysea.object.dto.Orders;
import top.starrysea.object.dto.Province;
import top.starrysea.object.dto.Work;
import top.starrysea.object.dto.WorkType;
import top.starrysea.object.view.in.ExportXlsCondition;

@Repository("orderDetailDao")
public class OrderDetailDaoImpl implements IOrderDetailDao {

	@Autowired
	private KumaSqlDao kumaSqlDao;

	@Override
	public List<OrderDetail> getAllOrderDetailDao(OrderDetail orderDetail) {
		kumaSqlDao.selectMode();
		ListSqlResult<OrderDetail> theResult = kumaSqlDao.select("name", "wt").select("work_name", "w")
				.from(OrderDetail.class, "od").innerjoin(Orders.class, "o", "order_id", OrderDetail.class,
						"order_id")
				.innerjoin(WorkType.class, "wt", "work_type_id", OrderDetail.class, "work_type_id")
				.innerjoin(Work.class, "w", "work_id", WorkType.class, "work_id")
				.where("order_id", "od", WhereType.EQUALS, orderDetail.getOrder().getOrderId())
				.where("order_num", "o", WhereType.EQUALS, orderDetail.getOrder().getOrderNum()).endForList(
						(rs, row) -> new OrderDetail.Builder()
								.workType(new WorkType.Builder().name(rs.getString("name"))
										.work(new Work.Builder().workName(rs.getString("work_name")).build()).build())
								.build());
		return theResult.getResult();
	}

	@Override
	public void saveOrderDetailsDao(List<OrderDetail> orderDetails) {
		kumaSqlDao.insertMode();
		kumaSqlDao.insert("id").insert("work_type_id").insert("order_id").table(OrderDetail.class)
				.batchEnd(new BatchPreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						ps.setString(1, orderDetails.get(i).getId());
						ps.setInt(2, orderDetails.get(i).getWorkType().getWorkTypeId());
						ps.setString(3, orderDetails.get(i).getOrder().getOrderId());
					}

					@Override
					public int getBatchSize() {
						return orderDetails.size();
					}
				});
	}

	@Override
	public boolean isOrderDetailExistDao(OrderDetail orderDetail) {
		kumaSqlDao.selectMode();
		IntegerSqlResult theResult = kumaSqlDao.select(SelectClause.COUNT).from(OrderDetail.class)
				.leftjoin(Orders.class, "o", "order_id", OrderDetail.class, "order_id")
				.leftjoin(WorkType.class, "wt", "work_type_id", OrderDetail.class, "work_type_id")
				.leftjoin(Work.class, "w", "work_id", WorkType.class, "work_id")
				.where("order_phone", "o", WhereType.EQUALS, orderDetail.getOrder().getOrderPhone())
				.where("work_id", "w", WhereType.EQUALS, orderDetail.getWorkType().getWork().getWorkId())
				.endForNumber();
		int count = theResult.getResult();
		if (count == 0)
			return false;
		else
			return true;
	}

	@Override
	public List<OrderDetail> getAllOrderDetailForXls(ExportXlsCondition exportXlsCondition) {
		kumaSqlDao.selectMode();
		ListSqlResult<OrderDetail> theResult = kumaSqlDao.select("order_name").select("province_name", "p")
				.select("city_name", "c").select("area_name", "a").select("order_address").select("order_remark")
				.select("order_phone").select("name", "wt").select("work_name", "w").from(Orders.class, "o")
				.leftjoin(Area.class, "a", "area_id", Orders.class, "order_area")
				.leftjoin(City.class, "c", "city_id", Area.class, "city_id")
				.leftjoin(Province.class, "p", "province_id", City.class, "province_id")
				.leftjoin(OrderDetail.class, "od", "order_id", Orders.class, "order_id")
				.leftjoin(WorkType.class, "wt", "work_type_id", OrderDetail.class, "work_type_id")
				.leftjoin(Work.class, "w", "work_id", WorkType.class, "work_id")
				.where("order_status", WhereType.EQUALS, 1)
				.where("order_time", WhereType.GREATER_EQUAL, exportXlsCondition.getStartTime())
				.orderBy("order_time",
						OrderByType.DESC)
				.endForList((rs, row) -> new OrderDetail.Builder()
						.order(new Orders.Builder().orderName(rs.getString("order_name"))
								.orderAddress(rs.getString("order_address")).orderRemark(rs.getString("order_remark"))
								.orderPhone(rs.getString("order_phone"))
								.orderArea(new Area.Builder().areaName(rs.getString("area_name"))
										.city(new City.Builder().cityName(rs.getString("city_name"))
												.province(new Province(null, rs.getString("province_name"))).build())
										.build())
								.build())
						.workType(new WorkType.Builder().name(rs.getString("wt.name"))
								.work(new Work.Builder().workName(rs.getString("w.work_name")).build()).build())
						.build());
		return theResult.getResult();
	}

	@Override
	public List<OrderDetail> getAllResendOrderDetailDao(OrderDetail orderDetail) {
		kumaSqlDao.selectMode();
		ListSqlResult<OrderDetail> theResult = kumaSqlDao.select("name", "wt").select("work_name", "w")
				.select("work_type_id", "wt").select("work_id", "w").select("order_num", "o").select("order_email", "o")
				.from(OrderDetail.class,
						"od")
				.innerjoin(Orders.class, "o", "order_id", OrderDetail.class, "order_id")
				.innerjoin(WorkType.class, "wt", "work_type_id", OrderDetail.class, "work_type_id")
				.innerjoin(Work.class, "w", "work_id", WorkType.class, "work_id")
				.where("order_id", "od", WhereType.EQUALS, orderDetail.getOrder().getOrderId())
				.endForList((rs, row) -> new OrderDetail.Builder()
						.workType(new WorkType.Builder().name(rs.getString("name"))
								.work(new Work.Builder().workId(rs.getInt("work_id"))
										.workName(rs.getString("work_name")).build())
								.workTypeId(rs.getInt("work_type_id")).build())
						.order(new Orders.Builder().orderEMail(rs.getString("order_email"))
								.orderNum(rs.getString("order_num")).build())
						.build());
		return theResult.getResult();
	}

	@Override
	public List<OrderDetail> getOrderDetailByUser(String userId) {
		kumaSqlDao.selectMode();
		ListSqlResult<OrderDetail> result = kumaSqlDao.select("order_time", "o").select("order_expressnum", "o")
				.select("work_cover", "w").select("work_name", "w").select("order_status", "o")
				.select("order_name", "o").select("order_remark", "o").select("order_phone", "o")
				.select("province_name", "p").select("city_name", "c").select("area_name", "a")
				.select("order_address", "o").select("order_id", "o").from(OrderDetail.class, "od")
				.innerjoin(Orders.class, "o", "order_id", OrderDetail.class, "order_id")
				.innerjoin(WorkType.class, "wt", "work_type_id", OrderDetail.class, "work_type_id")
				.innerjoin(Work.class, "w", "work_id", WorkType.class,
						"work_id")
				.innerjoin(Area.class, "a", "area_id", Orders.class,
						"order_area")
				.innerjoin(City.class, "c", "city_id", Area.class,
						"city_id")
				.innerjoin(Province.class, "p", "province_id", City.class, "province_id")
				.where("user_id", "o", WhereType.EQUALS, userId).orderBy("order_time", "o",
						OrderByType.DESC)
				.endForList((rs, row) -> new OrderDetail.Builder()
						.order(new Orders.Builder().orderId(rs.getString("order_id"))
								.orderTime(rs.getLong("order_time")).orderExpressnum(rs.getString("order_expressnum"))
								.orderStatus(rs.getShort("order_status")).orderName(rs.getString("order_name"))
								.orderRemark(rs.getString("order_remark")).orderPhone(rs.getString("order_phone"))
								.orderAddress(rs.getString("order_address"))
								.orderArea(new Area.Builder().areaName(rs.getString("area_name"))
										.city(new City.Builder().cityName(rs.getString("city_name"))
												.province(new Province(null, rs.getString("province_name"))).build())
										.build())
								.build())
						.workType(new WorkType.Builder().work(new Work.Builder().workCover(rs.getString("work_cover"))
								.workName(rs.getString("work_name")).build()).build())
						.build());
		return result.getResult();
	}

}
