package top.starrysea.dao;

import java.util.List;

import top.starrysea.object.dto.OrderDetail;
import top.starrysea.object.view.in.ExportXlsCondition;

public interface IOrderDetailDao {

	List<OrderDetail> getAllOrderDetailDao(OrderDetail orderDetail);

	void saveOrderDetailsDao(List<OrderDetail> orderDetails);

	boolean isOrderDetailExistDao(OrderDetail orderDetail);

	List<OrderDetail> getAllOrderDetailForXls(ExportXlsCondition exportXlsCondition);

	List<OrderDetail> getAllResendOrderDetailDao(OrderDetail orderDetail);

	List<OrderDetail> getOrderDetailByUser(String userId);
}
