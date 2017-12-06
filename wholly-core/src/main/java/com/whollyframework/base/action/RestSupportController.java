package com.whollyframework.base.action;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.whollyframework.base.model.ValueObject;
import com.whollyframework.base.service.IDesignService;
import com.whollyframework.util.StringUtil;
import com.whollyframework.util.json.JsonUtil;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

public abstract class RestSupportController<E, ID extends Serializable> extends BaseSupportController<E, ID> {


	/**
	 * 
	 */
	private static final long serialVersionUID = -1856981597203886816L;
	
	private static final Logger log = LoggerFactory.getLogger(RestSupportController.class);
	
	protected ID[] _selects;

	public RestSupportController(E content) {
		super(content);
	}
	
	public abstract IDesignService<E, ID> getService();
	
	/**
	 * 请求执行前方法（在执行Action操作之前执行）
	 */
	protected String preAction() {

		return "";
	}
	
	@ResponseBody 
	@RequestMapping(value = "/get", produces="application/json;charset=UTF-8")
	@ApiOperation(notes = "get", httpMethod = "GET", value = "根据ID获取数据对象")
	public Object get(@RequestParam("id")ID contentId,HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.setRequest(request);
		this.setResponse(response);

		try {
			return getService().find(contentId);
		} catch (Exception e) {
			log.error("[{}] {} , id = {} , 获取对象信息异常：{}", "GET", getCurrentPath() ,contentId, e.getMessage());
			Map<String, String> rtn = new HashMap<String, String>();
			rtn.put("code", "0");
			rtn.put("error", "执行获取对象信息失败，异常：" + e.getMessage());
			return rtn;
		}
	}

	@ResponseBody 
	@RequestMapping(value = "/save", method=RequestMethod.POST, produces = { "text/plain;charset=UTF-8" })
	@ApiOperation(notes = "save", httpMethod = "POST", value = "添加或更新数据对象")
    @ApiResponses(value = {@ApiResponse(code = 405, message = "invalid input")})
	public String save(E content, HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.setRequest(request);
		this.setResponse(response);
		this.setContent(content);
		
		Map<String, String> rtn = new HashMap<String, String>();
		boolean isNew = ((ValueObject) getContent()).getIstemp() == 1;
		try {
			String preResult = preAction();
			if (!StringUtil.isBlank(preResult))
				return preResult;
			setPublicVariables();
			// Save the value object and return the success message.
			if (isNew)
				getService().doCreate(getContent());
			else
				getService().doUpdate(getContent());
			rtn.put("code", "1");
			rtn.put("msg", "保存成功！");
		} catch (Exception e) {
			log.error("[{}] {} , id = {} , 保存对象信息异常：{}", "POST", getCurrentPath(), JsonUtil.toJson(getContent()), e.getMessage());
			rtn.put("code", "0");
			rtn.put("error", "执行保存对象信息失败，异常：" + e.getMessage());
		}
		return JsonUtil.toJson(rtn);
	}

	@ResponseBody 
	@RequestMapping(value = "/delete", method=RequestMethod.DELETE, produces="application/json;charset=UTF-8")
	@ApiOperation(notes = "delete", httpMethod = "DELETE", value = "根据ID数组删除数据对象")
	public Object delete(@RequestParam(value = "_selects")ID[] _selects, HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.setRequest(request);
		this.setResponse(response);
		this._selects = _selects;
		Map<String, String> rtn = new HashMap<String, String>();
		try {
			if (_selects != null)
				getService().doRemove(_selects);
			rtn.put("code", "1");
			rtn.put("msg", "删除成功！");
		} catch (Exception e) {
			log.error("[{}] {} , ids = {} , 删除对象信息异常：{}", "DELETE", getCurrentPath(), JsonUtil.toJson(_selects), e.getMessage());
			rtn.put("code", "0");
			if (e.getMessage().indexOf("Could not execute JDBC batch update") > -1) {
				rtn.put("error", "执行删除失败，删除对象存在外键引用关系，无法删除！");
			} else {
				rtn.put("error", "执行删除失败，异常:" + e.getMessage());
			}
		}
		return rtn;
	}
	
	@ResponseBody 
	@RequestMapping(value = "/list", produces = { "text/plain;charset=UTF-8" })
	@ApiOperation(notes = "list", httpMethod = "POST", value = "根据查询条件查询数据对象列表")
	public String list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.setRequest(request);
		this.setResponse(response);
		try {
			datas = this.getService().query(getParams());
			return datas.toJSON();
		} catch (Exception e) {
			log.error("[{}] {} , conditions = {} , 查询对象信息异常：{}", "POST", getCurrentPath(), JsonUtil.toJson(getParams().getDynaProperties()), e.getMessage());
			Map<String, String> rtn = new HashMap<String, String>();
			rtn.put("code", "0");
			rtn.put("error", "执行查询失败，异常：" + e.getMessage());
			return JsonUtil.toJson(rtn);
		}
	}
}
