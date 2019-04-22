package top.starrysea.object.view.in;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import top.starrysea.object.dto.Activity;
import top.starrysea.object.dto.Funding;

@ApiModel(value = "参与众筹对象", description = "参与众筹对象")
public class FundingForParticipate {
	
	@ApiModelProperty(value="活动id",name="activityId")
	@NotNull(message = "活动id不能为空")
	private Integer activityId;
	
	@ApiModelProperty(value="众筹金额",name="fundingMoney")
	@NotNull(message = "众筹金额不能为空")
	private Double fundingMoney;
	
	@ApiModelProperty(value="众筹人留言",name="fundingMessage")
	@NotEmpty(message = "众筹人留言不能为空!")
	@Length(max = 50, message = "众筹人留言长度不能超过50")
	private String fundingMessage;

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

	public Funding toDTO() {
		return new Funding.Builder().activity(new Activity.Builder().activityId(activityId).build())
				.fundingMoney(fundingMoney).fundingMessage(fundingMessage).build();
	}
}
