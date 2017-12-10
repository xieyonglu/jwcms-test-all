package jwcms.test.common.cache.redis.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.Response;

/**
 * redis专用返回结果对象
 * Created by lian on 15/5/30.
 */
public class RedisResult implements Serializable {

    private static final long serialVersionUID = -9076694561779996643L;
    private boolean success;
    private List<String> failKeys;
    private List<Object> failValues;
    private List<Response> responses;

    public static RedisResult wrapSuccessfulResult(List<Response> responses) {
        RedisResult result = new RedisResult();
        result.responses = responses;
        result.success = true;
        return result;
    }

    public static RedisResult wrapFailResult(List<String> failKeys) {
        RedisResult result = new RedisResult();
        result.failKeys = failKeys;
        result.success = false;
        return result;
    }

    public static RedisResult wrapFailResult(String key, Object value) {
        RedisResult result = wrapResult(key, value);
        result.success = false;
        return result;
    }

    private static RedisResult wrapResult(String key, Object value) {
        RedisResult result = new RedisResult();
        result.failKeys = new ArrayList<>();
        result.failKeys.add(key);
        result.failValues = new ArrayList<>();
        result.failValues.add(value);
        return result;
    }

    public static RedisResult wrapSuccessfulResult(String key, Object value) {
        RedisResult result = wrapResult(key, value);
        result.success = true;
        return result;
    }

	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param success the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * @return the failKeys
	 */
	public List<String> getFailKeys() {
		return failKeys;
	}

	/**
	 * @param failKeys the failKeys to set
	 */
	public void setFailKeys(List<String> failKeys) {
		this.failKeys = failKeys;
	}

	/**
	 * @return the failValues
	 */
	public List<Object> getFailValues() {
		return failValues;
	}

	/**
	 * @param failValues the failValues to set
	 */
	public void setFailValues(List<Object> failValues) {
		this.failValues = failValues;
	}

	/**
	 * @return the responses
	 */
	public List<Response> getResponses() {
		return responses;
	}

	/**
	 * @param responses the responses to set
	 */
	public void setResponses(List<Response> responses) {
		this.responses = responses;
	}
    
}
