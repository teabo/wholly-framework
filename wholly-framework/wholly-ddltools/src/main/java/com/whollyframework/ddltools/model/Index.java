package com.whollyframework.ddltools.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * NON_UNIQUE boolean => Can index values be non-unique. false when TYPE is
 * tableIndexStatistic INDEX_NAME String => index name; null when TYPE is
 * tableIndexStatistic COLUMN_NAME String => column name; null when TYPE is
 * tableIndexStatistic ASC_OR_DESC String => column sort sequence, "A" =>
 * ascending, "D" => descending, may be null if sort sequence is not supported;
 * null when TYPE is tableIndexStatistic
 * 
 * @author Chris Hsu
 * @since 2014-06-10
 */
public class Index implements Cloneable {

	public Index(String index_name) {
		this.indexName = index_name;
	}

	/**
	 * 列ID
	 */
	private String id;

	/**
	 * 索引名
	 */
	private String indexName;

	/**
	 * 索引列
	 */
	private List<IndexColumn> indexColumns = new ArrayList<IndexColumn>();

	/**
	 * 非唯一索引
	 */
	private boolean non_unique;

	/**
	 * 升/降序
	 */
	private String asc_or_desc;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String index_name) {
		this.indexName = index_name;
	}

	public void addIndexColumns(IndexColumn column) {
		indexColumns.add(column);
	}

	public List<IndexColumn> getIndexColumns() {
		return indexColumns;
	}

	public boolean isNon_unique() {
		return non_unique;
	}

	public void setNon_unique(boolean non_unique) {
		this.non_unique = non_unique;
	}

	public String getAsc_or_desc() {
		return asc_or_desc;
	}

	public void setAsc_or_desc(String asc_or_desc) {
		this.asc_or_desc = asc_or_desc;
	}

	public IndexColumn findIndexColumn(String field_name) {
		if (field_name != null)
			for (Iterator<IndexColumn> iterator = indexColumns.iterator(); iterator
					.hasNext();) {
				IndexColumn indexColumn = iterator.next();
				if (field_name.equalsIgnoreCase(indexColumn.getFieldName())) {
					return indexColumn;
				}
			}
		return null;
	}

}
