package top.starrysea.service.impl;

import static top.starrysea.common.Common.isNotNull;
import static top.starrysea.common.ResultKey.USER;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import top.starrysea.common.Common;
import top.starrysea.common.ServiceResult;
import top.starrysea.dao.IUserDao;
import top.starrysea.kql.facede.KumaRedisDao;
import top.starrysea.object.dto.User;
import top.starrysea.object.view.in.UserForSave;
import top.starrysea.service.IUserService;

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
			String activateCode = Common.getCharId(30);
			kumaRedisDao.set(activateCode, Common.toJson(userList), 600);
			user.setUserId(activateCode);
			// 由于发送邮件服务只能传一个对象所以使用userId来存储激活码了,真正的user还是存在redis中的
			return ServiceResult.of(true).setResult(USER, user);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ServiceResult.of(false).setErrInfo(e.getMessage());
		}
	}

	@Override
	public ServiceResult checkUserAvailabilityService(User user) {
		boolean checkResult = userDao.checkUserAvailabilityDao(user);
		return ServiceResult.of(checkResult).setErrInfo(checkResult ? null : "该邮箱已被注册!");
	}

	@Override
	public ServiceResult userLogin(User user) {
		User user1 = userDao.getUserDao(user);
		ServiceResult serviceResult= ServiceResult.of();
		if (isNotNull(user1)) {
			serviceResult.setSuccessed(true).setResult(USER, user1);
		} else {
			serviceResult.setSuccessed(false).setErrInfo("用户名或密码错误");
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
				kumaRedisDao.delete(redisKey);
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

	@Override
	public ServiceResult getUserInfoService(String userId) {
		return ServiceResult.of(true).setResult(USER, userDao.getUserInfoDao(userId));
	}

	@Override
	public ServiceResult editUserInfoService(User user) {
		userDao.updateUserDao(user);
		return ServiceResult.of(true);
	}
}