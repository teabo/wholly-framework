package com.whollyframework.web.init;

import java.sql.Connection;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.whollyframework.base.model.PersistenceUtils;
import com.whollyframework.ddltools.ddlutil.DBUtils;
import com.whollyframework.util.property.PropertyUtil;
import com.whollyframework.utils.ServicesFactory;

public class InitDateBase implements IInitialization{

	private final static Logger LOG = LoggerFactory.getLogger(InitDateBase.class);
	
	public void run() {
		Connection conn = null;
		try {
			DataSource dataSource = (DataSource)ServicesFactory.getService("dataSource");
			
			PropertyUtil.load("jdbc");
			
			List<String> tableNames = (List<String>) DBUtils.getTableNames(PropertyUtil.get("jdbc.dbtype").toUpperCase(), dataSource.getConnection());
			LOG.info("检查数据库信息......");
			if (tableNames.isEmpty() || !tableNames.contains("SYS_USER_INFO")){
				conn = dataSource.getConnection();
				SqlFileExecutor sqlExecutor = new SqlFileExecutor();			
				LOG.info("数据库初始化......");
				LOG.info("数据库初始化数据结构......");
				sqlExecutor.executeSQLFile(conn, "script/db-tables-create.sql");
				LOG.info("数据库初始化函数方法......");
				sqlExecutor.executeSQLFile(conn, "script/db-functions-create.sql", "(##split##;\\s*\r\n)|(##split##;\\s*\n)");
				LOG.info("数据库初始化数据......");
				sqlExecutor.executeSQLFile(conn, "script/db-init-datas.sql");
				LOG.info("数据库初始化成功！");
			}
		} catch (Exception e) {
			LOG.error("数据库初始化失败！");
			throw new RuntimeException(e.getMessage());
		} finally{
			try {
				PersistenceUtils.closeConnection(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

}
