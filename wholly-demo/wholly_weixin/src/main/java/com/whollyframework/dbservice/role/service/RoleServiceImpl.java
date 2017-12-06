package com.whollyframework.dbservice.role.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whollyframework.annotation.ClassLog;
import com.whollyframework.authentications.IWebUser;
import com.whollyframework.base.dao.IDesignDAO;
import com.whollyframework.base.dao.support.SQLUtils;
import com.whollyframework.base.dao.support.criterion.Criterion;
import com.whollyframework.base.dao.support.criterion.Criterions;
import com.whollyframework.base.dao.support.criterion.Junction;
import com.whollyframework.base.service.AbstractDesignService;
import com.whollyframework.dbservice.role.dao.RoleDAO;
import com.whollyframework.dbservice.role.model.Role;
import com.whollyframework.dbservice.roleuser.dao.RoleUserDAO;
import com.whollyframework.dbservice.roleuser.model.RoleUser;
import com.whollyframework.dbservice.roleuser.service.RoleUserService;
import com.whollyframework.util.StringUtil;

@Service("roleService")
@ClassLog(remark="角色管理")
public class RoleServiceImpl extends AbstractDesignService<Role, String>
		implements RoleService {

	@Resource(name = "roleDAO")
	private RoleDAO roleDao;

	@Autowired
	private RoleUserService roleUserService;

	@Autowired
	private RoleUserDAO roleUserDAO;

	public IDesignDAO<Role, String> getDAO() {
		return roleDao;
	}

	public void doRoleUserBind(String roleid, List<String> uidList,
			IWebUser currUser) throws Exception {

		/**
		 *  获取数据库中已经保存的关联关系
		 */
		List<String> rOldList = this.findResidByRole(roleid);
		/**
		 *  迭代资源id，保存新的关联关系
		 */
		for (String uid : uidList) {
			if (rOldList != null && !rOldList.contains(uid)) {
				RoleUser ru = new RoleUser();
				ru.setUserId(uid);
				if (currUser != null) {
					ru.setAuthorId(currUser.getId());
					ru.setAuthor(currUser.getName());
				}
				ru.setRoleId(roleid);
				ru.setCreated(new Date());
				roleUserService.doCreate(ru);
			}
		}
	}

	public List<String> findResidByRole(String roleid) throws SQLException {
		if(roleid!=null){
			return roleDao.selectUserIdByRole(roleid);	
		}else{
			return null;
		}
		
	}

	public List<String> getDeleteList(List<String> oldList, List<String> newList) {
		/**
		 *  旧的有，新的没有，表示需要删除
		 */
		List<String> deleteList = new ArrayList<String>();
		for (String old : oldList) {
			if (!newList.contains(old)) {
				deleteList.add(old);
			}
		}
		return deleteList;
	}

	@Override
	public void doRoleUserRemove(String roleid, List<String> userList)
			throws SQLException {
		if(!StringUtil.isBlank(roleid)){
			for (String del : userList) {
				SQLUtils sqlUtil = new SQLUtils();
				Criterion role = Criterions.eq("role_id", roleid);
				Criterion res = Criterions.eq("user_id", del);
				Junction junc = Criterions.conjunction().add(role).add(res);
				sqlUtil.addCriterion(junc);
				roleUserDAO.remove(sqlUtil);
			}			
		}
	}

	@Override
	public List<String> getAllIds() throws SQLException {
		return roleDao.getAllIds();
	}

	@Override
	public List<Role> showRolesByMid(String mid, String name) throws SQLException {
		return roleDao.getRolesByMid(mid, name);
	}

	@Override
	public List<Role> showRolesNotEqualMid(String mid, String name) throws SQLException {
		return roleDao.getRolesNotEqualMid(mid, name);
	}

}
