package com.whollyframework.ddltools.alteration;

import com.whollyframework.ddltools.model.Column;
import com.whollyframework.ddltools.model.Table;

/**
 * 
 * @author chris
 * @since 2011-12-20
 * 
 */
public abstract class ModelChange {

	protected Table _table;

	protected Column _targetColumn;
	
	protected boolean stopOnError = true;

	public abstract String getErrorMessage();
	
	/**
	 * @return  the stopOnError
	 * @uml.property  name="stopOnError"
	 */
	public boolean isStopOnError() {
		return stopOnError;
	}

	public Table getTable() {
		return _table;
	}

	public Column getTargetColumn() {
		return _targetColumn;
	}
}
