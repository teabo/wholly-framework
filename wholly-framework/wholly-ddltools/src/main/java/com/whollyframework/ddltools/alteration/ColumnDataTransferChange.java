package com.whollyframework.ddltools.alteration;

import com.whollyframework.ddltools.model.Column;
import com.whollyframework.ddltools.model.Table;

/**
 * 
 * @author chris
 * @since 2011-12-20
 * 
 */
public class ColumnDataTransferChange extends ModelChange {

	/**
	 * data source column
	 */
	private Column _sourceColumn;

	public ColumnDataTransferChange(Table _changedTable, Column _sourceColumn,
			Column _targetColumn) {
		this._table = _changedTable;
		this._sourceColumn = _sourceColumn;
		this._targetColumn = _targetColumn;
	}

	public Column getSourceColumn() {
		return _sourceColumn;
	}

	public String getErrorMessage(){
		// TODO Auto-generated method stub
		return null;
	}
}
