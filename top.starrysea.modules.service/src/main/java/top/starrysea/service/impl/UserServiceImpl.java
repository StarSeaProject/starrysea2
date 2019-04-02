package top.starrysea.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.starrysea.common.ServiceResult;
import top.starrysea.dao.IUserDao;
import top.starrysea.object.dto.User;
import top.starrysea.service.IUserService;

@Service("userService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserDao userDao;

    @Override
    public ServiceResult addUserService(User user) {
        userDao.saveUserDao(user);
        return ServiceResult.SUCCESS_SERVICE_RESULT;
    }
}
