package com.whollyframework.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.whollyframework.authentications.IWebUser;
import com.whollyframework.dbservice.menu.model.Menu;
import com.whollyframework.dbservice.menu.service.MenuService;

@Controller
@Scope("prototype")
@RequestMapping("/control")
public class PortalController {
	@Autowired
	private MenuService menuService;

	@RequestMapping("/index")
	public String index(HttpSession session, HttpServletRequest request)
			throws Exception {
		IWebUser user = getUser(session);
		if (user == null) {
			return "/security/login";
		}
		String userId = user.getId();
		List<Menu> menus = menuService.getTopMenu(userId);
		request.setAttribute("user", user);
		request.setAttribute("topmenus", menus);
		return "/control/index";
	}

	@RequestMapping("/module")
	public String module(String pid, HttpSession session,
			HttpServletRequest request) throws Exception {
		IWebUser user = getUser(session);
		if (user == null) {
			return "/security/login";
		}
		String userId = user.getId();
		List<Menu> menus = menuService.getSubMenuByPid(userId, pid);
		request.setAttribute("menus", menus);
		return "/control/module";
	}

	public IWebUser getUser(HttpSession session) {
		Object obj = session.getAttribute("USER");
		if (obj == null) {
			return null;
		}
		return (IWebUser) obj;
	}
}
