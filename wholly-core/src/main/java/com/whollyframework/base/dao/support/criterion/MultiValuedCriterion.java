package com.whollyframework.base.dao.support.criterion;

import org.hibernate.criterion.Restrictions;

public class MultiValuedCriterion implements Criterion {
	private final String propertyName;
	private final Object[] values;
	private final Op op;

	/**
     */
	public enum Op {
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

	public MultiValuedCriterion(String propertyName, Object[] values, Op op) {
		this.propertyName = propertyName;
		this.values = values;
		this.op = op;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public Object getValue() {
		return values;
	}

	public String getOp() {
		return op.getValueType();
	}

	public String toSqlString() {
		final StringBuilder fragment = new StringBuilder();
		fragment.append("(").append(getPropertyName());
		switch (op) {
		case IN:
		case NOT_IN:
			fragment.append(getOp()).append("(");
			for (int i = 0; values != null && i < values.length; i++) {
				if (i > 0) fragment.append(",");
				fragment.append("?");
			}
			fragment.append(")");
			break;
		case BETWEEN:
			fragment.append(getOp()).append("? AND ?");
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
		switch (op) {
		case IN:
		case NOT_IN:
			fragment.append(getOp()).append("(");
			for (int i = 0; values != null && i < values.length; i++) {
				if (i > 0) fragment.append(",");
				fragment.append(values[i]);
			}
			fragment.append(")");
			break;
		case BETWEEN:
			fragment.append(getOp()).append(values[0]).append(" AND ").append(values[1]);
			break;
		default:
			break;
		}
		fragment.append(")");

		return fragment.toString();
	}

	public org.hibernate.criterion.Criterion buildHibernateCriterion() {
		switch (op) {
		case IN:
			return Restrictions.in(getPropertyName(), values);
		case NOT_IN:
			return Restrictions.not(Restrictions.in(getPropertyName(), values));
		case BETWEEN:
			return Restrictions.between(getPropertyName(), values[0], values[1]);
		default:
			return null;
		}
	}

	public String getType() {
		return "MultiValued";
	}
}
