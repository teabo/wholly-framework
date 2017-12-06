package com.whollyframework.dbservice.operatelog.model;

import com.whollyframework.base.model.ValueObject;

public class OperateLog extends ValueObject implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private String operateType; //操作类型 
	private String operateResult; //操作结果 
	private String userId; //用户标识 
	private String regId; //应用标识 
	private java.util.Date insertTime; //日志入库时间 
	private String operateCondition; //操作条件 
	private String collectType; //日志采集方式 
	private String operateTime; //操作时间 
	private String operateName; //操作所在模块或功能名称 
	private String modelPath; //模块标识
	private String numId; //流水号 
	private int operateNumber; //本次操作返回的条目数 
	private String organization; //单位名称 
	private String userName; //用户姓名 
	private String id; //主键 
	private String errorCode; //失败原因代码 
	private String organizationId; //单位机构代码 
	private String terminalId; //终端标识 
	private String sendid; //日志采集批次号 

	/** setter and getter method */
	public void setOperateType(String operateType){
		this.operateType = operateType;
	}
	public String getOperateType(){
		return this.operateType;
	}
	public void setOperateResult(String operateResult){
		this.operateResult = operateResult;
	}
	public String getOperateResult(){
		return this.operateResult;
	}
	public void setUserId(String userId){
		this.userId = userId;
	}
	public String getUserId(){
		return this.userId;
	}
	public void setRegId(String regId){
		this.regId = regId;
	}
	public String getRegId(){
		return this.regId;
	}
	public void setInsertTime(java.util.Date insertTime){
		this.insertTime = insertTime;
	}
	public java.util.Date getInsertTime(){
		return this.insertTime;
	}
	public void setOperateCondition(String operateCondition){
		this.operateCondition = operateCondition;
	}
	public String getOperateCondition(){
		return this.operateCondition;
	}
	public void setCollectType(String collectType){
		this.collectType = collectType;
	}
	public String getCollectType(){
		return this.collectType;
	}
	public void setOperateTime(String operateTime){
		this.operateTime = operateTime;
	}
	public String getOperateTime(){
		return this.operateTime;
	}
	public String getModelPath() {
		return modelPath;
	}
	public void setModelPath(String modelPath) {
		this.modelPath = modelPath;
	}
	public void setOperateName(String operateName){
		this.operateName = operateName;
	}
	public String getOperateName(){
		return this.operateName;
	}
	public void setNumId(String numId){
		this.numId = numId;
	}
	public String getNumId(){
		return this.numId;
	}
	public void setOperateNumber(int operateNumber){
		this.operateNumber = operateNumber;
	}
	public int getOperateNumber(){
		return this.operateNumber;
	}
	public void setOrganization(String organization){
		this.organization = organization;
	}
	public String getOrganization(){
		return this.organization;
	}
	public void setUserName(String userName){
		this.userName = userName;
	}
	public String getUserName(){
		return this.userName;
	}
	public void setId(String id){
		this.id = id;
	}
	public String getId(){
		return this.id;
	}
	public void setErrorCode(String errorCode){
		this.errorCode = errorCode;
	}
	public String getErrorCode(){
		return this.errorCode;
	}
	public void setOrganizationId(String organizationId){
		this.organizationId = organizationId;
	}
	public String getOrganizationId(){
		return this.organizationId;
	}
	public void setTerminalId(String terminalId){
		this.terminalId = terminalId;
	}
	public String getTerminalId(){
		return this.terminalId;
	}
	public void setSendid(String sendid){
		this.sendid = sendid;
	}
	public String getSendid(){
		return this.sendid;
	}

}