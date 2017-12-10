package jwcms.test.common.cache.redis.base;

import java.util.List;
import java.util.Set;

/**
 * @author lianyuan
 * @since 15/12/24 14:37
 */
public interface ListFinder<K, V extends CacheSuffix> {

	/**
	 * 根据 id 集合查询(此id是V的除ID之外的任一字段或者字段的组合)
	 */
	List<V> find(Set<K> ids);

	/**
	 * 获取键值前缀
	 */
	String getCacheKeyPrefix();

	/**
	 * 设置key的失效时间 单位秒
	 */
	int getExpirationSeconds();

}
