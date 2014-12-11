package com.whollyframework.util.mapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.whollyframework.annotation.Ignore;

public class MappingTable {

	/**
	 * 唯一标识 id VARCHAR2(30)
	 */
	private String id;
	
	private String name;
	
	private String table_name;
	
	private String description;
	
	private List<MappingField> fields = new ArrayList<MappingField>();

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

	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Ignore
	public List<MappingField> getFields() {
		return fields;
	}

	@Ignore
	public void setFields(List<MappingField> fields) {
		this.fields = fields;
	}
	
	@Ignore
	public String getFieldsIdStr(){
		StringBuffer buf = new StringBuffer();
		if (fields!=null && fields.size()>0){
			for (Iterator<MappingField> iterator = fields.iterator(); iterator.hasNext();) {
				MappingField field = iterator.next();
				if (buf.length()>0){
					buf.append(",");
				}
				buf.append(field.getColumn_name());
			}
		}
		return buf.toString();
	}
}
