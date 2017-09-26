package jwcms.test;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import jwcms.test.manager.UserManager;
import jwcms.test.model.User;

/**
 * 单元测试
 * @author yonglu.xie
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext2.xml"})
public class UserServiceTest2 {
	
	@Resource
	private UserManager userManager;
	
	@Test
	public void queryUserById() throws Exception {
		User user = userManager.queryUserById(1L);
		System.out.println(user.getA() + "--" + user.getB());
	}
}
