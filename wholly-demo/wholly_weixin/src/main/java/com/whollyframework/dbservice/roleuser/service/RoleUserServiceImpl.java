package com.whollyframework.dbservice.roleuser.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.whollyframework.annotation.ClassLog;
import com.whollyframework.base.dao.IDesignDAO;
import com.whollyframework.base.service.AbstractDesignService;
import com.whollyframework.dbservice.roleuser.dao.RoleUserDAO;
import com.whollyframework.dbservice.roleuser.model.RoleUser;

@Service("roleUserService")
@ClassLog(remark="用户授权")
public class RoleUserServiceImpl extends AbstractDesignService<RoleUser,String> implements RoleUserService {
    
    @Resource(name = "roleUserDAO")
    private RoleUserDAO roleUserDao;
    
    
    public IDesignDAO<RoleUser,String> getDAO() {
        return roleUserDao;
    }
    
}
