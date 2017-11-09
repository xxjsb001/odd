package com.vtradex.wms.server.esbUtils;



import java.io.InputStream;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHelper {

	private static ObjectMapper objectMapper = new ObjectMapper();

	static {
		// 忽略字符串中不能识别的属性
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public static ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public static <T> T parseToObject(InputStream is, Class<T> toClass) {
		try {
			return objectMapper.readValue(is, toClass);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static <T> T parseToObject(byte[] b, int offset, int len, Class<T> valueType) {
		try {
			return objectMapper.readValue(b, offset, len, valueType);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static <T> T parseToObject(String json, Class<T> toClass) {
		try {
			return objectMapper.readValue(json, toClass);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 此方法可以用于复杂对象比如，List<Account>，其他方法返回的则是List<Map>
	 */
	public static <T> T parseToObject(String json, TypeReference<T> type) {
		try {
			return objectMapper.readValue(json, type);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static Map parseToMap(String json) {
		return parseToObject(json, Map.class);
	}

	public static Map parseToMapStrStr(String json) {
		return parseToObject(json, new TypeReference<Map<String, String>>() {
		});
	}

	public static Map parseToMap(byte[] b) {
		if (b == null || b.length == 0) {
			return null;
		}
		return parseToObject(b, 0, b.length, Map.class);
	}

	public static Map parseToMap(InputStream is) {
		return parseToObject(is, Map.class);
	}

	public static String parseToJson(Object o) {
		if (o == null) {
			return null;
		}
		try {
			return objectMapper.writeValueAsString(o);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
