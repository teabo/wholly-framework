package com.whollyframework.dbservice.sysconfig.dao;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.whollyframework.base.dao.ibatis.IBatisBaseDAO;
import com.whollyframework.dbservice.sysconfig.model.SysParam;


@Repository("sysConfigDAO")
public class SysConfigDAOImpl extends IBatisBaseDAO<SysParam,String> implements SysConfigDAO {

	public SysConfigDAOImpl() {
		super(SysParam.class.getSimpleName());
	}

	@Override
	public List<String> getAllParamKey() throws SQLException {
		
		return this.getSqlMapClientTemplate().queryForList(getBeanName() + ".getAllParamKey");
	}

	@Override
	public SysParam findByParamKey(String paramKey) throws SQLException {
		// TODO Auto-generated method stub
		return (SysParam) this.getSqlMapClientTemplate().queryForObject(getBeanName() + ".findByParamKey",paramKey);
	}

}
