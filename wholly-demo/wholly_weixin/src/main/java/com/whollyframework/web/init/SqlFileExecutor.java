package com.whollyframework.web.init;

import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.whollyframework.base.model.PersistenceUtils;

/**
 * 读取 SQL 脚本并执行
 * 
 * @author Unmi
 */
public class SqlFileExecutor {

	private final static Logger LOG = LoggerFactory.getLogger(SqlFileExecutor.class);

	/**
	 * 读取 SQL 文件，获取 SQL 语句
	 * 
	 * @param sqlFile
	 *            SQL 脚本文件
	 * @return List<sql> 返回所有 SQL 语句的 List
	 * @throws Exception
	 */
	private List<String> loadSql(String sqlFile, String split) throws Exception {
		List<String> sqlList = new ArrayList<String>();
		InputStream sqlFileIn = null;
		try {
			URL propsUrl = Thread.currentThread().getContextClassLoader().getResource(sqlFile);
			sqlFileIn = propsUrl.openStream();

			StringBuffer sqlSb = new StringBuffer();
			byte[] buff = new byte[1024];
			int byteRead = 0;
			while ((byteRead = sqlFileIn.read(buff)) != -1) {
				sqlSb.append(new String(buff, 0, byteRead));
			}

			// Windows 下换行是 /r/n, Linux 下是 /n
			String[] sqlArr = sqlSb.toString().split(split);
			for (int i = 0; i < sqlArr.length; i++) {
				String sql = sqlArr[i].trim();
				if (!sql.equals("")) {
					sqlList.add(sql);
				}
			}
			return sqlList;
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		} finally {
			if (sqlFileIn != null)
				sqlFileIn.close();
		}
	}

	/**
	 * 传入连接来执行 SQL 脚本文件，这样可与其外的数据库操作同处一个事物中
	 * 
	 * @param conn
	 *            传入数据库连接
	 * @param sqlFile
	 *            SQL 脚本文件
	 * @throws Exception
	 */
	public void executeSQLFile(Connection conn, String sqlFile) throws Exception {
		executeSQLFile(conn, sqlFile, "(;\\s*\r\n)|(;\\s*\n)");
	}
	
	public void executeSQLFile(Connection conn, String sqlFile, String split) throws Exception {
		Statement stmt = null;
		List<String> sqlList = loadSql(sqlFile, split);
		try {
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			for (String sql : sqlList) {
				stmt.addBatch(sql);
				LOG.debug("Add Batch SQL: " + sql);
			}
			int[] rows = stmt.executeBatch();
			LOG.debug("Row count:" + Arrays.toString(rows));
			conn.commit();
		} catch (Exception ex) {
			conn.rollback();
			throw ex;
		} finally {
			try {
				PersistenceUtils.closeStatement(stmt);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public long query(Connection conn, String sql, ArrayList<Object> values) throws Exception {
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			statement = getStatement(conn, sql, values);
			rs = statement.executeQuery();

			if (rs.next()) {
				return rs.getLong(1);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			PersistenceUtils.closeResultSet(rs);
			PersistenceUtils.closeStatement(statement);
		}

		return 0;
	}

	private PreparedStatement getStatement(Connection connection, String sql, ArrayList<Object> values)
			throws Exception {
		PreparedStatement stat = null;
		try {
			LOG.debug(sql);
			stat = connection.prepareStatement(sql);
			int amount = 0;
			if (values != null) {
				StringBuilder logValues = new StringBuilder();
				logValues.append("Values [");
				for (Object value : values) {
					if (amount > 0) {
						logValues.append(", ");
					}
					logValues.append(value);
					if (value instanceof Date) {
						stat.setObject(++amount, new Timestamp(((Date) value).getTime()));
					} else {
						stat.setObject(++amount, value);
					}
				}
				logValues.append("]");
				LOG.debug(logValues.toString());
			}
		} catch (SQLException e) {
			PersistenceUtils.closeStatement(stat);
			throw e;
		}
		return stat;
	}

	public static void main(String[] args) {
		String filePath = "script/db-functions-create.sql";
		if (args != null && args.length > 0) {
			filePath = args[0];
		}

		List<String> sqlList;
		try {
			sqlList = new SqlFileExecutor().loadSql(filePath, "(##split##;\\s*\r\n)|(##split##;\\s*\n)");
			System.out.println("size:" + sqlList.size());
			for (String sql : sqlList) {
				System.out.println("line: ");
				System.out.println(sql);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
