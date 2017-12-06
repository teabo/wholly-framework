package com.whollyframework.web.aspect;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LoggerUtil {

	protected static final Map<String, String> OPERATIONS = new HashMap();
	
	static {
		OPERATIONS.put("不为空", "snull_");
		OPERATIONS.put("为空", "null_");
		OPERATIONS.put("为空", "blank_");
		OPERATIONS.put("不等于0", "nz_");
		OPERATIONS.put("正数", "pn_");
		OPERATIONS.put("负数", "nn_");
		OPERATIONS.put("=", "n_");
		OPERATIONS.put("=", "d_");
		OPERATIONS.put("=", "s_");
		OPERATIONS.put("!=", "ne_");
		OPERATIONS.put("!=", "dne_");
		OPERATIONS.put("!=", "sne_");
		OPERATIONS.put(">", "gt_");
		OPERATIONS.put(">", "dgt_");
		OPERATIONS.put(">", "sgt_");
		OPERATIONS.put("<", "lt_");
		OPERATIONS.put("<", "dlt_");
		OPERATIONS.put("<", "slt_");
		OPERATIONS.put(">=", "gte_");
		OPERATIONS.put(">=", "dgte_");
		OPERATIONS.put(">=", "sgte_");

		OPERATIONS.put("<=", "lte_");
		OPERATIONS.put("<=", "dlte_");
		OPERATIONS.put("<=", "slte_");

		OPERATIONS.put("like", "sm_");
	}
	
	public static String getOperator(String param){
		// 得到下划线前面的内容
		String start = param.substring(0, param.indexOf("_")+1);//开头的字符串
		
		Iterator iter = OPERATIONS.entrySet().iterator(); 
		while (iter.hasNext()) { 
		    Map.Entry entry = (Map.Entry) iter.next(); 
		    Object key = entry.getKey(); 
		    Object val = entry.getValue();
		    
		    if(val.toString().equalsIgnoreCase(start)){
		    	return key.toString();
		    }
		}
		return ":";
	}
}
