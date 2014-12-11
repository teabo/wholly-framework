package com.whollyframework.ddltools.ddlutil;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.whollyframework.ddltools.alteration.AddColumnChange;
import com.whollyframework.ddltools.alteration.AddTableChange;
import com.whollyframework.ddltools.alteration.ColumnDataTypeChange;
import com.whollyframework.ddltools.alteration.ColumnRenameChange;
import com.whollyframework.ddltools.alteration.DropColumnChange;
import com.whollyframework.ddltools.alteration.DropTableChange;
import com.whollyframework.ddltools.alteration.ModelChange;
import com.whollyframework.ddltools.alteration.TableRenameChange;
import com.whollyframework.ddltools.model.Column;
import com.whollyframework.ddltools.model.DuplicateException;
import com.whollyframework.ddltools.model.Table;

/**
 * 
 * @author chris
 * @since 2013-9-16
 * 
 */
public class ChangeLog {
	private final static Logger log = Logger.getLogger(ChangeLog.class);

	private Collection<ModelChange> changes = new ArrayList<ModelChange>();

	public final static int FULL_COMPARE_TYPE = 10;

	public final static int ADD_COMPARE_TYPE = 1;

	public final static int ALTER_COMPARE_TYPE = 2;

	public final static int DROP_COMPARE_TYPE = 4;

	/**
	 * 新表格
	 */
	private Table _newTable;

	/**
	 * 旧表格
	 */
	private Table _oldTable;

	public void compare(int compareType) throws Exception {
		compare(_newTable, _oldTable, compareType);
	}

	public void compare(Table newTable, Table oldTable, int compareType)
			throws Exception {

		if (newTable != null) {
			// 1.旧表格存在，且相同则不新建
			if (oldTable == null
					&& ((compareType & ADD_COMPARE_TYPE) == ADD_COMPARE_TYPE || (compareType & FULL_COMPARE_TYPE) == FULL_COMPARE_TYPE)) {// 2.
				// 新增表格
				ModelChange addTableChange = new AddTableChange(newTable);
				changes.add(addTableChange);

				log.info("Add table create change,the new table is: "
						+ newTable.getName());
			} else {

				if (oldTable != null
						&& ((compareType & ALTER_COMPARE_TYPE) == ALTER_COMPARE_TYPE || (compareType & FULL_COMPARE_TYPE) == FULL_COMPARE_TYPE)) {
					// 1.表单名称，且数据库名称都不相同
					if (oldTable != null
							&& !newTable.getName().equals(oldTable.getName())) {
						ModelChange tableRenameChange = new TableRenameChange(
								oldTable, newTable);

						changes.add(tableRenameChange);
						log.info("Add table rename change,new name is: "
								+ newTable.getName());
					}

					compareFields(newTable, oldTable, compareType);
				}
			}
		} else {

			if (oldTable != null
					&& ((compareType & DROP_COMPARE_TYPE) == DROP_COMPARE_TYPE || (compareType & FULL_COMPARE_TYPE) == FULL_COMPARE_TYPE)) {
				// Add DropTableChange
				ModelChange dropTableChange = new DropTableChange(oldTable);
				changes.add(dropTableChange);
				log.info("Add table drop change,the drop table is: "
						+ oldTable.getName());
			}
		}
		filter();
	}

	protected void compareFields(Table newTable, Table oldTable, int compareType)
			throws Exception {
		compareFieldsToAddOrModify(newTable, oldTable, compareType);
		if ((compareType & DROP_COMPARE_TYPE) == DROP_COMPARE_TYPE
				|| (compareType & FULL_COMPARE_TYPE) == FULL_COMPARE_TYPE)
			compareFieldsToDrop(newTable, oldTable);
	}

	/**
	 * 以新表中的字段为基准,与旧表的字段做比较,查出需要新增或者修改的地方
	 * 
	 * @param newTable
	 *            新表
	 * @param oldTable
	 *            旧表
	 * @throws Exception
	 */
	protected void compareFieldsToAddOrModify(Table newTable, Table oldTable,
			int compareType) throws Exception {
		Collection<Column> newFields = newTable.getColumns();
		for (Iterator<Column> iter = newFields.iterator(); iter.hasNext();) {
			Column targetColumn = (Column) iter.next();
			Column orgColumn = oldTable.findColumn(targetColumn.getName());

			if (orgColumn == null
					&& ((compareType & ADD_COMPARE_TYPE) == ADD_COMPARE_TYPE || (compareType & FULL_COMPARE_TYPE) == FULL_COMPARE_TYPE)) { // 数据库字段不存在
				// 1.Add AddColumnChange, if the oldfield not found
				ModelChange addColumnChange = new AddColumnChange(newTable,
						targetColumn);
				changes.add(addColumnChange);
				log.info("Add column add change, new column is: "
						+ targetColumn.getName());
			} else {
				// 1.Add ColumnDataTypeChange
				if (((compareType & ALTER_COMPARE_TYPE) == ALTER_COMPARE_TYPE || (compareType & FULL_COMPARE_TYPE) == FULL_COMPARE_TYPE)) {
					String value = targetColumn.getDefualtValue();
					String oldValue = orgColumn.getDefualtValue();
					if (orgColumn != null) {
						boolean flag = false;
						if ((targetColumn.getTypeCode() != orgColumn
								.getTypeCode() && !(targetColumn.getTypeCode() == Types.DATE && orgColumn
								.getTypeCode() == Types.TIMESTAMP))) {
							flag = true;
						} else if (targetColumn.getTypeCode() == Types.NUMERIC
								&& (targetColumn.getLength() != orgColumn
										.getLength() || targetColumn
										.getFixedLength() != orgColumn
										.getFixedLength())) {
							flag = true;
						} else if ((targetColumn.getTypeCode() == Types.VARCHAR || targetColumn
								.getTypeCode() == Types.LONGVARCHAR)
								&& (targetColumn.getLength() != orgColumn
										.getLength())) {
							flag = true;
						} else if ((value != null && value.length()>0 && !value.equals(oldValue) && !("'"+value+"'").equals(oldValue))) {
							flag = true;
						}
						if (flag) {
							ModelChange columnDataTypeChange = new ColumnDataTypeChange(
									newTable, orgColumn, targetColumn);
							changes.add(columnDataTypeChange);
							log.info("Add column data type change , Alter column is: "
									+ targetColumn.getName());
						}
					}

					// 2.Add ColumnRenameChange, if the name of two fields not
					// the same
					if (orgColumn != null
							&& !targetColumn.getName().equalsIgnoreCase(
									orgColumn.getName())) {
						ModelChange columnRenameChange = new ColumnRenameChange(
								newTable, orgColumn, targetColumn);
						changes.add(columnRenameChange);
						log.info("Add column rename change, Alter name is: "
								+ targetColumn.getName());
					}
				}
			}
		}
	}

	/**
	 * 以旧表的字段为基准,与新表的字段做比较,查出需要删除的字段
	 * 
	 * @param oldTable
	 *            旧表
	 * @param newTable
	 *            新表
	 * @throws DuplicateException
	 */
	protected void compareFieldsToDrop(Table newTable, Table oldTable)
			throws DuplicateException {
		if (newTable == null) {
			return;
		}

		Collection<Column> oldFields = oldTable.getColumns();
		for (Iterator<Column> iter = oldFields.iterator(); iter.hasNext();) {
			Column sourceColumn = (Column) iter.next();
			Column targetColumn = newTable.findColumn(sourceColumn.getName());

			if (targetColumn == null) {
				ModelChange dropColumnChange = new DropColumnChange(newTable,
						sourceColumn);
				changes.add(dropColumnChange);
				log.info("Add column drop change, drop column is: "
						+ sourceColumn.getName());
			}
		}
	}

	/**
	 * 过滤，将添加和删除(同一个字段时)等消
	 */
	public void filter() {
		Collection<ModelChange> rtn = new ArrayList<ModelChange>();
		outer: for (Iterator<ModelChange> iterator = changes.iterator(); iterator
				.hasNext();) {
			ModelChange change = (ModelChange) iterator.next();
			if (change instanceof AddColumnChange) {
				String name = ((AddColumnChange) change).getTargetColumn()
						.getName();
				int typeCode = ((AddColumnChange) change).getTargetColumn()
						.getTypeCode();
				for (Iterator<ModelChange> iterator2 = changes.iterator(); iterator2
						.hasNext();) {
					ModelChange anChange = (ModelChange) iterator2.next();
					if (anChange instanceof DropColumnChange) {
						String anName = ((DropColumnChange) anChange)
								.getSourceColumn().getName();
						int anTypeCode = ((DropColumnChange) anChange)
								.getSourceColumn().getTypeCode();
						if (name.equals(anName) && typeCode == anTypeCode) {
							continue outer;
						}
					}
				}
			}
			rtn.add(change);
		}
		changes = rtn;
	}

	/**
	 * @return the changes
	 * @uml.property name="changes"
	 */
	public Collection<ModelChange> getChanges() {
		return changes;
	}

	public void addModelChange(ModelChange change) {
		changes.add(change);
	}

	public Table get_newTable() {
		return _newTable;
	}

	public void set_newTable(Table _newTable) {
		this._newTable = _newTable;
	}

	public Table get_oldTable() {
		return _oldTable;
	}

	public void set_oldTable(Table _oldTable) {
		this._oldTable = _oldTable;
	}

}
