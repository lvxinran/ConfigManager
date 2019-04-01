package cn.com.jj.manager.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;
/**
 * 
 * @author lvxr
 *
 */
@Service
public interface ConfigNodeTypeMapper {
	
	/**
	 * 
	 * @return
	 */
	@Insert("Insert into ConfigNodeType(nodePath, nodetype) values(#{nodePath}, #{nodetype})")
	void insertToNodeType(@Param("nodePath")String nodePath,@Param("nodetype")String nodetype);
	@Select("select nodetype from ConfigNodeType where nodePath=#{nodePath}")
	String showTypeByPath(@Param("nodePath")String nodePath);
	@Delete("Delete from ConfigNodeType where nodePath=#{nodePath}")
	void deleteNodeTypeByPath(String nodePath);
	@Update("update ConfigNodeType set nodePath=#{newPath} where nodePath=#{oldPath}")
	void updateNodePath(@Param("oldPath")String oldPath,@Param("newPath")String newPath);

}
