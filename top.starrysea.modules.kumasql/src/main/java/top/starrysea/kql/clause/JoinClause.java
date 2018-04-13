package top.starrysea.kql.clause;

import top.starrysea.kql.entity.Entity;

public class JoinClause {

	private JoinType joinType;
	private Class<? extends Entity> target;
	private String alias;
	private String targetColumn;
	private Class<? extends Entity> source;
	private String sourceColumn;

	public static JoinClause of(JoinType joinType, Class<? extends Entity> target, String alias, String targetColumn,
			Class<? extends Entity> source, String sourceColumn) {
		JoinClause joinClause = new JoinClause();
		joinClause.setJoinType(joinType);
		joinClause.setTarget(target);
		joinClause.setAlias(alias);
		joinClause.setTargetColumn(targetColumn);
		joinClause.setSource(source);
		joinClause.setSourceColumn(sourceColumn);
		return joinClause;
	}

	public JoinType getJoinType() {
		return joinType;
	}

	private void setJoinType(JoinType joinType) {
		this.joinType = joinType;
	}

	public Class<? extends Entity> getTarget() {
		return target;
	}

	private void setTarget(Class<? extends Entity> target) {
		this.target = target;
	}

	public String getAlias() {
		return alias;
	}

	private void setAlias(String alias) {
		this.alias = alias;
	}

	public String getTargetColumn() {
		return targetColumn;
	}

	private void setTargetColumn(String targetColumn) {
		this.targetColumn = targetColumn;
	}

	public Class<? extends Entity> getSource() {
		return source;
	}

	private void setSource(Class<? extends Entity> source) {
		this.source = source;
	}

	public String getSourceColumn() {
		return sourceColumn;
	}

	private void setSourceColumn(String sourceColumn) {
		this.sourceColumn = sourceColumn;
	}
}
