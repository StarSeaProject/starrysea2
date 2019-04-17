package top.starrysea.object.view.in;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class FundingForParticipate {
	@NotNull(message = "活动id不能为空")
	private Integer activityId;
	@NotNull(message = "众筹金额不能为空")
	private Double fundingMoney;
	@NotEmpty(message = "众筹人留言不能为空!")
	@Length(max = 50, message = "众筹人留言长度不能超过50")
	private String fundingMessage;
	@NotNull(message = "请选择支付类型")
	@Min(value = 1, message = "请选择正确的支付类型")
	@Max(value = 1, message = "请选择正确的支付类型")
	private Integer payType;

	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	public Double getFundingMoney() {
		return fundingMoney;
	}

	public void setFundingMoney(Double fundingMoney) {
		this.fundingMoney = fundingMoney;
	}

	public String getFundingMessage() {
		return fundingMessage;
	}

	public void setFundingMessage(String fundingMessage) {
		this.fundingMessage = fundingMessage;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}
}
