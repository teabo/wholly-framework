package com.whollyframework.web.syslog.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.whollyframework.base.action.BaseController;
import com.whollyframework.base.service.IDesignService;
import com.whollyframework.web.syslog.model.SysLog;
import com.whollyframework.web.syslog.service.SysLogService;


@Controller
@Scope("prototype")
@RequestMapping(value = "/control/syslog")
public class SysLogController extends BaseController<SysLog, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2821959082135068931L;

	public SysLogController() {
		super(new SysLog());
	}

	@Autowired
	SysLogService sysLogService;

    @Override
    public IDesignService<SysLog, String> getService() {
        return sysLogService;
    }
    
    @RequestMapping({"/index"})
    public String index(HttpServletRequest request, HttpServletResponse response){
    	try {
    		setRequest(request);
            setResponse(response);
    		
		} catch (Exception e) {
			
			e.printStackTrace();
		}
    	return forward("list");
    	
    }
    
    
}
