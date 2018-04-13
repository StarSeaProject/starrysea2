package top.starrysea.kql.clause;

public class InsertClause {

	private String columnName;
	private Object value;

	public static InsertClause of(String columnName) {
		InsertClause insertClause = new InsertClause();
		insertClause.setColumnName(columnName);
		return insertClause;
	}
	
	public static InsertClause of(String columnName, Object value) {
		InsertClause insertClause = new InsertClause();
		insertClause.setColumnName(columnName);
		insertClause.setValue(value);
		return insertClause;
	}

	public String getColumnName() {
		return columnName;
	}

	private void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Object getValue() {
		return value;
	}

	private void setValue(Object value) {
		this.value = value;
	}
}
