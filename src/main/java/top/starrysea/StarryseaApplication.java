package top.starrysea;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.data.redis.RedisFlushMode;

@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 30 * 60, redisFlushMode = RedisFlushMode.IMMEDIATE)
public class StarryseaApplication {

	public static void main(String[] args) {
		SpringApplication.run(StarryseaApplication.class, args);
	}
}
