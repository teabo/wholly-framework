package com.whollyframework.base.service;

import java.io.Serializable;
import java.util.List;

import com.whollyframework.base.dao.IDesignDAO;
import com.whollyframework.base.dao.support.AbstractSQLUtils;
import com.whollyframework.base.dao.support.SQLUtils;
import com.whollyframework.base.model.DataPackage;
import com.whollyframework.base.model.ParamsTable;

public abstract class AbstractDesignService<E, ID extends Serializable> {

	public abstract IDesignDAO<E, ID> getDAO();

	public void doRemove(E vo) throws Exception {
		getDAO().remove(vo);
	}

	public E find(ID id) throws Exception {
		return getDAO().find(id);
	}

	public void doRemove(ID pk) throws Exception {
		getDAO().remove(pk);
	}

	public List<E> getAll() throws Exception {
		return getDAO().getAll();
	}

	public DataPackage<E> query(ParamsTable params) throws Exception {
		return getDAO().query(params);
	}

	public void doCreate(E y) throws Exception {
		getDAO().create(y);
	}

	public void doUpdate(E vo) throws Exception {
		getDAO().update(vo);
	}

	public void doRemove(ID[] selects) throws Exception {
		getDAO().remove(selects);
	}

	public List<E> simpleQuery(ParamsTable params) throws Exception {
		AbstractSQLUtils sqlutils = new SQLUtils();
		sqlutils.createWhere(params);
		sqlutils.createOrderBy(params);
		return getDAO().simpleQuery(sqlutils);
	}

	public boolean isEmpty() throws Exception {
		AbstractSQLUtils sqlutils = new SQLUtils();
		if (getDAO().count(sqlutils) <= 0) {
			return true;
		}
		return false;
	}
}
