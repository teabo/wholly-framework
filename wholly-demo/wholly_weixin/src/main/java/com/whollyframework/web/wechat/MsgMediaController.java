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
import com.whollyframework.dbservice.wechat.msgmedia.model.MsgMediaVO;
import com.whollyframework.dbservice.wechat.msgmedia.service.MsgMediaService;

@Controller
@Scope("prototype")
@RequestMapping("/control/wechat/msgMedia")
public class MsgMediaController extends BaseController<MsgMediaVO, String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private MsgMediaService msgMediaService;

	public MsgMediaController() {
		super(new MsgMediaVO());
	}

	@Override
	public IDesignService<MsgMediaVO, String> getService() {
		return msgMediaService;
	}

	@RequestMapping({ "/index" })
	public String index(Model model, HttpServletRequest request, HttpServletResponse response) {

		return forward("list");
	}

}
