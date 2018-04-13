package top.starrysea.kql;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import top.starrysea.kql.SqlWithParams;
import top.starrysea.kql.clause.JoinClause;
import top.starrysea.kql.clause.JoinType;
import top.starrysea.kql.clause.LimitClause;
import top.starrysea.kql.clause.OrderByType;
import top.starrysea.kql.clause.OrderClause;
import top.starrysea.kql.clause.SelectClause;
import top.starrysea.kql.clause.TableClause;
import top.starrysea.kql.clause.WhereClause;
import top.starrysea.kql.clause.WhereType;
import top.starrysea.kql.entity.Entity;
import top.starrysea.kql.entity.IBuilder;
import top.starrysea.kql.genertor.GeneratorChain;
import top.starrysea.kql.genertor.JoinGenerator;
import top.starrysea.kql.genertor.LimitGenerator;
import top.starrysea.kql.genertor.OrderByGenerator;
import top.starrysea.kql.genertor.SelectGenerator;
import top.starrysea.kql.genertor.TableGenerator;
import top.starrysea.kql.genertor.WhereGenerator;
import top.starrysea.kql.handler.WhereHandlers;
import top.starrysea.kql.handler.IWhereHandler;

public class QuerySqlGenerator implements ISqlGenerator {

	private List<SelectClause> selectClauses;
	private List<TableClause> tableClauses;
	private List<WhereClause> whereClauses;
	private List<OrderClause> orderByClauses;
	private LimitClause limitClause;
	private static Map<WhereType, IWhereHandler> handlerMap = new EnumMap<>(WhereType.class);
	private List<JoinClause> joinClauses;
	private Map<Class<? extends Entity>, String> tableAlias;

	static {
		handlerMap.put(WhereType.EQUALS, WhereHandlers.equalsHandler);
		handlerMap.put(WhereType.FRONT_FUZZY, WhereHandlers.frontFuzzyHandler);
		handlerMap.put(WhereType.BACK_FUZZY, WhereHandlers.backFuzzyHandler);
		handlerMap.put(WhereType.FUZZY, WhereHandlers.fuzzyHandler);
		handlerMap.put(WhereType.IN, WhereHandlers.inHandler);
	}

	private QuerySqlGenerator(Builder builder) {
		this.selectClauses = builder.selectClauses;
		this.tableClauses = builder.tableClauses;
		this.whereClauses = builder.whereClauses;
		this.orderByClauses = builder.orderByClauses;
		this.limitClause = builder.limitClause;
		this.joinClauses = builder.joinClauses;
		this.tableAlias = builder.tableAlias;
	}

	public static Builder startQuery() {
		return new QuerySqlGenerator.Builder();
	}

	public static class Builder implements IBuilder<QuerySqlGenerator> {

		private List<SelectClause> selectClauses;
		private List<TableClause> tableClauses;
		private List<WhereClause> whereClauses;
		private List<OrderClause> orderByClauses;
		private LimitClause limitClause;
		private List<JoinClause> joinClauses;
		private Map<Class<? extends Entity>, String> tableAlias;

		public Builder() {
			selectClauses = new ArrayList<>();
			tableClauses = new ArrayList<>();
			whereClauses = new ArrayList<>();
			orderByClauses = new ArrayList<>();
			joinClauses = new ArrayList<>();
			tableAlias = new HashMap<>();
		}

		public Builder select(SelectClause selectClause) {
			this.selectClauses.add(selectClause);
			return this;
		}

		public Builder select(String colunmName) {
			SelectClause selectClause = SelectClause.of(colunmName);
			this.selectClauses.add(selectClause);
			return this;
		}

		public Builder select(String colunmName, String alias) {
			SelectClause selectClause = SelectClause.of(colunmName, alias);
			this.selectClauses.add(selectClause);
			return this;
		}

		public Builder from(Class<? extends Entity> table) {
			TableClause tableClause = TableClause.of(table);
			this.tableClauses.add(tableClause);
			return this;
		}

		public Builder from(Class<? extends Entity> table, String alias) {
			TableClause tableClause = TableClause.of(table, alias);
			this.tableClauses.add(tableClause);
			this.tableAlias.put(table, alias);
			return this;
		}

		public Builder where(String colunmName, WhereType whereType, Object value) {
			WhereClause whereClause = WhereClause.of(colunmName, whereType, value);
			this.whereClauses.add(whereClause);
			return this;
		}

		public Builder where(String colunmName, WhereType whereType, List<Object> valueList) {
			WhereClause whereClause = WhereClause.of(colunmName, whereType, valueList);
			this.whereClauses.add(whereClause);
			return this;
		}

		public Builder where(String colunmName, String alias, WhereType whereType, Object value) {
			WhereClause whereClause = WhereClause.of(colunmName, alias, whereType, value);
			this.whereClauses.add(whereClause);
			return this;
		}

		public Builder orderBy(String columnName) {
			OrderClause orderClause = OrderClause.of(columnName);
			this.orderByClauses.add(orderClause);
			return this;
		}

		public Builder orderBy(String columnName, OrderByType orderByType) {
			OrderClause orderClause = OrderClause.of(columnName, orderByType);
			this.orderByClauses.add(orderClause);
			return this;
		}

		public Builder orderBy(String columnName, String alias) {
			OrderClause orderClause = OrderClause.of(columnName, alias);
			this.orderByClauses.add(orderClause);
			return this;
		}

		public Builder orderBy(String columnName, String alias, OrderByType orderByType) {
			OrderClause orderClause = OrderClause.of(columnName, alias, orderByType);
			this.orderByClauses.add(orderClause);
			return this;
		}

		public Builder limit(int start) {
			this.limitClause = LimitClause.of(start);
			return this;
		}

		public Builder limit(int start, int limit) {
			this.limitClause = LimitClause.of(start, limit);
			return this;
		}

		public Builder innerjoin(Class<? extends Entity> target, String alias, String targetColumn,
				Class<? extends Entity> source, String sourceColumn) {
			JoinClause leftJoinClause = JoinClause.of(JoinType.INNER, target, alias, targetColumn, source,
					sourceColumn);
			this.joinClauses.add(leftJoinClause);
			this.tableAlias.put(target, alias);
			return this;
		}

		public Builder leftjoin(Class<? extends Entity> target, String alias, String targetColumn,
				Class<? extends Entity> source, String sourceColumn) {
			JoinClause leftJoinClause = JoinClause.of(JoinType.LEFT, target, alias, targetColumn, source, sourceColumn);
			this.joinClauses.add(leftJoinClause);
			this.tableAlias.put(target, alias);
			return this;
		}

		public Builder rightjoin(Class<? extends Entity> target, String alias, String targetColumn,
				Class<? extends Entity> source, String sourceColumn) {
			JoinClause leftJoinClause = JoinClause.of(JoinType.RIGHT, target, alias, targetColumn, source,
					sourceColumn);
			this.joinClauses.add(leftJoinClause);
			this.tableAlias.put(target, alias);
			return this;
		}

		public Builder fulljoin(Class<? extends Entity> target, String alias, String targetColumn,
				Class<? extends Entity> source, String sourceColumn) {
			JoinClause leftJoinClause = JoinClause.of(JoinType.FULL, target, alias, targetColumn, source, sourceColumn);
			this.joinClauses.add(leftJoinClause);
			this.tableAlias.put(target, alias);
			return this;
		}

		public Builder crossjoin(Class<? extends Entity> target, String alias, String targetColumn,
				Class<? extends Entity> source, String sourceColumn) {
			JoinClause leftJoinClause = JoinClause.of(JoinType.CROSS, target, alias, targetColumn, source,
					sourceColumn);
			this.joinClauses.add(leftJoinClause);
			this.tableAlias.put(target, alias);
			return this;
		}

		@Override
		public QuerySqlGenerator build() {
			if (selectClauses.isEmpty())
				throw new IllegalArgumentException("当前没有select子句!至少要添加一个selectClause!");
			if (tableClauses.isEmpty())
				throw new IllegalArgumentException("当前没有from子句!至少要添加一个表!");
			return new QuerySqlGenerator(this);
		}
	}

	public List<SelectClause> getSelectClauses() {
		return selectClauses;
	}

	public List<TableClause> getTableClauses() {
		return tableClauses;
	}

	public List<WhereClause> getWhereClauses() {
		return whereClauses;
	}

	public List<OrderClause> getOrderByClauses() {
		return orderByClauses;
	}

	public LimitClause getLimitClause() {
		return limitClause;
	}

	public static Map<WhereType, IWhereHandler> getHandlerMap() {
		return handlerMap;
	}

	public List<JoinClause> getJoinClauses() {
		return joinClauses;
	}

	public String getTableAlias(Class<? extends Entity> table) {
		return tableAlias.get(table);
	}

	@Override
	public SqlWithParams generate() {
		GeneratorChain generatorChain = new GeneratorChain(this);
		generatorChain.addGenerator(SelectGenerator.class);
		generatorChain.addGenerator(TableGenerator.class);
		if (!joinClauses.isEmpty())
			generatorChain.addGenerator(JoinGenerator.class);
		if (!whereClauses.isEmpty())
			generatorChain.addGenerator(WhereGenerator.class);
		if (!orderByClauses.isEmpty())
			generatorChain.addGenerator(OrderByGenerator.class);
		if (limitClause != null)
			generatorChain.addGenerator(LimitGenerator.class);
		return generatorChain.startGenerator();
	}
}
