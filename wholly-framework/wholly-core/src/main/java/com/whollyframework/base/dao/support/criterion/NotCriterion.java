package com.whollyframework.base.dao.support.criterion;

import org.hibernate.criterion.Restrictions;

public class NotCriterion implements Criterion {
	private final Criterion criterion;

	public NotCriterion(Criterion criterion) {
		this.criterion = criterion;
	}

	public Criterion getCriterion() {
		return criterion;
	}

	public String toSqlString() {
		// not
		final StringBuilder fragment = new StringBuilder();
		fragment.append("not (").append(criterion.toSqlString()).append(")");
		return fragment.toString();
	}

	public Object[] getParamValues() {
		return criterion.getParamValues();
	}

	@Override
	public String toString() {
		return "not " + criterion.toString();
	}

	public org.hibernate.criterion.Criterion buildHibernateCriterion() {
		return Restrictions.not(criterion.buildHibernateCriterion());
	}

	public String getType() {
		return "Not";
	}
}
