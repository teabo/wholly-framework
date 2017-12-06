package com.whollyframework.dbservice.operatelog.dao;

import java.sql.SQLException;
import java.util.Date;

import org.springframework.stereotype.Repository;

import com.whollyframework.base.dao.ibatis.IBatisBaseDAO;
import com.whollyframework.util.DBUtil;
import com.whollyframework.dbservice.operatelog.model.OperateLog;


@Repository("operateLogDAO")
public class OperateLogDAOImpl extends IBatisBaseDAO<OperateLog,String> implements OperateLogDAO {

	public OperateLogDAOImpl() {
		super(OperateLog.class.getSimpleName());
	}
	
	public int create(OperateLog vo) throws SQLException {
		Object obj = null;
		
		if (vo.getCreated() == null) {
			vo.setCreated(new Date());
		}
		if (vo.getLastModified() == null) {
			vo.setLastModified(new Date());
		}
		if ("MySQL".equals(DBUtil.RUNTIME_DBTYPE)) {
			Integer id = (Integer) this.sqlMapClient.insert(getBeanName() + "."
					+ "insertMysql", vo);
			vo.setId(id.toString());
		} else {
			obj= getSqlMapClientTemplate().insert(getBeanName() + "." + "insertObj", vo);
			vo.setId(obj.toString());
		}
		return 1;
	}

}
