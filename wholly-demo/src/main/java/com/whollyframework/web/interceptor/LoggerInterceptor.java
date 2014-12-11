package com.whollyframework.web.interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.whollyframework.authentications.IWebUser;
import com.whollyframework.util.DateUtil;
import com.whollyframework.util.StringUtil;
import com.whollyframework.utils.client.ClientInfo;
import com.whollyframework.web.syslog.model.SysLog;

public class LoggerInterceptor implements HandlerInterceptor{
//	@Autowired
//	private OperlogService operlogService;
	

	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
//		System.out.println("==============执行顺序: 3、afterCompletion================");  
		
	}

	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
//		System.out.println("==============执行顺序: 2、postHandle================");  
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object arg2) throws Exception {

//		System.out.println("==============执行顺序: 1、preHandle================");  
		
		try {
			HttpSession session=request.getSession(); 
			//获取url地址
			String requestURL=request.getRequestURI().replace(request.getContextPath(), "");
			//当url地址为登录的url的时候跳过拦截器
			int start=requestURL.lastIndexOf("/");
			int end=requestURL.lastIndexOf(".action");
			String actionName=requestURL.substring(start+1,end);
			
			SysLog czrz = new SysLog();
			czrz.setOperPath(requestURL);
			
			IWebUser user = getUser(session);
			
			if (actionName.startsWith("login")) {
				//login.action
				
				//记录登录信息  [username, password, authImg]
				/*String username = request.getParameter("username");
				
				czrz.setOperContent("登录系统:  用户名是  "+username);
				czrz.setOpertype(1);

				czrz.setOpertime(DateUtil.getToday());
				
				String agent = request.getHeader("User-Agent");
				ClientInfo clientinfo = new ClientInfo(request, agent);
				czrz.setAgentexplorer(clientinfo.getAgentExplorer());// 客户端浏览器
				czrz.setAgentsystem(clientinfo.getAgentOSystem());// 操作系统
				czrz.setAgentip(clientinfo.getRemoteIP());// 客户端ip
				czrz.setAgenthostname(clientinfo.getRemoteHost());// 客户端电脑名

				operlogService.doCreate(czrz);*/
				
				return true;
			}else if(actionName.startsWith("save") ){
				//save.action 
				
				String title = request.getParameter("content.title");
				StringBuffer buf = new StringBuffer();
				String id = request.getParameter("content.id");
				if (!StringUtil.isBlank(id)){
					buf.append("{执行更新操作}记录ID：[").append(id).append("]");
				} else {
					buf.append("{执行更新操作}");
				}
				if (StringUtil.isBlank(title)){
					title = request.getParameter("content.name");
					if (!StringUtil.isBlank(title)){
						buf.append(",  标题：[").append(title).append("]");
					}
				} else {
					buf.append(",  标题：[").append(title).append("]");
				}
				
				czrz.setOperContent(buf.toString());
				czrz.setOpertype(2);
				
			}else if (actionName.startsWith("edit")) {
				//edit.action  
				
				String id = request.getParameter("id");
				StringBuffer buf = new StringBuffer();
				
				if (StringUtil.isBlank(id)){
					id = request.getParameter("content.id");
					if (!StringUtil.isBlank(id)){
						buf.append("{执行打开操作}");
						buf.append("记录ID：[").append(id).append("]");
					}
				} else {
					buf.append("{执行打开操作}");
					buf.append("记录ID：[").append(id).append("]");
				}
				czrz.setOperContent(buf.toString());
				czrz.setOpertype(3);
			}else if (actionName.startsWith("delete")) {
				//deleteAjax.action
				
				String id = request.getParameter("_selects");
				StringBuffer buf = new StringBuffer();
				buf.append("{开始执行删除操作}");
				if (StringUtil.isBlank(id)){
					id = request.getParameter("id");
					if (!StringUtil.isBlank(id)){
						buf.append("记录ID集合：[").append(id).append("]");
					}
				} else {
					buf.append("记录ID集合：[").append(id).append("]");
				}
				czrz.setOperContent(buf.toString());
				czrz.setOpertype(4);
				
			}else if (actionName.startsWith("jsonList")) {
				//jsonList.action
				
				//记录datagrid的查询信息
				StringBuilder sb = new StringBuilder();
				
				Enumeration pNames=request.getParameterNames();
				while(pNames.hasMoreElements()){
				    String name=(String)pNames.nextElement();
				    String value=request.getParameter(name);
				    sb.append("[").append(name+" ").append(" : ").append(" "+value).append("]");
				}				
				
				czrz.setOperContent("查询数据, 查询条件为  "+ sb.toString());
				czrz.setOpertype(6);				
			}else{
				//其他的action
				czrz.setOperContent("其他的action");
				czrz.setOpertype(7);
			}
			
			czrz.setOpertime(DateUtil.getToday());
			czrz.setUsername(user.getName()==null? "" : user.getName());
			czrz.setLoginname(user.getLoginName());
			czrz.setUser_id(user.getId());
//			czrz.setUnit_id(user.getUnit_id());
//			czrz.setApp_id(user.getApp_id());
			String agent = request.getHeader("User-Agent");
			ClientInfo clientinfo = new ClientInfo(request, agent);
			czrz.setAgentexplorer(clientinfo.getAgentExplorer());// 客户端浏览器
			czrz.setAgentsystem(clientinfo.getAgentOSystem());// 操作系统
			czrz.setAgentip(clientinfo.getRemoteIP());// 客户端ip
			czrz.setAgenthostname(clientinfo.getRemoteHost());// 客户端电脑名

//			operlogService.doCreate(czrz);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		}
		
		return true;
	}
	
	
	public IWebUser getUser(HttpSession session) {
		Object obj = session.getAttribute("USER");
		if (obj == null) {
			return null;
		}
		return (IWebUser) obj;
	}

}
