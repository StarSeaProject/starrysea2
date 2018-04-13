package top.starrysea.kql;

import java.util.ArrayList;
import java.util.List;

import top.starrysea.kql.clause.InsertClause;
import top.starrysea.kql.entity.Entity;
import top.starrysea.kql.entity.IBuilder;

public class InsertSqlGenerator extends NonQuerySqlGenerator {

	private Class<? extends Entity> table;
	private List<InsertClause> insertClauses;

	private InsertSqlGenerator(Builder builder) {
		this.table = builder.table;
		this.insertClauses = builder.insertClauses;
	}

	public static class Builder implements IBuilder<InsertSqlGenerator> {

		private Class<? extends Entity> table;
		private List<InsertClause> insertClauses;

		public Builder() {
			insertClauses = new ArrayList<>();
		}

		public Builder into(Class<? extends Entity> table) {
			this.table = table;
			return this;
		}

		public Builder insert(String columnName) {
			InsertClause insertClause = InsertClause.of(columnName);
			insertClauses.add(insertClause);
			return this;
		}

		public Builder insert(String columnName, Object value) {
			InsertClause insertClause = InsertClause.of(columnName, value);
			insertClauses.add(insertClause);
			return this;
		}

		@Override
		public InsertSqlGenerator build() {
			if (table == null)
				throw new IllegalArgumentException("当前没有设置要操作哪张表!");
			if (insertClauses.isEmpty())
				throw new IllegalArgumentException("当前没有设置任何列!");
			return new InsertSqlGenerator(this);
		}

	}

	public List<InsertClause> getInsertClauses() {
		return insertClauses;
	}

	public Class<? extends Entity> getTable() {
		return table;
	}

	@Override
	protected ISqlGenerator getType() {
		return this;
	}

}
