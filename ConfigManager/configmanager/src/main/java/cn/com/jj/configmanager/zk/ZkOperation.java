package cn.com.jj.configmanager.zk;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;

import cn.com.jj.config.ZkConfigStatic;
/**
 * 
 * @author lvxr
 *
 */
public class ZkOperation {
	public static int zkCount = 0;
	
	public static ZkClient instanceZkClient() {
		zkCount++;
		ZkClient zk =new ZkClient(new ZkConnection(ZkConfigStatic.IPCONFIG));
		return zk;
	} 
	public static void finishZk(ZkClient zk) {
		zkCount--;
		zk.close();
	}
}
