package com.whollyframework.base.dao.support.criterion;

public class MultiValuedCriterion implements Criterion {
	private final String propertyName;
	private final Object[] values;
	private final Operation operation;

	/**
     */
	public enum Operation {
		IN, NOT_IN, BETWEEN;

		public String getValueType() {
			switch (this) {
			case IN:
				return " in ";
			case NOT_IN:
				return " not in ";
			case BETWEEN:
				return " between ";
			default:
				return null;
			}
		}
	}

	public MultiValuedCriterion(String propertyName, Object[] values, Operation op) {
		this.propertyName = propertyName;
		this.values = values;
		this.operation = op;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public Object getValue() {
		return values;
	}

	public Operation getOperation() {
		return this.operation;
	}

	public String getOpName() {
		return operation.getValueType();
	}
	
	public String toSqlString() {
		final StringBuilder fragment = new StringBuilder();
		fragment.append("(").append(getPropertyName());
		switch (operation) {
		case IN:
		case NOT_IN:
			fragment.append(getOpName()).append("(");
			for (int i = 0; values != null && i < values.length; i++) {
				if (i > 0) fragment.append(",");
				fragment.append("?");
			}
			fragment.append(")");
			break;
		case BETWEEN:
			fragment.append(getOpName()).append("? AND ?");
			break;
		default:
			break;
		}
		fragment.append(")");

		return fragment.toString();
	}

	public Object[] getParamValues() {
		return values;
	}

	public String toString() {
		final StringBuilder fragment = new StringBuilder();
		fragment.append("(").append(getPropertyName());
		switch (operation) {
		case IN:
		case NOT_IN:
			fragment.append(getOpName()).append("(");
			for (int i = 0; values != null && i < values.length; i++) {
				if (i > 0) fragment.append(",");
				fragment.append(values[i]);
			}
			fragment.append(")");
			break;
		case BETWEEN:
			fragment.append(getOpName()).append(values[0]).append(" AND ").append(values[1]);
			break;
		default:
			break;
		}
		fragment.append(")");

		return fragment.toString();
	}

	public String getType() {
		return "MultiValued";
	}
}
