package top.starrysea.kql.genertor;

import top.starrysea.kql.ISqlGenerator;
import top.starrysea.kql.SqlWithParams;
import top.starrysea.kql.clause.InsertClause;

import static top.starrysea.kql.common.Common.pojo2table;

import java.util.ArrayList;
import java.util.List;

public class InsertGenerator extends Generator {

	public InsertGenerator(ISqlGenerator sqlGenerator) {
		super(sqlGenerator);
	}

	@Override
	public SqlWithParams generate(SqlWithParams sqlWithParams) {
		StringBuilder insertBuilder = new StringBuilder();
		StringBuilder valueBuilder = new StringBuilder();
		List<Object> params = new ArrayList<>();
		insertBuilder.append("INSERT INTO ");
		insertBuilder.append(pojo2table(sqlGenerator.getTable().getSimpleName()) + "(");
		valueBuilder.append("VALUES(");
		for (InsertClause insertClause : sqlGenerator.getInsertClauses()) {
			insertBuilder.append(insertClause.getColumnName() + ",");
			valueBuilder.append("?,");
			params.add(insertClause.getValue());
		}
		insertBuilder.deleteCharAt(insertBuilder.length() - 1);
		insertBuilder.append(") ");
		valueBuilder.deleteCharAt(valueBuilder.length() - 1);
		valueBuilder.append(")");
		return new SqlWithParams(insertBuilder.append(valueBuilder).toString(), params.toArray());
	}

}
