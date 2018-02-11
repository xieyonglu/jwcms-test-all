package jwcms.test.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jwcms.test.framework.ResponseEntity;
import jwcms.test.interceptor.LoginRequired;
import jwcms.test.manager.FunctionManager;
import jwcms.test.util.FunctionContainer;

/**
 * <h1>咨询服务Controller</h1>
 * 
 * @author yonglu.xie
 * @date 2018/02/10
 *
 */
@RestController
@RequestMapping("/jwcms/function")
public class FunctionController {
	
	@Resource
	private FunctionManager functionManager;
	
	
	@RequestMapping(value = "/load_function", method = RequestMethod.POST)
	@ResponseBody
	@LoginRequired
	public ResponseEntity<?> loadFunction(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
		functionManager.refreshLoadAllFunction();
		
		return ResponseEntity.success();
	}
	
	
	@RequestMapping(value = "/load_function_by_db", method = RequestMethod.POST)
	@ResponseBody
	@LoginRequired
	public ResponseEntity<?> loadFunctionByDB(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
		functionManager.loadFunctionByDb();
		
		return ResponseEntity.success();
	}
	
	
	@RequestMapping(value = "/load_function_by_zk", method = RequestMethod.POST)
	@ResponseBody
	@LoginRequired
	public ResponseEntity<?> loadFunctionByZK(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
		functionManager.loadFunctionByZk();
		
		return ResponseEntity.success();
	}
	
	
	@RequestMapping(value = "/load_function_by_cache", method = RequestMethod.POST)
	@ResponseBody
	@LoginRequired
	public ResponseEntity<?> loadFunctionByCache(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
		for(Map.Entry<String, String> entry : FunctionContainer.getFunctions().entrySet()) {
			System.out.println(entry.getKey() + "==" + entry.getValue());
		}
		
		return ResponseEntity.success();
	}
	
}
