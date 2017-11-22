package com.whollyframework.util.json;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.whollyframework.util.StringUtil;

public class IncludePropertyFilter extends SimpleBeanPropertyFilter {
	protected static Map<String, Set<String>> _cachePropertiesToInclude = new HashMap<String, Set<String>>();

	protected boolean include(BeanPropertyWriter writer) {
		return true;
	}

	protected boolean include(PropertyWriter writer) {
		return true;
	}
	
	protected boolean include(BeanPropertyWriter writer, Object bean) {
		return get_propertiesToInclude(bean).contains(writer.getName());
	}

	public void serializeAsField(Object bean, JsonGenerator jgen, SerializerProvider provider, BeanPropertyWriter writer)
			throws Exception {
		if (include(writer, bean)) { // 序列化时根据Bean进行判断
			writer.serializeAsField(bean, jgen, provider);
		}
	}

	/**
	 * 获取匹配的字段
	 * 
	 * @param clazz
	 * @return
	 */
	protected Set<String> getIncludePropertyName(Class<?> clazz) {
		Set<String> rtn = new java.util.HashSet<String>();
		
		Class<?> current = clazz;
		while (current != null) {
			Field[] fields = current.getDeclaredFields();
			for (Field fd : fields) {
				if (fd.isAnnotationPresent(JsonProperty.class)) {
					rtn.add(fd.getName());
				}
			}
			current = current.getSuperclass();
		}
		
		current = clazz;
		while (current != null) {
			Method[] methods = current.getDeclaredMethods();
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
			current = current.getSuperclass();
		}
		rtn.add("id");

		return rtn;
	}

	public Set<String> get_propertiesToInclude(Object bean) {
		if (_cachePropertiesToInclude.containsKey(bean.getClass().getName())) {
			return _cachePropertiesToInclude.get(bean.getClass().getName());
		} else {
			Set<String> includes = getIncludePropertyName(bean.getClass());
			_cachePropertiesToInclude.put(bean.getClass().getName(), includes);
			return includes;

		}
	}

}
