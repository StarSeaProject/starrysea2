package top.starrysea.object.view.out;

import top.starrysea.object.dto.OrderDetail;

public class UserOrderDetailInfo {

	private String workCover;
	private String workName;

	public UserOrderDetailInfo(OrderDetail orderDetail) {
		this.workCover = orderDetail.getWorkType().getWork().getWorkCover();
		this.workName = orderDetail.getWorkType().getWork().getWorkName();
	}

	public String getWorkCover() {
		return workCover;
	}

	public String getWorkName() {
		return workName;
	}

}
