package com.whollyframework.support;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class JDBCProperty {
	private static Properties prop = null;

	public JDBCProperty() {
	}
	
	private static void init() {
		if (prop == null)
			prop = new Properties();
		InputStream is = JDBCProperty.class.getResourceAsStream(
				"/jdbc.properties");
		try {
			prop.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getProperty(String key, String defaultValue) {
		if (prop == null)
			init();
		return prop.getProperty(key, defaultValue);
	}

	public static String getProperty(String key) {
		return getProperty(key, null);
	}

}
