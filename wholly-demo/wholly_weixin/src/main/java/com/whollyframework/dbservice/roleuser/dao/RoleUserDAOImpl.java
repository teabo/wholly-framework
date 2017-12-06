package com.whollyframework.dbservice.roleuser.dao;

import org.springframework.stereotype.Repository;

import com.whollyframework.base.dao.ibatis.IBatisBaseDAO;
import com.whollyframework.dbservice.roleuser.model.RoleUser;


@Repository("roleUserDAO")
public class RoleUserDAOImpl extends IBatisBaseDAO<RoleUser,String> implements RoleUserDAO {

	public RoleUserDAOImpl() {
		super(RoleUser.class.getSimpleName());
	}

}
