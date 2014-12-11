package com.whollyframework.util;

import com.whollyframework.util.property.PropertyUtil;

public class DBUtil {
	
	public final static String DBTYPE_MYSQL="MySQL";
	
	public final static String DBTYPE_ORACLE="Oracle";
	
	public final static String DBTYPE_SQLSERVER="SQLServer";

	/**
	 * 当前运行的数据库类型
	 */
	public final static String RUNTIME_DBTYPE;
	
	static{
		PropertyUtil.load("jdbc");
		RUNTIME_DBTYPE = PropertyUtil.get("jdbc.dbtype");
	}
}
