package com.whollyframework.dbservice.user.service;
/**
 * 系统管理--用户管理
 * @author WangWenGuang
 * @2014-12-23
 */
public interface EncryAndDecryService {
	/**
	 * 加密
	 * @return
	 */
	public String encrypt(String str);
   /**
    * 解密
    * @return
    */
	public String decrypt(String str);
}
