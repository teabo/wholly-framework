package com.whollyframework.base.web.tag;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.whollyframework.constans.Web;
import com.whollyframework.util.StringUtil;
import com.whollyframework.utils.Dispatcher;

public class UrlTag extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5253126593180991689L;
	private String value;
	private String action;
	private String id;
	
	private boolean skin = true;

	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String val = getValue();
		String contextPath = request.getContextPath();
		StringBuilder url = new StringBuilder();
		if (skin){
		    url.append(new Dispatcher().getDispatchURL(new StringBuilder().append(contextPath).append("/portal/dispatch").append(val).toString(), request, pageContext
	                .getResponse()));
		} else if (!StringUtil.isBlank(val)) {
			url.append(contextPath).append(val);
		} else {
			url.append(contextPath).append(request.getAttribute(Web.SCOPE_ATTRIBUTE_NAMESPACE)).append("/").append(getAction()).append(".action");
		}
		try {
			pageContext.getOut().write(url.toString());
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

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
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
