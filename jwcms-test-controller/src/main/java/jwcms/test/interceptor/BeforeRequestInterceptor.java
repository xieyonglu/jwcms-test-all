package jwcms.test.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import jwcms.common.util.Constant;
import jwcms.common.util.JsonConvertUtils;
import jwcms.common.util.StringUtils;
import jwcms.test.framework.ResponseEntity;

/**
 * <h1>所有请求拦截器</h1>
 */
public class BeforeRequestInterceptor extends HandlerInterceptorAdapter {
	
	static final Log LOGGER = LogFactory.getLog(BeforeRequestInterceptor.class);
	
	//@Autowired
	//private UserService userService;
	
	static final String MASTER_TOKEN = "q1w2e3r4t5y6u7i8o9p0";
	
	static final Long MASTER_USER_ID = 100L;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String uri = request.getRequestURI();
		if (uri.indexOf("/ping") >= 0) {
			return true;
		}
		
		if (RequestMethod.OPTIONS.toString().equals(request.getMethod())) {
			return false;
		}
		
		if (!methodCanValidate(handler)) {
			return true;
		}
		String token = request.getHeader(Constant.TOKEN_HEADER_NAME);
		if (StringUtils.isEmpty(token)) {
			token = request.getParameter("token");
		}
		
		if (StringUtils.isEmpty(token)) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setContentType("application/json;chartset=UTF-8");
			response.getWriter().print(JsonConvertUtils.toJson(ResponseEntity.errorToken()));
			return false;
		}
		
		if (MASTER_TOKEN.equals(token)/* && !ProjectProperties.isPro()*/) {
			request.setAttribute(Constant.USERID_SESSION_KEY, MASTER_USER_ID);
			request.setAttribute(Constant.TOKEN_SESSION_KEY, MASTER_TOKEN);
			LOGGER.info("Master Token : " + token);
			return true;
		}
		
		if (StringUtils.isNotEmpty(token)) {
			try {
				Long userId = 100L;//userService.getUserIdByToken(token);
				request.setAttribute(Constant.USERID_SESSION_KEY, userId);
				request.setAttribute(Constant.TOKEN_SESSION_KEY, token);
			} catch (Exception e) {
				LOGGER.error("Exception : " + e);
			}
		}
		LOGGER.info("Before Request Success : " + token);
		return true;
	}

	private boolean methodCanValidate(Object handler) {
		if (handler instanceof HandlerMethod) {
			HandlerMethod hm = (HandlerMethod) handler;
			LoginRequired lr = hm.getMethod().getAnnotation(LoginRequired.class);
			//CarrierRequired cr = hm.getMethod().getAnnotation(CarrierRequired.class);
			//DriverRequired dr = hm.getMethod().getAnnotation(DriverRequired.class);
			if (lr != null/* || cr != null || dr != null*/) {
				return true;
			}
		}
		return false;
	}
}
