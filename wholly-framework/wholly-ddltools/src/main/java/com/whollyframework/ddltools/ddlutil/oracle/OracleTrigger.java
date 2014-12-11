package com.whollyframework.ddltools.ddlutil.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import com.whollyframework.ddltools.ddlutil.AbstractTrigger;
import com.whollyframework.ddltools.ddlutil.DBUtils;
import com.whollyframework.ddltools.model.Column;
import com.whollyframework.ddltools.model.Table;

public class OracleTrigger extends AbstractTrigger {

	public OracleTrigger(Connection conn) {
		super(conn);
		this.schema = DBUtils.getSchema(conn, DBUtils.DBTYPE_ORACLE);
	}

	@Override
	public void dropTrigger(String triggerName) throws SQLException {
		StringBuffer _writer = new StringBuffer();
		_writer.append("DROP TRIGGER " ).append( triggerName);
		_writer.append(SQL_DELIMITER);
		evaluateSQL(_writer.toString());
	}

	@Override
	public void createInsertTrigger(Table table) throws SQLException {
		String tableName = getTableFullName(table); 
		String Increment_tableName = tableName + Table.INC_TABLE_SUFFIX;
		String triggerName = tableName + INSERT_TRIGGER_SUFFIX;
		StringBuffer colBuf = new StringBuffer();
		StringBuffer vBuf = new StringBuffer();
		Collection<Column> cols = table.getColumns();
		
		colBuf.append(Table.FIELD_INC_DATA_ID).append(",");
		colBuf.append(Table.FIELD_INC_DATA_INPUTTIME).append(",");
		colBuf.append(Table.FIELD_INC_DATA_OPT).append(",");
		vBuf.append("sys_guid(),");
		vBuf.append("sysdate,");
		vBuf.append("1,");
		
		for (Iterator<Column> iterator = cols.iterator(); iterator.hasNext();) {
			Column column = iterator.next();
			colBuf.append(column.getName());
			vBuf.append(":new.").append(column.getName());
			if (iterator.hasNext()){
				colBuf.append(",");
				vBuf.append(",");
			}
		}
		StringBuffer _writer = new StringBuffer();
		_writer.append("CREATE OR REPLACE TRIGGER " ).append( triggerName);
		_writer.append(" AFTER INSERT ON " ).append( tableName);
		_writer.append(" FOR EACH ROW  BEGIN " );
		_writer.append(" INSERT INTO " ).append( Increment_tableName).append("(");
		_writer.append(colBuf).append(") VALUES (").append(vBuf);
		_writer.append(")").append(SQL_DELIMITER);
		
		_writer.append(" END ").append(triggerName).append(SQL_DELIMITER);
		evaluateSQL(_writer.toString());
	}

	@Override
	public void createUpdateTrigger(Table table) throws SQLException {
		String tableName = getTableFullName(table); 
		String Increment_tableName = tableName + Table.INC_TABLE_SUFFIX;
		String triggerName = tableName + UPDATE_TRIGGER_SUFFIX;
		StringBuffer colBuf = new StringBuffer();
		StringBuffer vBuf = new StringBuffer();
		Collection<Column> cols = table.getColumns();
		
		colBuf.append(Table.FIELD_INC_DATA_ID).append(",");
		colBuf.append(Table.FIELD_INC_DATA_INPUTTIME).append(",");
		colBuf.append(Table.FIELD_INC_DATA_OPT).append(",");
		vBuf.append("sys_guid(),");
		vBuf.append("sysdate,");
		vBuf.append("2,");
		
		for (Iterator<Column> iterator = cols.iterator(); iterator.hasNext();) {
			Column column = iterator.next();
			colBuf.append(column.getName());
			vBuf.append(":new.").append(column.getName());
			if (iterator.hasNext()){
				colBuf.append(",");
				vBuf.append(",");
			}
		}
		StringBuffer _writer = new StringBuffer();
		_writer.append("CREATE OR REPLACE TRIGGER " ).append( triggerName);
		_writer.append(" AFTER UPDATE ON " ).append( tableName);
		_writer.append(" FOR EACH ROW  BEGIN " );
		_writer.append(" INSERT INTO " ).append( Increment_tableName).append("(");
		_writer.append(colBuf).append(") VALUES (").append(vBuf);
		_writer.append(")").append(SQL_DELIMITER);
		
		_writer.append(" END ").append(triggerName).append(SQL_DELIMITER);
		evaluateSQL(_writer.toString());

	}

	@Override
	public void createDeleteTrigger(Table table) throws SQLException {
		String tableName = getTableFullName(table); 
		String Increment_tableName = tableName + Table.INC_TABLE_SUFFIX;
		String triggerName = tableName + DELETE_TRIGGER_SUFFIX;
		StringBuffer colBuf = new StringBuffer();
		StringBuffer vBuf = new StringBuffer();
		Collection<Column> cols = table.getColumns();
		
		colBuf.append(Table.FIELD_INC_DATA_ID).append(",");
		colBuf.append(Table.FIELD_INC_DATA_INPUTTIME).append(",");
		colBuf.append(Table.FIELD_INC_DATA_OPT).append(",");
		vBuf.append("sys_guid(),");
		vBuf.append("sysdate,");
		vBuf.append("3,");
		
		for (Iterator<Column> iterator = cols.iterator(); iterator.hasNext();) {
			Column column = iterator.next();
			colBuf.append(column.getName());
			vBuf.append(":old.").append(column.getName());
			if (iterator.hasNext()){
				colBuf.append(",");
				vBuf.append(",");
			}
		}
		StringBuffer _writer = new StringBuffer();
		_writer.append("CREATE OR REPLACE TRIGGER " ).append( triggerName);
		_writer.append(" BEFORE DELETE ON " ).append( tableName);
		_writer.append(" FOR EACH ROW  BEGIN " );
		_writer.append(" INSERT INTO " ).append( Increment_tableName).append("(");
		_writer.append(colBuf).append(") VALUES (").append(vBuf);
		_writer.append(")").append(SQL_DELIMITER);
		
		_writer.append(" END ").append(triggerName).append(SQL_DELIMITER);
		evaluateSQL(_writer.toString());
	}

	@Override
	public void createTrigger(Table table) throws SQLException {
		createInsertTrigger(table);
		createUpdateTrigger(table);
		createDeleteTrigger(table);
	}

	@Override
	public void dropTrigger(Table table) throws SQLException {
		String tableName = getTableFullName(table); 
		String insertTriggerName = tableName + INSERT_TRIGGER_SUFFIX;
		String updateTriggerName = tableName + UPDATE_TRIGGER_SUFFIX;
		String deleteTriggerName = tableName + DELETE_TRIGGER_SUFFIX;
		dropTrigger(insertTriggerName);
		dropTrigger(updateTriggerName);
		dropTrigger(deleteTriggerName);
		
	}

}
