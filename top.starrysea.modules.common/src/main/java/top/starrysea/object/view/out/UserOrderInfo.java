package top.starrysea.object.view.out;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import top.starrysea.common.Common;
import top.starrysea.object.dto.OrderDetail;
import top.starrysea.object.dto.Orders;

public class UserOrderInfo {

	private String orderTime;
	private String orderExpressnum;
	private List<UserOrderDetailInfo> userOrderDetailInfos;
	private short orderStatus;
	private String orderRemark;
	private String orderName;
	private String provinceName;
	private String cityName;
	private String areaName;
	private String orderAddress;

	public UserOrderInfo(OrderDetail orderDetail) {
		Orders order = orderDetail.getOrder();
		this.orderTime = Common.time2String(new Date(order.getOrderTime()));
		this.orderExpressnum = order.getOrderExpressnum();
		this.orderStatus = order.getOrderStatus();
		this.orderRemark = order.getOrderRemark();
		this.orderName = order.getOrderName();
		this.provinceName = order.getOrderArea().getCity().getProvince().getProvinceName();
		this.cityName = order.getOrderArea().getCity().getCityName();
		this.areaName = order.getOrderArea().getAreaName();
		this.orderAddress = order.getOrderAddress();
		this.userOrderDetailInfos = new ArrayList<>();
	}

	public String getOrderTime() {
		return orderTime;
	}

	public String getOrderExpressnum() {
		return orderExpressnum;
	}

	public short getOrderStatus() {
		return orderStatus;
	}

	public String getOrderRemark() {
		return orderRemark;
	}

	public String getOrderName() {
		return orderName;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public String getAreaName() {
		return areaName;
	}

	public String getOrderAddress() {
		return orderAddress;
	}

	public void addUserOrderDetailInfos(List<OrderDetail> orderDetails) {
		this.userOrderDetailInfos
				.addAll(orderDetails.stream().map(UserOrderDetailInfo::new).collect(Collectors.toList()));
	}

	public List<UserOrderDetailInfo> getUserOrderDetailInfos() {
		return userOrderDetailInfos;
	}

}
