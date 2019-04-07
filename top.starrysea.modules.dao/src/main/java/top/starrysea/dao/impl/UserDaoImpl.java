package top.starrysea.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import top.starrysea.dao.IUserDao;
import top.starrysea.kql.clause.UpdateSetType;
import top.starrysea.kql.clause.WhereType;
import top.starrysea.kql.facede.EntitySqlResult;
import top.starrysea.kql.facede.KumaSqlDao;
import top.starrysea.kql.facede.ListSqlResult;
import top.starrysea.object.dto.User;

import static top.starrysea.common.Common.isNotNull;
import static top.starrysea.common.Common.sha512;

@Repository("userDao")
public class UserDaoImpl implements IUserDao {

	@Autowired
	private KumaSqlDao kumaSqlDao;

	@Override
	public User saveUserDao(User user) {
		kumaSqlDao.insertMode();
		kumaSqlDao.insert("user_id", user.getUserId()).insert("user_email", user.getUserEmail())
				.insert("user_password", sha512(user.getUserEmail() + user.getUserPassword()))
				.insert("user_name", user.getUsername()).insert("user_osu_person", user.getOsuPerson())
				.insert("user_osu_team", user.getOsuTeam()).insert("user_osu_grade", user.getOsuGrade())
				.insert("user_dd_flag", user.getIsDD()).table(User.class).end();
		return user;
	}

	@Override
	public User getUserDao(User user) {
		kumaSqlDao.selectMode();
		ListSqlResult<String> userEmail = kumaSqlDao.select("1").from(User.class)
				.where("user_email", WhereType.EQUALS, user.getUserEmail()).endForList(String.class);
		if (userEmail.getResult().isEmpty()) {
			return null;
		}
		ListSqlResult<User> userResult = kumaSqlDao.select("user_id").select("user_email").select("user_name")
				.select("user_osu_person").select("user_osu_team").select("user_osu_grade").select("user_dd_flag")
				.from(User.class).where("user_email", WhereType.EQUALS, user.getUserEmail())
				.where("user_password", WhereType.EQUALS, sha512(user.getUserEmail() + user.getUserPassword()))
				.endForList((rs, row) -> new User.Builder().userId(rs.getString("user_id"))
						.userEmail(rs.getString("user_email")).username(rs.getString("user_name"))
						.osuPerson(rs.getShort("user_osu_person")).osuTeam(rs.getShort("user_osu_team"))
						.osuGrade(rs.getShort("user_osu_grade")).isDD(rs.getShort("user_dd_flag")).build());
		if (isNotNull(userResult.getResult())) {
			return userResult.getResult().get(0);
		} else {
			return null;
		}
	}

	@Override
	public boolean checkUserAvailabilityDao(User user) {
		kumaSqlDao.selectMode();
		ListSqlResult<String> list = kumaSqlDao.select("1").from(User.class)
				.where("user_email", WhereType.EQUALS, user.getUserEmail()).endForList(String.class);
		return list.getResult().isEmpty();
	}

	@Override
	public User getUserInfoDao(String userId) {
		kumaSqlDao.selectMode();
		EntitySqlResult<User> result = kumaSqlDao.select("user_email").select("user_name").select("user_osu_person")
				.select("user_osu_team").select("user_osu_grade").select("user_dd_flag").from(User.class)
				.where("user_id", WhereType.EQUALS, userId)
				.endForObject((rs, row) -> new User.Builder().userEmail(rs.getString("user_email"))
						.username(rs.getString("user_name")).osuPerson(rs.getShort("user_osu_person"))
						.osuTeam(rs.getShort("user_osu_team")).osuGrade(rs.getShort("user_osu_grade"))
						.isDD(rs.getShort("user_dd_flag")).build());
		return result.getResult();
	}

	@Override
	public void updateUserDao(User user) {
		kumaSqlDao.updateMode();
		kumaSqlDao.update("user_name", UpdateSetType.ASSIGN, user.getUsername())
				.update("user_osu_person", UpdateSetType.ASSIGN, user.getOsuPerson())
				.update("user_osu_team", UpdateSetType.ASSIGN, user.getOsuTeam())
				.update("user_osu_grade", UpdateSetType.ASSIGN, user.getOsuGrade())
				.update("user_dd_flag", UpdateSetType.ASSIGN, user.getIsDD()).table(User.class)
				.where("user_id", WhereType.EQUALS, user.getUserId()).end();
	}

	@Override
	public void updateUserPasswordDao(User user) {
		kumaSqlDao.updateMode();
		kumaSqlDao.update("user_password", UpdateSetType.ASSIGN, sha512(user.getUserEmail() + user.getUserPassword()))
				.table(User.class).where("user_id", WhereType.EQUALS, user.getUserId()).end();
	}
}
