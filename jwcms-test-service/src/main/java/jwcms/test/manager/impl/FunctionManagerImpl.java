package jwcms.test.manager.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import jwcms.test.manager.FunctionManager;
import jwcms.test.model.Function;
import jwcms.test.util.FunctionContainer;
import jwcms.test.util.ThisMachineIP;

@Service
public class FunctionManagerImpl implements FunctionManager, InitializingBean {

	private Logger logger = LoggerFactory.getLogger(FunctionManagerImpl.class);

	private String zkUrl = "localhost:2181";
	
	private static final String path = "/jwcms/function";

	private CuratorFramework client = null;

	private ConnectionStateListener clientListener = null;

	private PathChildrenCacheListener plis = null;

	// zk监听线程池
	private ExecutorService zkWatchThreadPool = Executors.newFixedThreadPool(1);

	/**
	 * 是否初始化
	 */
	private AtomicBoolean isInited = new AtomicBoolean(false);

	private static int MAX_WAIT_TIMES = 10;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if (!isInited.compareAndSet(false, true)) {
			logger.warn("function load is inited");
			return;
		}
		clientListener = new ConnectionStateListener() {
			@Override
			public void stateChanged(CuratorFramework client, ConnectionState newState) {
				if (newState == ConnectionState.CONNECTED) {
					logger.info(ThisMachineIP.IP + " connected zookeeper established successfully.");
				} else if (newState == ConnectionState.LOST) {
					logger.info(ThisMachineIP.IP + " connection lost, waiting for reconnection.");
					try {
						reinit();
					} catch (InterruptedException e) {
						logger.error(ThisMachineIP.IP + " connected error:" + e);
					}
				}
			}
		};
		plis = new PathChildrenCacheListener() {
			@SuppressWarnings("incomplete-switch")
			@Override
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
				if (event.getType().equals(PathChildrenCacheEvent.Type.CONNECTION_SUSPENDED)  || null == event.getData()) {
					return;
				}
				String pathName = event.getData().getPath();
				String functionName = ZKPaths.getNodeFromPath(pathName);
				switch (event.getType()) {
				case CHILD_ADDED: {
					String classPath = new String(client.getData().forPath(pathName), "UTF-8");
					FunctionContainer.addFunction(functionName, classPath);
					break;
				}
				case CHILD_REMOVED: {
					FunctionContainer.removeFunction(functionName);
					break;
				}
				case CHILD_UPDATED: {
					String classPath = new String(client.getData().forPath(pathName), "UTF-8");
					FunctionContainer.addFunction(functionName, classPath);
					break;
				}
				}
			}
		};
		
		// initiate client
		init();
		
		// add watcher
		watcherPath(path);
		
		// 如果连接zk失败，则等待
		int waitTimes = 0;
		while (!client.getZookeeperClient().isConnected() && waitTimes < MAX_WAIT_TIMES) {
			waitTimes++;
			Thread.sleep(100);
		}
		if (!client.getZookeeperClient().isConnected()) {
			logger.error("load function, zk is not connect, ip:{}", ThisMachineIP.IP);
		}
		
		loadAllFunction();
	}
	
	private void init() throws InterruptedException {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(5, 5000);
		client = CuratorFrameworkFactory.builder().connectString(zkUrl).connectionTimeoutMs(5000).sessionTimeoutMs(3000).retryPolicy(retryPolicy).build();
		client.getConnectionStateListenable().addListener(clientListener, zkWatchThreadPool);
		client.start();
		client.blockUntilConnected();
	}

	private void reinit() throws InterruptedException {
		unregister();
		init();
	}

	private void unregister() {
		if (client != null) {
			client.close();
			client = null;
		}
	}

	@SuppressWarnings("resource")
	private void watcherPath(String path) throws Exception {
		PathChildrenCache cache = new PathChildrenCache(client, path, false);
		cache.start();
		cache.getListenable().addListener(plis, zkWatchThreadPool);
	}

	@Override
	public void loadAllFunction() {
		// 清空加载的函数
		FunctionContainer.clearFunction();
		try {
			List<String> childrenPaths = client.getChildren().forPath(path);
			if (childrenPaths.isEmpty()) {
				initFunction();
				return;
			}
			// 重新加载函数
			for (String cpath : childrenPaths) {
				String content = new String(client.getData().forPath(path + "/" + cpath), "UTF-8");
				FunctionContainer.addFunction(cpath, content);
			}
		} catch (Exception e) {
			logger.error("FunctionManager.loadAllFunction error:" + e);
		}
	}

	/**
	 * 初始化内存和zk函数
	 * 
	 * @throws Exception
	 */
	private void initFunction() throws Exception {
		List<Function> functionList = listAllFunctions();
		for (Function f : functionList) {
			loadFunction(f);
		}
	}

	@Override
	public void loadFunction(Function f) throws Exception {
		byte[] data = null;
		boolean created = false;
		try {
			data = client.getData().forPath(path + "/" + f.getFunctionName());
		} catch (NoNodeException e) {
			logger.warn("getData exception, f:{}", f);
			client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path + "/" + f.getFunctionName());
			client.setData().forPath(path + "/" + f.getFunctionName(), f.getFunctionPath().getBytes());
			data = "".getBytes();
			created = true;
		}
		if (!created) {
			if (null != data) {
				client.setData().forPath(path + "/" + f.getFunctionName(), f.getFunctionPath().getBytes());
			} else {
				client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path + "/" + f.getFunctionName());
				client.setData().forPath(path + "/" + f.getFunctionName(), f.getFunctionPath().getBytes());
			}
		}
	}

	@Override
	public void removeFunction(Function f) {
		String zkPath = path + "/" + f.getFunctionName();
		try {
			client.delete().deletingChildrenIfNeeded().forPath(zkPath);
		} catch (Exception e) {
			logger.error("failed to deleted zk not for path :" + zkPath, e);
		}
	}

	public String getZkUrl() {
		return zkUrl;
	}

	public void setZkUrl(String zkUrl) {
		this.zkUrl = zkUrl;
	}

	@Override
	public void refreshLoadAllFunction() {
		try {
			List<Function> functions = listAllFunctions();
			for (Function function : functions) {
				Stat stat = client.checkExists().forPath(path + "/" + function.getFunctionName());
				if (null == stat) {
					loadFunction(function);
					continue;
				}
				String content = new String(client.getData().forPath(path + "/" + function.getFunctionName()), "utf-8");
				String functionPath = function.getFunctionPath();
				if (StringUtils.isBlank(content) || !content.equals(functionPath)) {
					loadFunction(function);
				}
			}
		} catch (Exception e) {
			logger.error("refreshLoadAllFunction exception,", e);
		}
	}

	@Override
	public void loadFunctionByDb() {
		List<Function> functions = listAllFunctions();
		for (Function function : functions) {
			FunctionContainer.addFunction(function.getFunctionName(), function.getFunctionPath());
		}
	}

	@Override
	public void loadFunctionByZk() {
		try {
			List<String> childrenPaths = client.getChildren().forPath(path);
			if (childrenPaths.isEmpty()) {
				initFunction();
				return;
			}
			// 重新加载函数
			for (String cpath : childrenPaths) {
				String content = new String(client.getData().forPath(path + "/" + cpath), "UTF-8");
				FunctionContainer.addFunction(cpath, content);
			}
		} catch (Exception e) {
			logger.error("FunctionManager.loadAllFunction error:" + e);
		}
	}

	@Override
	public List<Function> listAllFunctions() {
		List<Function> functions = new LinkedList<>();
		functions.add(new Function(1L, "a", "a.a.a"));
		functions.add(new Function(2L, "b", "b.b.b"));
		functions.add(new Function(3L, "c", "d.d.d"));
		functions.add(new Function(4L, "d", "e.e.e"));
		functions.add(new Function(5L, "e", "f.f.f"));
		
		return functions;
	}

}
