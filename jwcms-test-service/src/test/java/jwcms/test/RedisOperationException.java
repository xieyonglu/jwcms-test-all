package jwcms.test;

/**
 * Created by lianyuan on 15/11/19.
 */
public class RedisOperationException extends RuntimeException {

    private static final long serialVersionUID = 3317154016749700649L;

    public RedisOperationException(final Throwable cause) {
        super(cause);
    }

    public RedisOperationException(String msg) {
        super(msg);
    }
    public RedisOperationException(Throwable e, String msg) {
        super(msg, e);
    }
}
