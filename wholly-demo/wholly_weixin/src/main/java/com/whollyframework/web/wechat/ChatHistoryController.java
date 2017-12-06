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
import com.whollyframework.dbservice.wechat.chathistory.model.ChatHistoryVO;
import com.whollyframework.dbservice.wechat.chathistory.service.ChatHistoryService;

@Controller
@Scope("prototype")
@RequestMapping("/control/wechat/chatHistory")
public class ChatHistoryController extends BaseController<ChatHistoryVO, String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private ChatHistoryService chatHistoryService;

	public ChatHistoryController() {
		super(new ChatHistoryVO());
	}

	@Override
	public IDesignService<ChatHistoryVO, String> getService() {
		return chatHistoryService;
	}

	@RequestMapping({ "/index" })
	public String index(Model model, HttpServletRequest request, HttpServletResponse response) {

		return forward("list");
	}

}
