package ${model.packageName}.${model.subPackageName};

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.context.annotation.Scope;

import com.whollyframework.base.action.BaseController;

import ${model.packageName}.model.${model.objectName}VO;
import ${model.packageName}.service.${model.objectName}Service;

import com.whollyframework.base.service.IDesignService;


@Controller
@Scope("prototype")
@RequestMapping("${model.springMvcPath}")
public class ${model.className} extends BaseController<${model.objectName}VO,String> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    @Autowired
    private ${model.objectName}Service ${model.resourceName}Service;
    
    
    public ${model.className}() {
		super(new ${model.objectName}VO());
	}
 
    @Override
	public IDesignService<${model.objectName}VO,String> getService() {
		return ${model.resourceName}Service;
	}
	
	@RequestMapping({"/index"})
    public String index(Model model,HttpServletRequest request, HttpServletResponse response){
    	
    	return forward("list");
    }

}
