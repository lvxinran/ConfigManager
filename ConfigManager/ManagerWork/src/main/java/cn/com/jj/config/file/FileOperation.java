package cn.com.jj.config.file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileOperation {	
	
	public static void synToFile(String path,String content) throws IOException {
		

		File file = new File("d:"+path);
		File fileParent = file.getParentFile();
		if(!fileParent.exists()){
			fileParent.mkdirs();
		}
		file.createNewFile();
		FileWriter w=new FileWriter(file);
		try {
		    w.write(content);
			
			        } catch (IOException e) {
			         e.printStackTrace();
			 }finally {
				 w.close();
			}
	}
	

}
