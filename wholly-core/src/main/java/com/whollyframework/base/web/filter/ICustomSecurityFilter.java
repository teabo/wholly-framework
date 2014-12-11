package com.whollyframework.base.web.filter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

public interface ICustomSecurityFilter {

	void init(FilterConfig filterConfig) throws ServletException;
	
	/**
	 * 判断此访问地址是否需要拦截
	 * @param uri
	 * @return
	 */
	boolean isForegroundURI(String uri);
	
	/**
	 * 判断此访问地址是否为白名单访问地址
	 * @param uri
	 * @return
	 */
	boolean isExcludeURI(String uri);
	
	void destroy();
	
}
