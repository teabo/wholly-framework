package com.whollyframework.dbservice.roleuser.model;

import com.whollyframework.base.model.ValueObject;

public class RoleUser extends ValueObject implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private String userId; //null 
	private String roleId; //null 

	/** setter and getter method */
	public void setUserId(String userId){
		this.userId = userId;
	}
	public String getUserId(){
		return this.userId;
	}
	public void setRoleId(String roleId){
		this.roleId = roleId;
	}
	public String getRoleId(){
		return this.roleId;
	}

}