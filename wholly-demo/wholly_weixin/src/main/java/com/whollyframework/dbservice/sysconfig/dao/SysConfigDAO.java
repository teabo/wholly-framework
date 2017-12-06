package com.whollyframework.dbservice.sysconfig.dao;


import java.sql.SQLException;
import java.util.List;

import com.whollyframework.base.dao.IDesignDAO;
import com.whollyframework.dbservice.sysconfig.model.SysParam;


public interface SysConfigDAO extends IDesignDAO<SysParam,String> {

	List<String> getAllParamKey() throws SQLException ;

	SysParam findByParamKey(String name) throws SQLException;
	
}
