package com.huacai.web.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.util.JdbcUtils;
import com.alibaba.fastjson.JSON;
import com.huacai.web.common.DbCommon;

public class BaseDao extends BaseDataSourceDao {
	private static final Logger log = LogManager.getLogger(BaseDao.class);
	private static boolean _debug = true;

	
	/**
	 * 更新表
	 * @param tbName 表名
	 * @param map 字段及对应值
	 * @param where 更新条件
	 * @return
	 * @throws Exception
	 */
	public int _sql_update(String tbName, Map<String, Object> map, String where) throws Exception {
		return _sql_update(db, tbName, map, where, new Object[]{});
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
	public int _sql_update(String tbName, Map<String, Object> map, String where, List<Object> whereParams) throws Exception {
		return _sql_update(db, tbName, map, where, whereParams != null ? whereParams.toArray() : new Object[]{});
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
	public int _sql_update(String tbName, Map<String, Object> map, String where, Object[] whereParams) throws Exception {
		return _sql_update(db, tbName, map, where, whereParams != null ? whereParams : new Object[]{});
	}
	
	/**
	 * 更新表
	 * @param db 数据源
	 * @param tbName 表名
	 * @param map 字段及对应值
	 * @param where 更新条件
	 * @param whereParams 更新条件值
	 * @return 
	 * @throws Exception
	 */
	public int _sql_update(DbStruct db, String tbName, Map<String, Object> map, String where, List<Object> whereParams) throws Exception {
		return _sql_update(db, tbName, map, where, whereParams != null ? whereParams.toArray() : new Object[]{});
	}

	/**
	 * 更新表
	 * @param db 数据源
	 * @param tbName 表名
	 * @param map 字段及对应值
	 * @param where 更新条件
	 * @param whereParams 更新条件值
	 * @return 
	 * @throws Exception
	 */
	public int _sql_update(DbStruct db, String tbName, Map<String, Object> map, String where, Object[] whereParams) throws Exception {
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
		int r = db.getJtpl().update(sql.toString(), param.toArray());
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
	public int _sql_insert(String tbName, Map<String, Object> map) throws Exception {
		return _sql_insert(db, tbName, map);
	}
	/**
	 * 插入表
	 * @param db 数据源
	 * @param tbName 表名
	 * @param map 字段及对应值
	 * @return
	 * @throws Exception
	 */
	public int _sql_insert(DbStruct db, String tbName, Map<String, Object> map) throws Exception {

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
		int r = db.getJtpl().update(sql.toString(), param.toArray());
		log.debug("exe sql finish:" + DbCommon.runTime(t));
		return r;
	}

	/**
	 * 批量更新或插入
	 * @param sql 多个sql
	 * @return
	 * @throws Exception
	 */
	public int[] _batchUpdate(String sql[]) throws Exception {
		double t = DbCommon.getTime();
		log.debug("exe sql start");
		int[] r = db.getJtpl().batchUpdate(sql);
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
	public int[] _batchUpdate(String sql, List<Object[]> batchArgs) throws Exception {
		return _batchUpdate(db, sql, batchArgs);
	}
	/**
	 * 批量更新或插入--带参数
	 * @param db
	 * @param sql
	 * @param batchArgs 多个参数
	 * @return
	 * @throws Exception
	 */
	public int[] _batchUpdate(DbStruct db, String sql, List<Object[]> batchArgs) throws Exception {

		double t = DbCommon.getTime();
		log.debug("exe sql start");
		int[] r = db.getJtpl().batchUpdate(sql, batchArgs);

		log.debug("exe sql finish:" + DbCommon.runTime(t));
		return r;
	}

	/**
	 * 更新或插入
	 * 
	 * @param sql
	 * @return
	 */
	public int _update(String sql) throws Exception {
		return _update(db, sql);
	}
	/**
	 * 更新或插入
	 * @param db
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public int _update(DbStruct db, String sql) throws Exception {
		double t = DbCommon.getTime();
		log.debug("exe sql start");
		int r = db.getJtpl().update(sql);
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
	public int _update(String sql, List<Object> param) throws Exception {
		return _update(db, sql, param.toArray());
	}
	/**
	 * 更新或插入--带参数
	 * @param sql
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int _update(String sql, Object[] param) throws Exception {
		return _update(db, sql, param);
	}
	/**
	 * 更新或插入--带参数
	 * @param db
	 * @param sql
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int _update(DbStruct db, String sql, Object[] param) throws Exception {

		double t = DbCommon.getTime();
		log.debug("exe sql start");

		int r = db.getJtpl().update(sql, param);
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
	public Map<String, Object> _queryMap(String sql) throws Exception {
		return _queryMap(db, sql);
	}
	/**
	 * 查询单记录
	 * @param db
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> _queryMap(DbStruct db, String sql) throws Exception {
		double t = DbCommon.getTime();
		log.debug("exe sql start");
		sql = "select * from (" + sql + ") where rownum <=1 ";
		Map<String, Object> r = null;
		try {
			r = db.getJtpl().queryForMap(sql);
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
	public Map<String, Object> _queryMap(String sql, List<Object> param) throws Exception {
		return _queryMap(db, sql, param.toArray());
	}
	/**
	 * 查询单记录--带参数
	 * @param sql
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> _queryMap(String sql, Object[] param) throws Exception {
		return _queryMap(db, sql, param);
	}
	/**
	 * 查询单记录--带参数
	 * @param db
	 * @param sql
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> _queryMap(DbStruct db, String sql, Object[] param) throws Exception {
		double t = DbCommon.getTime();
		log.debug("exe sql start");
		if (_debug && param != null && param.length > 0) {
			log.debug("exe sql param=" + JSON.toJSONString(param));
		}
		if(db.isMySQL()){
			sql = sql + " limit 1";
		}else{
			sql = "select * from (" + sql + ") where rownum <=1 ";
		}
		
		Map<String, Object> r = null;
		try {
			r = db.getJtpl().queryForMap(sql, param);
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
	public List<Map<String, Object>> _queryList(String sql) throws Exception {
		return _queryList(db, sql);
	}
	/**
	 * 查询列表
	 * @param db
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> _queryList(DbStruct db, String sql) throws Exception {
		double t = DbCommon.getTime();
		log.debug("exe sql start");
		List<Map<String, Object>> r = db.getJtpl().queryForList(sql);
		log.debug("exe sql finish:" + DbCommon.runTime(t));
		return r;
	}

	/**
	 * 查询列表--带参数
	 * 
	 * @param sql
	 * @return
	 */
	public List<Map<String, Object>> _queryList(String sql, List<Object> param) throws Exception {
		return _queryList(db, sql, param.toArray());
	}
	/**
	 * 查询列表--带参数
	 * @param sql
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> _queryList(String sql, Object[] param) throws Exception {
		return _queryList(db, sql, param);
	}
	/**
	 * 查询列表--带参数
	 * @param db
	 * @param sql
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> _queryList(DbStruct db, String sql, Object[] param) throws Exception {
		double t = DbCommon.getTime();
		log.debug("exe sql start");

		List<Map<String, Object>> r = db.getJtpl().queryForList(sql, param);
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
	public List<Map<String, Object>> _queryList(String sql, int num) throws Exception {
		return _queryList(db, sql, num);
	}
	/**
	 * 列表，限制数量
	 * @param db
	 * @param sql
	 * @param num
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> _queryList(DbStruct db, String sql, int num) throws Exception {
		// System.out.println("sql:" + sql);
		if(db.isMySQL()){
			sql = sql + " limit ? ";
		}else{
			sql = "select * from (" + sql + ") where rownum <= ? ";
		}
		

		double t = DbCommon.getTime();
		log.debug("exe sql start");
		List<Map<String, Object>> r = db.getJtpl().queryForList(sql, num);
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
	public List<Map<String, Object>> _queryList(String sql, List<Object> param, int num) throws Exception {
		return _queryList(db, sql, param.toArray(), num);
	}
	/**
	 * 列表，限制数量--带参数
	 * @param sql
	 * @param param
	 * @param num
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> _queryList(String sql, Object[] param, int num) throws Exception {
		return _queryList(db, sql, param, num);
	}
	/**
	 * 列表，限制数量--带参数
	 * @param db
	 * @param sql
	 * @param param
	 * @param num
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> _queryList(DbStruct db, String sql, Object[] param, int num) throws Exception {
		// System.out.println("sql:" + sql);
		sql = "select * from (" + sql + ") where rownum <= " + num + " ";

		double t = DbCommon.getTime();
		log.debug("exe sql start");

		List<Map<String, Object>> r = db.getJtpl().queryForList(sql, param);
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
	public List<Map<String, Object>> _queryList(String sql, int pageSize, int curPage) throws Exception {
		return _queryList(db, sql, pageSize, curPage);
	}
	/**
	 * 分页列表
	 * @param db
	 * @param sql
	 * @param pageSize
	 * @param curPage
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> _queryList(DbStruct db, String sql, int pageSize, int curPage) throws Exception {
		// System.out.println("sql:" + sql);
		int beginNum = (curPage - 1) * pageSize;
		int endNum = (curPage * pageSize);
		/*
		 * String ptn = "SELECT * FROM (" +
		 * "SELECT T.*, ROWNUM RN, (SELECT COUNT(*) FROM (%%s)) as TOTAL_RECORD FROM (%%s) T WHERE ROWNUM <= %d) WHERE RN > %d"
		 * ;
		 */
		if(db.isMySQL()){
			sql = sql + " limit " + beginNum + ", " + endNum + "";
		}else{
			sql = "SELECT * FROM (" + "SELECT T.*, ROWNUM RN FROM (" + sql + ") T WHERE ROWNUM <= " + endNum + ") WHERE RN > " + beginNum + "";
		}
		

		double t = DbCommon.getTime();
		log.debug("exe sql start");
		List<Map<String, Object>> r = db.getJtpl().queryForList(sql);
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
	public List<Map<String, Object>> _queryList(String sql, List<Object> param, int pageSize, int curPage) throws Exception {
		return _queryList(db, sql, param.toArray(), pageSize, curPage);
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
	public List<Map<String, Object>> _queryList(String sql, Object[] param, int pageSize, int curPage) throws Exception {
		return _queryList(db, sql, param, pageSize, curPage);
	}
	/**
	 * 分页列表--带参数
	 * @param db
	 * @param sql
	 * @param param
	 * @param pageSize
	 * @param curPage
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> _queryList(DbStruct db, String sql, Object[] param, int pageSize, int curPage) throws Exception {
		// System.out.println("sql:" + sql);
		int beginNum = (curPage - 1) * pageSize;
		int endNum = (curPage * pageSize);
		
		if(db.isMySQL()){
			sql = sql + " limit " + beginNum + ", " + endNum + "";
		}else{
			sql = "SELECT * FROM (" + "SELECT T.*, ROWNUM RN FROM (" + sql + ") T WHERE ROWNUM <= " + endNum + ") WHERE RN > " + beginNum + "";
		}
		

		double t = DbCommon.getTime();
		log.debug("exe sql start");

		List<Map<String, Object>> r = db.getJtpl().queryForList(sql, param);
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
	public int _count(String sql) throws Exception {
		return _count(db, sql);
	}
	/**
	 * 统计数量
	 * @param db
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public int _count(DbStruct db, String sql) throws Exception {

		double t = DbCommon.getTime();
		log.debug("exe sql start");
		int r = db.getJtpl().queryForObject("select count(*) from (" + sql + ")", Integer.class);
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
	public int _count(String sql, List<Object> param) throws Exception {
		return _count(db, sql, param.toArray());
	}
	/**
	 * 统计数量--带参数
	 * @param sql
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int _count(String sql, Object[] param) throws Exception {
		return _count(db, sql, param);
	}
	/**
	 * 统计数量--带参数
	 * @param db
	 * @param sql
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int _count(DbStruct db, String sql, Object[] param) throws Exception {
		double t = DbCommon.getTime();
		log.debug("exe sql start");

		int r = db.getJtpl().queryForObject("select count(*) from (" + sql + ")", param, Integer.class);
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
	public int _queryInt(String sql) throws Exception {
		return _queryInt(db, sql);
	}
	/**
	 * 统计数量--带参数
	 * @param db
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public int _queryInt(DbStruct db, String sql) throws Exception {
		double t = DbCommon.getTime();
		log.debug("exe sql start");
		int r = db.getJtpl().queryForObject(sql, Integer.class);
		log.debug("exe sql finish:" + DbCommon.runTime(t));
		return r;
	}

	/**
	 * 获取数量--带参数
	 * 
	 * @param sql
	 * @return
	 */
	public int _queryInt(String sql, List<Object> param) throws Exception {
		return _queryInt(db, sql, param.toArray());
	}
	/**
	 * 获取数量--带参数
	 * @param sql
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int _queryInt(String sql, Object[] param) throws Exception {
		return _queryInt(db, sql, param);
	}
	/**
	 * 获取数量--带参数
	 * @param db
	 * @param sql
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int _queryInt(DbStruct db, String sql, Object[] param) throws Exception {

		double t = DbCommon.getTime();
		log.debug("exe sql start");

		int r = db.getJtpl().queryForObject(sql, param, Integer.class);
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
	public String _getSequenceNext(String sName) {
		return sName + ".nextval";
	}

	/**
	 * 获取下一个SEQUENCE val
	 * @param sName
	 * @return
	 * @throws Exception
	 */
	public int _getSequenceNextval(String sName) throws Exception {
		return this._queryInt("SELECT " + sName + ".nextval FROM dual");
	}

	public int _getSequenceNextval(DbStruct db, String sName) throws Exception {
		return this._queryInt(db, "SELECT " + sName + ".nextval FROM dual");
	}

	/**
	 * 获取当前SEQUENCE val
	 * @param sName
	 * @return
	 * @throws Exception
	 */
	public int _getSequenceCurrval(String sName) throws Exception {
		return this._queryInt("SELECT " + sName + ".currval FROM dual");
	}
	/**
	 * 获取当前SEQUENCE val
	 * @param db
	 * @param sName
	 * @return
	 * @throws Exception
	 */
	public int _getSequenceCurrval(DbStruct db, String sName) throws Exception {
		return this._queryInt(db, "SELECT " + sName + ".currval FROM dual");
	}

	/**
	 * 美化sql
	 * @param sql
	 * @return
	 */
	public String _formatSql(String sql){
		return _formatSql(db, sql); 
	}
	/**
	 * 美化sql
	 * @param db
	 * @param sql
	 * @return
	 */
	public String _formatSql(DbStruct db, String sql){
		return SQLUtils.format(sql, db.isMySQL() ? JdbcUtils.MYSQL : JdbcUtils.ORACLE); 
		// 根据数据库类型不同选择不同的dbType，包括Oracle、MySql、POSTGRESQL等等
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//dataSource.getConnection().getMetaData().getDatabaseProductName()
	}

}
