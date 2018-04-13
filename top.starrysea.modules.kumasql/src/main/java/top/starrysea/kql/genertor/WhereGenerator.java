package top.starrysea.kql.genertor;

import static top.starrysea.kql.common.Common.isNotNull;

import java.util.ArrayList;
import java.util.List;

import top.starrysea.kql.ISqlGenerator;
import top.starrysea.kql.QuerySqlGenerator;
import top.starrysea.kql.SqlWithParams;
import top.starrysea.kql.clause.WhereClause;
import top.starrysea.kql.handler.HandleResult;
import top.starrysea.kql.handler.IWhereHandler;

public class WhereGenerator extends Generator {

	public WhereGenerator(ISqlGenerator sqlGenerator) {
		super(sqlGenerator);
	}

	@Override
	public SqlWithParams generate(SqlWithParams sqlWithParams) {
		StringBuilder sqlBuffer = new StringBuilder(sqlWithParams.getSql());
		List<WhereClause> whereClauses = sqlGenerator.getWhereClauses();
		StringBuilder whereBuffer = new StringBuilder();
		List<Object> params = new ArrayList<>();

		List<String> whereClauseString = new ArrayList<>();
		for (WhereClause where : whereClauses) {
			if (isNotNull(where.getValue()) || isNotNull(where.getValueList())) {
				IWhereHandler handler = QuerySqlGenerator.getHandlerMap().get(where.getWhereType());
				HandleResult result = handler.handleWhereBuffer(where, params);
				whereClauseString.add(result.getBuffer());
				params = result.getPreParams();
			}
		}
		if (!whereClauseString.isEmpty()) {
			whereBuffer.append("WHERE ");
			whereBuffer.append(String.join(" AND ", whereClauseString.toArray(new String[0]))).append(" ");
		}

		if (getNextGenerator() != null) {
			return getNextGenerator()
					.generate(new SqlWithParams(sqlBuffer.append(whereBuffer).toString(), params.toArray()));
		} else {
			return new SqlWithParams(sqlBuffer.append(whereBuffer).toString(), params.toArray());
		}
	}
}
