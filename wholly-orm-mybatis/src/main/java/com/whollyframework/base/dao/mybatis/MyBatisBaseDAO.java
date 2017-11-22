package com.whollyframework.base.dao.mybatis;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.whollyframework.base.dao.support.AbstractSQLUtils;
import com.whollyframework.base.dao.support.SQLUtils;
import com.whollyframework.base.dao.support.criterion.Criterions;
import com.whollyframework.base.model.DataPackage;
import com.whollyframework.base.model.ParamsTable;
import com.whollyframework.base.model.ValueObject;
import com.whollyframework.constans.Web;
import com.whollyframework.util.DBUtil;
import com.whollyframework.util.StringUtil;
import com.whollyframework.util.reflect.ReflectUtil;
import com.whollyframework.util.sequence.Sequence;

@SuppressWarnings("unchecked")
public class MyBatisBaseDAO<E, ID extends Serializable>{

	@Autowired
	protected SqlSessionFactory sqlSessionFactory;
	

	/**
	 * beanName与sqlmap中的namespace对应
	 */
	private String beanName;
	
	public MyBatisBaseDAO(){
		Class<E> entityClass = ReflectUtil.getSuperClassGenricType(getClass());
		this.beanName = entityClass.getSimpleName();
	}

	public MyBatisBaseDAO(String beanName) {
		// beanName与sqlmap中的namespace对应。
		// 在进行数据库语句操作时，需要根据namespace来区分获取不同的操作语句。
		this.beanName = beanName;
	}

	public SqlSession getSqlSession() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		return sqlSession;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getBeanName() {
		return beanName;
	}

	public int removeCascade(String id) {
		try {
			int zhu = this.getSqlSession().delete(
					getBeanName() + ".deleteByPrimaryKey", id);
			int chi = this.getSqlSession().delete(
					getBeanName() + ".deleteCascade", id);
			return (zhu + chi);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public ValueObject find(String sqlid, Object id) throws SQLException {
		return (ValueObject) this.getSqlSession().selectOne(
				getBeanName() + "." + sqlid, id);
	}

	public List<E> getAll() throws SQLException {
		return this.getSqlSession().selectList(
				getBeanName() + ".getAll");
	}

	public List<E> simpleQuery(SQLUtils sqlutils, int page, int lines) throws SQLException {
		return this.getSqlSession().selectList(
				getBeanName() + ".selectByWhere_Clause", sqlutils,
				new RowBounds(lines * (page - 1), lines));
	}

	public List<? extends ValueObject> simpleQuery(String selectid, SQLUtils sqlutils, int page, int lines) throws SQLException {
		return this.getSqlSession().selectList(
				getBeanName() + "." + selectid, sqlutils,
				new RowBounds(lines * (page - 1), lines));
	}
	public List<? extends ValueObject> simpleQuery(String selectid, Map<String, Object> params) throws SQLException {
		return this.getSqlSession().selectList(
				getBeanName() + "." + selectid, params);
	}
	public List<? extends ValueObject> simpleQuery(String selectid, Map<String, Object> params, int page,
			int lines) throws SQLException {
		return this.getSqlSession().selectList(
				getBeanName() + "." + selectid, params, new RowBounds(lines * (page - 1), lines));
	}

	public void create(String sqlid, ValueObject vo) throws SQLException {
		if (vo.getCreated()==null){
			vo.setCreated(new Date());
		}
		if (vo.getLastModified()==null){
			vo.setLastModified(new Date());
		}
		if (DBUtil.DBTYPE_MYSQL.equals(DBUtil.RUNTIME_DBTYPE)) {
			Integer id = (Integer) this.getSqlSession().insert(
					getBeanName() + "." + sqlid + "Mysql", vo);
			vo.setId(id.toString());
		} else {
			if (StringUtil.isBlank(vo.getId())) {
//				String strId = (String) this.sqlMapClient
//						.queryForObject("IBatisDefault."
//								+ DBUtil.RUNTIME_DBTYPE + "getRecordId", null);
				vo.setId(Sequence.getSequence());
			}
			this.getSqlSession().insert(getBeanName() + "." + sqlid,
					vo);

		}
	}

	/**
	 * 更新指定sqlmap的单个对象
	 * 
	 * @return
	 * @throws SQLException 
	 */
	public int update(String sqlid, ValueObject vo) throws SQLException {
		if (vo.getCreated()==null){
			vo.setCreated(new Date());
		}
		if (vo.getLastModified()==null){
			vo.setLastModified(new Date());
		}
		return this.getSqlSession().update(
				getBeanName() + "." + sqlid, vo);
	}

	public int count(String selectid, AbstractSQLUtils filter) throws SQLException {
		Integer amount = Integer.valueOf(0);
		List<Integer> rst = this.getSqlSession().selectList(
				getBeanName() + "." + selectid, filter);
		if (!rst.isEmpty())
			amount = (Integer) rst.get(0);
		return amount.intValue();
	}

	public int count(String selectid, Map<String, Object> params)
			throws SQLException {
		Integer amount = Integer.valueOf(0);
		List<Integer> rst = this.getSqlSession().selectList(
				getBeanName() + "." + selectid, params);
		if (!rst.isEmpty())
			amount = (Integer) rst.get(0);
		return amount.intValue();
	}

	public DataPackage<E> getDatapackage(ParamsTable params, int page, int lines)
			throws SQLException {
		SQLUtils sqlutils = new SQLUtils();
		String app_id = params.getParameterAsString("s_app_id");
		if (StringUtil.isBlank(app_id)){
			String application = params.getParameterAsString("application");
			if (!StringUtil.isBlank(application)){
				params.setParameter("s_app_id", application);
			}
		}
		sqlutils.createWhere(params);
		sqlutils.createOrderBy(params);
		DataPackage<E> datas = new DataPackage<E>();
		datas.rowCount = count(sqlutils);
		if (datas.rowCount % lines == 0 && datas.rowCount / lines < page) {
			page = datas.rowCount / lines;
		} else if (datas.rowCount / lines + 1 < page) {
			page = datas.rowCount / lines + 1;
		}
		datas.datas = simpleQuery(sqlutils, page, lines);
		datas.linesPerPage = lines;
		datas.pageNo = page;

		return datas;
	}

	@SuppressWarnings("rawtypes")
	public DataPackage<? extends ValueObject> getDatapackageByParamsTable(String sqlid,
			ParamsTable params) throws SQLException {
		String _currpage = params.getParameterAsString("_currpage");
		int page = (_currpage != null && _currpage.length() > 0) ? Integer
				.parseInt(_currpage) : 1;
		String _pagelines = params.getParameterAsString("_pagelines");
		int lines = (_pagelines != null && _pagelines.length() > 0) ? Integer
				.parseInt(_pagelines) : Integer
				.valueOf(Web.DEFAULT_LINES_PER_PAGE);
		DataPackage datas = new DataPackage();
		SQLUtils sqlutils = new SQLUtils();
		String app_id = params.getParameterAsString("s_app_id");
		if (StringUtil.isBlank(app_id)){
			String application = params.getParameterAsString("application");
			if (!StringUtil.isBlank(application)){
				params.setParameter("s_app_id", application);
			}
		}
		sqlutils.createWhere(params);
		sqlutils.createOrderBy(params);
		datas.rowCount = count("count_" + sqlid, sqlutils);
		if (datas.rowCount % lines == 0 && datas.rowCount / lines < page) {
			page = datas.rowCount / lines;
		} else if (datas.rowCount / lines + 1 < page) {
			page = datas.rowCount / lines + 1;
		}
		datas.datas = simpleQuery(sqlid, sqlutils, page, lines);
		datas.linesPerPage = lines;
		datas.pageNo = page;

		return datas;
	}

	public DataPackage<? extends ValueObject> getDatapackageByParamsMap(String sqlid,
			Map<String, Object> params) throws SQLException {
		if (params == null)
			params = new HashMap<String, Object>();
		Object _currpage = params.get("_currpage");
		int page = (_currpage != null)
				&& !StringUtil.isBlank(_currpage.toString()) ? Integer
				.parseInt(_currpage.toString()) : 1;
		Object _pagelines = params.get("_pagelines");
		int lines = (_pagelines != null)
				&& !StringUtil.isBlank(_pagelines.toString()) ? Integer
				.parseInt(_pagelines.toString()) : Integer
				.valueOf(Web.DEFAULT_LINES_PER_PAGE);
		return getDatapackageByParamsMap(sqlid, params, page, lines);
	}

	@SuppressWarnings("rawtypes")
	public DataPackage<? extends ValueObject> getDatapackageByParamsMap(String sqlid,
			Map<String, Object> params, int page, int lines)
			throws SQLException {
		DataPackage datas = new DataPackage();
		datas.rowCount = count("count_" + sqlid, params);
		if (datas.rowCount % lines == 0 && datas.rowCount / lines < page) {
			page = datas.rowCount / lines;
		} else if (datas.rowCount / lines + 1 < page) {
			page = datas.rowCount / lines + 1;
		}
		datas.datas = simpleQuery(sqlid, params, page, lines);
		datas.linesPerPage = lines;
		datas.pageNo = page;

		return datas;
	}

	public List<E> simpleQuery(ParamsTable params) throws SQLException {
		SQLUtils sqlutils = new SQLUtils();
		sqlutils.createWhere(params);
		sqlutils.createOrderBy(params);
		return this.getSqlSession().selectList(
				getBeanName() + ".selectByWhere_Clause", sqlutils);
	}

	public ValueObject getData(SQLUtils sqlutils) throws SQLException {
		Collection<E> l = simpleQuery(sqlutils);
		return l != null && l.size() > 0 ? (ValueObject) l.iterator().next() : null;
	}

	public int getNextId(String name) throws SQLException {
		String id = (String) getSqlSession().selectOne(
				"IBatisDefault.OracleSequence", name);
		return Integer.valueOf(id);
	}
	
	public List<? extends ValueObject> queryByDynaSQLUtils(SQLUtils sqlutils) throws SQLException {
		return this.getSqlSession().selectList(getBeanName()+".selectBySQLUtils", sqlutils);
	}
	
	public void createByDynaSQLUtils(SQLUtils sqlutils) throws SQLException {
		 this.getSqlSession().insert(getBeanName()+".insertBySQLUtils", sqlutils);
	}
	
	public int updateByDynaSQLUtils(SQLUtils sqlutils) throws SQLException {
		return getSqlSession().update(getBeanName()+".updateBySQLUtils", sqlutils);
	}
	
	public int removeByDynaSQLUtils(SQLUtils sqlutils) throws SQLException {
		return getSqlSession().delete(getBeanName()+".deleteBySQLUtils", sqlutils);
	}

	public List<E> simpleQuery(AbstractSQLUtils filter) throws SQLException {
		return this.getSqlSession().selectList(
				getBeanName() + ".selectByWhere_Clause", filter);
	}

	public DataPackage<E> query(ParamsTable params) throws SQLException {
		String _currpage = params.getParameterAsString("_currpage");
		int page = (_currpage != null && _currpage.length() > 0) ? Integer
				.parseInt(_currpage) : 1;
		String _pagelines = params.getParameterAsString("_pagelines");
		int lines = (_pagelines != null && _pagelines.length() > 0) ? Integer
				.parseInt(_pagelines) : Integer
				.valueOf(Web.DEFAULT_LINES_PER_PAGE);
		return getDatapackage(params, page, lines);
	}

	public E find(ID id) throws SQLException {
		return (E) find("selectByPrimaryKey", id);
	}

	public void create(E vo) throws SQLException {
		create("insert", (ValueObject) vo);
	}

	public int update(E vo) throws SQLException {
		return update("updateByPrimaryKey", (ValueObject) vo);
	}

	public int remove(AbstractSQLUtils filter) throws SQLException {
		return this.getSqlSession().delete(
				getBeanName() + ".deleteByWhere_Clause", filter);
	}

	public int remove(ID id) throws SQLException {
		return this.getSqlSession().delete(
				getBeanName() + ".deleteByPrimaryKey", id);
	}

	public int remove(ID[] selects) throws SQLException {
		SQLUtils sqlutils = new SQLUtils();
		sqlutils.addCriterion(Criterions.in("id", selects));
		return remove(sqlutils);
	}

	public int remove(E vo) throws SQLException {
		SQLUtils sqlutils = new SQLUtils();
		sqlutils.addCriterion(Criterions.eq("id", ((ValueObject) vo).getId()));
		return remove(sqlutils);
	}

	public int count(AbstractSQLUtils filter) throws SQLException {
		return count("countByWhere_Clause", filter);
	}
}
