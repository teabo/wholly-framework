package com.whollyframework.dbservice.permission.model;

import com.whollyframework.base.model.ValueObject;

public class Permission extends ValueObject implements java.io.Serializable{


	private static final long serialVersionUID = 8564995159324126024L;
	
	private String resId; //根据资源类型，取决于具体资源对应表的主键 
	private String resType; //资源类型，包含菜单资源，及以后预留资源 
	private String roleId; //null 

	/** setter and getter method */
	public void setResId(String resId){
		this.resId = resId;
	}
	public String getResId(){
		return this.resId;
	}
	public void setResType(String resType){
		this.resType = resType;
	}
	public String getResType(){
		return this.resType;
	}
	public void setRoleId(String roleId){
		this.roleId = roleId;
	}
	public String getRoleId(){
		return this.roleId;
	}

}