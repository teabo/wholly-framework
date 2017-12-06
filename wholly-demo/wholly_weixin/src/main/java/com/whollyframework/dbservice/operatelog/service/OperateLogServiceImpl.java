package com.whollyframework.dbservice.operatelog.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.whollyframework.base.dao.IDesignDAO;
import com.whollyframework.base.service.AbstractDesignService;
import com.whollyframework.dbservice.operatelog.dao.OperateLogDAO;
import com.whollyframework.dbservice.operatelog.model.OperateLog;

@Service("operateLogService")
public class OperateLogServiceImpl extends AbstractDesignService<OperateLog,String> implements OperateLogService {
    
    @Resource(name = "operateLogDAO")
    private OperateLogDAO operateLogDao;
    
    
    public IDesignDAO<OperateLog,String> getDAO() {
        return operateLogDao;
    }
    
    public void doInsertObj(OperateLog paramE) throws Exception{
    		operateLogDao.create(paramE);
    }
    
}
