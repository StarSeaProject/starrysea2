package top.starrysea.kql.genertor;

import static top.starrysea.kql.common.Common.pojo2table;

import top.starrysea.kql.ISqlGenerator;
import top.starrysea.kql.SqlWithParams;
import top.starrysea.kql.clause.JoinClause;

public class JoinGenerator extends Generator {

	protected JoinGenerator(ISqlGenerator sqlGenerator) {
		super(sqlGenerator);
	}

	@Override
	public SqlWithParams generate(SqlWithParams sqlWithParams) {
		StringBuilder joinBuilder = new StringBuilder();
		for (JoinClause joinClause : sqlGenerator.getJoinClauses()) {
			joinBuilder.append(joinClause.getJoinType().getValue());
			joinBuilder.append(pojo2table(joinClause.getTarget().getSimpleName()));
			joinBuilder.append(" AS " + joinClause.getAlias());
			joinBuilder.append(" ON " + joinClause.getAlias() + "." + joinClause.getTargetColumn() + " = ");
			String sourceAlias = sqlGenerator.getTableAlias(joinClause.getSource()) != null
					? sqlGenerator.getTableAlias(joinClause.getSource())
					: pojo2table(joinClause.getSource().getSimpleName());
			joinBuilder.append(sourceAlias + "." + joinClause.getSourceColumn() + " ");
		}
		if (getNextGenerator() != null) {
			return getNextGenerator().generate(
					new SqlWithParams(sqlWithParams.getSql() + joinBuilder.toString(), sqlWithParams.getParams()));
		} else {
			return new SqlWithParams(sqlWithParams.getSql() + joinBuilder.toString(), sqlWithParams.getParams());
		}
	}

}
