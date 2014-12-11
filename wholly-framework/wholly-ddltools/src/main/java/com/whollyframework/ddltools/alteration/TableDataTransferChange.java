package com.whollyframework.ddltools.alteration;

import com.whollyframework.ddltools.model.Table;

/**
 * 
 * @author chris
 * @since 2011-12-20
 * 
 */
public class TableDataTransferChange extends ModelChange {
	private Table _sourceTable;

	private Table _targetTable;

	public TableDataTransferChange(Table _sourceTable, Table _targetTable) {
		this._sourceTable = _sourceTable;
		this._targetTable = _targetTable;
		this._table = _targetTable;
	}

	public Table getSourceTable() {
		return _sourceTable;
	}

	public Table getTargetTable() {
		return _targetTable;
	}

	public String getErrorMessage(){
		
		return null;
	}
}
