package com.whollyframework.base.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

import com.whollyframework.constans.Environment;
import com.whollyframework.constans.Web;
import com.whollyframework.util.ObjectUtil;
import com.whollyframework.util.StringUtil;
import com.whollyframework.util.property.DefaultProperty;

public class StartupListener extends HttpServlet implements ServletContextListener {
	// Notification that the web application is ready to process requests
	private ICustomStartupListener customListener = null;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public StartupListener(){
		String listener = DefaultProperty.getProperty(Web.FRAMEWORK_STARTUP_LISTENER);
		if (!StringUtil.isBlank(listener))
			customListener = (ICustomStartupListener) ObjectUtil.getInstance(listener);
	}

	public void contextInitialized(ServletContextEvent sce) {

		try {
			// 初始化应用真实路径
			Environment evt = Environment.getInstance();
			evt.setContextPath(sce.getServletContext().getContextPath());
			evt.setApplicationRealPath(sce.getServletContext().getRealPath("/"));

			// 已废弃
			sce.getServletContext().setAttribute(Environment.class.getName(), evt);
			
			if (customListener!=null){
				customListener.contextInitialized(sce);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	public void contextDestroyed(ServletContextEvent sce) {
		if (customListener!=null){
			customListener.contextDestroyed(sce);
		}
	}

}
