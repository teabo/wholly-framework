package com.whollyframework.authentications;

import java.util.List;

/**
 * @author Chris Xu
 * @since 2011-4-29 上午10:31:01
 */
public interface IUser extends IValueObject {

	/**
	 * 查询权限
	 */
	public final static int OPER_QUERY = 0;

	/**
	 * 查询与新增权限
	 */
	public final static int OPER_CREATE = 1;
	
	/**
	 * 查询与修改权限
	 */
	public final static int OPER_UPDATE = 2;
	
	/**
	 * 查询、修改与新增权限
	 */
	public final static int OPER_CREATE_UPDATE = 3;
	
	/**
	 * 查询与删除权限
	 */
	public final static int OPER_DELETE = 4;
	
	/**
	 * 查询、新增与删除权限
	 */
	public final static int OPER_CREATE_DELETE = 5;
	
	/**
	 * 查询、修改与删除权限
	 */
	public final static int OPER_UPDATE_DELETE = 6;
	
	/**
	 * 查询、新增、修改与删除权限
	 */
	public final static int OPER_ALL = 7;

	String getName();
	/**
	 * 获取登录账号
	 * @return 登录账号
	 */
	String getLoginName();

	/**
	 * 获取用户上级对象
	 * @return 用户上级对象
	 */
	IUser getSuperior();
	
	/**
	 * 获取用户所属角色
	 * @return 用户所属角色集合
	 */
	List<? extends IRole> getRoles();
	
	/**
	 * 获取用户所属级别
	 * @return 用户所属级别集合
	 */
	List<? extends IGroup> getGroups();
	
	/**
	 * 获取用户拥有的权限
	 * @return 用户拥有的权限集合
	 */
	List<? extends IPermission> getPermissions();
	
	/**
	 * 获取用户所属部门
	 * @return 用户所属部门集合
	 */
	List<? extends IOrganization> getOrganizations();
	
	/**
	 * 是否为管理员用户
	 * @return true|false：是否为后台管理用户
	 */
	boolean isAdmin();
	
	/**
	 * 职务
	 * @return
	 */
	String getStation();
	
	/**
	 * 身份证ID
	 * @return
	 */
	String getCertId();
}
