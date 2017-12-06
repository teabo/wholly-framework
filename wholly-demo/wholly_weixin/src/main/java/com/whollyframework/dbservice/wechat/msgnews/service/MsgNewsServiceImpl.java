package com.whollyframework.dbservice.wechat.msgnews.service;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.whollyframework.dbservice.wechat.msgnews.dao.MsgNewsDAO;
import com.whollyframework.dbservice.wechat.msgnews.model.MsgNewsVO;
import com.whollyframework.annotation.ClassLog;
import com.whollyframework.base.dao.IDesignDAO;
import com.whollyframework.base.service.AbstractDesignService;



@Service("msgNewsService")
@ClassLog(remark="图文信息管理")
public class MsgNewsServiceImpl extends AbstractDesignService<MsgNewsVO, String> implements
		MsgNewsService {

	@Resource(name="msgNewsDAO")
	private MsgNewsDAO msgNewsDAO;
	
	public IDesignDAO<MsgNewsVO, String> getDAO() {
		return msgNewsDAO;
	}

}
