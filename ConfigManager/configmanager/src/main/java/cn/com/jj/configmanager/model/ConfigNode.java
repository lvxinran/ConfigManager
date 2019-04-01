package cn.com.jj.configmanager.model;

import java.util.List;
/**
 * 
 * @author lvxr
 *
 */
public class ConfigNode {
	private String title;
	private String path;
	private boolean expand;
	private List<ConfigNode> children;
	private int sortInGroup;
	private String nodeType;
	private String nodeContent;
	
	public String getNodeType() {
		return nodeType;
	}
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isExpand() {
		return expand;
	}
	public void setExpand(boolean expand) {
		this.expand = expand;
	}
	
	public int getSortInGroup() {
		return sortInGroup;
	}
	public void setSortInGroup(int sortInGroup) {
		this.sortInGroup = sortInGroup;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public List<ConfigNode> getChildren() {
		return children;
	}
	public void setChildren(List<ConfigNode> children) {
		this.children = children;
	}
	public String getNodeContent() {
		return nodeContent;
	}
	public void setNodeContent(String nodeContent) {
		this.nodeContent = nodeContent;
	}
	@Override
	public String toString() {
		return "ConfigNode [title=" + title + ", path=" + path + ", expand=" + expand + ", children=" + children
				+ ", sortInGroup=" + sortInGroup + ", nodeType=" + nodeType + ", nodeContent=" + nodeContent + "]";
	}
	
	
	

	



	
	
}
