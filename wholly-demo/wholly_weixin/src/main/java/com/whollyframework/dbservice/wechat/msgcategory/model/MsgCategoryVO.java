package com.whollyframework.dbservice.wechat.msgcategory.model;

import com.whollyframework.base.model.ValueObject;

public class MsgCategoryVO extends ValueObject {
	private static final long serialVersionUID = 1L;
	
	private String msgType; //被动响应消息类型(1：text 文本消息、2：image 图片消息、3：voice 语音消息、4：video 视频消息、5：music 音乐消息、6：news 图文消息) 
	private String mediaId; //媒体信息ID 
	private String itemDesc; //应答描述 
	private String itemKey; //应答标识 
	private String content; //文本消息 

	/** setter and getter method */
	public void setMsgType(String msgType){
		this.msgType = msgType;
	}
	public String getMsgType(){
		return this.msgType;
	}
	public void setMediaId(String mediaId){
		this.mediaId = mediaId;
	}
	public String getMediaId(){
		return this.mediaId;
	}
	public void setItemDesc(String itemDesc){
		this.itemDesc = itemDesc;
	}
	public String getItemDesc(){
		return this.itemDesc;
	}
	public void setItemKey(String itemKey){
		this.itemKey = itemKey;
	}
	public String getItemKey(){
		return this.itemKey;
	}
	public void setContent(String content){
		this.content = content;
	}
	public String getContent(){
		return this.content;
	}

	
}
