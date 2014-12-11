package com.whollyframework.base.web.listener;

import javax.servlet.ServletContextEvent;

public interface ICustomStartupListener {

	void contextInitialized(ServletContextEvent sce);
	
	void contextDestroyed(ServletContextEvent sce);
}
