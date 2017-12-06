package com.whollyframework.web.listener;

import javax.servlet.ServletContextEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.whollyframework.base.web.listener.ICustomStartupListener;
import com.whollyframework.web.init.InitSystem;
import com.whollyframework.web.login.log.LoginLogThread;
import com.whollyframework.web.login.log.ThreadPool;

public class CustomStartupListener implements ICustomStartupListener {

	private final static Logger LOG = LoggerFactory.getLogger(CustomStartupListener.class);
	
	private ThreadPool pool = new ThreadPool(4, LoginLogThread.class.getName());
	
	public void contextInitialized(ServletContextEvent sce) {
		LOG.info("系统初始化......");
		InitSystem.init();
		
		pool.start();
	}

	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		pool.destory();
	}

}
