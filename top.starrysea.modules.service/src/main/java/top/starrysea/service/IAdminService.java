package top.starrysea.service;

import top.starrysea.common.ServiceResult;
import top.starrysea.object.dto.Admin;

public interface IAdminService {
	ServiceResult loginService(Admin admin);
}
