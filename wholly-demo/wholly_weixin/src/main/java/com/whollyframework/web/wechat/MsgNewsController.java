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
import com.whollyframework.dbservice.wechat.msgnews.model.MsgNewsVO;
import com.whollyframework.dbservice.wechat.msgnews.service.MsgNewsService;

@Controller
@Scope("prototype")
@RequestMapping("/control/wechat/msgNews")
public class MsgNewsController extends BaseController<MsgNewsVO, String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private MsgNewsService msgNewsService;

	public MsgNewsController() {
		super(new MsgNewsVO());
	}

	@Override
	public IDesignService<MsgNewsVO, String> getService() {
		return msgNewsService;
	}

	@RequestMapping({ "/index" })
	public String index(Model model, HttpServletRequest request, HttpServletResponse response) {

		return forward("list");
	}

}
