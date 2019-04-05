package top.starrysea;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class CacheConfiguration {
	@Value("${spring.redis.host}")
	private String host;

	@Value("${spring.redis.port}")
	private int port;

	@Value("${spring.redis.password}")
	private String password;

	@Value("${spring.redis.jedis.pool.max-active}")
	private String maxActive;

	@Value("${spring.redis.jedis.pool.max-idle}")
	private String maxIdle;

	@Value("${spring.redis.jedis.pool.max-wait}")
	private long maxWaitMillis;

	@Value("${spring.redis.jedis.pool.min-idle}")
	private String minIdle;

	@Value("${spring.redis.timeout}")
	private int timeout;

	@Bean
	public JedisPool redisPoolFactory() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(Integer.parseInt(maxIdle));
		jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
		jedisPoolConfig.setMinIdle(Integer.parseInt(minIdle));
		jedisPoolConfig.setMaxTotal(Integer.parseInt(maxActive));
		return new JedisPool(jedisPoolConfig, host, port, timeout, password);
	}

	@Bean
	public CacheManager defaultCacheManager() {
		return new ConcurrentMapCacheManager();
	}
}
