package top.starrysea.object.dto;

import top.starrysea.kql.entity.Entity;
import top.starrysea.kql.entity.IBuilder;

public class Postage implements Entity {

	private int postageId;
	private Province province;
	private int postageMoney;
	
	public Postage(Builder builder) {
		this.postageId=builder.postageId;
		this.province=builder.province;
		this.postageMoney=builder.postageMoney;
	}
	
	public static class Builder implements IBuilder<Postage> {

		private int postageId;
		private Province province;
		private int postageMoney;

		public Builder postageId(int postageId) {
			this.postageId = postageId;
			return this;
		}

		public Builder province(int provinceId) {
			this.province = new Province(provinceId);
			return this;
		}

		public Builder postageMoney(int postageMoney) {
			this.postageMoney = postageMoney;
			return this;
		}

		@Override
		public Postage build() {
			return new Postage(this);
		}
	}

	public int getPostageId() {
		return postageId;
	}

	public void setPostageId(int postageId) {
		this.postageId = postageId;
	}

	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	public int getPostageMoney() {
		return postageMoney;
	}

	public void setPostageMoney(int postageMoney) {
		this.postageMoney = postageMoney;
	}
	
}
