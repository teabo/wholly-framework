package com.whollyframework.ddltools.model;

public class IndexColumn implements Cloneable {
	
	public IndexColumn(String field_name){
		this.fieldName = field_name;
	}

	/**
	 * 列ID
	 */
	private String id;

	/**
	 * 索引名
	 */
	private String fieldName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String field_name) {
		this.fieldName = field_name;
	}
}
