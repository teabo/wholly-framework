package com.whollyframework.dbservice.permission.service;


import java.sql.SQLException;
import java.util.List;

import com.whollyframework.authentications.IWebUser;
import com.whollyframework.base.service.IDesignService;
import com.whollyframework.dbservice.permission.model.Permission;

public interface PermissionService extends IDesignService<Permission,String>{

	void doRoleResBind(String roleid, List<String> asList, IWebUser currUser) throws Exception; 
   
	public List<String> findResidByRole(String roleid) throws SQLException;
	
	/**
	 * 根据用户ID获取所有权限资源
	 * @param userId
	 * @return
	 * @throws SQLException
	 */
	public List<String> findResidByUserId(String userId) throws SQLException;
	
	/**
	 * 为角色绑定菜单项
	 * @param rids 角色id
	 * @param mid 菜单项id
	 */
	public void bindMenuForRoles(String[] rids, String mid)  throws Exception;
	
	/**
	 * 根据菜单资源id获取相关联的角色id
	 * @param mid
	 * @return
	 */
	public List<String> getRidsByMid(String mid)  throws SQLException;
	
	/**
	 * 根据资源id删除权限
	 * @param id
	 */
	public void deletePermissionByMid(String id)  throws SQLException;
}
