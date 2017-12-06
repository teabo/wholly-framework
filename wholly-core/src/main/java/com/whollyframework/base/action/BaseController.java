package com.whollyframework.base.action;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.whollyframework.base.model.DataPackage;
import com.whollyframework.base.model.ValueObject;
import com.whollyframework.base.service.IDesignService;
import com.whollyframework.constans.Web;
import com.whollyframework.util.StringUtil;
import com.whollyframework.util.json.JsonUtil;
import com.whollyframework.util.property.DefaultProperty;
import com.whollyframework.utils.http.ResponseUtil;

public abstract class BaseController<E, ID extends Serializable> extends
		SupportController<E, ID> {

	/**
     * 
     */
	private static final long serialVersionUID = 3190502014384090183L;
	/**
	 * The default logger.
	 */
	private static final Logger log = Logger.getLogger(BaseController.class);

	private static final String ERROR_URI = DefaultProperty
			.getProperty(Web.FRAMEWORK_ERROR_URI);

	public BaseController(E content) {
		super(content);
	}

	protected String create() {
		try {
			if (!preAction())
				return ERROR();
			setPublicVariables();
		} catch (Exception e) {
			e.printStackTrace();
			addFieldError("", e.getMessage());
			return ERROR();
		}
		return forward("content");
	}

	protected String edit() {
		try {
			if (!preAction())
				return ERROR();
			E contentVO = getService().find(contentId);
			setContent(contentVO);
		} catch (Exception e) {
			e.printStackTrace();
			addFieldError("", e.getMessage());
			return ERROR();
		}
		return forward("content");
	}

	protected String save() {
		boolean isNew = ((ValueObject) getContent()).getIstemp() == 1;
		try {
			if (!preAction())
				return forward("content");
			setPublicVariables();
			// Save the value object and return the success message.
			if (isNew)
				getService().doCreate(getContent());
			else
				getService().doUpdate(getContent());

			this.addActionMessage("保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
			this.addFieldError("1", e.getMessage());
			if (isNew)
				((ValueObject) getContent()).setIstemp(1);
			return forward("content");
		}
		return redirect("list.action");
	}
	
	protected String saveAndNew() {
		boolean isNew = ((ValueObject) getContent()).getIstemp() == 1;
		try {
			if (!preAction())
				return forward("content");
			setPublicVariables();
			// Save the value object and return the success message.
			if (isNew)
				getService().doCreate(getContent());
			else
				getService().doUpdate(getContent());

			this.addActionMessage("保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
			this.addFieldError("1", e.getMessage());
			if (isNew)
				((ValueObject) getContent()).setIstemp(1);
			return forward("content");
		}
		return create();
	}

	protected String list() {
		try {
			if (!preAction())
				return ERROR();
			setDatas((DataPackage<E>) this.getService().query(getParams()));
		} catch (Exception e) {
			e.printStackTrace();
			addFieldError("", e.getMessage());
		}
		String type = getParams().getParameterAsString("_forwordUri");
		if (StringUtil.isBlank(type))
			return forward("list");
		else
			return forward("list" + type);

	}

	protected String delete() {
		try {
			if (!preAction())
				return ERROR();
			if (_selects != null)
				getService().doRemove(_selects);
			addActionMessage("删除成功！");
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getMessage().indexOf("Could not execute JDBC batch update") > -1) {
				addFieldError("", "存在外键引用关系，无法删除！");
			} else {
				log.error("doDelete", e);
				addFieldError("", e.getMessage());
			}
		}

		// 重定向，防止重复提交，当然这样不能完全解决重复提交的问题，只是简单处理一下，若要较好的防止重复提交可以结合token做，
		// 以“/”开关，相对于当前项目根路径，不以“/”开关，相对于当前路径
		// return fullPathRedirect("/user/list.action");
		return redirect("list.action");
	}

	protected void deleteAjax() {
		Map<String, String> rtn = new HashMap<String, String>();
		try {
			if (_selects != null)
				getService().doRemove(_selects);
			rtn.put("code", "1");
			rtn.put("msg", "删除成功！");
		} catch (Exception e) {
			log.error("deleteAjaxAction异常", e);
			rtn.put("code", "0");
			if (e.getMessage().indexOf("Could not execute JDBC batch update") > -1) {
				rtn.put("error", "存在外键引用关系，无法删除！");
			} else {
				rtn.put("error", e.getMessage());
			}
		}
		ResponseUtil.setJsonToResponse(getResponse(), JsonUtil.toJson(rtn));
	}

	protected void jsonList() {
		try {
			datas = this.getService().query(getParams());
			ResponseUtil.setJsonToResponse(getResponse(), datas.toJSON());
		} catch (Exception e) {
			log.error("jsonListAction异常", e);
			Map<String, String> rtn = new HashMap<String, String>();
			rtn.put("code", "0");
			rtn.put("error", "查询参数异常：" + e.getMessage());
			ResponseUtil.setJsonToResponse(getResponse(), JsonUtil.toJson(rtn));
		}
	}

	protected String ERROR() {
		return fullPathForward(ERROR_URI);
	}

	public abstract IDesignService<E, ID> getService();
}
