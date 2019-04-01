package cn.com.jj.manager.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.com.jj.config.ZkConfigStatic;
import cn.com.jj.config.file.FileOperation;
import cn.com.jj.manager.mapper.ConfigNodeTypeMapper;

/**
 * 
 * @author lvxr
 *
 */

@Component
public class ConfigListenerUtil {
	@Autowired
	private ConfigNodeTypeMapper configNodeTypeMapper;
	List<String> allNodes = new ArrayList<String>();
	ZkClient zk = new ZkClient(new ZkConnection(ZkConfigStatic.IPCONFIG));

	/**
	 * 路径配置
	 * 
	 * @throws Exception
	 */
	public void openAllListener() throws Exception {
		Properties properties = new Properties();
		      // 使用InPutStream流读取properties文件
		BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/resources/config.properties"));
		properties.load(bufferedReader);
		      // 获取key对应的value值
		String[] paths = properties.getProperty("paths").split(",");
		
		try {
			for (String path : paths) {
				if (zk.exists(path)) {
					buildConfigContent(path);
					open(path);
				}
				openChildListener(path);				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void open(String path) {

		List<String> children = zk.getChildren(path);
		for (String childPath : children) {
			if (zk.countChildren(path + "/" + childPath) > 0) {
				open(path + "/" + childPath);
			}
			openChildListener(path + "/" + childPath);
			openListener(path + "/" + childPath);

		}
	}

	public void openListener(String path) {

		zk.subscribeDataChanges(path, new IZkDataListener() {
			@Override
			public void handleDataChange(String arg0, Object arg1) throws Exception {
				System.out.println("监听到配置文件内容被修改：" + path + "-" + arg0 + "-" + arg1);
//				Thread.sleep(100);
				buildConfigContent(path);
			}

			@Override
			public void handleDataDeleted(String deletePath) throws Exception {
				System.out.println("监听到配置文件内容被删除" + deletePath);
//				Thread.sleep(100);
				buildConfigContent(path);
			}

		});

	}

	public void openChildListener(String path) {
		zk.subscribeChildChanges(path, new IZkChildListener() {

			@Override
			public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
				System.out.println("监听节点" + parentPath + "-" + currentChilds);
//				Thread.sleep(100);
//				zk.unsubscribeChildChanges(parentPath, this);
				if (currentChilds != null) {
					open(parentPath);
//					Thread.sleep(100);					
				}
				buildConfigContent(path);
			}
		});
	}

	public void buildConfigContent(String path) throws IOException {
		path = getConfigPath(path);
		if (path.length() < 1) {
			return;
		}
		if (!zk.exists(path)) {
			return;
		}
		StringBuilder builder = new StringBuilder();
		build(path, builder);
		FileOperation.synToFile(path, builder.toString());
	}

	public void build(String path, StringBuilder builder) {
		List<String> children = zk.getChildren(path);
		for (String child : children) {
			String temp = path + "/" + child;
			if ("title".equals(configNodeTypeMapper.showTypeByPath(temp))) {
				builder.append("[" + child + "]" + "\r");
				if (zk.getChildren(temp).size() > 0) {
					build(temp, builder);
				}
			} else if ("property".equals(configNodeTypeMapper.showTypeByPath(temp))) {
				builder.append(child);
				builder.append("=" + zk.readData(temp) + "\r");
			}
		}
	}

	public String getConfigPath(String path) {
		if (configNodeTypeMapper.showTypeByPath(path) != null
				&& !"file".equals(configNodeTypeMapper.showTypeByPath(path))) {
			String tempPath = path.substring(0, path.lastIndexOf("/"));
			return getConfigPath(tempPath);
		}
		return path;

	}
}
