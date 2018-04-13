package top.starrysea.kql.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import top.starrysea.kql.entity.Entity;

public class Common {

	private static final Logger logger = LoggerFactory.getLogger(Common.class);
	private static final ObjectMapper mapper = new ObjectMapper();
	
	// 私有构造器防止外部创建新的Util对象
	private Common() {
	}

	public static boolean isNotNull(Object object) {
		boolean result = false;
		if (object == null)
			return result;
		if (object instanceof String) {
			String temp = (String) object;
			if (!temp.equals(""))
				result = true;
			else
				result = false;
		} else if (object instanceof Entity) {
			result = true;
		} else if (object instanceof Integer) {
			int i = (int) object;
			if (i == 0)
				result = false;
			else
				result = true;
		} else if (object instanceof Short) {
			short i = (short) object;
			if (i == 0)
				result = false;
			else
				result = true;
		} else if (object instanceof List) {
			List<?> l = (List<?>) object;
			result = !l.isEmpty();
		}
		return result;
	}

	public static String pojo2table(String pojoName) {
		List<Integer> uppercaseIndex = new ArrayList<>();
		StringBuilder tableName = new StringBuilder();
		for (int i = 1; i < pojoName.length(); i++) {
			if (Character.isUpperCase(pojoName.charAt(i))) {
				uppercaseIndex.add(i);
			}
		}
		tableName.append(Character.toLowerCase(pojoName.charAt(0)));
		if (!uppercaseIndex.isEmpty()) {
			for (Integer index : uppercaseIndex) {
				tableName.append(pojoName.substring(1, index));
				tableName.append("_" + Character.toLowerCase(pojoName.charAt(index)));
			}
			tableName.append(pojoName.substring(uppercaseIndex.get(uppercaseIndex.size() - 1) + 1));
		} else {
			tableName.append(pojoName.substring(1));
		}
		return tableName.toString();
	}
	
	public static <T> List<T> jsonToList(String json, Class<T> clazz) {
		JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, clazz);
		try {
			return mapper.readValue(json, javaType);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return Collections.emptyList();
	}
}
