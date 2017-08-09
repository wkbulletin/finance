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

@Repository("roleDao")
@Configurable(preConstruction=true)
public class RoleDao{
	private static final Logger log=LogManager.getLogger(RoleDao.class);
	
	@Autowired
	private BossDB bossDB;
	
	
	/**
	 * 获取角色权限列表
	 * @param pid
	 * @return
	 */
	public JSONArray getRolePrivList(int role_id) {
		String sql = "SELECT * FROM BOSS_ADMIN_PRIVILEGE LEFT JOIN BOSS_ADMIN_RELATION_RP on RELATION_PRIV_ID=PRIVILEGE_ID and RELATION_ROLE_ID= ? ";
		sql += " WHERE PRIVILEGE_STATUS=1 ";
		sql += " ORDER BY PRIVILEGE_PID,PRIVILEGE_ORDER,PRIVILEGE_ID";
		
		JSONArray jsonList = new JSONArray();
		try {
			jsonList = DbCommon.field2json(bossDB.queryList(sql, new Object[]{role_id}));
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
	public boolean batchUpdateRolePriv(int role_id, List<Object[]> batchArgs) {
		DataSourceTransactionManager tran = bossDB.getTransManager();
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = tran.getTransaction(def);
		try{
			String sql = "delete from BOSS_ADMIN_RELATION_RP where RELATION_ROLE_ID=?";
			bossDB.update(sql, new Object[]{role_id});
			
			
			if(batchArgs.size()>0){

				sql = "INSERT INTO BOSS_ADMIN_RELATION_RP"; 
				sql += "(RELATION_ROLE_ID, RELATION_PRIV_ID, RELATION_CREATE_TIME) "; 
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
	public JSONObject getRole(int id) {
		String sql = "SELECT * FROM BOSS_ADMIN_ROLE WHERE ROLE_ID = ?";
		 
		
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
	public int checkRoleNameIsExists(int id, String roleName) {
		try {
			String sql = "SELECT count(*) FROM BOSS_ADMIN_ROLE WHERE ROLE_ID <> ? and ROLE_NAME=? ";
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
	public JSONObject getRoleList(int pageSize, int curPage, JSONObject queryData) {
		String sql = "SELECT * FROM BOSS_ADMIN_ROLE WHERE 1=1 ";
		
		List<Object> param = new ArrayList<Object>();
		if(queryData!=null){
			String role_name = DbUtil.escapeSQLLike(queryData.getString("role_name"));
			if(role_name!=null&&role_name.length()>0){
				sql += " and role_name like ? ESCAPE '/'";
				param.add("%"+role_name+"%");
			}
			
			String role_status = DbUtil.escapeSQLLike(queryData.getString("role_status"));
			if(role_status!=null&&role_status.length()>0){
				sql += " and role_status=?";
				param.add(role_status);
			}
			
			/*String role_level = DbUtil.escapeSQLLike(queryData.getString("role_level"));
			if(role_level!=null&&role_level.length()>0){
				sql += " and role_level=?";
				param.add(role_level);
			}*/
			
		}
		 
		
		sql += " ORDER BY role_id DESC";
		
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
	public int insertRole(Map<String, Object> map) {
		try {
			return bossDB.sql_insert("BOSS_ADMIN_ROLE", map);
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
	public int updateRoleStauts(int id) {
		try {
			String sql = "update BOSS_ADMIN_ROLE set ROLE_STATUS=1-ROLE_STATUS where ROLE_ID=?";
			int result = bossDB.update(sql, new Object[] {id});
			
			//如果状态为禁用，解除管理员绑定的状态
			JSONObject roleInfo = getRole(id);
			if(roleInfo!=null&&roleInfo.getIntValue("role_status")!=1){
				sql = "delete from BOSS_ADMIN_RELATION_RU where RELATION_ROLE_ID=?";
				bossDB.update(sql, new Object[]{id});
			}
			
			return result;
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
	public int updateRole(int id, Map<String, Object> map) {
		try {
			return bossDB.sql_update("BOSS_ADMIN_ROLE", map, "ROLE_ID=?", new Object[] {id});
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
	public int delRole(int id) {
		try {
			String sql = "delete from BOSS_ADMIN_ROLE where ROLE_ID=?";
			return bossDB.update(sql, new Object[] {id});
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return 0;
	}
	
	public int getRoleLevelById(int role_id){
		try {
			String sql = "select role_level from BOSS_ADMIN_ROLE where ROLE_ID=?";
			return bossDB.queryInt(sql, new Object[] {role_id});
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return 0;
	}
	
}
