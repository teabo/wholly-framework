package com.whollyframework.web.system;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.whollyframework.base.action.BaseController;
import com.whollyframework.base.service.IDesignService;
import com.whollyframework.dbservice.org.model.Organization;
import com.whollyframework.dbservice.org.service.OrgService;
import com.whollyframework.util.StringUtil;
import com.whollyframework.util.json.JsonUtil;
import com.whollyframework.utils.http.ResponseUtil;

/**
 * 部门管理模块
 * 
 * @author tongzhiw
 * 
 *         2014-12-23
 */
@Controller
@RequestMapping(value = "/control/system/org")
@Scope("prototype")
public class OrgController extends BaseController<Organization, String> {

	private static final long serialVersionUID = 1L;

	/**
	 * 日志
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(OrgController.class);

	@Autowired
	OrgService orgService;

	@Override
	public IDesignService<Organization, String> getService() {
		return orgService;
	}

	public OrgController() {
		super(new Organization());
	}

	@Override
	protected String create() {
		setContent(new Organization());
		return super.create();
	}

	@RequestMapping({ "/index" })
	public String index(HttpServletRequest request, HttpServletResponse response) {
		setRequest(request);
		setResponse(response);
		return forward("list");
	}

	@Override
	@RequestMapping(value = "deleteAjax")
	public void deleteAjax(String[] _selects, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			setRequest(request);
			setResponse(response);
			super.deleteAjax(_selects, request, response);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}

	@RequestMapping(value = "/saveAndNew")
	public String saveAndNew(Organization vo, HttpServletRequest request,
			HttpServletResponse response) {
		setRequest(request);
		setResponse(response);
		setContent(vo);
		try {
			if (StringUtil.isBlank(vo.getId()))
				getService().doCreate(vo);
			else
				getService().doUpdate(vo);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		setContent(new Organization());
		return super.create();
	}

	/**
	 * 获取有层次结构的部门列表
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getAllORG")
	public void getAllORG(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			setRequest(request);
			setResponse(response);
			String unitId = request.getParameter("unitId");
			Map<String, String> menuMap = orgService.getAllOrgMap(unitId);
			ResponseUtil.setJsonToResponse(response, JsonUtil.toJson(menuMap));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}

	/**
	 * 获取部门列表
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getOrgListAjax")
	public void getOrgListAjax(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			setRequest(request);
			setResponse(response);
			List<Organization> orgList = orgService.getAll();
			ResponseUtil.setJsonToResponse(response, JsonUtil.toJson(orgList));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}

	@RequestMapping(value = "/getOrgNameAjax/{id}")
	public void getOrgNameAjax(HttpServletRequest request,
			@PathVariable String id, HttpServletResponse response) {
		try {
			setRequest(request);
			setResponse(response);
			Organization org = orgService.find(id);
			String valueName = "";
			if (org != null) {
				valueName = org.getName();
			}
			ResponseUtil
					.setJsonToResponse(response, JsonUtil.toJson(valueName));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}

}
