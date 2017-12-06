package com.whollyframework.dbservice.sysconfig.model;

import com.whollyframework.base.model.ValueObject;

public class SysParam extends ValueObject implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private String paramValue; //参数值 
	private String paramKey; //参数名称 
	private String paramDesc;
	
	
	public String getParamValue() {
		return paramValue;
	}
	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}
	public String getParamKey() {
		return paramKey;
	}
	public void setParamKey(String paramKey) {
		this.paramKey = paramKey;
	}
	public String getParamDesc() {
		return paramDesc;
	}
	public void setParamDesc(String paramDesc) {
		this.paramDesc = paramDesc;
	}

	/** setter and getter method */
		
	
	
}