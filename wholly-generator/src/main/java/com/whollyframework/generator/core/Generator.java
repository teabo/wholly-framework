package com.whollyframework.generator.core;

import java.util.Map;

public interface Generator {

	public void generate(String templateFileName, Map<String,Object> data,String fileName) ;

	public void genAll(String objectName, String tableName, String pkName);
}
