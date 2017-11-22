package com.whollyframework.generator.util;

import org.xml.sax.SAXException;

/**
 * 自定义异常
 * @author Andy
 * @date 2011-12-15
 */
public class WebException extends SAXException {

	private static final long serialVersionUID = -7982120914187621227L;
	
	private String massage;
	private Throwable cause;
	private String errorCode;

	public WebException () {
		this("");
	}
	
	public WebException(String massage) {
		super(massage);
		this.massage = massage;
	}
	
	public WebException(String massage, String errorCode) {
		super(massage);
		this.massage = massage;
		this.errorCode = errorCode;
	}
	
	public WebException (String massage, Throwable cause, String errorCode) {
		super(massage);
		this.massage = massage;
		this.cause = cause;
		this.errorCode = errorCode;
	}

	public String toString() {
		StringBuffer returnValue = new StringBuffer(massage);
		if (cause != null) {
			returnValue.append("\n: ").append(cause.getMessage());
		}
		Result result = new Result();
		result.addItem("code", "-1").addItem("massage", returnValue.toString())
				.addItem("id", "-1").addItem("error-code", errorCode);
		return result.toString();
	}
	
}
