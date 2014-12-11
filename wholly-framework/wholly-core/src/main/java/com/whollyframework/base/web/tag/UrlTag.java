package com.whollyframework.base.web.tag;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.whollyframework.utils.Dispatcher;

public class UrlTag extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5253126593180991689L;
	private String value;

	private String id;
	
	private boolean skin = true;

	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String val = getValue();
		String contextPath = request.getContextPath();
		String url;
		if (skin){
		    url = new Dispatcher().getDispatchURL(contextPath + "/portal/dispatch" + val, request, pageContext
	                .getResponse());
		} else {
		    url = contextPath + val;
		}
		try {
			pageContext.getOut().write(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;

	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

    public boolean isSkin() {
        return skin;
    }

    public void setSkin(boolean skin) {
        this.skin = skin;
    }
}
