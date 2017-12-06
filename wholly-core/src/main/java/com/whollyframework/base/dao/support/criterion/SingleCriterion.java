package com.whollyframework.base.dao.support.criterion;

public class SingleCriterion implements Criterion {
	private final String propertyName;
	private final Operation operation;

	public SingleCriterion(String propertyName, Operation op) {
		this.propertyName = propertyName;
		this.operation = op;
	}

	/**
     */
	public enum Operation {
		NULL, NOTNULL, EMPTY, NOTEMPTY;

		public String getValueType() {
			switch (this) {
			case NULL:
				return " is null ";
			case NOTNULL:
				return " is not null ";
			case EMPTY:
				return " = '' ";
			case NOTEMPTY:
				return " <> '' ";
			default:
				return null;
			}
		}
	}

	public String getPropertyName() {
		return propertyName;
	}

	public String getOpName() {
		return operation.getValueType();
	}
	
	public Operation getOperation() {
		return operation;
	}

	
	public String toSqlString() {
		final StringBuilder fragment = new StringBuilder();
		fragment.append(getPropertyName()).append(getOpName());
		return fragment.toString();
	}

	public Object[] getParamValues() {
		return new Object[0];
	}

	public String toString() {
		final StringBuilder fragment = new StringBuilder();
		fragment.append(getPropertyName()).append(getOpName());
		return fragment.toString();
	}

	public String getType() {
		return "Single";
	}
}
