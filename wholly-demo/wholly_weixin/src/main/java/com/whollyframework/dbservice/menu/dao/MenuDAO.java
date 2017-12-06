package com.whollyframework.dbservice.menu.dao;


import java.sql.SQLException;
import java.util.List;

import com.whollyframework.base.dao.IDesignDAO;
import com.whollyframework.dbservice.menu.model.Menu;


public interface MenuDAO extends IDesignDAO<Menu,String> {

	List<Menu> simpleQueryByParentId(String parentid)throws SQLException ;

	List<Menu> getMenuByPmenuIdIsNull()throws SQLException ;

	
}
