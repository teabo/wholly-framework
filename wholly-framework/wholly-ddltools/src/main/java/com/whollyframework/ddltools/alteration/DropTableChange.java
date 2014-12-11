package com.whollyframework.ddltools.alteration;

import com.whollyframework.ddltools.model.Table;

/**
 * 
 * @author chris
 * @since 2011-12-20
 * 
 */
public class DropTableChange extends ModelChange {

	public DropTableChange(Table _sourceTable) {
		this._table = _sourceTable;
	}

	public String getErrorMessage() {
		return "表存在数据:"+_table.getName();
	}
}
