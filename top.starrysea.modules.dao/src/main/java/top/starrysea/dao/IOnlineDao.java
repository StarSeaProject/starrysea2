package top.starrysea.dao;

import java.util.List;

import top.starrysea.common.DaoResult;
import top.starrysea.object.dto.Online;

public interface IOnlineDao {

	List<Online> getAllOnlineDao();

	DaoResult saveOnlineDao(Online online);
}
