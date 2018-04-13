package top.starrysea.kql.facede;

public class EntitySqlResult<T> extends SqlResult {

	private T result;

	public EntitySqlResult(T result) {
		super(true);
		this.result = result;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

}
