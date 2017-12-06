package com.whollyframework.dbservice.wechat.msgmedia.dao;


import org.springframework.stereotype.Repository;

import com.whollyframework.dbservice.wechat.msgmedia.model.MsgMediaVO;
import com.whollyframework.base.dao.ibatis.IBatisBaseDAO;


@Repository("msgMediaDAO")
public class MsgMediaDAOImpl extends IBatisBaseDAO<MsgMediaVO, String> implements MsgMediaDAO {

	public MsgMediaDAOImpl() {
		super();
	}
	
}
