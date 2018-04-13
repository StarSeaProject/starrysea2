package top.starrysea.kql.facede;

public class SqlResult {

	private boolean successed;
	private String errInfo;

	public SqlResult(boolean successed) {
		this.successed = successed;
	}

	public SqlResult(boolean successed, String errInfo) {
		this.successed = successed;
		this.errInfo = errInfo;
	}

	public boolean isSuccessed() {
		return successed;
	}

	public void setSuccessed(boolean successed) {
		this.successed = successed;
	}

	public String getErrInfo() {
		return errInfo;
	}

	public void setErrInfo(String errInfo) {
		this.errInfo = errInfo;
	}
}
