package com.whollyframework.constans;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerListener;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The environment variable.
 */
public class Environment implements Serializable {
	
	private static final Logger log = LoggerFactory.getLogger(Environment.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String _wcpath;

	private String _contextPath;

	private String _wwwServer;

	private int _wwwPort = -1;

	private String encoding;

	/**
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @param encoding
	 *            the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	private static Environment env = null;

	private Environment() {
		Properties prop = new Properties();
		InputStream is = Environment.class.getClassLoader()
				.getResourceAsStream("default.properties");
		try {
			prop.load(is);
		} catch (IOException e) {
			log.error("加载属性文件\"default.properties\"异常：",e);
		}

		setEncoding(prop.getProperty("teabo.framework.i18n.encoding"));
	}

	public static Environment getInstance() {
		if (env == null) {
			env = new Environment();
		}

		return env;
	}

	/**
	 * @param wcpath
	 *            The web context path.
	 */
	public void setApplicationRealPath(String wcpath) {
		_wcpath = wcpath;
	}

	/**
	 * Retrieve the web context path.
	 * 
	 * @return The web context path.
	 */
	public String getApplicationRealPath() {
		return (_wcpath != null) ? _wcpath : "";
	}

	/**
	 * Retrieve the web context physicsal path.
	 * 
	 * @param path
	 *            The web path.
	 * @return The web context physicsal path.
	 */
	public String getRealPath(String path) {
		String realpath = (path != null) ? getApplicationRealPath() + path : "";
		realpath = realpath.replaceAll("\\\\", "/");

		return realpath;
	}

	/**
	 * Set request context path;
	 * 
	 * @param cpath
	 * @return
	 */
	public String setContextPath(String cpath) {
		return _contextPath = cpath;
	}

	public String getContextPath() {
		return _contextPath;
	}

	public String get_wwwServer() {
		return _wwwServer;
	}

	public void set_wwwServer(String _wwwServer) {
		this._wwwServer = _wwwServer;
	}

	public int get_wwwPort() {
		return _wwwPort;
	}

	public void set_wwwPort(int _wwwPort) {
		this._wwwPort = _wwwPort;
	}

	public String getContext(String uri) {
		if (_contextPath.equals("/")) {
			return uri;
		} else {
			return _contextPath + uri;
		}
	}

	/**
	 * 任务调度器
	 */
	private Scheduler scheduler;

	public Scheduler getScheduler() {
		if (this.scheduler == null) {
			// 初始化任务调度器
			SchedulerFactory sf = new StdSchedulerFactory();
			try {
				this.scheduler = sf.getScheduler();
			} catch (SchedulerException e) {
				log.error("初始化任务调度器异常：",e);
			}
		}

		return this.scheduler;
	}

	public void addTriggerListener(TriggerListener triggerListener) {
		// 注册监听器
		// TaskTriggerListener listener = new TaskTriggerListener();
		try {
			this.scheduler.getListenerManager().addTriggerListener(
					triggerListener,
					GroupMatcher.triggerGroupEquals(TaskConstants.TASK_GROUP));
		} catch (SchedulerException e) {
			log.error("注册任务监听器异常：",e);
		}
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

}
