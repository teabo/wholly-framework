package com.whollyframework.base.service;

import java.io.Serializable;
import java.util.List;

import com.whollyframework.base.dao.support.SQLUtils;
import com.whollyframework.base.model.ParamsTable;

public abstract class AbstractDesignService<E, ID extends Serializable> extends BaseService<E, ID> {

	public List<E> simpleQuery(ParamsTable params) throws Exception {
		return simpleQuery(params, new SQLUtils());
	}

	public boolean isEmpty() throws Exception {
		return isEmpty(new SQLUtils());
	}
}
