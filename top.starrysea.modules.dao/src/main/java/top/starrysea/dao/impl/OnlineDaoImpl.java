package top.starrysea.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import top.starrysea.common.Common;
import top.starrysea.common.Condition;
import top.starrysea.common.DaoResult;
import top.starrysea.dao.IOnlineDao;
import top.starrysea.kql.clause.SelectClause;
import top.starrysea.kql.clause.UpdateSetType;
import top.starrysea.kql.clause.WhereType;
import top.starrysea.kql.facede.IntegerSqlResult;
import top.starrysea.kql.facede.KumaSqlDao;
import top.starrysea.kql.facede.ListSqlResult;
import top.starrysea.kql.facede.UpdateSqlResult;
import top.starrysea.object.dto.Online;
import top.starrysea.object.dto.Orders;

@Repository("onlineDao")
public class OnlineDaoImpl implements IOnlineDao {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private KumaSqlDao kumaSqlDao;

	public static final int PAGE_LIMIT = 10;

	@Override
	public List<Online> getAllOnlineDao(Online online) {
		kumaSqlDao.selectMode();
		ListSqlResult<Online> theResult = kumaSqlDao.select("online_email").select("online_status").from(Online.class)
				.where("online_status", WhereType.EQUALS, online.getOnlineStatus())
				.endForList((rs, row) -> new Online.Builder().onlineEmail(rs.getString("online_email"))
						.onlineStatus(rs.getShort("online_status")).build());
		return theResult.getResult();
	}

	@Override
	public List<Online> getOnlineDao(Online online) {
		kumaSqlDao.selectMode();
		List<Online> onlines = new ArrayList<>();
		try {
			if (Common.isNotNull(online.getOnlinePhone())) {
				ListSqlResult<Online> theResult = kumaSqlDao.select("online_email").select("online_status")
						.from(Online.class, "o")
						.leftjoin(Orders.class, "od", "order_email", Online.class, "online_email")
						.where("order_phone", WhereType.EQUALS, online.getOnlinePhone())
						.where("online_status", WhereType.EQUALS, online.getOnlineStatus())
						.endForList((rs, row) -> new Online.Builder().onlineEmail(rs.getString("online_email"))
								.onlineStatus(rs.getShort("online_status")).build());
				return theResult.getResult();
			} else if (Common.isNotNull(online.getOnlineEmail())) {
				ListSqlResult<Online> theResult = kumaSqlDao.select("online_email").select("online_status")
						.from(Online.class).where("online_email", WhereType.EQUALS, online.getOnlineEmail())
						.where("online_status", WhereType.EQUALS, online.getOnlineStatus())
						.endForList((rs, row) -> new Online.Builder().onlineEmail(rs.getString("online_email"))
								.onlineStatus(rs.getShort("online_status")).build());
				return theResult.getResult();
			}
			return onlines;
		} catch (Exception e) {
			e.printStackTrace();
			return onlines;
		}
	}

	@Override
	public DaoResult saveOnlineDao(Online online) {
		kumaSqlDao.insertMode();
		try {
			kumaSqlDao.insert("online_id", online.getOnlineId()).insert("online_email", online.getOnlineEmail())
					.insert("online_status", online.getOnlineStatus()).table(Online.class).end();
		} catch (DuplicateKeyException e) {
			return new DaoResult(false, "重复的email!");
		}
		return new DaoResult(true);
	}

	@Override
	public int updateOnlineDao(Online online) {
		kumaSqlDao.updateMode();
		UpdateSqlResult theResult = kumaSqlDao.update("online_status", UpdateSetType.ASSIGN, online.getOnlineStatus())
				.table(Online.class).where("online_email", WhereType.EQUALS, online.getOnlineEmail()).end();
		return theResult.getKeyHolder().getKey().intValue();
	}

	@Override
	public int deleteOnlineDao(Online online) {
		kumaSqlDao.deleteMode();
		UpdateSqlResult theResult = kumaSqlDao.table(Online.class)
				.where("online_email", WhereType.EQUALS, online.getOnlineEmail()).end();
		return theResult.getKeyHolder().getKey().intValue();
	}

	@Override
	public int getOnlineCountDao(Condition condition, Online online) {
		IntegerSqlResult theResult = kumaSqlDao.select(SelectClause.COUNT).from(Online.class)
				.where("online_status", WhereType.EQUALS, online.getOnlineStatus()).endForNumber();
		return theResult.getResult();
	}

}
