package com.whollyframework.web.login.log;

import com.whollyframework.authentications.IOrganization;
import com.whollyframework.authentications.IUser;
import com.whollyframework.dbservice.operatelog.service.OperateLogService;

public class LoginLog {

	private OperateLogService operlogService;
	
	private IOrganization org = null;
	private IUser user;

	/** 客户端操作系统 */
	private String agentsystem;

	/** 客户端浏览器 */
	private String agentexplorer;
	
	/**请求的action的路径*/
	private String operPath;

	
	/** 客户端IP */
	private String agentip;

	/** 客户端电脑名 */
	private String agenthostname;
	
	public IOrganization getOrg() {
		return org;
	}
	public void setOrg(IOrganization org) {
		this.org = org;
	}
	public IUser getUser() {
		return user;
	}
	public void setUser(IUser user) {
		this.user = user;
	}
	public String getAgentsystem() {
		return agentsystem;
	}
	public void setAgentsystem(String agentsystem) {
		this.agentsystem = agentsystem;
	}
	public String getAgentexplorer() {
		return agentexplorer;
	}
	public void setAgentexplorer(String agentexplorer) {
		this.agentexplorer = agentexplorer;
	}
	public String getAgentip() {
		return agentip;
	}
	public void setAgentip(String agentip) {
		this.agentip = agentip;
	}
	public String getAgenthostname() {
		return agenthostname;
	}
	public void setAgenthostname(String agenthostname) {
		this.agenthostname = agenthostname;
	}
	
	public String getOperPath() {
		return operPath;
	}
	public void setOperPath(String operPath) {
		this.operPath = operPath;
	}
	
	public OperateLogService getOperlogService() {
		return operlogService;
	}
	public void setOperlogService(OperateLogService operlogService) {
		this.operlogService = operlogService;
	}
}
