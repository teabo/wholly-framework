package com.whollyframework.web.syslog.dao;

import org.springframework.stereotype.Repository;

import com.whollyframework.base.dao.ibatis.IBatisBaseDAO;
import com.whollyframework.web.syslog.model.SysLog;

/**
 * 操作日志DAO实现类
 * 
 * @author Chris Hsu
 * @since 2011-10-9
 * 
 */

@Repository
public class SysLogDAOImpl extends IBatisBaseDAO<SysLog,String> implements
		SysLogDAO {

	public SysLogDAOImpl() {
		// 将VO对象名传入基类(IBatisBaseDAO)，给beanName属性赋值。
		// 与sqlmap中的namespace对应。在进行数据库语句操作时，需要根据namespace来区分获取不同的操作语句。
		super(SysLog.class.getSimpleName());
	}

}
