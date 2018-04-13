package top.starrysea.kql.facede;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import top.starrysea.kql.DeleteSqlGenerator;
import top.starrysea.kql.ISqlGenerator;
import top.starrysea.kql.InsertSqlGenerator;
import top.starrysea.kql.QuerySqlGenerator;
import top.starrysea.kql.SqlWithParams;
import top.starrysea.kql.UpdateSqlGenerator;
import top.starrysea.kql.clause.OrderByType;
import top.starrysea.kql.clause.SelectClause;
import top.starrysea.kql.clause.UpdateSetType;
import top.starrysea.kql.clause.WhereType;
import top.starrysea.kql.entity.Entity;
import top.starrysea.kql.entity.IBuilder;

@Component
public class KumaSqlDaoImpl implements KumaSqlDao {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private JdbcTemplate template;

	private ThreadLocal<IBuilder<? extends ISqlGenerator>> builder = new ThreadLocal<>();

	private ThreadLocal<OperationType> operationType = new ThreadLocal<>();

	private static final String NOT_SELECT_MODE_INFO = "当前不是SELECT模式,请调用changeMode进入SELECT模式!";

	public KumaSqlDaoImpl() {
		operationType.set(OperationType.SELECT);
		builder.set(new QuerySqlGenerator.Builder());
	}

	@Override
	public KumaSqlDao selectMode() {
		this.operationType.set(OperationType.SELECT);
		builder.set(new QuerySqlGenerator.Builder());
		return this;
	}

	@Override
	public KumaSqlDao insertMode() {
		this.operationType.set(OperationType.INSERT);
		builder.set(new InsertSqlGenerator.Builder());
		return this;
	}

	@Override
	public KumaSqlDao updateMode() {
		this.operationType.set(OperationType.UPDATE);
		builder.set(new UpdateSqlGenerator.Builder());
		return this;
	}

	@Override
	public KumaSqlDao deleteMode() {
		this.operationType.set(OperationType.DELETE);
		builder.set(new DeleteSqlGenerator.Builder());
		return this;
	}

	public KumaSqlDao select(SelectClause selectClause) {
		if (operationType.get() != OperationType.SELECT)
			throw new IllegalStateException(NOT_SELECT_MODE_INFO);
		top.starrysea.kql.QuerySqlGenerator.Builder queryBuilder = (top.starrysea.kql.QuerySqlGenerator.Builder) builder
				.get();
		queryBuilder.select(selectClause);
		return this;
	}

	public KumaSqlDao select(String colunmName) {
		if (operationType.get() != OperationType.SELECT)
			throw new IllegalStateException(NOT_SELECT_MODE_INFO);
		top.starrysea.kql.QuerySqlGenerator.Builder queryBuilder = (top.starrysea.kql.QuerySqlGenerator.Builder) builder
				.get();
		queryBuilder.select(colunmName);
		return this;
	}

	public KumaSqlDao select(String colunmName, String alias) {
		if (operationType.get() != OperationType.SELECT)
			throw new IllegalStateException(NOT_SELECT_MODE_INFO);
		top.starrysea.kql.QuerySqlGenerator.Builder queryBuilder = (top.starrysea.kql.QuerySqlGenerator.Builder) builder
				.get();
		queryBuilder.select(colunmName, alias);
		return this;
	}

	public KumaSqlDao from(Class<? extends Entity> table) {
		if (operationType.get() != OperationType.SELECT)
			throw new IllegalStateException(NOT_SELECT_MODE_INFO);
		top.starrysea.kql.QuerySqlGenerator.Builder queryBuilder = (top.starrysea.kql.QuerySqlGenerator.Builder) builder
				.get();
		queryBuilder.from(table);
		return this;
	}

	public KumaSqlDao from(Class<? extends Entity> table, String alias) {
		if (operationType.get() != OperationType.SELECT)
			throw new IllegalStateException(NOT_SELECT_MODE_INFO);
		top.starrysea.kql.QuerySqlGenerator.Builder queryBuilder = (top.starrysea.kql.QuerySqlGenerator.Builder) builder
				.get();
		queryBuilder.from(table, alias);
		return this;
	}

	public KumaSqlDao where(String columnName, WhereType whereType, Object value) {
		if (operationType.get() == OperationType.SELECT) {
			top.starrysea.kql.QuerySqlGenerator.Builder queryBuilder = (top.starrysea.kql.QuerySqlGenerator.Builder) builder
					.get();
			queryBuilder.where(columnName, whereType, value);
			return this;
		} else if (operationType.get() == OperationType.UPDATE) {
			top.starrysea.kql.UpdateSqlGenerator.Builder updateBuilder = (top.starrysea.kql.UpdateSqlGenerator.Builder) builder
					.get();
			updateBuilder.where(columnName, whereType, value);
			return this;
		} else if (operationType.get() == OperationType.DELETE) {
			top.starrysea.kql.DeleteSqlGenerator.Builder deleteBuilder = (top.starrysea.kql.DeleteSqlGenerator.Builder) builder
					.get();
			deleteBuilder.where(columnName, whereType, value);
			return this;
		} else
			throw new IllegalStateException(NOT_SELECT_MODE_INFO);
	}

	@Override
	public KumaSqlDao where(String columnName, WhereType whereType, List<Object> valueList) {
		if (operationType.get() == OperationType.SELECT) {
			top.starrysea.kql.QuerySqlGenerator.Builder queryBuilder = (top.starrysea.kql.QuerySqlGenerator.Builder) builder
					.get();
			queryBuilder.where(columnName, whereType, valueList);
			return this;
		} else if (operationType.get() == OperationType.UPDATE) {
			top.starrysea.kql.UpdateSqlGenerator.Builder updateBuilder = (top.starrysea.kql.UpdateSqlGenerator.Builder) builder
					.get();
			updateBuilder.where(columnName, whereType, valueList);
			return this;
		} else if (operationType.get() == OperationType.DELETE) {
			top.starrysea.kql.DeleteSqlGenerator.Builder deleteBuilder = (top.starrysea.kql.DeleteSqlGenerator.Builder) builder
					.get();
			deleteBuilder.where(columnName, whereType, valueList);
			return this;
		} else
			throw new IllegalStateException(NOT_SELECT_MODE_INFO);
	}

	public KumaSqlDao where(String columnName, String alias, WhereType whereType, Object value) {
		if (operationType.get() == OperationType.SELECT) {
			top.starrysea.kql.QuerySqlGenerator.Builder queryBuilder = (top.starrysea.kql.QuerySqlGenerator.Builder) builder
					.get();
			queryBuilder.where(columnName, alias, whereType, value);
			return this;
		} else
			throw new IllegalStateException(NOT_SELECT_MODE_INFO);
	}

	@Override
	public KumaSqlDao orderBy(String columnName) {
		if (operationType.get() != OperationType.SELECT)
			throw new IllegalStateException(NOT_SELECT_MODE_INFO);
		top.starrysea.kql.QuerySqlGenerator.Builder queryBuilder = (top.starrysea.kql.QuerySqlGenerator.Builder) builder
				.get();
		queryBuilder.orderBy(columnName);
		return this;
	}

	@Override
	public KumaSqlDao orderBy(String columnName, OrderByType orderByType) {
		if (operationType.get() != OperationType.SELECT)
			throw new IllegalStateException(NOT_SELECT_MODE_INFO);
		top.starrysea.kql.QuerySqlGenerator.Builder queryBuilder = (top.starrysea.kql.QuerySqlGenerator.Builder) builder
				.get();
		queryBuilder.orderBy(columnName, orderByType);
		return this;
	}

	@Override
	public KumaSqlDao orderBy(String columnName, String alias) {
		if (operationType.get() != OperationType.SELECT)
			throw new IllegalStateException(NOT_SELECT_MODE_INFO);
		top.starrysea.kql.QuerySqlGenerator.Builder queryBuilder = (top.starrysea.kql.QuerySqlGenerator.Builder) builder
				.get();
		queryBuilder.orderBy(columnName, alias);
		return this;
	}

	@Override
	public KumaSqlDao orderBy(String columnName, String alias, OrderByType orderByType) {
		if (operationType.get() != OperationType.SELECT)
			throw new IllegalStateException(NOT_SELECT_MODE_INFO);
		top.starrysea.kql.QuerySqlGenerator.Builder queryBuilder = (top.starrysea.kql.QuerySqlGenerator.Builder) builder
				.get();
		queryBuilder.orderBy(columnName, alias, orderByType);
		return this;
	}

	@Override
	public KumaSqlDao limit(int start) {
		if (operationType.get() != OperationType.SELECT)
			throw new IllegalStateException(NOT_SELECT_MODE_INFO);
		top.starrysea.kql.QuerySqlGenerator.Builder queryBuilder = (top.starrysea.kql.QuerySqlGenerator.Builder) builder
				.get();
		queryBuilder.limit(start);
		return this;
	}

	@Override
	public KumaSqlDao limit(int start, int limit) {
		if (operationType.get() != OperationType.SELECT)
			throw new IllegalStateException(NOT_SELECT_MODE_INFO);
		top.starrysea.kql.QuerySqlGenerator.Builder queryBuilder = (top.starrysea.kql.QuerySqlGenerator.Builder) builder
				.get();
		queryBuilder.limit(start, limit);
		return this;
	}

	@Override
	public KumaSqlDao innerjoin(Class<? extends Entity> target, String alias, String targetColumn,
			Class<? extends Entity> source, String sourceColumn) {
		if (operationType.get() != OperationType.SELECT)
			throw new IllegalStateException(NOT_SELECT_MODE_INFO);
		top.starrysea.kql.QuerySqlGenerator.Builder queryBuilder = (top.starrysea.kql.QuerySqlGenerator.Builder) builder
				.get();
		queryBuilder.innerjoin(target, alias, targetColumn, source, sourceColumn);
		return this;
	}

	@Override
	public KumaSqlDao leftjoin(Class<? extends Entity> target, String alias, String targetColumn,
			Class<? extends Entity> source, String sourceColumn) {
		if (operationType.get() != OperationType.SELECT)
			throw new IllegalStateException(NOT_SELECT_MODE_INFO);
		top.starrysea.kql.QuerySqlGenerator.Builder queryBuilder = (top.starrysea.kql.QuerySqlGenerator.Builder) builder
				.get();
		queryBuilder.leftjoin(target, alias, targetColumn, source, sourceColumn);
		return this;
	}

	@Override
	public KumaSqlDao rightjoin(Class<? extends Entity> target, String alias, String targetColumn,
			Class<? extends Entity> source, String sourceColumn) {
		if (operationType.get() != OperationType.SELECT)
			throw new IllegalStateException(NOT_SELECT_MODE_INFO);
		top.starrysea.kql.QuerySqlGenerator.Builder queryBuilder = (top.starrysea.kql.QuerySqlGenerator.Builder) builder
				.get();
		queryBuilder.rightjoin(target, alias, targetColumn, source, sourceColumn);
		return this;
	}

	@Override
	public KumaSqlDao fulljoin(Class<? extends Entity> target, String alias, String targetColumn,
			Class<? extends Entity> source, String sourceColumn) {
		if (operationType.get() != OperationType.SELECT)
			throw new IllegalStateException(NOT_SELECT_MODE_INFO);
		top.starrysea.kql.QuerySqlGenerator.Builder queryBuilder = (top.starrysea.kql.QuerySqlGenerator.Builder) builder
				.get();
		queryBuilder.fulljoin(target, alias, targetColumn, source, sourceColumn);
		return this;
	}

	@Override
	public KumaSqlDao crossjoin(Class<? extends Entity> target, String alias, String targetColumn,
			Class<? extends Entity> source, String sourceColumn) {
		if (operationType.get() != OperationType.SELECT)
			throw new IllegalStateException(NOT_SELECT_MODE_INFO);
		top.starrysea.kql.QuerySqlGenerator.Builder queryBuilder = (top.starrysea.kql.QuerySqlGenerator.Builder) builder
				.get();
		queryBuilder.crossjoin(target, alias, targetColumn, source, sourceColumn);
		return this;
	}

	@Override
	public KumaSqlDao insert(String columnName) {
		if (operationType.get() != OperationType.INSERT)
			throw new IllegalStateException("当前不是INSERT模式,请调用insertMode()进入INSERT模式!");
		top.starrysea.kql.InsertSqlGenerator.Builder insertBuilder = (top.starrysea.kql.InsertSqlGenerator.Builder) builder
				.get();
		insertBuilder.insert(columnName);
		return this;
	}

	@Override
	public KumaSqlDao insert(String columnName, Object value) {
		if (operationType.get() != OperationType.INSERT)
			throw new IllegalStateException("当前不是INSERT模式,请调用insertMode()进入INSERT模式!");
		top.starrysea.kql.InsertSqlGenerator.Builder insertBuilder = (top.starrysea.kql.InsertSqlGenerator.Builder) builder
				.get();
		insertBuilder.insert(columnName, value);
		return this;
	}

	@Override
	public KumaSqlDao table(Class<? extends Entity> table) {
		if (operationType.get() == OperationType.INSERT) {
			top.starrysea.kql.InsertSqlGenerator.Builder insertBuilder = (top.starrysea.kql.InsertSqlGenerator.Builder) builder
					.get();
			insertBuilder.into(table);
			return this;
		} else if (operationType.get() == OperationType.DELETE) {
			top.starrysea.kql.DeleteSqlGenerator.Builder deleteBuilder = (top.starrysea.kql.DeleteSqlGenerator.Builder) builder
					.get();
			deleteBuilder.table(table);
			return this;
		} else if (operationType.get() == OperationType.UPDATE) {
			top.starrysea.kql.UpdateSqlGenerator.Builder updateBuilder = (top.starrysea.kql.UpdateSqlGenerator.Builder) builder
					.get();
			updateBuilder.table(table);
			return this;
		}
		throw new IllegalStateException("SELECT模式不支持该方法!");
	}

	@Override
	public KumaSqlDao update(String columnName, UpdateSetType updateSetType) {
		if (operationType.get() != OperationType.UPDATE)
			throw new IllegalStateException("当前不是UPDATE模式,请调用updateMode()进入UPDATE模式!");
		top.starrysea.kql.UpdateSqlGenerator.Builder updateBuilder = (top.starrysea.kql.UpdateSqlGenerator.Builder) builder
				.get();
		updateBuilder.update(columnName, updateSetType);
		return this;
	}

	@Override
	public KumaSqlDao update(String columnName, UpdateSetType updateSetType, Object value) {
		if (operationType.get() != OperationType.UPDATE)
			throw new IllegalStateException("当前不是UPDATE模式,请调用updateMode()进入UPDATE模式!");
		top.starrysea.kql.UpdateSqlGenerator.Builder updateBuilder = (top.starrysea.kql.UpdateSqlGenerator.Builder) builder
				.get();
		updateBuilder.update(columnName, updateSetType, value);
		return this;
	}

	@Override
	public <T> ListSqlResult<T> endForList(RowMapper<T> rowMapper) {
		if (operationType.get() != OperationType.SELECT)
			throw new UnsupportedOperationException("endForList方法仅支持SELECT模式,增删改请使用无参数版本的end方法");
		SqlWithParams sqlWithParams = builder.get().build().generate();
		logSql(sqlWithParams);
		List<T> result = template.query(sqlWithParams.getSql(), sqlWithParams.getParams(), rowMapper);
		resetBuilder();
		return new ListSqlResult<>(result);
	}

	@Override
	public <T> ListSqlResult<T> endForList(Class<T> clazz) {
		if (operationType.get() != OperationType.SELECT)
			throw new UnsupportedOperationException("endForList方法仅支持SELECT模式,增删改请使用无参数版本的end方法");
		SqlWithParams sqlWithParams = builder.get().build().generate();
		logSql(sqlWithParams);
		List<T> result = template.queryForList(sqlWithParams.getSql(), sqlWithParams.getParams(), clazz);
		resetBuilder();
		return new ListSqlResult<>(result);
	}

	@Override
	public IntegerSqlResult endForNumber() {
		if (operationType.get() != OperationType.SELECT)
			throw new UnsupportedOperationException("endForNumber方法仅支持SELECT模式,增删改请使用无参数版本的end方法");
		QuerySqlGenerator querySqlGenerator = (QuerySqlGenerator) builder.get().build();
		if (querySqlGenerator.getSelectClauses().size() != 1)
			throw new IllegalArgumentException("只能SELECT一列的数字!如SELECT COUNT(*) FROM...");
		SqlWithParams sqlWithParams = querySqlGenerator.generate();
		logSql(sqlWithParams);
		Integer result = template.queryForObject(sqlWithParams.getSql(), sqlWithParams.getParams(), Integer.class);
		resetBuilder();
		return new IntegerSqlResult(result);
	}

	@Override
	public <T> EntitySqlResult<T> endForObject(RowMapper<T> rowMapper) {
		if (operationType.get() != OperationType.SELECT)
			throw new UnsupportedOperationException("endForObject方法仅支持SELECT模式,增删改请使用无参数版本的end方法");
		SqlWithParams sqlWithParams = builder.get().build().generate();
		logSql(sqlWithParams);
		T result = template.queryForObject(sqlWithParams.getSql(), sqlWithParams.getParams(), rowMapper);
		resetBuilder();
		return new EntitySqlResult<>(result);
	}

	@Override
	public UpdateSqlResult end() {
		if (operationType.get() == OperationType.SELECT)
			throw new UnsupportedOperationException("该end方法不支持SELECT模式");
		SqlWithParams sqlWithParams = builder.get().build().generate();
		logSql(sqlWithParams);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		template.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sqlWithParams.getSql(), Statement.RETURN_GENERATED_KEYS);
				Object[] params = sqlWithParams.getParams();
				for (int i = 1, len = params.length; i <= len; i++) {
					ps.setObject(i, params[i - 1]);
				}
				return ps;
			}
		}, keyHolder);
		resetBuilder();
		return new UpdateSqlResult(keyHolder);
	}

	private void logSql(SqlWithParams sqlWithParams) {
		if (logger.isInfoEnabled()) {
			logger.info("生成的sql为:{}", sqlWithParams.getSql());
			logger.info("sql的参数为{}", Arrays.toString(sqlWithParams.getParams()));
		}
	}

	@Override
	@Transactional
	public SqlResult batchEnd(BatchPreparedStatementSetter bpss) {
		if (operationType.get() == OperationType.SELECT)
			throw new UnsupportedOperationException("该end方法不支持SELECT模式");
		SqlWithParams sqlWithParams = builder.get().build().generate();
		if (logger.isInfoEnabled()) {
			logger.info("生成的sql为:{}", sqlWithParams.getSql());
			logger.info("sql的参数为{}", Arrays.toString(sqlWithParams.getParams()));
		}
		template.batchUpdate(sqlWithParams.getSql(), bpss);
		resetBuilder();
		return new SqlResult(true);
	}

	private void resetBuilder() {
		switch (operationType.get()) {
		case INSERT:
			builder.set(new InsertSqlGenerator.Builder());
			break;
		case DELETE:
			builder.set(new DeleteSqlGenerator.Builder());
			break;
		case SELECT:
			builder.set(new QuerySqlGenerator.Builder());
			break;
		case UPDATE:
			builder.set(new UpdateSqlGenerator.Builder());
			break;
		}
	}

	@Override
	public JdbcTemplate getTemplate() {
		return template;
	}
}
