package top.starrysea.kql.facede;

import java.util.List;
import java.util.Map;

public interface KumaRedisDao {

	<T> List<T> getList(String key, Class<T> clazz);

	String set(String key, String value);

	String set(String key, String value, long second);

	Long delete(String key);

	void mapSet(String hashKey, String key, String value);

	void mapDel(String hashKey, String... key);

	Map<String, String> mapGetAll(String hashKey);
}
