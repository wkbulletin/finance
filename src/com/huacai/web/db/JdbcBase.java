package com.huacai.web.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.util.JdbcUtils;
import com.alibaba.fastjson.JSON;
import com.huacai.web.common.DbCommon;

public class JdbcBase {
	protected static final Logger log = LogManager.getLogger(JdbcBase.class);
	private static boolean _debug = true;
	
	protected JdbcTemplate jtpl;
	protected DataSourceTransactionManager transManager;
	protected DataSource dataSource;
	
	private String dbType;
	private String dbProductName;
	private String dbProductVersion;
	
	private boolean isMySQL;
	private boolean isOracle;
	
	
	/**
	 * 获取DataSource
	 * @return
	 */
	public DataSource getDataSource() {
		return dataSource;
	}
	/**
	 * 获取JdbcTemplate
	 * @return
	 */
	public JdbcTemplate getJtpl() {
		return jtpl;
	}
	/**
	 * 获取DataSourceTransactionManager
	 * @return
	 */
	public DataSourceTransactionManager getTransManager() {
		return transManager;
	}
	
	/**
	 * 设置数据库信息
	 */
	public void setDbInfo() {
		try {
			setDbProductName(dataSource.getConnection().getMetaData().getDatabaseProductName());
			setDbProductVersion(dataSource.getConnection().getMetaData().getDatabaseProductVersion());
			setDbType(dbProductName.toLowerCase());
		} catch (SQLException e) {
			log.error("", e);
		}
		
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
	
	/**
	 * 设置数据库产品名称
	 * @return
	 */
	public String getDbProductName() {
		return dbProductName;
	}
	/**
	 * 获取数据库产品名称
	 * @param dbProductName
	 */
	public void setDbProductName(String dbProductName) {
		this.dbProductName = dbProductName;
	}
	/**
	 * 设置数据库版本
	 * @return
	 */
	public String getDbProductVersion() {
		return dbProductVersion;
	}
	/**
	 * 获取数据库版本
	 * @param dbProductVersion
	 */
	public void setDbProductVersion(String dbProductVersion) {
		this.dbProductVersion = dbProductVersion;
	}
	
	
	/**
	 * 更新表
	 * @param tbName 表名
	 * @param map 字段及对应值
	 * @param where 更新条件
	 * @return
	 * @throws Exception
	 */
	public int sql_update(String tbName, Map<String, Object> map, String where) throws Exception {
		return sql_update(tbName, map, where, new Object[]{});
	}
	
	/**
	 * 更新表
	 * @param tbName 表名
	 * @param map 字段及对应值
	 * @param where 更新条件
	 * @param whereParams 更新条件值
	 * @return 
	 * @throws Exception
	 */
	public int sql_update(String tbName, Map<String, Object> map, String where, List<Object> whereParams) throws Exception {
		return sql_update(tbName, map, where, whereParams != null ? whereParams.toArray() : new Object[]{});
	}

	/**
	 * 更新表
	 * @param tbName 表名
	 * @param map 字段及对应值
	 * @param where 更新条件
	 * @param whereParams 更新条件值
	 * @return 
	 * @throws Exception
	 */
	public int sql_update(String tbName, Map<String, Object> map, String where, Object[] whereParams) throws Exception {
		List<Object> param = new ArrayList<Object>();

		StringBuffer sqlKeys = new StringBuffer();

		Set<Entry<String, Object>> entrys = map.entrySet();
		String key;
		Object val;
		for (Entry<String, Object> entry : entrys) {
			key = entry.getKey();
			val = entry.getValue();

			sqlKeys.append(key + "=?,");

			param.add(val);
		}
		sqlKeys.deleteCharAt(sqlKeys.length() - 1);

		StringBuffer sql = new StringBuffer();

		sql.append("update " + tbName + " set ");
		sql.append(sqlKeys.toString());
		if (where != null && where.length() > 0) {
			sql.append(" where ");
			sql.append(where);
		}
		if(whereParams!=null&&whereParams.length>0){
			for(Object whereParam : whereParams){
				param.add(whereParam);
			}
			
		}

		// System.out.println(param);
		double t = DbCommon.getTime();
		log.debug("exe sql start");
		int r = jtpl.update(sql.toString(), param.toArray());
		log.debug("exe sql finish:" + DbCommon.runTime(t));
		return r;
	}

	/**
	 * 插入表
	 * @param tbName 表名
	 * @param map 字段及对应值
	 * @return
	 * @throws Exception
	 */
	public int sql_insert(String tbName, Map<String, Object> map) throws Exception {

		List<Object> param = new ArrayList<Object>();

		StringBuffer sqlKeys = new StringBuffer();
		StringBuffer sqlValues = new StringBuffer();

		Set<Entry<String, Object>> entrys = map.entrySet();
		String key;
		Object val;
		for (Entry<String, Object> entry : entrys) {
			key = entry.getKey();
			val = entry.getValue();
			if (val instanceof String && ((String) val).trim().toLowerCase().endsWith(".nextval")) {
				sqlKeys.append(key + ",");
				sqlValues.append(((String) val) + ",");
			} else {
				sqlKeys.append(key + ",");
				sqlValues.append("?,");

				param.add(val);
			}
		}
		sqlKeys.deleteCharAt(sqlKeys.length() - 1);
		sqlValues.deleteCharAt(sqlValues.length() - 1);

		StringBuffer sql = new StringBuffer();

		sql.append("insert into " + tbName + " (");
		sql.append(sqlKeys.toString());
		sql.append(") values (");
		sql.append(sqlValues.toString());
		sql.append(") ");
		// System.out.println(param);

		double t = DbCommon.getTime();
		log.debug("exe sql start");
		int r = jtpl.update(sql.toString(), param.toArray());
		if(r == 1){
			r = jtpl.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
		}
		log.debug("exe sql finish:" + DbCommon.runTime(t));
		return r;
	}

	/**
	 * 批量更新或插入
	 * @param sql 多个sql
	 * @return
	 * @throws Exception
	 */
	public int[] batchUpdate(String sql[]) throws Exception {
		double t = DbCommon.getTime();
		log.debug("exe sql start");
		int[] r = jtpl.batchUpdate(sql);
		log.debug("exe sql finish:" + DbCommon.runTime(t));
		return r;
	}
	/**
	 * 批量更新或插入--带参数
	 * @param sql
	 * @param batchArgs 多个参数
	 * @return
	 * @throws Exception
	 */
	public int[] batchUpdate(String sql, List<Object[]> batchArgs) throws Exception {

		double t = DbCommon.getTime();
		log.debug("exe sql start");
		int[] r = jtpl.batchUpdate(sql, batchArgs);

		log.debug("exe sql finish:" + DbCommon.runTime(t));
		return r;
	}

	/**
	 * 更新或插入
	 * 
	 * @param sql
	 * @return
	 */
	public int update(String sql) throws Exception {
		double t = DbCommon.getTime();
		log.debug("exe sql start");
		int r = jtpl.update(sql);
		log.debug("exe sql finish:" + DbCommon.runTime(t));
		return r;
	}

	/**
	 * 更新或插入--带参数
	 * @param sql
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int update(String sql, List<Object> param) throws Exception {
		return update(sql, param.toArray());
	}
	/**
	 * 更新或插入--带参数
	 * @param sql
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int update(String sql, Object[] param) throws Exception {

		double t = DbCommon.getTime();
		log.debug("exe sql start");

		int r = jtpl.update(sql, param);
		if (_debug && param != null && param.length > 0) {
			log.debug("exe sql param=" + JSON.toJSONString(param));
		}
		log.debug("exe sql finish:" + DbCommon.runTime(t));
		return r;
	}

	/**
	 * 查询单条记录
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryMap(String sql) throws Exception {
		double t = DbCommon.getTime();
		log.debug("exe sql start");
		sql = "select * from (" + sql + ") where rownum <=1 ";
		Map<String, Object> r = null;
		try {
			r = jtpl.queryForMap(sql);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		log.debug("exe sql finish:" + DbCommon.runTime(t));
		return r;
	}

	/**
	 * 查询单记录--带参数
	 * 
	 * @param sql
	 * @return
	 */
	public Map<String, Object> queryMap(String sql, List<Object> param) throws Exception {
		return queryMap(sql, param.toArray());
	}
	/**
	 * 查询单记录--带参数
	 * @param sql
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryMap(String sql, Object[] param) throws Exception {
		double t = DbCommon.getTime();
		log.debug("exe sql start");
		if (_debug && param != null && param.length > 0) {
			log.debug("exe sql param=" + JSON.toJSONString(param));
		}
		if(isMySQL()){
			sql = sql + " limit 1";
		}else{
			sql = "select * from (" + sql + ") where rownum <=1 ";
		}
		
		Map<String, Object> r = null;
		try {
			r = jtpl.queryForMap(sql, param);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}

		log.debug("exe sql finish:" + DbCommon.runTime(t));
		return r;
	}

	/**
	 * 查询列表
	 * 
	 * @param sql
	 * @return
	 */
	public List<Map<String, Object>> queryList(String sql) throws Exception {
		double t = DbCommon.getTime();
		log.debug("exe sql start");
		List<Map<String, Object>> r = jtpl.queryForList(sql);
		log.debug("exe sql finish:" + DbCommon.runTime(t));
		return r;
	}

	/**
	 * 查询列表--带参数
	 * 
	 * @param sql
	 * @return
	 */
	public List<Map<String, Object>> queryList(String sql, List<Object> param) throws Exception {
		return queryList(sql, param.toArray());
	}
	/**
	 * 查询列表--带参数
	 * @param sql
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryList(String sql, Object[] param) throws Exception {
		double t = DbCommon.getTime();
		log.debug("exe sql start");

		List<Map<String, Object>> r = jtpl.queryForList(sql, param);
		if (_debug && param != null && param.length > 0) {
			log.debug("exe sql param=" + JSON.toJSONString(param));
		}
		log.debug("exe sql finish:" + DbCommon.runTime(t));
		return r;
	}

	/**
	 * 列表，限制数量
	 * 
	 * @param sql
	 * @param num
	 * @return
	 */
	public List<Map<String, Object>> queryList(String sql, int num) throws Exception {
		// System.out.println("sql:" + sql);
		if(isMySQL()){
			sql = sql + " limit ? ";
		}else{
			sql = "select * from (" + sql + ") where rownum <= ? ";
		}
		

		double t = DbCommon.getTime();
		log.debug("exe sql start");
		List<Map<String, Object>> r = jtpl.queryForList(sql, num);
		log.debug("exe sql finish:" + DbCommon.runTime(t));
		return r;
	}

	/**
	 * 列表，限制数量--带参数
	 * 
	 * @param sql
	 * @param num
	 * @return
	 */
	public List<Map<String, Object>> queryList(String sql, List<Object> param, int num) throws Exception {
		return queryList(sql, param.toArray(), num);
	}
	/**
	 * 列表，限制数量--带参数
	 * @param sql
	 * @param param
	 * @param num
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryList(String sql, Object[] param, int num) throws Exception {
		// System.out.println("sql:" + sql);
		sql = "select * from (" + sql + ") where rownum <= " + num + " ";

		double t = DbCommon.getTime();
		log.debug("exe sql start");

		List<Map<String, Object>> r = jtpl.queryForList(sql, param);
		if (_debug && param != null && param.length > 0) {
			log.debug("exe sql param=" + JSON.toJSONString(param));
		}
		log.debug("exe sql finish:" + DbCommon.runTime(t));
		return r;
	}

	/**
	 * 分页列表
	 * 
	 * @param sql
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public List<Map<String, Object>> queryList(String sql, int pageSize, int curPage) throws Exception {
		// System.out.println("sql:" + sql);
		int beginNum = (curPage - 1) * pageSize;
		int endNum = (curPage * pageSize);
		/*
		 * String ptn = "SELECT * FROM (" +
		 * "SELECT T.*, ROWNUM RN, (SELECT COUNT(*) FROM (%%s)) as TOTAL_RECORD FROM (%%s) T WHERE ROWNUM <= %d) WHERE RN > %d"
		 * ;
		 */
		if(isMySQL()){
			sql = sql + " limit " + beginNum + ", " + endNum + "";
		}else{
			sql = "SELECT * FROM (" + "SELECT T.*, ROWNUM RN FROM (" + sql + ") T WHERE ROWNUM <= " + endNum + ") WHERE RN > " + beginNum + "";
		}
		

		double t = DbCommon.getTime();
		log.debug("exe sql start");
		List<Map<String, Object>> r = jtpl.queryForList(sql);
		log.debug("exe sql finish:" + DbCommon.runTime(t));
		return r;
	}

	/**
	 * 分页列表--带参数
	 * 
	 * @param sql
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public List<Map<String, Object>> queryList(String sql, List<Object> param, int pageSize, int curPage) throws Exception {
		return queryList(sql, param.toArray(), pageSize, curPage);
	}
	/**
	 * 分页列表--带参数
	 * @param sql
	 * @param param
	 * @param pageSize
	 * @param curPage
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryList(String sql, Object[] param, int pageSize, int curPage) throws Exception {
		// System.out.println("sql:" + sql);
		int beginNum = (curPage - 1) * pageSize;
		int endNum = (curPage * pageSize);
		
		if(isMySQL()){
			sql = sql + " limit " + beginNum + ", " + endNum + "";
		}else{
			sql = "SELECT * FROM (" + "SELECT T.*, ROWNUM RN FROM (" + sql + ") T WHERE ROWNUM <= " + endNum + ") WHERE RN > " + beginNum + "";
		}
		

		double t = DbCommon.getTime();
		log.debug("exe sql start");

		List<Map<String, Object>> r = jtpl.queryForList(sql, param);
		if (_debug && param != null && param.length > 0) {
			log.debug("exe sql param=" + JSON.toJSONString(param));
		}
		log.debug("exe sql finish:" + DbCommon.runTime(t));
		return r;
	}

	/**
	 * 统计数量
	 * 
	 * @param sql
	 * @return
	 */
	public int count(String sql) throws Exception {

		double t = DbCommon.getTime();
		log.debug("exe sql start");
		int r = jtpl.queryForObject("select count(*) from (" + sql + ")", Integer.class);
		log.debug("exe sql finish:" + DbCommon.runTime(t));
		return r;
	}

	/**
	 * 统计数量--带参数
	 * @param sql
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int count(String sql, List<Object> param) throws Exception {
		return count(sql, param.toArray());
	}
	/**
	 * 统计数量--带参数
	 * @param sql
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int count(String sql, Object[] param) throws Exception {
		double t = DbCommon.getTime();
		log.debug("exe sql start");

		int r = jtpl.queryForObject("select count(*) from (" + sql + ") count_c", param, Integer.class);
		if (_debug && param != null && param.length > 0) {
			log.debug("exe sql param=" + JSON.toJSONString(param));
		}
		log.debug("exe sql finish:" + DbCommon.runTime(t));
		return r;
	}

	/**
	 * 获取数量
	 * 
	 * @param sql
	 * @return
	 */
	public int queryInt(String sql) throws Exception {
		double t = DbCommon.getTime();
		log.debug("exe sql start");
		int r = jtpl.queryForObject(sql, Integer.class);
		log.debug("exe sql finish:" + DbCommon.runTime(t));
		return r;
	}

	/**
	 * 获取数量--带参数
	 * 
	 * @param sql
	 * @return
	 */
	public int queryInt(String sql, List<Object> param) throws Exception {
		return queryInt(sql, param.toArray());
	}
	/**
	 * 获取数量--带参数
	 * @param sql
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int queryInt(String sql, Object[] param) throws Exception {

		double t = DbCommon.getTime();
		log.debug("exe sql start");

		int r = jtpl.queryForObject(sql, param, Integer.class);
		if (_debug && param != null && param.length > 0) {
			log.debug("exe sql param=" + JSON.toJSONString(param));
		}
		log.debug("exe sql finish:" + DbCommon.runTime(t));
		return r;
	}


	/**
	 * 获取数量
	 * 
	 * @param sql
	 * @return
	 */
	public long queryLong(String sql) throws Exception {
		double t = DbCommon.getTime();
		log.debug("exe sql start");
		long r = jtpl.queryForObject(sql, Long.class);
		log.debug("exe sql finish:" + DbCommon.runTime(t));
		return r;
	}

	/**
	 * 获取数量--带参数
	 * 
	 * @param sql
	 * @return
	 */
	public long queryLong(String sql, List<Object> param) throws Exception {
		return queryLong(sql, param.toArray());
	}
	/**
	 * 获取数量--带参数
	 * @param sql
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public long queryLong(String sql, Object[] param) throws Exception {

		double t = DbCommon.getTime();
		log.debug("exe sql start");

		long r = jtpl.queryForObject(sql, param, Long.class);
		if (_debug && param != null && param.length > 0) {
			log.debug("exe sql param=" + JSON.toJSONString(param));
		}
		log.debug("exe sql finish:" + DbCommon.runTime(t));
		return r;
	}
	
	/**
	 * 获取下一个SEQUENCE sql
	 * @param sName
	 * @return
	 */
	public String getSequenceNext(String sName) {
		return sName + ".nextval";
	}

	/**
	 * 获取下一个SEQUENCE val
	 * @param sName
	 * @return
	 * @throws Exception
	 */
	public long getSequenceNextval(String sName) throws Exception {
		return queryLong("SELECT " + sName + ".nextval FROM dual");
	}

	/**
	 * 获取当前SEQUENCE val
	 * @param sName
	 * @return
	 * @throws Exception
	 */
	public long getSequenceCurrval(String sName) throws Exception {
		return queryLong("SELECT " + sName + ".currval FROM dual");
	}
 
	/**
	 * 美化sql
	 * @param sql
	 * @return
	 */
	public String formatSql(String sql){
		return SQLUtils.format(sql, isMySQL() ? JdbcUtils.MYSQL : JdbcUtils.ORACLE); 
		// 根据数据库类型不同选择不同的dbType，包括Oracle、MySql、POSTGRESQL等等
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//dataSource.getConnection().getMetaData().getDbProductName()
	}

}