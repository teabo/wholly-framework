package com.whollyframework.base.dao.hibernate;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;

import com.whollyframework.base.dao.support.AbstractSQLUtils;
import com.whollyframework.base.dao.support.HibernateSQLUtils;
import com.whollyframework.base.model.DataPackage;
import com.whollyframework.base.model.ParamsTable;
import com.whollyframework.util.StringUtil;

/**
 * 封装的Hibernat DAO泛型基类.
 * 
 * 扩展功能包括分页查询,按属性过滤条件列表查询. 可在Service层直接使用,也可以扩展泛型DAO子类使用,见两个构造函数的注释.
 * 
 * @param <T> DAO操作的对象类型
 * @param <ID> 主键类型
 * 
 * @author chris hsu
 */
public class HibernateBaseDAO<T, ID extends Serializable> extends HibernateSupportDAO<T, Serializable> {

	/**
	 * 用于Dao层子类的构造函数. 通过子类的泛型定义取得对象类型Class. eg. public class UserDao extends HibernateBaseDAO<User, String>{ }
	 */
	public HibernateBaseDAO() {
		super();
	}

	/**
	 * 用于省略Dao层, Service层直接使用通用HibernateDao的构造函数. 在构造函数中定义对象类型Class. eg. HibernateBaseDAO<User, String> userDao = new
	 * HibernateBaseDAO<User, String>(sessionFactory, User.class);
	 */
	public HibernateBaseDAO(final SessionFactory sessionFactory, final Class<T> entityClass) {
		super(sessionFactory, entityClass);
	}

	/**
	 * Get datas List .
	 * 
	 * @param filter AbstractSQLUtils
	 * @return List
	 * @see com.whollyframework.base.dao.IDesignDAO#simpleQuery(com.whollyframework.base.dao.support.AbstractSQLUtils)
	 */
	@SuppressWarnings("unchecked")
	public List<T> simpleQuery(AbstractSQLUtils filter) throws SQLException {
		Criterion[] criterions = ((HibernateSQLUtils) filter).toHibernateCriterions();
		Criteria c = createCriteria(criterions);
		((HibernateSQLUtils) filter).setOrderBy(c);
		return c.list();
	}

	/**
	 * Get datas List .
	 * 
	 * @param params ParamsTable
	 * @return List
	 * @see com.whollyframework.base.dao.IDesignDAO#simpleQuery(com.whollyframework.base.model.ParamsTable)
	 */
	public List<T> simpleQuery(ParamsTable params) throws SQLException {
		HibernateSQLUtils sqlUtils = new HibernateSQLUtils();
		sqlUtils.createWhere(params);
		sqlUtils.createOrderBy(params);
		return simpleQuery(sqlUtils);
	}

	/**
	 * Get DataPackage .
	 * 
	 * @param params ParamsTable
	 * @return List
	 * @see com.whollyframework.base.dao.IDesignDAO#query(com.whollyframework.base.model.ParamsTable)
	 */
	public DataPackage<T> query(ParamsTable params) throws SQLException {
		String _currpage = params.getParameterAsString("_currpage");
		String _pagelines = params.getParameterAsString("_pagelines");

		int page = (_currpage != null && _currpage.length() > 0) ? Integer.parseInt(_currpage) : 1;
		int lines = (_pagelines != null && _pagelines.length() > 0) ? Integer.parseInt(_pagelines) : Integer.MAX_VALUE;
		return getDatapackage(params, page, lines);
	}

	/**
	 * Get DataPackage .
	 * 
	 * @param params ParamsTable
	 * @param page page
	 * @param line line
	 * @return List
	 * @see com.whollyframework.base.dao.IDesignDAO#query(com.whollyframework.base.model.ParamsTable,int,int)
	 */
	public DataPackage<T> getDatapackage(ParamsTable params, int page, int lines) throws SQLException {
		HibernateSQLUtils sqlUtils = new HibernateSQLUtils();
		sqlUtils.createWhere(params);
		sqlUtils.createOrderBy(params);
		DataPackage<T> result = new DataPackage<T>();
		result.rowCount = count(sqlUtils);
		result.pageNo = page;
		result.linesPerPage = lines;

		if (result.pageNo > result.getPageCount()) {
			result.pageNo = 1;
			page = 1;
		}

		result.datas = getDatas(sqlUtils, page, lines);
		return result;
	}

	protected Collection<T> getDatas(AbstractSQLUtils filter, int page, int lines) {
		Criterion[] criterions = ((HibernateSQLUtils) filter).toHibernateCriterions();
		Criteria c = createCriteria(criterions);
		((HibernateSQLUtils) filter).setOrderBy(c);
		// 设置分页信息
		c.setFirstResult((page - 1) * lines);
		c.setMaxResults(lines);
		return null;
	}

	/**
	 * find the value object by primary key
	 * @param id primary key
	 * @return Object 
	 * @see com.whollyframework.base.dao.IDesignDAO#find(ID)
	 */
	public T find(ID id) throws SQLException {
		return findUniqueBy(getIdName(), id);
	}

	/**
	 * create the value object.
	 * @param vo value object
	 * @see com.whollyframework.base.dao.IDesignDAO#create(com.whollyframework.base.model.ValueObject)
	 */
	public void create(T vo) throws SQLException {
		save(vo);
	}

	/**
	 * update the value object.
	 * @param vo value object
	 * @see com.whollyframework.base.dao.IDesignDAO#update(com.whollyframework.base.model.ValueObject)
	 */
	public int update(T vo) throws SQLException {
		try {
			save(vo);
		} catch (Exception e) {
			return 0;
		}
		return 1;
	}

	public int remove(AbstractSQLUtils filter) throws SQLException {
		StringBuilder hql = new StringBuilder();
		hql.append("Delete ").append(entityClass.getSimpleName()).append(" where ");
		com.whollyframework.base.dao.support.criterion.Criterion[] criterions = (com.whollyframework.base.dao.support.criterion.Criterion[]) filter
				.getFilterCriterions();
		List<Object> values = new ArrayList<Object>();
		for (int i = 0; i < criterions.length;) {
			hql.append(criterions[i].toSqlString());
			Collections.addAll(values, criterions[i].getParamValues());
			if (++i < criterions.length) {
				hql.append(" and ");
			}
		}

		return executeUpdate(hql.toString(), values);
	}

	public int remove(ID id) throws SQLException {
		try {
			delete(id);
		} catch (Exception e) {
			return 0;
		}
		return 1;
	}

	public int remove(ID[] selects) throws SQLException {
		try {
			delete(selects);
		} catch (Exception e) {
			return 0;
		}
		return 1;
	}

	public int remove(T vo) throws SQLException {
		try {
			delete(vo);
		} catch (Exception e) {
			return 0;
		}
		return 1;
	}

	public int count(AbstractSQLUtils filter) throws SQLException {
		Criterion[] criterions = ((HibernateSQLUtils) filter).toHibernateCriterions();
		return count(createCriteria(criterions));
	}

	public int count(final Criteria criteria) throws SQLException {
		// 执行Count查询
		Long totalCountObject = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
		return (totalCountObject != null) ? totalCountObject.intValue() : 0;
	}

	public int getNextId(String string) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * 执行count查询获得本次Hql查询所能获得的对象总数.
	 * 
	 * 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
	 */
	protected long count(final String hql, final Object... values) {
		String countHql = prepareCountHql(hql);

		try {
			Long count = findUnique(countHql, values);
			return count;
		} catch (Exception e) {
			throw new RuntimeException("hql can't be auto count, hql is:" + countHql, e);
		}
	}

	/**
	 * 执行count查询获得本次Hql查询所能获得的对象总数.
	 * 
	 * 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
	 */
	protected long count(final String hql, final Map<String, ?> values) {
		String countHql = prepareCountHql(hql);

		try {
			Long count = findUnique(countHql, values);
			return count;
		} catch (Exception e) {
			throw new RuntimeException("hql can't be auto count, hql is:" + countHql, e);
		}
	}

	private String prepareCountHql(String orgHql) {
		String fromHql = orgHql;
		// select子句与order by子句会影响count查询,进行简单的排除.
		fromHql = "from " + StringUtil.substringAfter(fromHql, "from");
		fromHql = StringUtil.substringBefore(fromHql, "order by");

		String countHql = "select count(*) " + fromHql;
		return countHql;
	}
}
