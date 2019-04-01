package cn.com.jj.configmanager;

import org.I0Itec.zkclient.ZkClient;
import org.junit.Test;

import cn.com.jj.configmanager.zk.ZkOperation;
/**
 * 
 * @author lvxr
 *
 */
public class AppTest 
{	
	@Test
	public void jj() {
		ZkClient zk  = ZkOperation.instanceZkClient();
		zk.writeData("/jj", "");
	}
	
}
