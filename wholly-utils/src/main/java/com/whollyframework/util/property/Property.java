package com.whollyframework.util.property;

public class Property {

	private String value; // 参数值
	private String key; // 参数名称
	private String description;// 描述

	public Property(){}
	
	public Property(String key, String value){
		this.key = key;
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPropertyValue(String defaultValue) {
		return value == null ? defaultValue : value;
	}

}
