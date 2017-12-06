package com.whollyframework.dbservice.org.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.whollyframework.annotation.ClassLog;
import com.whollyframework.base.dao.IDesignDAO;
import com.whollyframework.base.service.AbstractDesignService;
import com.whollyframework.util.StringUtil;
import com.whollyframework.util.TreeNode;
import com.whollyframework.dbservice.org.dao.OrgDAO;
import com.whollyframework.dbservice.org.model.Organization;

/**
 * 组织机构管理service实现类
 * 
 * @author Chris Xu
 * @since 2011-08-29
 * 
 */
@Service("orgService")
@ClassLog(remark="组织机构")
public class OrgServiceImpl extends AbstractDesignService<Organization, String> implements OrgService {

	@Resource(name = "orgDAO")
	private OrgDAO orgDao;

	@Override
	public IDesignDAO<Organization, String> getDAO() {
		return orgDao;
	}

	@Override
	public List<Organization> simpleQueryByParentId(String unitId, String pid) throws SQLException {
		if (StringUtil.isBlank(pid) || "null".equals(pid)) {
			return orgDao.getOrganizationByOrganizationIdIsNull(unitId);
		} else {
			return orgDao.simpleQueryByParentId(unitId, pid);
		}

	}

	/**
	 * 
	 * 获取菜单
	 */
	public Map<String, String> getAllOrgMap(String unitId) throws Exception {
		Map<String, String> map = new LinkedHashMap<String, String>();
		List<Organization> list = this.simpleQueryByParentId(unitId, "");
		map.put("0", "顶级机构");
		if (list.size() > 0) {
			for (Iterator<Organization> iterator = list.iterator(); iterator.hasNext();) {
				Organization organization = (Organization) iterator.next();
				map.put(String.valueOf(organization.getId()), organization.getName());
				Map<String, String> smap = getSubOrganizationlist(unitId, organization.getId(), 2);
				if (!smap.isEmpty())
					map.putAll(smap);
			}
		}
		return map;
	}

	/**
	 * 获取节点树菜单
	 * 
	 * @param checkedList
	 *            需要选中的菜单资源
	 * @return
	 * @throws Exception
	 */
	public List<TreeNode> getAllOrgTree(String unitId, List<String> checkedList) throws Exception {
		List<TreeNode> treeNodes = new ArrayList<TreeNode>();
		List<Organization> list = this.simpleQueryByParentId(unitId, "");

		if (list != null && list.size() > 0) {

			for (Iterator<Organization> iterator = list.iterator(); iterator.hasNext();) {
				Organization organization = (Organization) iterator.next();
				// map.put(String.valueOf(Organization.getId()),
				// Organization.getName());
				TreeNode pnode = new TreeNode();
				pnode.setText(organization.getName());
				pnode.setId(organization.getId());
				List<TreeNode> childrens = getSubOrganizationTree(unitId, organization.getId(), checkedList);
				if (childrens != null && childrens.size() > 0) {
					pnode.setChildren(childrens);
				}
				if (checkedList != null && checkedList.contains(organization.getId())) {
					pnode.setChecked(true);
					System.out.println("=========================true" + organization.getName());
				}
				if (pnode.getChildren() != null)
					treeNodes.add(pnode);
			}
		}
		return treeNodes;
	}

	private List<TreeNode> getSubOrganizationTree(String unitId, String pid, List<String> checkedList)
			throws SQLException {

		List<TreeNode> treeNodes = new ArrayList<TreeNode>();

		List<Organization> list = this.simpleQueryByParentId(unitId, pid);
		if (list != null && list.size() > 0) {
			for (Iterator<Organization> iterator = list.iterator(); iterator.hasNext();) {
				Organization Organization = (Organization) iterator.next();
				TreeNode pnode = new TreeNode();
				pnode.setText(Organization.getName());
				pnode.setId(Organization.getId());
				if (checkedList != null && checkedList.contains(Organization.getId())) {
					pnode.setChecked(true);
					System.out.println("=========================true" + Organization.getName());
				}
				List<TreeNode> childrens = getSubOrganizationTree(unitId, Organization.getId(), checkedList);
				if (childrens != null && childrens.size() > 0) {
					pnode.setChildren(childrens);
				}
				treeNodes.add(pnode);
			}
		}
		return treeNodes;
	}

	private Map<String, String> getSubOrganizationlist(String unitId, String pid, int deep) throws Exception {
		String pixf = "|-------------------------";
		Map<String, String> map = new LinkedHashMap<String, String>();
		List<Organization> list = this.simpleQueryByParentId(unitId, pid);
		if (list != null && list.size() > 0) {
			for (Iterator<Organization> iterator = list.iterator(); iterator.hasNext();) {
				Organization organization = (Organization) iterator.next();
				map.put(String.valueOf(organization.getId()), pixf.substring(0, deep) + organization.getName());
				Map<String, String> smap = getSubOrganizationlist(unitId, organization.getId(), deep + 1);
				if (!smap.isEmpty())
					map.putAll(smap);
			}
		}
		return map;
	}

	@Override
	public List<Organization> getOrganizationByOrganizationOrgType(
			String orgType) throws SQLException {
		// TODO Auto-generated method stub
		return orgDao.getOrganizationByOrganizationOrgType(orgType);
	}
   
    	 
}
