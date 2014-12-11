package com.whollyframework.ddltools.alteration;

import com.whollyframework.ddltools.model.Table;

/**
 * 
 * @author chris
 * @since 2011-12-20
 * 
 */
public class AddTableChange extends ModelChange {

	public AddTableChange(Table _newTable) {
		this._table = _newTable;
	}

	public String getErrorMessage(){
		return "表名已经存在:" + this._table.getName();
	}
	
}
