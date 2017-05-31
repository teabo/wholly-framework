package com.whollyframework.base.service;

import java.io.Serializable;

import com.whollyframework.base.dao.IDesignDAO;
import com.whollyframework.base.dao.MybatisCurdRepository;

public abstract class MybatisCurdService<E, ID extends Serializable> extends BaseService<E, ID> {

	public abstract MybatisCurdRepository<E, ID> getRepository();

	@Override
	public IDesignDAO<E, ID> getDAO() {
		return getRepository();
	}


}
