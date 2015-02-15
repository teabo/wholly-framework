package com.whollyframework.protobuf.pojo;

import java.io.Serializable;

public class InnerMessagePojo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3502984328747108249L;
	private String name = "name";
	private int id;
	private EnumType type = EnumType.UNIVERSAL;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public EnumType getType() {
		return type;
	}
	public void setType(EnumType type) {
		this.type = type;
	}
	
	
}
