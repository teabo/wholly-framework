package com.whollyframework.ddltools.alteration;

import com.whollyframework.ddltools.model.Column;
import com.whollyframework.ddltools.model.Table;

/**
 * 
 * @author chris
 * @since 2011-12-20
 * 
 */
public class DropColumnChange extends ModelChange {
	private Column _sourceColumn;
	
	public DropColumnChange(Table _changedTable, Column _sourceColumn) {
		this._table = _changedTable;
		this._sourceColumn = _sourceColumn;
	}

	public Column getSourceColumn() {
		return _sourceColumn;
	}

	public String getErrorMessage(){
		
		return "列存在数据:"+_sourceColumn;
	}
}
