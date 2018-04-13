package top.starrysea.kql.genertor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import top.starrysea.kql.ISqlGenerator;
import top.starrysea.kql.SqlWithParams;
import top.starrysea.kql.clause.SelectClause;

public class SelectGenerator extends Generator {

	public SelectGenerator(ISqlGenerator sqlGenerator) {
		super(sqlGenerator);
	}

	@Override
	public SqlWithParams generate(SqlWithParams sqlWithParams) {
		List<String> select = new ArrayList<>();
		select.add("SELECT");
		select.add(String.join(",", sqlGenerator.getSelectClauses().stream().map(SelectClause::getColunmName)
				.collect(Collectors.toList())));
		if (getNextGenerator() != null) {
			return getNextGenerator().generate(new SqlWithParams(String.join(" ", select) + " ", null));
		} else {
			return new SqlWithParams(String.join(" ", select) + " ", null);
		}
	}

}
