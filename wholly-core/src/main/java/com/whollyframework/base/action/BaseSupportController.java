package com.whollyframework.base.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.whollyframework.authentications.IWebUser;
import com.whollyframework.base.model.DataPackage;
import com.whollyframework.base.model.ParamsTable;
import com.whollyframework.base.model.ValueObject;
import com.whollyframework.constans.Web;

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
	
	protected ID contentId;

	/**
	 * 数据包
	 */
	protected DataPackage<E> datas;

	protected ID[] _selects;

	/**
	 * 路径命名空间
	 */
	private String namespace;

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

	private LinkedHashMap<String, String> fieldErrors = new LinkedHashMap<String, String>();

	private ArrayList<String> actionMessages = new ArrayList<String>();

	private IWebUser user;

	public BaseSupportController(E content) {
		this.content = content;
		RequestMapping requestMapping = this.getClass().getAnnotation(RequestMapping.class);
		if (requestMapping != null && requestMapping.value() != null) {
			this.namespace = requestMapping.value()[0];
		}
	}

	@RequestMapping(value = "/create")
	public String create(HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.setRequest(request);
		this.setResponse(response);
		String result = create();
		((ValueObject) getContent()).setIstemp(1);
		setAttribute("content", getContent());
		setActionNotices();
		return result;
	}

	@RequestMapping(value = "/edit")
	public String edit(@RequestParam("id")ID contentId,HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.setRequest(request);
		this.setResponse(response);
		this.contentId = contentId;
		String result = edit();
		setAttribute("content", getContent());
		setActionNotices();
		return result;
	}

	@RequestMapping(value = "/save")
	public String save(E content, HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.setRequest(request);
		this.setResponse(response);
		this.setContent(content);
		String result = save();
		setAttribute("content", getContent());
		setActionNotices();
		return result;
	}

	@RequestMapping(value = "/list")
	public String list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.setRequest(request);
		this.setResponse(response);
		String result = list();
		setAttribute("datas", getDatas());
		setActionNotices();
		return result;
	}

	@RequestMapping(value = "/delete")
	public String delete(ID[] _selects, HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.setRequest(request);
		this.setResponse(response);
		this._selects = _selects;
		return delete();
	}

	@RequestMapping(value = "/deleteAjax")
	public void deleteAjax(ID[] _selects, HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.setRequest(request);
		this.setResponse(response);
		this._selects = _selects;
		deleteAjax();
	}
	
	@RequestMapping(value = "/jsonList")
	public void jsonList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.setRequest(request);
		this.setResponse(response);
		jsonList();
	}
	
	/**
	 * ajax数据查询方法，以JSON字符串方式返回
	 */
	protected abstract void jsonList();

	/**
	 * ajax数据删除方法，以JSON字符串方式返回
	 */
	protected abstract void deleteAjax();

	protected abstract String create();

	protected abstract String edit();

	protected abstract String save();

	protected abstract String list();

	protected abstract String delete();

	/**
	 * 跳转到目的地址，相对路径(相对于namespace路径)跳转, 采用dispatcher方式
	 * 
	 * @param viewpath
	 * @return
	 */
	protected String forward(String viewpath) {
		StringBuilder path = new StringBuilder();
		path.append(namespace).append("/").append(viewpath);
		return path.toString();
	}

	/**
	 * 跳转到目的地址，绝对路径跳转, 采用dispatcher方式
	 * 
	 * @param viewpath
	 * @return
	 */
	protected String fullPathForward(String viewpath) {
		return viewpath;
	}

	/**
	 * 跳转到目的地址，相对路径跳转, 采用redirect方式
	 * 
	 * @param viewpath
	 * @return
	 */
	protected String redirect(String viewpath) {
		StringBuilder path = new StringBuilder();
		path.append("redirect:").append(namespace).append("/").append(viewpath);
		return path.toString();
	}

	/**
	 * 跳转到目的地址，绝对路径跳转, 采用redirect方式
	 * 
	 * @param viewpath
	 * @return
	 */
	protected String fullPathRedirect(String viewpath) {
		StringBuilder path = new StringBuilder();
		path.append("redirect:").append(viewpath);
		return path.toString();
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
	 * 
	 * @return 命名空间值（即Controller定义的路径）
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * 设置命名空间值
	 * 
	 * @param namespace
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
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

	/**
	 * 是否存在字段错误
	 * 
	 * @return true | false
	 */
	public boolean hasFieldErrors() {
		return fieldErrors.size() > 0;
	}

	/**
	 * 是否存在请求方法信息
	 * 
	 * @return true | false
	 */
	public boolean hasActionMessages() {
		return actionMessages.size() > 0;
	}

	/**
	 * 将对象添加到Request对象属性集中
	 * 
	 * @param name 属性名
	 * @param obj 数据对象
	 */
	public void setAttribute(String name, Object obj) {
		request.setAttribute(name, obj);
	}

	protected void setActionNotices() {
		request.setAttribute("hasFieldErrors", hasFieldErrors());
		request.setAttribute("hasActionMessages", hasActionMessages());
		request.setAttribute("fieldErrors", fieldErrors);
		request.setAttribute("actionMessages", actionMessages);
	}

	public void addFieldError(String key, String value) {
		fieldErrors.put(key, value);
	}

	public LinkedHashMap<String, String> getFieldErrors() {
		return fieldErrors;
	}

	public void addActionMessage(String actionMessage) {
		actionMessages.add(actionMessage);
	}

	public ArrayList<String> getActionMessages() {
		return actionMessages;
	}

	public IWebUser getUser() {
		return user;
	}

	public void setUser(IWebUser user) {
		this.user = user;
	}
}
