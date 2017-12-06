package com.whollyframework.dbservice.wechat.chathistory.service;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.whollyframework.dbservice.wechat.chathistory.dao.ChatHistoryDAO;
import com.whollyframework.dbservice.wechat.chathistory.model.ChatHistoryVO;
import com.whollyframework.annotation.ClassLog;
import com.whollyframework.base.dao.IDesignDAO;
import com.whollyframework.base.service.AbstractDesignService;



@Service("chatHistoryService")
@ClassLog(remark="微信聊天记录")
public class ChatHistoryServiceImpl extends AbstractDesignService<ChatHistoryVO, String> implements
		ChatHistoryService {

	@Resource(name="chatHistoryDAO")
	private ChatHistoryDAO chatHistoryDAO;
	
	public IDesignDAO<ChatHistoryVO, String> getDAO() {
		return chatHistoryDAO;
	}

}
