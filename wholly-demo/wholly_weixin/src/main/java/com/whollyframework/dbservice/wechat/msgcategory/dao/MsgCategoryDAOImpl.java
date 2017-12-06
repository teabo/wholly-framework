package com.whollyframework.dbservice.wechat.msgcategory.dao;


import org.springframework.stereotype.Repository;

import com.whollyframework.dbservice.wechat.msgcategory.model.MsgCategoryVO;
import com.whollyframework.base.dao.ibatis.IBatisBaseDAO;


@Repository("msgCategoryDAO")
public class MsgCategoryDAOImpl extends IBatisBaseDAO<MsgCategoryVO, String> implements MsgCategoryDAO {

	public MsgCategoryDAOImpl() {
		super();
	}
	
}
