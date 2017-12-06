package com.whollyframework.dbservice.org.model;

import com.whollyframework.authentications.IOrganization;
import com.whollyframework.base.model.ValueObject;

/**
 * 部门管理实体
 * 
 * @author tongzhiw
 * 
 *         2014-12-23
 */
public class Organization extends ValueObject implements IOrganization {

	private static final long serialVersionUID = 1L;
	private String name;  
	private String parentCode;  
	private int orgType;  
	private String orgCode;  
	private int delflag;  
	private String telephone;  

	/** setter and getter method */
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return this.name;
	}
	public void setParentCode(String parentCode){
		this.parentCode = parentCode;
	}
	public String getParentCode(){
		return this.parentCode;
	}
	public void setOrgType(int orgType){
		this.orgType = orgType;
	}
	public int getOrgType(){
		return this.orgType;
	}
	public void setOrgCode(String orgCode){
		this.orgCode = orgCode;
	}
	public String getOrgCode(){
		return this.orgCode;
	}
	public void setDelflag(int delflag){
		this.delflag = delflag;
	}
	public int getDelflag(){
		return this.delflag;
	}
	public void setTelephone(String telephone){
		this.telephone = telephone;
	}
	public String getTelephone(){
		return this.telephone;
	}

}