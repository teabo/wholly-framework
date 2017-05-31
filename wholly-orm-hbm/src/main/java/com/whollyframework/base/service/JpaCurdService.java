package com.whollyframework.base.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.whollyframework.base.dao.JpaCurdDAO;
import com.whollyframework.base.dao.support.AbstractSQLUtils;
import com.whollyframework.base.model.DataPackage;
import com.whollyframework.base.model.ParamsTable;
/**
 * jpa的crud实现需要注入PagingAndSortingRepository
 * @author zhangzhongmin
 *
 * @param <E>
 * @param <ID>
 */
public abstract class JpaCurdService<E, ID extends Serializable>{

	public abstract PagingAndSortingRepository<E,ID> getRepository();

	public JpaCurdDAO<E, ID> getDAO() {
		return (JpaCurdDAO<E, ID>) getRepository();
	}

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

	protected List<E> simpleQuery(ParamsTable params, AbstractSQLUtils sqlutils) throws Exception {
		sqlutils.createWhere(params);
		sqlutils.createOrderBy(params);
		return getDAO().simpleQuery(sqlutils);
	}

	protected boolean isEmpty(AbstractSQLUtils sqlutils) throws Exception {
		if (getDAO().count(sqlutils) <= 0) {
			return true;
		}
		return false;
	}
	

}
