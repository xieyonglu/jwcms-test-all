package jwcms.test.common.cache.redis.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import jwcms.test.common.cache.redis.exception.RedisException;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * 哨兵读写分离连接池 Created by lianyuan on 16/2/19.
 */
public class SentinelMasterSlavePool extends JedisSentinelPool {
	
    private final Logger logger = LoggerFactory.getLogger(SentinelMasterSlavePool.class);
    
    private String masterName;
    
    private Set<String> sentinels;
    
    private Boolean masterWriteOnly = Boolean.FALSE;
    
    private List<JedisPoolExt> jedisReadPools;
    
    private List<PoolChangeListener> poolChangeListeners = new CopyOnWriteArrayList<>();

    public SentinelMasterSlavePool(String masterName, Boolean masterWriteOnly, Set<String> sentinels, GenericObjectPoolConfig poolConfig, int connectionTimeout, int soTimeout, String password, int database, String clientName) {
        super(masterName, sentinels, poolConfig, connectionTimeout, soTimeout, password, database, clientName);
        this.masterWriteOnly = masterWriteOnly;
        this.masterName = masterName;
        this.sentinels = sentinels;
        
        try {
            initReadPool();
            
            initSentinelListeners(sentinels);
        } catch (Exception e) {
            throw new RedisException("SentinelMasterSlavePool init error.", e);
        }
    }

    private synchronized void initReadPool() {
        Set<JedisPoolExt> tempPoolExts = new HashSet<>();

        for (String sentinel : sentinels) {
            List<String> sentinelAdd = Arrays.asList(sentinel.split(":"));
            String host = sentinelAdd.get(0);
            int port = Integer.parseInt(sentinelAdd.get(1));

            infoLog("Connecting to Sentinel " + sentinelAdd);

            Jedis jedis = null;
            try {
                jedis = new Jedis(host, port);
                // master允许读时,才放入读池
                if (!masterWriteOnly) {
					List<Map<String, String>> allMasterList = jedis.sentinelMasters();
					List<Map<String, String>> myMasterList = new ArrayList<>();
					for (Map<String, String> masterMap : allMasterList) {
						if (this.masterName.equals(masterMap.get("name"))) {
							myMasterList.add(masterMap);
						}
					}
                    addRedisPoolToSet(tempPoolExts, myMasterList);
                }
                List<Map<String, String>> slaveList = jedis.sentinelSlaves(masterName);
                addRedisPoolToSet(tempPoolExts, slaveList);
            } catch (JedisConnectionException e) {
                errorLog("Cannot connect to sentinel running @ " + sentinelAdd + ". Trying next one.", e);
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }
        jedisReadPools = Lists.newArrayList(tempPoolExts);
        infoLog(clientName + " jedisReadPools is ready." + jedisReadPools);
    }

    private void addRedisPoolToSet(Set<JedisPoolExt> redisPoolExtList, List<Map<String, String>> redisList) {
        for (Map<String, String> redis : redisList) {
            String host = redis.get("ip");
            int port = Integer.valueOf(redis.get("port"));
            String flags = redis.get("flags");

            if (!flags.contains("s_down")) {
                JedisPoolExt masterJedisPool = new JedisPoolExt(poolConfig, host, port, connectionTimeout, soTimeout, password, database, clientName);
                redisPoolExtList.add(masterJedisPool);
            }
        }
    }

    private synchronized void addRedisPoolToReadPool(String host, int port) {
        HostAndPort hostAndPort = new HostAndPort(host, port);
        boolean isExist = false;
        for (JedisPoolExt jedisPoolExt : jedisReadPools) {
            if (jedisPoolExt.getHostAndPort().equals(hostAndPort)) {
                isExist = true;
                break;
            }
        }
        if (!isExist) {
            JedisPoolExt jedisPoolExt = new JedisPoolExt(poolConfig, host, port, connectionTimeout, soTimeout, password, database, clientName);
            jedisReadPools.add(jedisPoolExt);
        }
    }

    private synchronized void removeRedisPoolFromReadPool(String host, int port) {
        HostAndPort hostAndPort = new HostAndPort(host, port);
        for (JedisPoolExt jedisPoolExt : jedisReadPools) {
            if (jedisPoolExt.getHostAndPort().equals(hostAndPort)) {
                jedisReadPools.remove(jedisPoolExt);
                infoLog("Remove JedisReadPool at " + hostAndPort);
                break;
            }
        }
    }

    protected Jedis getWriteResource() {
        return getResource();
    }

    protected Jedis getReadResource() {
        if (jedisReadPools.size() == 0) {
            throw new RedisException("there is no jedis for read");
        }
        Random rand = new Random();
        int randNum = rand.nextInt(jedisReadPools.size());
        JedisPool jedisPool = jedisReadPools.get(randNum);
        try {
            return jedisPool.getResource();
        } catch (Throwable e) {
            if (jedisReadPools.size() > 1) {
                while (true) {
                    int randNum1 = rand.nextInt(jedisReadPools.size());
                    if (randNum1 != randNum) {
                        try {
                            JedisPool jedisPool1 = jedisReadPools.get(randNum1);
                            return jedisPool1.getResource();
                        } catch (Exception e1) {
                            throw new RedisException("SentinelMasterSlavePool getReadResource retry error", e1);
                        }
                    }
                }
            } else {
                throw new RedisException("SentinelMasterSlavePool getReadResource error", e);
            }

        }
    }

    private void initSentinelListeners(Set<String> sentinels) {
        for (String sentinel : sentinels) {
            List<String> getMasterAddrByNameResult = Arrays.asList(sentinel.split(":"));
            String host = getMasterAddrByNameResult.get(0);
            int port = Integer.parseInt(getMasterAddrByNameResult.get(1));

            PoolChangeListener masterListener = new PoolChangeListener(masterName, host, port);
            masterListener.setDaemon(true);
            poolChangeListeners.add(masterListener);
            masterListener.start();
        }
    }

    public void destroy() {
        super.destroy();
        for (PoolChangeListener p : poolChangeListeners) {
            p.shutdown();
        }
    }

    public void resetReadPool() {
        initReadPool();
    }

    protected class PoolChangeListener extends Thread {
        protected String masterName;
        protected String host;
        protected int port;
        protected long subscribeRetryWaitTimeMillis = 5000;
        protected volatile Jedis j;
        protected AtomicBoolean running = new AtomicBoolean(false);

        public PoolChangeListener(String masterName, String host, int port) {
            super(String.format("PoolChangeListener-%s-[%s:%d]", masterName, host, port));
            this.masterName = masterName;
            this.host = host;
            this.port = port;
        }

        public void run() {
            running.set(true);

            while (running.get()) {
                j = new Jedis(host, port);
                try {
                    // double check that it is not being shutdown
                    if (!running.get()) {
                        break;
                    }

                    j.subscribe(new JedisPubSub() {
                        @Override
                        public void onMessage(String channel, String message) {
							if (!message.contains(masterName)) {
								return;
							}
                        	
                            infoLog(clientName + " Sentinel " + host + ":" + port + " published: " + message + ".channel:" + channel);
                            String[] switchMasterMsg = message.split(" ");

                            if (switchMasterMsg.length > 0 && switchMasterMsg[0].toLowerCase().equals("sentinel")) {
                                return;
                            }

                            if ("+switch-master".equals(channel)) {
                                if (switchMasterMsg.length > 3) {
                                    initReadPool();
                                } else {
                                    logInvalidMessage(channel, message);
                                }
                            } else if ("+slave".equals(channel) || "-sdown".equals(channel)) {
                                if (switchMasterMsg.length > 7) {
                                    addRedisPoolToReadPool(switchMasterMsg[2], Integer.valueOf(switchMasterMsg[3]));
                                } else {
                                    logInvalidMessage(channel, message);
                                }
                            } else if ("+sdown".equals(channel)) {
                                if (switchMasterMsg.length > 7) {
                                    removeRedisPoolFromReadPool(switchMasterMsg[2], Integer.valueOf(switchMasterMsg[3]));
                                } else if (switchMasterMsg.length != 4) {
                                    // master +sdown length=4
                                    logInvalidMessage(channel, message);
                                }
                            }
                        }

                        private void logInvalidMessage(String channel, String message) {
                            errorLog("Invalid message received on Sentinel " + host + ":" + port + " on channel " + channel + ": " + message, null);
                        }
                    }, "+switch-master", "+slave", "+sdown", "-sdown");

                } catch (JedisConnectionException e) {
                    if (running.get()) {
                        errorLog("Lost connection to Sentinel at " + host + ":" + port + ". Sleeping 5000ms and retrying.", e);
                        try {
                            Thread.sleep(subscribeRetryWaitTimeMillis);
                        } catch (InterruptedException e1) {
                            errorLog("Sleep interrupted: ", e1);
                        }
                    } else {
                        infoLog("Unsubscribing from Sentinel at " + host + ":" + port);
                    }
                } finally {
                    j.close();
                }
            }
        }

        public void shutdown() {
            try {
                infoLog("Shutting down listener on " + host + ":" + port);
                running.set(false);
                // This isn't good, the Jedis object is not thread safe
                if (j != null) {
                    j.disconnect();
                }
            } catch (Exception e) {
                errorLog("Caught exception while shutting down: ", e);
            }
        }
    }

    public void infoLog(String msg) {
        logger.info(msg);
        log.info(msg);
    }

    public void errorLog(String msg, Throwable e) {
        logger.error(msg, e);
//        log.severe(msg);
    }
}
