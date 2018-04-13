package top.starrysea.kql.genertor;

import java.util.ArrayList;
import java.util.List;

import top.starrysea.kql.QuerySqlGenerator;
import top.starrysea.kql.SqlWithParams;

public class GeneratorChain {

	private List<Generator> chain = new ArrayList<>();

	private GeneratorFactory generatorFactory;

	public GeneratorChain(QuerySqlGenerator sqlGenerator) {
		generatorFactory = new GeneratorFactory(sqlGenerator);
	}

	public void addGenerator(Class<?> generatortype) {
		chain.add((Generator) generatorFactory.getGenerator(generatortype));
	}

	public SqlWithParams startGenerator() {
		for (int i = 0; i < chain.size() - 1; i++) {
			chain.get(i).setNextGenerator(chain.get(i + 1));
		}
		return chain.get(0).generate(new SqlWithParams());
	}
}
