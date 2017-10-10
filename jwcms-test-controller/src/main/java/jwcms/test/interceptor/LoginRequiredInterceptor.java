package jwcms.test.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import jwcms.test.common.util.Constant;
import jwcms.test.common.util.JsonConvertUtils;
import jwcms.test.framework.ResponseEntity;

/**
 * <h1>登陆拦截器</h1>
 * 
 */
public class LoginRequiredInterceptor extends HandlerInterceptorAdapter {
	
	static final Log LOGGER = LogFactory.getLog(LoginRequiredInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String uri = request.getRequestURI();
		if (uri.indexOf("/ping") >= 0) {
			return true;
		}
		
		Long userId = null;
		// 验证是否已经登陆
		if (methodCanValidate(handler) && request.getAttribute(Constant.USERID_SESSION_KEY) == null) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setContentType("application/json;chartset=UTF-8");
			response.getWriter().print(JsonConvertUtils.toJson(ResponseEntity.errorToken()));
			LOGGER.info("Login Required Fail, User ID : " + userId);
			return false;
		}
		
		if (request.getAttribute(Constant.USERID_SESSION_KEY) != null) {
			userId = Long.parseLong(request.getAttribute(Constant.USERID_SESSION_KEY).toString());
		}
		
		LOGGER.info("Login Required Success, User ID : " + userId);
		return true;
	}

	private boolean methodCanValidate(Object handler) {
		if (handler instanceof HandlerMethod) {
			HandlerMethod hm = (HandlerMethod) handler;
			LoginRequired lr = hm.getMethod().getAnnotation(LoginRequired.class);
			if (lr != null) {
				return true;
			}
		}
		return false;
	}
}
