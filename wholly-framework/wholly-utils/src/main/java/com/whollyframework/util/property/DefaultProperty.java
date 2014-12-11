package com.whollyframework.util.property;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * The default property.
 */
public class DefaultProperty {
	private static Properties prop = null;

    /**
     * Initialize the proerties
     * @throws Exception
     */
    private static void init(){
	    if (prop == null)
            prop = new Properties();

	    InputStream is = DefaultProperty.class.getClassLoader()
                .getResourceAsStream("default.properties");
	    try {
			prop.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the property value
	 * @param key The property key 
	 * @param defaultValue The default value.
	 * @return The Property value. 
	 * @throws Exception
	 */
	public static String getProperty(String key, String defaultValue){
		if (prop == null) {
			init();
		}
		return prop.getProperty(key, defaultValue);
	}
	
	/**
	 * Get the property value
	 * @param key The property key 
	 * @return The Property value. 
	 * @throws Exception
	 */
	public static String getProperty(String key){
		return getProperty(key, null);
	}
    
} 
