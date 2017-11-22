package com.whollyframework.util.property;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.whollyframework.util.IOUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XmlPropsUtil {
	private final static Logger LOG = Logger.getLogger(XmlPropsUtil.class);

	private static Map<String, Map<String, Property>> settings = new HashMap<String, Map<String, Property>>();

	public static void load(String name) {
		if (!isLoaded(name)) {
			Map<String, Property> props = new HashMap<String, Property>();

			URL propsUrl = Thread.currentThread().getContextClassLoader().getResource(name + ".xml");

			if (propsUrl == null) {
				throw new IllegalStateException(name + ".xml missing");
			}

			InputStream is = null;
			try {
				is = propsUrl.openStream();
				XStream xstream = new XStream(new DomDriver());
				xstream.alias("Property", Property.class);
				List<Property> propertys = (List<Property>) xstream.fromXML(is);
				for (int i = 0; i < propertys.size(); i++) {
					Property prop = propertys.get(i);
					props.put(prop.getKey(), prop);
				}
			} catch (Exception e) {
				throw new RuntimeException("Could not load " + name + ".xml:" + e);
			} finally {
				IOUtils.closeQuietly(is);
			}
			LOG.debug(" Load xml props file successed");

			settings.put(name, props);
		}
	}

	public static String get(String key) {
		for (Iterator<String> iter = settings.keySet().iterator(); iter.hasNext();) {
			String value = get(iter.next(), key);
			if (value != null) {
				return value;
			}
		}
		return null;
	}

	public static String get(String propName, String key) {
		if (settings.get(propName) != null) {
			Map<String, Property> props = settings.get(propName);
			Property prop = props.get(key);
			if (prop != null) {
				return prop.getValue();
			}
		}
		return null;
	}

	public static void clear() {
		settings.clear();

	}

	public static void reload() {
		for (Iterator<String> iter = settings.keySet().iterator(); iter.hasNext();) {
			reload(iter.next());
		}
	}

	public static void reload(String name) {
		settings.remove(name);
		load(name);
	}

	public static Map<String, Property> toMap() {
		Map<String, Property> rtn = new HashMap<String, Property>();

		for (Iterator<Map<String, Property>> iter = settings.values().iterator(); iter.hasNext();) {
			Map<String, Property> props = iter.next();
			for (Iterator<String> iterator2 = props.keySet().iterator(); iterator2.hasNext();) {
				String key = iterator2.next();
				rtn.put(key, props.get(key));
			}
		}
		return rtn;
	}

	private static boolean isLoaded(String name) {
		return settings.keySet().contains(name);
	}
}
