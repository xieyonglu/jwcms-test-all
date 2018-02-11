package jwcms.test.common.lock.zookeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * Created by IDEA User: mashaohua Date: 2016-09-30 16:09 Desc:
 */
public class ZookeeperLock implements Lock, Watcher {
	private ZooKeeper zk;
	private String root = "/locks";// 根
	private String lockName;// 竞争资源的标志
	private String myZnode;// 当前锁
	private int sessionTimeout = 30000;
	private List<Exception> exception = new ArrayList<Exception>();

	/**
	 * 创建分布式锁,使用前请确认config配置的zookeeper服务可用
	 * 
	 * @param config
	 *            127.0.0.1:2181
	 * @param lockName
	 *            竞争资源标志,lockName中不能包含单词lock
	 */
	public ZookeeperLock(String config, String lockName) {
		this.lockName = lockName;
		// 创建一个与服务器的连接
		try {
			zk = new ZooKeeper(config, sessionTimeout, this);
			Stat stat = zk.exists(root, false);
			if (stat == null) {
				// 创建根节点
				zk.create(root, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			}
		} catch (IOException e) {
			exception.add(e);
		} catch (KeeperException e) {
			exception.add(e);
		} catch (InterruptedException e) {
			exception.add(e);
		}
	}

	@Override
	public void lock() {
		if (exception.size() > 0) {
			throw new LockException(exception.get(0));
		}
		if (!tryLock()) {
			throw new LockException("您的操作太频繁，请稍后再试");
		}
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		this.lock();
	}

	@Override
	public boolean tryLock() {
		try {
			myZnode = zk.create(root + "/" + lockName, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			return true;
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		return tryLock();
	}

	@Override
	public void unlock() {
		try {
			zk.delete(myZnode, -1);
			myZnode = null;
			zk.close();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (KeeperException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Condition newCondition() {
		return null;
	}

	@Override
	public void process(WatchedEvent watchedEvent) {
		//
	}
	
	public class LockException extends RuntimeException {
		private static final long serialVersionUID = 1L;
		public LockException(String e){
			super(e);
		}
		public LockException(Exception e){
			super(e);
		}
	}
}
