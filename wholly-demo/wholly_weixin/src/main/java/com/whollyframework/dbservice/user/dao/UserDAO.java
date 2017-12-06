package com.whollyframework.dbservice.user.dao;

import java.sql.SQLException;
import java.util.List;

import com.whollyframework.authentications.IUser;
import com.whollyframework.base.dao.IDesignDAO;
import com.whollyframework.base.model.DataPackage;
import com.whollyframework.base.model.ParamsTable;
import com.whollyframework.dbservice.role.model.Role;
import com.whollyframework.dbservice.user.model.UserVO;

/**
 * 系统管理--用户管理模块
 * @author WangWenGuang
 * @2014-12-23
 */
public interface UserDAO extends IDesignDAO<UserVO,String> {

	/**
	 * 根据部门ID与角色ID获取用户列表
	 * 
	 * @param oid
	 * 			  部门ID
	 * @param rid
	 *            角色ID
	 * @return 用户列表
	 */
	List<UserVO> queryByOidAndRid(String oid, String rid) throws SQLException;
	
	/**
	 * 根据角色ID获取用户列表
	 * 
	 * @param rid
	 *            角色ID
	 * @return 用户列表
	 */
	List<UserVO> queryByRId(String rid) throws SQLException;


	/**
	 * 根据角色ID获取用户列表数据包对象
	 * 
	 * @param rid
	 *            角色ID
	 * @return 用户列表数据包对象
	 */
	DataPackage<UserVO> getDatapackageByRid(ParamsTable params, String rid,
			int page, int lines) throws SQLException;

	/**
	 * 根据组织机构ID获取用户列表数据包对象
	 * 
	 * @param oid
	 *            组织机构ID
	 * @return 用户列表数据包对象
	 */
	DataPackage<UserVO> getDatapackageByOid(ParamsTable params, String oid,
			int page, int lines) throws SQLException;

    /**
     * 通过登录名获取用户
     * @param unit_id
     * @param loginname
     * @return
     * @throws SQLException
     */
	IUser getUserByLoginname(String unit_id, String loginname) throws SQLException;
	/**
	 * 通过证件号码获取用户
	 * @param cert_id
	 * @param unit_id
	 * @return
	 * @throws SQLException
	 */
	IUser getUserByCertId(String cert_id, String unit_id) throws SQLException;

	
	/**
	 * 根据用户ID获取部门领导列表
	 * @param uid 用户ID
	 * @return
	 */
	List<UserVO> getLeaderByUId(String uid) throws SQLException;

	/** 根据用户查询关联的角色id */
	List<String> queryRidByUid(String uid)throws SQLException;

	/** 根据用户查询其部门id */
	List<String> queryOrgIdByUserId(String uid)throws SQLException;

	/** 根据单位查询用户 */
	List<UserVO> queryUnitUser(String uid)throws SQLException;
	/**
	 * 启用禁用用户
	 * @param user
	 * @throws SQLException
	 */
	public void updateStatus(IUser user)throws SQLException;
	/**
	 * 获取用户角色
	 * @param userId
	 * @return
	 * @throws SQLException
	 */
	public List<Role> getUerRoles(String userId)throws SQLException;
}
