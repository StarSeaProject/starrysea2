package top.starrysea.trade;

import javax.annotation.Generated;

public class PayelvesPayRequest {
	private Integer channel;
	private Double price;
	private Integer payType;
	private String subject;
	private String body;
	private String orderId;
	private String userId;
	private String backPara;
	private String dateTime;

	@Generated("SparkTools")
	private PayelvesPayRequest(Builder builder) {
		this.channel = builder.channel;
		this.price = builder.price;
		this.payType = builder.payType;
		this.subject = builder.subject;
		this.body = builder.body;
		this.orderId = builder.orderId;
		this.userId = builder.userId;
		this.backPara = builder.backPara;
		this.dateTime = builder.dateTime;
	}

	public Integer getChannel() {
		return channel;
	}

	public void setChannel(Integer channel) {
		this.channel = channel;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBackPara() {
		return backPara;
	}

	public void setBackPara(String backPara) {
		this.backPara = backPara;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * Creates builder to build {@link PayelvesPayRequest}.
	 * 
	 * @return created builder
	 */
	@Generated("SparkTools")
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Builder to build {@link PayelvesPayRequest}.
	 */
	@Generated("SparkTools")
	public static final class Builder {
		private Integer channel;
		private Double price;
		private Integer payType;
		private String subject;
		private String body;
		private String orderId;
		private String userId;
		private String backPara;
		private String dateTime;

		private Builder() {
		}

		public Builder withChannel(Integer channel) {
			this.channel = channel;
			return this;
		}

		public Builder withPrice(Double price) {
			this.price = price;
			return this;
		}

		public Builder withPayType(Integer payType) {
			this.payType = payType;
			return this;
		}

		public Builder withSubject(String subject) {
			this.subject = subject;
			return this;
		}

		public Builder withBody(String body) {
			this.body = body;
			return this;
		}

		public Builder withOrderId(String orderId) {
			this.orderId = orderId;
			return this;
		}

		public Builder withUserId(String userId) {
			this.userId = userId;
			return this;
		}

		public Builder withBackPara(String backPara) {
			this.backPara = backPara;
			return this;
		}

		public Builder withDateTime(String dateTime) {
			this.dateTime = dateTime;
			return this;
		}

		public PayelvesPayRequest build() {
			return new PayelvesPayRequest(this);
		}
	}

}
