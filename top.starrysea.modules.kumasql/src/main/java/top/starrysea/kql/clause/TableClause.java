package top.starrysea.kql.clause;

import top.starrysea.kql.entity.Entity;

public class TableClause {
	private Class<? extends Entity> table;
	private String alias;

	public static TableClause of(Class<? extends Entity> table) {
		TableClause tableClause = new TableClause();
		tableClause.setTable(table);
		return tableClause;
	}

	public static TableClause of(Class<? extends Entity> table, String alias) {
		TableClause tableClause = new TableClause();
		tableClause.setTable(table);
		tableClause.setAlias(alias);
		return tableClause;
	}

	public Class<? extends Entity> getTable() {
		return table;
	}

	private void setTable(Class<? extends Entity> table) {
		this.table = table;
	}

	public String getAlias() {
		return alias;
	}

	private void setAlias(String alias) {
		this.alias = alias;
	}
}
