package jwcms.test.common.cache.redis.core;

import redis.clients.jedis.Jedis;

/**
 * redis数据源接口
 * Created by lianyuan on 2015/10/10.
 */
public interface RedisDataSource {

    Jedis getRedisWriteClient();

    Jedis getRedisReadClient();

    void resetReadPool();
}
