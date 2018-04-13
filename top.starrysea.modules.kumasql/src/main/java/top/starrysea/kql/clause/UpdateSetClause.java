package top.starrysea.kql.clause;

public class UpdateSetClause {

	private String columnName;
	private UpdateSetType updateSetType;
	private Object value;

	public static UpdateSetClause of(String columnName, UpdateSetType updateSetType) {
		UpdateSetClause updateSetClause = new UpdateSetClause();
		updateSetClause.setColumnName(columnName);
		updateSetClause.setUpdateSetType(updateSetType);
		return updateSetClause;
	}

	public static UpdateSetClause of(String columnName, UpdateSetType updateSetType, Object value) {
		UpdateSetClause updateSetClause = new UpdateSetClause();
		updateSetClause.setColumnName(columnName);
		updateSetClause.setUpdateSetType(updateSetType);
		updateSetClause.setValue(value);
		return updateSetClause;
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

	public UpdateSetType getUpdateSetType() {
		return updateSetType;
	}

	private void setUpdateSetType(UpdateSetType updateSetType) {
		this.updateSetType = updateSetType;
	}

}
