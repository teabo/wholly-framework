package com.whollyframework.ddltools.alteration;

import com.whollyframework.ddltools.model.Column;
import com.whollyframework.ddltools.model.Table;

/**
 * 
 * @author chris
 * @since 2011-12-20
 * 
 */
public class ColumnRenameChange extends ModelChange {
	private Column _sourceColumn;

	public ColumnRenameChange(Table _changedColumn, Column _sourceColumn,
			Column _targetColumn) {
		this._table = _changedColumn;
		this._sourceColumn = _sourceColumn;
		this._targetColumn = _targetColumn;
	}

	public Column getSourceColumn() {
		return _sourceColumn;
	}

	public String getErrorMessage() {
		
		return "列名已经存在:" + _targetColumn;
	}
}
