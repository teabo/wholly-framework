package com.whollyframework.dbservice.org.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.whollyframework.base.dao.ibatis.IBatisBaseDAO;
import com.whollyframework.dbservice.org.model.Organization;

/**
 * 组织机构管理DAO实现类
 * 
 * @author Chris Xu
 * @since 2011-08-29
 * 
 */
@Repository("orgDAO")
public class OrgDAOImpl extends IBatisBaseDAO<Organization, String> implements
		OrgDAO {

	public OrgDAOImpl() {
		super(Organization.class.getSimpleName());
	}

	@Override
	public List<Organization> simpleQueryByParentId(String unitId,
			String parentid) throws SQLException {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("parentid", parentid);
		params.put("unit_id", unitId);
		return this.getSqlMapClientTemplate().queryForList(
				this.getBeanName() + ".simpleQueryByParentId", params);
	}

	@Override
	public List<Organization> getOrganizationByOrganizationIdIsNull(
			String unitId) throws SQLException {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("unit_id", unitId);
		return this.getSqlMapClientTemplate().queryForList(
				this.getBeanName() + ".getOrganizationByOrganizationIdIsNull",
				params);
	}
	
	@Override
	public List<Organization> getOrganizationByOrganizationOrgType(String orgType)
    throws SQLException {
		   List<Organization> oraglist = new ArrayList<Organization>();
		   oraglist = this.getSqlMapClientTemplate().queryForList(
					this.getBeanName() + ".getOrganizationByOrganizationOrgType", orgType);
		   return oraglist;
	}
}
