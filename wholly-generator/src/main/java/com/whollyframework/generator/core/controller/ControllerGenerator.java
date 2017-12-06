package com.whollyframework.generator.core.controller;

import java.util.Map;

import com.whollyframework.generator.constant.Constants;
import com.whollyframework.generator.core.AbstractGenerator;
import com.whollyframework.generator.model.JavaModel;

public class ControllerGenerator extends AbstractGenerator {

	public ControllerGenerator() {
		super("Controller");
	}

	@Override
	protected void initJavaModel(JavaModel model, Map<String, String> prop) {
		model.setSpringMvcPath(prop.get(Constants.PROP_MVC_PATH_KEY) + "/" + model.getResourceName());
		model.setSpringRestPath(prop.get(Constants.PROP_REST_PATH_KEY) + "/" + model.getResourceName());
	}

}
