package com.huacai.web.dao;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

public class BaseDataSourceDao {
	private static final Logger log = LogManager
			.getLogger(BaseDataSourceDao.class);
	/**
	 * 主库
	 */
	protected DbStruct db = null;

	/**
	 * 自动设置数据源，多个库可以设置多个 setDataSource,根据配置名称设置方法注入 如 set***DataSource
	 */
	@Autowired
	public void setBossAdminDataSource(DataSource bossAdminDataSource) {
		if (db == null) {
			db = new DbStruct();
		}
		try {
			db.setDataSource(bossAdminDataSource);
		} catch (Exception e) {
			log.error("", e);
		}
		db.setJtpl(new JdbcTemplate(bossAdminDataSource));
	}

	/**
	 * 事务管理，多个库可以设置多个
	 * 
	 * @param transactionManager
	 *            如 set***TransactionManager
	 */
	public void setBossAdminTransactionManager(
			DataSourceTransactionManager bossAdminTransactionManager) {
		if (db == null) {
			db = new DbStruct();
		}
		db.setTransManager(bossAdminTransactionManager);
	}


	/**
	 * trade 交易
	 */
	@Autowired
	public void setBossTradeDataSource(DataSource bossTradeDataSource) {
		if (db == null) {
			db = new DbStruct();
		}
		try {
			db.setDataSource(bossTradeDataSource);
		} catch (Exception e) {
			log.error("", e);
		}
		db.setJtpl(new JdbcTemplate(bossTradeDataSource));
	}

	
	/**
	 * push 推送
	 */
	@Autowired
	public void setPushDataSource(DataSource pushDataSource) {
		if (db == null) {
			db = new DbStruct();
		}
		try {
			db.setDataSource(pushDataSource);
		} catch (Exception e) {
			log.error("", e);
		}
		db.setJtpl(new JdbcTemplate(pushDataSource));
	}
	
	
	@Autowired
	public void setBossTradeTransactionManager(
			DataSourceTransactionManager bossAdminTransactionManager) {
		if (db == null) {
			db = new DbStruct();
		}
		db.setTransManager(bossAdminTransactionManager);
	}

	/**
	 * 报表
	 */
	@Autowired
	public void setBossReportDataSource(DataSource bossReportDataSource) {
		if (db == null) {
			db = new DbStruct();
		}
		try {
			db.setDataSource(bossReportDataSource);
		} catch (Exception e) {
			log.error("", e);
		}
		db.setJtpl(new JdbcTemplate(bossReportDataSource));
	}
	 
	@Autowired
	public void setBossReportTransactionManager(
			DataSourceTransactionManager bossReportTransactionManager) {
		if (db == null) {
			db = new DbStruct();
		}
		db.setTransManager(bossReportTransactionManager);
	}

}
