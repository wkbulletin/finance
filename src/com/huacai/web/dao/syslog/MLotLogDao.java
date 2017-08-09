package com.huacai.web.dao.syslog;

import java.util.ArrayList;
import java.util.List;

import libcore.util.DbUtil;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huacai.web.common.DbCommon;
import com.huacai.web.db.datasource.BossDB;

@Repository("mLotLogDao")
@Configurable(preConstruction=true)
public class MLotLogDao{
	private static final Logger log=LogManager.getLogger(MLotLogDao.class);
	
	@Autowired
	private BossDB bossDB;
	/**
	 * 获取彩种列表
	 * @return
	 */
	public JSONArray getLotList() {
		String sql = "SELECT GAME_ID AS key,GAME_NAME AS val FROM CTS_GAME " +
				" WHERE 1=1 ";
		sql += " ORDER BY GAME_ID";
		
		JSONArray jsonList = new JSONArray();
		try {
			jsonList = DbCommon.field2json(bossDB.queryList(sql));
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return jsonList;
	}

	/**
	 * 获取列表
	 * @return
	 */
	public JSONObject getLogList(int pageSize, int curPage, JSONObject queryData) {
		String sql = "SELECT t1.*,t2.admin_loginname,t2.admin_realname,t3.GAME_NAME AS GAME_NAME FROM TRD_GAME_TERMRECORD t1 " +
				" LEFT JOIN BOSS_ADMIN_USER t2 ON OPERID=ADMIN_ID "+
				" LEFT JOIN CTS_GAME t3 ON t1.GAMEID=t3.GAME_ID "+
				" WHERE 1=1 ";
		
		List<Object> param = new ArrayList<Object>();
		if(queryData!=null){
			String logs_admin_loginname = DbUtil.escapeSQLLike(queryData.getString("logs_admin_loginname"));
			if(logs_admin_loginname!=null&&logs_admin_loginname.length()>0){
				if(logs_admin_loginname.equals("系统")){
					sql += " and OPERID =0";
				}else{
					sql += " and admin_loginname like ? ESCAPE '/'";
					param.add("%"+logs_admin_loginname+"%");
				}

			}
			String logs_admin_realname = DbUtil.escapeSQLLike(queryData.getString("logs_admin_realname"));
			if(logs_admin_realname!=null&&logs_admin_realname.length()>0){
				sql += " and admin_realname like ? ESCAPE '/'";
				param.add("%"+logs_admin_realname+"%");
			}
			
			String logs_lot_id = DbUtil.escapeSQLLike(queryData.getString("logs_lot_id"));
			if(logs_lot_id!=null&&logs_lot_id.length()>0){
				sql += " and GAMEID=?";
				param.add(logs_lot_id);
			}
//			String log_status = DbUtil.escapeSQLLike(queryData.getString("log_status"));
//			if(log_status!=null&&log_status.length()>0){
//				sql += " and log_status=?";
//				param.add(log_status);
//			}
			
			String begin_time = DbUtil.escapeSQLLike(queryData.getString("begin_time"));
			String end_time = DbUtil.escapeSQLLike(queryData.getString("end_time"));
			if(begin_time!=null&&begin_time.length()>0&&end_time!=null&&end_time.length()>0){
				
				sql += " and OPERTIME >= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')";
				sql += " and OPERTIME <= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')";
				
				param.add(begin_time + " 00:00:00");
				param.add(end_time + " 23:59:59");
			}
		}
		
		sql += " ORDER BY t1.OPERTIME desc  ";
		
		JSONObject logsListData = new JSONObject();
		JSONArray jsonList = new JSONArray();
		try {
			int totalCount = bossDB.count(sql, param);
			logsListData.put("count", totalCount);
			if(totalCount>0){
				jsonList = DbCommon.field2json(bossDB.queryList(sql, param, pageSize, curPage));
			}
			logsListData.put("list", jsonList);
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return logsListData;
	}
	
	/**
	 * 导出列表
	 * @return
	 */
	public JSONObject getLogExportList(JSONObject queryData) {
		int maxNum = 10000;
		String sql = "SELECT t1.*,t2.admin_loginname,t2.admin_realname,t3.GAME_NAME AS GAME_NAME FROM TRD_GAME_TERMRECORD t1 " +
				" LEFT JOIN BOSS_ADMIN_USER t2 ON OPERID=ADMIN_ID "+
				" LEFT JOIN CTS_GAME t3 ON t1.GAMEID=t3.GAME_ID "+
				" WHERE 1=1 ";
		
		
		List<Object> param = new ArrayList<Object>();
		if(queryData!=null){
			String logs_admin_loginname = queryData.getString("logs_admin_loginname");
			if(logs_admin_loginname!=null&&logs_admin_loginname.length()>0){
				sql += " and admin_loginname like ? ESCAPE '/'";
				param.add("%"+logs_admin_loginname+"%");
			}
			String logs_admin_realname = queryData.getString("logs_admin_realname");
			if(logs_admin_realname!=null&&logs_admin_realname.length()>0){
				sql += " and admin_realname like ? ESCAPE '/'";
				param.add("%"+logs_admin_realname+"%");
			}
			
			String logs_lot_id = queryData.getString("logs_lot_id");
			if(logs_lot_id!=null&&logs_lot_id.length()>0){
				sql += " and GAMEID=?";
				param.add(logs_lot_id);
			}
//			String log_status = DbUtil.escapeSQLLike(queryData.getString("log_status"));
//			if(log_status!=null&&log_status.length()>0){
//				sql += " and log_status=?";
//				param.add(log_status);
//			}
			
			String begin_time = queryData.getString("begin_time");
			String end_time = queryData.getString("end_time");
			if(begin_time!=null&&begin_time.length()>0&&end_time!=null&&end_time.length()>0){
				
				sql += " and OPERTIME >= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')";
				sql += " and OPERTIME <= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')";
				
				param.add(begin_time + " 00:00:00");
				param.add(end_time + " 23:59:59");
			}
		}
		
		sql += " ORDER BY t1.OPERTIME desc ";
		
		JSONObject logsListData = new JSONObject();
		JSONArray jsonList = new JSONArray();
		try {
			int totalCount = bossDB.count(sql, param);
			if(totalCount<=maxNum){
				logsListData.put("count", totalCount);
				if(totalCount>0){
					jsonList = DbCommon.field2json(bossDB.queryList(sql, param, maxNum));
				}
				logsListData.put("list", jsonList);
			}else{
				logsListData.put("count", -1);
			}
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return logsListData;
	}
	
	/**
	 * 查询控审日志
	 * @param num
	 * @return
	 */
	public JSONArray getSysLogListInfo(String num) {
		JSONArray jsonList = new JSONArray();
		try {
			
			String sql = "SELECT t1.*,t3.GAME_NAME AS GAME_NAME FROM TRD_GAME_TERMRECORD t1 " +
					" LEFT JOIN CTS_GAME t3 ON t1.GAMEID=t3.GAME_ID "+
					" order by t1.OPERTIME desc  ";

			sql = "select * from (" + sql + ") where rownum <=? ";
			
			jsonList= DbCommon.field2json(bossDB.queryList(sql.toString(),new Object[]{ num}));
		} catch (Exception e) {
			log.error("DB error", e);
			e.printStackTrace();
		}
		return jsonList;
	}
	
	
	/**
	 * 查询正常及异常事件
	 * @param pid
	 * @return
	 */
	public JSONObject countSysLogByType() {
		String nromalSql = "SELECT * FROM TRD_GAME_TERMRECORD WHERE LOGLEVEL = 1 and opertime >= now() -1";
		
		String unnormalSql = "SELECT * FROM TRD_GAME_TERMRECORD WHERE LOGLEVEL != 1 and opertime >= now() -1";
		
		JSONObject json = new JSONObject();
		try {
			int normalCount = bossDB.count(nromalSql);
			
			int unnormalCount = bossDB.count(unnormalSql);
			
			json.put("normal", normalCount);
			json.put("unnormal", unnormalCount);
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return json;
	}

	
	/**
	 * 查询所有控审日志
	 * @return
	 */
	public JSONArray getAllSysLogListInfo() {
		JSONArray jsonList = new JSONArray();
		try {
			
			String sql = "SELECT t1.*,TO_CHAR(t1.OPERTIME,'YYYY-MM-DD HH24:mm:ss') as uptime, t2.admin_loginname,t2.admin_realname,t3.GAME_NAME AS GAME_NAME FROM TRD_GAME_TERMRECORD t1 " +
					" LEFT JOIN BOSS_ADMIN_USER t2 ON OPERID=ADMIN_ID "+
					" LEFT JOIN CTS_GAME t3 ON t1.GAMEID=t3.GAME_ID where t1.opertime >= now() - 3 "+
					" order by t1.OPERTIME desc  ";

			sql = "select * from (" + sql + ") where rownum <=? ";
			
			jsonList= DbCommon.field2json(bossDB.queryList(sql.toString(),new Object[]{ 100 }));
		} catch (Exception e) {
			log.error("DB error", e);
			e.printStackTrace();
		}
		return jsonList;
	}
	
}
