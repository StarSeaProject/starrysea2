package top.starrysea.kql.clause;

public class SelectClause {

	private String colunmName;
	public static final SelectClause COUNT = SelectClause.of("COUNT(*)");
	public static final SelectClause MAX = SelectClause.of("MAX(*)");
	public static final SelectClause MIN = SelectClause.of("MIN(*)");

	public static SelectClause of(String colunmName) {
		SelectClause selectClause = new SelectClause();
		selectClause.setColunmName(colunmName);
		return selectClause;
	}

	public static SelectClause of(String colunmName, String alias) {
		SelectClause selectClause = new SelectClause();
		selectClause.setColunmName(alias + "." + colunmName);
		return selectClause;
	}

	public String getColunmName() {
		return colunmName;
	}

	private void setColunmName(String colunmName) {
		this.colunmName = colunmName;
	}
}
