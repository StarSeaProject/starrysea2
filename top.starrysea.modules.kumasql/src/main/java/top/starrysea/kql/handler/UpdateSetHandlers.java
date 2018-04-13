package top.starrysea.kql.handler;

public class UpdateSetHandlers {

	private UpdateSetHandlers() {
	}

	public static final IUpdateSetHandler assignHandler = (updateSetClause, params) -> {
		String whereClause = updateSetClause.getColumnName() + " = ?";
		params.add(updateSetClause.getValue());
		return new HandleResult(whereClause, params);
	};

	public static final IUpdateSetHandler addHandler = (updateSetClause, params) -> {
		String whereClause = updateSetClause.getColumnName() + " = " + updateSetClause.getColumnName() + " + ? ";
		params.add(updateSetClause.getValue());
		return new HandleResult(whereClause, params);
	};

	public static final IUpdateSetHandler reduceHandler = (updateSetClause, params) -> {
		String whereClause = updateSetClause.getColumnName() + " = " + updateSetClause.getColumnName() + " - ? ";
		params.add(updateSetClause.getValue());
		return new HandleResult(whereClause, params);
	};
}
