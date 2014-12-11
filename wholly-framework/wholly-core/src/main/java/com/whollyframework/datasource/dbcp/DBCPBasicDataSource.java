package com.whollyframework.datasource.dbcp;

import java.util.HashMap;

import org.apache.commons.dbcp.BasicDataSource;

import com.whollyframework.util.Security;

public class DBCPBasicDataSource extends BasicDataSource {

	private HashMap<String,String> pwdMap = new HashMap<String, String>();
	
	public void setPassword(String password) {
		String pswd = pwdMap.get(password);
		if (pswd==null){
			pswd = decrypt(password);
			pwdMap.put(password, pswd);
		}
		super.setPassword(pswd);
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
	
	public static void main(String[] args){
		DBCPBasicDataSource ds= new DBCPBasicDataSource();
		try {
			System.out.println(ds.encrypt("teabo"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
