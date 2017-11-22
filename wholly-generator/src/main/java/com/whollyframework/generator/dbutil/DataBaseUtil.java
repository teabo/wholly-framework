package com.whollyframework.generator.dbutil;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.whollyframework.base.model.PersistenceUtils;
import com.whollyframework.ddltools.ddlutil.DBUtils;
import com.whollyframework.ddltools.ddlutil.SQLBuilder;
import com.whollyframework.ddltools.model.Column;
import com.whollyframework.ddltools.model.Table;
import com.whollyframework.util.Security;
import com.whollyframework.util.property.PropertyUtil;

public class DataBaseUtil {

	public final static Logger log = Logger.getLogger(DataBaseUtil.class);
	public static final String ORACLE = "0";
	public static final String MYSQL = "1";
	public static final String SQLSERVER = "2";

	public static final String PROPERTIES_FILE = "generator";

	private static HashMap<String, DataSource> cachedDataSource = new HashMap<String, DataSource>();

	private static String dbType;
	static {
		PropertyUtil.load(PROPERTIES_FILE);
		dbType = PropertyUtil.get(PROPERTIES_FILE, "jdbc.dbtype");
	}

	public static Connection getConnection() throws Exception {
		String username = PropertyUtil.get(PROPERTIES_FILE, "jdbc.username");
		String password = PropertyUtil.get(PROPERTIES_FILE, "jdbc.password");
		String driver = PropertyUtil.get(PROPERTIES_FILE, "jdbc.driver");
		String url = PropertyUtil.get(PROPERTIES_FILE, "jdbc.url");
		String poolSize = PropertyUtil.get(PROPERTIES_FILE, "jdbc.poolSize");
		String timeout = PropertyUtil.get(PROPERTIES_FILE, "jdbc.timeout");

		return getConnection(username, password, driver, url, poolSize, timeout);
	}

	public static Table getTable(String tableName) {
		try {
			return getTable(tableName, dbType, getConnection());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Table getTable(String tableName, String dbType, Connection conn) {
		if (dbType.equals(ORACLE)) {
			return DBUtils.getTable(tableName, DBUtils.DBTYPE_ORACLE, conn);
		} else if (dbType.equals(SQLSERVER)) {
			return DBUtils.getTable(tableName, DBUtils.DBTYPE_MSSQL, conn);
		} else if (dbType.equals(MYSQL)) {
			return DBUtils.getTable(tableName, DBUtils.DBTYPE_MYSQL, conn);
		}

		return null;
	}

	/**
	 * 获取列的集合
	 * 
	 * @return
	 * @throws Exception
	 *
	 */
	public static List<Column> getColumns(Table table) throws Exception {
		if (table == null) {
			return null;
		}
		return (ArrayList<Column>) table.getColumns();
	}

	public static String getDataSourceKey(String username, String password, String driver, String url) {
		StringBuffer buf = new StringBuffer();

		buf.append(driver).append("||");
		buf.append(url).append("||");
		buf.append(username).append("||");
		buf.append(password).append("||");

		try {
			return Security.encodeToMD5(buf.toString());
		} catch (NoSuchAlgorithmException e) {
			return buf.toString();
		}
	}

	public static Connection getConnection(String username, String password, String driver, String url, String poolSize,
			String timeout) throws Exception {
		String key = getDataSourceKey(username, password, driver, url);
		DataSource dataSource = cachedDataSource.get(key);
		if (dataSource == null) {
			dataSource = PersistenceUtils.getDBCPDataSource(username, password, driver, url, poolSize, timeout);

			cachedDataSource.put(key, dataSource);
		}

		return dataSource.getConnection();
	}

	/**
	 * 2.6新增
	 * 
	 * @param sql
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	public int executeBatch(String sql, Connection conn) throws Exception {
		String[] commands = sql.split(SQLBuilder.SQL_DELIMITER);
		return executeBatch(commands, conn);
	}

	/**
	 * 2.6新增
	 * 
	 * @param commands
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	public int executeBatch(String[] commands, Connection conn) throws Exception {
		return DBUtils.executeBatch(commands, conn);
	}

	public static synchronized void remove(String datasourceKey) {
		cachedDataSource.remove(datasourceKey);
	}

	/**
	 * 取数据库类型名称
	 * 
	 * @param typeCode
	 * @return
	 */
	public static String getTypeName(String typeCode) {
		try {
			return getTypeName(Integer.parseInt(typeCode));
		} catch (Exception e) {
			return typeCode;
		}
	}

	/**
	 * 取数据库类型名称
	 * 
	 * @param typeCode
	 * @return
	 */
	public static String getTypeName(int typeCode) {

		switch (typeCode) {
		case Types.LONGNVARCHAR:
			return "LONGNVARCHAR";
		case Types.NCHAR:
			return "NCHAR";
		case Types.NVARCHAR:
			return "NVARCHAR";
		case Types.ROWID:
			return "ROWID";
		case Types.BIT:
			return "BIT";
		case Types.TINYINT:
			return "TINYINT";
		case Types.BIGINT:
			return "BIGINT";
		case Types.LONGVARBINARY:
			return "LONGVARBINARY";
		case Types.VARBINARY:
			return "VARBINARY";
		case Types.BINARY:
			return "BINARY";
		case Types.LONGVARCHAR:
			return "LONGVARCHAR";
		case Types.CHAR:
			return "CHAR";
		case Types.NUMERIC:
			return "NUMBER";
		case Types.DECIMAL:
			return "DECIMAL";
		case Types.INTEGER:
			return "INTEGER";
		case Types.SMALLINT:
			return "SMALLINT";
		case Types.FLOAT:
			return "FLOAT";
		case Types.REAL:
			return "REAL";
		case Types.DOUBLE:
			return "DOUBLE";
		case Types.VARCHAR:
			return "VARCHAR2";
		case Types.BOOLEAN:
			return "BOOLEAN";
		case Types.DATE:
		case Types.TIMESTAMP:
		case Types.TIME:
			return "DATE";
		case Types.BLOB:
			return "BLOB";
		case Types.CLOB:
			return "CLOB";
		}
		return "unknow";
	}

	public static String toJavaType(Column column) {
		if (column.getFixedLength() > 0) {
			return toJavaType(column.getTypeCode(), column.getLength(), column.getFixedLength());
		}
		return toJavaType(column.getTypeCode(), column.getLength());
	}

	/**
	 * 根据数据库类型生成java类型
	 * 
	 * @param dataType
	 * @return
	 *
	 */
	public static String toJavaType(String dataType) {
		return toJavaType(Integer.parseInt(dataType), new int[0]);
	}

	private static String toJavaType(int dataType, int... precision) {
		switch (dataType) {
		case -5:
			return "long";
		case Types.NUMERIC:
		case Types.DECIMAL:
			if (precision != null && precision.length > 0) {
				if (precision.length == 2 && precision[1]>0) {
					return "double";
				} else if (precision.length == 1) {
					if (precision[0]<10){
						return "int";
					} else if (precision[0]<19){
						return "long";
					}
				}
			}
			return "double";
		case Types.INTEGER:
			return "int";
		case Types.SMALLINT:
			return "short";
		case Types.FLOAT:
		case Types.REAL:
		case Types.DOUBLE:
			return "double";
		case 12:
			return "String";
		case 91:
		case 93:
			return "java.util.Date";
		}
		return "String";
	}

	// public static void main(String[] args) {
	// String datasourceId = "cd3fe4ac-49d5-48d2-a586-5a6bfa0e6bd2";
	//
	// try {
	// Collection<Table> tables = getTablesBySchema("XQUERY", datasourceId);
	// for (Iterator<Table> iterator = tables.iterator(); iterator
	// .hasNext();) {
	// Table table = (Table) iterator.next();
	// System.out.println(table.getName());
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }
}
