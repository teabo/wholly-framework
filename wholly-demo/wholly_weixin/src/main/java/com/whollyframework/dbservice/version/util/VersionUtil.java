package com.whollyframework.dbservice.version.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.whollyframework.base.model.ParamsTable;
import com.whollyframework.dbservice.version.model.Version;
import com.whollyframework.dbservice.version.service.VersionService;
import com.whollyframework.utils.ServicesFactory;

public class VersionUtil {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(VersionUtil.class);
	private static VersionService versionService = (VersionService) ServicesFactory
			.getService("versionService");

	/**
	 * 初始化版本信息
	 */
	public static void initVersion() {
		Properties pro = new Properties();
		try {
			String fileName = Thread.currentThread().getContextClassLoader()
					.getResource("version.properties").getPath();
			pro.load(new FileReader(new File(fileName)));
			String app_version = pro.getProperty("app_version");
			String db_version = pro.getProperty("db_version");
			String version_desc = pro.getProperty("version_desc");
			ParamsTable pt = new ParamsTable();
			pt.setParameter("s_app_version", app_version);
			pt.setParameter("s_db_version", db_version);
			Collection<Version> versions = versionService.simpleQuery(pt);
			if (null != versions && versions.size() > 0) {
				return;
			} else {
				Version version = new Version();
				version.setAppVersion(app_version);
				version.setDbVersion(db_version);
				version.setVersionDesc(version_desc);
				version.setCreateDate(new Date());
				versionService.doCreate(version);
			}
		} catch (FileNotFoundException e) {
			LOGGER.warn("初始化版本信息失败：" + e.getMessage());
		} catch (IOException e) {
			LOGGER.warn("初始化版本信息失败：" + e.getMessage());
		} catch (Exception e) {
			LOGGER.warn("初始化版本信息失败：" + e.getMessage());
		}
	}

}
