package jwcms.test.common.cache.redis.common;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

/**
 * Created by lianyuan on 16/3/22.
 */
public class JsonRedisSerializerHandler {

	public static GenericJackson2JsonRedisSerializer getJsonRedisSerializer() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

		return new GenericJackson2JsonRedisSerializer(objectMapper);
	}

}
