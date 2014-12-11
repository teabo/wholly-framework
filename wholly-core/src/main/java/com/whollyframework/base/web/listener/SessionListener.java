package com.whollyframework.base.web.listener;

import java.util.Enumeration;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.whollyframework.utils.SessionContext;

public class SessionListener implements HttpSessionListener {

	public void sessionCreated(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		SessionContext.getInstance().addSession(session);
	}

	public void sessionDestroyed(HttpSessionEvent event) {

		HttpSession session = event.getSession();
		SessionContext.getInstance().removeSession(session);
		/**
		 * User logout, Clean all the object in session.
		 */
		Enumeration<?> enumeration = session.getAttributeNames();
		while (enumeration.hasMoreElements()) {
			String element = (String) enumeration.nextElement();
			session.removeAttribute(element);
		}
	}

}
