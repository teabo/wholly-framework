package com.whollyframework.base.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.whollyframework.authentications.IGroup;
import com.whollyframework.authentications.IOrganization;
import com.whollyframework.authentications.IPermission;
import com.whollyframework.authentications.IRole;
import com.whollyframework.authentications.IUserExtend;
import com.whollyframework.authentications.IWebUser;

/**
 * @author Chris Xu
 * @since 2011-4-29 下午03:58:21
 */
public class WebUser extends BaseUser implements IWebUser{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6359094093266773829L;

	public WebUser(BaseUser user){
		this.setId(user.getId());
		this.setLoginName(user.getLoginName());
		this.setCertId(user.getCertId());
		this.setUserName(user.getUserName());
		this.setSuperior(user.getSuperior());
		this.setUserLevel(user.getUserLevel());
		this.setOrganizations(user.getOrganizations());
		this.setGroups(user.getGroups());
		this.setPermissions(user.getPermissions());
		this.setRoles(user.getRoles());
		this.setUserExtend(user.getUserExtend());
	}

	private List<? extends IRole> roles;
	
	private List<? extends IGroup> groups;

	private List<? extends IOrganization> organizations;
	
	private List<? extends IPermission> permissions;//权限
	
	private Map<Object,Object> _FromTmpspace = new HashMap<Object,Object>();
	
	private String _onlineUserid;
	
	private String _sessionid;
	
	private IUserExtend userExtend;
	
	public List<? extends IRole> getRoles() {
		return roles;
	}

	public void setRoles(List<? extends IRole> roles) {
		this.roles = roles;
	}

	public List<? extends IGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<? extends IGroup> groups) {
		this.groups = groups;
	}

	public List<? extends IOrganization> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(List<? extends IOrganization> organizations) {
		this.organizations = organizations;
	}

	public List<? extends IPermission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<? extends IPermission> permissions) {
		this.permissions = permissions;
	}
	
	public boolean isUnitAdmin() {
		return ADMIN_UNITS_TYPE==(userLevel&ADMIN_UNITS_TYPE);
	}
	
	public boolean isSuperAdmin() {
		return ADMIN_SUPER_TYPE==(userLevel&ADMIN_SUPER_TYPE);
	}
	
	public boolean isDeveloper() {
		return ADMIN_CONFIG_TYPE==(userLevel&ADMIN_CONFIG_TYPE);
	}
	/* (non-Javadoc)
	 * @see com.whollyframework.security.CEWebUser#clearTmpspace()
	 */
	public void clearTmpspace() {
		_FromTmpspace.clear();
	}

	/* (non-Javadoc)
	 * @see com.whollyframework.security.CEWebUser#getFromTmpspace(java.lang.Object)
	 */
	public Object getFromTmpspace(Object key) {
		return _FromTmpspace.get(key);
	}

	/* (non-Javadoc)
	 * @see com.whollyframework.security.CEWebUser#putToTmpspace(java.lang.Object, java.lang.Object)
	 */
	public void putToTmpspace(Object docid, Object doc) {
		_FromTmpspace.put(docid, doc);
	}

	/* (non-Javadoc)
	 * @see com.whollyframework.security.CEWebUser#removeFromTmpspace(java.lang.Object)
	 */
	public void removeFromTmpspace(Object key) {
		_FromTmpspace.remove(key);
	}

	public void setOnlineUserid(String onlineUserid) {
		_onlineUserid = onlineUserid;
	}
	
	public String getOnlineUserid() {
		return _onlineUserid;
	}

	public void setSessionid(String sessionid) {
		_sessionid = sessionid;
	}
	
	public String getSessionid() {
		return _sessionid;
	}
	
	public IUserExtend getUserExtend() {
		return userExtend;
	}

	public void setUserExtend(IUserExtend userExtend) {
		this.userExtend = userExtend;
	}
}
