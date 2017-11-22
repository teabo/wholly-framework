package com.whollyframework.generator.util;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.whollyframework.util.StringUtil;
@SuppressWarnings("unchecked")
public class SystemConf {

	private static Map<String, Map<String, String>> defaultProps = null;

	public static final String TAG_CONFIGURATIONS = "configurations";
	
	public static final String TAG_RESOURCES = "resources";
	
	public static final String TAG_INCLUDE = "include";
	
	public static final String TAG_GENERATORS = "generators";
	
	public static final String TAG_GENERATOR = "generator";
	
	public static final String TAG_CONFIG = "config";
	
	public static final String TAG_PROPERTY = "property";
	
	public static final String ATT_PATH = "path";
	
	public static final String ATT_NAME = "name";
	
	public static final String ATT_VALUE = "value";
	
	public static final String ATT_CLASS = "class";
	
	private static List<String> resources = new ArrayList<String>();
	
	private static String loadFileName = "gencode-conf.xml";
	
	public static void Load(String fileName){
		loadFileName = fileName;
	}
	/**
	 * Initialize the proerties
	 * 
	 * @throws Exception
	 */
	private static void init() {
		if (defaultProps == null)
			defaultProps = new LinkedHashMap<String, Map<String, String>>();

		try {
			URL propsUrl = Thread.currentThread().getContextClassLoader()
					.getResource(loadFileName);
			InputStream in = propsUrl.openStream();
			IParseXML parser = XMLParseUtil.getParser(in,
					new SysConfDefaultHandler());
			Object[] result = (Object[]) parser.returnObj();
			defaultProps.putAll((Map<String, Map<String, String>>) result[0]);
			resources.addAll((List<? extends String>) result[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void reload(String key) {
		if (defaultProps == null)
			defaultProps = new LinkedHashMap<String, Map<String, String>>();

		try {
			URL propsUrl = Thread.currentThread().getContextClassLoader()
					.getResource("gencode-conf.xml");
			InputStream in = propsUrl.openStream();
			IParseXML parser = XMLParseUtil.getParser(in,
					new SysConfDefaultHandler(key));
			if (StringUtil.isBlank(key)){
				Object[] result = (Object[]) parser.returnObj();
				defaultProps.putAll((Map<String, Map<String, String>>) result[0]);
				resources.addAll((List<? extends String>) result[1]);
			}else{
				Object[] result = (Object[]) parser.returnObj();
				Map<String, Map<String, String>> defaultProps = (Map<String, Map<String, String>>) result[0];
				SystemConf.defaultProps.put(key, defaultProps.get(key));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param sysConfType
	 *            sysConfType
	 * @param key
	 *            The property key
	 * @param defaultValue
	 * @return The Property value find from default Property,if no value,return
	 *         defaultValue,else return the Property value.
	 * @throws Exception
	 */
	public static String getProperty(String sysConfType, String key,
			String defaultValue){
		if (defaultProps == null) {
			init();
		}
		Map<String, String> prop = defaultProps.get(sysConfType);
		String rtn = null;
		if (prop != null) {
			rtn = prop.get(key);
		}
		if (!StringUtil.isBlank(rtn) && !rtn.equals(defaultValue))
			return rtn;
		return defaultValue;
	}
	
	public static Set<String> propsKeyset(){
		if (defaultProps == null) {
			init();
		}
		return defaultProps.keySet();
	}

	public static Map<String, String> getPropertys(String sysConfType){
		if (defaultProps == null) {
			init();
		}
		return defaultProps.get(sysConfType);
	}

	/**
	 * Get the property value
	 * 
	 * @param key
	 *            The property key
	 * @return The Property value.
	 * @throws Exception
	 * @throws Exception
	 */
	public static String getProperty(String sysConfType, String key) {
		return getProperty(sysConfType, key, null);
	}
	
	public static boolean equalsPropValue(String sysConfType, String key, String equulsValue){
		String values = getProperty(sysConfType, key, null);
		if(!StringUtil.isBlank(values)){
			String[] value = values.split("\\|");
			for (int i = 0; i < value.length; i++) {
				if (value[i].equalsIgnoreCase(equulsValue)){
					return true;
				}
			}
		}
		return false;
	}

	public static List<String> getResources() {
		return resources;
	}
}

class SysConfDefaultHandler extends AbstractParseXML {

	SysConfDefaultHandler() {
	}

	SysConfDefaultHandler(String key) {
		reloadKey = key;
		isreload = true;
	}

	private String reloadKey = null;
	private boolean isreload = false;
	private boolean isLoadParam = false;
	private boolean loadAble = true;
	private Map<String, String> prop;
	private List<String> resources = new ArrayList<String>();
	private Map<String, Map<String, String>> defaultProps = new LinkedHashMap<String, Map<String, String>>();

	/**
	 * 标签开始时的事件 uri ：元素的命名空间 localName ：元素的本地名称（不带前缀） qName ：元素的限定名（带前缀）
	 */
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		try {
			if (loadAble) {
				if (SystemConf.TAG_CONFIGURATIONS.equalsIgnoreCase(qName)) {

				} else if (SystemConf.TAG_CONFIG.equalsIgnoreCase(qName)) {
					prop = new LinkedHashMap<String, String>();
					for (int i = 0; i < attributes.getLength(); i++) {
						if (SystemConf.ATT_NAME.equalsIgnoreCase(attributes.getQName(i))) {
							String name = attributes.getValue(i);
							if (isreload) {
								if (name != null && reloadKey == null) {
									defaultProps.put(name, prop);
									isLoadParam = true;
								} else if (reloadKey != null
										&& reloadKey.equalsIgnoreCase(name)) {
									defaultProps.put(name, prop);
									isLoadParam = true;
								} else {
									isLoadParam = false;
								}
							} else {
								defaultProps.put(name, prop);
								isLoadParam = true;
							}
							break;
						}
					}
				} else if (SystemConf.TAG_GENERATORS.equalsIgnoreCase(qName)){
					prop = new LinkedHashMap<String, String>();
					isLoadParam = true;
					defaultProps.put(SystemConf.TAG_GENERATORS, prop);
				} else if ((isLoadParam) && (SystemConf.TAG_PROPERTY.equalsIgnoreCase(qName)
						|| SystemConf.TAG_GENERATOR.equalsIgnoreCase(qName))) {
					String name = null;
					String value = "";
					for (int i = 0; i < attributes.getLength(); i++) {
						if (SystemConf.ATT_NAME.equalsIgnoreCase(attributes.getQName(i))) {
							name = attributes.getValue(i);
						} else if (SystemConf.ATT_VALUE.equalsIgnoreCase(attributes
								.getQName(i))) {
							value = attributes.getValue(i);
						} else if (SystemConf.ATT_CLASS.equalsIgnoreCase(attributes
								.getQName(i))) {
							value = attributes.getValue(i);
						}
					}
					if (!StringUtil.isBlank(name)) {
						prop.put(name, value);
					}
				} else if (SystemConf.TAG_INCLUDE.equalsIgnoreCase(qName)) {
					for (int i = 0; i < attributes.getLength(); i++) {
						if (SystemConf.ATT_PATH.equalsIgnoreCase(attributes.getQName(i))) {
							String file = attributes.getValue(i);
							resources.add(file);
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		if ("Configuration".equalsIgnoreCase(qName)) {
			if (isreload && isLoadParam) {
				loadAble = false;
			}
		}
	}

	@Override
	public Object returnObj() {
		return new Object[]{defaultProps, resources};
	}
}