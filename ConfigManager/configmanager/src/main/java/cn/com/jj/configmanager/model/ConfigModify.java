package cn.com.jj.configmanager.model;
/**
 * 
 * @author lvxr
 *
 */
public class ConfigModify {
	private String nodePath;
	private String oldTitle;
	private String modifyTitle;
	private String content;
	public String getNodePath() {
		return nodePath;
	}
	public void setNodePath(String nodePath) {
		this.nodePath = nodePath;
	}
	public String getOldTitle() {
		return oldTitle;
	}
	public void setOldTitle(String oldTitle) {
		this.oldTitle = oldTitle;
	}
	public String getModifyTitle() {
		return modifyTitle;
	}
	public void setModifyTitle(String modifyTitle) {
		this.modifyTitle = modifyTitle;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public ConfigModify(String nodePath, String oldTitle, String modifyTitle, String content) {
		super();
		this.nodePath = nodePath;
		this.oldTitle = oldTitle;
		this.modifyTitle = modifyTitle;
		this.content = content;
	}
	
}
