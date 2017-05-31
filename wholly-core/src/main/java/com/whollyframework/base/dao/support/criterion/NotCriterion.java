package com.whollyframework.base.dao.support.criterion;

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

	public String getType() {
		return "Not";
	}
}
