package top.starrysea.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.starrysea.common.Common;
import top.starrysea.common.DaoResult;
import top.starrysea.common.ServiceResult;
import top.starrysea.dao.IUserDao;
import top.starrysea.kql.facede.KumaRedisDao;
import top.starrysea.object.dto.User;
import top.starrysea.object.view.in.UserForSave;
import top.starrysea.service.IUserService;

import java.util.ArrayList;
import java.util.List;

import static top.starrysea.common.ResultKey.*;

@Service("userService")
public class UserServiceImpl implements IUserService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IUserDao userDao;
	@Autowired
	private KumaRedisDao kumaRedisDao;

	@Override
	public ServiceResult registerService(User user) {
		try {
			user.setUserId(Common.getCharId("U-", 20));
			List<User> userList = new ArrayList<>();
			userList.add(user);
			kumaRedisDao.set(user.getUserId(), Common.toJson(userList));
			return ServiceResult.of(true).setResult(USER, user);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ServiceResult.of(false).setErrInfo(e.getMessage());
		}
	}

	@Override
	public ServiceResult checkUserAvailabilityService(User user) {
		user.setUserPassword("");
		DaoResult daoResult = userDao.getUserDao(user);
		ServiceResult serviceResult;
		if (daoResult.getErrInfo().equals("用户账号不存在")) {
			serviceResult = ServiceResult.of(true);
		} else if (daoResult.getErrInfo().equals("密码错误")) {
			serviceResult = ServiceResult.of("用户已存在");
		} else {
			serviceResult = ServiceResult.of("其他错误");
		}
		return serviceResult;
	}

	@Override
	public ServiceResult userLogin(User user) {
		DaoResult daoResult = userDao.getUserDao(user);
		ServiceResult serviceResult = ServiceResult.of(daoResult.isSuccessed());
		if (serviceResult.isSuccessed()) {
			serviceResult.setResult(USER, daoResult.getResult(User.class));
		} else {
			serviceResult.setErrInfo(daoResult.getErrInfo());
		}
		return serviceResult;
	}

	@Override
	public ServiceResult activateService(String redisKey) {
		ServiceResult serviceResult = ServiceResult.of();
		try {
			List<UserForSave> userList = kumaRedisDao.getList(redisKey, UserForSave.class);
			if (userList.isEmpty()) {
				serviceResult.setSuccessed(false).setErrInfo("无效的激活码");
				logger.info("激活失败:激活码 {} 无效", redisKey);
			} else {
				UserForSave userForSave = userList.get(0);
				userDao.saveUserDao(userForSave.toDTO());
				kumaRedisDao.delete(userForSave.getUserId());
				serviceResult.setSuccessed(true).setResult(USER, userForSave.toDTO());
				logger.info("用户 {} 的激活成功", userForSave.getUserEmail());
			}
		} catch (Exception e) {
			logger.info("激活失败:使用 {} 激活时出现异常", redisKey);
			logger.error(e.getMessage(), e);
			serviceResult.setSuccessed(false).setErrInfo(e.getMessage());
		}
		return serviceResult;
	}
}
