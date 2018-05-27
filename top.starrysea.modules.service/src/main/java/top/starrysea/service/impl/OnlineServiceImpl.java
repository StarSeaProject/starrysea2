package top.starrysea.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import top.starrysea.common.Common;
import top.starrysea.common.DaoResult;
import top.starrysea.common.ServiceResult;
import top.starrysea.dao.IOnlineDao;
import top.starrysea.object.dto.Online;
import top.starrysea.service.IOnlineService;
import static top.starrysea.common.ResultKey.*;

@Service("onlineService")
public class OnlineServiceImpl implements IOnlineService {

	@Autowired
	private IOnlineDao onlineDao;

	@Override
	public ServiceResult addMailService(Online online) {
		online.setOnlineId(Common.getCharId("O-", 10));
		online.setOnlineStatus((short) 1);
		DaoResult daoResult = onlineDao.saveOnlineDao(online);
		ServiceResult sr = ServiceResult.of(daoResult.isSuccessed());
		if (!daoResult.isSuccessed())
			sr.setErrInfo(daoResult.getErrInfo());
		return sr;
	}

	@Override
	public ServiceResult queryOnlineService(Online online) {
		return ServiceResult.of(true).setResult(LIST_1, onlineDao.getOnlineDao(online));
	}

	@Override
	public ServiceResult modifyOnlineService(Online online) {
		if (onlineDao.updateOnlineDao(online) < 1) {
			return ServiceResult.of(false).setErrInfo("更新失败");
		}
		return ServiceResult.of(true);
	}

	@Override
	public ServiceResult removeOnlineService(Online online) {
		if (onlineDao.deleteOnlineDao(online) < 1) {
			return ServiceResult.of(false).setErrInfo("删除失败");
		}
		return ServiceResult.of(true);
	}

}
