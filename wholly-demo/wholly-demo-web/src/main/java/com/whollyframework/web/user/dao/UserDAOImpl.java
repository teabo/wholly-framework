package com.whollyframework.web.user.dao;

import org.springframework.stereotype.Repository;

import com.whollyframework.base.dao.hibernate.HibernateBaseDAO;
import com.whollyframework.web.user.model.UserVO;

@Repository("userDAO")
public class UserDAOImpl extends HibernateBaseDAO<UserVO, String> implements IUserDAO {


}
