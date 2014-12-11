package com.whollyframework.util.mapping;

import java.util.HashMap;
import java.util.Map;

import com.whollyframework.util.StringUtil;

public class BeanMapping {

	private static Map<Class<?>, Map<String, String>> ALLFIELDS = new HashMap<Class<?>, Map<String, String>>();
	
	private static Map<Class<?>, Map<String, String>> ALLMAPPINGS = new HashMap<Class<?>, Map<String, String>>();
	
	public static synchronized void addFieldMapping(Class<?> clazz, String fieldName, String mappingName){
		Map<String, String> fields = ALLFIELDS.get(clazz);
		Map<String, String> mappings = ALLMAPPINGS.get(clazz);
		if (fields==null){
			fields = new HashMap<String, String>();
			ALLFIELDS.put(clazz, fields);
			mappings = new HashMap<String, String>();
			ALLMAPPINGS.put(clazz, mappings);
		}
		String omapping = fields.get(fieldName);
		if (StringUtil.isBlank(omapping)){
			fields.put(fieldName, mappingName);
			mappings.put(mappingName, fieldName);
		} else {
			if (!omapping.equalsIgnoreCase(mappingName)){
				mappings.remove(omapping);
				fields.put(fieldName, mappingName);
				mappings.put(mappingName, fieldName);
			}
		}
	}
	
	public static String getFieldName(Class<?> clazz, String mappingName){
		return getFieldName(clazz, mappingName, null);
	}
	
	public static String getFieldName(Class<?> clazz, String mappingName, String defaultValue){
		Map<String, String> mappings = ALLMAPPINGS.get(clazz);
		if (mappings!=null){
			String value =mappings.get(mappingName);
			return StringUtil.isBlank(value)?defaultValue: value;
		}
		return defaultValue;
	}
	
	public static String getMappingName(Class<?> clazz, String fieldName){
		return getMappingName(clazz, fieldName, null);
	}
	
	public static String getMappingName(Class<?> clazz, String fieldName, String defaultValue){
		Map<String, String> fields = ALLFIELDS.get(clazz);
		if (fields!=null){
			String value =fields.get(fieldName);
			return StringUtil.isBlank(value)?defaultValue: value;
		}
		return defaultValue;
	}
	
	public static boolean isCached(Class<?> clazz){
		return ALLFIELDS.get(clazz)!=null;
	}
}
