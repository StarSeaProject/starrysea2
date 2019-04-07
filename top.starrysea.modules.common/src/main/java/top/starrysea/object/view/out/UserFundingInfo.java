package top.starrysea.object.view.out;

import java.util.List;

import top.starrysea.object.dto.Funding;

public class UserFundingInfo {

	private String activityCover;
	private String activityName;
	private String fundingTime;
	private String fundingMessage;
	private List<UserFundingWorkInfo> works;

	public UserFundingInfo(Funding funding, List<UserFundingWorkInfo> works) {
		this.activityCover = funding.getActivity().getActivityCover();
		this.activityName = funding.getActivity().getActivityName();
		this.fundingTime = funding.getFundingTime();
		this.works = works;
		this.fundingMessage = funding.getFundingMessage();
	}

	public String getActivityCover() {
		return activityCover;
	}

	public String getActivityName() {
		return activityName;
	}

	public String getFundingTime() {
		return fundingTime;
	}

	public List<UserFundingWorkInfo> getWorks() {
		return works;
	}

	public String getFundingMessage() {
		return fundingMessage;
	}

}
