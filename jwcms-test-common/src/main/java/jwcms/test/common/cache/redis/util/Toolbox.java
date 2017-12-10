package jwcms.test.common.cache.redis.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import jwcms.test.common.cache.redis.base.CacheSuffix;
import jwcms.test.common.cache.redis.base.Identified;


/**
 * @author lianyuan
 * @since 15/11/19 20:05
 */
public abstract class Toolbox {

    /**
     * 获取第一个值不为null 的元素
     */
    public static <E> E firstNonNull(E... list) {
        for (E e : list) {
            if (e != null) {
                return e;
            }
        }
        return null;
    }

    /**
     * 将入参中所有不为 null 元素收集起来
     */
    public static <E> List<E> collectNonNull(E... list) {
        List<E> data = new ArrayList<>();
        for (E e : list) {
            if (e != null) {
                data.add(e);
            }
        }
        return data;
    }

    public static <E> List<E> filter(Predicate<E> predicate, E... list) {
        List<E> data = new ArrayList<>();
        for (E e : list) {
            if (predicate.apply(e)) {
                data.add(e);
            }
        }
        return data;
    }

    /**
     * @param iterable 集合
     * @param <K>      id 的类型
     * @param <E>      集合中元素类型
     * @return 从 id 到元素的映射
     */
    public static <K, E extends Identified<K>> Map<K, E> asMap(Iterable<E> iterable) {
        return Maps.uniqueIndex(iterable, new Function<E, K>() {
            @Override
            public K apply(E e) {
                return e.getId();
            }
        });
    }
    
    public static <K, V extends CacheSuffix<K>> Multimap<K, V> asMultimap(List<V> list) {
        Multimap<K, V> multimap = ArrayListMultimap.create();
        if (!list.isEmpty()){
            for (V v : list){
                multimap.put(v.getCacheKeySuffix(),v);
            }
        }
        return multimap;
    }

    public static <K, E extends Identified<K>> List<K> extractIdList(Iterable<E> iterable) {
        List<K> result = new ArrayList<>();
        for (E e : iterable) {
            result.add(e.getId());
        }
        return result;
    }

    /**
     * 用来过滤空串及 null 串
     */
    public static final Predicate<String> BLANK_STRING_FILTER = new Predicate<String>() {
        @Override
        public boolean apply(String s) {
            return !StringUtils.isBlank(s);
        }
    };
}
