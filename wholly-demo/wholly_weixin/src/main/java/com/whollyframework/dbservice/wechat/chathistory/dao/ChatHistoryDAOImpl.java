package com.whollyframework.dbservice.wechat.chathistory.dao;


import org.springframework.stereotype.Repository;

import com.whollyframework.base.dao.ibatis.IBatisBaseDAO;
import com.whollyframework.dbservice.wechat.chathistory.model.ChatHistoryVO;


@Repository("chatHistoryDAO")
public class ChatHistoryDAOImpl extends IBatisBaseDAO<ChatHistoryVO, String> implements ChatHistoryDAO {

	public ChatHistoryDAOImpl() {
		super();
	}
	
}
