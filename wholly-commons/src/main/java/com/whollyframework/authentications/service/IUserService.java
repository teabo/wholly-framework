package com.whollyframework.authentications.service;

import java.util.List;

import com.whollyframework.authentications.IService;
import com.whollyframework.authentications.IUser;
import com.whollyframework.authentications.IWebUser;

/**
 * @author Chris Xu
 * @since 2011-4-29 上午10:54:30
 */
public interface IUserService extends IService{

	/**
	 * @param filterField
	 * @return
	 */
	List<? extends IUser> queryByRoleId(String filterField)throws Exception;

	/**
	 * @param userid
	 * @return
	 */
	IWebUser getWebUserInstance(String userid)throws Exception;

	

}
