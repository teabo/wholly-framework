package com.whollyframework.generator.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.whollyframework.generator.util.SystemConf;

import freemarker.template.Configuration;

public class ConfigurationFactory {

	private static Map<String, Configuration> cfg_map = new HashMap<String, Configuration>();

	public static Map<String, Configuration> getConfigurations() throws IOException {
		List<String> resources = SystemConf.getResources();
		for (String path : resources) {
			getConfiguration(path);
		}
		return cfg_map;
		
	}

	public static Map<String, Configuration> getConfigurations(String[] templateDir) throws IOException {
		Map<String, Configuration> cfgs = new HashMap<String, Configuration>();
		for (int i = 0; i < templateDir.length; i++) {
			cfgs.put(templateDir[i], getConfiguration(templateDir[i]));
		}
		return cfgs;
	}

	public static Configuration getConfiguration(String templateDir) throws IOException {
		Configuration cfg = cfg_map.get(templateDir);
		if (cfg==null){
			cfg = initConfiguration(templateDir);
		}
		return cfg;
	}
	
	private static Configuration initConfiguration(String templateDir) throws IOException {
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
		//File templateDirFile = new File(templateDir);
		cfg.setClassLoaderForTemplateLoading(ConfigurationFactory.class.getClassLoader(), templateDir);
		cfg.setLocale(Locale.CHINA);
		cfg.setDefaultEncoding("UTF-8");
		cfg_map.put(templateDir, cfg);
		return cfg;
	}
}
