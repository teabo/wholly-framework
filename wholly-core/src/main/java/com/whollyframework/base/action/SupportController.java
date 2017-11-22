package com.whollyframework.base.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.whollyframework.authentications.Authorizations;
import com.whollyframework.base.model.ValueObject;
import com.whollyframework.constans.Web;

/**
 * 
 * @author chris hsu
 * @since 上午9:00:26
 * 
 */
public abstract class SupportController<E, ID extends Serializable> extends BaseSupportController<E, ID> implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 5314927874937426042L;

	protected ID contentId;

	protected ID[] _selects;
	
	protected Authorizations authorizations = new Authorizations();

	private LinkedHashMap<String, String> fieldErrors = new LinkedHashMap<String, String>();

	private ArrayList<String> actionMessages = new ArrayList<String>();

	/**
	 * 路径命名空间
	 */
	private String namespace;
	
	private String viewSuffix = ".jsp";

	SupportController(E content) {
		super(content);
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

	@RequestMapping(value = "/saveAndNew")
	public String saveAndNew(E content, HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.setRequest(request);
		this.setResponse(response);
		this.setContent(content);
		String result = saveAndNew();
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
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/delete")
	public String delete(@RequestParam(value = "_selects")String[] _selects, HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.setRequest(request);
		this.setResponse(response);
		this._selects = (ID[]) _selects;
		return delete();
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/deleteAjax")
	public void deleteAjax(@RequestParam(value = "_selects")String[] _selects, HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.setRequest(request);
		this.setResponse(response);
		this._selects = (ID[]) _selects;
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
	
	protected abstract String saveAndNew();

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
		path.append(getNamespace()).append("/").append(viewpath);
		if (viewpath.indexOf(".action")==-1){
			path.append(viewSuffix);
		}
		return path.toString();
	}

	/**
	 * 跳转到目的地址，绝对路径跳转, 采用dispatcher方式
	 * 
	 * @param viewpath
	 * @return
	 */
	protected String fullPathForward(String viewpath) {
		StringBuilder path = new StringBuilder();
		path.append(viewpath);
		if (viewpath.indexOf(".action")==-1){
			path.append(viewSuffix);
		}
		return path.toString();
	}

	/**
	 * 跳转到目的地址，相对路径跳转, 采用redirect方式
	 * 
	 * @param viewpath
	 * @return
	 */
	protected String redirect(String viewpath) {
		StringBuilder path = new StringBuilder();
		path.append("redirect:").append(getNamespace()).append("/").append(viewpath);
		if (viewpath.indexOf(".action")==-1){
			path.append(viewSuffix);
		}
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
		if (viewpath.indexOf(".action")==-1){
			path.append(viewSuffix);
		}
		return path.toString();
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
		getRequest().setAttribute(name, obj);
	}

	protected void setActionNotices() {
		getRequest().setAttribute(Web.SCOPE_ATTRIBUTE_AUTHORIZATIONS, authorizations);
		getRequest().setAttribute(Web.SCOPE_ATTRIBUTE_HAS_FIELDERRORS, hasFieldErrors());
		getRequest().setAttribute(Web.SCOPE_ATTRIBUTE_HAS_ACTIONMESSAGES, hasActionMessages());
		getRequest().setAttribute(Web.SCOPE_ATTRIBUTE_FIELDERRORS, fieldErrors);
		getRequest().setAttribute(Web.SCOPE_ATTRIBUTE_ACTIONMESSAGES, actionMessages);
	}
	
	public void setNamespaceToScope(HttpServletRequest request){
		request.setAttribute(Web.SCOPE_ATTRIBUTE_NAMESPACE, getNamespace());
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
	/**
	 * 请求执行前方法（在执行Action操作之前执行）
	 */
	protected boolean preAction() {

		return true;
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

	public String getViewSuffix() {
		return viewSuffix;
	}

	public void setViewSuffix(String viewSuffix) {
		this.viewSuffix = viewSuffix;
	}
	
}
