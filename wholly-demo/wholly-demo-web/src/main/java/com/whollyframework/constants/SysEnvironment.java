package com.whollyframework.constants;

import org.quartz.Scheduler;

import com.whollyframework.constans.Environment;
import com.whollyframework.constans.Web;
import com.whollyframework.util.property.DefaultProperty;

public class SysEnvironment {

	/**
	 * @return the encoding
	 */
	public String getEncoding() {
		return Environment.getInstance().getEncoding();
	}

	/**
	 * @param encoding
	 *            the encoding to set
	 */
	public void setEncoding(String encoding) {
		Environment.getInstance().setEncoding(encoding);
	}

	private static SysEnvironment env = null;
	private String webSiteTitle;

	private SysEnvironment() {
		webSiteTitle = DefaultProperty.getProperty(Web.FRAMEWORK_WEBSITE_TITLE);
	}

	public static SysEnvironment getInstance() {
		if (env == null) {
			env = new SysEnvironment();
		}

		return env;
	}

	/**
	 * @param wcpath
	 *            The web context path.
	 */
	public void setApplicationRealPath(String wcpath) {
		Environment.getInstance().setApplicationRealPath(wcpath);
	}

	/**
	 * Retrieve the web context path.
	 * 
	 * @return The web context path.
	 */
	public String getApplicationRealPath() {
		return Environment.getInstance().getApplicationRealPath();
	}

	/**
	 * Retrieve the web context physicsal path.
	 * 
	 * @param path
	 *            The web path.
	 * @return The web context physicsal path.
	 */
	public String getRealPath(String path) {
		return Environment.getInstance().getRealPath(path);
	}

	/**
	 * Set request context path;
	 * 
	 * @param cpath
	 * @return
	 */
	public String setContextPath(String cpath) {
		return Environment.getInstance().setContextPath(cpath);
	}

	public String getContextPath() {
		return Environment.getInstance().getContextPath();
	}

	public String getServerName() {
		return Environment.getInstance().getServerName();
	}

	public void setServerName(String serverName) {
		Environment.getInstance().setServerName(serverName);
	}

	public int getServerPort() {
		return Environment.getInstance().getServerPort();
	}

	public void setServerPort(int serverPort) {
		Environment.getInstance().setServerPort(serverPort);
	}

	public String getContext(String uri) {
		return Environment.getInstance().getContext(uri);
	}

	public Scheduler getScheduler() {
		return Environment.getInstance().getScheduler();
	}

	public void setScheduler(Scheduler scheduler) {
		Environment.getInstance().setScheduler(scheduler);
	}

	public String getWebSiteTitle() {
		return webSiteTitle;
	}

	public void setWebSiteTitle(String webSiteTitle) {
		this.webSiteTitle = webSiteTitle;
	}
}
