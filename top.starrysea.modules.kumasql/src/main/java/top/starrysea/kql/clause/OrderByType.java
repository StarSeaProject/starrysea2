package top.starrysea.kql.clause;

public enum OrderByType {

	ASC("ASC"), DESC("DESC");

	private String orderByTypeInfo;

	private OrderByType(String orderByType) {
		this.orderByTypeInfo = orderByType;
	}

	public String getType() {
		return this.orderByTypeInfo;
	}
}
