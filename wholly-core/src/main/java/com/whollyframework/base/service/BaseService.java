package com.whollyframework.base.service;

import java.io.Serializable;
import java.util.List;

import com.whollyframework.annotation.MethodLog;
import com.whollyframework.base.dao.IDesignDAO;
import com.whollyframework.base.dao.support.AbstractSQLUtils;
import com.whollyframework.base.model.DataPackage;
import com.whollyframework.base.model.ParamsTable;

public abstract class BaseService<E, ID extends Serializable> {

	public abstract IDesignDAO<E, ID> getDAO();

	@MethodLog(operType="4", remark="删除数据对象")
	public int doRemove(E vo) throws Exception {
		return getDAO().remove(vo);
	}

	@MethodLog(operType="1", remark="根据ID获取数据对象")
	public E find(ID id) throws Exception {
		return getDAO().find(id);
	}
	
	@MethodLog(operType="4", remark="根据ID删除数据对象")
	public int doRemove(ID pk) throws Exception {
		return getDAO().remove(pk);
	}

	@MethodLog(operType="1", remark="查询表所有数据记录")
	public List<E> getAll() throws Exception {
		return getDAO().getAll();
	}

	@MethodLog(operType="1", remark="根据条件查询表数据记录(分页查询)")
	public DataPackage<E> query(ParamsTable params) throws Exception {
		return getDAO().query(params);
	}

	@MethodLog(operType="2", remark="新增数据对象")
	public int doCreate(E y) throws Exception {
		return getDAO().create(y);
	}

	@MethodLog(operType="3", remark="更新数据对象")
	public int doUpdate(E vo) throws Exception {
		return getDAO().update(vo);
	}

	@MethodLog(operType="4", remark="根据ID数组删除数据对象")
	public int doRemove(ID[] selects) throws Exception {
		return getDAO().remove(selects);
	}

	@MethodLog(operType="1", remark="根据条件查询表数据记录")
	protected List<E> simpleQuery(ParamsTable params, AbstractSQLUtils sqlutils) throws Exception {
		sqlutils.createWhere(params);
		sqlutils.createOrderBy(params);
		return getDAO().simpleQuery(sqlutils);
	}

	@MethodLog(operType="1", remark="根据条件统计结果是否为空")
	protected boolean isEmpty(AbstractSQLUtils sqlutils) throws Exception {
		if (getDAO().count(sqlutils) <= 0) {
			return true;
		}
		return false;
	}
}
