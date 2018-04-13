package top.starrysea.kql.facede;

import java.util.List;

public interface KumaRedisDao {

	<T> List<T> getList(String key, Class<T> clazz);

	String set(String key, String value);

	Long delete(String key);
}
