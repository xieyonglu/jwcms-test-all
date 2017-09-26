package jwcms.test.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import jwcms.common.exception.ServiceException;
import jwcms.common.query.QueryResult;
import jwcms.test.criteria.UserCriteria;
import jwcms.test.framework.ResponseEntity;
import jwcms.test.interceptor.LoginRequired;
import jwcms.test.manager.UserManager;
import jwcms.test.model.User;
import jwcms.test.model.user.CreateUserRequest;
import jwcms.test.model.user.UpdateUserRequest;

/**
 * <h1>咨询服务Controller</h1>
 * 
 * @author yonglu.xie
 * @date 2017/07/12
 *
 */
@RestController
@RequestMapping("/jwcms/user")
@Api(tags="微信公众平台接口服务", description="提供微信公众平台需要统一管理的接口服务 ")
public class UserController {
	
	@Resource
	private UserManager userManager;
	
	
	@RequestMapping(value = "/create_user", method = RequestMethod.POST)
	@ResponseBody
	@LoginRequired
	@Transactional(rollbackFor = Exception.class)
	@ApiOperation(value="创建用户", notes="根据User对象创建用户", produces = "application/json")
    @ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User")
	public ResponseEntity<?> createService(HttpServletRequest httpRequest, HttpServletResponse httpResponse, @Valid @RequestBody CreateUserRequest request, BindingResult br) throws Exception {
		if (br.hasErrors()) {
            System.out.println("Error -> " + br.getObjectName() + "--" + br.getFieldError().getDefaultMessage());
            throw new ServiceException(br.getFieldError().getDefaultMessage());
        }
		
		User user = new User();
		user.setA(request.getA());
		user.setB(request.getB());
		userManager.createUser(user);
		
		return ResponseEntity.success();
	}
	

	@RequestMapping(value = "/remove_user/{userId}", method = RequestMethod.POST)
	@ResponseBody
	@LoginRequired
	@Transactional(rollbackFor = Exception.class)
	@ApiOperation(value="删除用户", notes="根据url的id来指定删除对象")
	@ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Long")
	public ResponseEntity<?> removeService(HttpServletRequest httpRequest, HttpServletResponse httpResponse, @PathVariable Long userId) throws Exception {
		userManager.removeUser(userId);
		
		return ResponseEntity.success();
	}
	
	
	@RequestMapping(value = "/update_user", method = RequestMethod.POST)
	@ResponseBody
	@LoginRequired
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<?> updateService(HttpServletRequest httpRequest, HttpServletResponse httpResponse, @RequestBody UpdateUserRequest request) throws Exception {
		User user = new User();
		user.setId(request.getId());
		user.setA(request.getA());
		user.setB(request.getB());
		userManager.updateUser(user);
		
		return ResponseEntity.success();
	}
	

	@RequestMapping(value = "/page_user", method = RequestMethod.GET)
	@ResponseBody
	@LoginRequired
	@ApiOperation(value="获取用户列表", notes="")
	public ResponseEntity<?> pageService(HttpServletRequest httpRequest, HttpServletResponse httpResponse, UserCriteria criteria) throws Exception {
		QueryResult<User> queryResult = userManager.pageUser(criteria);
		
		return ResponseEntity.success(queryResult);
	}
	
	
	@RequestMapping(value = "/query_user_by_id/{userId}", method = RequestMethod.GET)
	@ResponseBody
	@LoginRequired
//	@ApiOperation(value="获取用户详细信息", notes="根据url的id来获取用户详细信息")
//	@ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Long")
	public ResponseEntity<?> queryServiceByServiceId(HttpServletRequest httpRequest, HttpServletResponse httpResponse, @PathVariable Long userId) throws Exception {
		User user = userManager.queryUserById(userId);
		
		return ResponseEntity.success(user);
	}
	
}
