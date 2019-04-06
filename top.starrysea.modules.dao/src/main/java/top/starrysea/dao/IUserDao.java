package top.starrysea.dao;

import top.starrysea.object.dto.User;

public interface IUserDao {

	User saveUserDao(User user);

	User getUserDao(User user);

	boolean checkUserAvailabilityDao(User user);

	User getUserInfoDao(String userId);
	
	void updateUserDao(User user);

	void updateUserPasswordDao(User user);
}
