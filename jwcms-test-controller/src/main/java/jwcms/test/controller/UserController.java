package jwcms.test.controller;

import java.util.LinkedList;
import java.util.List;

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
import jwcms.test.common.exception.ServiceException;
import jwcms.test.common.query.QueryResult;
import jwcms.test.criteria.UserCriteria;
import jwcms.test.framework.ResponseEntity;
import jwcms.test.interceptor.LoginRequired;
import jwcms.test.manager.UserManager;
import jwcms.test.model.User;
import jwcms.test.model.user.CreateUserRequest;
import jwcms.test.model.user.PageUserResponse;
import jwcms.test.model.user.QueryUserByIdResponse;
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
@Api(tags="用户接口服务", description="提供用户接口服务 ")
public class UserController {
	
	@Resource
	private UserManager userManager;
	
	
	@RequestMapping(value = "/create_user", method = RequestMethod.POST)
	@ResponseBody
	@LoginRequired
	@Transactional(rollbackFor = Exception.class)
	@ApiOperation(value="创建用户", notes="根据User对象创建用户", produces = "application/json")
    @ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User")
	public ResponseEntity<?> createUser(HttpServletRequest httpRequest, HttpServletResponse httpResponse, @Valid @RequestBody CreateUserRequest request, BindingResult br) throws Exception {
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
	@ApiOperation(value="删除用户", notes="根据用户Id来指定删除对象")
	@ApiImplicitParam(name = "userId", value = "用户Id", required = true, dataType = "Long")
	public ResponseEntity<?> removeUser(HttpServletRequest httpRequest, HttpServletResponse httpResponse, @PathVariable Long userId) throws Exception {
		userManager.removeUser(userId);
		
		return ResponseEntity.success();
	}
	
	
	@RequestMapping(value = "/update_user", method = RequestMethod.POST)
	@ResponseBody
	@LoginRequired
	@Transactional(rollbackFor = Exception.class)
	@ApiOperation(value="修改用户", notes="根据User对象修改用户", produces = "application/json")
    @ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User")
	public ResponseEntity<?> updateUser(HttpServletRequest httpRequest, HttpServletResponse httpResponse, @RequestBody UpdateUserRequest request) throws Exception {
		User user = new User();
		user.setId(request.getId());
		user.setA(request.getA());
		user.setB(request.getB());
		userManager.updateUser(user);
		
		return ResponseEntity.success();
	}
	
	
	@RequestMapping(value = "/query_user_by_id/{userId}", method = RequestMethod.GET)
	@ResponseBody
	@LoginRequired
	@ApiOperation(value="获取用户详细信息", notes="根据用户Id来获取用户详细信息", response = QueryUserByIdResponse.class)
	@ApiImplicitParam(name = "userId", value = "用户Id", required = true, dataType = "Long")
	public ResponseEntity<?> queryUserById(HttpServletRequest httpRequest, HttpServletResponse httpResponse, @PathVariable Long userId) throws Exception {
		User user = userManager.queryUserById(userId);
		
		//
		QueryUserByIdResponse response = new QueryUserByIdResponse();
		response.setId(user.getId());
		response.setA(user.getA());
		response.setB(user.getB());
		
		return ResponseEntity.success(response);
	}
	

	@RequestMapping(value = "/page_user", method = RequestMethod.GET)
	@ResponseBody
	@LoginRequired
	@ApiOperation(value="分页查询用户", notes="", response = PageUserResponse.class)
	public ResponseEntity<?> pageUser(HttpServletRequest httpRequest, HttpServletResponse httpResponse, UserCriteria criteria) throws Exception {
		QueryResult<User> queryResult = userManager.pageUser(criteria);
		
		//
		PageUserResponse response = new PageUserResponse();
		List<PageUserResponse.UserResponse> users = new LinkedList<>();
		queryResult.getResult().forEach(user -> {
			PageUserResponse.UserResponse newUser = response.new UserResponse();
			newUser.setId(user.getId());
			newUser.setA(user.getA());
			newUser.setB(user.getB());
			
			users.add(newUser);
		});
		response.setResult(users);
		response.setTotalCount(queryResult.getTotalCount());
		
		return ResponseEntity.success(response);
	}
	
}
