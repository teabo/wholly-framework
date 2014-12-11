package com.whollyframework.base.web.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.whollyframework.utils.ContextUtil;

public class WebContextLoaderListener extends ContextLoaderListener{

	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		
		ServletContext context = event.getServletContext();
        ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
        
        ContextUtil.setServletContext(context);
        ContextUtil.setApplicationContext(ctx);
	}

}
