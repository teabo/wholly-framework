package com.whollyframework.ddltools.alteration;

import com.whollyframework.ddltools.model.Column;
import com.whollyframework.ddltools.model.Table;

/**
 * 
 * @author chris
 * @since 2011-12-20
 * 
 */
public class AddColumnChange extends ModelChange {
	
	public AddColumnChange(Table _changedTable, Column _targetColumn) {
		this._table = _changedTable;
		this._targetColumn = _targetColumn;
	}

	public String getErrorMessage()  {
		 return "列名已经存在:"+_targetColumn.getName();
	}
}
