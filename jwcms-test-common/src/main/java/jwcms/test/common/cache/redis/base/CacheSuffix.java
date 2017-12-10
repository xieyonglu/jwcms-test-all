package jwcms.test.common.cache.redis.base;

/**
 * Created by lianyuan on 16/3/21.
 */
public interface CacheSuffix<K> {
	
	K getCacheKeySuffix();
	
}
