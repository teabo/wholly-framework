package com.whollyframework.dbservice.menu.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whollyframework.annotation.ClassLog;
import com.whollyframework.base.dao.IDesignDAO;
import com.whollyframework.base.dao.support.SQLUtils;
import com.whollyframework.base.dao.support.criterion.Criterion;
import com.whollyframework.base.dao.support.criterion.Criterions;
import com.whollyframework.base.dao.support.criterion.Junction;
import com.whollyframework.base.service.AbstractDesignService;
import com.whollyframework.util.StringUtil;
import com.whollyframework.util.TreeNode;
import com.whollyframework.dbservice.menu.dao.MenuDAO;
import com.whollyframework.dbservice.menu.model.Menu;
import com.whollyframework.dbservice.permission.dao.PermissionDAO;
import com.whollyframework.dbservice.permission.service.PermissionService;

@Service("menuService")
@ClassLog(remark="菜单管理")
public class MenuServiceImpl extends AbstractDesignService<Menu,String> implements MenuService {
    
    @Resource(name = "menuDAO")
    private MenuDAO menuDao;
    
    @Resource(name = "permissionDAO")
    private PermissionDAO permissionDao;
    
    @Autowired
    PermissionService permissionService; 
    
    public IDesignDAO<Menu,String> getDAO() {
        return menuDao;
    }
    
    @Override
	public int doRemove(String pk) throws Exception {
		// 级联删除Permission
		deletePermissionByMid(pk);
		return menuDao.remove(pk);
	}
    
	public void deletePermissionByMid(String id) throws SQLException {
		SQLUtils sqlUtil = new SQLUtils();
		Criterion res = Criterions.eq("res_id", id);
		Junction junc = Criterions.conjunction().add(res);
		sqlUtil.addCriterion(junc);
		permissionDao.remove(sqlUtil);
	}


	public List<Menu> getTopMenu(String userId) throws SQLException{
    	List<Menu> topList = new ArrayList<Menu>();
    	List<String> mids = permissionService.findResidByUserId(userId);
    	List<Menu> list = this.simpleQueryByParentId("");
    	for(Menu m : list){
    		if(mids!=null && mids.contains(m.getId())){
    			topList.add(m);
    		}
    	}
    	return topList;
    }
    
    public List<Menu> getSubMenuByPid(String userId,String pid) throws SQLException{
    	
    	List<String> permissionList = permissionService.findResidByUserId(userId);
    	
    	return getSubMenu(pid,permissionList);
    	
    }
    
	@Override
	public List<Menu> simpleQueryByParentId(String pid) throws SQLException {
		// TODO Auto-generated method stub
		if (StringUtil.isBlank(pid) || "null".equals(pid)) {
			return menuDao.getMenuByPmenuIdIsNull();
		} else {
			return menuDao.simpleQueryByParentId(pid);
		}
		
	}
	
	/**
	 * 
	 * 获取菜单
	 */
	public Map<String, String> getAllMenuMap() throws Exception {
		Map<String, String> map = new LinkedHashMap<String, String>();
		List<Menu> list = this.simpleQueryByParentId("");
		map.put("", "");
		if (list != null && list.size() > 0) {
			for (Iterator<Menu> iterator = list.iterator(); iterator.hasNext();) {
				Menu menu = (Menu) iterator.next();
				map.put(String.valueOf(menu.getId()), menu.getName());
				Map<String, String> smap = get_submenulist(menu.getId(), 2);
				if (!smap.isEmpty())
					map.putAll(smap);
			}
		}
		return map;
	}
	
	/**
	 * 获取所有菜单，包括子菜单
	 * @return
	 * @throws Exception
	 */
	public List<Menu> getAllMenuByPermission(List<String> permissionList) throws Exception {
		List<Menu> treeNodes = new ArrayList<Menu>();
		List<Menu> list = this.simpleQueryByParentId("");
		
		if (list != null && list.size() > 0) {
			
			for (Iterator<Menu> iterator = list.iterator(); iterator.hasNext();) {
				Menu menu = (Menu) iterator.next();
				if(permissionList!=null && permissionList.contains(menu.getId())){
					//map.put(String.valueOf(menu.getId()), menu.getName());
					List<Menu> childrens = getSubMenu(menu.getId(),permissionList);
					if(childrens!=null&&childrens.size()>0){
						//System.out.println("=p_menu="+menu.getName());
						menu.setSubMenu(childrens);		
						treeNodes.add(menu);
					}
				}
 			}
		}
		return treeNodes;
	}
	
	/**
	 * 获取节点树菜单
	 * @param checkedList 需要选中的菜单资源
	 * @return
	 * @throws Exception
	 */
	public List<TreeNode> getAllMenuTree(List<String> checkedList) throws Exception {
		List<TreeNode> treeNodes = new ArrayList<TreeNode>();
		List<Menu> list = this.simpleQueryByParentId("");
		
		if (list != null && list.size() > 0) {
			
			for (Iterator<Menu> iterator = list.iterator(); iterator.hasNext();) {
				Menu menu = (Menu) iterator.next();
				//map.put(String.valueOf(menu.getId()), menu.getName());
				TreeNode pnode = new TreeNode();
				pnode.setText(menu.getName());
				pnode.setId(menu.getId());
				List<TreeNode> childrens = getSubMenuTree(menu.getId(), checkedList);
				if(childrens!=null&&childrens.size()>0){
					pnode.setChildren(childrens);					
				}
				treeNodes.add(pnode);
			}
		}
		return treeNodes;
	}

	public List<TreeNode> getSubMenuTree(String pid,List<String> checkedList) throws SQLException{
		
		List<TreeNode> treeNodes = new ArrayList<TreeNode>();
		
		List<Menu> list = this.simpleQueryByParentId(pid);
		if (list != null && list.size() > 0) {
			for (Iterator<Menu> iterator = list.iterator(); iterator.hasNext();) {
				Menu menu = (Menu) iterator.next();
				TreeNode pnode = new TreeNode();
				pnode.setText(menu.getName());
				pnode.setId(menu.getId());
				List<TreeNode> childrens  = getSubMenuTree(menu.getId(),checkedList);
				if(childrens!=null&&childrens.size()>0){
					pnode.setChildren(childrens);					
				}else{
					if(checkedList!=null && checkedList.contains(menu.getId())){
						pnode.setChecked(true);//叶子节点才选中
					}
				}
				treeNodes.add(pnode);
			}
		}
		return treeNodes;
	}
	
	/**
	 * 根据权限获取子菜单
	 * @param pid
	 * @param permissionList
	 * @return
	 * @throws SQLException
	 */
	public List<Menu> getSubMenu(String pid,List<String> permissionList) throws SQLException{
		List<Menu> treeNodes = new ArrayList<Menu>();
		List<Menu> list = this.simpleQueryByParentId(pid);
		if (list != null && list.size() > 0) {
			for (Iterator<Menu> iterator = list.iterator(); iterator.hasNext();) {
				Menu menu = (Menu) iterator.next();
				if(permissionList!=null && permissionList.contains(menu.getId())){
					//System.out.println("=sub_menu="+menu.getName());
					List<Menu> childrens  = getSubMenu(menu.getId(),permissionList);
					if(childrens!=null&&childrens.size()>0){
						menu.setSubMenu(childrens);					
					}	
					treeNodes.add(menu);
				}
			}
		}
		return treeNodes;
	}
	
	private Map<String, String> get_submenulist(String pid, int deep)
			throws Exception {
		String pixf = "|-------------------------";
		Map<String, String> map = new LinkedHashMap<String, String>();
		List<Menu> list = this.simpleQueryByParentId(pid);
		if (list != null && list.size() > 0) {
			for (Iterator<Menu> iterator = list.iterator(); iterator.hasNext();) {
				Menu menu = (Menu) iterator.next();
				map.put(String.valueOf(menu.getId()), pixf.substring(0, deep)
						+ menu.getName());
				Map<String, String> smap = get_submenulist(menu.getId(), 
						deep + 1);
				if (!smap.isEmpty())
					map.putAll(smap);
			}
		}
		return map;
	}
    
}
