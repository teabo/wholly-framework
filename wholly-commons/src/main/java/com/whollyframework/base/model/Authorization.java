package com.whollyframework.base.model;

import com.whollyframework.authentications.IAuthorization;

public class Authorization implements IAuthorization {

	public Authorization(Type type, String name, int value){
		this.type = type;
		this.name = name;
		this.value = value;
	}
	/**
	 * 权限标识名
	 */
	private String name;
	
	/**
	 * 权限类型
	 */
	private Type type;
	
	/**
	 * 权限值
	 */
	private int value;
	
	public int getValue() {
		return value;
	}

	public Type getType() {
		return type;
	}

	public String getName() {
		return name;
	}

}
