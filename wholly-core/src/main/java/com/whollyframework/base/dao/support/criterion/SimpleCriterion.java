package com.whollyframework.base.dao.support.criterion;

public class SimpleCriterion implements Criterion {
	private final String propertyName;
	private final Object value;
	private boolean ignoreCase;
	private final Operation operation;
	private Object[] paramValues;

	/**
     */
	public enum Operation {
		EQ, NE, LE, GE, LT, GT, LIKE;

		public String getValueType() {
			switch (this) {
			case EQ:
				return " = ";
			case NE:
				return " <> ";
			case LE:
				return " <= ";
			case GE:
				return " >= ";
			case LT:
				return " < ";
			case GT:
				return " > ";
			case LIKE:
				return " like ";
			default:
				return null;
			}
		}
	}

	public SimpleCriterion(String propertyName, Object value, Operation op) {
		this.propertyName = propertyName;
		this.value = value;
		this.operation = op;
	}

	public SimpleCriterion(String propertyName, Object value, Operation op, boolean ignoreCase) {
		this.propertyName = propertyName;
		this.value = value;
		this.operation = op;
		this.ignoreCase = ignoreCase;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public Object getValue() {
		return value;
	}

	public String getOpName() {
		return operation.getValueType();
	}
	
	public Operation getOperation() {
		return operation;
	}

	public boolean isIgnoreCase() {
		return ignoreCase;
	}

	public void setIgnoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	public String toSqlString() {
		final StringBuilder fragment = new StringBuilder();
		fragment.append(getPropertyName());
		fragment.append(getOpName()).append("?");
		return fragment.toString();
	}

	public Object[] getParamValues() {
		if (paramValues == null) {
			paramValues = new Object[] { value };
		}
		return paramValues;
	}

	public String toString() {
		final StringBuilder fragment = new StringBuilder();
		fragment.append(getPropertyName()).append(getOpName()).append(value);
		return fragment.toString();
	}

	public String getType() {
		return "Simple";
	}

}
