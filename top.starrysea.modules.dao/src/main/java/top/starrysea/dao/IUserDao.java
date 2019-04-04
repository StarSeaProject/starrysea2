package top.starrysea.dao;

import top.starrysea.common.DaoResult;
import top.starrysea.object.dto.User;

public interface IUserDao {

	User saveUserDao(User user);

	DaoResult getUserDao(User user);

}
