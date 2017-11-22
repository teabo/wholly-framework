package com.whollyframework.util.property;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.whollyframework.constans.Web;
import com.whollyframework.util.IOUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * The default property.
 */
public class DefaultProperty {
	private final static Logger LOG = Logger.getLogger(DefaultProperty.class);

	private static Properties prop = null;

	private static Map<String, Property> props = null;

	/**
	 * Initialize the proerties
	 * 
	 * @throws Exception
	 */
	private static void init() {
		URL propsUrl = Thread.currentThread().getContextClassLoader().getResource(Web.FRAMEWORK_XML_PROPS_FILE);

		if (propsUrl != null) {
			if (props == null)
				props = new HashMap<String, Property>();
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
				LOG.info(" Load  " + Web.FRAMEWORK_XML_PROPS_FILE + " file successed");
			} catch (Exception e) {
				throw new RuntimeException("Could not load " + Web.FRAMEWORK_XML_PROPS_FILE + ":" + e);
			} finally {
				IOUtils.closeQuietly(is);
			}
		} else {
			if (prop == null)
				prop = new Properties();
			propsUrl = Thread.currentThread().getContextClassLoader().getResource(Web.FRAMEWORK_PROPERTIES_FILE);
			InputStream is = null;
			try {
				is = propsUrl.openStream();
				prop.load(is);
				LOG.info(" Load " + Web.FRAMEWORK_PROPERTIES_FILE + " file successed");
			} catch (IOException e) {
				throw new RuntimeException("Could not load " + Web.FRAMEWORK_PROPERTIES_FILE + ":" + e);
			} finally {
				IOUtils.closeQuietly(is);
			}
		}
	}

	/**
	 * Get the property value
	 * 
	 * @param key
	 *            The property key
	 * @param defaultValue
	 *            The default value.
	 * @return The Property value.
	 * @throws Exception
	 */
	public static String getProperty(String key, String defaultValue) {
		return getProp(key, defaultValue).getValue();
	}
	
	public static Property getProp(String key, String defaultValue) {
		if (props == null && prop == null) {
			init();
		}
		if (props != null) {
			Property prop = props.get(key);
			return prop != null ? prop : new Property(key, defaultValue);
		}
		return new Property(key, prop.getProperty(key, defaultValue));
	}

	/**
	 * Get the property value
	 * 
	 * @param key
	 *            The property key
	 * @return The Property value.
	 * @throws Exception
	 */
	public static String getProperty(String key) {
		return getProperty(key, null);
	}

}
