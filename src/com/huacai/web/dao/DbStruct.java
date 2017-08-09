package com.huacai.web.dao;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

public class DbStruct {
	private JdbcTemplate jtpl;
	private DataSourceTransactionManager transManager;
	private DataSource dataSource;
	
	private String dbType;
	private String DatabaseProductName;
	private String DatabaseProductVersion;
	
	private boolean isMySQL;
	private boolean isOracle;
	
	/**
	 * 获取jdbc对象
	 * @return
	 */
	public JdbcTemplate getJtpl() {
		return jtpl;
	}
	/**
	 * 设置jdbc对象
	 * @param jtpl
	 */
	public void setJtpl(JdbcTemplate jtpl) {
		this.jtpl = jtpl;
	}
	/**
	 * 获取事物管理对象
	 * @return
	 */
	public DataSourceTransactionManager getTransManager() {
		return transManager;
	}
	/**
	 * 设置事物管理对象
	 * @param transManager
	 */
	public void setTransManager(DataSourceTransactionManager transManager) {
		this.transManager = transManager;
	}
	/**
	 * 获取数据源
	 * @return
	 */
	public DataSource getDataSource() {
		return dataSource;
	}
	/**
	 * 设置数据源
	 * @param dataSource
	 * @throws Exception
	 */
	public void setDataSource(DataSource dataSource) throws Exception {
		this.dataSource = dataSource;
		setDatabaseProductName(dataSource.getConnection().getMetaData().getDatabaseProductName());
		setDatabaseProductVersion(dataSource.getConnection().getMetaData().getDatabaseProductVersion());
		setDbType(DatabaseProductName.toLowerCase());
	}
	/**
	 * 获取数据库类型
	 * @return
	 */
	public String getDbType() {
		return dbType;
	}
	/**
	 * 设置数据库类型
	 * @param dbType
	 */
	public void setDbType(String dbType) {
		this.dbType = dbType;
		isMySQL = getDbType().equals("mysql");
		isOracle = getDbType().equals("oracle");
	}
	/**
	 * 设置数据库产品名称
	 * @return
	 */
	public String getDatabaseProductName() {
		return DatabaseProductName;
	}
	/**
	 * 获取数据库产品名称
	 * @param databaseProductName
	 */
	public void setDatabaseProductName(String databaseProductName) {
		DatabaseProductName = databaseProductName;
	}
	/**
	 * 设置数据库版本
	 * @return
	 */
	public String getDatabaseProductVersion() {
		return DatabaseProductVersion;
	}
	/**
	 * 获取数据库版本
	 * @param databaseProductVersion
	 */
	public void setDatabaseProductVersion(String databaseProductVersion) {
		DatabaseProductVersion = databaseProductVersion;
	}
	
	
	/**
	 * 判断数据库是否是mysql
	 * @return
	 */
	public boolean isMySQL() {
		return isMySQL;
	}
	/**
	 * 判断数据库是否是oracle
	 */
	public boolean isOracle() {
		return isOracle;
	}

}
