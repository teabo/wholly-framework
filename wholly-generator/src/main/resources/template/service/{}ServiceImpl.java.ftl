package ${model.packageName}.service;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import ${model.packageName}.dao.${model.objectName}DAO;
import ${model.packageName}.model.${model.objectName}VO;
import com.whollyframework.base.dao.IDesignDAO;
import com.whollyframework.base.service.${model.baseServiceName};



@Service("${model.resourceName}Service")
public class ${model.className} extends ${model.baseServiceName}<${model.objectName}VO, String> implements
		${model.implementsName} {

	@Resource(name="${model.resourceName}DAO")
	private ${model.objectName}DAO ${model.resourceName}DAO;
	
	public IDesignDAO<${model.objectName}VO, String> getDAO() {
		return ${model.resourceName}DAO;
	}

}
