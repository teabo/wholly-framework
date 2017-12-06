package com.whollyframework.base.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * The persistence utility.
 */
public class PersistenceUtils {
	private final static Log logger = LogFactory.getLog(PersistenceUtils.class);

	public static final ThreadLocal<Map<String,Connection>> runtimeDBConn = new ThreadLocal<Map<String,Connection>>();

	/**
	 * 表格-状态(是否存在)映射
	 */
	public static Map<String,Object> tableStateMap = new HashMap<String,Object>();

	/**
	 * 关闭Hibernate Session及其他数据库连接
	 * 
	 * @throws Exception
	 */
	public static void closeConnection() throws Exception {

		Map<String,Connection> connMap = (Map<String,Connection>) PersistenceUtils.runtimeDBConn.get();
		if (connMap != null) {
			for (Iterator<Connection> iterator = connMap.values().iterator(); iterator.hasNext();) {
				Connection conn = (Connection) iterator.next();
				if (!conn.isClosed()) {
					conn.close();
				}
				conn = null;
			}
		}

		PersistenceUtils.runtimeDBConn.set(null);
		logger.debug("has closed a runtimeDBConn!!!");
	}
	
	public static DataSource getC3P0DataSource(String username, String password, String driver, String url,
			String poolSize, String timeout) throws Exception {
		return getC3P0DataSource(username, password, driver, url, Integer.parseInt(poolSize), Integer.parseInt(timeout));
	}

	public static DataSource getC3P0DataSource(String username, String password, String driver, String url,
			int poolSize, int timeout) throws Exception {

		ComboPooledDataSource ds = new ComboPooledDataSource();

		ds.setUser(username);
		ds.setPassword(password);
		ds.setDriverClass(driver);
		ds.setJdbcUrl(url);
		ds.setMaxPoolSize(poolSize);
		ds.setMaxIdleTime(timeout);
		// ds.setMaxAdministrativeTaskTime(5000);
		ds.setNumHelperThreads(20);
		ds.setMaxStatements(0);
		logger.debug("has new a ComboPooledDataSource!!!");
		return ds;
	}

	public static DataSource getDBCPDataSource(String username, String password, String driver, String url,
			String poolSize, String timeout) {
		return getDBCPDataSource(username, password, driver, url, Integer.parseInt(poolSize), Integer.parseInt(timeout));
	}
	
	public static DataSource getDBCPDataSource(String username, String password, String driver, String url,
			int poolSize, int timeout) {

		BasicDataSource ds = new BasicDataSource();

		ds.setUsername(username);
		ds.setPassword(password);
		ds.setDriverClassName(driver);
		ds.setUrl(url);
		// ds.setPoolPreparedStatements(true);
		// ds.setMaxOpenPreparedStatements(10);
		// ds.setInitialSize(10);
		ds.setMaxIdle(5);
		ds.setMaxActive(poolSize);

		ds.setMaxWait(timeout);
		ds.setDefaultAutoCommit(true);
		logger.debug("has new a BasicDataSource!!!");
		return ds;
	}

	public static void closeConnection(Connection dbConnection) throws Exception {
		try {
			if (dbConnection != null && !dbConnection.isClosed()) {
				dbConnection.close();
			}
			logger.debug("has closed a dbConnection!!!");
		} catch (SQLException se) {
			throw new Exception("SQL Exception while closing " + "DB connection : \n" + se);
		}
	}

	public static void closeResultSet(ResultSet result) throws Exception {
		try {
			if (result != null) {
				result.close();
			}
			logger.debug("has closed a ResultSet!!!");
		} catch (SQLException se) {
			throw new Exception("SQL Exception while closing " + "Result Set : \n" + se);
		}
	}

	public static void closeStatement(Statement stmt) throws Exception {
		try {
			if (stmt != null) {
				stmt.close();
			}
			logger.debug("has closed a Statement!!!");
		} catch (SQLException se) {
			throw new Exception("SQL Exception while closing " + "Statement : \n" + se);
		}
	}

	public static Map<String,Object> getTableStateMap() {
		return tableStateMap;
	}
}
