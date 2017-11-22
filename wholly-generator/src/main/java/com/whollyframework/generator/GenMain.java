package com.whollyframework.generator;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import com.whollyframework.generator.core.AbstractGenerator;
import com.whollyframework.generator.util.SystemConf;
import com.whollyframework.util.ObjectUtil;

/**
 * 代码产生器,根据类模板用FreeMarker产生代码.
 * <br>模板例子请参照template/mvc目录下的{}Controller.java.ftl
 * @author Chris Hsu
 */
public class GenMain {
  
    /**
     * 测试，根据配置文件生成相应的模块代码
     */
    public static void main(String[] args) throws IOException {
    	String objectName = "User";
    	String tableName = "T_USER";
    	String pkName = "ID";
    	String config = "gencode-conf.xml";
    	if (args!=null && args.length>0){
    		if (args.length <3){
    			throw new RuntimeException("参数错误！要求传入3个参数，实际传入"+args.length+"个参数。");
    		}
    		objectName = args[0];
    		tableName = args[1];
    		pkName = args[2];
    		
    		if (args.length > 3){
    			config = args[3];
    		}
    	}
    	
    	SystemConf.Load(config);
    	genAll(objectName, tableName, pkName);
    	System.out.println("代码已生成");
    }

    private static void genAll(String objectName, String tableName, String pkName){
    	Map<String, String> generators = SystemConf.getPropertys(SystemConf.TAG_GENERATORS);
    	Set<String> keys = SystemConf.propsKeyset();
		for (String key : keys) {
			if (!SystemConf.TAG_GENERATORS.equalsIgnoreCase(key)) {
				Map<String, String> prop = SystemConf.getPropertys(key);
				
				String generatorClassName = prop.get(SystemConf.TAG_GENERATOR);
				String generatorClazz = generators.get(generatorClassName);
				AbstractGenerator generator = (AbstractGenerator) ObjectUtil.getInstance(generatorClazz);
				generator.setGenerator(key);
				
				generator.genAll(objectName, tableName, pkName);
			}
		}
    }
}
