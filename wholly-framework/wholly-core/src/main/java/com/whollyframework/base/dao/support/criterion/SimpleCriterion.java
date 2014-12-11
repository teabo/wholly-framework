package com.whollyframework.base.dao.support.criterion;

import org.hibernate.criterion.Restrictions;

public class SimpleCriterion implements Criterion {
	private final String propertyName;
	private final Object value;
	private boolean ignoreCase;
	private final Op op;
	private Object[] paramValues;

	/**
     */
	public enum Op {
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

	public SimpleCriterion(String propertyName, Object value, Op op) {
		this.propertyName = propertyName;
		this.value = value;
		this.op = op;
	}

	public SimpleCriterion(String propertyName, Object value, Op op, boolean ignoreCase) {
		this.propertyName = propertyName;
		this.value = value;
		this.op = op;
		this.ignoreCase = ignoreCase;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public Object getValue() {
		return value;
	}

	public String getOp() {
		return op.getValueType();
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
		fragment.append(getOp()).append("?");
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
		fragment.append(getPropertyName()).append(getOp()).append(value);
		return fragment.toString();
	}

	public org.hibernate.criterion.Criterion buildHibernateCriterion() {
		switch (op) {
		case EQ:
			return Restrictions.eq(getPropertyName(), value);
		case NE:
			return Restrictions.ne(getPropertyName(), value);
		case LE:
			return Restrictions.le(getPropertyName(), value);
		case GE:
			return Restrictions.ge(getPropertyName(), value);
		case LT:
			return Restrictions.lt(getPropertyName(), value);
		case GT:
			return Restrictions.gt(getPropertyName(), value);
		case LIKE:
			return Restrictions.like(getPropertyName(), value);
		default:
			return null;
		}

	}

	public String getType() {
		return "Simple";
	}

}
