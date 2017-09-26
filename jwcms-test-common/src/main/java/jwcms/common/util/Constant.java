package jwcms.common.util;

/**
 * <h1>系统常量</h1>
 * 
 */
public class Constant {

	/**
	 * token在http header中的key
	 */
	public static final String TOKEN_HEADER_NAME = "x-token";

	/**
	 * User-Agent在http header中的key
	 */
	public static final String USER_AGENT_HEADER_NAME = "User-Agent";

	/**
	 * work code在http header中key
	 */
	public static final String WORK_CODE_HEADER_NAME = "x-work-code";

	/**
	 * token验证超时秒数
	 */
	public static final Integer TOKEN_VALIDATION_TIMEOUT = 3;

	/**
	 * 用户ID在session中的key
	 */
	public static final String USERID_SESSION_KEY = "__USER_ID__";
	
	/**
	 * token在session中的key
	 */
	public static final String TOKEN_SESSION_KEY = "X-TOKEN";
	
}
