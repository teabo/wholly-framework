package com.whollyframework.dbservice.userbindip.service;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.whollyframework.annotation.ClassLog;
import com.whollyframework.base.dao.IDesignDAO;
import com.whollyframework.base.model.ParamsTable;
import com.whollyframework.base.service.AbstractDesignService;
import com.whollyframework.dbservice.userbindip.dao.BindIpDAO;
import com.whollyframework.dbservice.userbindip.model.BindIp;
import com.whollyframework.util.json.JsonUtil;

@Service("bindIpService")
@ClassLog(remark="用户IP绑定")
public class BindIpServiceImpl extends AbstractDesignService<BindIp,String> implements BindIpService {
    
    @Resource(name = "bindIpDAO")
    private BindIpDAO bindIpDao;
    
    
    public IDesignDAO<BindIp,String> getDAO() {
        return bindIpDao;
    }


	@Override
	public void saveBindIps(String json,String userId) throws Exception {
	    Collection<Object> collections=JsonUtil.toCollection(json, BindIp.class);
	    for(Object obj:collections){
	    	BindIp bindIp=(BindIp)obj;
	    	bindIp.setUserId(userId);
	    	doCreate(bindIp);
	    }
	}


	@Override
	public void updateBindIps(String json) throws Exception {
		Collection<Object> collections=JsonUtil.toCollection(json, BindIp.class);
	    for(Object obj:collections){
	    	BindIp bindIp=(BindIp)obj;
	    	doUpdate(bindIp);
	    }
		
	}


	@Override
	public void deleteBindIps(String json) throws Exception {
		Collection<Object> collections=JsonUtil.toCollection(json, BindIp.class);
	    for(Object obj:collections){
	    	BindIp bindIp=(BindIp)obj;
	    	doRemove(bindIp.getId());
	    }
	}


	@Override
	public List<BindIp> findUserBindIps(String useId) throws Exception {
		ParamsTable params=new ParamsTable();
		params.setParameter("s_user_id", useId);
		return simpleQuery(params);
	}
    
}
