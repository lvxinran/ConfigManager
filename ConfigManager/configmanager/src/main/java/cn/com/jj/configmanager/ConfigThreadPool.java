package cn.com.jj.configmanager;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.I0Itec.zkclient.ZkClient;

import cn.com.jj.configmanager.model.ConfigNodeType;
/**
 * 
 * @author lvxr
 *
 */
public class ConfigThreadPool {
		private static Map<ConfigNodeType, String> map = null;
		static ExecutorService pool = Executors.newFixedThreadPool(2);
		static ZkClient zk = null;
		
		
		public static ExecutorService getPool() {
			return pool;
		}

		public static Map<ConfigNodeType, String> getMap() {
			return map;
		}

		public static void setMap(Map<ConfigNodeType, String> map) {
			ConfigThreadPool.map = map;
		}

		public static ZkClient getZk() {
			return zk;
		}

		public static void setZk(ZkClient zk) {
			ConfigThreadPool.zk = zk;
		}

		public static void startInsert() {
			pool.execute(new MyRunnable());
			pool.execute(new MyRunnable());
		}
	 
		static class MyRunnable implements Runnable {
				
				@Override
				public void run() {
					while(map!=null&&map.size()>0) {
						for (Map.Entry<ConfigNodeType, String> entry : map.entrySet()) {
							zk.createPersistent(entry.getKey().getNodePath(),true);
							zk.writeData(entry.getKey().getNodePath(), entry.getValue());
							map.remove(entry.getKey());
						}
					}
					
				}
				
			}
}