package com.whollyframework.base.dao;

import com.whollyframework.base.model.ValueObject;


public interface IRuntimeDAO {
	public void create(ValueObject vo) throws Exception;

	public void remove(String pk) throws Exception;

	public void update(ValueObject vo) throws Exception;

	public ValueObject find(String id) throws Exception;
}
