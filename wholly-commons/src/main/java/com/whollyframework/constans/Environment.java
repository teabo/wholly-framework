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
				.getResourceAsStream(Web.FRAMEWORK_PROPERTIES_FILE);
		try {
			prop.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}

		setEncoding(prop.getProperty(Web.FRAMEWORK_I18N_ENCODING));
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

	/**
	 * http://localhost:8080/demo
	 * @return localhost
	 */
	public String getServerName() {
		return _wwwServer;
	}

	public void setServerName(String serverName) {
		this._wwwServer = serverName;
	}

	/**
	 * http://localhost:8080/demo
	 * @return 8080
	 */
	public int getServerPort() {
		return _wwwPort;
	}
	
	public void setServerPort(int serverPort) {
		this._wwwPort = serverPort;
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
