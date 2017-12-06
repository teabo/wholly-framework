package com.whollyframework.dbservice.permission.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.whollyframework.base.model.ParamsTable;
import com.whollyframework.base.service.AbstractDesignService;
import com.whollyframework.dbservice.menu.service.MenuService;
import com.whollyframework.dbservice.permission.dao.PermissionDAO;
import com.whollyframework.dbservice.permission.model.Permission;
import com.whollyframework.dbservice.role.model.Role;
import com.whollyframework.dbservice.user.service.UserServiceImpl;

@Service("permissionService")
@ClassLog(remark="权限管理")
public class PermissionServiceImpl extends AbstractDesignService<Permission,String> implements PermissionService {
    
    @Resource(name = "permissionDAO")
    private PermissionDAO permissionDao;
    
    @Autowired
    private MenuService MenuService ;
    
    @Autowired
    private UserServiceImpl userService; 
    
    public IDesignDAO<Permission,String> getDAO() {
        return permissionDao;
    }

	@Override
	public void doRoleResBind(String roleid, List<String> resList, IWebUser currUser) throws Exception {
				//获取数据库中已经保存的关联关系
				List<String> rOldList = this.findResidByRole(roleid);
				//迭代资源id，保存新的关联关系
				for(String rid :resList){
					if(!rOldList.contains(rid)){
						Permission ru = new Permission();
						ru.setRoleId(roleid);
						ru.setResId(rid);
						ru.setResType("2");//菜单类型
						if(currUser!=null){
							ru.setAuthorId(currUser.getId());
							ru.setAuthor(currUser.getName());
						}
						ru.setRoleId(roleid);
						ru.setCreated(new Date());
						this.doCreate(ru);
					}
				}
				List<String> deleteList = getDeleteList(rOldList,resList);
				for(String del : deleteList){
					SQLUtils sqlUtil = new SQLUtils();
					Criterion role = Criterions.eq("role_id", roleid);
					Criterion res = Criterions.eq("res_id", del);
					Junction junc = Criterions.conjunction().add(role).add(res);
					sqlUtil.addCriterion(junc);
					permissionDao.remove(sqlUtil);
				}
		}
    
	/**
	 * 根据角色id获取库中已经保存的资源id集合
	 * @param roleid
	 * @return
	 * @throws SQLException
	 */
	public List<String> findResidByRole(String roleid) throws SQLException {
		ParamsTable param = new ParamsTable();
		param.setParameter("s_role_id", roleid);
		List<Permission> ps = permissionDao.simpleQuery(param);
		List<String> pStr = new ArrayList<String>();
		if(ps!=null && ps.size()>0){
			for(Permission p : ps){
				pStr.add(p.getResId());
			}
		}
		return pStr;
	}

	/**
	 * 根据用户ID获取该用户具有的权限资源
	 * @param userId
	 * @return
	 * @throws SQLException
	 */
	public List<String> findResidByUserId(String userId) throws SQLException{
		List<Role> roles = userService.getUerRoles(userId);
		List<String> resids = new ArrayList<String>();
		for(Role r: roles){
			List<String> tmp = findResidByRole(r.getId());
			for(String s : tmp){//去除重复资源
				if(!resids.contains(s)){
					resids.add(s);
				}
			}
		}
		return resids;
	}
	
	public List<String> getDeleteList(List<String> oldList,List<String> newList){
		//旧的有，新的没有，表示需要删除
		List<String> deleteList = new ArrayList<String>();
		for(String old : oldList){
			if(!newList.contains(old)){
				deleteList.add(old);
			}
		}
		return deleteList;
	}

	@Override
	public void bindMenuForRoles(String[] rids, String mid) throws Exception {
		List<String> oldRids = getRidsByMid(mid); //原有的
		
		if(rids != null && rids.length > 0 && mid != null && mid.trim().length() > 0) {
			for(String rid : rids) {
				if(!oldRids.contains(rid)) {
					Permission permission = new Permission();
					permission.setRoleId(rid);
					permission.setResId(mid);
					permission.setResType("2");
					permission.setCreated(new Date());
					this.doCreate(permission);
				}
			}
		}
		
		//要去除的部分
		List<String> delList = getDeleteList(oldRids, Arrays.asList(rids));
		if(delList != null && delList.size() > 0) {
			for(String del : delList) {
				SQLUtils sqlUtil = new SQLUtils();
				Criterion role = Criterions.eq("role_id", del);
				Criterion res = Criterions.eq("res_id", mid);
				Junction junc = Criterions.conjunction().add(role).add(res);
				sqlUtil.addCriterion(junc);
				permissionDao.remove(sqlUtil);
			}
		}
		
	}

	@Override
	public List<String> getRidsByMid(String mid) throws SQLException {
		List<String> rids = new ArrayList<String>();
		
		ParamsTable param = new ParamsTable();
		param.setParameter("s_RES_ID", mid);
		List<Permission> permissions = permissionDao.simpleQuery(param);
		if(permissions != null && permissions.size() > 0) {
			for(Permission permission : permissions) {
				if(!rids.contains(permission.getRoleId())) {
					rids.add(permission.getRoleId());
				}
			}
		}
		
		return rids;
	}

	@Override
	public void deletePermissionByMid(String id) throws SQLException {
		SQLUtils sqlUtil = new SQLUtils();
		Criterion res = Criterions.eq("res_id", id);
		Junction junc = Criterions.conjunction().add(res);
		sqlUtil.addCriterion(junc);
		permissionDao.remove(sqlUtil);
	}
    
}
