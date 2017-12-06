package com.whollyframework.dbservice.menu.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.whollyframework.base.dao.ibatis.IBatisBaseDAO;
import com.whollyframework.dbservice.menu.model.Menu;


@Repository("menuDAO")
public class MenuDAOImpl extends IBatisBaseDAO<Menu,String> implements MenuDAO {

	public MenuDAOImpl() {
		super(Menu.class.getSimpleName());
	}

	@Override
	public List<Menu> simpleQueryByParentId(String parentid) throws SQLException {
		// TODO Auto-generated method stub
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("parentid", parentid);
		return this.getSqlMapClientTemplate().queryForList(this.getBeanName() + ".simpleQueryByParentId", params);
	}

	@Override
	public List<Menu> getMenuByPmenuIdIsNull() throws SQLException {
		// TODO Auto-generated method stub
		return this.getSqlMapClientTemplate().queryForList(this.getBeanName() + ".getMenuByPmenuIdIsNull");
	}

}
