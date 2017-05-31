package com.whollyframework.authentications.service;

import java.io.Serializable;
import java.util.List;

import com.whollyframework.authentications.IService;
import com.whollyframework.authentications.IWebUser;

/**
 * @author Chris Xu
 * @since 2011-4-29 上午10:54:30
 */
public interface IUserService<E, ID extends Serializable> extends IService<E, ID>{

	/**
	 * @param roleid
	 * @return
	 */
	List<E> queryByRoleId(ID roleid)throws Exception;

	/**
	 * @param userid
	 * @return
	 */
	IWebUser getWebUserInstance(ID userid)throws Exception;

	

}
