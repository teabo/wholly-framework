package com.whollyframework.generator.util;

import java.io.Serializable;

public class Result implements Serializable{

	private static final long serialVersionUID = 1655775811362869533L;
	
	private StringBuffer buf = new StringBuffer();
	
	public Result addItem(String name, String value){
		buf.append("<"+name+">"+value+"</"+name+">");
		return this;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		buffer.append("<Result>");
		buffer.append(buf);
		buffer.append("</Result>");
		return buffer.toString();
	}

	
}
