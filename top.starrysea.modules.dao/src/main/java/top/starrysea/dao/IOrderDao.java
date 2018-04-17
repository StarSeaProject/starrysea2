package top.starrysea.dao;

import java.util.List;

import top.starrysea.common.Condition;
import top.starrysea.object.dto.Orders;

public interface IOrderDao {

	List<Orders> getAllOrderDao(Condition condition, Orders order);

	int getOrderCountDao(Condition condition, Orders order);

	Orders getOrderDao(Orders order);

	Orders saveOrderDao(Orders order);

	void updateOrderDao(Orders order);

	void deleteOrderDao(Orders order);

	void updateAddressDao(Orders order);

}
