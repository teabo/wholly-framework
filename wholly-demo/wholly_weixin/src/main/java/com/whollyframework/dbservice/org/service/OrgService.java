package com.whollyframework.dbservice.org.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.whollyframework.base.service.IDesignService;
import com.whollyframework.util.TreeNode;
import com.whollyframework.dbservice.org.model.Organization;

/**
 * 组织机构管理service接口类
 * 
 * @author Chris Xu
 * @since 2011-08-29
 * 
 */
public interface OrgService extends IDesignService<Organization, String> {
	List<Organization> simpleQueryByParentId(String unitId, String string)
			throws SQLException;

	public Map<String, String> getAllOrgMap(String unitId) throws Exception;

	public List<TreeNode> getAllOrgTree(String unitId, List<String> checkedList)
			throws Exception;
	public List<Organization> getOrganizationByOrganizationOrgType(String orgType)
			throws SQLException;
}
