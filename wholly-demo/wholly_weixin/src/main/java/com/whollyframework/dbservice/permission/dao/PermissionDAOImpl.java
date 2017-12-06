package com.whollyframework.dbservice.permission.dao;

import org.springframework.stereotype.Repository;

import com.whollyframework.base.dao.ibatis.IBatisBaseDAO;
import com.whollyframework.dbservice.permission.model.Permission;


@Repository("permissionDAO")
public class PermissionDAOImpl extends IBatisBaseDAO<Permission,String> implements PermissionDAO {

	public PermissionDAOImpl() {
		super(Permission.class.getSimpleName());
	}

}
