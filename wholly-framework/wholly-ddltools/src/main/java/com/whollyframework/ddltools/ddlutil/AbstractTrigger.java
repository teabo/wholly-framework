package com.whollyframework.ddltools.ddlutil;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.whollyframework.ddltools.model.Table;

public abstract class AbstractTrigger {

	protected static Logger log = Logger.getLogger(AbstractTrigger.class);

	protected Connection conn;

	public final static String SQL_DELIMITER = ";";

	public final static String INSERT_TRIGGER_SUFFIX = "_INSERT_TRIGGER";

	public final static String UPDATE_TRIGGER_SUFFIX = "_UPDATE_TRIGGER";

	public final static String DELETE_TRIGGER_SUFFIX = "_DELETE_TRIGGER";

	protected String schema;

	public AbstractTrigger(Connection conn) {
		this.conn = conn;
	}

	/**
	 * @param schema
	 *            the schema to set
	 * @uml.property name="schema"
	 */
	public void setSchema(String schema) {
		this.schema = schema;
	}

	protected String getTableFullName(Table table) {

//		if (schema != null && schema.trim().length() > 0) {
//			StringBuffer buf = new StringBuffer();
//			buf.append(schema).append(".").append(table.getName());
//			return buf.toString().toUpperCase();
//		}
		return table.getName().toUpperCase();
	}

	protected String getTableFullName(String tableName) {
//		if (schema != null && schema.trim().length() > 0) {
//			StringBuffer buf = new StringBuffer();
//			buf.append(schema).append(".").append(tableName);
//			return buf.toString().toUpperCase();
//		}
		return tableName.toUpperCase();
	}

	/**
	 * 删除触发器
	 * 
	 * @param triggerName
	 * @throws SQLException
	 */
	public abstract void dropTrigger(String triggerName) throws SQLException;
	
	/**
	 * 删除触发器
	 * 
	 * @param triggerName
	 * @throws SQLException
	 */
	public abstract void dropTrigger(Table table) throws SQLException;
	
	/**
	 * 创建记录触发器
	 * 
	 * @param triggerName
	 * @throws SQLException
	 */
	public abstract void createTrigger(Table table) throws SQLException;

	/**
	 * 创建插入记录触发器
	 * 
	 * @param triggerName
	 * @throws SQLException
	 */
	public abstract void createInsertTrigger(Table table) throws SQLException;

	/**
	 * 创建更新记录触发器
	 * 
	 * @param triggerName
	 * @throws SQLException
	 */
	public abstract void createUpdateTrigger(Table table) throws SQLException;

	/**
	 * 创建删除记录触发器
	 * 
	 * @param triggerName
	 * @throws SQLException
	 */
	public abstract void createDeleteTrigger(Table table) throws SQLException;

	public int evaluateSQL(String sql)
			throws SQLException {
		Statement statement = null;
		int errors = 0;
		int commandCount = 0;

		try {
			statement = conn.createStatement();
			log.info("executing SQL: " + sql);

			int results = statement.executeUpdate(sql);

			log.info("After execution, " + results
					+ " row(s) have been changed");
			log.info("Executed " + commandCount + " SQL command(s) with "
					+ errors + " error(s)");

		} catch (SQLException ex) {
			log.warn("SQL Command " + sql + " failed with: " + ex.getMessage());
			throw ex;
		} finally {
			closeStatement(statement);
		}
		return errors;
	}

	protected void closeStatement(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (Exception e) {
				log.info(
						"Ignoring exception that occurred while closing statement",
						e);
			}
		}
	}
}
