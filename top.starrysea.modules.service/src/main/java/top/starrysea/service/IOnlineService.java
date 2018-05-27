package top.starrysea.service;

import top.starrysea.common.ServiceResult;
import top.starrysea.object.dto.Online;

public interface IOnlineService {

	ServiceResult addMailService(Online online);

	ServiceResult queryOnlineService(Online online);

	ServiceResult modifyOnlineService(Online online);

	ServiceResult removeOnlineService(Online online);

}
