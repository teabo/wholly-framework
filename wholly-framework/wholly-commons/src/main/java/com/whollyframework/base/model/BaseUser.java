package com.whollyframework.base.model;

import com.whollyframework.authentications.IUser;


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
	 * 性别
	 */
	private String sex;

	/**
	 * 移动电话
	 */
	private String telephone;

	/**
	 * 办公电话
	 */
	private String offerphone;
	
	/**
	 * 学历
	 */
	private String education;
	
	/**
	 * 相片路径
	 */
	private String photoUrl;

	/**
	 * 上级用户对象
	 */
	private IUser superior;

	/**
	 * Email地址
	 */
	private String email;

	/**
	 * 用户等级
	 */
	protected int userLevel;

	/**
	 * 职务
	 */
	private String station;
	
	/**状态  1.在职 2.离职**/
    private int state = 1;
    
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getOfferphone() {
		return offerphone;
	}

	public void setOfferphone(String offerphone) {
		this.offerphone = offerphone;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public IUser getSuperior() {
		return superior;
	}

	public void setSuperior(IUser superior) {
		this.superior = superior;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(int userLevel) {
		this.userLevel = userLevel;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
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
}
