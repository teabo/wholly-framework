package com.whollyframework.datasource.proxool;

import java.util.HashMap;

import org.logicalcobwebs.proxool.ProxoolDataSource;

import com.whollyframework.util.Security;

public class DefaultProxoolDataSource extends ProxoolDataSource {

	private HashMap<String,String> pwdMap = new HashMap<String, String>();
	
	/**
	 * 重置数据库链接信息为明文
	 */
	public void setPassword(String password) {
		String pswd = pwdMap.get(password);
		if (pswd==null){
			pswd = decrypt(password);
			pwdMap.put(password, pswd);
		}
		super.setPassword(pswd);
		String url = reSetUrl(super.getDriverUrl(), super.getPassword());
		super.setDriverUrl(url);
	}

	/* 替换url的密码为明文 */
	public String reSetUrl(String url, String pwd) {
		int begin = url.indexOf("password=");
		if (begin>0){
			StringBuffer buf = new StringBuffer();
			buf.append(url.substring(0, begin + 10)).append(pwd);
			String subs = url.substring(begin + 10);
			int end = subs.lastIndexOf('@');
			if (end>0){
				buf.append(subs.substring(end));
			}
			return buf.toString();
		}
		
		return url;
	}

	/**
	 * 新的密码加密机制
	 * 
	 * @param s
	 * @return
	 * @throws Exception
	 */
	protected String encrypt(final String s) throws Exception {
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
		DefaultProxoolDataSource ds = new DefaultProxoolDataSource();
		try {
			System.out.println(ds.encrypt("teabo"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
