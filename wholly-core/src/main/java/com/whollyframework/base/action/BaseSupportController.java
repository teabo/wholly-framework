package com.whollyframework.base.action;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.whollyframework.authentications.IOrganization;
import com.whollyframework.authentications.IUser;
import com.whollyframework.authentications.IWebUser;
import com.whollyframework.base.model.DataPackage;
import com.whollyframework.base.model.ParamsTable;
import com.whollyframework.base.model.ValueObject;
import com.whollyframework.constans.Web;
import com.whollyframework.util.DateUtil;
import com.whollyframework.util.StringUtil;

/**
 * 
 * @author chris hsu
 * @since 上午9:00:26
 * 
 */
public abstract class BaseSupportController<E, ID extends Serializable> implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 5314927874937426042L;

	/**
	 * The default logger.
	 */
	private static final Logger log = Logger.getLogger(BaseSupportController.class);

	/**
	 * 数据对象
	 */
	private E content;

	/**
	 * 数据包
	 */
	protected DataPackage<E> datas;

	/**
	 * 参数集对象
	 */
	private ParamsTable params;

	private HttpServletRequest request;

	private HttpServletResponse response;
	/**
	 * 会话对象
	 */
	private HttpSession session;

	private IWebUser user;

	public BaseSupportController(E content) {
		this.content = content;
	}

	/**
	 * 主要应用在创建、编辑、查看等方法调用时
	 * 
	 * @return 返回数据对象
	 */
	public E getContent() {
		return content;
	}

	/**
	 * 设置数据对象
	 * 
	 * @param content 对应的数据对象实例
	 */
	public void setContent(E content) {
		this.content = content;
	}

	/**
	 * 
	 * @return 返回数据包，主要是查询出的数据对象的集合，包含分页信息
	 */
	public DataPackage<E> getDatas() {
		return datas;
	}

	/**
	 * 设置数据包对象值
	 * 
	 * @param datas
	 */
	public void setDatas(DataPackage<E> datas) {
		this.datas = datas;
	}

	/**
	 * Get the Parameters table
	 * 
	 * @return ParamsTable
	 */
	public ParamsTable getParams() {
		putRequestParameters();
		return params;
	}

	/**
	 * Put all the request parameters map in to parameters table.
	 */
	@SuppressWarnings({ "deprecation", "rawtypes" })
	private void putRequestParameters() {
		if (params == null) {
			// If the parameters table is empty, then initiate it.
			params = new ParamsTable();

			Map m = request.getParameterMap();
			String realPath = request.getRealPath("/");
			params.setParameter("realPath", realPath);
			params.setContextPath(request.getContextPath());
			Iterator iter = m.keySet().iterator();
			while (iter.hasNext()) {
				String name = (String) iter.next();
				Object value = m.get(name);
				try {
					// If there is only one string in the string array, the put
					// the
					// string only, not array.
					if (value instanceof String[]) if (((String[]) value).length > 1) params.setParameter(name, value);
					else params.setParameter(name, ((String[]) value)[0]);
					else params.setParameter(name, value);
				} catch (Exception e) {
					log.warn("Set parameter: " + name + " failed, the value is: " + value);
				}
			}
			params.setHttpRequest(request);
			// put the session id to parameters table.
			params.setSessionid(getSession().getId());
			
			// put the page line count id to parameters table.
			if (params.getParameter("_pagelines") == null) params
					.setParameter("_pagelines", Web.DEFAULT_LINES_PER_PAGE);
		}
	}
	
	/**
	 * 设置全局变量的值
	 */
	protected void setPublicVariables() {
		setPublicVariables((ValueObject) getContent());
	}

	/**
	 * 设置初始化对象的全局变量的值
	 */
	protected void setPublicVariables(ValueObject vo) {
		/** 设置创建时间 **/
		if (vo.getCreated() == null) {
			vo.setCreated(DateUtil.getToday());
		}
		/**** 设置创建用户以及机构等 ***/
		IUser user = this.getUser();
		if (StringUtil.isBlank(vo.getAuthor())) {
			if (user.getOrganizations().size() > 0) {
				IOrganization org = user.getOrganizations().get(0);
				vo.setOrg(org.getName());
				vo.setOrgId(org.getId());
			}
			vo.setAuthorId(user.getId());
			vo.setAuthor(user.getName());
		}
		/**** 设置常变量 *****/
		vo.setLastModified(DateUtil.getToday());
		vo.setLastModifyId(user.getId());
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
		setSession(request.getSession());
		IWebUser user = (IWebUser) getSession().getAttribute(Web.SESSION_ATTRIBUTE_USER);
		this.setUser(user);
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	public IWebUser getUser() {
		return user;
	}

	public void setUser(IWebUser user) {
		this.user = user;
	}
	
	/**
	 * 获取当前访问路径
	 * 
	 * @return 当前访问路径字串
	 */
	protected String getCurrentPath() {
		return getRequest().getRequestURI();
	}

}
