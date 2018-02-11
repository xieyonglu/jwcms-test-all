package jwcms.test;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

@Service("redisTemplate")
public class RedisTemplate implements RedisOperations {
	
	@Resource(name = "redisSentinel")
	private JedisSentinelPool jedisSentinelPool;

	@Override
	public <T> T execute(ConnectionCallback<T, Jedis> action) throws RedisOperationException {
		Jedis jedis = null;
		try {
			// 从连接池中获取jedis的sentinel资源
			jedis = jedisSentinelPool.getResource();
			// 执行回调方法
			return action.doInConnection(jedis);
		} catch (Exception e) {
			throw new RedisOperationException(e, "Redis Service Failed!");
		} finally {
			if (null != jedis) {
				jedis.close();
			}
		}
	}

	@Override
	public boolean exist(final String key) throws RedisOperationException {
		return execute(new ConnectionCallback<Boolean, Jedis>() {
			@Override
			public Boolean doInConnection(Jedis jedis) throws RedisOperationException {
				return jedis.exists(key);
			}
		});
	}

	@Override
	public String set(final String key, final String value) throws RedisOperationException {
		return execute(new ConnectionCallback<String, Jedis>() {
			@Override
			public String doInConnection(Jedis jedis) throws RedisOperationException {
				try {
					return jedis.set(key, value);
				} catch (Exception e) {
					throw new RedisOperationException(e, "write to redis failed!key:" + key + " value:" + value);
				}
			}
		});
	}

	@Override
	public String set(final String key, final String value, final int expireTime) throws RedisOperationException {
		return execute(new ConnectionCallback<String, Jedis>() {
			@Override
			public String doInConnection(Jedis jedis) throws RedisOperationException {
				try {
					String result = jedis.set(key, value);
					jedis.expire(key, expireTime);
					return result;
				} catch (Exception e) {
					throw new RedisOperationException(e, "write to redis failed!key:" + key + " value:" + value);
				}
			}
		});
	}

	@Override
	public String get(final String key) throws RedisOperationException {
		return execute(new ConnectionCallback<String, Jedis>() {
			@Override
			public String doInConnection(Jedis jedis) throws RedisOperationException {
				boolean isExist = exist(key);
				if (isExist) {
					return jedis.get(key);
				}
				return null;
			}
		});
	}

	@Override
	public Long del(final String key) throws RedisOperationException {
		return execute(new ConnectionCallback<Long, Jedis>() {
			@Override
			public Long doInConnection(Jedis jedis) throws RedisOperationException {
				boolean isExist = exist(key);
				if (isExist) {
					return jedis.del(key);
				}
				return null;
			}
		});
	}

	@Override
	public Set<String> keys(final String keyPattern) throws RedisOperationException {
		return execute(new ConnectionCallback<Set<String>, Jedis>() {
			@Override
			public Set<String> doInConnection(Jedis jedis) {
				Set<String> sets = jedis.keys(keyPattern);
				return sets;
			}
		});
	}

	@Override
	public List<String> lrange(final String key, final long start, final long end) throws RedisOperationException {
		return execute(new ConnectionCallback<List<String>, Jedis>() {
			@Override
			public List<String> doInConnection(Jedis jedis) throws RedisOperationException {
				return jedis.lrange(key, start, end);
			}
		});
	}

	@Override
	public long rpush(final String key, final String... values) throws RedisOperationException {
		return execute(new ConnectionCallback<Long, Jedis>() {
			@Override
			public Long doInConnection(Jedis jedis) throws RedisOperationException {
				return jedis.rpush(key, values);
			}
		});
	}
}
