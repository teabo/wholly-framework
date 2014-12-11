package com.whollyframework.ddltools.model;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * 
 * @author chris
 * @since 2011-12-20
 * 
 */
public class Table implements Cloneable{
	
	public final static String INC_TABLE_SUFFIX = "_INCREMENT";
	
	public final static String FIELD_INC_DATA_OPT = "inc_data_opt";
	
	public final static String FIELD_INC_DATA_ID = "inc_data_id";
	
	public final static String FIELD_INC_DATA_INPUTTIME = "inc_data_inputtime";
	
	public final static String FIELD_INC_DATA_STATUS = "inc_data_status";
	
	public final static String FIELD_INC_DATA_VERSION = "inc_data_version";
	
	public final static String FIELD_INC_DATA_BATCH_VERSION = "inc_data_batch_no";
	
	private String name;

	private String formName;

	private Collection<Column> columns = new ArrayList<Column>();
	
	private Collection<Index> indexs = new ArrayList<Index>();

	public Table(String name) {
		this.name = name;
	}

	public Collection<Column> getColumns() {
		return columns;
	}

	public void setColumns(Collection<Column> columns) {
		this.columns = columns;
	}

	public void addColumn(Column column) {
		columns.add(column);
	}

	public Collection<Index> getIndexs() {
		return indexs;
	}

	public void setIndexs(Collection<Index> indexs) {
		this.indexs = indexs;
	}

	public void addIndexs(Index index) {
		indexs.add(index);
	}
	
	
	public Index findIndex(String index_name){
		if (index_name != null)
		for (Iterator<Index> iterator = indexs.iterator(); iterator.hasNext();) {
			Index index = iterator.next();
			if (index_name.equalsIgnoreCase(index.getIndexName())){
				return index;
			}
		}
		return null;
	}
	
	public boolean isIndex(String field_name){
		for (Iterator<Index> iterator = indexs.iterator(); iterator.hasNext();) {
			Index index = iterator.next();
			if (index.findIndexColumn(field_name)!=null){
				return true;
			}
		}
		return false;
	}
	
	public Column findPrimaryKey(){
		for (Iterator<Column> iter = columns.iterator(); iter.hasNext();) {
			Column column = (Column) iter.next();
			if (column.isPrimaryKey()){
				return column;
			}
		}
		return null;
	}
	
	public void addIncColumns() throws DuplicateException{
		Column id = findColumn(FIELD_INC_DATA_ID);
		if (id==null){
			id = new Column("",FIELD_INC_DATA_ID,Types.VARCHAR);
			addColumn(id);
		}
		id.setTypeCode(Types.VARCHAR);
		id.setLength(40);
		Column primaryKey = findPrimaryKey();
		if (primaryKey==null){
			id.setPrimaryKey(true);
		}
		
		Column inputTime = findColumn(FIELD_INC_DATA_INPUTTIME);
		if (inputTime==null){
			inputTime = new Column("",FIELD_INC_DATA_INPUTTIME,Types.DATE);
			addColumn(inputTime);
		}
		inputTime.setTypeCode(Types.DATE);
		inputTime.setDefualtValue("sysdate");
		
		Column opt = findColumn(FIELD_INC_DATA_OPT);
		if (opt==null){
			opt = new Column("",FIELD_INC_DATA_OPT,Types.NUMERIC,2);
			addColumn(opt);
		}
		opt.setTypeCode(Types.NUMERIC);
		opt.setLength(2);
		opt.setFixedLength(0);
		opt.setDefualtValue("1");
		
		Column status = findColumn(FIELD_INC_DATA_STATUS);
		if (status==null){
			status = new Column("",FIELD_INC_DATA_STATUS,Types.NUMERIC,2);
			addColumn(status);
		}
		status.setTypeCode(Types.NUMERIC);
		status.setLength(2);
		status.setFixedLength(0);
		status.setDefualtValue("0");
		
		Column version = findColumn(FIELD_INC_DATA_VERSION);
		if (version==null){
			version = new Column("",FIELD_INC_DATA_VERSION,Types.NUMERIC,2);
			addColumn(version);
		}
		version.setTypeCode(Types.NUMERIC);
		version.setLength(2);
		version.setFixedLength(0);
		version.setDefualtValue("0");
		
		Column batch_version = findColumn(FIELD_INC_DATA_BATCH_VERSION);
		if (batch_version==null){
			batch_version = new Column("",FIELD_INC_DATA_BATCH_VERSION,Types.NUMERIC,2);
			addColumn(batch_version);
		}
		batch_version.setTypeCode(Types.NUMERIC);
		batch_version.setLength(2);
		batch_version.setFixedLength(0);
		batch_version.setDefualtValue("0");
		
	}
	
	public Column findColumn(String name) throws DuplicateException {
		ArrayList<Column> tmp = new ArrayList<Column>();

		for (Iterator<Column> iter = columns.iterator(); iter.hasNext();) {
			Column column = (Column) iter.next();
			if (column.getName().equalsIgnoreCase(name)) {
				tmp.add(column);
			}
		}

		if (tmp.size() > 1) {
			Column column = (Column) tmp.get(0);
			throw new DuplicateException("(" + column.getFieldName() + ")字段名重复");
		}

		if (tmp.size() > 0) {
			return (Column) tmp.get(0);
		}

		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public boolean equals(Object obj) {
		if (obj instanceof Table) {
			Table anTable = (Table) obj;
			if (this.getName().equalsIgnoreCase(anTable.getName())) {
				for (Iterator<Column> iterator = getColumns().iterator(); iterator.hasNext();) {
					try {
						Column column = (Column) iterator.next();
						Column anColumn = anTable.findColumn(column.getName());
						if (!column.equals(anColumn)) {
							return false;
						}
					} catch (Exception e) {
						return false;
					}
				}
				return true;
			}
		}

		return super.equals(obj);
	}

	public int hashCode() {
		return super.hashCode();
	}

	public Object clone() throws CloneNotSupportedException {
		Table target = (Table) super.clone();
		Collection<Column> columns = new ArrayList<Column>();
		for (Iterator<Column> iterator = getColumns().iterator(); iterator.hasNext();) {
			Column column = (Column) iterator.next();
			columns.add((Column) column.clone());
		}
		target.setColumns(columns);
		return target;
	}
	
}
