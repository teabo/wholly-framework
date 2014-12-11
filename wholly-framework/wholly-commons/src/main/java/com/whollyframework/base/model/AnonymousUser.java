package com.whollyframework.base.model;

import java.util.ArrayList;
import java.util.List;

import com.whollyframework.authentications.IGroup;
import com.whollyframework.authentications.IOrganization;
import com.whollyframework.authentications.IPermission;
import com.whollyframework.authentications.IRole;

public class AnonymousUser extends BaseUser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 398445588264200904L;

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

}
