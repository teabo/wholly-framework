package com.whollyframework.dbservice.sysconfig.service;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.whollyframework.annotation.ClassLog;
import com.whollyframework.base.dao.IDesignDAO;
import com.whollyframework.base.service.AbstractDesignService;
import com.whollyframework.dbservice.sysconfig.dao.SysConfigDAO;
import com.whollyframework.dbservice.sysconfig.model.SysParam;

@Service("sysConfigService")
@ClassLog(remark="系统参数设置")
public class SysConfigServiceImpl extends AbstractDesignService<SysParam,String> implements SysConfigService {
    
    @Resource(name = "sysConfigDAO")
    private SysConfigDAO sysConfigDao;
    
    
    public IDesignDAO<SysParam,String> getDAO() {
        return sysConfigDao;
    }


	@Override
	public List<String> getAllParamKey() throws SQLException  {
		
		return sysConfigDao.getAllParamKey();
	}


	@Override
	public SysParam findByParamKey(String paramKey) throws SQLException {
		
		return sysConfigDao.findByParamKey(paramKey);
	}

    
}
