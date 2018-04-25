package top.starrysea.dao;

import java.util.List;

import top.starrysea.object.dto.OrderDetail;

public interface IOrderDetailDao {

	List<OrderDetail> getAllOrderDetailDao(OrderDetail orderDetail);
	
	void saveOrderDetailsDao(List<OrderDetail> orderDetails);
	
	boolean isOrderDetailExistDao(OrderDetail orderDetail);
	
	List<OrderDetail> getAllOrderDetailForXls();
	
	List<OrderDetail> getAllResendOrderDetailDao(OrderDetail orderDetail);
}
