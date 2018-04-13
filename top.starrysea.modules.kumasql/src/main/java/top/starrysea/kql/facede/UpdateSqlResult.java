package top.starrysea.kql.facede;

import org.springframework.jdbc.support.KeyHolder;

public class UpdateSqlResult extends SqlResult {

	private KeyHolder keyHolder;

	public UpdateSqlResult(KeyHolder keyHolder) {
		super(true);
		this.keyHolder = keyHolder;
	}

	public KeyHolder getKeyHolder() {
		return keyHolder;
	}

	public void setKeyHolder(KeyHolder keyHolder) {
		this.keyHolder = keyHolder;
	}

}
