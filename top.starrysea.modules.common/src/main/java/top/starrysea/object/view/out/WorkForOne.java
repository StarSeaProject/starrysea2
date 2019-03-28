package top.starrysea.object.view.out;

public class WorkForOne {

	private String workName;
	private String workUploadTime;
	private String workPdfpath;
	private String workPdfPassword;
	private Integer workClick;
	private String workCover;

	public WorkForOne(String workName, String workUploadTime, String workPdfpath, Integer workClick, String workCover,
			String workPdfPassword) {
		this.workName = workName;
		this.workUploadTime = workUploadTime;
		this.workPdfpath = workPdfpath;
		this.workPdfPassword = workPdfPassword;
		this.workClick = workClick;
		this.workCover = workCover;
	}

	public String getWorkName() {
		return workName;
	}

	public String getWorkUploadTime() {
		return workUploadTime;
	}

	public String getWorkPdfpath() {
		return workPdfpath;
	}

	public Integer getWorkClick() {
		return workClick;
	}

	public String getWorkCover() {
		return workCover;
	}

	public String getWorkPdfPassword() {
		return workPdfPassword;
	}

}
