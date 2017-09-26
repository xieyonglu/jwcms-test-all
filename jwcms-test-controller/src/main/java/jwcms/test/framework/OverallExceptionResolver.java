package jwcms.test.framework;

import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import jwcms.common.exception.ServiceException;
import jwcms.common.exception.SystemException;


/**
 * <h1>系统全局异常处理器</h1>
 * 
 */
public class OverallExceptionResolver implements HandlerExceptionResolver {

	static final Log LOGGER = LogFactory.getLog(OverallExceptionResolver.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		String className = OverallExceptionResolver.class.getName();
		String methodName = "afterCompletion";
		if (handler instanceof HandlerMethod) {
			HandlerMethod m = (HandlerMethod) handler;
			className = m.getBeanType().getName();
			methodName = m.getMethod().getName();
		}

		LOGGER.error(MessageFormat.format("{0}.{1} RESTFUL({2}) METHOD({3}) Error, Exception : {4}", className, methodName, request.getRequestURI(), request.getMethod(), ex.getMessage()));

		try {
			if (ex instanceof ServiceException) {
				ResponseEntity<?> responseEntity = ResponseEntity.fail(((ServiceException) ex).getErrorCode(), ex.getMessage());
				ResponseHandler.response(response, responseEntity);
			} else if (ex instanceof SystemException) {
				ResponseEntity<?> responseEntity = ResponseEntity.fail(((SystemException) ex).getErrorCode(), ex.getMessage());
				ResponseHandler.response(response, responseEntity);
			} else {
				ResponseEntity<?> responseEntity = ResponseEntity.error(ex.getMessage());
				ResponseHandler.response(response, responseEntity);
			}
		} catch (Exception e) {
			LOGGER.error("OverallExceptionResolver Handler Exception Error." + e);
		}

		return null;
	}

}
