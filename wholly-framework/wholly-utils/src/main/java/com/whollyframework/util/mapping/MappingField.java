package com.whollyframework.util.mapping;

import com.whollyframework.annotation.Column;
import com.whollyframework.util.StringUtil;


public class MappingField {
	/**
	 * 唯一标识 id VARCHAR2(30)
	 */
	@Column
	private String id;

	/**
	 * 实体表id svt_id varchar2(30) 30 false false false
	 */
	@Column
	private String svt_id;
	
	/**
	 * 字段显示名 name VARCHAR2(30)
	 */
	@Column
	private String name;

	/**
	 * 字段名 column_name VARCHAR2(30)
	 */
	@Column
	private String column_name;

	/**
	 * 字段类型 data_type varchar2(30)
	 */
	@Column
	private String data_type;

	/**
	 * 字段长度 data_length varchar2(10)
	 */
	@Column
	private int data_length;

	/**
	 * 字段长度 fixed_length varchar2(10)
	 */
	@Column
	private int fixed_length;

	/**
	 * 是否唯一标识 uniquekey number(1)
	 */
	@Column
	private int uniquekey;
	
	/**
	 * 默认值 default_value VARCHAR2(100)
	 */
	@Column
	private String default_value;

	/**
	 * 备注 description varchar2(500)
	 */
	@Column
	private String description;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColumn_name() {
		return column_name;
	}

	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}

	public String getData_type() {
		return data_type;
	}

	public void setData_type(String data_type) {
		this.data_type = data_type;
	}

	public int getData_length() {
		return data_length;
	}

	public void setData_length(int data_length) {
		this.data_length = data_length;
	}

	public int getUniquekey() {
		return uniquekey;
	}

	public void setUniquekey(int uniquekey) {
		this.uniquekey = uniquekey;
	}

	public String getDefault_value() {
		return default_value;
	}

	public void setDefault_value(String default_value) {
		this.default_value = default_value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSvt_id() {
		return svt_id;
	}

	public void setSvt_id(String svt_id) {
		this.svt_id = svt_id;
	}

	public int getFixed_length() {
		return fixed_length;
	}

	public void setFixed_length(int fixed_length) {
		this.fixed_length = fixed_length;
	}

	public boolean equals(Object obj) {
		if (obj instanceof MappingField) {
			return StringUtil.equals(((MappingField) obj).getId(), id);
		}
		return super.equals(obj);
	}
}
