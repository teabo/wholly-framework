package com.whollyframework.base.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.whollyframework.base.model.DataPackage;
import com.whollyframework.base.model.ParamsTable;

public interface IDesignService<E, ID extends Serializable> {
	public List<E> getAll() throws Exception;

	public DataPackage<E> query(ParamsTable params) throws Exception;

	public E find(ID id) throws Exception;

	public void doCreate(E vo) throws Exception;
	
	public void doUpdate(E vo) throws Exception;

	public void doRemove(E vo) throws Exception;

	public void doRemove(ID[] selects) throws Exception;

	public void doRemove(ID pk) throws Exception;
	
	public boolean isEmpty() throws Exception;

	public Collection<E> simpleQuery(ParamsTable params) throws Exception;
}
