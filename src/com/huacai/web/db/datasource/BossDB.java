package com.huacai.web.db.datasource;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;

import com.huacai.web.db.JdbcBase;

@Repository("bossDB")
@Configurable(preConstruction = true)
public class BossDB extends JdbcBase {

	/**
	 * JDBC
	 */
	@Autowired
	public void setBossAdminJdbcTemplate(JdbcTemplate bossAdminJdbcTemplate) {
		this.jtpl = bossAdminJdbcTemplate;
	}

	/**
	 * 设置数据源
	 */
	@Autowired
	public void setBossAdminDataSource(DataSource bossAdminDataSource) {
		this.dataSource = bossAdminDataSource;
		setDbInfo();
	}

	/**
	 * 事务管理
	 */
	@Autowired
	public void setBossAdminTransactionManager(DataSourceTransactionManager bossAdminTransactionManager) {
		this.transManager = bossAdminTransactionManager;
	}

}
