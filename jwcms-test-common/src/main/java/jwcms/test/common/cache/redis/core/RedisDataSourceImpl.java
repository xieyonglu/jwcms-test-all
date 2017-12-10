package jwcms.test.common.cache.redis.core;

import redis.clients.jedis.Jedis;

/**
 * redis数据源实现
 * Created by lianyuan on 2015/10/10.
 */
public class RedisDataSourceImpl implements RedisDataSource {

    private SentinelMasterSlavePool jedisSentinelPool;

    @Override
    public Jedis getRedisWriteClient() {
        return jedisSentinelPool.getWriteResource();
    }

    @Override
    public Jedis getRedisReadClient() {
        return jedisSentinelPool.getReadResource();
    }

    @Override
    public void resetReadPool() {
        jedisSentinelPool.resetReadPool();
    }

	/**
	 * @return the jedisSentinelPool
	 */
	public SentinelMasterSlavePool getJedisSentinelPool() {
		return jedisSentinelPool;
	}

	/**
	 * @param jedisSentinelPool the jedisSentinelPool to set
	 */
	public void setJedisSentinelPool(SentinelMasterSlavePool jedisSentinelPool) {
		this.jedisSentinelPool = jedisSentinelPool;
	}
    
}
