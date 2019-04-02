package top.starrysea.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import top.starrysea.dao.IUserDao;
import top.starrysea.kql.facede.KumaSqlDao;
import top.starrysea.object.dto.User;

@Repository("userDao")
public class UserDaoImpl implements IUserDao {

    @Autowired
    private KumaSqlDao kumaSqlDao;

    @Override
    public User registerDao(User user) {
        kumaSqlDao.insertMode();
        kumaSqlDao.insert("user_id", user.getUserId())
                .insert("user_email", user.getUserEmail())
                .insert("user_password", user.getUserPassword())
                .insert("user_name", user.getUsername())
                .insert("user_osu1", user.getOsu1())
                .insert("user_osu2", user.getOsu2())
                .insert("user_osu3", user.getOsu3())
                .table(User.class).end();
        return user;
    }
}
