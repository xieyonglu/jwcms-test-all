package jwcms.test;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import jwcms.test.manager.UserManager;
import jwcms.test.model.User;

/**
 * 单元测试
 * @author yonglu.xie
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class UserServiceTest {
	
	@Resource
	private UserManager userManager;
	
	@Test
	public void queryUserById() throws Exception {
		User user = userManager.queryUserById(1L);
		System.out.println(user.getA() + "--" + user.getB());
	}
}
