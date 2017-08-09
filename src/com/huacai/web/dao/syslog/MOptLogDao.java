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

@Repository("mOptLogDao")
@Configurable(preConstruction=true)
public class MOptLogDao{
	private static final Logger log=LogManager.getLogger(MOptLogDao.class);
	
	@Autowired
	private BossDB bossDB;
	

	/**
	 * 获取列表
	 * @return
	 */
	public JSONObject getLogList(int pageSize, int curPage, JSONObject queryData) {
		String sql = "SELECT * FROM BOSS_LOGS_OPERATE WHERE 1=1 ";
		
		List<Object> param = new ArrayList<Object>();
		if(queryData!=null){
			String logs_admin_loginname = DbUtil.escapeSQLLike(queryData.getString("logs_admin_loginname"));
			if(logs_admin_loginname!=null&&logs_admin_loginname.length()>0){
				sql += " and logs_admin_loginname like ? ESCAPE '/'";
				param.add("%"+logs_admin_loginname+"%");
			}
			String logs_admin_realname = DbUtil.escapeSQLLike(queryData.getString("logs_admin_realname"));
			if(logs_admin_realname!=null&&logs_admin_realname.length()>0){
				sql += " and logs_admin_realname like ? ESCAPE '/'";
				param.add("%"+logs_admin_realname+"%");
			}
			
			String logs_app = DbUtil.escapeSQLLike(queryData.getString("logs_app"));
			if(logs_app!=null&&logs_app.length()>0){
				sql += " and logs_app=?";
				param.add(logs_app);
			}
			
			String begin_time = DbUtil.escapeSQLLike(queryData.getString("begin_time"));
			String end_time = DbUtil.escapeSQLLike(queryData.getString("end_time"));
			if(begin_time!=null&&begin_time.length()>0&&end_time!=null&&end_time.length()>0){
				
				sql += " and logs_create_time >= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')";
				sql += " and logs_create_time <= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')";
				
				param.add(begin_time + " 00:00:00");
				param.add(end_time + " 23:59:59");
			}
		}
		
		sql += " ORDER BY logs_create_time desc, logs_id DESC";
		
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
		String sql = "SELECT * FROM BOSS_LOGS_OPERATE WHERE 1=1 ";
		
		List<Object> param = new ArrayList<Object>();
		if(queryData!=null){
			String logs_admin_loginname = queryData.getString("logs_admin_loginname");
			if(logs_admin_loginname!=null&&logs_admin_loginname.length()>0){
				sql += " and logs_admin_loginname like ? ESCAPE '/'";
				param.add("%"+logs_admin_loginname+"%");
			}
			String logs_admin_realname = queryData.getString("logs_admin_realname");
			if(logs_admin_realname!=null&&logs_admin_realname.length()>0){
				sql += " and logs_admin_realname like ? ESCAPE '/'";
				param.add("%"+logs_admin_realname+"%");
			}
			
			String logs_app = queryData.getString("logs_app");
			if(logs_app!=null&&logs_app.length()>0){
				sql += " and logs_app=?";
				param.add(logs_app);
			}
			
			String begin_time = queryData.getString("begin_time");
			String end_time =queryData.getString("end_time");
			if(begin_time!=null&&begin_time.length()>0&&end_time!=null&&end_time.length()>0){
				
				sql += " and logs_create_time >= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')";
				sql += " and logs_create_time <= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')";
				
				param.add(begin_time + " 00:00:00");
				param.add(end_time + " 23:59:59");
			}
		}
		
		sql += " ORDER BY logs_create_time desc, logs_id DESC";
		
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

}
