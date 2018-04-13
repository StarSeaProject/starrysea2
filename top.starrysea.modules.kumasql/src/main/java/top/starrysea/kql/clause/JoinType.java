package top.starrysea.kql.clause;

public enum JoinType {

	INNER("INNER JOIN "), LEFT("LEFT JOIN "), RIGHT("RIGHT JOIN "), FULL("FULL JOIN "),CROSS("CROSS JOIN ");

	private String value;

	JoinType(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
