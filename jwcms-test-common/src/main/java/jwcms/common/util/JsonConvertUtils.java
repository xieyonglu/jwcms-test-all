package jwcms.common.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import jwcms.common.exception.SystemException;

@SuppressWarnings("unchecked")
public class JsonConvertUtils {

	private static final Log LOGGER = LogFactory.getLog(JsonConvertUtils.class);

	private static ObjectMapper mapper = new ObjectMapper();
	
	private static final String ERROR_MESSAGE = "JSON转换出错!";

	private JsonConvertUtils() {
	}

	private static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	/**
	 * 把json字符串转成java对象
	 */
	public static <T> T toObject(String json, Class<T> objectClass) {
		if (isEmpty(json)) {
			return null;
		}

		try {
			return mapper.readValue(json.getBytes("utf-8"), objectClass);
		} catch (JsonParseException e) {
			LOGGER.error("JsonConvertUtils.toObject JsonParseException error, the error message is {}", e);
			throw new SystemException(ERROR_MESSAGE);
		} catch (UnrecognizedPropertyException e) {
			LOGGER.error("JsonConvertUtils.toObject UnrecognizedPropertyException error, the error message is {}", e);
			throw new SystemException(ERROR_MESSAGE);
		} catch (IOException e) {
			LOGGER.error("JsonConvertUtils.toObject IOException error, the error message is {}", e);
			throw new SystemException(ERROR_MESSAGE);
		}
	}

	public static Map<String, Object> toMap(String json) {
		if (isEmpty(json)) {
			return null;
		}

		try {
			JsonNode node = mapper.readTree(json);
			return mapper.convertValue(node, Map.class);
		} catch (IOException e) {
			LOGGER.error("JsonConvertUtils.toMap error, the error message is {}", e);
			throw new SystemException("");
		}
	}

	public static <T> List<T> toObjectList(String json, Class<T> objectClass) {
		if (isEmpty(json)) {
			return new LinkedList<>();
		}

		try {
			List<T> result = new ArrayList<>();
			JsonNode listNode = mapper.readTree(json);
			for (JsonNode objNode : listNode) {
				T t = mapper.convertValue(objNode, objectClass);
				result.add(t);
			}

			return result;
		} catch (JsonParseException e) {
			LOGGER.error("JsonConvertUtils.toObjectList error, the error message is {}", e);
			throw new SystemException(ERROR_MESSAGE);
		} catch (UnrecognizedPropertyException e) {
			LOGGER.error("JsonConvertUtils.toObjectList error, the error message is {}", e);
			throw new SystemException(ERROR_MESSAGE);
		} catch (IOException e) {
			LOGGER.error("JsonConvertUtils.toObjectList error, the error message is {}", e);
			throw new SystemException(ERROR_MESSAGE);
		}
	}

	/**
	 * 把对象转成json字符串
	 */
	public static String toJson(Object obj) {
		if (obj == null) {
			return null;
		}

		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			LOGGER.error("JsonConvertUtils.toJson error, the error message is {}", e);
			throw new SystemException(ERROR_MESSAGE);
		}
	}

}
