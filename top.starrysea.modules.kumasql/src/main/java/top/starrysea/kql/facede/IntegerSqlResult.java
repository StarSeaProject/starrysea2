package top.starrysea.kql.facede;

public class IntegerSqlResult extends SqlResult {

	private int result;
	
	public IntegerSqlResult(int result) {
		super(true);
		this.result=result;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}
}
