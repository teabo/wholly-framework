package com.whollyframework.base.dao.support.criterion;

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

	public String getType() {
		return "SQL";
	}
}
