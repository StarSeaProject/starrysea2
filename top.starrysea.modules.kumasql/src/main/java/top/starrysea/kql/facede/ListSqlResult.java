package top.starrysea.kql.facede;

import java.util.List;

public class ListSqlResult<T> extends SqlResult {

	private List<T> result;

	public ListSqlResult(List<T> result) {
		super(true);
		this.result = result;
	}

	public List<T> getResult() {
		return result;
	}

}
