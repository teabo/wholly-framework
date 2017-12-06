package com.whollyframework.web.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.whollyframework.base.action.BaseController;
import com.whollyframework.base.model.DataPackage;
import com.whollyframework.base.model.ParamsTable;
import com.whollyframework.base.model.ValueObject;
import com.whollyframework.base.service.IDesignService;
import com.whollyframework.dbservice.org.model.Organization;
import com.whollyframework.dbservice.org.service.OrgService;
import com.whollyframework.dbservice.role.model.Role;
import com.whollyframework.dbservice.role.service.RoleService;
import com.whollyframework.dbservice.roleuser.model.RoleUser;
import com.whollyframework.dbservice.roleuser.service.RoleUserService;
import com.whollyframework.dbservice.user.model.UserVO;
import com.whollyframework.dbservice.user.service.EncryAndDecryService;
import com.whollyframework.dbservice.user.service.UserService;
import com.whollyframework.dbservice.userbindip.model.BindIp;
import com.whollyframework.dbservice.userbindip.service.BindIpService;
import com.whollyframework.util.StringUtil;
import com.whollyframework.util.json.JsonUtil;
import com.whollyframework.util.property.DefaultProperty;
import com.whollyframework.util.sequence.Sequence;
import com.whollyframework.utils.http.ResponseUtil;

/**
 * 系统管理--用户管理
 * 
 * @author wenguang
 * 
 */
@Controller
@RequestMapping(value = "/control/system/user")
@Scope("prototype")
public class UserController extends BaseController<UserVO, String> {
	private static final long serialVersionUID = -2821959082135068931L;
	private static final Logger log = LoggerFactory
			.getLogger(UserController.class);

	public UserController() {
		super(new UserVO());
	}

	@Autowired
	UserService iUserService;
	@Autowired
	OrgService orgService;
	@Autowired
	RoleUserService roleUserService;

	@Autowired
	RoleService roleService;

	@Resource(name = "bindIpService")
	BindIpService bindIpService;

	@Override
	public IDesignService<UserVO, String> getService() {
		return iUserService;
	}

	@RequestMapping({ "/index" })
	public String index(HttpServletRequest request, HttpServletResponse response) {
		setRequest(request);
		setResponse(response);
		return forward("list");
	}

	/**
	 * 停用
	 * 
	 * @param _selects
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "dolock")
	@ResponseBody
	public Map<String, String> dolock(String[] _selects) {
		Map<String, String> rtn = new HashMap<String, String>();
		try {
			if (_selects != null)
				iUserService.doUpdateStatus(_selects, 0);
			rtn.put("code", "1");
			rtn.put("msg", "停用用户成功");
		} catch (Exception e) {
			rtn.put("code", "0");
			if (e.getMessage().indexOf("Could not execute JDBC batch update") > -1)
				rtn.put("error", "停用用户失败");
			else
				rtn.put("error", e.getMessage());
			log.error("停用用户失败：", e);
		}
		return rtn;
	}

	/**
	 * 启用
	 * 
	 * @param _selects
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "unlock")
	@ResponseBody
	public Map<String, String> unlock(String[] _selects) {
		Map<String, String> rtn = new HashMap<String, String>();
		try {
			if (_selects != null)
				iUserService.doUpdateStatus(_selects, 1);
			rtn.put("code", "1");
			rtn.put("msg", "启用用户成功");
		} catch (Exception e) {
			rtn.put("code", "0");
			if (e.getMessage().indexOf("Could not execute JDBC batch update") > -1)
				rtn.put("error", "启用用户失败");
			else
				rtn.put("error", e.getMessage());
			log.error("启用用户失败：", e);
		}
		return rtn;
	}

	/**
	 * 启用
	 * 
	 * @param _selects
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "resetPassword")
	@ResponseBody
	public Map<String, String> resetPassword(String[] _selects) {
		Map<String, String> rtn = new HashMap<String, String>();
		try {
			String defualtPassword = DefaultProperty
					.getProperty("ygframework.user.reset.password");
			if (_selects != null)
				iUserService.resetPassword(_selects, defualtPassword);
			rtn.put("code", "1");
			rtn.put("msg", "操作成功，重置密码为:"+defualtPassword);
		} catch (Exception e) {
			rtn.put("code", "0");
			if (e.getMessage().indexOf("Could not execute JDBC batch update") > -1)
				rtn.put("error", "启用用户失败");
			else
				rtn.put("error", e.getMessage());
			log.error("启用用户失败：", e);
		}
		return rtn;
	}

	/**
	 * 进入密码 修改页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "userPwd")
	public String userPwd() {
		try {
			if (getUser() != null) {
				this.getContent().setId(getUser().getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.addFieldError("1", e.getMessage());
		}
		return "/control/system/user/updatepassword";
	}

	/**
	 * 修改密码
	 * 
	 * @param response
	 * @param userId
	 * @param oldpwd
	 * @param newpwd2
	 */
	@RequestMapping(value = "changePassword")
	@ResponseBody
	public Map<String, String> changePassword(String userId, String oldpwd,
			String newpwd2) {
		Map<String, String> rtn = new HashMap<String, String>();
		try {
			iUserService.dochangePwd(userId, oldpwd, newpwd2);
			rtn.put("code", "0");
			rtn.put("msg", "修改成功，下次登录生效");
		} catch (Exception e) {
			log.error("修改密码失败", e);
			rtn.put("code", "1");
			rtn.put("msg", "修改失败：" + e.getMessage());
		}
		return rtn;
	}

	/**
	 * 部门下拉框
	 * 
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "orgSelect")
	@ResponseBody
	public List<Organization> nodeSelect() throws Exception {
		return orgService.getAll();
	}

	public boolean preAction() {

		if (getCurrentPath().indexOf("save") > -1) {
			UserVO user = getContent();
			if (iUserService.hasSameLoignName(user)) {
				throw new RuntimeException("存在相同用户名！");
			}
			String _password = user.getNewpassword();
			if (StringUtil.isNotBlank(_password)) {
				user.setUserPassword(((EncryAndDecryService) getService())
						.encrypt(_password));
			}

		}
		return true;
	}

	/**
	 * 删除
	 * 
	 * @param _selects
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "deleteAjax")
	@Override
	public void deleteAjax(String[] _selects, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, String> rtn = new HashMap<String, String>();
		try {
			if (_selects != null)
				getService().doRemove(_selects);
			rtn.put("code", "1");
			rtn.put("msg", "删除失败");
		} catch (Exception e) {
			rtn.put("code", "0");
			if (e.getMessage().indexOf("Could not execute JDBC batch update") > -1)
				rtn.put("error", "删除失败");
			else
				rtn.put("error", e.getMessage());
			log.error("删除失败：", e);
		}
		ResponseUtil.setJsonToResponse(response, JsonUtil.toJson(rtn));
		return;
	}

	/**
	 * 保存并新建
	 * 
	 * @param UserVO
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "saveAndNew")
	public String saveAndNew(UserVO vo, HttpServletRequest request,
			HttpServletResponse response) {
		setRequest(request);
		setResponse(response);
		setContent(vo);
		try {

			try {
				preAction();
			} catch (Exception e) {
				request.setAttribute("errorMsg", e.getMessage());
				request.setAttribute("content", vo);
				return forward("content");
			}
			if (StringUtil.isBlank(vo.getId()))
				getService().doCreate(vo);
			else
				getService().doUpdate(vo);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		setContent(new UserVO());
		return forward("content");
	}

	@RequestMapping({ "/userRoleList" })
	public void jsonUserRoleList(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			// this.datas = getService().query(getParams());
			setRequest(request);
			setResponse(response);

			ParamsTable params = getParams();
			String type = params.getParameterAsString("type");// 1 已加入用户 2 未加入用户
			String userId = params.getParameterAsString("userId");// 角色id

			DataPackage<Role> roleData = new DataPackage<Role>();

			if (!StringUtil.isBlank(userId)) {// 编辑进入
				if ("1".equals(type)) {
					params.setParameter(
							"$SYS_USER_ROLE_SET$[ID==ROLE_ID]S_USER_ID", userId);
					roleData = roleService.query(params);
				} else if ("2".equals(type)) {// 为加入的用户
					//
					// ParamsTable params1 = new ParamsTable();
					params.setParameter(
							"$SYS_USER_ROLE_SET$[ID!=ROLE_ID]S_USER_ID", userId);
					roleData = roleService.query(params);
				}
			}
			ResponseUtil.setJsonToResponse(getResponse(), roleData.toJSON());

		} catch (Exception e) {
			Map<String, String> rtn = new HashMap<String, String>();
			rtn.put("code", "0");
			rtn.put("error", "查询参数异常：" + e.getMessage());
			ResponseUtil.setJsonToResponse(getResponse(), JsonUtil.toJson(rtn));
		}
	}

	/**
	 * 跳转
	 * 
	 * @return
	 */
	@RequestMapping({ "userRole" })
	public String userRole() {
		return "/control/system/user/user_role_list";
	}

	@RequestMapping({ "doBind" })
	@ResponseBody
	public Map<String, String> doBind(String[] _selects, String userId)
			throws Exception {
		for (String roleId : _selects) {
			RoleUser roleUser = new RoleUser();
			roleUser.setUserId(userId);
			roleUser.setRoleId(roleId);
			roleUserService.doCreate(roleUser);
		}
		Map<String, String> rtn = new HashMap<String, String>();
		rtn.put("code", "0");
		rtn.put("msg", "操作成功！");
		return rtn;
	}

	@RequestMapping({ "unBind" })
	@ResponseBody
	public Map<String, String> unBind(String[] _selects, String userId)
			throws Exception {
		for (String roleId : _selects) {
			ParamsTable params = new ParamsTable();
			params.setParameter("s_user_id", userId);
			params.setParameter("s_role_id", roleId);
			List<RoleUser> lis = (List<RoleUser>) roleUserService
					.simpleQuery(params);
			if (lis.size() > 0) {
				roleUserService.doRemove(lis.get(0).getId());
			}
		}
		Map<String, String> rtn = new HashMap<String, String>();
		rtn.put("code", "0");
		rtn.put("msg", "操作成功！");
		return rtn;
	}

	@RequestMapping({ "bindIpList" })
	public void jsonBindIpList(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			setRequest(request);
			setResponse(response);
			ParamsTable params = getParams();
			String userId = params.getParameterAsString("s_user_id");// 角色id
			DataPackage<BindIp> rtnDate = new DataPackage<BindIp>();
			if (!StringUtil.isBlank(userId)) {// 编辑进入
				rtnDate = bindIpService.query(params);
			}
			ResponseUtil.setJsonToResponse(getResponse(), rtnDate.toJSON());
		} catch (Exception e) {
			Map<String, String> rtn = new HashMap<String, String>();
			rtn.put("code", "0");
			rtn.put("error", "查询参数异常：" + e.getMessage());
			ResponseUtil.setJsonToResponse(getResponse(), JsonUtil.toJson(rtn));
		}
	}

	@RequestMapping(value = "saveUser")
	protected String saveUser(UserVO content, HttpServletRequest request,
			HttpServletResponse response) {
		setRequest(request);
		setResponse(response);
		setContent(content);
		boolean isNew = ((ValueObject) getContent()).getIstemp() == 1;
		try {
			
			try {
				preAction();
			} catch (Exception e) {
				request.setAttribute("errorMsg", e.getMessage());
				request.setAttribute("content",content);
				return forward("content");
			}
			setPublicVariables();
			ParamsTable params = getParams();
			String insertIps = params.getParameterAsString("insertIps");
			String updateIps = params.getParameterAsString("updateIps");
			String deleteIps = params.getParameterAsString("deleteIps");
			if (isNew) {
				content.setId(Sequence.getSequence());
				getService().doCreate(content);
				if (!StringUtil.isBlank(insertIps)) {
					bindIpService.saveBindIps(insertIps, content.getId());
				}
				if (!StringUtil.isBlank(updateIps)) {
					bindIpService.updateBindIps(updateIps);
				}
				if (!StringUtil.isBlank(deleteIps)) {
					bindIpService.deleteBindIps(deleteIps);
				}
			} else {
				getService().doUpdate(content);
				if (!StringUtil.isBlank(insertIps)) {
					bindIpService.saveBindIps(insertIps, content.getId());
				}
				if (!StringUtil.isBlank(updateIps)) {
					bindIpService.updateBindIps(updateIps);
				}
				if (!StringUtil.isBlank(deleteIps)) {
					bindIpService.deleteBindIps(deleteIps);
				}
			}
			addActionMessage("保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
			addFieldError("1", e.getMessage());
			if (isNew)
				((ValueObject) getContent()).setIstemp(1);
			return forward("content");
		}
		return redirect("list.action");
	}

}
