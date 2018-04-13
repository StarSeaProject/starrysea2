package top.starrysea.kql.genertor;

import top.starrysea.kql.ISqlGenerator;
import top.starrysea.kql.SqlGeneratorAdapter;
import top.starrysea.kql.SqlWithParams;

public abstract class Generator {

	public abstract SqlWithParams generate(SqlWithParams sqlWithParams);

	protected SqlGeneratorAdapter sqlGenerator;

	protected Generator nextGenerator = null;

	protected Generator(ISqlGenerator sqlGenerator) {
		this.sqlGenerator = new SqlGeneratorAdapter(sqlGenerator);
	}

	public Generator getNextGenerator() {
		return nextGenerator;
	}

	public Generator setNextGenerator(Generator nextGenerator) {
		this.nextGenerator = nextGenerator;
		return this;
	}

}
