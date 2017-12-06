package com.whollyframework.dbservice.user.service;



import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.whollyframework.annotation.ClassLog;
import com.whollyframework.authentications.IUser;
import com.whollyframework.authentications.IWebUser;
import com.whollyframework.base.dao.IDesignDAO;
import com.whollyframework.base.dao.support.SQLUtils;
import com.whollyframework.base.dao.support.criterion.Criterions;
import com.whollyframework.base.dao.support.criterion.Junction;
import com.whollyframework.base.model.ParamsTable;
import com.whollyframework.base.service.AbstractDesignService;
import com.whollyframework.dbservice.role.model.Role;
import com.whollyframework.dbservice.user.dao.UserDAO;
import com.whollyframework.dbservice.user.model.UserVO;
import com.whollyframework.util.Security;
import com.whollyframework.util.StringUtil;

@Service("userService")
@ClassLog(remark="用户管理")
public class UserServiceImpl extends AbstractDesignService<UserVO, String> implements UserService {
	@Resource(name = "userDAO")
	private UserDAO userDao;
	
    @Override
    public IDesignDAO<UserVO, String> getDAO() {
        return userDao;
    }

	@Override
	public String encrypt(String str) {
		return Security.encryptPassword(str);
	}

	@Override
	public String decrypt(String str) {
		return Security.decryptPassword(str);
	}

	@Override
	public void updateStatus(IUser user) throws SQLException {
		userDao.updateStatus(user);		
	}

	@Override
	public void doUpdateStatus(String[] ids,int state) throws Exception {
		for(String userId:ids){
			UserVO vo=find(userId);
			vo.setStatus(state);
			doUpdate(vo);
		}
	}

	@Override
	public List<Role> getUerRoles(String userId) throws SQLException {
		return userDao.getUerRoles(userId);
	}
	@Override
	public IUser login(String username, String userpwd) throws Exception {
		if(StringUtil.isBlank(username)||StringUtil.isBlank(userpwd)){
			return null;
		}
		ParamsTable params=new ParamsTable();
		SQLUtils sqlutils = new SQLUtils();
		sqlutils.createWhere(params);
		Junction or =Criterions.disjunction();
		or.add(Criterions.eq("loginName", username));
		or.add(Criterions.eq("alias_name", username));
		sqlutils.addCriterion(or);
		sqlutils.createOrderBy(params);
	    List <UserVO> users = getDAO().simpleQuery(sqlutils);
	    if(users.size()==0){
	    	return null;
	    }
		if (users!=null&&users.size()>0&&userpwd.equals(decrypt(users.get(0).getUserPassword()))) {
			return users.get(0);
		}
		return null;
	}
	
	
	@Override
	public List<UserVO> queryByRoleId(String filterField)
			throws Exception {
		return null;
	}

	@Override
	public IWebUser getWebUserInstance(String userid) throws Exception {
		return null;
	}

	@Override
	public void dochangePwd(String userId, String oldpwd, String newpwd)
			throws Exception {
	   UserVO user= find(userId);
	   if(!decrypt(user.getUserPassword()).equals(oldpwd)){
		   throw new Exception("用户密码不正确！");
	   }
	   user.setUserPassword(encrypt(newpwd));
	   doUpdate(user);
	}
    /**
     * 是否存在相同登录名用户
     * @param user
     * @return
     */
	public boolean hasSameLoignName(UserVO user)  {
		try {
			ParamsTable param = new ParamsTable();
			param.setParameter("s_loginname", user.getLoginName());
			List<UserVO> users;
			users = simpleQuery(param);
			return users.size() > 0
					&& !users.get(0).getId().equals(user.getId());
		} catch (Exception e) {
		}
		return false;
	}

	@Override
	public void resetPassword(String[] _selects, String defualtPassword) throws Exception {
		for(String userId:_selects){
			UserVO user= find(userId);
			user.setUserPassword(encrypt(defualtPassword));
			doUpdate(user);
		}
	}
	
	public static void main(String[] args) {
		System.out.println(Security.encryptPassword("123"));
	}
}
