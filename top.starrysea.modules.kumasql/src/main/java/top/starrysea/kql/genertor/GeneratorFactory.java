package top.starrysea.kql.genertor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import top.starrysea.kql.ISqlGenerator;

public class GeneratorFactory {

	private Map<Class<?>, Generator> generators = new HashMap<>();

	private ISqlGenerator sqlGenerator;

	public GeneratorFactory(ISqlGenerator sqlGenerator) {
		this.sqlGenerator = sqlGenerator;
		initGenerators();
	}

	private void initGenerators() {
		generators.put(SelectGenerator.class, new SelectGenerator(sqlGenerator));
		generators.put(TableGenerator.class, new TableGenerator(sqlGenerator));
		generators.put(WhereGenerator.class, new WhereGenerator(sqlGenerator));
		generators.put(OrderByGenerator.class, new OrderByGenerator(sqlGenerator));
		generators.put(LimitGenerator.class, new LimitGenerator(sqlGenerator));
		generators.put(JoinGenerator.class, new JoinGenerator(sqlGenerator));
		generators.put(InsertGenerator.class, new InsertGenerator(sqlGenerator));
		generators.put(UpdateGenerator.class, new UpdateGenerator(sqlGenerator));
		generators.put(DeleteGenerator.class, new DeleteGenerator(sqlGenerator));
		generators = Collections.unmodifiableMap(generators);
	}

	public <T> T getGenerator(Class<T> type) {
		return type.cast(generators.get(type));
	}
}
