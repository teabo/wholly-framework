package com.whollyframework.dbservice.version.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.whollyframework.annotation.ClassLog;
import com.whollyframework.base.dao.IDesignDAO;
import com.whollyframework.base.service.AbstractDesignService;
import com.whollyframework.dbservice.version.dao.VersionDAO;
import com.whollyframework.dbservice.version.model.Version;

@Service("versionService")
@ClassLog(remark="版本管理")
public class VersionServiceImpl extends AbstractDesignService<Version,String> implements VersionService {
    
    @Resource(name = "versionDAO")
    private VersionDAO versionDao;
    
    
    public IDesignDAO<Version,String> getDAO() {
        return versionDao;
    }
    
}
