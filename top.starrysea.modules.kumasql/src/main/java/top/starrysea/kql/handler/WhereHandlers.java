package top.starrysea.kql.handler;

import java.util.ArrayList;
import java.util.List;

public class WhereHandlers {

	private WhereHandlers() {
	}

	public static final IWhereHandler equalsHandler = (where, params) -> {
		String whereClause = where.getColumnName() + " = ?";
		params.add(where.getValue());
		return new HandleResult(whereClause, params);
	};

	public static final IWhereHandler frontFuzzyHandler = (where, params) -> {
		String whereClause = where.getColumnName() + " LIKE ?";
		params.add("%" + where.getValue());
		return new HandleResult(whereClause, params);
	};

	public static final IWhereHandler backFuzzyHandler = (where, params) -> {
		String whereClause = where.getColumnName() + " LIKE ?";
		params.add(where.getValue() + "%");
		return new HandleResult(whereClause, params);
	};

	public static final IWhereHandler fuzzyHandler = (where, params) -> {
		String whereClause = where.getColumnName() + " LIKE ?";
		params.add("%" + where.getValue() + "%");
		return new HandleResult(whereClause, params);
	};

	public static final IWhereHandler greaterHandler = (where, params) -> {
		String whereClause = where.getColumnName() + " > ?";
		params.add("%" + where.getValue() + "%");
		return new HandleResult(whereClause, params);
	};

	public static final IWhereHandler greaterEqualHandler = (where, params) -> {
		String whereClause = where.getColumnName() + " >= ?";
		params.add("%" + where.getValue() + "%");
		return new HandleResult(whereClause, params);
	};

	public static final IWhereHandler lessHandler = (where, params) -> {
		String whereClause = where.getColumnName() + " < ?";
		params.add("%" + where.getValue() + "%");
		return new HandleResult(whereClause, params);
	};

	public static final IWhereHandler lessEqualHandler = (where, params) -> {
		String whereClause = where.getColumnName() + " <= ?";
		params.add("%" + where.getValue() + "%");
		return new HandleResult(whereClause, params);
	};
	
	public static final IWhereHandler inHandler = (where,params) -> {
		List<String> elements=new ArrayList<>();
		for(Object value:where.getValueList()) {
			elements.add("?");
			params.add(value.toString());
		}
		String whereClause = where.getColumnName()+ " IN ("+String.join(",", elements)+")";
		return new HandleResult(whereClause, params);
	};
}
