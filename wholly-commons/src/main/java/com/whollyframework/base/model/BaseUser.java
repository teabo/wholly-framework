package com.whollyframework.base.model;

import com.whollyframework.authentications.IUser;
import com.whollyframework.authentications.IUserExtend;


/**
 * @author Chris Xu
 * @since 2011-4-29 下午04:00:21
 */
public abstract class BaseUser extends ValueObject implements IUser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3198932468310138486L;

	public static final int ADMIN_SUPER_TYPE=0x10000;
	
	public static final int ADMIN_UNITS_TYPE=0x01000;
	
	public static final int ADMIN_CONFIG_TYPE=0x00100;
	
	public static final int NORMAL_DOMAIN = 0x000000;

	public static final int UPGRADE_DOMAIN = 0x000001;

	public static final int ADVANCED_DOMAIN = 0x000010;

	public static final int VIP_DOMAIN = 0x000100;

	public static final int SUPER_DOMAIN = 0x001000;
	
	/**
	 * 激活
	 */
	public static final int STATUSACTIVATE = 1;
	/**
	 * 非激活
	 */
	public static final int STATUSNOTACTIVATE = 0;


	/**
	 * 用户姓名
	 */
	private String name;
	
	/**
	 * 用户性别
	 */
	private String sex;

	/**
	 * 用户登录名
	 */
	private String loginName;
	
	/**
	 * 用户登陆别名
	 */
	private String aliasName;

	/**
	 * 密码
	 */
	private String userPassword;
	
	/**
	 * 证件ID
	 */
	private String certId; 

	/**
	 * 上级用户对象
	 */
	private IUser superior;

	/**
	 * 用户等级
	 */
	protected int userLevel;
	/**
	 * 用户状态。 0:禁用；1:启用
	 */
	private int status; 
    
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getUserName() {
		return name;
	}

	public void setUserName(String name) {
		this.name = name;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getCertId() {
		return certId;
	}

	public void setCertId(String certId) {
		this.certId = certId;
	}

	public IUser getSuperior() {
		return superior;
	}

	public void setSuperior(IUser superior) {
		this.superior = superior;
	}

	public int getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(int userLevel) {
		this.userLevel = userLevel;
	}

	/**
	 * 用户状态。
	 * @return  0:禁用；1:启用
	 */
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isAdmin() {
		return userLevel >= 0x00000001;
	}
	
	public void setAdmin(boolean b) {
		if (b)
			this.setUserLevel(0x00000001);
		else
			this.setUserLevel(0);
	}

	public boolean isUnitAdmin() {
		return ADMIN_UNITS_TYPE==(userLevel&ADMIN_UNITS_TYPE);
	}
	
	public boolean isDeveloper() {
		return ADMIN_CONFIG_TYPE==(userLevel&ADMIN_CONFIG_TYPE);
	}

	public boolean isSuperAdmin() {
		return ADMIN_SUPER_TYPE==(userLevel&ADMIN_SUPER_TYPE);
	}
	
	public abstract IUserExtend getUserExtend();
	
	public abstract void setUserExtend(IUserExtend userExtend);
}
