package com.whollyframework.dbservice.role.service;


import java.sql.SQLException;
import java.util.List;

import com.whollyframework.authentications.IWebUser;
import com.whollyframework.authentications.service.IRoleService;
import com.whollyframework.base.service.IDesignService;
import com.whollyframework.dbservice.role.model.Role;

public interface RoleService extends IDesignService<Role,String>, IRoleService<Role,String>{
   
	public void doRoleUserBind(String roleid, List<String> uidList,IWebUser currUser)throws Exception;
	
	public List<String> findResidByRole(String roleid) throws SQLException;
	
	public List<String> getAllIds() throws SQLException;

	public void doRoleUserRemove(String roleid, List<String> asList) throws SQLException;
	
	public List<Role> showRolesByMid(String mid, String name) throws SQLException;
	public List<Role> showRolesNotEqualMid(String mid, String name) throws SQLException;
}
