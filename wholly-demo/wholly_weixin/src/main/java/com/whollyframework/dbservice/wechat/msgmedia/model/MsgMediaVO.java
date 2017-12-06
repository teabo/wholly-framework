package com.whollyframework.dbservice.wechat.msgmedia.model;

import com.whollyframework.base.model.ValueObject;

public class MsgMediaVO extends ValueObject {
	private static final long serialVersionUID = 1L;
	
	private String hqmusicurl;  
	private String description; //简要信息 
	private String musicurl;  
	private String title; //媒体标题 
	private String funcflag;  

	/** setter and getter method */
	public void setHqmusicurl(String hqmusicurl){
		this.hqmusicurl = hqmusicurl;
	}
	public String getHqmusicurl(){
		return this.hqmusicurl;
	}
	public void setDescription(String description){
		this.description = description;
	}
	public String getDescription(){
		return this.description;
	}
	public void setMusicurl(String musicurl){
		this.musicurl = musicurl;
	}
	public String getMusicurl(){
		return this.musicurl;
	}
	public void setTitle(String title){
		this.title = title;
	}
	public String getTitle(){
		return this.title;
	}
	public void setFuncflag(String funcflag){
		this.funcflag = funcflag;
	}
	public String getFuncflag(){
		return this.funcflag;
	}

	
}
