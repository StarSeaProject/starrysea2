package top.starrysea.object.view.in;

import javax.validation.constraints.NotBlank;

public class OrderDetailForRemoveOrder {

	@NotBlank(message = "作品详细id不能为空")
	private String orderDetailId;

	public String getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(String orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

}
