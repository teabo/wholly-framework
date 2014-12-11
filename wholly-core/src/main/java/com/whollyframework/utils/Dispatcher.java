package com.whollyframework.utils;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.whollyframework.constans.Web;
import com.whollyframework.util.property.DefaultProperty;

/**
 * @author Chris Xu
 * @version 2010-11-14 下午02:32:05
 */
public class Dispatcher {
	private static final Logger LOG = Logger.getLogger(Dispatcher.class);

	private static String DEFAULT_SKINTYPE = null;
	static {
		try {
			DEFAULT_SKINTYPE = DefaultProperty.getProperty(Web.FRAMEWORK_DEFAULT_SKIN);
		} catch (Exception e) {
		}
		if (DEFAULT_SKINTYPE == null)
			DEFAULT_SKINTYPE = Web.DEFAULT_SKIN_VALUE;
	}
	private static final String DISPATCH_TAG = "/dispatch/";
	private static final String DISPATCH_VALID_PATH = "/portal/dispatch/";
	private static final String path_separator= "/";

	public void forward(String url, ServletRequest req, ServletResponse res) throws ServletException, IOException {
		if (url.indexOf(DISPATCH_VALID_PATH) != -1) {
			url = getDispatchURL(url, req, res);
			url = url.replace(((HttpServletRequest) req).getContextPath(), "");
		}
		LOG.debug(" [FORWARD] " + url);
		RequestDispatcher dispatcher = req.getRequestDispatcher(url);
		dispatcher.forward(req, res);
	}

	public void sendRedirect(String url, ServletRequest req, ServletResponse res) throws IOException {
		url = getDispatchURL(url, req, res);
		LOG.debug(" [REDIRECT] " + url);
		((HttpServletResponse) res).sendRedirect(url);
	}

	/**
	 * 
	 * @param url
	 * @param req
	 * @param res
	 * @return
	 */
	public String getDispatchURL(String url, ServletRequest req, ServletResponse res) {
		if (url.indexOf(DISPATCH_VALID_PATH) != -1) {
			HttpServletRequest hreq = (HttpServletRequest) req;
			HttpSession session = hreq.getSession();
			String skinType = (String) session.getAttribute(Web.CURRENT_SKIN);
			StringBuilder skin = new StringBuilder();
			skin.append(path_separator);
			if (skinType == null){
				skin.append(DEFAULT_SKINTYPE);
			} else {
				skin.append(skinType);
			}
			skin.append(path_separator);
			url = url.replace(DISPATCH_TAG, skin.toString());
		}
		return url;
	}

	/**
	 * 
	 * @param url
	 * @param skinType
	 * @return
	 */
	public String getDispatchURL(String url, String skinType) {
		if (url.indexOf(DISPATCH_VALID_PATH) != -1) {
			StringBuilder skin = new StringBuilder();
			skin.append(path_separator);
			if (skinType == null){
				skin.append(DEFAULT_SKINTYPE);
			} else {
				skin.append(skinType);
			}
			skin.append(path_separator);
			url = url.replace(DISPATCH_TAG, skin.toString());
		}

		return url;
	}
}
