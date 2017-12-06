package com.whollyframework.dbservice.user.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.whollyframework.authentications.IGroup;
import com.whollyframework.authentications.IOrganization;
import com.whollyframework.authentications.IPermission;
import com.whollyframework.authentications.IRole;
import com.whollyframework.authentications.IUserExtend;
import com.whollyframework.base.model.BaseUser;
import com.whollyframework.base.model.ParamsTable;
import com.whollyframework.dbservice.org.model.Organization;
import com.whollyframework.dbservice.org.service.OrgService;
import com.whollyframework.dbservice.role.model.Role;
import com.whollyframework.dbservice.user.service.UserServiceImpl;
import com.whollyframework.util.StringUtil;
import com.whollyframework.utils.ServicesFactory;

/**
 * 系统管理--用户管理模块
 * 
 * @author WangWenGuang
 * @2014-12-23
 */
public class UserVO extends BaseUser {
	private static final Logger log = LoggerFactory.getLogger(UserVO.class);
    public static final  String HAS_BIND_IP="1";//用户绑定IP标志;
	private static final long serialVersionUID = 4542702158032151669L;

	private List<? extends IRole> roles; // 角色

	private List<? extends IGroup> groups; // 角色组

	private List<? extends IOrganization> organizations; // 部门

	private List<? extends IPermission> permissions; // 权限
	private String newpassword;

	private String roleIds; // 用户的所有角色id

	private String unit_name; // 单位名称

	private int userType; // 默认的用户类型0; 用户类型; 0本地用户; 1漫游用户

	private String unitId;

	private String photourl;

	private String certType;// 00 身份证 ; 01 警官证 ; 03 军官证

	private String policeNo;// 警号

	private String hasBindIp;// 是否启用IP绑定

	private String bindIps;
	
	private IUserExtend userExtend;
    
	private int userState;//只是为了做兼容导出用
	public String getBindIps() {
		return bindIps;
	}

	public void setBindIps(String bindIps) {
		this.bindIps = bindIps;
	}

	public String getHasBindIp() {
		return hasBindIp;
	}

	public void setHasBindIp(String hasBindIp) {
		this.hasBindIp = hasBindIp;
	}

	public String getPoliceNo() {
		return policeNo;
	}

	public void setPoliceNo(String policeNo) {
		this.policeNo = policeNo;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 * 备注
	 */
	private String remarks;

	/**
	 * 
	 */

	public String getPhotourl() {
		return photourl;
	}

	public void setPhotourl(String photourl) {
		this.photourl = photourl;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getRemarks() {
		return remarks;
	}
   /**
    * 解密后的用户密码
    * @return
    */
	public String getRealPassword() {
		String passWord = getUserPassword();
		if (StringUtil.isBlank(passWord)) {
			return "";
		} else {
			UserServiceImpl service = (UserServiceImpl) ServicesFactory
					.getService("userService");
			return service.decrypt(passWord);
		}
	}

	public void setNewpassword(String newpassword) {
		this.newpassword = newpassword;
	}
	public String getNewpassword(){
		return this.newpassword;
	}

	public String getUnit_name() {
		return unit_name;
	}

	public void setUnit_name(String unit_name) {
		this.unit_name = unit_name;
	}

	public String getRoleIds() {

		return roleIds;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public void setRoles(List<? extends IRole> roles) {
		this.roles = roles;
	}

	public List<? extends IGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<? extends IGroup> groups) {
		this.groups = groups;
	}

	@JsonIgnore
	public List<? extends IPermission> getPermissions() {
		if (permissions == null) {
			permissions = new ArrayList<IPermission>();
		}
		return permissions;
	}

	public void setPermissions(List<? extends IPermission> permissions) {
		this.permissions = permissions;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = StringUtil.join(roleIds.split(","), ";");
	}

	public void setOrganizations(List<? extends IOrganization> departments) {
		this.organizations = departments;
	}

	@JsonIgnore
	public List<? extends IOrganization> getOrganizations() {
		if (StringUtil.isBlank(getOrgId())) {
			return new ArrayList<Organization>();
		}
		if (organizations == null) {
			OrgService service = (OrgService) ServicesFactory
					.getService("orgService");
			try {
				ParamsTable params = new ParamsTable();
				params.setParameter("s_id", getOrgId());
				return (List<? extends IOrganization>) service
						.simpleQuery(params);
			} catch (Exception e) {
				log.error("查找机构失败！", e);
			}
		}
		return new ArrayList<Organization>();
	}

	@JsonIgnore
	public List<? extends IRole> getRoles() {

		if (StringUtil.isBlank(getId())) {
			return new ArrayList<Role>();
		}
		if (roles == null) {
			UserServiceImpl service = (UserServiceImpl) ServicesFactory
					.getService("userService");
			try {
				return service.getUerRoles(getId());
			} catch (Exception e) {
				log.error("查找角色失败！", e);
			}
		}
		return new ArrayList<Role>();
	}

	public String getCertType() {
		return certType;
	}

	public void setCertType(String certType) {
		this.certType = certType;
	}

	public int getUserState() {
		return getStatus();
	}

	@Override
	public IUserExtend getUserExtend() {
		return userExtend;
	}

	@Override
	public void setUserExtend(IUserExtend userExtend) {
		this.userExtend = userExtend;
	}

	public void setSuperAdmin(boolean b) {
		setUserLevel(ADMIN_SUPER_TYPE);
	}

    
}
