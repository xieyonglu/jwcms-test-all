package jwcms.test.common.cache.redis.base;

import java.util.Collection;
import java.util.List;

/**
 * @author lianyuan
 * @since 15/12/21 11:07
 */
public interface Finder<V extends Identified<Long>> {
	
    /**
     * 根据 id(id必须是V的id) 集合查询
     */
    List<V> find(Collection<Long> ids);

    /**
     * 获取键值前缀
     */
    String getCacheKeyPrefix();
    
}