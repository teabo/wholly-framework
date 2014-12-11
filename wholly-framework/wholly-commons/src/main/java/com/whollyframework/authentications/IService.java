package com.whollyframework.authentications;




/**
 * @author Chris Xu
 * @since 2011-4-29 上午10:56:18
 */
public interface IService {

	/**
	 * @param authorId
	 * @return
	 */
	public IValueObject find(String authorId) throws Exception;
}
