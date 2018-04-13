package top.starrysea.kql;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import top.starrysea.kql.clause.UpdateSetClause;
import top.starrysea.kql.clause.UpdateSetType;
import top.starrysea.kql.clause.WhereClause;
import top.starrysea.kql.clause.WhereType;
import top.starrysea.kql.entity.Entity;
import top.starrysea.kql.entity.IBuilder;
import top.starrysea.kql.handler.WhereHandlers;
import top.starrysea.kql.handler.IUpdateSetHandler;
import top.starrysea.kql.handler.IWhereHandler;
import top.starrysea.kql.handler.UpdateSetHandlers;

public class UpdateSqlGenerator extends NonQuerySqlGenerator {

	private Class<? extends Entity> table;
	private List<UpdateSetClause> updateSetClauses;
	private List<WhereClause> whereClauses;
	private static Map<WhereType, IWhereHandler> whereHandlerMap = new EnumMap<>(WhereType.class);
	private static Map<UpdateSetType, IUpdateSetHandler> updateSetHandlerMap = new EnumMap<>(UpdateSetType.class);

	static {
		whereHandlerMap.put(WhereType.EQUALS, WhereHandlers.equalsHandler);
		whereHandlerMap.put(WhereType.FRONT_FUZZY, WhereHandlers.frontFuzzyHandler);
		whereHandlerMap.put(WhereType.BACK_FUZZY, WhereHandlers.backFuzzyHandler);
		whereHandlerMap.put(WhereType.FUZZY, WhereHandlers.fuzzyHandler);
		whereHandlerMap.put(WhereType.GREATER, WhereHandlers.greaterHandler);
		whereHandlerMap.put(WhereType.GREATER_EQUAL, WhereHandlers.greaterEqualHandler);
		whereHandlerMap.put(WhereType.LESS, WhereHandlers.lessHandler);
		whereHandlerMap.put(WhereType.LESS_EQUAL, WhereHandlers.lessEqualHandler);
		whereHandlerMap.put(WhereType.IN, WhereHandlers.inHandler);

		updateSetHandlerMap.put(UpdateSetType.ASSIGN, UpdateSetHandlers.assignHandler);
		updateSetHandlerMap.put(UpdateSetType.ADD, UpdateSetHandlers.addHandler);
		updateSetHandlerMap.put(UpdateSetType.REDUCE, UpdateSetHandlers.reduceHandler);
	}

	private UpdateSqlGenerator(Builder builder) {
		this.table = builder.table;
		this.updateSetClauses = builder.updateSetClauses;
		this.whereClauses = builder.whereClauses;
	}

	public static class Builder implements IBuilder<UpdateSqlGenerator> {

		private Class<? extends Entity> table;
		private List<UpdateSetClause> updateSetClauses;
		private List<WhereClause> whereClauses;

		public Builder() {
			updateSetClauses = new ArrayList<>();
			whereClauses = new ArrayList<>();
		}

		public Builder table(Class<? extends Entity> table) {
			this.table = table;
			return this;
		}

		public Builder update(String columnName, UpdateSetType updateSetType) {
			UpdateSetClause updateSetClause = UpdateSetClause.of(columnName, updateSetType);
			updateSetClauses.add(updateSetClause);
			return this;
		}

		public Builder update(String columnName, UpdateSetType updateSetType, Object value) {
			UpdateSetClause updateSetClause = UpdateSetClause.of(columnName, updateSetType, value);
			updateSetClauses.add(updateSetClause);
			return this;
		}

		public Builder where(String columnName, WhereType whereType, Object value) {
			WhereClause updateWhereClause = WhereClause.of(columnName, whereType, value);
			whereClauses.add(updateWhereClause);
			return this;
		}

		public Builder where(String columnName, WhereType whereType, List<Object> valueList) {
			WhereClause updateWhereClause = WhereClause.of(columnName, whereType, valueList);
			whereClauses.add(updateWhereClause);
			return this;
		}

		@Override
		public UpdateSqlGenerator build() {
			if (table == null)
				throw new IllegalArgumentException("操作的表不能为空!");
			return new UpdateSqlGenerator(this);
		}

	}

	public Class<? extends Entity> getTable() {
		return table;
	}

	public List<UpdateSetClause> getUpdateSetClauses() {
		return updateSetClauses;
	}

	public List<WhereClause> getWhereClauses() {
		return whereClauses;
	}

	public static Map<WhereType, IWhereHandler> getWhereHandlerMap() {
		return whereHandlerMap;
	}

	public static Map<UpdateSetType, IUpdateSetHandler> getUpdateSetHandlerMap() {
		return updateSetHandlerMap;
	}

	@Override
	protected ISqlGenerator getType() {
		return this;
	}

}
