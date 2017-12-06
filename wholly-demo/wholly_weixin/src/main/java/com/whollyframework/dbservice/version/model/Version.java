package com.whollyframework.dbservice.version.model;

import com.whollyframework.base.model.ValueObject;

public class Version extends ValueObject implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private String appVersion; //应用版本 
	private java.util.Date createDate; //创建时间 
	private String versionDesc; //版本描述 
	private String dbVersion; //数据库表版本 

	/** setter and getter method */
	public void setAppVersion(String appVersion){
		this.appVersion = appVersion;
	}
	public String getAppVersion(){
		return this.appVersion;
	}
	public void setCreateDate(java.util.Date createDate){
		this.createDate = createDate;
	}
	public java.util.Date getCreateDate(){
		return this.createDate;
	}
	public void setVersionDesc(String versionDesc){
		this.versionDesc = versionDesc;
	}
	public String getVersionDesc(){
		return this.versionDesc;
	}
	public void setDbVersion(String dbVersion){
		this.dbVersion = dbVersion;
	}
	public String getDbVersion(){
		return this.dbVersion;
	}

}