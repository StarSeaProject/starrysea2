package top.starrysea.kql.genertor;

import static top.starrysea.kql.common.Common.pojo2table;

import java.util.ArrayList;
import java.util.List;

import top.starrysea.kql.ISqlGenerator;
import top.starrysea.kql.SqlWithParams;
import top.starrysea.kql.UpdateSqlGenerator;
import top.starrysea.kql.clause.WhereClause;
import top.starrysea.kql.handler.HandleResult;
import top.starrysea.kql.handler.IWhereHandler;

public class DeleteGenerator extends Generator {

	public DeleteGenerator(ISqlGenerator sqlGenerator) {
		super(sqlGenerator);
	}

	@Override
	public SqlWithParams generate(SqlWithParams sqlWithParams) {
		StringBuilder deleteBuilder = new StringBuilder();
		List<Object> params = new ArrayList<>();
		deleteBuilder.append("DELETE FROM " + pojo2table(sqlGenerator.getTable().getSimpleName()) + " ");
		List<String> whereClauseString = new ArrayList<>();
		if (!sqlGenerator.getWhereClauses().isEmpty()) {
			deleteBuilder.append("WHERE ");
			for (WhereClause whereClause : sqlGenerator.getWhereClauses()) {
				IWhereHandler handler = UpdateSqlGenerator.getWhereHandlerMap().get(whereClause.getWhereType());
				HandleResult result = handler.handleWhereBuffer(whereClause, params);
				whereClauseString.add(result.getBuffer());
				params = result.getPreParams();
			}
			deleteBuilder.append(String.join(" AND ", whereClauseString.toArray(new String[0])));
			return new SqlWithParams(deleteBuilder.toString(), params.toArray());
		}
		return new SqlWithParams(deleteBuilder.toString(), params.toArray());
	}

}
