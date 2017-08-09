package com.huacai.web.dao.privilege;

import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huacai.web.common.DbCommon;
import com.huacai.web.db.datasource.BossDB;

import libcore.util.DbUtil;

@Repository("privilegeDao")
@Configurable(preConstruction=true)
public class PrivilegeDao{
	private static final Logger log=LogManager.getLogger(PrivilegeDao.class);
	
	@Autowired
	private BossDB bossDB;
	
	
	/**
	 * 获取单条
	 * @param pid
	 * @return
	 */
	public JSONObject getPrivilege(int id) {
		String sql = "SELECT * FROM BOSS_ADMIN_PRIVILEGE WHERE PRIVILEGE_ID = ?";
		 
		
		JSONObject json = new JSONObject();
		try {
			json = DbCommon.field2json(bossDB.queryMap(sql, new Object[] {id}));
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return json;
	}
	
	/**
	 * 获取下级ID列表
	 * @param pid
	 * @return
	 */
	public int[] getSubPrivilegeId(int id) {
		String sql = "SELECT PRIVILEGE_ID FROM BOSS_ADMIN_PRIVILEGE WHERE PRIVILEGE_PID = ?";
		
		try {
			List<Map<String, Object>> lists = bossDB.queryList(sql, new Object[] {id});
			int[] r = new int[lists.size()];
			for(int i = 0 ; i < lists.size(); i++){
				r[i] = DbUtil.getInt(lists.get(i).get("PRIVILEGE_ID"));
			}
			return r;
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return new int[0];
	}
	
	/**
	 * 获取列表
	 * @param pid
	 * @return
	 */
	public JSONArray getPrivilegeList(int pid) {
		String sql = "SELECT * FROM BOSS_ADMIN_PRIVILEGE WHERE PRIVILEGE_PID = ?  ORDER BY privilege_pid,privilege_order";
		 
		
		JSONArray jsonList = new JSONArray();
		try {
			jsonList = DbCommon.field2json(bossDB.queryList(sql, new Object[] {pid}));
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return jsonList;
	}
	/**
	 * 获取所有列表
	 * @param pid
	 * @return
	 */
	public JSONArray getAllPrivilegeList() {
		String sql = "SELECT * FROM BOSS_ADMIN_PRIVILEGE WHERE 1=1  ORDER BY privilege_pid,privilege_order";
		 
		
		JSONArray jsonList = new JSONArray();
		try {
			jsonList = DbCommon.field2json(bossDB.queryList(sql,new Object[] {}));
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return jsonList;
	}
	
	/**
	 * 插入
	 * @param map
	 * @return
	 */
	public int insertPrivilege(Map<String, Object> map) {
		try {
			return bossDB.sql_insert("BOSS_ADMIN_PRIVILEGE", map);
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
	public int updatePrivilege(int id, Map<String, Object> map) {
		try {
			return bossDB.sql_update("BOSS_ADMIN_PRIVILEGE", map, "PRIVILEGE_ID=?", new Object[] {id});
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
	public int delPrivilege(int id) {
		try {
			String sql = "delete from BOSS_ADMIN_PRIVILEGE where PRIVILEGE_ID=?";
			return bossDB.update(sql, new Object[] {id});
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return 0;
	}
	
	/**
	 * 批量删除
	 * @param batchArgs
	 * @return
	 */
	public int[] batchDelPrivilege(List<Object[]> batchArgs) {
		try {
			String sql = "delete from BOSS_ADMIN_PRIVILEGE where PRIVILEGE_ID=?";
			return bossDB.batchUpdate(sql, batchArgs);
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return null;
	}
	/**
	 * 批量更新
	 * @param batchArgs
	 * @return
	 */
	public int[] batchUpdatePrivilege(List<Object[]> batchArgs) {
		try {
			String sql = "update BOSS_ADMIN_PRIVILEGE set " +
					"PRIVILEGE_PID=?, " +
					"PRIVILEGE_TYPE=?, " +
					"PRIVILEGE_ALIAS=?, " +
					"PRIVILEGE_NAME=?, " +
					"PRIVILEGE_URL=?, " +
					"PRIVILEGE_DESC=?, " +
					
					"PRIVILEGE_STATUS=?, " +
					
					"PRIVILEGE_ORDER=?, " +
					"PRIVILEGE_UPDATE_TIME=now() " +
					
					" where PRIVILEGE_ID=?";
			return bossDB.batchUpdate(sql, batchArgs);
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return null;
	}
	/**
	 * 批量插入
	 * @param batchArgs
	 * @return
	 */
	public int[] batchInsertPrivilege(List<Object[]> batchArgs) {
		try {
			String sql = "insert into BOSS_ADMIN_PRIVILEGE  (" +
					 
					"PRIVILEGE_PID, " +
					"PRIVILEGE_TYPE, " +
					"PRIVILEGE_NAME, " +
					"PRIVILEGE_URL, " +
					"PRIVILEGE_STATUS, " +
					"PRIVILEGE_ORDER," +
					
					"PRIVILEGE_CREATE_TIME," +
					"PRIVILEGE_UPDATE_TIME" +
					//"PRIVILEGE_LEVEL"+
					
					") values (" +
					
					"  " +
					"?, " +
					"?, " +
					"?, " +
					"?, " +
					"?, " +
					"?, " +
					"now(), " +
					"now() " +
					//"?"+
					
					")";
			return bossDB.batchUpdate(sql, batchArgs);
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return null;
	}
	
	/**
	 * 获取列表
	 * @param pid
	 * @return
	 */
	public JSONArray getSysTermList(int pid) {
		String sql = "SELECT * FROM BOSS_ADMIN_PRIVILEGE WHERE PRIVILEGE_PID = ? and PRIVILEGE_STATUS=? ORDER BY privilege_pid,privilege_order";
		 
		
		JSONArray jsonList = new JSONArray();
		try {
			jsonList = DbCommon.field2json(bossDB.queryList(sql, new Object[] {pid,1}));
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return jsonList;
	}
}
