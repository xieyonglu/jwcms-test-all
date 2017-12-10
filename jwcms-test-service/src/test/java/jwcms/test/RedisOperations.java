package jwcms.test;

import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;

public interface RedisOperations {
	/**
	 * Details：执行回调服务
	 */
	<T> T execute(ConnectionCallback<T, Jedis> action) throws RedisOperationException;

	/**
	 * Details：设置key-value
	 */
	String set(final String key, final String value) throws RedisOperationException;

	/**
	 * Details：设置键值对，并设置该键的过期时间
	 */
	String set(final String key, final String value, final int expireTime) throws RedisOperationException;

	/**
	 * Details：获取指定键对应的值
	 */
	String get(final String key) throws RedisOperationException;

	/**
	 * Details：删除指定的键
	 */
	Long del(final String key) throws RedisOperationException;

	/**
	 * Details：获取key值的集合
	 */
	Set<String> keys(final String keyPattern) throws RedisOperationException;

	/**
	 * Details：判断当前key是否存在
	 */
	boolean exist(final String key) throws RedisOperationException;

	/**
	 * attention:list分页操作
	 */
	List<String> lrange(final String key, final long start, final long end) throws RedisOperationException;

	/**
	 * Details：列表添加元素
	 */
	long rpush(final String key, final String... values) throws RedisOperationException;
}