package com.whollyframework.base.dao.support.criterion;

import org.hibernate.criterion.Restrictions;

public class SQLCriterion implements Criterion {
	private final String sql;

	public SQLCriterion(String sql) {
		this.sql = sql;
	}

	public String getPropertyName() {
		return sql;
	}

	public String toSqlString() {
		return sql;
	}

	public Object[] getParamValues() {
		return new Object[0];
	}

	public org.hibernate.criterion.Criterion buildHibernateCriterion() {
		return Restrictions.sqlRestriction(sql);
	}

	public String getType() {
		return "SQL";
	}
}
