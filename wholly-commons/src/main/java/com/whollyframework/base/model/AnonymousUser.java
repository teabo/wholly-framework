package com.whollyframework.base.model;

import java.util.ArrayList;
import java.util.List;

import com.whollyframework.authentications.IGroup;
import com.whollyframework.authentications.IOrganization;
import com.whollyframework.authentications.IPermission;
import com.whollyframework.authentications.IRole;
import com.whollyframework.authentications.IUserExtend;

public class AnonymousUser extends BaseUser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 398445588264200904L;
	private IUserExtend userExtend;
	
	public List<? extends IRole> getRoles() {
		return new ArrayList<IRole>();
	}

	public List<? extends IGroup> getGroups() {
		return new ArrayList<IGroup>();
	}

	public List<? extends IPermission> getPermissions() {
		return new ArrayList<IPermission>();
	}

	public List<? extends IOrganization> getOrganizations() {
		return new ArrayList<IOrganization>();
	}
	
	public IUserExtend getUserExtend() {
		return userExtend;
	}

	public void setUserExtend(IUserExtend userExtend) {
		this.userExtend = userExtend;
	}
}
