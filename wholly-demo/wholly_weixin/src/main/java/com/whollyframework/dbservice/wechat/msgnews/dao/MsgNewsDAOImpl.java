package com.whollyframework.dbservice.wechat.msgnews.dao;


import org.springframework.stereotype.Repository;

import com.whollyframework.dbservice.wechat.msgnews.model.MsgNewsVO;
import com.whollyframework.base.dao.ibatis.IBatisBaseDAO;


@Repository("msgNewsDAO")
public class MsgNewsDAOImpl extends IBatisBaseDAO<MsgNewsVO, String> implements MsgNewsDAO {

	public MsgNewsDAOImpl() {
		super();
	}
	
}
