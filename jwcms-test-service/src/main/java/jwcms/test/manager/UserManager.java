package jwcms.test.manager;

import java.util.List;

import jwcms.test.common.query.QueryResult;
import jwcms.test.criteria.UserCriteria;
import jwcms.test.model.User;


/**
 * <h1>咨询Service Manager</h1>
 * 
 * @author yonglu.xie
 * @date 2017/06/26
 *
 */
public interface UserManager {
	
	
	public void createUser(User user) throws Exception;
	
	
	public void removeUser(Long serviceId) throws Exception;
	
	
	public void updateUser(User user) throws Exception;
	
	
	public User queryUserById(Long serviceId) throws Exception;
	
	
	public List<User> queryUser(UserCriteria criteria) throws Exception;
	
	
	public Long countUser(UserCriteria criteria) throws Exception;
	
	
	public QueryResult<User> pageUser(UserCriteria criteria) throws Exception;
	

}
