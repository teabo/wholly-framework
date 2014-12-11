package com.whollyframework.ddltools.model;

import java.sql.Types;

/**
 * 
 * @author chris
 * @since 2011-12-20
 * 
 */
public class Column implements Cloneable {
	/**
	 * 文本类型
	 */
	public static final String VALUE_TYPE_VARCHAR = "VALUE_TYPE_VARCHAR";

	/**
	 * 数字类型
	 */
	public static final String VALUE_TYPE_NUMBER = "VALUE_TYPE_NUMBER";

	/**
	 * 日期类型
	 */
	public static final String VALUE_TYPE_DATE = "VALUE_TYPE_DATE";

	/**
	 * BLOB类型
	 */
	public static final String VALUE_TYPE_BLOB = "VALUE_TYPE_BLOB";

	/**
	 * 大文本类型
	 */
	public static final String VALUE_TYPE_TEXT = "VALUE_TYPE_TEXT";
	
	/**
	 * 列ID
	 */
	private String id;

	/**
	 * 列名
	 */
	private String name;

	/**
	 * 列类型，取值参考java.sql.Types
	 */
	private int typeCode;

	/**
	 * 是否主键
	 */
	private boolean primaryKey;

	/**
	 * 是不允许为空
	 */
	private boolean notNull;

	/**
	 * 字段名称
	 */
	private String fieldName;

	/**
	 * 长度
	 */
	private int length;
	
	/**
	 * 小数位
	 */
	private int fixedLength;
	
	/**
	 * 默认值
	 */
	private String defualtValue;
	
	/**
	 * 是否为索引列
	 */
	private boolean isIndexColumn;

	/**
	 * 字段描述
	 */
	private String comment;
	
	public Column(String id, String fieldName, int typeCode) {
		this(id, fieldName, typeCode, 0, 0);
	}
	
	public Column(String id, String fieldName, int typeCode, int length) {
		this(id, fieldName, typeCode, length, 0);
	}

	public Column(String id, String fieldName, int typeCode, int length, int fixedLength) {
		this.id = id;
		this.name = fieldName;
		this.fieldName = fieldName;
		this.typeCode = typeCode;
		this.length = length;
		this.fixedLength = fixedLength;
	}

	public Column(String id, String fieldName, int typeCode, boolean isPrimaryKey, boolean isNotNull) {
		this.id = id;
		this.name = fieldName;
		this.fieldName = fieldName;
		this.typeCode = typeCode;
		this.primaryKey = isPrimaryKey;
		this.notNull = isNotNull;
	}
	
	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getFixedLength() {
		return fixedLength;
	}

	public void setFixedLength(int fixedLength) {
		this.fixedLength = fixedLength;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public String getFieldSQLType(String sqlTypeName) {
		StringBuffer fieldSQLType = new StringBuffer();
		fieldSQLType.append(sqlTypeName);
		switch (typeCode) {
		
		case Types.NUMERIC:
		case Types.DECIMAL:
			if (length>0){
				fieldSQLType.append("(").append(length).append(",").append(fixedLength).append(")");
			} else {
				fieldSQLType.append("(22,5)");
			}
			break;
			
		case Types.VARCHAR:
			if (length>0){
				fieldSQLType.append("(").append(length).append(")");
			} else {
				fieldSQLType.append("(254)");
			}
			break;
			
		default:
			break;
		}
		return fieldSQLType.toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(int typeCode) {
		this.typeCode = typeCode;
	}

	public boolean isPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public String toString() {
		return name;
	}

	public boolean equals(Object obj) {
		if(obj == null)return false;
		if(!(obj instanceof Column))return false;
		Column anColumn = (Column) obj;

		if (this.getName().equalsIgnoreCase(anColumn.getName()) 
				&& this.getTypeCode() == anColumn.getTypeCode()) {
			return true;
		} else {
			return super.equals(obj);
		}
	}
	
	public int hashCode(){
		return super.hashCode();
	}

	/**
	 * 比较新旧Column是否兼容
	 * 
	 * @param column
	 *            要比较的列
	 * @return true or false
	 */
	public boolean isCompatible(Column anColumn) {
		boolean rtn = false;

		int anotherTypeCode = anColumn.getTypeCode();

		switch (typeCode) {
		case Types.VARCHAR:
			switch (anotherTypeCode) {
			case Types.CLOB:
				rtn = true;
				break;
			default:
				rtn = false;
			}
			break;

		case Types.NUMERIC:
			switch (anotherTypeCode) {
			case Types.CLOB:
				rtn = true;
				break;
			default:
				rtn = false;
			}
			break;

		case Types.DATE:
			switch (anotherTypeCode) {
			case Types.CLOB:
				rtn = true;
				break;
			default:
				rtn = false;
			}
			break;

		case Types.CLOB:
			switch (anotherTypeCode) {
			case Types.CLOB:
				rtn = true;
				break;
			default:
				rtn = false;
			}
			break;

		default:
			break;
		}
		return rtn;
	}

	public boolean isNotNull() {
		return notNull;
	}

	public void setNotNull(boolean notNull) {
		this.notNull = notNull;
	}

	public String getDefualtValue() {
		return defualtValue;
	}

	public void setDefualtValue(String defualtValue) {
		this.defualtValue = defualtValue;
	}

	public boolean isIndexColumn() {
		return isIndexColumn;
	}

	public void setIndexColumn(boolean isIndexColumn) {
		this.isIndexColumn = isIndexColumn;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
