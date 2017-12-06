package com.whollyframework.dbservice.wechat.msgcategory.service;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.whollyframework.dbservice.wechat.msgcategory.dao.MsgCategoryDAO;
import com.whollyframework.dbservice.wechat.msgcategory.model.MsgCategoryVO;
import com.whollyframework.annotation.ClassLog;
import com.whollyframework.base.dao.IDesignDAO;
import com.whollyframework.base.service.AbstractDesignService;



@Service("msgCategoryService")
@ClassLog(remark="应答分类信息")
public class MsgCategoryServiceImpl extends AbstractDesignService<MsgCategoryVO, String> implements
		MsgCategoryService {

	@Resource(name="msgCategoryDAO")
	private MsgCategoryDAO msgCategoryDAO;
	
	public IDesignDAO<MsgCategoryVO, String> getDAO() {
		return msgCategoryDAO;
	}

}
