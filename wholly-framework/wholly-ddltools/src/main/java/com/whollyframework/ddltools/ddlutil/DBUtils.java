package com.whollyframework.ddltools.ddlutil;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.whollyframework.ddltools.model.Column;
import com.whollyframework.ddltools.model.Table;

public class DBUtils {

	public final static Logger log = Logger.getLogger(DBUtils.class);

	public static final String DBTYPE_ORACLE = "ORACLE";

	public static final String DBTYPE_MSSQL = "MSSQL";

	public static final String DBTYPE_MYSQL = "MYSQL";

	public static final String DBTYPE_HSQLDB = "HSQLDB";

	public static final String DBTYPE_DB2 = "DB2";

	public static final String NO_NEED_PARAM = "@@NIND";

	public static Collection<String> getTableNames(String[] conditions,
			String[] cValues, String dbType, Connection conn) {
		try {
			if (dbType.equals(DBTYPE_ORACLE)) {
				return getOracleTableNames(conditions, cValues, dbType, conn);
			} else if (dbType.equals(DBTYPE_MSSQL)) {
			} else if (dbType.equals(DBTYPE_MYSQL)) {
			} else if (dbType.equals(DBTYPE_HSQLDB)) {
			} else if (dbType.equals(DBTYPE_DB2)) {
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return new ArrayList<String>();
	}

	protected static Collection<String> getOracleTableNames(
			String[] conditions, String[] cValues, String dbType,
			Connection conn) {
		Collection<String> rtn = new ArrayList<String>();
		PreparedStatement pstm = null;
		ResultSet tableSet = null;
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT T.* FROM USER_OBJECTS T WHERE (T.OBJECT_TYPE='TABLE' OR T.OBJECT_TYPE='VIEW') ");
			if (conditions != null && conditions.length > 0) {
				for (int i = 0; i < conditions.length; i++) {

					int st = conditions[i].indexOf("_");
					if (st > 0) {
						String fieldname = conditions[i].substring(st + 1);

						if (!"@@NIND".equalsIgnoreCase(cValues[i])) {
							if (conditions[i].toLowerCase().startsWith("s_")
									|| conditions[i].toLowerCase().startsWith(
											"n_")) {
								sql.append(" AND ").append(fieldname).append("=?");
							} else if (conditions[i].toLowerCase().startsWith(
									"sm_")) {
								sql.append(" AND ").append(fieldname).append(" like ?");
							} else if (conditions[i].toLowerCase().startsWith(
									"ism_")) {
								sql.append(" AND ").append(fieldname).append(" not like ?");
							} else {
								sql.append(" AND ").append(conditions[i]).append("=?");
							}
						}

					} else {
						sql.append(conditions[i]);
						if (!"@@NIND".equalsIgnoreCase(cValues[i]))
							sql.append("=?");

					}
				}
			}
			pstm = conn.prepareStatement(sql.toString());
			if (conditions != null) {
				int j = 1;
				for (int i = 0; i < cValues.length; i++) {
					if (!"@@NIND".equalsIgnoreCase(cValues[i])) {
						pstm.setObject(j, cValues[i]);
						j++;
					}
				}
			}
			tableSet = pstm.executeQuery();
			if (tableSet != null)
				while (tableSet.next()) {
					String tableName = tableSet.getString("OBJECT_NAME");
					rtn.add(tableName);
				}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (tableSet != null) {
					tableSet.close();
				}
				if (pstm != null) {
					pstm.close();
				}
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}

		return rtn;
	}

	public static Collection<String> getTableNames(String dbType,
			Connection conn) {
		Collection<String> rtn = new ArrayList<String>();

		ResultSet tableSet = null;
		try {

			String catalog = null;
			String schemaPattern = null;
			String schema = getSchema(conn, dbType);

			if (dbType.equals(DBTYPE_ORACLE)) {
				schemaPattern = schema;
			} else if (dbType.equals(DBTYPE_MSSQL)) {
				schemaPattern = schema;
			} else if (dbType.equals(DBTYPE_MYSQL)) {
				catalog = schema;
			} else if (dbType.equals(DBTYPE_HSQLDB)) {
				schemaPattern = schema;
			} else if (dbType.equals(DBTYPE_DB2)) {
				schemaPattern = schema;
			}

			DatabaseMetaData metaData = conn.getMetaData();
			tableSet = metaData.getTables(catalog, schemaPattern, null,
					new String[] { "TABLE" });

			while (tableSet.next()) {
				String tableName = tableSet.getString(3);
				rtn.add(tableName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (tableSet != null) {
					tableSet.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return rtn;
	}

	public static Table getTable(String tableName, String dbType,
			Connection conn) {
		Collection<Table> tables = getTables(tableName, dbType, conn);
		if (tables != null && !tables.isEmpty()) {
			return tables.iterator().next();
		}

		return null;
	}

	/**
	 * 
	 * @param tableName
	 *            数据表名称
	 * @param applicationId
	 *            应用ID
	 * @return 表集合
	 */
	public static Collection<Table> getTables(String tableName, String dbType,
			Connection conn) {

		Collection<Table> rtn = new ArrayList<Table>();

		ResultSet tableSet = null;
		Statement smt = null;
		try {

			String catalog = null;
			String schemaPattern = null;

			String schema = getSchema(conn, dbType);

			if (dbType.equals(DBTYPE_ORACLE)) {
				schemaPattern = schema;
			} else if (dbType.equals(DBTYPE_MSSQL)) {
				schemaPattern = "DBO";
			} else if (dbType.equals(DBTYPE_MYSQL)) {
				catalog = schema;
			} else if (dbType.equals(DBTYPE_HSQLDB)) {
				schemaPattern = schema;
			} else if (dbType.equals(DBTYPE_DB2)) {
				schemaPattern = schema;
			}

			DatabaseMetaData metaData = conn.getMetaData();
			tableSet = metaData.getColumns(catalog, schemaPattern, tableName,
					null);
			Map<String, String> map = new HashMap<String, String>();
			if (tableSet != null) {
				while (tableSet.next()) {
					String name = tableSet.getString("COLUMN_NAME");
					String defaultValue = tableSet.getString("COLUMN_DEF");
					map.put(name, defaultValue);
				}
				tableSet.close();
			}
			if (map.size() > 0) {
				Table table = new Table(tableName.toUpperCase());
				String sqlStr = "SELECT * FROM " + tableName;
				smt = conn.createStatement();
				tableSet = smt.executeQuery(sqlStr);
				ResultSetMetaData rsmd = tableSet.getMetaData();
				for (int i = 0; i < rsmd.getColumnCount(); i++) {
					String name = rsmd.getColumnLabel(i + 1);
					int typeCode = rsmd.getColumnType(i + 1);
					Column column = new Column("", name.toUpperCase(), typeCode);
					if (typeCode == Types.VARCHAR) {
						int length = rsmd.getPrecision(i + 1);
						column.setLength(length);
					} else if (typeCode == Types.NUMERIC) {
						int length = rsmd.getPrecision(i + 1);
						int fixedLength = rsmd.getScale(i + 1);
						column.setLength(length);
						column.setFixedLength(fixedLength);
					}
					column.setDefualtValue(map.get(name));
					table.getColumns().add(column);
				}
				rtn.add(table);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (tableSet != null) {
					tableSet.close();
				}
				if (smt != null) {
					smt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return rtn;
	}

	public int executeBatch(String sql, Connection conn) throws Exception {
		String[] commands = sql.split(SQLBuilder.SQL_DELIMITER);
		return executeBatch(commands, conn);
	}

	public int executeBatch(String[] commands, Connection conn)
			throws Exception {
		int errors = 0;
		int commandCount = 0;
		Statement statement = null;
		// we tokenize the SQL along the delimiters, and we also make sure that
		// only delimiters
		// at the end of a line or the end of the string are used (row mode)
		try {
			if (conn != null && commands != null) {
				conn.setAutoCommit(false);
				statement = conn.createStatement();

				for (int i = 0; i < commands.length; i++) {
					String command = commands[i];

					if (command.trim().length() == 0) {
						continue;
					}
					commandCount++;
					log.info("executing SQL: " + command);
					int results = statement.executeUpdate(command);

					log.info("After execution, " + results
							+ " row(s) have been changed");
				}
				conn.commit();
				log.info("Executed " + commandCount + " SQL command(s) with "
						+ errors + " error(s)");
			} else if (conn != null) {
				log.debug("Exception: has no connection,nothing has done");
			} else if (commands != null) {
				log.debug("message: the commands is null,nothing has done");
			}
		} catch (SQLException ex) {
			if (conn != null) {
				conn.rollback();
			}
			throw ex;
		} catch (Exception e) {
			if (conn != null) {
				conn.rollback();
			}
			throw e;
		} finally {
			statement.close();
		}
		return errors;
	}

	public static String getSchema(Connection conn, String dbType) {
		if (dbType.equals(DBTYPE_ORACLE) || dbType.equals(DBTYPE_DB2)) {
			try {
				return conn.getMetaData().getUserName().trim().toUpperCase();
			} catch (SQLException sqle) {
				return "";
			}
		} else if (dbType.equals(DBTYPE_MYSQL)) {
			try {
				return conn.getCatalog();
			} catch (SQLException sqle) {
				return "";
			}

		} else if (dbType.equals(DBTYPE_MSSQL)) {
			try {
				ResultSet rs = conn.getMetaData().getSchemas();
				if (rs != null) {
					if (rs.next())
						return rs.getString(1).trim().toUpperCase();
				}
			} catch (SQLException sqle) {
				return "";
			}
		} else if (dbType.equals(DBTYPE_HSQLDB)) {
			return "public".toUpperCase();
		}
		return "";
	}
}
