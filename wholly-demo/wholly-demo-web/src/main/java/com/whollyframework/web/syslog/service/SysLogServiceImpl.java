package com.whollyframework.web.syslog.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whollyframework.base.dao.IDesignDAO;
import com.whollyframework.base.service.AbstractDesignService;
import com.whollyframework.web.syslog.dao.SysLogDAO;
import com.whollyframework.web.syslog.model.SysLog;



/**
 * 操作日志Service实现类
 * 
 * @author Chris Hsu
 * @since 2011-10-9
 * 
 */
@Service
public class SysLogServiceImpl extends AbstractDesignService<SysLog,String> implements
SysLogService {

	@Autowired
	private SysLogDAO sysLogDAO;
	
	public IDesignDAO<SysLog,String> getDAO() {
		return sysLogDAO;
	}
	

}
