package com.whollyframework.web.user.service;

import org.springframework.stereotype.Component;

import com.whollyframework.base.dao.IDesignDAO;
import com.whollyframework.base.service.hibernate.HibernateDesignService;
import com.whollyframework.web.user.model.UserVO;

@Component
public class UserService extends HibernateDesignService<UserVO, String> implements IUserService {

    @Override
    public IDesignDAO<UserVO, String> getDAO() {
        // TODO Auto-generated method stub
        return null;
    }

}
