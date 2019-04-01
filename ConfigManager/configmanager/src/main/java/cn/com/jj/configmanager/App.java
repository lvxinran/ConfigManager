package cn.com.jj.configmanager;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * 
 * @author lvxr
 *
 */
@SpringBootApplication
@MapperScan({"cn.com.jj.configmanager.mapper"})
public class App 
{
    public static void main( String[] args ){
    	SpringApplication.run(App.class, args);
//    	new ConfigManagerService().openListener("/jj/jjconfig1");
    }
}
