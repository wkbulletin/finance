package com.huacai.web.dao.privilege;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huacai.web.common.DbCommon;
import com.huacai.web.db.datasource.BossDB;

import libcore.util.DbUtil;

@Repository("adminDao")
@Configurable(preConstruction=true)
public class AdminDao{
	private static final Logger log=LogManager.getLogger(AdminDao.class);
	
	@Autowired
	private BossDB bossDB;
	
	
	/**
	 * 获取角色权限列表
	 * @param pid
	 * @return
	 */
	public JSONArray getRoleList(long admin_id) {
		String sql = "SELECT * FROM BOSS_ADMIN_ROLE LEFT JOIN BOSS_ADMIN_RELATION_RU on RELATION_ROLE_ID=ROLE_ID and RELATION_ADMIN_ID= ? ";
		sql += " WHERE ROLE_STATUS=1 ";
		sql += " ORDER BY ROLE_ID ";
		
		JSONArray jsonList = new JSONArray();
		try {
			jsonList = DbCommon.field2json(bossDB.queryList(sql, new Object[]{admin_id}));
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return jsonList;
	}
	/**
	 * 批量更新
	 * @param batchArgs
	 * @return
	 */
	public boolean batchUpdateAdminRole(long admin_id, List<Object[]> batchArgs) {
		DataSourceTransactionManager tran = bossDB.getTransManager();
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = tran.getTransaction(def);
		try{
			String sql = "delete from BOSS_ADMIN_RELATION_RU where RELATION_ADMIN_ID=?";
			bossDB.update(sql, new Object[]{admin_id});
			
			
			if(batchArgs.size()>0){

				sql = "INSERT INTO BOSS_ADMIN_RELATION_RU"; 
				sql += "(RELATION_ADMIN_ID, RELATION_ROLE_ID, RELATION_CREATE_TIME) "; 
				sql += " VALUES ";  
				sql += " (?, ?, now())"; 
				bossDB.batchUpdate(sql, batchArgs);
			}
			 
			return true;
		}catch(Exception ex){
			tran.rollback(status); 	
			log.error("DB error", ex);
		}finally{
			tran.commit(status);
		}
		
		return false;
	}
	
	/**
	 * 获取单条
	 * @param pid
	 * @return
	 */
	public JSONObject getAdmin(long id) {
		String sql = "SELECT * FROM BOSS_ADMIN_USER WHERE ADMIN_ID = ?";
		 
		
		JSONObject json = new JSONObject();
		try {
			json = DbCommon.field2json(bossDB.queryMap(sql, new Object[] {id}));
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return json;
	}
	/**
	 * 获取检查名称是否存在 
	 * @param pid
	 * @return
	 */
	public int checkAdminLoginNameIsExists(long id, String roleName) {
		try {
			String sql = "SELECT count(*) FROM BOSS_ADMIN_USER WHERE ADMIN_ID <> ? and ADMIN_LOGINNAME=? ";
			return bossDB.queryInt(sql, new Object[] {id, roleName})>=1 ? 1 : 0;
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return -1;
	}
 
	/**
	 * 获取列表
	 * @param pid
	 * @return
	 */
	public JSONObject getAdminList(int pageSize, int curPage, JSONObject queryData) {
//		String sql = "SELECT t1.*, t2.ROLE_NAME " +
//				"FROM ADMIN_USER t1 " +
//				"LEFT JOIN (select RELATION_ADMIN_ID, ROLE_NAME from ADMIN_ROLE,(select RELATION_ADMIN_ID,min(RELATION_ROLE_ID) RELATION_ROLE_ID from ADMIN_RELATION_ROLE_USER GROUP BY RELATION_ADMIN_ID) where RELATION_ROLE_ID=ROLE_ID) t2 ON t1.ADMIN_ID=t2.RELATION_ADMIN_ID " +
//				"WHERE 1=1 ";
		String sql = "SELECT t1.*,(\n" +
				"		select group_concat(ROLE_NAME) AS ROLE_NAME from BOSS_ADMIN_ROLE,BOSS_ADMIN_RELATION_RU where RELATION_ROLE_ID=ROLE_ID and RELATION_ADMIN_ID=ADMIN_ID \n" +
				"		) AS ROLE_NAME\n" +
				"FROM BOSS_ADMIN_USER t1 \n" +
				"WHERE 1=1 ";
		
		
		List<Object> param = new ArrayList<Object>();
		if(queryData!=null){
			String admin_loginname = queryData.getString("admin_loginname");
			if(admin_loginname!=null&&admin_loginname.length()>0){
				admin_loginname = DbUtil.escapeSQLLike(admin_loginname);
				sql += " and admin_loginname like ? ESCAPE '/'";
				param.add("%"+admin_loginname+"%");
			}
			String admin_realname = queryData.getString("admin_realname");
			if(admin_realname!=null&&admin_realname.length()>0){
				admin_realname =DbUtil.escapeSQLLike(admin_realname);
				sql += " and admin_realname like ? ESCAPE '/'";
				param.add("%"+admin_realname+"%");
			}
			
			String admin_status = queryData.getString("admin_status");
			if(admin_status!=null&&admin_status.length()>0){
				sql += " and admin_status=?";
				param.add(admin_status);
			}
			
			String admin_type = queryData.getString("admin_type");
			if(admin_type!=null&&admin_type.length()>0){
				sql += " and admin_type=?";
				param.add(admin_type);
			}
			
			/*String admin_level = queryData.getString("admin_level");
			if(admin_level!=null&&admin_level.length()>0){
				sql += " and admin_level=?";
				param.add(admin_level);
			}*/
			
			String admin_areacode = queryData.getString("admin_areacode");
			if(admin_areacode!=null&&admin_areacode.length()>0){
				sql += " and ADMIN_AREACODE=?";
				param.add(admin_areacode);
			}
			
		}
		 
		
		//sql += " ORDER BY ADMIN_STATUS DESC,ADMIN_ID DESC";
		sql += " ORDER BY ADMIN_CREATE_TIME desc";
		
		JSONObject roleListData = new JSONObject();
		JSONArray jsonList = new JSONArray();
		try {
			int totalCount = bossDB.count(sql, param);
			roleListData.put("count", totalCount);
			if(totalCount>0){
				jsonList = DbCommon.field2json(bossDB.queryList(sql, param, pageSize, curPage));
			}
			roleListData.put("list", jsonList);
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return roleListData;
	}
	
	/**
	 * 插入
	 * @param map
	 * @return
	 */
	public long insertAdmin(Map<String, Object> map) {
		try {
			return bossDB.sql_insert("BOSS_ADMIN_USER", map);
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return 0;
	}
	
	/**
	 * 更新
	 * @param id
	 * @param map
	 * @return
	 */
	public int updateAdmin(long id, Map<String, Object> map) {
		try {
			return bossDB.sql_update("BOSS_ADMIN_USER", map, "ADMIN_ID=?", new Object[] {id});
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return 0;
	}
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public int delAdmin(long id) {
		try {
			String sql = "delete from BOSS_ADMIN_USER where ADMIN_ID=?";
			return bossDB.update(sql, new Object[] {id});
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return 0;
	}
	
	
	/**
	 * 更新状态
	 * @param id
	 * @param map
	 * @return
	 */
	public int updateAdminStauts(long id) {
		try {
			String sql = "update BOSS_ADMIN_USER set ADMIN_STATUS=1-ADMIN_STATUS where ADMIN_ID=?";
			return bossDB.update(sql, new Object[] {id});
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return 0;
	}
	
	public int getAdminLevel(long admin_id){
		String sql="select ADMIN_LEVEL from BOSS_ADMIN_USER where ADMIN_ID=?";
		try {
			return bossDB.queryInt(sql, new Object[]{admin_id});
		} catch (Exception e) {
			log.error("DB error", e);
			e.printStackTrace();
		}
		return 0;
	}
	
}
