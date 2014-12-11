package com.whollyframework.utils;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ServicesFactory {
	private static boolean init = false;
	private static ApplicationContext factory = null;
	/**
	 * 获得Spring框架应用上下文对象
	 * 
	 * @return ApplicationContext
	 */
	public static ApplicationContext getApplicationContext() {
		// 判断如果 ApplicationContext 的对象 ＝＝ NULL
		if (factory == null) {
			try {
				factory = new ClassPathXmlApplicationContext(new String[] {
						"applicationContext.xml"
						});
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		// 返回ApplicationContext
		return factory;
	}

	private static void init(ServletContext servletContext) {
		if (!init) {
			try{
				factory = WebApplicationContextUtils
						.getRequiredWebApplicationContext(servletContext);
			} catch (Exception e){
				getApplicationContext();
			}
			init = true;
		}
	}

	public static Object getService(ServletContext servletContext,
			String serviceName) {
		init(servletContext);
		return factory.getBean(serviceName);
	}

	public static Object getService(String serviceName) {
		init(ContextUtil.getServletContext());
		return factory.getBean(serviceName);
	}
}
