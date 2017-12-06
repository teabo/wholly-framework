package com.whollyframework.base.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.whollyframework.base.action.SupportController;

public class SystemInterceptor extends HandlerInterceptorAdapter implements HandlerInterceptor {

	@Override
	public void afterConcurrentHandlingStarted(
			HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//System.out.println("==============执行顺序: 4、afterConcurrentHandlingStarted================");
	}
	
	public void afterCompletion(HttpServletRequest request, HttpServletResponse arg1, Object handler, Exception arg3)
			throws Exception {
		 //System.out.println("==============执行顺序: 3、afterCompletion================");

	}

	public void postHandle(HttpServletRequest request, HttpServletResponse arg1, Object handler, ModelAndView arg3)
			throws Exception {
		 //System.out.println("==============执行顺序: 2、postHandle================");
		try {
			// 获取url地址
			String requestURL = request.getRequestURI().replace(request.getContextPath(), "");
			if (requestURL.lastIndexOf(".action") > 0) {
				HandlerMethod actionHandler = (HandlerMethod) handler;
				if (actionHandler.getBean() instanceof SupportController) {
					@SuppressWarnings("rawtypes")
					SupportController action = (SupportController) actionHandler.getBean();
					action.setNamespaceToScope(request);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		// System.out.println("==============执行顺序: 1、preHandle================");
		return true;
	}

}
