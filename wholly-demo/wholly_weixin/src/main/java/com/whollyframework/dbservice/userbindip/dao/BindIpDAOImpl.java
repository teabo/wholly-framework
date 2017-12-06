package com.whollyframework.dbservice.userbindip.dao;

import org.springframework.stereotype.Repository;

import com.whollyframework.base.dao.ibatis.IBatisBaseDAO;
import com.whollyframework.dbservice.userbindip.model.BindIp;

/**
 * 用户管理模块--IP绑定
 * @author WangWenGuang
 * @2015-1-13
 */
@Repository("bindIpDAO")
public class BindIpDAOImpl extends IBatisBaseDAO<BindIp,String> implements BindIpDAO {

	public BindIpDAOImpl() {
		super(BindIp.class.getSimpleName());
	}

}
