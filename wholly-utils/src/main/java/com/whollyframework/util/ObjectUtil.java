package com.whollyframework.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.converters.AbstractArrayConverter;
import org.apache.log4j.Logger;

import com.whollyframework.annotation.Column;
import com.whollyframework.annotation.Ignore;
import com.whollyframework.util.json.IncludePropertyFilter;
import com.whollyframework.util.mapping.MappingField;

/**
 * @author chris
 * @date 2012-11-29
 * 
 */
public class ObjectUtil {
	public final static Logger log = Logger.getLogger(ObjectUtil.class);

	private static final String GET_METHOD = "get";

	private static final int GET_METHOD_LEN = GET_METHOD.length();

	private static final String SET_METHOD = "set";

	private static final int SET_METHOD_LEN = SET_METHOD.length();

	private static Map<Class<?>, Map<String, Method>> sMethods;

	private static Map<Class<?>, Map<String, Method>> gMethods;

	private static Map<Class<?>, Map<String, MappingField>> allMappingFields;

	private static final Map<Class<?>, Map<MethodKey, Method>> class2MethodMap = new ConcurrentHashMap<Class<?>, Map<MethodKey, Method>>();

	/**
	 * 类与方法的映射（方法名全部大写）
	 */
	private static final Map<Class<?>, Map<MethodKey, Method>> class2MethodUpperCaseMap = new ConcurrentHashMap<Class<?>, Map<MethodKey, Method>>();

	/**
	 * 基础类型与包装类型的映射
	 */
	static final Map<Class<?>, Class<?>> base2PackClassMap = new HashMap<Class<?>, Class<?>>();

	static {
		sMethods = new HashMap<Class<?>, Map<String, Method>>();
		gMethods = new HashMap<Class<?>, Map<String, Method>>();
		allMappingFields = new HashMap<Class<?>, Map<String, MappingField>>();

		// 注册转换器
		BeanUtilsBean util = BeanUtilsBean.getInstance();
		util.getConvertUtils().register(new IntegerArrayConverter(), int[].class);

		base2PackClassMap.put(int.class, Integer.class);
		base2PackClassMap.put(double.class, Double.class);
		base2PackClassMap.put(long.class, Long.class);
		base2PackClassMap.put(boolean.class, Boolean.class);
		base2PackClassMap.put(char.class, Character.class);
		base2PackClassMap.put(byte.class, Byte.class);
		base2PackClassMap.put(short.class, Short.class);
		base2PackClassMap.put(float.class, Float.class);
	}

	public static Map<String, Method> getGMethods(Class<?> clazz, String... removals) {
		return getGMethods(clazz,
				removals != null && removals.length > 0 && removals[0] != null ? Arrays.asList(removals) : null);
	}

	public static Map<String, Method> getGMethods(Class<?> clazz, List<String> removals) {
		if (removals == null) {
			removals = new ArrayList<String>();
		} else {
			removals = new ArrayList<String>(removals);
		}
		// 固定移除Object的getClass方法产生的字段值
		removals.add("class");
		return getMethods(clazz, GET_METHOD, GET_METHOD_LEN, gMethods, removals);
	}

	public static Map<String, Method> getSMethods(Class<?> clazz, String... removals) {
		return getSMethods(clazz,
				removals != null && removals.length > 0 && removals[0] != null ? Arrays.asList(removals) : null);
	}

	public static Map<String, Method> getSMethods(Class<?> clazz, List<String> removals) {
		return getMethods(clazz, SET_METHOD, SET_METHOD_LEN, sMethods, removals);
	}

	private static Map<String, Method> getMethods(Class<?> clazz, String prefix, int prefixLen,
			Map<Class<?>, Map<String, Method>> allMethods, List<String> removals) {
		Map<String, Method> result = allMethods.get(clazz);
		if (result == null) {
			// 提取所有字段对应的getter/setter方法
			result = new HashMap<String, Method>();
			Method[] cms = clazz.getMethods();
			String methodName = null;
			Ignore ignore = null;
			for (Method method : cms) {
				if ((methodName = method.getName()).startsWith(prefix)) {
					ignore = method.getAnnotation(Ignore.class);
					if (ignore != null && ignore.value()) {
						continue;
					}
					method.setAccessible(true);
					result.put(makeKey(methodName, prefixLen), method);
				}
			}
			allMethods.put(clazz, new HashMap<String, Method>(result));
		}
		// 允许每次根据具体情况移除特定字段
		if (removals != null) {
			for (String name : removals) {
				result.remove(name.toLowerCase());
			}
		}
		return result;
	}

	private static String makeKey(String methodName, int len) {
		StringBuffer fieldName = new StringBuffer(32);
		char[] chs = methodName.substring(len).toCharArray();
		chs[0] = Character.toLowerCase(chs[0]);
		for (char ch : chs) {
			// if (Character.isUpperCase(ch)) {
			// fieldName.append('_');
			// }
			// fieldName.append(Character.toLowerCase(ch));
			fieldName.append(ch);
		}
		return fieldName.toString();
	}

	public static Map<String, MappingField> getColumnFields(Class<?> clazz, String... removals) {
		return getColumnFields(clazz,
				removals != null && removals.length > 0 && removals[0] != null ? Arrays.asList(removals) : null);
	}

	private static Map<String, MappingField> getColumnFields(Class<?> clazz, List<String> removals) {
		Map<String, MappingField> result = allMappingFields.get(clazz);
		if (result == null) {
			// 提取所有字段
			result = new HashMap<String, MappingField>();
			Field[] fields = clazz.getDeclaredFields();
			String fieldName = null;
			Ignore ignore = null;
			Column column = null;
			for (int i = 0; i < fields.length; i++) {
				if (Modifier.isStatic(fields[i].getModifiers()) || Modifier.isFinal(fields[i].getModifiers())) {
					continue;
				}
				ignore = fields[i].getAnnotation(Ignore.class);
				if (ignore != null && ignore.value()) {
					continue;
				}

				column = fields[i].getAnnotation(Column.class);
				if (column != null && !StringUtil.isBlank(column.value())) {
					MappingField field = new MappingField();
					// 以匹配getter/setter键
					fieldName = makeKey(fields[i].getName(), 0);
					field.setName(fieldName);
					field.setColumn_name(column.value());
					result.put(fieldName, field);
				}
			}

			allMappingFields.put(clazz, result);
		}
		// 允许每次根据具体情况移除特定字段
		if (removals != null) {
			for (String name : removals) {
				result.remove(name.toLowerCase());
			}
		}

		return result;
	}

	public static String[] getFieldNames(String[] columnNames, Class<?> clazz, String... removals) {
		return getFieldNames(columnNames, clazz,
				removals != null && removals.length > 0 && removals[0] != null ? Arrays.asList(removals) : null);
	}

	public static String[] getFieldNames(String[] columnNames, Class<?> clazz, List<String> removals) {
		Map<String, MappingField> fields = getColumnFields(clazz, removals);
		if (fields.size() > 0)
			for (int i = 0; i < columnNames.length; i++) {
				columnNames[i] = getMethodName(fields, columnNames[i]);
			}
		return columnNames;
	}

	private static String getMethodName(Map<String, MappingField> fields, String columnName) {
		for (Iterator<MappingField> iterator = fields.values().iterator(); iterator.hasNext();) {
			MappingField field = iterator.next();
			if (columnName.equalsIgnoreCase(field.getColumn_name())) {
				return field.getName();
			}
		}
		return columnName;
	}

	public static Object copyProperties(Object dest, Object orig)
			throws IllegalAccessException, InvocationTargetException {
		BeanUtils.copyProperties(dest, orig);
		return dest;
	}

	/**
	 * 直接使用对象的属性进行复制
	 * 
	 * @param dest
	 *            目标对象
	 * @param orig
	 *            原对象
	 * @param excludeNull
	 *            是否排除空值
	 */
	public static void copyByFields(Object dest, Object orig, boolean excludeNull) {
		Field[] fields = orig.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			try {
				fields[i].setAccessible(true);
				if (Modifier.isFinal(fields[i].getModifiers())) {
					continue;
				}

				Object value = fields[i].get(orig);
				if (excludeNull) {
					if (value != null) {
						fields[i].set(dest, value);
					}
				} else {
					fields[i].set(dest, value);
				}

			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	public static Collection<Field> getClassAllFields(Class<?> clazz) {
		Collection<Field> rtn = new ArrayList<Field>();
		Class<?> current = clazz;

		while (current != null) {
			Field[] fields = current.getDeclaredFields();
			rtn.addAll(Arrays.asList(fields));

			current = current.getSuperclass();
		}

		return rtn;
	}

	public static void setProperty(Object bean, String name, Object value) {
		setProperty(bean, name, value, true);
	}

	public static void setProperty(Object bean, String name, Object value, boolean caseSensitive) {
		if (value == null) {
			// Nothing to do
			return;
		}
		if (name.indexOf("_") != -1) {
			name = name.replaceAll("_", "");
		}

		StringBuffer methodName = new StringBuffer(name);
		methodName.setCharAt(0, Character.toUpperCase(methodName.charAt(0)));
		methodName.insert(0, SET_METHOD);

		Method method = getMethodByName(bean, methodName.toString(), value, caseSensitive);
		if (method != null) {
			invokeMethod(bean, method, new Object[] { value });
		}
	}

	/**
	 * 根据方法名称获取方法
	 * 
	 * @param bean
	 * @param methodName
	 * @param caseSensitive
	 * @return
	 */
	private static Method getMethodByName(Object bean, String methodName, Object value, boolean caseSensitive) {
		if (class2MethodMap.containsKey(bean.getClass())) { // 从缓存中获取
			if (caseSensitive) {
				Map<MethodKey, Method> name2MethodMap = class2MethodMap.get(bean.getClass());
				return name2MethodMap.get(new MethodKey(methodName, new Class[] { value.getClass() }));
			} else {
				Map<MethodKey, Method> name2MethodUpperCaseMap = class2MethodUpperCaseMap.get(bean.getClass());
				return name2MethodUpperCaseMap
						.get(new MethodKey(methodName.toUpperCase(), new Class[] { value.getClass() }));
			}
		} else {
			Method[] methods = bean.getClass().getMethods();
			MethodKey otherKey = new MethodKey(methodName, new Class[] { value.getClass() });

			for (int i = 0; i < methods.length; i++) {
				Method method = methods[i];
				MethodKey key = new MethodKey(method.getName(), method.getParameterTypes());

				if (caseSensitive) {
					if (key.equals(otherKey)) {
						return method;
					}
				} else {
					if (key.equalsIgnoreCase(otherKey)) {
						return method;
					}
				}
			}
		}

		return null;
	}

	public static Collection<Method> getWriteMethods(Object bean) {
		Collection<Method> rtn = new ArrayList<Method>();

		Method[] methods = bean.getClass().getMethods();
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getName().startsWith(SET_METHOD)) {
				rtn.add(methods[i]);
			}
		}
		return rtn;
	}

	public static void invokeMethod(Object bean, Method method, Object[] values) {
		try {
			boolean allowInvoke = true;

			Class<?>[] paramClazzs = method.getParameterTypes();

			if (paramClazzs.length != values.length) {
				allowInvoke = false;
			} else {
				for (int i = 0; i < paramClazzs.length; i++) {
					// 是否可以转型为ParameterType

					if (!paramClazzs[i].isAssignableFrom(values[i].getClass()) && !paramClazzs[i].isPrimitive()) {
						allowInvoke = false;
						break;
					}
				}
			}

			if (allowInvoke) {
				method.invoke(bean, values);
			}
		} catch (IllegalArgumentException e) {
			log.debug(method.getName() + " error: " + e.getMessage());
		} catch (IllegalAccessException e) {
			log.debug(method.getName() + " error: " + e.getMessage());
		} catch (InvocationTargetException e) {
			log.debug(method.getName() + " error: " + e.getMessage());
		}
	}

	/**
	 * 使用序列化进行深度克隆
	 * 
	 * @param obj
	 * @return
	 */
	public static Object clone(Object obj) {
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bout);
			out.writeObject(obj);
			out.close();

			ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
			ObjectInputStream in = new ObjectInputStream(bin);
			Object newObj = in.readObject();
			in.close();

			return newObj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean isMap(Object obj) {
		if (obj != null) {
			if (Map.class.isAssignableFrom(obj.getClass())) {
				return true;
			}
		}

		return false;
	}

	public static boolean isArray(Object obj) {
		if (obj != null) {
			if (obj.getClass().isArray()) {
				return true;
			}
		}

		return false;
	}

	public static boolean isCollection(Object obj) {
		if (obj != null && Collection.class.isAssignableFrom(obj.getClass())) {
			return true;
		}
		return false;
	}

	/**
	 * 对于org.apache.commons.beanutils.converters.IntegerArrayConverter的扩展,
	 * 处理Object[]对象
	 * 
	 * @author Nicholas
	 * 
	 */
	public static class IntegerArrayConverter extends AbstractArrayConverter {
		int[] model = new int[0];
		Object[] objects = new Object[0];

		/**
		 * @SuppressWarnings convert方法不支持泛型 *(non-Javadoc)
		 * @see org.apache.commons.beanutils.converters.AbstractArrayConverter#convert(java.lang.Class,
		 *      java.lang.Object)
		 */
		@SuppressWarnings("rawtypes")
		public Object convert(Class type, Object value) {
			// Deal with a null value
			if (value == null) {
				if (useDefault) {
					return (defaultValue);
				} else {
					throw new ConversionException("No value specified");
				}
			}

			// Deal with the no-conversion-needed case
			if (model.getClass() == value.getClass()) {
				return (value);
			}

			// Deal with input value as a String array
			if (strings.getClass() == value.getClass()) {
				try {
					String values[] = (String[]) value;
					int results[] = new int[values.length];
					for (int i = 0; i < values.length; i++) {
						results[i] = Integer.parseInt(values[i]);
					}
					return (results);
				} catch (Exception e) {
					if (useDefault) {
						return (defaultValue);
					} else {
						throw new ConversionException(value.toString(), e);
					}
				}
			}

			// Deal with input value as a Object array
			if (objects.getClass() == value.getClass()) {
				try {
					Object[] values = (Object[]) value;
					int results[] = new int[values.length];

					for (int i = 0; i < values.length; i++) {
						if (values[i].getClass() == Integer.class) {
							results[i] = (((Integer) values[i]).intValue());
						} else if (values[i].getClass() == String.class) {
							results[i] = Integer.parseInt((String) values[i]);
						}
					}

					return results;
				} catch (Exception e) {
					if (useDefault) {
						return (defaultValue);
					} else {
						throw new ConversionException(value.toString(), e);
					}
				}
			}

			// Parse the input value as a String into elements
			// and convert to the appropriate type
			try {
				List list = parseElements(value.toString());
				int results[] = new int[list.size()];
				for (int i = 0; i < results.length; i++) {
					results[i] = Integer.parseInt((String) list.get(i));
				}
				return (results);
			} catch (Exception e) {
				if (useDefault) {
					return (defaultValue);
				} else {
					throw new ConversionException(value.toString(), e);
				}
			}
		}
	}

	public static String getSimpleName(Class<?> clazz) {
		String fullName = clazz.getName();
		String simpleName = fullName.substring(fullName.lastIndexOf(".") + 1, fullName.length());
		return simpleName;
	}

	public static Object convertToMap(Class<?> type, Map<?, ?> map)
			throws IntrospectionException, IllegalAccessException, InstantiationException, InvocationTargetException {
		BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
		Object obj = type.newInstance(); // 创建 JavaBean 对象

		// 给 JavaBean 对象的属性赋值
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();

			if (map.containsKey(propertyName)) {
				// 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
				Object value = map.get(propertyName);

				Object[] args = new Object[1];
				args[0] = value;

				descriptor.getWriteMethod().invoke(obj, args);
			}
		}
		return obj;
	}

	/**
	 * 将对象转换为MAP
	 * 
	 * @param bean
	 * @return
	 */
	public static Map<Object, Object> convertToMap(Object bean) {
		IncludePropertyFilter filter = new IncludePropertyFilter();
		Set<String> props = filter.get_propertiesToInclude(bean);

		Class<?> type = bean.getClass();
		Map<Object, Object> returnMap = new HashMap<Object, Object>();

		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(type);
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (int i = 0; i < propertyDescriptors.length; i++) {
				PropertyDescriptor descriptor = propertyDescriptors[i];
				String propertyName = descriptor.getName();
				if (!propertyName.equals("class") && props.contains(propertyName)) {
					Method readMethod = descriptor.getReadMethod();
					try {
						Object result = readMethod.invoke(bean, new Object[0]);
						if (result != null) {
							returnMap.put(propertyName, result);
						} else {
							returnMap.put(propertyName, "");
						}
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}

				}
			}
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}

		return returnMap;
	}

	/**
	 * 获取类型
	 * 
	 * @param type
	 * @return
	 */
	public static Class<?> getClass(String type) {
		Class<?> result = null;
		try {
			result = Class.forName(type);
		} catch (Exception e) {
		}
		return result;
	}

	public Constructor<?> getConstructor(String type, Class<?>... parameterTypes)
			throws NoSuchMethodException, SecurityException {
		Constructor<?> result = null;
		try {
			result = getClass(type).getConstructor(parameterTypes);
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * 获取实例
	 * 
	 * @param type
	 * @return
	 */
	public static Object getInstance(String type) {
		Object result = null;
		try {
			result = getClass(type).newInstance();
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * 获取实例
	 * 
	 * @param data
	 * @return
	 */
	public static Object getInstance(byte[] data) {
		Object result = null;
		ByteArrayInputStream buffer = new ByteArrayInputStream(data);
		ObjectInputStream is = null;
		try {
			is = new ObjectInputStream(buffer);
			result = is.readObject();
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			close(is);
			close(buffer);
		}
		return result;
	}

	/**
	 * 获取字节码
	 * 
	 * @param instance
	 * @return
	 */
	public static byte[] getBytes(Object instance) {
		byte[] result = null;
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		ObjectOutputStream os = null;
		try {
			os = new ObjectOutputStream(buffer);
			os.writeObject(instance);
			result = buffer.toByteArray();
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			close(os);
			close(buffer);
		}
		return result;
	}

	private static void close(Closeable instance) {
		if (instance != null) {
			try {
				instance.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * @param args
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IntrospectionException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 */
	public static void main(String[] args) throws IntrospectionException, IllegalAccessException,
			InvocationTargetException, IllegalArgumentException, SecurityException, NoSuchFieldException {
		// FormField field = new InputField();
		// field.setId("1001");
		// field.setFieldtype("test");
		// System.out.println(convertToMap(field));
	}
}

class MethodKey implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5322967332090584316L;

	String methodName;

	Class<?>[] methodParameters;

	public MethodKey(String methodName, Class<?>[] methodParameters) {
		this.methodName = methodName;
		this.methodParameters = methodParameters;
	}

	// public native int hashCode();

	public String toString() {
		StringBuffer tmp = new StringBuffer();
		if (methodParameters.length > 0) {
			tmp.append("(");
			for (int i = 0; i < methodParameters.length; i++) {
				if (methodParameters[i] != null)
					tmp.append(methodParameters[i]).append(",");
			}
			tmp.deleteCharAt(tmp.lastIndexOf(","));
			tmp.append(")");
		}

		return methodName + tmp;
	}

	public boolean equals(Object obj) {
		if (obj != null && obj instanceof MethodKey) {
			MethodKey ck = (MethodKey) obj;
			if ((methodName != null && methodName.equals(ck.methodName))) {
				if (methodParameters != null) {
					if (methodParameters.length == ck.methodParameters.length) {
						for (int i = 0; i < methodParameters.length; i++) {
							Class<?> param1 = methodParameters[i];
							Class<?> param2 = ck.methodParameters[i];
							if (param1 != null) {
								if (param2.isPrimitive()) {
									Class<?> packageClass = (Class<?>) ObjectUtil.base2PackClassMap.get(param2);
									if (!packageClass.equals(param1)) {
										return false;
									}
								} else {
									if (!param2.isAssignableFrom(param1)) {
										return false;
									}
								}
							} else {
								// 参数都未空
								if (param2 != null)
									return false;
							}
						}
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean equalsIgnoreCase(Object obj) {
		if (obj != null && obj instanceof MethodKey) {
			MethodKey ck = (MethodKey) obj;
			if ((methodName != null && methodName.equalsIgnoreCase(ck.methodName))) {
				if (methodParameters != null) {
					if (methodParameters.length == ck.methodParameters.length) {
						for (int i = 0; i < methodParameters.length; i++) {
							Class<?> param1 = methodParameters[i];
							Class<?> param2 = ck.methodParameters[i];
							if (param1 != null) {
								if (!param1.isAssignableFrom(param2))
									return false;
							} else {
								if (param2 != null)
									return false;
							}
						}
						return true;
					}
				}
			}
		}
		return false;
	}

	public int hashCode() {
		return methodName.hashCode();
	}
}
