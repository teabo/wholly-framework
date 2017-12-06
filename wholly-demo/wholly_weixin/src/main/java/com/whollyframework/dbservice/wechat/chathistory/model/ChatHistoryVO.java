package com.whollyframework.dbservice.wechat.chathistory.model;

import com.weixin.sdk.msg.in.InMsg;
import com.weixin.sdk.msg.out.OutMsg;
import com.whollyframework.base.model.ValueObject;

public class ChatHistoryVO extends ValueObject {
	private static final long serialVersionUID = 1L;
	
	private String msgType; //被动响应消息类型(1：text 文本消息、2：image 图片消息、3：voice 语音消息、4：video 视频消息、5：music 音乐消息、6：news 图文消息) 
	private String fromUserName; //发送用户 
	private String toUserName; //接收用户 
	private int createTime; //创建时间 
	private String msgXml; //消息内容XML 
	
	public ChatHistoryVO() {}
	
	public ChatHistoryVO(InMsg inMsg) {
		setFromUserName(inMsg.getFromUserName());
		setToUserName(inMsg.getToUserName());
		setMsgType(inMsg.getMsgType());
		setMsgXml(inMsg.getMsgXML());
		setCreateTime(inMsg.getCreateTime());
	}
	
	public ChatHistoryVO(OutMsg inMsg) {
		setFromUserName(inMsg.getFromUserName());
		setToUserName(inMsg.getToUserName());
		setMsgType(inMsg.getMsgType());
		setMsgXml(inMsg.getMsgXML());
		setCreateTime(inMsg.getCreateTime());
	}

	/** setter and getter method */
	public void setMsgType(String msgType){
		this.msgType = msgType;
	}
	public String getMsgType(){
		return this.msgType;
	}
	public void setFromUserName(String fromUserName){
		this.fromUserName = fromUserName;
	}
	public String getFromUserName(){
		return this.fromUserName;
	}
	public void setToUserName(String toUserName){
		this.toUserName = toUserName;
	}
	public String getToUserName(){
		return this.toUserName;
	}
	public void setCreateTime(int createTime){
		this.createTime = createTime;
	}
	public int getCreateTime(){
		return this.createTime;
	}
	public void setMsgXml(String msgXml){
		this.msgXml = msgXml;
	}
	public String getMsgXml(){
		return this.msgXml;
	}

	
}
