package ${model.packageName}.dao;


import org.springframework.stereotype.Repository;

import ${model.packageName}.model.${model.objectName}VO;
import ${model.extendsPackageName};


@Repository("${model.resourceName}DAO")
public class ${model.className} extends ${model.extendsName}<${model.objectName}VO, String> implements ${model.implementsName} {

	public ${model.className}() {
		super();
	}
	
}
