package com.whollyframework.dbservice.wechat.msgnews.model;

import com.whollyframework.base.model.ValueObject;

public class MsgNewsVO extends ValueObject {
	private static final long serialVersionUID = 1L;
	
	private String description; //简要信息 
	private String itemId; //主题ID 
	private String title; //新闻标题 
	private String picurl; //图片URL 
	private String url; //新闻URL 

	/** setter and getter method */
	public void setDescription(String description){
		this.description = description;
	}
	public String getDescription(){
		return this.description;
	}
	public void setItemId(String itemId){
		this.itemId = itemId;
	}
	public String getItemId(){
		return this.itemId;
	}
	public void setTitle(String title){
		this.title = title;
	}
	public String getTitle(){
		return this.title;
	}
	public void setPicurl(String picurl){
		this.picurl = picurl;
	}
	public String getPicurl(){
		return this.picurl;
	}
	public void setUrl(String url){
		this.url = url;
	}
	public String getUrl(){
		return this.url;
	}

	
}
