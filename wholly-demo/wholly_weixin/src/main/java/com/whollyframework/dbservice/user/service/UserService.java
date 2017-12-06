package com.whollyframework.dbservice.user.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Component;

import com.whollyframework.authentications.IUser;
import com.whollyframework.authentications.service.IUserService;
import com.whollyframework.base.service.IDesignService;
import com.whollyframework.dbservice.role.model.Role;
import com.whollyframework.dbservice.user.model.UserVO;
/**
 * 系统管理--用户管理
 * @author WangWenGuang
 * @2014-12-23
 */
@Component
public interface UserService extends IDesignService<UserVO, String>,
		IUserService<UserVO, String>, EncryAndDecryService {
	/**
	 * 更新用户状态
	 * @param user
	 * @throws SQLException
	 */
	public void updateStatus(IUser user) throws SQLException;
	/**
	 * 批量更新用户状态
	 * @param user
	 * @throws SQLException
	 */
	public void doUpdateStatus(String id[], int state) throws Exception;
    /**
     * 获取用户角色列表
     * @param userId
     * @return
     * @throws SQLException
     */
	public List<Role> getUerRoles(String userId) throws SQLException;
	/**
	 * 用户登录
	 * @param username
	 * @param userpwd
	 * @return
	 * @throws Exception
	 */
	IUser login(String username, String userpwd) throws Exception;
	/**
	 * 修改用户密码
	 * @param userId
	 * @param oldpwd
	 * @param newpwd
	 * @throws Exception
	 */
	public void dochangePwd(String userId,String oldpwd,String newpwd)throws Exception;
	/**
	 * 是否存在相同登录名
	 * @param user
	 * @return
	 */
	public boolean hasSameLoignName(UserVO user);
	/**
	 * 重置密码
	 * @param _selects
	 * @param defualtPassword
	 * @throws Exception
	 */
	public void resetPassword(String[] _selects, String defualtPassword)throws Exception;
}
