package cn.com.jj.configmanager.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.com.jj.configmanager.mapper.ConfigNodeTypeMapper;
import cn.com.jj.configmanager.model.ConfigModify;
import cn.com.jj.configmanager.model.ConfigNode;
import cn.com.jj.configmanager.model.ConfigNodeType;
import cn.com.jj.configmanager.util.ConfigUtil;
import cn.com.jj.configmanager.zk.ZkOperation;

/**
 * 
 * @author lvxr
 *
 */
@Service
public class ConfigManagerService {

	@Autowired
	private ConfigNodeTypeMapper configNodeTypeMapper;

	ZkClient zk = ZkOperation.instanceZkClient();
	List<ConfigNode> configNodes;
	String currentTitle = "";
	long time = 0;

	public String showNodeInfo(String path) {
		if (!zk.exists(path)) {
			return null;
		}
		String content = zk.readData(path);
		return content;
	}
	public boolean upLoadCheck(String path,String title) {
		return zk.exists(ConfigUtil.buildCompletePath(path, title));
	}
	public ConfigNode ls(String path, String title, int count) throws Exception {
		ConfigNode node = new ConfigNode();
		if (zk.exists(path)) {
			node.setPath(path);
			node.setTitle(title);
			node.setSortInGroup(count);
			node.setExpand(true);
			node.setNodeType(configNodeTypeMapper.showTypeByPath(path));
			if (zk.readData(path) != null) {
				String content = zk.readData(path);
				node.setNodeContent(content);
//				if(("file").equals(content.getNodeType())||("title").equals(content.getNodeType())) {
//					node.setExpand(false);
//				}
			}
			if (zk.countChildren(path) > 0) {
				List<ConfigNode> childNodes = new ArrayList<ConfigNode>();
				int childeCount = 0;
				for (String tempNode : zk.getChildren(path)) {
					childeCount++;
					childNodes.add(ls(ConfigUtil.buildCompletePath(path, tempNode), tempNode, childeCount));
				}
				node.setChildren(childNodes);
			} else {
				return node;
			}

		}
		return node;
	}

	public boolean addConfigNode(String parentNodePath, String currentTitle, String nodeType, String content) {
		boolean result = false;
		try {

			if (!zk.exists(parentNodePath)) {
				return false;
			}
			if (zk.exists(ConfigUtil.buildCompletePath(parentNodePath, currentTitle))) {
				return false;
			}
			String newPath = ConfigUtil.buildCompletePath(parentNodePath, currentTitle);
			zk.createPersistent(newPath);
			zk.writeData(newPath, content);
			configNodeTypeMapper.insertToNodeType(newPath, nodeType);
			result = true;

		} catch (Exception e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}

	public boolean modifyNode(ConfigModify modify) {
		String convertPath = new StringBuilder(modify.getNodePath()).reverse().toString().replaceFirst(
				new StringBuilder(modify.getOldTitle()).reverse().toString(), new StringBuilder(modify.getModifyTitle()).reverse().toString());
		String newPath = new StringBuilder(convertPath).reverse().toString();
		if (zk.exists(modify.getNodePath())) {
			configNodes = new ArrayList<ConfigNode>();
			remove(modify.getNodePath(), false);
			if (configNodes.size() > 0) {
				for (ConfigNode configNode : configNodes) {
					zk.delete(configNode.getPath(), -1);
				}
				for (ConfigNode configNode : configNodes) {
					String path = configNode.getPath().replaceFirst(modify.getNodePath(), newPath);
					configNodeTypeMapper.updateNodePath(configNode.getPath(), path);
					configNode.setPath(path);
					if (!zk.exists(configNode.getPath())) {
						zk.createPersistent(configNode.getPath(), true);
					}
					if (configNode.getNodeContent() != null) {
						zk.writeData(configNode.getPath(), configNode.getNodeContent());
					}
				}
			}
		}
		return true;
	}

	public void modifyContent(String path, String content) {
		String pathContent = zk.readData(path);
		pathContent = content;
		zk.writeData(path, pathContent);
	}

	public boolean removeNode(String path) {
		boolean result = false;
		try {
			configNodes = new ArrayList<ConfigNode>();
			remove(path, true);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	private void remove(String path, boolean type) {
		List<String> children = zk.getChildren(path);
		for (String pathCd : children) {
			// 获取父节点下面的子节点路径
			String newPath = "";
			// 递归调用,判断是否是根节点
			if ("/".equals(path)) {
				newPath = "/" + pathCd;
			} else {
				newPath = ConfigUtil.buildCompletePath(path, pathCd);
			}
			remove(newPath, type);
		}
		// 删除节点,并过滤zookeeper节点和 /节点
		if (path != null && !path.trim().startsWith("/zookeeper") && !path.trim().equals("/")) {
			if (type) {
				zk.delete(path, -1);
				configNodeTypeMapper.deleteNodeTypeByPath(path);
			} else {
				ConfigNode tempConf = new ConfigNode();
				tempConf.setPath(path);
				if (zk.readData(path) != null) {
					tempConf.setNodeContent((String) zk.readData(path));
				}
				configNodes.add(tempConf);
			}
		}

	}

	public void openListener(String path) {
		zk.subscribeDataChanges(path, new IZkDataListener() {
			@Override
			public void handleDataChange(String arg0, Object arg1) throws Exception {
				System.out.println("监听到配置文件被修改：" + arg0 + arg1);
			}

			@Override
			public void handleDataDeleted(String arg0) throws Exception {
				System.out.println("监听到配置文件被删除");
			}

		});
	}

	public List<ConfigNode> showPreview(String path) {
		if (!zk.exists(path)) {
			return null;
		}
		configNodes = new ArrayList<ConfigNode>();
		show(path);

		return configNodes;
	}

	public void show(String path) {
		List<String> childPathes = zk.getChildren(path);
		for (String tempPath : childPathes) {
			String buildTempPath = ConfigUtil.buildCompletePath(path, tempPath);
			ConfigNode tempConf = new ConfigNode();
			tempConf.setPath(buildTempPath);
			tempConf.setNodeType(configNodeTypeMapper.showTypeByPath(buildTempPath));
			tempConf.setTitle(tempPath);
			if (zk.readData(buildTempPath) != null) {
				tempConf.setNodeContent(zk.readData(buildTempPath));
			}
			configNodes.add(tempConf);
			String newPath = "";
			// 递归调用,判断是否是根节点
			if ("/".equals(path)) {
				newPath = "/" + tempPath;
			} else {
				newPath = ConfigUtil.buildCompletePath(path, tempPath);
			}
			if (zk.getChildren(newPath).size() > 0) {
				show(newPath);
			}
		}
	}

	public String upload(MultipartFile file, String dirPath) {
		if(zk.exists(ConfigUtil.buildCompletePath(dirPath, file.getOriginalFilename()))) {
			return null;
		}
		BufferedReader reader = null;
		Map<ConfigNodeType, String> map = new ConcurrentHashMap<ConfigNodeType, String>(16);
		long beginTime = System.currentTimeMillis();
		
		addConfigNode(dirPath, file.getOriginalFilename(), "file", "");
		dirPath = ConfigUtil.buildCompletePath(dirPath,file.getOriginalFilename());
		try {
			reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				String removeTokenStr = ConfigUtil.getRemoveTokenStr(line);
				if (removeTokenStr.contains("[") && removeTokenStr.contains("]")) {
					currentTitle = ConfigUtil.getConfigTitleHandle(line);
					map.put(new ConfigNodeType(ConfigUtil.buildCompletePath(dirPath, currentTitle), "title") , "");
				} else if (removeTokenStr.contains("=")) {
					// 0代表属性名，1代表属性值
					map.put(new ConfigNodeType(ConfigUtil.buildCompletePath(dirPath, currentTitle) + "/"
							+ ConfigUtil.getConfigPropertyHandle(removeTokenStr, 0), "property"),
							ConfigUtil.getConfigPropertyHandle(removeTokenStr, 1));
				}
			}
			configNodeTypeMapper.batchInsert(map.keySet());
			ConfigUploadMonitor.startMonitor();
			long middleTime = System.currentTimeMillis();
			double progress = 0.0;
			for (Map.Entry<ConfigNodeType, String> entry : map.entrySet()) {
				progress++;
				zk.createPersistent(entry.getKey().getNodePath(),true);
				zk.writeData(entry.getKey().getNodePath(), entry.getValue());
				ConfigUploadMonitor.setProgress(progress*100/map.size());
			}
			long endTime = System.currentTimeMillis();


			System.out.println("导入完成，总用时" + (endTime - beginTime)+"ms,zk用时："+(endTime-middleTime));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				file.getInputStream().close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "success";
	}

}
