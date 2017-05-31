package com.whollyframework.base.service;

import java.io.Serializable;
import java.util.List;

import com.whollyframework.base.dao.support.HibernateSQLUtils;
import com.whollyframework.base.model.ParamsTable;

public abstract class HibernateCurdService<E, ID extends Serializable> extends BaseService<E, ID> {
	public List<E> simpleQuery(ParamsTable params) throws Exception {
		return simpleQuery(params, new HibernateSQLUtils());
	}

	public boolean isEmpty() throws Exception {
		return isEmpty(new HibernateSQLUtils());
	}
}
