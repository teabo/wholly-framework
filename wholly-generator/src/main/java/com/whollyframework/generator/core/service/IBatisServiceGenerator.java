package com.whollyframework.generator.core.service;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.whollyframework.generator.core.AbstractGenerator;
import com.whollyframework.generator.model.JavaModel;

public class IBatisServiceGenerator extends AbstractGenerator {

	public final static String IMPL = "Impl";
	
	public IBatisServiceGenerator() {
		super("IBatis.Service");
	}


	@Override
	protected void initJavaModel(JavaModel model, Map<String, String> prop) {
		if (model.getClassName().endsWith(IMPL)) {
			String impInterface = StringUtils.substringBefore(model.getClassName(), IMPL);
			model.setImplementsName(impInterface);
		}
		model.setBaseServiceName("AbstractDesignService");
	}

}
