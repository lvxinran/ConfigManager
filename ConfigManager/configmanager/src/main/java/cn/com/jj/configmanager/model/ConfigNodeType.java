package cn.com.jj.configmanager.model;
/**
 * 
 * @author lvxr
 *
 */
public class ConfigNodeType{


	private String nodePath;
	private String nodetype;
	public String getNodePath() {
		return nodePath;
	}
	public void setNodePath(String nodePath) {
		this.nodePath = nodePath;
	}
	public String getNodetype() {
		return nodetype;
	}
	public void setNodetype(String nodetype) {
		this.nodetype = nodetype;
	}
	public ConfigNodeType(String nodePath, String nodetype) {
		super();
		this.nodePath = nodePath;
		this.nodetype = nodetype;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nodePath == null) ? 0 : nodePath.hashCode());
		result = prime * result + ((nodetype == null) ? 0 : nodetype.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConfigNodeType other = (ConfigNodeType) obj;
		if (nodePath == null) {
			if (other.nodePath != null)
				return false;
		} else if (!nodePath.equals(other.nodePath))
			return false;
		if (nodetype == null) {
			if (other.nodetype != null)
				return false;
		} else if (!nodetype.equals(other.nodetype))
			return false;
		return true;
	}
	
	
}
