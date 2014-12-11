package com.whollyframework.base.service.hibernate;

import java.io.Serializable;
import java.util.List;

import com.whollyframework.base.dao.support.AbstractSQLUtils;
import com.whollyframework.base.dao.support.HibernateSQLUtils;
import com.whollyframework.base.model.ParamsTable;
import com.whollyframework.base.service.AbstractDesignService;

public abstract class HibernateDesignService<E, ID extends Serializable> extends AbstractDesignService<E, ID>{

	public List<E> simpleQuery(ParamsTable params) throws Exception {
		AbstractSQLUtils sqlutils = new HibernateSQLUtils();
		sqlutils.createWhere(params);
		sqlutils.createOrderBy(params);
		return getDAO().simpleQuery(sqlutils);
	}

	public boolean isEmpty() throws Exception {
		AbstractSQLUtils sqlutils = new HibernateSQLUtils();
		if (getDAO().count(sqlutils) <= 0) {
			return true;
		}
		return false;
	}
}
