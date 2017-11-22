package com.whollyframework.base.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.whollyframework.util.json.JsonUtil;

/**
 * Data package, usually use in the list.
 */
public class DataPackage<E> implements Serializable {
	private static final long serialVersionUID = -3847321538691386074L;
	/**
	 * 总行数
	 * 
	 * @uml.property name="rowCount"
	 */
	public int rowCount;
	/**
	 * 每页显示的行数
	 * 
	 * @uml.property name="linesPerPage"
	 */
	public int linesPerPage;
	/**
	 * 页码
	 * 
	 * @uml.property name="pageNo"
	 */
	public int pageNo;
	/**
	 * 当前页数据集
	 * 
	 * @uml.property name="datas"
	 */
	public Collection<E> datas;

	/**
	 * Gets 当前页数据集
	 * 
	 * @return Returns the datas.
	 * @uml.property name="datas"
	 */
	public Collection<E> getDatas() {
		if (datas == null) {
			datas = new ArrayList<E>();
		}

		return datas;
	}

	/**
	 * Set当前页数据集
	 * 
	 * @param datas
	 *            The datas to set.
	 * @uml.property name="datas"
	 */
	public void setDatas(Collection<E> datas) {
		this.datas = datas;
	}

	/**
	 * Gets 每页显示的行数
	 * 
	 * @return Returns the linesPerPage.
	 * @uml.property name="linesPerPage"
	 */
	public int getLinesPerPage() {
		return linesPerPage;
	}

	/**
	 * Set 每页显示的行数
	 * 
	 * @param linesPerPage
	 *            The linesPerPage to set.
	 * @uml.property name="linesPerPage"
	 */
	public void setLinesPerPage(int linesPerPage) {
		this.linesPerPage = linesPerPage;
	}

	/**
	 * Gets 页码
	 * 
	 * @return Returns the pageNo.
	 * @uml.property name="pageNo"
	 */
	public int getPageNo() {
		return pageNo;
	}

	/**
	 * Set 页码
	 * 
	 * @param pageNo
	 *            The pageNo to set.
	 * @uml.property name="pageNo"
	 */
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	/**
	 * Gets 总行数
	 * 
	 * @return Returns the rowCount.
	 * @uml.property name="rowCount"
	 */
	public int getRowCount() {
		return rowCount;
	}

	/**
	 * Set 总行数
	 * 
	 * @param rowCount
	 *            The rowCount to set.
	 * @uml.property name="rowCount"
	 */
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	/**
	 * Gets 总页数
	 * 
	 * 
	 * @return The total page number.
	 */
	public int getPageCount() {
		return (int) Math.ceil((double) rowCount / (double) linesPerPage);
	}

	/**
	 * 转换为JSON格式,暂时先可以这样，后续可能需要再修改
	 * 
	 * @return
	 */
	public String toJSON() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", getRowCount());
		map.put("_pagelines", getLinesPerPage());
		map.put("_currpage", getPageNo());
		map.put("rows", getDatas());
		
		return JsonUtil.toJson(map);
	}
	
	//修改为支持日期字段
	public String toJsonDate() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", getRowCount());
		map.put("_pagelines", getLinesPerPage());
		map.put("_currpage", getPageNo());
		map.put("rows", getDatas());
		
		return JsonUtil.toJsonDate(map);
	}
	
}
