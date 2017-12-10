package jwcms.test.common.cache.redis.core;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import redis.clients.jedis.Protocol;

/**
 * Created by lianyuan on 16/2/19.
 */
public class SentinelMasterSlavePoolProxy implements FactoryBean<SentinelMasterSlavePool>, InitializingBean {

	private SentinelMasterSlavePool sentinelMasterSlavePool;
	private final Pattern p = Pattern.compile("^.+[:]\\d{1,5}\\s*$");
	private String sentinels;
	private String password;
	private String masterName;
	private Boolean masterWriteOnly = Boolean.FALSE;
	private GenericObjectPoolConfig poolConfig;
	private int connectionTimeout = Protocol.DEFAULT_TIMEOUT;
	private int soTimeout = Protocol.DEFAULT_TIMEOUT;
	private int database = 0;
	private String clientName;

	public void destroy() {
		sentinelMasterSlavePool.destroy();
	}

	@Override
	public SentinelMasterSlavePool getObject() throws Exception {
		return sentinelMasterSlavePool;
	}

	@Override
	public Class<SentinelMasterSlavePool> getObjectType() {
		return SentinelMasterSlavePool.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		String[] strings = sentinels.split(",");
		Set<String> sentinels = new HashSet<>();
		for (String redisHost : strings) {
			boolean isIpPort = p.matcher(redisHost).matches();
			if (!isIpPort) {
				throw new IllegalArgumentException("ip 或 port 不合法");
			}
			sentinels.add(redisHost);
		}
		if (StringUtils.isBlank(password)) {
			password = null;
		}
		
		this.sentinelMasterSlavePool = new SentinelMasterSlavePool(masterName, masterWriteOnly, sentinels, poolConfig, connectionTimeout, soTimeout, password, database, clientName);
	}

	/**
	 * @return the sentinelMasterSlavePool
	 */
	public SentinelMasterSlavePool getSentinelMasterSlavePool() {
		return sentinelMasterSlavePool;
	}

	/**
	 * @param sentinelMasterSlavePool
	 *            the sentinelMasterSlavePool to set
	 */
	public void setSentinelMasterSlavePool(SentinelMasterSlavePool sentinelMasterSlavePool) {
		this.sentinelMasterSlavePool = sentinelMasterSlavePool;
	}

	/**
	 * @return the sentinels
	 */
	public String getSentinels() {
		return sentinels;
	}

	/**
	 * @param sentinels
	 *            the sentinels to set
	 */
	public void setSentinels(String sentinels) {
		this.sentinels = sentinels;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the masterName
	 */
	public String getMasterName() {
		return masterName;
	}

	/**
	 * @param masterName
	 *            the masterName to set
	 */
	public void setMasterName(String masterName) {
		this.masterName = masterName;
	}

	/**
	 * @return the masterWriteOnly
	 */
	public Boolean getMasterWriteOnly() {
		return masterWriteOnly;
	}

	/**
	 * @param masterWriteOnly
	 *            the masterWriteOnly to set
	 */
	public void setMasterWriteOnly(Boolean masterWriteOnly) {
		this.masterWriteOnly = masterWriteOnly;
	}

	/**
	 * @return the poolConfig
	 */
	public GenericObjectPoolConfig getPoolConfig() {
		return poolConfig;
	}

	/**
	 * @param poolConfig
	 *            the poolConfig to set
	 */
	public void setPoolConfig(GenericObjectPoolConfig poolConfig) {
		this.poolConfig = poolConfig;
	}

	/**
	 * @return the connectionTimeout
	 */
	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	/**
	 * @param connectionTimeout
	 *            the connectionTimeout to set
	 */
	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	/**
	 * @return the soTimeout
	 */
	public int getSoTimeout() {
		return soTimeout;
	}

	/**
	 * @param soTimeout
	 *            the soTimeout to set
	 */
	public void setSoTimeout(int soTimeout) {
		this.soTimeout = soTimeout;
	}

	/**
	 * @return the database
	 */
	public int getDatabase() {
		return database;
	}

	/**
	 * @param database
	 *            the database to set
	 */
	public void setDatabase(int database) {
		this.database = database;
	}

	/**
	 * @return the clientName
	 */
	public String getClientName() {
		return clientName;
	}

	/**
	 * @param clientName
	 *            the clientName to set
	 */
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

}
