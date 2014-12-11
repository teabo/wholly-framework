package com.whollyframework.base.dao.support.criterion;

import org.hibernate.criterion.Restrictions;

public class SingleCriterion implements Criterion {
	private final String propertyName;
	private final Op op;

	public SingleCriterion(String propertyName, Op op) {
		this.propertyName = propertyName;
		this.op = op;
	}

	/**
     */
	public enum Op {
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

	public String getOp() {
		return op.getValueType();
	}

	public String toSqlString() {
		final StringBuilder fragment = new StringBuilder();
		fragment.append(getPropertyName()).append(getOp());
		return fragment.toString();
	}

	public Object[] getParamValues() {
		return new Object[0];
	}

	public String toString() {
		final StringBuilder fragment = new StringBuilder();
		fragment.append(getPropertyName()).append(getOp());
		return fragment.toString();
	}

	public org.hibernate.criterion.Criterion buildHibernateCriterion() {
		switch (op) {
		case NULL:
			return Restrictions.isNull(getPropertyName());
		case NOTNULL:
			return Restrictions.isNotNull(getPropertyName());
		case EMPTY:
			return Restrictions.isEmpty(getPropertyName());
		case NOTEMPTY:
			return Restrictions.isNotEmpty(getPropertyName());
		default:
			return null;
		}
	}

	public String getType() {
		return "Single";
	}
}
