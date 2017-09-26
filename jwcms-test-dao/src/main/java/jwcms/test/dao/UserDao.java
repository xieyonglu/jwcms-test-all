package jwcms.test.dao;

import java.util.List;

import jwcms.test.criteria.UserCriteria;
import jwcms.test.model.TUser;


/**
 * 数据访问对象接口
 * 
 * @since 2017-06-26
 */
public interface UserDao {

	/**
	 * 插入数据
	 * 
	 * @param User
	 * @return 插入数据的主键
	 */
	public Long createUser(TUser User);

	/**
	 * 删除记录
	 * 
	 * @param id
	 * @return 受影响的行数
	 */
	public Integer removeUser(Long UserId);

	/**
	 * 更新记录
	 * 
	 * @param User
	 * @return 受影响的行数
	 */
	public Integer updateUser(TUser User);

	/**
	 * 根据主键获取User
	 * 
	 * @param id
	 * @return User
	 */
	public TUser queryUserById(Long UserId);

	/**
	 * 统计记录数
	 * 
	 * @param User
	 * @return 查出的记录数
	 */
	public Long countUser(UserCriteria criteria);

	/**
	 * 获取对象列表
	 * 
	 * @param User
	 * @return 对象列表
	 */
	public List<TUser> queryUser(UserCriteria criteria);

}
