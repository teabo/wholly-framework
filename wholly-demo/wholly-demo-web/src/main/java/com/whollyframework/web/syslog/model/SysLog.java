package com.whollyframework.web.syslog.model;

import java.util.Date;

import com.whollyframework.base.model.ValueObject;
import com.whollyframework.util.DateUtil;
/**
 * 操作日志
 * @author Chris Hsu
 */
public class SysLog extends ValueObject{
   
	private static final long serialVersionUID = 5228338535903532426L;

	/** 登陆名 */
	private String loginname;

	/** 用户姓名 */
	private String username;

	/** 用户ID */
	private String userid;

	/** 登陆时间 */
	private Date opertime;

	/** 操作类型：1-登陆、2-插入、3-修改、4-删除、5-浏览 、6-查询类型   、7-记录其他的action*/
	private int opertype;
	
	/** 操作内容 */
	private String operContent;
	
	/** 操作模块 */
	private String operPath;

	/** 客户端操作系统 */
	private String agentsystem;

	/** 客户端浏览器 */
	private String agentexplorer;

	/** 客户端IP */
	private String agentip;

	/** 客户端电脑名 */
	private String agenthostname;

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUser_id() {
		return userid;
	}

	public void setUser_id(String userid) {
		this.userid = userid;
	}

	public String getOpertimeStr() {
		return DateUtil.getDateTimeStr(opertime);
	}

	public Date getOpertime() {
		return opertime;
	}

	public void setOpertime(Date opertime) {
		this.opertime = opertime;
	}

	public int getOpertype() {
		return opertype;
	}

	public void setOpertype(int opertype) {
		this.opertype = opertype;
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

	public String getOperContent() {
		return operContent;
	}

	public void setOperContent(String operContent) {
		this.operContent = operContent;
	}

	public String getOperPath() {
		return operPath;
	}

	public void setOperPath(String operPath) {
		this.operPath = operPath;
	}
}