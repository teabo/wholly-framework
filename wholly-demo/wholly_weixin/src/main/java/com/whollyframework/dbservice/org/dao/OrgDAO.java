package com.whollyframework.dbservice.org.dao;

import java.sql.SQLException;
import java.util.List;

import com.whollyframework.base.dao.IDesignDAO;
import com.whollyframework.dbservice.org.model.Organization;

/**
 * 组织机构管理DAO接口类
 * 
 * @author Chris Xu
 * @since 2011-08-29
 * 
 */
public interface OrgDAO extends IDesignDAO<Organization, String> {

	List<Organization> simpleQueryByParentId(String unitId, String parentid)
			throws SQLException;

	List<Organization> getOrganizationByOrganizationIdIsNull(String unitId)
			throws SQLException;
	List<Organization> getOrganizationByOrganizationOrgType(String orgType)
			throws SQLException;
}
