package top.starrysea.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import top.starrysea.common.Common;
import top.starrysea.common.DaoResult;
import top.starrysea.common.ResultKey;
import top.starrysea.common.ServiceResult;
import top.starrysea.dao.IUserDao;
import top.starrysea.object.dto.User;
import top.starrysea.service.IUserService;

@Service("userService")
public class UserServiceImpl implements IUserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IUserDao userDao;

    @Override
    public ServiceResult registerService(User user) {
        try {
            user.setUserId(Common.getCharId("U-", 20));
            userDao.saveUserDao(user);
            return ServiceResult.of(true).setResult(ResultKey.USER, user);
        } catch (DuplicateKeyException e) {
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
}
