package com.whollyframework.dbservice.role.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.whollyframework.base.dao.ibatis.IBatisBaseDAO;
import com.whollyframework.base.dao.support.AbstractSQLUtils;
import com.whollyframework.dbservice.role.model.Role;


@Repository("roleDAO")
public class RoleDAOImpl extends IBatisBaseDAO<Role,String> implements RoleDAO {

	public RoleDAOImpl() {
		super(Role.class.getSimpleName());
	}
	
    public int remove(String sqlMapId, AbstractSQLUtils filter) throws SQLException {
	     return getSqlMapClientTemplate().delete(
	       getBeanName() + "."+sqlMapId, filter);
   }
    
   public List<String> selectUserIdByRole(String roleid)throws SQLException{
	   return getSqlMapClientTemplate().queryForList(
		       getBeanName() + ".selectUserIdByRole", roleid);
   }

   
	public List<String> getAllIds() throws SQLException {
		// TODO Auto-generated method stub
		return getSqlMapClientTemplate().queryForList(
			       getBeanName() + ".getAllIds");
	}

	/**
	 * 根据资源id查询角色信息
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Role> getRolesByMid(String mid, String name) throws SQLException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("resId", mid);
		map.put("name", name);
		return getSqlMapClientTemplate().queryForList(getBeanName() + ".getRolesByMid", map);
	}

	/**
	 * 查询不等于某资源id的角色信息
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Role> getRolesNotEqualMid(String mid, String name) throws SQLException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("resId", mid);
		map.put("name", name);
		return getSqlMapClientTemplate().queryForList(getBeanName() + ".getRolesNotEqualMid", map);
	}

}
