package top.starrysea.object.view.out;

import top.starrysea.object.dto.Work;

public class UserFundingWorkInfo {

	private String workUrl;
	private String workName;

	public UserFundingWorkInfo(Work work) {
		this.workUrl = "/work/" + work.getWorkId();
		this.workName = work.getWorkName();
	}

	public String getWorkUrl() {
		return workUrl;
	}

	public String getWorkName() {
		return workName;
	}

}
