package top.starrysea.object.dto;

import top.starrysea.kql.entity.Entity;
import top.starrysea.kql.entity.IBuilder;
import top.starrysea.object.view.out.WorkForAll;
import top.starrysea.object.view.out.WorkForOne;

public class Work implements Entity {

	private Integer workId;
	private String workName;
	private String workUploadTime;
	private String workPdfpath;
	private String workPdfPassword;
	private String workCover;
	private String workSummary;
	private Integer workClick;
	private Activity activity;

	private Work(Builder builder) {
		this.workId = builder.workId;
		this.workName = builder.workName;
		this.workUploadTime = builder.workUploadTime;
		this.workPdfpath = builder.workPdfpath;
		this.workPdfPassword = builder.workPdfPassword;
		this.workCover = builder.workCover;
		this.workSummary = builder.workSummary;
		this.workClick = builder.workClick;
		this.activity = builder.activity;
	}

	public static class Builder implements IBuilder<Work> {

		private Integer workId;
		private String workName;
		private String workUploadTime;
		private String workPdfpath;
		private String workPdfPassword;
		private String workCover;
		private String workSummary;
		private Integer workClick;
		private Activity activity;

		public Builder workId(Integer workId) {
			this.workId = workId;
			return this;
		}

		public Builder workName(String workName) {
			this.workName = workName;
			return this;
		}

		public Builder workUploadTime(String workUploadTime) {
			this.workUploadTime = workUploadTime;
			return this;
		}

		public Builder workPdfpath(String workPdfpath) {
			this.workPdfpath = workPdfpath;
			return this;
		}

		public Builder workCover(String workCover) {
			this.workCover = workCover;
			return this;
		}

		public Builder workSummary(String workSummary) {
			this.workSummary = workSummary;
			return this;
		}

		public Builder workClick(Integer workClick) {
			this.workClick = workClick;
			return this;
		}

		public Builder workPdfPassword(String workPdfPassword) {
			this.workPdfPassword = workPdfPassword;
			return this;
		}

		public Builder activityId(int activityId) {
			this.activity = new Activity.Builder().activityId(activityId).build();
			return this;
		}

		@Override
		public Work build() {
			return new Work(this);
		}

	}

	public Integer getWorkId() {
		return workId;
	}

	public void setWorkId(Integer workId) {
		this.workId = workId;
	}

	public String getWorkName() {
		return workName;
	}

	public void setWorkName(String workName) {
		this.workName = workName;
	}

	public String getWorkUploadTime() {
		return workUploadTime;
	}

	public void setWorkUploadTime(String workUploadTime) {
		this.workUploadTime = workUploadTime;
	}

	public String getWorkPdfpath() {
		return workPdfpath;
	}

	public void setWorkPdfpath(String workPdfpath) {
		this.workPdfpath = workPdfpath;
	}

	public String getWorkCover() {
		return workCover;
	}

	public void setWorkCover(String workCover) {
		this.workCover = workCover;
	}

	public String getWorkSummary() {
		return workSummary;
	}

	public void setWorkSummary(String workSummary) {
		this.workSummary = workSummary;
	}

	public Integer getWorkClick() {
		return workClick;
	}

	public void setWorkClick(Integer workClick) {
		this.workClick = workClick;
	}

	public String getWorkPdfPassword() {
		return workPdfPassword;
	}

	public void setWorkPdfPassword(String workPdfPassword) {
		this.workPdfPassword = workPdfPassword;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public WorkForAll toVoForAll() {
		return new WorkForAll(workId, workName, workCover, workSummary);
	}

	public WorkForOne toVoForOne() {
		return new WorkForOne(workName, workUploadTime, workPdfpath, workClick, workCover, workPdfPassword);
	}
}
