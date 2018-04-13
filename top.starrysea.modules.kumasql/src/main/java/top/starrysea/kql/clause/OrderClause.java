package top.starrysea.kql.clause;

public class OrderClause {

	private String columnName;

	public static OrderClause of(String columnName) {
		OrderClause orderClause = new OrderClause();
		orderClause.setColumnName(columnName);
		return orderClause;
	}

	public static OrderClause of(String columnName, OrderByType orderByType) {
		OrderClause orderClause = new OrderClause();
		orderClause.setColumnName(columnName + " " + orderByType.getType());
		return orderClause;
	}

	public static OrderClause of(String columnName, String alias) {
		OrderClause orderClause = new OrderClause();
		orderClause.setColumnName(alias + "." + columnName);
		return orderClause;
	}

	public static OrderClause of(String columnName, String alias, OrderByType orderByType) {
		OrderClause orderClause = new OrderClause();
		orderClause.setColumnName(alias + "." + columnName + " " + orderByType.getType());
		return orderClause;
	}

	public String getColumnName() {
		return columnName;
	}

	private void setColumnName(String colunmName) {
		this.columnName = colunmName;
	}
}
