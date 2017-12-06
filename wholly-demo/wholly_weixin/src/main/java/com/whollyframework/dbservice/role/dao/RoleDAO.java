package com.whollyframework.dbservice.role.dao;


import java.sql.SQLException;
import java.util.List;

import com.whollyframework.base.dao.IDesignDAO;
import com.whollyframework.base.dao.support.AbstractSQLUtils;
import com.whollyframework.dbservice.role.model.Role;



public interface RoleDAO extends IDesignDAO<Role,String> {
	public int remove(String sqlMapId, AbstractSQLUtils filter) throws SQLException ;
	
	public List<String> selectUserIdByRole(String roleid) throws SQLException ;

	public List<String> getAllIds()throws SQLException ;
	
	public List<Role> getRolesByMid (String mid, String name) throws SQLException;
	
	public List<Role> getRolesNotEqualMid(String mid, String name) throws SQLException;
}
