package jwcms.test.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * <h1>日志拦截器</h1>
 */
public class LoggerInterceptor extends HandlerInterceptorAdapter {

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod hm = (HandlerMethod) handler;
			String uri = request.getRequestURI().replace(request.getContextPath(), "");
			Log LOGGER = LogFactory.getLog(hm.getBeanType());
			StringBuilder sb = new StringBuilder();
			sb.append(" URI : ").append(uri).append("\t").append(" Request Method : ").append(request.getMethod())
					.append("\t").append(" Class : ").append(hm.getBeanType().getName()).append("\t")
					.append(" Method : ").append(hm.getMethod().getName()).append("\t");

			if (ex != null) {
				sb.append(" Exception : ").append(ex);
				LOGGER.error(sb.toString());
			} else {
				sb.append(" Success.");
				LOGGER.info(sb.toString());
			}
		}
	}
}
