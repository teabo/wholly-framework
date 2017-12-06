package com.whollyframework.dbservice.user.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.whollyframework.authentications.IUser;
import com.whollyframework.base.dao.ibatis.IBatisBaseDAO;
import com.whollyframework.base.model.DataPackage;
import com.whollyframework.base.model.ParamsTable;
import com.whollyframework.dbservice.role.model.Role;
import com.whollyframework.dbservice.user.model.UserVO;
import com.whollyframework.util.StringUtil;
/**
 * 系统管理--用户管理
 * @author WangWenGuang
 * @2014-12-23
 */
@SuppressWarnings("unchecked")
@Repository("userDAO")
public class UserDAOImpl extends IBatisBaseDAO<UserVO,String> implements UserDAO {

	public UserDAOImpl() {
		// 将VO对象名传入基类(IBatisBaseDAO)，给beanName属性赋值。
		// 与sqlmap中的namespace对应。在进行数据库语句操作时，需要根据namespace来区分获取不同的操作语句。
		super(UserVO.class.getSimpleName());
	}

	/**
	 * 根据部门ID与角色ID获取用户列表
	 * 
	 * @param oid
	 *            部门ID
	 * @param rid
	 *            角色ID
	 * @return 用户列表
	 * @throws SQLException 
	 */
	public List<UserVO> queryByOidAndRid(String oid, String rid) throws SQLException {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("oid", oid);
		if (!StringUtil.isBlank(rid))
			params.put("rid", rid);
		return this.getSqlMapClientTemplate().queryForList(
				getBeanName() + ".selectByOidAndRid", params);
	}

	public List<UserVO> queryByRId(String rid) throws SQLException {
		return this.getSqlMapClientTemplate().queryForList(
				getBeanName() + ".selectsByRid", rid);
	}


	public IUser getUserByLoginname(String unit_id, String loginname) throws SQLException {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("unit_id", unit_id);
		params.put("loginname", loginname);
		return (IUser) getSqlMapClientTemplate().queryForObject(
				getBeanName() + ".getUserByLoginname", params);
	}

	public DataPackage<UserVO> getDatapackageByRid(ParamsTable params,
			String rid, int page, int lines) throws SQLException {
		if (params == null)
			params = new ParamsTable();
		params.setParameter("rid", rid);
		params.setParameter("_currpage", page);
		params.setParameter("_pagelines", lines);
		return (DataPackage<UserVO>) getDatapackageByParamsMap("selectByRid", params.getParams());
	}

	public DataPackage<UserVO> getDatapackageByOid(ParamsTable params,
			String oid, int page, int lines) throws SQLException {
		if (params == null)
			params = new ParamsTable();
		params.setParameter("oid", oid);
		params.setParameter("_currpage", page);
		params.setParameter("_pagelines", lines);
		return (DataPackage<UserVO>) getDatapackageByParamsMap("selectByOid", params.getParams());
	}
	
	/**
	 * 根据用户ID获取部门领导列表
	 * @param uid 用户ID
	 * @return
	 */
	public List<UserVO> getLeaderByUId(String uid) throws SQLException{
		return getSqlMapClientTemplate().queryForList(getBeanName() +".selectLeaderByUid", uid);
	}
	
	/** 根据用户查询关联的角色id */
	public List<String> queryRidByUid(String uid)throws SQLException{
		return getSqlMapClientTemplate().queryForList(getBeanName()+".queryRidByUid", uid);
	}
	
	/** 根据用户查询其部门id */
	public List<String> queryOrgIdByUserId(String uid)throws SQLException{
		return getSqlMapClientTemplate().queryForList(getBeanName()+".queryOrgIdByUserId", uid);
	}

	public IUser getUserByCertId(String cert_id, String unit_id) throws SQLException {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("unit_id", unit_id);
		params.put("cert_id", cert_id);
		return (IUser) find("getUserByCert_id", params);
	}
	
	/** 根据单位查询用户 */
	public List<UserVO> queryUnitUser(String uid)throws SQLException{
		return getSqlMapClientTemplate().queryForList(getBeanName()+".queryUnitUser", uid);
	}


	public void updateStatus(IUser user)throws SQLException{
		getSqlMapClientTemplate().update(getBeanName()+".updateStatus", user);
	}
	
	@Override
	public List<Role> getUerRoles(String userId) throws SQLException {
		return getSqlMapClientTemplate().queryForList(
			       getBeanName() + ".gerUserRoles", userId);
	}
}
