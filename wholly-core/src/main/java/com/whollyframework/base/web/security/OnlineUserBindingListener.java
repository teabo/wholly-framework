/*
 * Created on 2005-4-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.whollyframework.base.web.security;

/**
 * @author Administrator
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.log4j.Logger;

import com.whollyframework.authentications.IWebUser;
import com.whollyframework.util.property.DefaultProperty;
import com.whollyframework.util.sequence.Sequence;

public class OnlineUserBindingListener implements HttpSessionBindingListener {
	private static final Logger logger = Logger
			.getLogger(OnlineUserBindingListener.class);

	IWebUser _user;

	public OnlineUserBindingListener(IWebUser user) {
		_user = user;
	}

	public void valueBound(HttpSessionBindingEvent httpSessionBindingEvent) {
		String onlineUserid = null;
		try {
			onlineUserid = Sequence.getTimeSequence();
		} catch (Exception e) {
			onlineUserid = Sequence.getSequence();
		}
		if (this._user != null) {
			this._user.setOnlineUserid(onlineUserid);
			this._user.setSessionid(httpSessionBindingEvent.getSession()
					.getId());
		}
		try {
			OnlineUsers.add(onlineUserid, _user);
			if ("true".equalsIgnoreCase(DefaultProperty.getProperty("DEBUG")))
				logger.info("用户[" + _user.getName() + "]绑定会话["
						+ _user.getSessionid() + "]成功！");
		} catch (Throwable e) {
			e.printStackTrace();
			if ("true".equalsIgnoreCase(DefaultProperty.getProperty("DEBUG")))
				logger.error(
						"用户[" + _user.getName() + "]绑定会话["
								+ _user.getSessionid() + "]出现异常:", e);
		}
	}

	public void valueUnbound(HttpSessionBindingEvent httpSessionBindingEvent) {
		if (this._user != null) {
			try {
				OnlineUsers.remove(this._user.getOnlineUserid());
				if ("true".equalsIgnoreCase(DefaultProperty
						.getProperty("DEBUG")))
					logger.info("用户[" + _user.getName() + "]取消绑定会话["
							+ _user.getSessionid() + "]成功！");
			} catch (Throwable e) {
				e.printStackTrace();
				if ("true".equalsIgnoreCase(DefaultProperty
						.getProperty("DEBUG")))
					logger.error(
							"用户[" + _user.getName() + "]取消绑定会话["
									+ _user.getSessionid() + "]出现异常出现异常:", e);
			}
		}
	}
}
