package top.starrysea.kql.clause;

public class LimitClause {

	private int start;
	private int limit;

	public static LimitClause of(int start) {
		LimitClause limitClause = new LimitClause();
		limitClause.setStart(start);
		return limitClause;
	}

	public static LimitClause of(int start, int limit) {
		LimitClause limitClause = new LimitClause();
		limitClause.setStart(start);
		limitClause.setLimit(limit);
		return limitClause;
	}

	public int getStart() {
		return start;
	}

	private void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	private void setLimit(int limit) {
		this.limit = limit;
	}

	public String getLimitClause() {
		if (limit == 0) {
			return "" + start;
		} else {
			return start + "," + limit;
		}
	}
}
