package jwcms.test.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import jwcms.common.exception.ServiceException;
import jwcms.common.query.QueryResult;
import jwcms.test.converter.UserConverter;
import jwcms.test.criteria.UserCriteria;
import jwcms.test.dao.UserDao;
import jwcms.test.exception.UserExceptionCode;
import jwcms.test.manager.UserManager;
import jwcms.test.model.TUser;
import jwcms.test.model.User;



/**
 * <h1>咨询服务Manager实现类</h1>
 * 
 * @author yonglu.xie
 * @date 2017/06/28
 *
 */
@Service
public class UserManagerImpl implements UserManager {
	
	@Resource
	private UserDao userDao;
	
	@Resource
	private UserConverter userConverter;

	@Override
	public void createUser(User user) throws Exception {
		TUser tuser = userConverter.invert(user);
		userDao.createUser(tuser);
	}

	@Override
	public void removeUser(Long userId) throws Exception {
		userDao.removeUser(userId);
	}

	@Override
	public void updateUser(User user) throws Exception {
		TUser tuser = userConverter.invert(user);
		userDao.updateUser(tuser);
	}

	@Override
	public User queryUserById(Long userId) throws Exception {
		if(userId.compareTo(1L) == 0) {
			throw new ServiceException(UserExceptionCode.USER_NOT_FOUND);
		}
		
		TUser tuser = userDao.queryUserById(userId);
		return userConverter.convert(tuser);
	}

	@Override
	public List<User> queryUser(UserCriteria criteria) throws Exception {
		List<TUser> tusers =  userDao.queryUser(criteria);
		return userConverter.convertList(tusers);
	}

	@Override
	public Long countUser(UserCriteria criteria) throws Exception {
		return userDao.countUser(criteria);
	}

	@Override
	public QueryResult<User> pageUser(UserCriteria criteria) throws Exception {
		//
		List<User> users = this.queryUser(criteria);
		
		//
		Long totalCount = this.countUser(criteria);
		
		//
		QueryResult<User> queryResult = new QueryResult<User>();
		queryResult.setResult(users);
		queryResult.setTotalCount(totalCount);
		
		return queryResult;
	}
	
}
