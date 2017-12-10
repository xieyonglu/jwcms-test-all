package jwcms.test.common.cache.redis.base;

/**
 * 拥有 id 字段的类均可实现此类
 *
 * @author lianyuan
 * @since 15/11/19 20:25
 */
public interface Identified<E> {

	E getId();

}
