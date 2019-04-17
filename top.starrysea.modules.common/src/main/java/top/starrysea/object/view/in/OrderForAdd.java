package top.starrysea.object.view.in;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import top.starrysea.object.dto.Area;
import top.starrysea.object.dto.OrderDetail;
import top.starrysea.object.dto.Orders;

public class OrderForAdd {

	@Valid
	@NotEmpty(message = "购物车中没有物品")
	private List<OrderDetailForAddOrder> orderDetails;
	@NotBlank(message = "收货人姓名不能为空")
	@Size(max = 10, message = "姓名长度不能超过10")
	private String orderName;
	@NotNull(message = "地区Id不能为空")
	private Integer orderArea;
	@NotBlank(message = "收货地址不能为空")
	@Size(max = 50, message = "收货地址长度不能超过50")
	private String orderAddress;
	@NotBlank(message = "收件人邮箱不能为空")
	@Email(message = "输入的邮箱地址不是合法的")
	private String orderEmail;
	@Size(max = 50, message = "备注长度不能超过50")
	private String orderRemark;
	@NotBlank(message = "收货人手机不能为空")
	@Size(max = 15, message = "收货人手机长度不能超过15")
	private String orderPhone;
	@NotBlank(message = "token序列不能为空!这是二次提交!")
	private String token;
	/*
	 * @NotNull(message = "请选择支付类型")
	 * 
	 * @Min(value = 1, message = "请选择正确的支付类型")
	 * 
	 * @Max(value = 1, message = "请选择正确的支付类型")
	 */
	private Integer payType;

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public Integer getOrderArea() {
		return orderArea;
	}

	public void setOrderArea(Integer orderArea) {
		this.orderArea = orderArea;
	}

	public String getOrderAddress() {
		return orderAddress;
	}

	public void setOrderAddress(String orderAddress) {
		this.orderAddress = orderAddress;
	}

	public String getOrderEmail() {
		return orderEmail;
	}

	public void setOrderEmail(String orderEmail) {
		this.orderEmail = orderEmail;
	}

	public String getOrderRemark() {
		return orderRemark;
	}

	public void setOrderRemark(String orderRemark) {
		this.orderRemark = orderRemark;
	}

	public String getOrderPhone() {
		return orderPhone;
	}

	public void setOrderPhone(String orderPhone) {
		this.orderPhone = orderPhone;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<OrderDetailForAddOrder> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List<OrderDetailForAddOrder> orderDetails) {
		this.orderDetails = orderDetails;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Orders toDTO() {
		return new Orders.Builder().orderName(orderName).orderArea(new Area.Builder().areaId(orderArea).build())
				.orderAddress(orderAddress).orderEMail(orderEmail).orderRemark(orderRemark).orderPhone(orderPhone)
				.build();
	}

	public List<OrderDetail> toDTOOrderDetail() {
		return orderDetails.stream().map(OrderDetailForAddOrder::toDTO).collect(Collectors.toList());
	}
}
