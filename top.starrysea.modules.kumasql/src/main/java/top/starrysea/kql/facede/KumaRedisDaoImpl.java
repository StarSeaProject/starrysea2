package top.starrysea.kql.facede;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import static top.starrysea.kql.common.Common.jsonToList;

@Component
public class KumaRedisDaoImpl implements KumaRedisDao {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private JedisPool jedisPool;

	@Override
	public <T> List<T> getList(String key, Class<T> clazz) {
		try (Jedis jedis = jedisPool.getResource();) {
			if (jedis.exists(key)) {
				return jsonToList(jedis.get(key), clazz);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return Collections.emptyList();
	}

	@Override
	public String set(String key, String value) {
		try (Jedis jedis = jedisPool.getResource();) {
			return jedis.set(key, value);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "";
	}

	@Override
	public Long delete(String key) {
		try (Jedis jedis = jedisPool.getResource();) {
			return jedis.del(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return 0L;
	}

}
