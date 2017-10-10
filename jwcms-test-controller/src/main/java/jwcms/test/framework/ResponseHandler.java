package jwcms.test.framework;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;

import jwcms.test.common.util.JsonConvertUtils;


/**
 * <h1>响应处理器</h1>
 * 
 */
public class ResponseHandler {
	
	private static final Log LOGGER = LogFactory.getLog(ResponseHandler.class);
	private static final String DEFAULT_ENCODING = "UTF-8";
	private static final String DEFAULT_CONTENT_TYPE = "application/json;chartset=UTF-8";

	private ResponseHandler() {
	}

	public static void response401(HttpServletResponse response) throws IOException {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(DEFAULT_CONTENT_TYPE);
		response.setCharacterEncoding(DEFAULT_ENCODING);
		response.getWriter().print(JsonConvertUtils.toJson(ResponseEntity.errorToken()));
		LOGGER.info("ResponseHandler.response401 Access Token is Empty.");
		response.getWriter().flush();
		response.getWriter().close();
	}

	public static void response(HttpServletResponse response, HttpStatus status, ResponseEntity<?> entity) throws IOException {
		response.setStatus(status.value());
		response.setContentType(DEFAULT_CONTENT_TYPE);
		response.setCharacterEncoding(DEFAULT_ENCODING);
		response.getWriter().print(JsonConvertUtils.toJson(entity));
		LOGGER.info("ResponseHandler.response Access Token is Empty.");
		response.getWriter().flush();
		response.getWriter().close();
	}

	public static void response(HttpServletResponse response, ResponseEntity<?> entity) throws IOException {
		response.setContentType(DEFAULT_CONTENT_TYPE);
		response.setCharacterEncoding(DEFAULT_ENCODING);
		response.getWriter().print(JsonConvertUtils.toJson(entity));
		LOGGER.info("BeforeRequestInterceptor.response entity.");
		response.getWriter().flush();
		response.getWriter().close();
	}
	
}
