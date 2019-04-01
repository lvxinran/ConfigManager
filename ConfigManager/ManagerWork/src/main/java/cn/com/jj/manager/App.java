package cn.com.jj.manager;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import cn.com.jj.manager.util.ConfigListenerUtil;
import cn.com.jj.manager.util.SpringUtil;

/**
 * 
 * @author lvxr
 *
 */
@SpringBootApplication
@MapperScan({"cn.com.jj.manager.mapper"})
public class App {
	 
    public static void main(String[] args ) throws Exception{
    	SpringApplication.run(App.class, args);
    	//打开监听
    	SpringUtil.getBean(ConfigListenerUtil.class).openAllListener();
    	
    }
}
