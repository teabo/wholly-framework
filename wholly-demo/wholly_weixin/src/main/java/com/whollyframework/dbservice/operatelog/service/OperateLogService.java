package com.whollyframework.dbservice.operatelog.service;


import com.whollyframework.base.service.IDesignService;
import com.whollyframework.dbservice.operatelog.model.OperateLog;

public interface OperateLogService extends IDesignService<OperateLog,String>{
   
	public void doInsertObj(OperateLog paramE) throws Exception;
}
