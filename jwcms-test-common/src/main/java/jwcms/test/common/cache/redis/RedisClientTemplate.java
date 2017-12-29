package jwcms.test.common.cache.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import com.google.common.base.Charsets;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import jwcms.test.common.cache.redis.common.JsonRedisSerializerHandler;
import jwcms.test.common.cache.redis.core.RedisDataSource;
import jwcms.test.common.cache.redis.exception.RedisException;
import redis.clients.jedis.Jedis;

/**
 * 外部直接调用的redis入口 Created by lianyuan on 2015/9/16.
 */
@SuppressWarnings("unchecked")
public class RedisClientTemplate {

	@Resource
    private RedisDataSource redisDataSource;

    private final GenericJackson2JsonRedisSerializer serializer = JsonRedisSerializerHandler.getJsonRedisSerializer();

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisClientTemplate.class);

    public RedisClientTemplate(RedisDataSource redisDataSource) {
        this.redisDataSource = redisDataSource;
    }

    private final byte[] NX_BYTES = "NX".getBytes(Charsets.UTF_8);
    private final byte[] EX_BYTES = "EX".getBytes(Charsets.UTF_8);

    /**
     * 设置单个值
     */
    public String set(String key, Object value) {
        Jedis jedis = getRedisWriteClient();
        try {
            return jedis.set(key.getBytes(Charsets.UTF_8), serializer.serialize(value));
        } catch (Exception e) {
            throw new RedisException(e);
        } finally {
            jedis.close();
        }
    }
    
    /**
     * 设置单个值,加上超时时间
     */
    public String setex(String key, Object value, int expiration) {
        Jedis jedis = getRedisWriteClient();
        try {
            return jedis.setex(key.getBytes(Charsets.UTF_8), expiration, serializer.serialize(value));
        } catch (Exception e) {
            throw new RedisException(e);
        } finally {
            jedis.close();
        }
    }

    /**
     * 如果不存在,则设置单个值,加上超时时间
     */
    public String setnxex(String key, Object value, int expiration) {
        Jedis jedis = getRedisWriteClient();
        try {
            return null;//jedis.set(key.getBytes(Charsets.UTF_8), serializer.serialize(value), /*NX_BYTES, EX_BYTES,*/ expiration);
        } catch (Exception e) {
            throw new RedisException(e);
        } finally {
            jedis.close();
        }
    }

    public Long setnx(String key, Object value) {
        Jedis jedis = getRedisWriteClient();
        try {
            return jedis.setnx(key.getBytes(Charsets.UTF_8), serializer.serialize(value));
        } catch (Exception e) {
            throw new RedisException(e);
        } finally {
            jedis.close();
        }
    }

    public void expire(String key, int expiration) {
        Jedis jedis = getRedisWriteClient();
        try {
            jedis.expire(key.getBytes(Charsets.UTF_8), expiration);
        } catch (Exception e) {
            throw new RedisException(e);
        } finally {
            jedis.close();
        }
    }

//    /**
//     * 批量设置
//     */
//
//    public void set(List<String> keys, List<Object> values) {
//        Jedis jedis = getRedisWriteClient();
//        try {
//            Pipeline p = jedis.pipelined();
//            int size = keys.size();
//            for (int i = 0; i < size; i++) {
//                p.set(keys.get(i).getBytes(), serializer.serialize(values.get(i)));
//            }
//            p.sync();
//        } catch (Exception e) {
//            throw new RedisException(e);
//        } finally {
//            jedis.close();
//        }
//    }

    private Jedis getRedisReadClient() {
        Jedis jedis = redisDataSource.getRedisReadClient();
        if (jedis == null) {
            throw new RedisException("readRedis can not connect");
        }
        printHap(jedis);
        return jedis;
    }

    private void printHap(Jedis jedis) {
        LOGGER.debug(jedis.getClient().getHost() + ":" + jedis.getClient().getPort());
    }

    private Jedis getRedisWriteClient() {
        Jedis jedis = redisDataSource.getRedisWriteClient();
        if (jedis == null) {
            throw new RedisException("writeRedis can not connect");
        }
        printHap(jedis);
        return jedis;
    }

    /**
     * 设置单个值,带过期时间
     */

    public String psetex(String key, long milliseconds, Object value) {
        Jedis jedis = getRedisWriteClient();
        try {
            //其他方法全都是用的serializer,那么这里也要用serializer
            return jedis.psetex(key.getBytes(Charsets.UTF_8), milliseconds, serializer.serialize(value));
        } catch (Exception e) {
            throw new RedisException(e);
        } finally {
            jedis.close();
        }
    }

    /**
     * 获取单个值
     */

    public String get(String key) {
        return get(key, String.class);
    }

    /**
     * 获取单个对象
     */

    public <V> V get(String key, Class<V> clazz) {
        byte[] result;
        Jedis jedis = getRedisReadClient();
        try {
            result = jedis.get(key.getBytes(Charsets.UTF_8));
            if (result != null) {
                return serializer.deserialize(result, clazz);
            }
        } catch (Exception e) {
            throw new RedisException(e);
        } finally {
            jedis.close();
        }
        return null;
    }


    /**
     * 批量查询指定对象
     */

    public <V> Map<String, V> get(Set<String> keys, Class<V> clazz) {
        if (keys.isEmpty()) {
            return Maps.newHashMap();
        }
        Jedis jedis = getRedisReadClient();
        List<String> keysList = new ArrayList<>(keys);
        try {
            Map<String, V> result = new HashMap<>();

            List<String> valueList = jedis.mget(keysList.toArray(new String[keysList.size()]));

            for (int i = 0; i < keysList.size(); i++) {
                if (valueList.get(i) != null) {
                    result.put(keysList.get(i), serializer.deserialize(valueList.get(i).getBytes(Charsets.UTF_8), clazz));
                }
            }
            return result;
        } catch (Exception e) {
            throw new RedisException(e);
        } finally {
            jedis.close();
        }
    }


	public <V> Multimap<String, V> mget(Set<String> keys) {
        if (keys.isEmpty()) {
            return ArrayListMultimap.create();
        }
        Jedis jedis = getRedisReadClient();
        List<String> keysList = new ArrayList<>(keys);
        try {
            Multimap<String, V> result = ArrayListMultimap.create();

            List<String> valueList = jedis.mget(keysList.toArray(new String[keysList.size()]));

            for (int i = 0; i < keysList.size(); i++) {
                if (valueList.get(i) != null) {
                    result.putAll(keysList.get(i), serializer.deserialize(valueList.get(i).getBytes(Charsets.UTF_8), List.class));
                }
            }
            return result;
        } catch (Exception e) {
            throw new RedisException(e);
        } finally {
            jedis.close();
        }
    }

    /**
     * 删除,支持批量删除
     */
    public Long del(final String... keys) {
        Jedis jedis = getRedisWriteClient();
        try {
            return jedis.del(keys);
        } catch (Exception e) {
            throw new RedisException(e);
        } finally {
            jedis.close();
        }
    }

    /**
     * 存map
     */

    public void setMap(String key, Map<String, String> map) {
        Jedis jedis = getRedisWriteClient();
        try {
            jedis.hmset(key, map);
        } catch (Exception e) {
            throw new RedisException(e);
        } finally {
            jedis.close();
        }
    }

    /**
     * 获取map
     */

    public Map<String, String> getMap(String key) {
        Jedis jedis = getRedisReadClient();
        try {
            return jedis.hgetAll(key);
        } catch (Exception e) {
            throw new RedisException(e);
        } finally {
            jedis.close();
        }
    }

//    /**
//     * 批量删除
//     *
//     * @param keys
//     * @return
//     */
//
//    public Long del(List<String> keys) {
//        Jedis jedis = getRedisWriteClient();
//        Long result = 0L;
//        List<Response<Long>> responses = new ArrayList<>(keys.size());
//        try {
//            Pipeline p = jedis.pipelined();
//            for (String key : keys) {
//                responses.add(p.del(key));
//            }
//            p.sync();
//
//            for (Response<Long> response : responses) {
//                result += response.get();
//            }
//
//        } catch (Exception e) {
//            throw new RedisException(e);
//        } finally {
//            jedis.close();
//        }
//
//        return result;

//    }

//    /**
//     * 删除map中的field
//     *
//     * @param key
//     * @return
//     */
//
//    public Long hdel(String key, String... fields) {
//        Jedis jedis = getRedisWriteClient();
//        try {
//            return jedis.hdel(key, fields);
//        } catch (Exception e) {
//            throw new RedisException(e);
//        } finally {
//            jedis.close();
//        }
//    }

//
//    public void sadd(String key, boolean reset, String... fields) {
//        Jedis jedis = getRedisWriteClient();
//        try {
//            Pipeline pipeline = jedis.pipelined();
//            if (reset) {
//                pipeline.del(key);
//            }
//            pipeline.sadd(key, fields);
//            pipeline.sync();
//        } catch (Exception e) {
//            throw new RedisException(e);
//        } finally {
//            jedis.close();
//        }
//    }


//    public <V> Map<String, V> hget(List<String> keys, List<String> fields, Class<V> clazz) {
//        Jedis jedis = getRedisReadClient();
//        try {
//            Pipeline p = jedis.pipelined();
//            Map<String, Response<String>> responses = new HashMap<>(keys.size());
//            for (int i = 0; i < keys.size(); i++) {
//                String key = keys.get(i);
//                String field = fields.get(i);
//                responses.put(key + ":" + field, p.hget(key, field));
//            }
//            p.sync();
//
//            Map<String, V> result = new HashMap<>();
//            for (String key : responses.keySet()) {
//                String value = responses.get(key).get();
//                if (value != null) {
//                    result.put(key, serializer.deserialize(value.getBytes(Charsets.UTF_8), clazz));
//                }
//            }
//            return result;
//        } catch (Exception e) {
//            throw new RedisException(e);
//        } finally {
//            jedis.close();
//        }
//    }

//
//    public void hset(List<String> keys, List<String> fields, List<?> values, int second) {
//        Jedis jedis = getRedisWriteClient();
//        try {
//            Pipeline p = jedis.pipelined();
//            int size = keys.size();
//            for (int i = 0; i < size; i++) {
//                p.hset(keys.get(i).getBytes(Charsets.UTF_8), fields.get(i).getBytes(Charsets.UTF_8), serializer.serialize(values.get(i)));
//                p.expire(keys.get(i).getBytes(Charsets.UTF_8), second);
//            }
//            p.sync();
//        } catch (Exception e) {
//            throw new RedisException(e);
//        } finally {
//            jedis.close();
//        }
//    }
}
