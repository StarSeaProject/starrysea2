package top.starrysea.dao;

import java.util.List;

import top.starrysea.common.Condition;
import top.starrysea.common.DaoResult;
import top.starrysea.object.dto.Online;

public interface IOnlineDao {

	List<Online> getAllOnlineDao(Online online);

	List<Online> getOnlineDao(Online online);

	DaoResult saveOnlineDao(Online online);

	int updateOnlineDao(Online online);

	int deleteOnlineDao(Online online);
	
	int getOnlineCountDao(Condition condition,Online online);

}
