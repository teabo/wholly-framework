package com.whollyframework.web.login;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.whollyframework.base.action.BaseController;
import com.whollyframework.base.model.WebUser;
import com.whollyframework.base.service.IDesignService;
import com.whollyframework.base.web.security.OnlineUserBindingListener;
import com.whollyframework.constans.Web;
import com.whollyframework.util.json.JsonUtil;
import com.whollyframework.utils.client.ClientInfo;
import com.whollyframework.utils.http.ResponseUtil;
import com.whollyframework.web.login.log.LoginLog;
import com.whollyframework.web.login.log.LoginLogQueue;
import com.whollyframework.dbservice.operatelog.service.OperateLogService;
import com.whollyframework.dbservice.org.service.OrgService;
import com.whollyframework.dbservice.user.model.UserVO;
import com.whollyframework.dbservice.user.service.UserService;
import com.whollyframework.dbservice.userbindip.model.BindIp;
import com.whollyframework.dbservice.userbindip.service.BindIpService;

@Controller
@RequestMapping(value = "/admin")
@Scope("prototype")
public class LoginController extends BaseController<UserVO, String> {
	private static final long serialVersionUID = -2821959082135068931L;
	private static final Logger log = LoggerFactory.getLogger(LoginController.class);

	public LoginController() {
		super(new UserVO());
	}

	@Autowired
	UserService iUserService;
	@Autowired
	OrgService orgService;

	@Autowired
	private OperateLogService operlogService;

	@Resource(name = "bindIpService")
	BindIpService bindIpService;

	@Override
	public IDesignService<UserVO, String> getService() {
		return iUserService;
	}

	@RequestMapping({ "/login" })
	public void login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
		log.info("用户{" + username + "}");
		Map<String, Object> result = new HashMap<String, Object>();
		// HttpServletResponse response = getResponse();
		// sHttpServletRequest request = getRequest();
		try {
			HttpSession session = request.getSession();
			String code = (String) session.getAttribute(Web.SESSION_ATTRIBUTE_CHECKCODE);
			String checkcode = request.getParameter("authImg");
			if (checkcode == null || checkcode.trim().length() <= 0) {
				result.put("code", -1);
				result.put("message", "请输入图片显示的4个字符,不分大小写!");
				ResponseUtil.setJsonToResponse(response, JsonUtil.toJson(result));
				return;
			} else if (!checkcode.equalsIgnoreCase(code)) {
				result.put("code", -1);
				result.put("message", "输入字符错误,请重新输入图片显示的4个字符!");
				ResponseUtil.setJsonToResponse(response, JsonUtil.toJson(result));
				return;
			}

			UserVO user = (UserVO) iUserService.login(username, password);
			if (user == null) {
				throw new Exception("用户名或密码错误！");
			}
			if (UserVO.HAS_BIND_IP.equals(user.getHasBindIp())) {
				List<BindIp> bindLis = bindIpService.findUserBindIps(user.getId());
				boolean isInnerIp = false;
				try {
					for (BindIp bindIp : bindLis) {
						ClientInfo clientinfo = new ClientInfo(request, request.getHeader("User-Agent"));
						if (bindIp.isInnserIp(clientinfo.getRemoteIP())) {
							isInnerIp = true;
							break;
						}
					}
				} catch (Exception e) {
					throw new Exception("用户不在绑定IP范围内！");
				}
				if (!isInnerIp) {
					throw new Exception("用户不在绑定IP范围内！");
				}
			}
			if (user.getStatus() == 1) {
				WebUser webUser = new WebUser(user);
				webUser.setSex(user.getSex());// 用户的性别
				setActionContext(operlogService, webUser, request, response, null);
			} else {
				throw new Exception("登录失效，请与管理员联系！");
			}
			result.put("code", 0);
			result.put("message", "登录成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", 1);
			result.put("message", e.getMessage());

		}
		ResponseUtil.setJsonToResponse(response, JsonUtil.toJson(result));

	}

	public void setActionContext(OperateLogService operlogService, WebUser webUser, HttpServletRequest request,
			HttpServletResponse response, Cookie pwdErrorTimes) {
		HttpSession session = request.getSession();
		session.setAttribute(Web.SESSION_ATTRIBUTE_USER, webUser);
		session.setAttribute(Web.SESSION_ATTRIBUTE_ONLINEUSER, new OnlineUserBindingListener(webUser));

		LoginLog log = new LoginLog();
		log.setOperlogService(operlogService);
		log.setOrg(null);
		log.setUser(webUser);

		try {
			ClientInfo clientinfo = new ClientInfo(request, request.getHeader("User-Agent"));
			String requestURL = request.getRequestURI().replace(request.getContextPath(), "");

			log.setAgentexplorer(clientinfo.getAgentExplorer());// 客户端浏览器
			log.setAgentsystem(clientinfo.getAgentOSystem());// 操作系统
			log.setAgentip(clientinfo.getRemoteIP());// 客户端ip
			log.setAgenthostname(clientinfo.getRemoteHost());// 客户端电脑名
			log.setOperPath(requestURL);
		} catch (Exception e) {
			e.printStackTrace();
		}

		LoginLogQueue.addQueue(log);
	}

}
