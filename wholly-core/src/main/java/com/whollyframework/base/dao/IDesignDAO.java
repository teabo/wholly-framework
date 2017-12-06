package com.whollyframework.base.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import com.whollyframework.base.dao.support.AbstractSQLUtils;
import com.whollyframework.base.model.DataPackage;
import com.whollyframework.base.model.ParamsTable;

public interface IDesignDAO<E, ID extends Serializable> {
	public List<E> getAll() throws SQLException;

	public List<E> simpleQuery(AbstractSQLUtils filter) throws SQLException;
	
	public List<E> simpleQuery(ParamsTable params) throws SQLException;

	/**
	 * @param params
	 * @return
	 */
	public DataPackage<E> query(ParamsTable params) throws SQLException;
	
	public DataPackage<E> getDatapackage(ParamsTable params,int page,int lines) throws SQLException;
	
	public E find(ID id) throws SQLException;

	public int create(E vo) throws SQLException;

	public int update(E vo) throws SQLException;

	public int remove(AbstractSQLUtils filter) throws SQLException;

	public int remove(ID id) throws SQLException;
	
	public int remove(ID[] selects) throws SQLException;

	public int remove(E vo) throws SQLException;

	public int count(AbstractSQLUtils filter) throws SQLException;

	public int getNextId(String string) throws SQLException;
	
}
