package com.whollyframework.generator.core.dao;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.whollyframework.generator.core.AbstractGenerator;
import com.whollyframework.generator.model.JavaModel;

public class IBatisDAOGenerator extends AbstractGenerator {

public final static String IMPL = "Impl";
	
	public IBatisDAOGenerator() {
		super("IBatis.DAO");
	}

	@Override
	protected void initJavaModel(JavaModel model, Map<String, String> prop) {
		if (model.getClassName().endsWith(IMPL)) {
			String impInterface = StringUtils.substringBefore(model.getClassName(), IMPL);
			model.setImplementsName(impInterface);
			model.setExtendsName("IBatisBaseDAO");
			model.setExtendsPackageName("com.whollyframework.base.dao.ibatis.IBatisBaseDAO");
		} else {
			model.setExtendsName("IDesignDAO");
		}
	}

}
