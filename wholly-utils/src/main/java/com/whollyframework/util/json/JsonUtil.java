package com.whollyframework.util.json;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.whollyframework.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * @proejct yun-app
 * @author denny
 * @date 2012-11-29
 * @copyright 2012 www.yunbosource.com Inc. All rights reserved
 * 
 */
public class JsonUtil {
	private static ObjectMapper mapper;
	
	private static ObjectMapper objectMapper;

	public static Collection<Object> toCollection(String JSONStr) {
		Collection<Object> rtn = new ArrayList<Object>();

		JSONArray array = JSONArray.fromObject(JSONStr);
		for (int i = 0; i < array.size(); i++) {
			Object obj = array.get(i);
			if (obj instanceof JSONObject) {
				rtn.add(toMap((JSONObject) obj));
			} else {
				rtn.add(obj);
			}
		}

		return rtn;
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public static Collection<Object> toCollection(String JSONStr, Class<?> objClass) {
		JSONArray array = JSONArray.fromObject(JSONStr);
		List<Object> list = JSONArray.toList(array, objClass);
		return list;
	}

	public static Map<String, Object> toMap(String jsonStr) {
		JSONObject jsonObject = JSONObject.fromObject(jsonStr);
		return toMap(jsonObject);
	}

	private static Map<String, Object> toMap(JSONObject jsonObject) {
		Map<String, Object> rtn = new HashMap<String, Object>();

		if (jsonObject.isNullObject()) {
			return null;
		}
		for (Iterator<?> iterator = jsonObject.keys(); iterator.hasNext();) {
			String key = (String) iterator.next();
			Object obj = jsonObject.get(key);

			if (obj instanceof JSONArray) {
				rtn.put(key, toArray((JSONArray) obj));
			} else if (obj instanceof JSONObject) {
				rtn.put(key, toMap((JSONObject) obj));
			} else {
				rtn.put(key, obj);
			}
		}

		return rtn;
	}

	private static Object[] toArray(JSONArray jsonArray) {
		Object[] rtn = new Object[jsonArray.size()];

		for (int i = 0; i < jsonArray.size(); i++) {
			Object obj = jsonArray.get(i);
			if (obj instanceof JSONObject) {
				rtn[i] = toMap((JSONObject) obj);
			} else {
				rtn[i] = obj;
			}
		}

		return rtn;
	}

	public static Object toBean(String jsonStr, Class<?> objClass) {
		try {
			return getMapper().readValue(jsonStr, objClass);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 含有日期字段的bean转换为json，日期格式在JsonDateValueProcessor类中指定
	 * @param obj
	 * @return
	 */
	public static String toJsonDate(Object obj) {
		JsonConfig jsonConfig = new JsonConfig();  
        jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());  
        JSONObject jo = JSONObject.fromObject(obj, jsonConfig);
		return jo.toString();
	}

	public static String toJson(Object obj) {
		try {
//			SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept(getAllIncludePropertyName());
			SimpleBeanPropertyFilter filter = new IncludePropertyFilter();
			FilterProvider fp = new SimpleFilterProvider().addFilter("includeFilter", filter);

			return getMapper().writer(fp).writeValueAsString(obj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";
	}
	
	public static String toJson2(Object obj) {
		try {
//			SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept(getAllIncludePropertyName());
			SimpleBeanPropertyFilter filter = new IncludePropertyFilter();
			FilterProvider fp = new SimpleFilterProvider().addFilter("includeFilter", filter);

			return getMapper2().writer(fp).writeValueAsString(obj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";
	}

	protected static Set<String> getIncludePropertyName(Class<?> clazz) {
		Set<String> rtn = new java.util.HashSet<String>();
		Field[] fields = clazz.getDeclaredFields();
		for (Field fd : fields) {
			if (fd.isAnnotationPresent(JsonProperty.class)) {
				rtn.add(fd.getName());
			}
		}

		Method[] methods = clazz.getDeclaredMethods();
		for (Method md : methods) {
			if (md.isAnnotationPresent(JsonProperty.class)) {
				String name = "";
				if (md.getName().startsWith("set")) {
					name = md.getName().replaceFirst("set", "");
				} else if (md.getName().startsWith("is")) {
					name = md.getName().replaceFirst("is", "");
				} else if (md.getName().startsWith("get")) {
					name = md.getName().replaceFirst("get", "");
				}

				if (!StringUtil.isBlank(name)) {
					name = name.replace(name.charAt(0), (char) (name.charAt(0) + 32));
					rtn.add(name);
				}
			}
		}

		return rtn;
	}

	public static ObjectMapper getMapper() {
		if (mapper == null) {
			mapper = new ObjectMapper(); // can reuse, share globally
			mapper.setAnnotationIntrospector(new CustomFilteringIntrospector());
			
		}

		return mapper;
	}
	
	public static ObjectMapper getMapper2() {
		if (objectMapper == null) {
			objectMapper = new ObjectMapper(); // can reuse, share globally
			objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
			//使Jackson JSON支持Unicode编码非ASCII字符  
		    SimpleModule module = new SimpleModule();  
		    module.addSerializer(String.class, new StringUnicodeSerializer());  
		    objectMapper.registerModule(module);  
		    //设置null值不参与序列化(字段不被显示)  
		    objectMapper.setSerializationInclusion(Include.NON_NULL);  
		    objectMapper.getSerializerProvider().setNullKeySerializer(new CustomNullKeySerializer());
		    objectMapper.setAnnotationIntrospector(new CustomFilteringIntrospector());
			
		}

		return objectMapper;
	}

	
	
	public static void main(String[] args) throws Exception {
		// 1. Collection to String
		Collection<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> node = new HashMap<String, Object>();
		node.put("id","001");
		
		list.add(node);
		Map<String, Object> attr = new HashMap<String, Object>();
		Map<String, Object> valueMap = new HashMap<String, Object>();
		valueMap.put("aaa", "aaa");
		attr.put(
				"valueMap",
				"{11df-8a45-2d46df42-be72-dfc9d0c0080b:'名称5',11df-8a45-2d4b9a33-be72-dfc9d0c0080b:'n1',11df-8a45-2d4b9a34-be72-dfc9d0c0080b:'@amp;nbsp'}");
		attr.put("nullvalue", null);
		attr.put("ustt", "<a>sfdsdf<test></a>\"/>");
		node.put("attrs", attr);

		String json = toJson(attr);
		System.out.println(json);
		
		json = toJson(list);
		System.out.println(json);
		
		json = toJson2(list);
		System.out.println(json);
	}
}
