package com.whollyframework.generator.core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.whollyframework.generator.constant.Constants;
import com.whollyframework.generator.model.JavaModel;
import com.whollyframework.generator.util.SystemConf;
import com.whollyframework.util.StringUtil;

import freemarker.template.Template;
import freemarker.template.TemplateException;

public abstract class AbstractGenerator implements Generator {

	private String generator;

	public AbstractGenerator(String generator) {
		this.generator = generator;
	}

	public String getGenerator() {
		return generator;
	}

	public void setGenerator(String generator) {
		this.generator = generator;
	}

	public String transfer(String str, boolean isUpperCase){
		String prefix;
		if (isUpperCase){
			prefix = str.substring(0, 1).toUpperCase();
		} else {
			prefix = str.substring(0, 1).toLowerCase();
		}
        return prefix + str.substring(1);
    }
	
	@Override
	public void genAll(String objectName, String tableName, String pkName) {
		generate(objectName, tableName, pkName, SystemConf.getPropertys(generator));
	}

	public void generate(String objectName, String tableName, String pkName, Map<String, String> prop) {
		JavaModel model = new JavaModel();
		model.setPackageName(prop.get(Constants.PROP_PACKAGE_KEY) + "." + objectName.toLowerCase());
		String fileName = StringUtils.substringBefore(prop.get(Constants.PROP_TEMPLATE_FILE_KEY), ".ftl");
		fileName = fileName.replace("{}", objectName);
		String className = StringUtils.substringBefore(fileName, ".");
		model.setClassName(className);
		String resourceName = uncapFirst(objectName);
		model.setResourceName(resourceName);
		model.setObjectName(objectName);
		model.setSubPackageName(prop.get(Constants.PROP_SUBPACKAGE_KEY));
		model.setTableName(tableName);
		model.setPkName(pkName);
		initJavaModel(model, prop);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("model", model);
		String src = prop.get(Constants.PROP_SRC_KEY);
		if (StringUtil.isBlank(src)) {
			src = "src";
		}
		StringBuffer filePath = new StringBuffer();
		filePath.append(src).append("/");
		filePath.append(package2path(prop.get(Constants.PROP_PACKAGE_KEY))).append("/").append(objectName.toLowerCase())
				.append("/");
		filePath.append(prop.get(Constants.PROP_SUBPACKAGE_KEY)).append("/").append(fileName);
		StringBuffer template = new StringBuffer();
		template.append(prop.get(Constants.PROP_TEMPLATE_PATH_KEY)).append("/");
		template.append(prop.get(Constants.PROP_TEMPLATE_FILE_KEY));
		generate(template.toString(), data, filePath.toString());
	}

	protected abstract void initJavaModel(JavaModel model, Map<String, String> prop);

	public void generate(String templateFileName, Map<String, Object> data, String fileName) {
		try {
			String templateFileDir = templateFileName.substring(0, templateFileName.lastIndexOf("/"));
			String templateFile = templateFileName.substring(templateFileName.lastIndexOf("/"),
					templateFileName.length());
			// Get the templat object
			String genFileDir = fileName.substring(0, fileName.lastIndexOf("/"));
			Template template = ConfigurationFactory.getConfiguration(templateFileDir).getTemplate(templateFile);

			org.apache.commons.io.FileUtils.forceMkdir(new File(genFileDir));
			File output = new File(fileName);
			Writer writer = new FileWriter(output);
			template.process(data, writer);
			writer.close();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected String package2path(String packageName) {
		return packageName.replace('.', '/');
	}

	protected String getFileName(String filePath) {
		String fileName = StringUtils.substringAfterLast(filePath, "/");
		if (fileName.equals("") || fileName == null) {
			fileName = StringUtils.substringAfterLast(filePath, "\\");
		}
		return fileName;
	}

	protected String capFirst(String string) {
		String s = String.valueOf(string.charAt(0)).toUpperCase();
		s = s + string.substring(1);
		return s;
	}

	protected String uncapFirst(String string) {
		String s = String.valueOf(string.charAt(0)).toLowerCase();
		s = s + string.substring(1);
		return s;
	}
}
