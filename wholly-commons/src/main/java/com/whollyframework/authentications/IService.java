package com.whollyframework.authentications;

import java.io.Serializable;




/**
 * @author Chris Xu
 * @since 2011-4-29 上午10:56:18
 */
public interface IService<E, ID extends Serializable> {

	/**
	 * @param id
	 * @return
	 */
	public E find(ID id) throws Exception;
}
