package com.whollyframework.dbservice.menu.service;


import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.whollyframework.base.service.IDesignService;
import com.whollyframework.util.TreeNode;
import com.whollyframework.dbservice.menu.model.Menu;

public interface MenuService extends IDesignService<Menu,String>{

	List<Menu> simpleQueryByParentId(String string)throws SQLException ;
   
	public Map<String, String> getAllMenuMap() throws Exception;
	
	public List<TreeNode> getAllMenuTree(List<String> checkedList) throws Exception ;
	
	public List<TreeNode> getSubMenuTree(String pid,List<String> checkedList) throws SQLException;
	
	public List<Menu> getSubMenu(String pid,List<String> permissionList) throws SQLException;
	
	public List<Menu> getAllMenuByPermission(List<String> permissionList) throws Exception; 
	
	public List<Menu> getTopMenu(String userId) throws SQLException;
	
	public List<Menu> getSubMenuByPid(String userId,String pid) throws SQLException;
}
