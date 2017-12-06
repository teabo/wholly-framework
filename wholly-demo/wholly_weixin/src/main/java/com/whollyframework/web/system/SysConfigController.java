package com.whollyframework.web.system;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.whollyframework.base.action.BaseController;
import com.whollyframework.base.service.IDesignService;
import com.whollyframework.dbservice.sysconfig.model.SysParam;
import com.whollyframework.dbservice.sysconfig.service.SysConfigService;

@Controller
@Scope("prototype")
@RequestMapping("/control/system/context")
public class SysConfigController extends BaseController<SysParam, String>{

	
	
	public SysConfigController() {
		super(new SysParam());
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3681963435912758150L;

	@Autowired
	SysConfigService sysConfigService; 

	@Override
	public IDesignService<SysParam, String> getService() {
		return sysConfigService;
	}
	
	
	@RequestMapping({"/index"})
	public String index(HttpServletRequest request, HttpServletResponse response){
		try{
			setRequest(request);
	        setResponse(response);
		}catch (Exception e){
			e.printStackTrace();
			addFieldError("", e.getMessage());
		}
		return "/control/system/context/content";
	    }
	
	
	/*@RequestMapping({"/saveRow"})
	public void saveRow(SysParam vo, HttpServletRequest request, HttpServletResponse response){
	    try {
	    	setRequest(request);
	        setResponse(response);
	        String rows = getParams().getParameterAsString("rows");
	        Collection<Object> sysParamObj = JsonUtil.toCollection(rows, SysParam.class);
	        
	        if(sysParamObj.iterator().hasNext()){
	        	
		        SysParam sysParam = (SysParam)sysParamObj.iterator().next(); 
		        SysContextUtil.setAttr(sysParam);
		        SysParamConvertUtil.getNode();
		        
	        } 
		} catch (Exception e) {
				
			e.printStackTrace();
		}
	}*/
	
	/* @RequestMapping({"/index"})
	 public String index(HttpServletRequest request, HttpServletResponse response){
	    	try {
	    		setRequest(request);
	            setResponse(response);
	            List<Sysbean> datas = new ArrayList<Sysbean>();
	            for(String key : KEY_LIST){
	            	//ExManager.getAttr(key);
	            	String cn_name = NAME_MAP.get(key);
	            	if(cn_name!=null){
	            		Sysbean sb = new Sysbean();
	            		sb.setCnParamsKey(cn_name);
	            		sb.setParamskey(key);
	            		sb.setParamsValue("");
	            		//sb.setParamsValue(ExManager.getAttr(key)+"");
	            		datas.add(sb);
	            	}
	            }
	            request.setAttribute("datas",datas);
	            //ExManager.destory();
			} catch (Exception e) {
				
				e.printStackTrace();
			}
	    	return "/control/exchange/context/systemcontext/content";
	    }*/
	
		/*@RequestMapping({"/deleteAjax"})
	    public void deleteAjax(String[] _selects, HttpServletRequest request, HttpServletResponse response) throws Exception {
	         try {
	        	 setRequest(request);
		         setResponse(response);
		         this._selects = _selects;
		         deleteAjax();
			} catch (Exception e) {
				e.printStackTrace();
			}
	         
	       }*/
	       
}
