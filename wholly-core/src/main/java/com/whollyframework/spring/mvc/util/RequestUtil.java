package com.whollyframework.spring.mvc.util;

import javax.servlet.http.HttpServletRequest;

public class RequestUtil {

	/**
	 * 获取当前请求的基地址（例如：http://localhost:8080/ctas/xxx.do 返回
	 * http://localhost:8080/ctas）
	 *
	 * @param request
	 * @return
	 */
	public static String getBaseUrl(HttpServletRequest request) {
		return request.getRequestURL().substring(
				0,
				request.getRequestURL().indexOf(request.getContextPath())
						+ request.getContextPath().length());
	}
	
	public static String getContextPath(HttpServletRequest request) {
		return request.getContextPath();
	}
}
