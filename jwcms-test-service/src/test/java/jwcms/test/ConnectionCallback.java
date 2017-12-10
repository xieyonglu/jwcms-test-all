package jwcms.test;

/** 
 * 描述：redis连接的回调接口 
 */  
public interface ConnectionCallback<T, P> {  
    /** 
     * Details：连接redis服务 
     * @param shardedJedis jedis分片 
     * @return 
     * T 
     */  
    T doInConnection(P p) throws RedisOperationException;  
} 