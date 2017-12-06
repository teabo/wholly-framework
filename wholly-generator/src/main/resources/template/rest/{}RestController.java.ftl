package ${model.packageName}.${model.subPackageName};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.context.annotation.Scope;

import com.whollyframework.base.action.RestSupportController;

import ${model.packageName}.model.${model.objectName}VO;
import ${model.packageName}.service.${model.objectName}Service;

import com.whollyframework.base.service.IDesignService;


@RestController
@Scope("prototype")
@RequestMapping("${model.springRestPath}")
public class ${model.className} extends RestSupportController<${model.objectName}VO, String> {
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

}
