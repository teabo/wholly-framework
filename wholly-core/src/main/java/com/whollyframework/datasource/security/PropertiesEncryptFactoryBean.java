package com.whollyframework.datasource.security;

import java.util.HashMap;
import java.util.Properties;

import org.springframework.beans.factory.FactoryBean;

import com.whollyframework.util.Security;
import com.whollyframework.util.StringUtil;

@SuppressWarnings("rawtypes")
public class PropertiesEncryptFactoryBean implements FactoryBean {

	private HashMap<String,String> pwdMap = new HashMap<String, String>();
	
	private Properties properties;
	
	public Object getObject() throws Exception {
		return getProperties();
	}

	public Class getObjectType() {
		return java.util.Properties.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties inProperties) {
		this.properties = inProperties;
//		String originalUsername = properties.getProperty("user");
		String originalPassword = properties.getProperty("password");
//		if (!StringUtil.isBlank(originalUsername)){
//			String newUsername = decrypt(originalUsername);
//			properties.put("user", newUsername);
//		}
		if (!StringUtil.isBlank(originalPassword)){
			String newPassword = pwdMap.get(originalPassword);
			if (newPassword==null){
				newPassword = decrypt(originalPassword);
				pwdMap.put(originalPassword, newPassword);
			}
			properties.put("password", newPassword);
		}
	}
	
	/**
	 * 新的密码加密机制
	 * 
	 * @param s
	 * @return
	 * @throws Exception
	 */
	protected String encrypt(final String s) {
		return Security.encryptPassword(s);
	}

	/**
	 * 新的密码解密机制
	 * 
	 * @param s
	 * @return
	 * @throws Exception
	 */
	protected String decrypt(final String s) {
		return Security.decryptPassword(s);
	}

	public static void main(String[] args) {
		PropertiesEncryptFactoryBean bean = new PropertiesEncryptFactoryBean();
		try {
			System.out.println(bean.encrypt("sunshine"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

