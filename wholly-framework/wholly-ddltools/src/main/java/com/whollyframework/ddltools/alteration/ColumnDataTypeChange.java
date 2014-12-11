package com.whollyframework.ddltools.alteration;

import com.whollyframework.ddltools.model.Column;
import com.whollyframework.ddltools.model.Table;

/**
 * 
 * @author chris
 * @since 2011-12-20
 * 
 */
public class ColumnDataTypeChange extends ModelChange {
	private Column _sourceColumn;

	public ColumnDataTypeChange(Table _changedTable, Column _sourceColumn,
			Column _targetColumn) {
		this._table = _changedTable;
		this._sourceColumn = _sourceColumn;
		this._targetColumn = _targetColumn;
	}

	public Column getSourceColumn() {
		return _sourceColumn;
	}

	public String getErrorMessage(){
		
		return "目标列名与原列名不兼容:" + _targetColumn.getName();
	}
}
