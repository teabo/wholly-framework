package com.whollyframework.base.service;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.jamonapi.proxy.MonProxyFactory;
import com.whollyframework.base.dao.IRuntimeDAO;
import com.whollyframework.base.model.DataPackage;
import com.whollyframework.base.model.ParamsTable;
import com.whollyframework.base.model.PersistenceUtils;
import com.whollyframework.base.model.ValueObject;
import com.whollyframework.base.model.WebUser;
import com.whollyframework.util.sequence.Sequence;

/**
 * The base abstract run time process bean.
 */
public abstract class AbstractRunTimeProcess<E> implements IRunTimeProcess<E> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7120284978893104541L;

	/**
	 * The dataSourceId id
	 */
	protected String dataSourceId;

	/**
	 * The data sources.
	 */
	protected static HashMap<String, DataSource> dataSources = new HashMap<String, DataSource>();

	/**
	 * 单态时存在问题
	 */
	public static final ThreadLocal<Map<String, Integer>> transactionSignal = new ThreadLocal<Map<String, Integer>>();

	/**
	 * The constructor with application id.
	 * 
	 * @param applicationId
	 */
	public AbstractRunTimeProcess(String applicationId) {
		this.dataSourceId = applicationId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.base.ejb.IRunTimeProcess#doView(java.lang.String)
	 */
	public ValueObject find(String id) throws Exception {
		return getDAO().find(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.base.ejb.IRunTimeProcess#doCreate(cn.myapps.base.dao.ValueObject)
	 */
	public void doCreate(ValueObject vo) throws Exception {
		try {
			if (vo.getId() == null || vo.getId().trim().length() == 0) {
				vo.setId(Sequence.getSequence());
			}

			beginTransaction();
			getDAO().create(vo);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.base.ejb.IRunTimeProcess#doRemove(java.lang.String)
	 */
	public void doRemove(String pk) throws Exception {
		try {
			beginTransaction();
			getDAO().remove(pk);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.base.ejb.IRunTimeProcess#doUpdate(cn.myapps.base.dao.ValueObject)
	 */
	public void doUpdate(ValueObject vo) throws Exception {
		try {
			beginTransaction();
			getDAO().update(vo);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.base.ejb.IRunTimeProcess#doQuery(cn.myapps.base.action.ParamsTable,
	 *      cn.myapps.core.user.action.WebUser)
	 */
	public DataPackage<E> query(ParamsTable params, WebUser user) throws Exception {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.base.ejb.IRunTimeProcess#beginTransaction()
	 */
	public void beginTransaction() throws Exception {
		beginTransaction(getDataSourceId());
	}
	
	public void beginTransaction(String dataSourceId) throws Exception {

		int signal = getTransactionSignal(dataSourceId);

		if (signal == 0)
			getConnection(dataSourceId).setAutoCommit(false);

		signal++;

		setTransactionSignal(dataSourceId,signal);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.base.ejb.IRunTimeProcess#commitTransaction()
	 */
	public void commitTransaction() throws Exception {
		commitTransaction(getDataSourceId());
	}
	
	public void commitTransaction(String dataSourceId) throws Exception {
		int signal = getTransactionSignal(dataSourceId);
		signal--;

		if (signal == 0) {
			Connection connection = getConnection(dataSourceId);
			connection.commit();
			connection.setAutoCommit(true);
		}

		setTransactionSignal(dataSourceId,signal);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.base.ejb.IRunTimeProcess#rollbackTransaction()
	 */
	public void rollbackTransaction() throws Exception {
		rollbackTransaction(getDataSourceId());
	}
	
	public void rollbackTransaction(String dataSourceId) throws Exception {
		int signal = getTransactionSignal(dataSourceId);
		signal--;

		if (signal == 0) {
			Connection connection = getConnection(dataSourceId);
			connection.rollback();
			connection.setAutoCommit(true);
		}

		setTransactionSignal(dataSourceId, signal);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.myapps.base.ejb.IRunTimeProcess#closeConnection()
	 */
	public void closeConnection() throws Exception {
		closeConnection(getDataSourceId());
	}
	
	public void closeConnection(String dataSourceId) throws Exception {
		if (getConnection(dataSourceId) != null) {
			if (!getConnection(dataSourceId).isClosed())
				getConnection(dataSourceId).close();
			Map<String, Connection> connMap = PersistenceUtils.runtimeDBConn.get();
			if (connMap!=null){
				connMap.remove(dataSourceId);
			}
		}
	}

	/**
	 * Get the relate Dao
	 * 
	 * @return The relate Dao.
	 * @throws Exception
	 */
	protected abstract IRuntimeDAO getDAO() throws Exception;

	/**
	 * Get the dataSourceId id.
	 * 
	 * @return
	 */
	public String getDataSourceId() {
		return dataSourceId;
	}

	/**
	 * @param dataSourceId
	 *            the dataSourceId to set
	 */
	protected void setDataSourceId(String dataSourceId) {
		this.dataSourceId = dataSourceId;
	}

	/**
	 * The data source.
	 * 
	 * @return The data source.
	 * @throws Exception
	 */
	protected abstract DataSource getDataSource(String dataSoruceId) throws Exception ;

	/**
	 * Remove the data source.
	 * 
	 * @param application
	 *            The application name
	 */
	public static synchronized void removeDataSource(String application) {
		dataSources.remove(application);
	}

	/**
	 * Get the connection
	 * 
	 * @return the connection
	 * @throws Exception
	 */
	protected Connection getConnection() throws Exception {
		return getConnection(getDataSourceId());
	}
	/**
	 * Get the connection
	 * 
	 * @return the connection
	 * @throws Exception
	 */
	protected Connection getConnection(String dataSoruceId) throws Exception {
		Map<String, Connection> connMap = PersistenceUtils.runtimeDBConn.get();
		Connection conn = null;
		if (connMap != null) {
			conn = (Connection) connMap.get(dataSoruceId);
			if (conn == null || conn.isClosed()) {
				conn = getDataSource(dataSoruceId).getConnection();
				connMap.put(dataSoruceId, conn);
			}
		} else {
			conn = getDataSource(dataSoruceId).getConnection();
			Map<String, Connection> map = new HashMap<String, Connection>();
			map.put(dataSoruceId, conn);
			PersistenceUtils.runtimeDBConn.set(map);
		}

		return MonProxyFactory.monitor(conn);
	}

	/**
	 * Get the transaction singal.
	 * 
	 * @return The transcation singal.
	 */
	protected int getTransactionSignal(String dataSourceId) {
		Map<String, Integer> signal = transactionSignal.get();
		if (signal != null) {
			Integer v = signal.get(dataSourceId);
			return v.intValue();
		} else {
			return 0;
		}
	}
	/**
	 * Set the transaction signal
	 * 
	 * @param signal
	 *            The transaction signal to set.
	 */
	protected void setTransactionSignal(String dataSourceId, int signal) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put(dataSourceId, Integer.valueOf(signal));
		transactionSignal.set(map);
	}

}
