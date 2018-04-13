package top.starrysea.kql.genertor;

import top.starrysea.kql.ISqlGenerator;
import top.starrysea.kql.SqlWithParams;

public class LimitGenerator extends Generator {

	public LimitGenerator(ISqlGenerator sqlGenerator) {
		super(sqlGenerator);
	}

	@Override
	public SqlWithParams generate(SqlWithParams sqlWithParams) {
		if (sqlGenerator.getLimitClause() == null) {
			return sqlWithParams;
		}
		if (getNextGenerator() != null) {
			return getNextGenerator().generate(new SqlWithParams(
					sqlWithParams.getSql() + "LIMIT " + sqlGenerator.getLimitClause().getLimitClause(),
					sqlWithParams.getParams()));
		} else {
			return new SqlWithParams(sqlWithParams.getSql() + "LIMIT " + sqlGenerator.getLimitClause().getLimitClause(),
					sqlWithParams.getParams());
		}
	}

}
