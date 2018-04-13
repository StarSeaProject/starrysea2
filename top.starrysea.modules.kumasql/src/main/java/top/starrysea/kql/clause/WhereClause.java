package top.starrysea.kql.clause;

import java.util.List;

public class WhereClause {

	private String columnName;
	private WhereType whereType;
	private Object value;
	private List<Object> valueList;

	private WhereClause() {
	}

	public static WhereClause of(String columnName, WhereType whereType, Object value) {
		WhereClause whereClause = new WhereClause();
		whereClause.setColumnName(columnName);
		whereClause.setWhereType(whereType);
		if (whereType == WhereType.IN)
			throw new IllegalArgumentException("该方法不支持IN类型的where子句,请使用of(String,WhereType,List<Object>)的版本");
		whereClause.setValue(value);
		return whereClause;
	}

	public static WhereClause of(String columnName, WhereType whereType, List<Object> valueList) {
		WhereClause whereClause = new WhereClause();
		whereClause.setColumnName(columnName);
		whereClause.setWhereType(whereType);
		whereClause.setValueList(valueList);
		return whereClause;
	}

	public static WhereClause of(String columnName, String alias, WhereType whereType, Object value) {
		WhereClause whereClause = new WhereClause();
		whereClause.setColumnName(alias + "." + columnName);
		whereClause.setWhereType(whereType);
		whereClause.setValue(value);
		return whereClause;
	}

	public String getColumnName() {
		return columnName;
	}

	private void setColumnName(String colunmName) {
		this.columnName = colunmName;
	}

	public WhereType getWhereType() {
		return whereType;
	}

	private void setWhereType(WhereType whereType) {
		this.whereType = whereType;
	}

	public Object getValue() {
		return value;
	}

	private void setValue(Object value) {
		this.value = value;
	}

	public List<Object> getValueList() {
		return valueList;
	}

	private void setValueList(List<Object> valueList) {
		this.valueList = valueList;
	}

}
