package com.whollyframework.authentications;



/** 
 * @author Chris Xu
 * @version 2011-2-2 上午12:00:01
 */
public interface IWebUser extends IUser{
	
	boolean isUnitAdmin();
	
	boolean isDeveloper();
	
	boolean isSuperAdmin();
	/**
	 * 根据指定文档ID获取缓存文档对象
	 * @param key
	 * 			文档ID
	 * @return 文档对象
	 */
	Object getFromTmpspace(Object key);

	/**
	 * 清除缓存文档对象
	 */
	void clearTmpspace();

	/**
	 * 加入用户缓存
	 * @param key 标识ID
	 * @param o 缓存文档对象
	 */
	void putToTmpspace(Object key, Object o);

	/**
	 * 根据标识ID移除缓存对象
	 * @param key 标识ID
	 */
	void removeFromTmpspace(Object key);
	
	/**
	 * 设置在线用户临时ID
	 * @param onlineUserid
	 */
	void setOnlineUserid(String onlineUserid);
	
	/**
	 * 获取在线用户临时ID
	 */
	String getOnlineUserid();
	
	/**
	 * 设置在线用户sessionid
	 * @param sessionid
	 */
	void setSessionid(String sessionid);
	
	/**
	 * 获取在线用户sessionid
	 */
	String getSessionid();
	
}

