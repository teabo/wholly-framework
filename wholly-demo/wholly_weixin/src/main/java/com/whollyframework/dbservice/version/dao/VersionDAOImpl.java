package com.whollyframework.dbservice.version.dao;

import org.springframework.stereotype.Repository;

import com.whollyframework.base.dao.ibatis.IBatisBaseDAO;
import com.whollyframework.dbservice.version.model.Version;


@Repository("versionDAO")
public class VersionDAOImpl extends IBatisBaseDAO<Version,String> implements VersionDAO {

	public VersionDAOImpl() {
		super(Version.class.getSimpleName());
	}

}
