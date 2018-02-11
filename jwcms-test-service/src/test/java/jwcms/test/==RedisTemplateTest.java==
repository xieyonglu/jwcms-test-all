package jwcms.test;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-redis-test.xml" })
public class RedisTemplateTest {
	
	@Resource(name = "redisTemplate")
	private RedisTemplate redisTemplate;

	@Test
	public void testSet() {
		try {
			redisTemplate.set("1234567", "7890", 100);
		} catch (RedisOperationException e) {
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void testGet() {
		try {
			System.out.println(redisTemplate.get("1234567"));
		} catch (RedisOperationException e) {
			System.out.println(e.getMessage());
		}
	}
}
