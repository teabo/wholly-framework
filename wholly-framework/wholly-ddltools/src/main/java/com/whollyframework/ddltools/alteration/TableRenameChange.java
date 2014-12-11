package com.whollyframework.ddltools.alteration;

import com.whollyframework.ddltools.model.Table;

/**
 * 
 * @author chris
 * @since 2011-12-20
 * 
 */
public class TableRenameChange extends ModelChange {
	private Table _changedTable;

	private Table _targetTable;

	public TableRenameChange(Table _changedTable, Table _targetTable) {
		this._table = _targetTable;
		this._changedTable = _changedTable;
		this._targetTable = _targetTable;
	}

	public Table getChangedTable() {
		return _changedTable;
	}

	public Table getTargetTable() {
		return _targetTable;
	}

	public String getErrorMessage(){
		
		return "表名已经存在:"+_targetTable.getName();
	}
}
