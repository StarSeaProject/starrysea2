package top.starrysea.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.beans.factory.annotation.Autowired;

import top.starrysea.kql.facede.KumaRedisDao;

@WebListener
public class SessionListener implements HttpSessionListener {

	@Autowired
	private KumaRedisDao kumaRedisDao;

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		kumaRedisDao.mapDel("shoppingcar", se.getSession().getId());
	}
}
