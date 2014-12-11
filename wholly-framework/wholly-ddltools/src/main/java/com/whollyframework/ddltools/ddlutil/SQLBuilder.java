package com.whollyframework.ddltools.ddlutil;

import com.whollyframework.ddltools.model.Column;
import com.whollyframework.ddltools.model.Table;

import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 
 * @author chris
 * @since 2011-12-20
 * 
 */
public abstract class SQLBuilder {
	public final static String SQL_DELIMITER = ";";

	/**
	 * @uml.property name="typeNames"
	 * @uml.associationEnd qualifier="key:java.lang.Object java.lang.String"
	 */
	protected Map<Object, Object> typeNames = new HashMap<Object, Object>();

	protected String schema;

	protected StringBuffer _writer;

	public abstract void findTable(String tableName);

	public abstract void findTable(Table table);

	public abstract void getTableDatas(Table table);

	public abstract void getColumnDatas(Table table, Column column);

	public abstract void findColumn(Table table, Column column);

	/**
	 * @param schema
	 *            the schema to set
	 * @uml.property name="schema"
	 */
	public void setSchema(String schema) {
		this.schema = schema;
	}

	protected String getSqlTypeName(Integer code) {
		return (String) typeNames.get(code);
	}

	protected String getTableFullName(Table table) {
		if (schema != null && schema.trim().length() > 0) {
			String tableFullName = schema + "." + table.getName();
			return tableFullName;
		}
		return table.getName();
	}

	protected String getTableFullName(String tableName) {
		String rtn = tableName;
		if (schema != null && schema.trim().length() > 0) {
			String tableFullName = schema + "." + tableName;
			rtn = tableFullName;
		}
		return rtn.toUpperCase();
	}

	public void setWriter(StringBuffer writer) {
		this._writer = writer;
	}

	public void createTableAsSelect(Table table, Table changedTable, boolean dropFirst) {
		String tableFullName = getTableFullName(table);

		_writer = new StringBuffer();
		_writer.append("create table " ).append( tableFullName);
		_writer.append(" AS SELECT ").append(getColumnNameString(table)).append(" FROM " + changedTable.getName());
		_writer.append(SQL_DELIMITER);
	}
	
	private String getColumnNameString(Table table) {
		StringBuffer buffer = new StringBuffer();

		Collection<Column> columns = table.getColumns();
		if (columns != null && !columns.isEmpty()) {
			for (Iterator<Column> iter = columns.iterator(); iter.hasNext();) {
				Column column = (Column) iter.next();

				buffer.append(column.getName());
				buffer.append(",");
			}
			buffer.deleteCharAt(buffer.length() - 1);
		}

		return buffer.toString();
	}
	
	private String getColumnString(Table table) {
		StringBuffer buffer = new StringBuffer();

		Collection<Column> columns = table.getColumns();
		if (columns != null && !columns.isEmpty()) {
			for (Iterator<Column> iter = columns.iterator(); iter.hasNext();) {
				Column column = (Column) iter.next();
				int code = column.getTypeCode();
				String sqlTypeName = column.getFieldSQLType(getSqlTypeName(code));
				buffer.append(column.getName());
				buffer.append(" ").append(sqlTypeName);
				String defaultValue = column.getDefualtValue();
				if (defaultValue !=null && !"".equals(defaultValue.trim())){
					if (code == Types.VARCHAR || code == Types.LONGVARCHAR ||code == Types.CLOB){
						if (column.isPrimaryKey()){
							buffer.append(" DEFAULT ").append(defaultValue);
						} else {
							buffer.append(" DEFAULT '").append(defaultValue).append("'");
						}
					} else {
						buffer.append(" DEFAULT ").append(defaultValue);
					}
				}
				if (column.isPrimaryKey()) {
					buffer.append(" NOT NULL PRIMARY KEY");
				}
				buffer.append(",");
			}
			buffer.deleteCharAt(buffer.length() - 1);
		}

		return buffer.toString();
	}

	/**
	 * create table
	 * 
	 * @param table
	 */
	public void createTable(Table table, boolean dropFirst) {
		String tableFullName = getTableFullName(table);

		_writer = new StringBuffer();

		_writer.append("create table ").append( tableFullName).append( "(");
		_writer.append(getColumnString(table));
		_writer.append(")");
		_writer.append(SQL_DELIMITER);
	}

	/**
	 * add column
	 * 
	 * @param changedTable
	 * @param targetColumn
	 */
	public void addColumn(Table changedTable, Column targetColumn) {
		String tableFullName = getTableFullName(changedTable);
		String colName = targetColumn.getName();
		Integer typeCode = Integer.valueOf(targetColumn.getTypeCode());
		String sqlTypeName = targetColumn.getFieldSQLType(getSqlTypeName(typeCode));

		_writer.append("alter table ").append(tableFullName).append(" ");
		_writer.append("add ").append(colName).append(" ").append(sqlTypeName);
		_writer.append(SQL_DELIMITER);
	}

	/**
	 * transfer data from source column to target column
	 * 
	 * @param changedTable
	 * @param sourceColumn
	 * @param targetColumn
	 */
	public void columnDataCopy(Table changedTable, Column sourceColumn, Column targetColumn) {
		String tableFullName = getTableFullName(changedTable);

		_writer.append("update ").append(tableFullName).append(" ");
		_writer.append("set ").append(targetColumn).append(" = ").append(sourceColumn);
		_writer.append(SQL_DELIMITER);
	}

	/**
	 * modify column stored data type
	 * 
	 * @param changedTable
	 * @param targetColumn
	 */
	public void modifyColumnDataType(Table changedTable, Column targetColumn) {
		String tableFullName = getTableFullName(changedTable);
		String targetColumnName = targetColumn.getName();
		String targetTypeName = targetColumn.getFieldSQLType(getSqlTypeName(targetColumn.getTypeCode()));

		_writer.append("alter table ").append(tableFullName);
		_writer.append(" modify ").append(targetColumnName);
		_writer.append(" ").append(targetTypeName);
		_writer.append(SQL_DELIMITER);
	}

	/**
	 * rename column
	 * 
	 * @param changedTable
	 * @param sourceColumn
	 * @param targetColumn
	 */
	public void columnRename(Table changedTable, Column sourceColumn, Column targetColumn) {
		String tableFullName = getTableFullName(changedTable);

		_writer.append("alter table ").append(tableFullName);
		_writer.append(" rename column ").append( sourceColumn.getName());
		_writer.append(" to ").append( targetColumn.getName());
		_writer.append(SQL_DELIMITER);
	}

	/**
	 * drop column
	 * 
	 * @param changedTable
	 * @param sourceColumn
	 */
	public void dropColumn(Table changedTable, Column sourceColumn) {
		String tableFullName = getTableFullName(changedTable);

		_writer.append("alter table ").append( tableFullName);
		_writer.append(" drop column ").append( sourceColumn.getName());
		_writer.append(SQL_DELIMITER);
	}

	/**
	 * drop table
	 * 
	 * @param table
	 */
	public void dropTable(Table table) {
		_writer.append("drop table ").append( getTableFullName(table));
		_writer.append(SQL_DELIMITER);
	}

	/**
	 * transfer data from source table to target table
	 * 
	 * @param sourceTable
	 * @param targetTable
	 */
	public void tableDataCopy(Table sourceTable, Table targetTable) {
		String sourceTableName = getTableFullName(sourceTable);
		String targetTableName = getTableFullName(targetTable);

		_writer.append("insert into ").append(targetTableName);
		_writer.append(" select * from " ).append( sourceTableName);
		_writer.append(SQL_DELIMITER);
	}

	/**
	 * rename table
	 * 
	 * @param changedTable
	 * @param targetTable
	 * @return
	 */
	public void tableRename(Table changedTable, Table targetTable) {
		String changedTableName = getTableFullName(changedTable);
		String targetTableName = getTableFullName(targetTable);

		_writer.append("alter table ").append( changedTableName);
		_writer.append(" rename to " ).append( targetTableName);
		_writer.append(SQL_DELIMITER);

	}

	public String getSQL() {
		return _writer.toString();
	}

}
