package jwcms.test.common.cache.redis.util;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.cache.transaction.TransactionAwareCacheDecorator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lianyuan
 * @since 15/12/21 11:07
 */
//@Configuration
//@EnableCaching
public class CacheConfig {

	@Bean
	@Autowired
	public CacheManager cacheManager(RedisClientFacade redisClientFacade) {
		SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
		// 事务内的@CachePut,@CacheEvict,@Cacheable 对redis的修改操作,统一在事务成功提交后执行
		Cache springRedisCache = new TransactionAwareCacheDecorator(new SpringRedisCache(redisClientFacade, "test", false, RedisConstant.DEFAULT_EXPIRATION_SECONDS_NUMBER));

		simpleCacheManager.setCaches(Arrays.asList(springRedisCache));
		return simpleCacheManager;
	}

}
