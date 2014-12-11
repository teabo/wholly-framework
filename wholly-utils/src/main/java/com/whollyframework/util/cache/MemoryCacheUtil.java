package com.whollyframework.util.cache;

import java.util.HashMap;

import com.whollyframework.authentications.IWebUser;

public class MemoryCacheUtil {
	private static HashMap<Object, Object> _publicspace = new HashMap<Object, Object>();

	public static void putToPublicSpace(Object key, Object o) {
		_publicspace.put(key, o);
	}

	public static Object getFromPublicSpace(Object key) {
		return _publicspace.get(key);
	}

	public static void removeFromPublicSpace(Object key) {
		_publicspace.remove(key);
	}

	/**
	 * 清空私有空间
	 * @param user
	 */
	public static void clearPrivateSpace(IWebUser user) {
		user.clearTmpspace();
	}

	public static void putToPrivateSpace(Object key, Object o, IWebUser user) {
		if (user != null) {
			user.putToTmpspace(key, o);
		}
	}

	public static Object getFromPrivateSpace(Object key, IWebUser user) {
		if (user != null) {
			return user.getFromTmpspace(key);
		}

		return null;
	}

	public static void removeFromPrivateSpace(Object key, IWebUser user) {
		if (user != null) {
			user.removeFromTmpspace(key);
		}
	}
}
