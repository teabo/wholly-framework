package com.whollyframework.util.security;

import com.whollyframework.util.security.decoder.RSADecoder;
import com.whollyframework.util.security.encoder.RSAEncoder;


public class RSA {
	
	/**
	 * 加密
	 * 
	 * @param datasource
	 *            String
	 * @return String
	 * @throws Exception 
	 */
	public static String encrypt(String datasource) throws Exception {
		RSAEncoder encoder = new RSAEncoder();
		return encoder.encryptString(datasource);
	}

	/**
	 * 解密
	 * 
	 * @param src
	 *            String
	 * @return String
	 * @throws Exception
	 */
	public static String decrypt(String src) throws Exception {
		RSADecoder decoder = new RSADecoder();
		return decoder.decryptString(src);
	}

	// 测试
	public static void main(String args[]) {
		// 待加密内容
		String str = "测试内容";

		// 直接将如上内容解密
		try {
			String result = RSA.encrypt(str);
			System.out.println("加密后：" +  result);
			String decryResult = RSA.decrypt(result);
			System.out.println("解密后：" + decryResult);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}
}