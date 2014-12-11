package com.whollyframework.ddltools.ddlutil.oracle;

import java.sql.Types;

import com.whollyframework.ddltools.ddlutil.SQLBuilder;
import com.whollyframework.ddltools.model.Column;
import com.whollyframework.ddltools.model.Table;

/**
 * 
 * @author Chris
 * 
 */
public class OracleBuilder extends SQLBuilder {
	public OracleBuilder() {
		registerColumnType(Types.VARCHAR, "VARCHAR2");
		registerColumnType(Types.LONGVARCHAR, "VARCHAR2(4000)");
		registerColumnType(Types.NUMERIC, "NUMBER");
		registerColumnType(Types.DECIMAL, "NUMBER");
		registerColumnType(Types.INTEGER, "NUMBER(10,0)");
		registerColumnType(Types.BIT, "NUMBER(1,0)");
		registerColumnType(Types.DATE, "DATE");
		registerColumnType(Types.TIMESTAMP, "TIMESTAMP(6)");
		registerColumnType(Types.CLOB, "CLOB");
		registerColumnType(Types.BLOB, "BLOB");
	}

	private void registerColumnType(int code, String name) {
		typeNames.put(Integer.valueOf(code), name);
	}

	public void findTable(Table table) {
		String tableName = table.getName().toUpperCase();
		findTable(tableName);
	}

	public void getTableDatas(Table table) {
		String tableName = getTableFullName(table);
		_writer.append("SELECT * FROM " ).append(tableName);
		_writer.append(SQL_DELIMITER);
	}

	public void getColumnDatas(Table table, Column column) {

		_writer.append("SELECT * FROM ").append( table.getName().toUpperCase());
		_writer.append("  WHERE ISTMP = 0 AND  ").append( column.getName().toUpperCase());
		_writer.append(" is not null");
		_writer.append(SQL_DELIMITER);
	}

	public void findColumn(Table table, Column Column) {

		_writer.append("SELECT * FROM USER_TAB_COLS WHERE TABLE_NAME ='").append( table.getName().toUpperCase());
		_writer.append("' AND  COLUMN_NAME ='").append( Column.getName().toUpperCase());
		_writer.append("'");
		_writer.append(SQL_DELIMITER);

	}

	public void findTable(String tableName) {
		_writer.append("SELECT * FROM USER_TABLES WHERE TABLE_NAME ='" ).append(tableName);
		_writer.append("'");
		_writer.append(SQL_DELIMITER);
	}

	public String getTableFullName(Table table) {
		if (schema != null && schema.trim().length() > 0) {
			String tableFullName = schema + "." + table.getName();
			return tableFullName;
		}
		return table.getName();
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

		_writer.append("ALTER TABLE ").append(tableFullName);
		_writer.append(" RENAME COLUMN ").append(sourceColumn.getName());
		_writer.append(" TO ").append(targetColumn.getName());
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
		String defaultValue = targetColumn.getDefualtValue();
		if (defaultValue !=null){
			if (typeCode == Types.VARCHAR || typeCode == Types.LONGVARCHAR ||typeCode == Types.CLOB){
				if (targetColumn.isPrimaryKey()){
					_writer.append(" default ").append(defaultValue);
				} else {
					_writer.append(" default '").append(defaultValue).append("'");
				}
			} else {
				_writer.append(" default ").append(defaultValue);
			}
		}
		_writer.append(SQL_DELIMITER);
	}
}
