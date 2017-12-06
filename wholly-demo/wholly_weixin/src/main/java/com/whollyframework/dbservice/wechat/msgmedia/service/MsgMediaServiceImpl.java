package com.whollyframework.dbservice.wechat.msgmedia.service;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.whollyframework.dbservice.wechat.msgmedia.dao.MsgMediaDAO;
import com.whollyframework.dbservice.wechat.msgmedia.model.MsgMediaVO;
import com.whollyframework.annotation.ClassLog;
import com.whollyframework.base.dao.IDesignDAO;
import com.whollyframework.base.service.AbstractDesignService;



@Service("msgMediaService")
@ClassLog(remark="媒体信息管理")
public class MsgMediaServiceImpl extends AbstractDesignService<MsgMediaVO, String> implements
		MsgMediaService {

	@Resource(name="msgMediaDAO")
	private MsgMediaDAO msgMediaDAO;
	
	public IDesignDAO<MsgMediaVO, String> getDAO() {
		return msgMediaDAO;
	}

}
