package cn.com.jj.configmanager.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.com.jj.configmanager.mapper.ConfigNodeTypeMapper;
import cn.com.jj.configmanager.model.ConfigModify;
import cn.com.jj.configmanager.model.ConfigNode;
import cn.com.jj.configmanager.model.ConfigNodeType;
import cn.com.jj.configmanager.service.ConfigManagerService;
import cn.com.jj.configmanager.service.ConfigUploadMonitor;

/**
 * 
 * @author lvxr
 *
 */
@CrossOrigin
@RestController
@EnableAsync
public class ConfigManagerController {

	@Autowired
	private ConfigManagerService configManagerService;
	@Autowired
	private ConfigNodeTypeMapper configNodeTypeMapper;
	/**
	 * 查询zookeeper中所有节点
	 * 
	 * @return
	 * @throws Exception
	 */

	@GetMapping("allNodes")
	public ConfigNode showAllNodes() throws Exception {
		ConfigNode node = configManagerService.ls("/jj", "jj", 1);
		return node;
	}

	@GetMapping("nodeType")
	public List<ConfigNodeType> showType() {
		return configNodeTypeMapper.showAllType();
	}

	/**
	 * 添加节点
	 * 
	 * @param parentNodePath
	 * @param currentTitle
	 * @return
	 */
	@PostMapping("node")
	public boolean addNode(String parentNodePath, String currentTitle,String nodeType, String content) {
		return configManagerService.addConfigNode(parentNodePath, currentTitle, nodeType, content);
	}

	/**
	 * 修改节点
	 * 
	 * @param nodePath
	 * @param oldTitle	
	 * @param modifyTitle
	 * @return
	 */
	@PutMapping("node")
	public boolean modifyNode(@RequestBody  ConfigModify modifyObject) {

		configManagerService.modifyContent(modifyObject.getNodePath(), modifyObject.getContent());
		if (!modifyObject.getOldTitle().equals(modifyObject.getModifyTitle())) {
			configManagerService.modifyNode(modifyObject);	
		}
		return true;
	}

	/**
	 * 删除某个节点下所有节点
	 * 
	 * @param path
	 * @return
	 */
	@DeleteMapping("node")
	public boolean removeNode(String path) {
		return configManagerService.removeNode(path);
	}

	/**
	 * 查询某个路径下的配置信息
	 * 
	 * @param path
	 * @return
	 */
	@GetMapping("showNodeInfo")
	public String showNodeInfo(String path) {
		return configManagerService.showNodeInfo(path);
	}

	/**
	 * 配置文件预览
	 * 
	 * @param path
	 * @return
	 */
	@GetMapping("showPreview")
	public List<ConfigNode> showPreview(String path) {
		return configManagerService.showPreview(path);
	}

	/**
	 * 配置文件上传
	 * 	  
	 * @param file
	 * @param request
	 * @return
	 */
	@PostMapping("upload")
	public String handleFileUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		return configManagerService.upload(file,request.getParameter("path"));
	} 
	@GetMapping("uploadProcess")
	public Double uploadProcess() {		
		return ConfigUploadMonitor.getProgress();
	}	
	@GetMapping("upLoadCheck")
	public boolean upLoadCheck(String path,String title) {
		return configManagerService.upLoadCheck(path, title);
	}
}
