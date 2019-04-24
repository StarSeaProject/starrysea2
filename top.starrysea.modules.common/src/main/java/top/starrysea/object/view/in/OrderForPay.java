package top.starrysea.object.view.in;

import top.starrysea.object.dto.Orders;

public class OrderForPay {

	private String orderId;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Orders toDTO() {
		Orders order = new Orders();
		order.setOrderId(orderId);
		return order;
	}

}
