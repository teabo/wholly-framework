package com.whollyframework.generator.core.config;

import com.whollyframework.generator.model.JavaModel;

public class MyBatisSqlmapGenerator extends IBatisSqlmapGenerator {

	public MyBatisSqlmapGenerator() {
		super("MyBatis.Sqlmap");
	}

	/**
	 * @return
	 * @throws Exception
	 *
	 */
	protected String getFiledName(JavaModel model, String col) {
		return "#{" + getBeanFiledName(model, col) + "}";
	}
}
