package com.whollyframework.dbservice.sysconfig.service;


import java.sql.SQLException;
import java.util.List;

import com.whollyframework.base.service.IDesignService;
import com.whollyframework.dbservice.sysconfig.model.SysParam;

public interface SysConfigService extends IDesignService<SysParam,String>{

	List<String> getAllParamKey() throws SQLException ;

	SysParam findByParamKey(String paramKey) throws SQLException;
	
    
}
