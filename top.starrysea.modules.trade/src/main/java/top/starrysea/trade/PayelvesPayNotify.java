package top.starrysea.trade;

import top.starrysea.object.dto.Funding;
import top.starrysea.object.dto.Orders;

public class PayelvesPayNotify {
	private String openId;
	private String appKey;
	private String sign;
	private String version;
	private String outTradeNo;
	private Integer payType;
	private Integer amount;
	private Integer status;
	private String payUserId;
	private String orderId;
	private String dateTime;
	private String backPara;

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getPayUserId() {
		return payUserId;
	}

	public void setPayUserId(String payUserId) {
		this.payUserId = payUserId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getBackPara() {
		return backPara;
	}

	public void setBackPara(String backPara) {
		this.backPara = backPara;
	}

	public Orders toOrders() {
		return new Orders.Builder().orderId(orderId).userId(payUserId).build();
	}

	public Funding toFunding() {
		return new Funding.Builder().fundingMoney(amount.doubleValue() / 100).userId(payUserId).fundingNum(orderId)
				.build();
	}
}
