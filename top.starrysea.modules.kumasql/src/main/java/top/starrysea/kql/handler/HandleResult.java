package top.starrysea.kql.handler;

import java.util.List;

public class HandleResult {

	private String buffer;
	private List<Object> preParams;

	public HandleResult(String where, List<Object> preParams) {
		this.buffer = where;
		this.preParams = preParams;
	}

	public String getBuffer() {
		return buffer;
	}

	public List<Object> getPreParams() {
		return preParams;
	}

}
