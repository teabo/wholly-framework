package com.whollyframework.web.filter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import com.whollyframework.base.web.filter.ICustomSecurityFilter;

public class CustomSecurityFilter implements ICustomSecurityFilter {

	public void init(FilterConfig filterConfig) throws ServletException {

	}

	public boolean isForegroundURI(String uri) {
		return false;
	}

	public boolean isExcludeURI(String uri) {
		return 	uri.indexOf("/res/") >= 0
				|| uri.indexOf("/wechat/") >= 0
				|| uri.indexOf("/s/") >= 0
				|| uri.indexOf("/ws/") >= 0
				|| uri.indexOf("/apidocs/") >= 0
				|| uri.indexOf("/api-docs") >= 0;
	}

	public void destroy() {

	}

}
