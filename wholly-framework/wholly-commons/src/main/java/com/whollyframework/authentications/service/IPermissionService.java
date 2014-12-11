package com.whollyframework.authentications.service;

import java.util.Collection;

import com.whollyframework.authentications.IRole;


/**
 * @author Chris Xu
 * @since 2011-4-29 上午10:42:48
 */
public interface IPermissionService {

	/**
	 * @param rolelist
	 * @param id
	 * @param operationCode
	 * @param resType
	 * @param b
	 * @return
	 */
	boolean check(Collection<? extends IRole> rolelist, String id, int operationCode,
			int resType, boolean b) throws Exception;

	/**
	 * @param roles
	 * @param id
	 * @param type
	 * @param resType
	 * @return
	 */
	boolean check(Collection<? extends IRole> roles, String id, int type, int resType) throws Exception;

	
}
