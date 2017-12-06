package com.whollyframework.web.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.whollyframework.base.action.BaseController;
import com.whollyframework.base.service.IDesignService;
import com.whollyframework.dbservice.operatelog.model.OperateLog;
import com.whollyframework.dbservice.operatelog.service.OperateLogService;

/**
 * 操作日志Action
 * 
 * @author pwh
 * @since 2011-10-9
 * 
 */
@Controller
@Scope("prototype")
@RequestMapping(value = "/control/system/operlog")
public class OperlogController extends BaseController<OperateLog, String> {

	public OperlogController() {
		super(new OperateLog());
	}

	private static final long serialVersionUID = 1L;

	@Autowired
	private OperateLogService operlogService;

	public void setOperlogService(OperateLogService operlogService) {
		this.operlogService = operlogService;
	}

	public IDesignService<OperateLog, String> getService() {
		return operlogService;
	}

	public String create() {
		setContent(new OperateLog());
		return forward("content.jsp");
	}

	public String getPtitle() {
		return "操作日志";
	}

	@RequestMapping({"/index"})
	public String index() {
		
		return "/control/system/operlog/list";
	}
}
