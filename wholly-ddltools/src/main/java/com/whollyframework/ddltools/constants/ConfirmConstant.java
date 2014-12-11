package com.whollyframework.ddltools.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author chris
 * @since 2011-12-20
 * 
 */
public class ConfirmConstant {
	/**
	 * @uml.property name="msgCodeNames"
	 * @uml.associationEnd qualifier="key:java.lang.Object java.lang.String"
	 */
	public static Map<Integer, String> msgCodeNames;

	public static final int FORM_EXIST = 0;

	public static final int FORM_DATA_EXIST = 1;

	public static final int FIELD_EXIST = 2;

	public static final int FIELD_DATA_EXIST = 3;

	public static final int FIELD_TYPE_INCOMPATIBLE = 4;

	public static final int FIELD_DUPLICATE = 5;

	static {
		msgCodeNames = new HashMap<Integer, String>();
		msgCodeNames.put(Integer.valueOf(FORM_EXIST), "表名已经存在");
		msgCodeNames.put(Integer.valueOf(FORM_DATA_EXIST), "表存在数据");
		msgCodeNames.put(Integer.valueOf(FIELD_EXIST), "字段名已经存在");
		msgCodeNames.put(Integer.valueOf(FIELD_DATA_EXIST), "字段存在数据");
		msgCodeNames.put(Integer.valueOf(FIELD_TYPE_INCOMPATIBLE), "字段名存在无效字符");
		msgCodeNames.put(Integer.valueOf(FIELD_DUPLICATE), "字段名重复");
	}

	public static String getMsgKeyName(int keyCode) {
		return (String) msgCodeNames.get(Integer.valueOf(keyCode));
	}
}
