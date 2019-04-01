package cn.com.jj.configmanager.util;
/**
 * 
 * @author lvxr
 *
 */
public class ConfigUtil {
	public String removeChar(String str) {
		str.replace("/", "");
		return str;
	}
	public static String getConfigTitleHandle(String line) {
		return line.replace("[", "").replace("]", "").replace(" ", "");
	}
	public static String getConfigPropertyHandle(String line,int index){
		return line.split("=", 2)[index];
	}
	public static String getRemoveTokenStr(String line) {
		return line.contains(";")?line.split(";")[0]:line;
	}
	public static String buildCompletePath(String parentPath,String title) {
		return new StringBuilder().append(parentPath).append("/").append(title).toString();
	}
}
