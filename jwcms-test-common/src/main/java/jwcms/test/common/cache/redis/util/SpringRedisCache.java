package jwcms.test.common.cache.redis.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.util.Assert;

/**
 * 使用{@link RedisClientFacade} 来存取缓存的{@link org.springframework.cache.Cache}实现
 */
public class SpringRedisCache extends AbstractValueAdaptingCache {

    private final Logger logger = LoggerFactory.getLogger(SpringRedisCache.class);


    private final String name;

    private int expiration;

    RedisClientFacade redisClientFacade;


    /**
     * Create a new ConcurrentMapCache with the specified name.
     *
     * @param name            the name of the cache
     * @param allowNullValues whether to accept and convert {@code null}
     *                        values for this cache
     */
    public SpringRedisCache(RedisClientFacade redisClientFacade, String name, boolean allowNullValues, int expiration) {
        this(name, allowNullValues);
        this.redisClientFacade = redisClientFacade;
        this.expiration = expiration;
    }

    /**
     * Create a new ConcurrentMapCache with the specified name and the
     * given internal {@link ConcurrentMap} to use.
     *
     * @param name            the name of the cache
     * @param allowNullValues whether to allow {@code null} values
     *                        (adapting them to an internal null holder value)
     */
    public SpringRedisCache(String name, boolean allowNullValues) {
        super(allowNullValues);
        Assert.notNull(name, "Name must not be null");
        this.name = name;
    }


    @Override
    public final String getName() {
        return this.name;
    }

    @Override
    public final Object getNativeCache() {
        return this.redisClientFacade;
    }

    /**
     * 获取给定 key 下的缓存值
     */
    @Override
    protected Object lookup(Object key) {
        try {
            Object object =  redisClientFacade.get(key.toString());
//            if(null != object){
//            	return JSONObject.parseObject((String) object, CategoryDO.class);
//            }
            return object;
        } catch (Exception e) {
            logger.error("redis error lookup: {}", key, e);
        }
        return null;
    }

    /**
     * 缓存给定键值的值对象
     */
    @Override
    public void put(Object key, Object value) {
        if (value == null && !isAllowNullValues()) {
            return;
        }
        if (expiration == 0) {
            redisClientFacade.set(key.toString(), value);
        } else {
            redisClientFacade.setex(key.toString(), value, expiration);
        }
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        throw new UnsupportedOperationException("putIfAbsent操作未实现!");
    }

    /**
     * 清除给定 key 的值
     */
    @Override
    public void evict(Object key) {
        try {
            redisClientFacade.delByKey(key.toString());
        } catch (Exception e) {
            logger.error("redis error evict: {}", key, e);
        }
    }

    /**
     * 清空缓存
     */
    @Override
    public void clear() {
        throw new UnsupportedOperationException("清空缓存操作未实现!");
    }

	@Override
	public <T> T get(Object key, Callable<T> valueLoader) {
		// TODO Auto-generated method stub
		return null;
	}

}
