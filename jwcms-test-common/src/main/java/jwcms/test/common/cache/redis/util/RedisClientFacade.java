package jwcms.test.common.cache.redis.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import jwcms.test.common.cache.redis.RedisClientTemplate;
import jwcms.test.common.cache.redis.base.CacheSuffix;
import jwcms.test.common.cache.redis.base.Finder;
import jwcms.test.common.cache.redis.base.Identified;
import jwcms.test.common.cache.redis.base.ListFinder;
import jwcms.test.common.cache.redis.core.RedisDataSource;

/**
 * redis 门面类,对操作进行包装
 *
 * @author lianyuan
 * @since 15/12/17 10:52
 */
@Component
public class RedisClientFacade implements InitializingBean {

    protected static final Logger logger = LoggerFactory.getLogger(RedisClientFacade.class);

    private RedisClientTemplate redisClientTemplate;

    @Autowired
    private RedisDataSource redisDataSource;
//    @Autowired
//    private ThemisTyphonManager themisTyphonManager;

//  @Autowired
//  private CategoryManager categoryManager;

    Map<Class<?>, Finder> finderMap = new HashMap<>();

    Map<Class<?>, ListFinder> listFinderMap = new HashMap<>();
    //线程池配置 todo
    final ExecutorService executorService = Executors.newFixedThreadPool(5);

    /**
     * 一个id只有一条数据可以调用  key:value ->1:1
     */
    public <V extends Identified<Long>> List<V> queryList(Set<Long> ids, Class<V> clz) {
        if (ids.isEmpty()) {
            return Lists.newArrayList();
        }
        final Finder<V> finder = get(clz);
        try {
            final String keyPrefix = finder.getCacheKeyPrefix();
            Set<String> keys = Sets.newHashSet(Iterables.transform(ids, new Function<Long, String>() {
                @Override
                public String apply(Long integer) {
                    return keyPrefix + integer.toString();
                }
            }));
            final RedisClientFacade redisClientFacade = ((RedisClientFacade) AopContext.currentProxy());
            Map<String, V> cached = redisClientFacade.mgetByKeysClass(keys, clz);
            List<V> result = Lists.newArrayList(cached.values());
            Set<String> missedKey = Sets.symmetricDifference(cached.keySet(), Sets.newHashSet(keys));
            if (!missedKey.isEmpty()) {
                Set<Long> missedIdSet = Sets.newHashSet();
                for (String s : missedKey) {
                    missedIdSet.add(Long.valueOf(StringUtils.removeStart(s, finder.getCacheKeyPrefix())));
                }
                final List<V> fromDb = finder.find(missedIdSet);
                if (fromDb != null) {
                    result.addAll(fromDb);
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            for (V v : fromDb) {
                                redisClientFacade.setex(finder.getCacheKeyPrefix() + v.getId(), v, RedisConstant.DEFAULT_EXPIRATION_SECONDS_NUMBER);
                            }
                        }
                    });
                }
            }
            return result;
        } catch (Exception e) {
            logger.error("根据 id 集合获取 {}出错", clz, e);
            return finder.find(ids);
        }
    }

    /**
     * 一个id有n条数据可以调用 key:value ->1:n
     */
    public <K, V extends CacheSuffix> List<V> mgetList(Set<K> idKeys, Class<V> clz) {
        if (idKeys.isEmpty()) {
            return Lists.newArrayList();
        }
        final ListFinder<K, V> finder = mget(clz);
        try {
            final String keyPrefix = finder.getCacheKeyPrefix();

            final BiMap<K, String> keysMap = HashBiMap.create();
            for (K key : idKeys) {
                keysMap.put(key, keyPrefix + key);
            }
            Set<String> cacheKeySet = keysMap.values();
            final RedisClientFacade redisClientFacade = ((RedisClientFacade) AopContext.currentProxy());
            Multimap<String, V> cached = redisClientFacade.mgetByKeys(cacheKeySet, clz);
            List<V> result = new ArrayList<>();
            for (V v : cached.values()) {
                result.add(v);
            }
            Set<String> missedKey = Sets.symmetricDifference(cached.keySet(), cacheKeySet);

            if (!missedKey.isEmpty()) {
                Set<K> missedIdSet = Sets.newHashSet();
                for (String s : missedKey) {
                    missedIdSet.add(keysMap.inverse().get(s));
                }
                final List<V> fromDb = finder.find(missedIdSet);
                if (fromDb != null) {
                    result.addAll(fromDb);
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            Multimap<K, V> multimap = Toolbox.asMultimap(fromDb);
                            for (K key : multimap.keySet()) {
                                redisClientFacade.setex(keysMap.get(key), Lists.newArrayList(multimap.get(key)), finder.getExpirationSeconds());
                            }
                        }
                    });
                }
            }
            return result;
        } catch (Exception e) {
            logger.error("根据 idKeys{} 集合获取 {}出错", idKeys, clz);
            throw new RuntimeException("mget error", e);
        }
    }


    /**
     * 一个id有n条数据可以调用 key:value ->1:n
     */
    public <K, V extends CacheSuffix<K>> Multimap<K, V> mgetKey2valueListMap(Set<K> idKeys, Class<V> clz) {
        if (idKeys.isEmpty()) {
            return ArrayListMultimap.create();
        }
        final ListFinder<K, V> finder = mget(clz);
        try {
            final String keyPrefix = finder.getCacheKeyPrefix();
            final BiMap<K, String> keysMap = HashBiMap.create();
            for (K key : idKeys) {
                keysMap.put(key, keyPrefix + key);
            }
            Set<String> cacheKeySet = keysMap.values();
            final RedisClientFacade redisClientFacade = ((RedisClientFacade) AopContext.currentProxy());
            Multimap<String, V> cached = redisClientFacade.mgetByKeys(cacheKeySet, clz);
            Multimap<K, V> result = ArrayListMultimap.create();
            for (String key : cached.keySet()) {
                result.putAll(keysMap.inverse().get(key), cached.get(key));
            }
            Set<String> missedKey = Sets.symmetricDifference(cached.keySet(), cacheKeySet);

            if (!missedKey.isEmpty()) {
                Set<K> missedIdSet = Sets.newHashSet();
                for (String s : missedKey) {
                    missedIdSet.add(keysMap.inverse().get(s));
                }
                final List<V> fromDb = finder.find(missedIdSet);
                if (fromDb != null) {
                    final Multimap<K, V> multimap = Toolbox.asMultimap(fromDb);
                    result.putAll(multimap);
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            for (K key : multimap.keySet()) {
                                redisClientFacade.setex(keysMap.get(key), Lists.newArrayList(multimap.get(key)), finder.getExpirationSeconds());
                            }
                        }
                    });
                }
            }
            return result;
        } catch (Exception e) {
            logger.error("根据 idKeys{} 集合获取 {}出错", idKeys, clz);
            throw new RuntimeException("mget error", e);
        }
    }

//    @CatCacheTransaction
    public Object get(String key) {
    	return redisClientTemplate.get(key);
    }

//    @CatCacheTransaction
    public <V> Map<String, V> mgetByKeysClass(Set<String> keys, Class<V> clazz) {
        return redisClientTemplate.get(keys, clazz);
    }

//  @CatCacheTransaction
    public <V> Multimap<String, V> mgetByKeys(Set<String> keys, Class clazz) {
        return redisClientTemplate.mget(keys);
    }

//    @CatCacheTransaction
    public String set(String key, Object value) {
    	return redisClientTemplate.set(key, value);
//    	return themisTyphonManager.put(s, JSONObject.toJSONString(value));
    }

//  @CatCacheTransaction
    public String setex(String key, Object value, int expiration) {
        return redisClientTemplate.setex(key, value, expiration);
    }
    
//    @CatCacheTransaction
    public Long setnx(String key) {
        return redisClientTemplate.setnx(key, System.currentTimeMillis());
    }
    
//    @CatCacheTransaction
    public void expire(String key, int expiration) {
        redisClientTemplate.expire(key, expiration);
    }
    
//    @CatCacheTransaction
    public Long delByKey(String... keys) {
      return redisClientTemplate.del(keys);
    }

    @PostConstruct
    public void init() {

//        finderMapInit();

        listFinderMapInit();
    }

    private <V extends Identified<Long>> Finder<V> get(Class<V> clz) {
        Finder<V> finder = finderMap.get(clz);
        if (finder == null) {
            throw new IllegalArgumentException("找不到给定类" + clz + "的 Finder 实现");
        }
        return finder;
    }

    private <K, V extends CacheSuffix> ListFinder<K, V> mget(Class<V> clz) {
        ListFinder<K, V> finder = listFinderMap.get(clz);
        if (finder == null) {
            throw new IllegalArgumentException("找不到给定类" + clz + "的 Finder 实现");
        }
        return finder;
    }

    private void listFinderMapInit() {
    }

//    private void finderMapInit() {
//        finderMap.put(CategoryDO.class, new Finder<CategoryDO>() {
//            public List<CategoryDO> find(Collection<Long> ids) {
//                return categoryManager.findByIds(ids);
//            }
//
//            @Override
//            public String getCacheKeyPrefix() {
//                return RedisConstant.CATEGORY_KEY;
//            }
//        });
//    }

	@Override
    public void afterPropertiesSet() throws Exception {
        this.redisClientTemplate = new RedisClientTemplate(redisDataSource);
    }
}
