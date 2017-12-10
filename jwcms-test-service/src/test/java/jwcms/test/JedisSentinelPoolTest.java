package jwcms.test;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;

public class JedisSentinelPoolTest extends TestCase {
	
	private static final String MASTER_NAME = "mymaster";
	private static final String localHost = "127.0.0.1";

//	protected static HostAndPort master = new HostAndPort(localHost, 6379);
//	protected static HostAndPort slave1 = new HostAndPort(localHost, 6380);
//	protected static HostAndPort slave2 = new HostAndPort(localHost, 6381);

	protected static HostAndPort sentinel1 = new HostAndPort(localHost, 26379);
	protected static HostAndPort sentinel2 = new HostAndPort(localHost, 26380);
	protected static HostAndPort sentinel3 = new HostAndPort(localHost, 26381);

	protected static Jedis sentinelJedis1;
	protected static Jedis sentinelJedis2;
	protected static Jedis sentinelJedis3;

	protected Set<String> sentinels = new HashSet<String>();

	@Before
	public void setUp() throws Exception {
		sentinels.add(sentinel1.toString());
		sentinels.add(sentinel2.toString());
		sentinels.add(sentinel3.toString());

		sentinelJedis1 = new Jedis(sentinel1.getHost(), sentinel1.getPort());
		sentinelJedis2 = new Jedis(sentinel2.getHost(), sentinel2.getPort());
		sentinelJedis3 = new Jedis(sentinel3.getHost(), sentinel3.getPort());
	}

	@Test(expected = JedisConnectionException.class)
	public void initializeWithNotAvailableSentinelsShouldThrowException() {
		Set<String> wrongSentinels = new HashSet<String>();
		wrongSentinels.add(new HostAndPort(localHost, 65432).toString());
		wrongSentinels.add(new HostAndPort(localHost, 65431).toString());

		JedisSentinelPool pool = new JedisSentinelPool(MASTER_NAME, wrongSentinels);
		pool.destroy();
	}

	@Test(expected = JedisException.class)
	public void initializeWithNotMonitoredMasterNameShouldThrowException() {
		final String wrongMasterName = "wrongMasterName";
		JedisSentinelPool pool = new JedisSentinelPool(wrongMasterName, sentinels);
		pool.destroy();
	}

	@Test
	public void checkCloseableConnections() throws Exception {
		GenericObjectPoolConfig config = new GenericObjectPoolConfig();

		JedisSentinelPool pool = new JedisSentinelPool(MASTER_NAME, sentinels, config, 10000, "pw", 3);
		Jedis jedis = pool.getResource();
		jedis.auth("pw");
		jedis.set("foo", "bar");
		String ss = jedis.get("test");
		assertEquals("bar", jedis.get("foo"));
		jedis.close();
		pool.close();
		assertTrue(pool.isClosed());
	}

	@Test
	public void ensureSafeTwiceFailover() throws InterruptedException {
		JedisSentinelPool pool = new JedisSentinelPool(MASTER_NAME, sentinels, new GenericObjectPoolConfig(), 9000,
				"pw", 3);

		forceFailover(pool);
		// after failover sentinel needs a bit of time to stabilize before a new
		// failover
		Thread.sleep(100);
		forceFailover(pool);

		// you can test failover as much as possible
	}

	@Test
	public void returnResourceShouldResetState() {
		GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		config.setMaxTotal(1);
		config.setBlockWhenExhausted(false);
		JedisSentinelPool pool = new JedisSentinelPool(MASTER_NAME, sentinels, config, 1000, "pw", 3);

		Jedis jedis = pool.getResource();
		Jedis jedis2 = null;

		try {
			jedis.set("hello", "jedis");
			Transaction t = jedis.multi();
			t.set("hello", "world");
			jedis.close();

			jedis2 = pool.getResource();

			assertTrue(jedis == jedis2);
			assertEquals("jedis", jedis2.get("hello"));
		} catch (JedisConnectionException e) {
			if (jedis2 != null) {
				jedis2 = null;
			}
		} finally {
			jedis2.close();

			pool.destroy();
		}
	}

	@Test
	public void checkResourceIsCloseable() {
		GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		config.setMaxTotal(1);
		config.setBlockWhenExhausted(false);
		JedisSentinelPool pool = new JedisSentinelPool(MASTER_NAME, sentinels, config, 1000, "pw", 3);

		Jedis jedis = pool.getResource();
		try {
			jedis.set("hello", "jedis");
		} finally {
			jedis.close();
		}

		Jedis jedis2 = pool.getResource();
		try {
			assertEquals(jedis, jedis2);
		} finally {
			jedis2.close();
		}
	}

	private void forceFailover(JedisSentinelPool pool) throws InterruptedException {
		HostAndPort oldMaster = pool.getCurrentHostMaster();

		// jedis connection should be master
		Jedis beforeFailoverJedis = pool.getResource();
		assertEquals("PONG", beforeFailoverJedis.ping());

//		waitForFailover(pool, oldMaster);

		Jedis afterFailoverJedis = pool.getResource();
		assertEquals("PONG", afterFailoverJedis.ping());
		assertEquals("pw", afterFailoverJedis.configGet("requirepass").get(1));
//		assertEquals(3, afterFailoverJedis.getDB());

		// returning both connections to the pool should not throw
		beforeFailoverJedis.close();
		afterFailoverJedis.close();
	}

//	private void waitForFailover(JedisSentinelPool pool, HostAndPort oldMaster) throws InterruptedException {
//		HostAndPort newMaster = JedisSentinelTestUtil.waitForNewPromotedMaster(MASTER_NAME, sentinelJedis1,
//				sentinelJedis2);
//
//		waitForJedisSentinelPoolRecognizeNewMaster(pool, newMaster);
//	}

	private void waitForJedisSentinelPoolRecognizeNewMaster(JedisSentinelPool pool, HostAndPort newMaster)
			throws InterruptedException {

		while (true) {
			HostAndPort currentHostMaster = pool.getCurrentHostMaster();

			if (newMaster.equals(currentHostMaster))
				break;

			System.out.println("JedisSentinelPool's master is not yet changed, sleep...");

			Thread.sleep(100);
		}
	}

}