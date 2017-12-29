package jwcms.test.common.cache.redis.core;

import java.util.Objects;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPool;

/**
 * Created by lianyuan on 16/2/24.
 */
public class JedisPoolExt extends JedisPool {

	private String host;

	private int port;

	public JedisPoolExt(GenericObjectPoolConfig poolConfig, String host, int port, int connectionTimeout, int soTimeout, String password, int database, String clientName) {
//		super(poolConfig, host, port, connectionTimeout, soTimeout, password, database, clientName, false, null, null, null);
		super(poolConfig, host, port, connectionTimeout, soTimeout, password, database, clientName);
		
//		 public JedisPool(final GenericObjectPoolConfig poolConfig, final String host, int port,
//			      final int connectionTimeout, final int soTimeout, final String password, final int database,
//			      final String clientName, final boolean ssl, final SSLSocketFactory sslSocketFactory,
//			      final SSLParameters sslParameters, final HostnameVerifier hostnameVerifier) {
//			    super(poolConfig, new JedisFactory(host, port, connectionTimeout, soTimeout, password,
//			        database, clientName, ssl, sslSocketFactory, sslParameters, hostnameVerifier));
//			  }

		this.port = port;
		this.host = host;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof JedisPoolExt) {
			JedisPoolExt jedisPoolExt = (JedisPoolExt) obj;
			return Objects.equals(this.host, jedisPoolExt.host) && this.port == jedisPoolExt.port;
		}
		return (this == obj);
	}

	@Override
	public int hashCode() {
		return (host + ":" + port).hashCode();
	}

	@Override
	public String toString() {
		return host + ":" + port;
	}

	public HostAndPort getHostAndPort() {
		return new HostAndPort(host, port);
	}
}
