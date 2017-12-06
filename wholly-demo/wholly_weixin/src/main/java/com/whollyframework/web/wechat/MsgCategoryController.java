package com.whollyframework.web.wechat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.whollyframework.base.action.BaseController;
import com.whollyframework.base.service.IDesignService;
import com.whollyframework.dbservice.wechat.msgcategory.model.MsgCategoryVO;
import com.whollyframework.dbservice.wechat.msgcategory.service.MsgCategoryService;

@Controller
@Scope("prototype")
@RequestMapping("/control/wechat/msgCategory")
public class MsgCategoryController extends BaseController<MsgCategoryVO, String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private MsgCategoryService msgCategoryService;

	public MsgCategoryController() {
		super(new MsgCategoryVO());
	}

	@Override
	public IDesignService<MsgCategoryVO, String> getService() {
		return msgCategoryService;
	}

	@RequestMapping({ "/index" })
	public String index(Model model, HttpServletRequest request, HttpServletResponse response) {

		return forward("list");
	}

}
