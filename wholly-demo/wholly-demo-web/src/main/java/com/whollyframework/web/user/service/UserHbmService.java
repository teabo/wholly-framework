package com.whollyframework.web.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.whollyframework.base.dao.IDesignDAO;
import com.whollyframework.base.service.HibernateCurdService;
import com.whollyframework.web.user.dao.IUserDAO;
import com.whollyframework.web.user.model.UserVO;

@Component
public class UserHbmService extends HibernateCurdService<UserVO, String> implements IUserService {

	@Autowired
	private IUserDAO userDAO;
	
    public IDesignDAO<UserVO, String> getDAO() {
        return userDAO;
    }

}
