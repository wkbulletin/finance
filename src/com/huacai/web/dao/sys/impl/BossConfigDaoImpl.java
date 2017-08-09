package com.huacai.web.dao.sys.impl;
import java.util.List;
import java.util.Map;

import libcore.util.DbUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.huacai.web.db.datasource.BossDB;

@Repository("agencyConfigDao")
@Configurable(preConstruction=true)
public class BossConfigDaoImpl{
	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private BossDB bossDB;
	
 
	/**
	 * 获取配置记录
	 * @param agencyId
	 * @param key
	 * @return
	 */
	public JSONObject getConfigRec(String key) {
		
		String sql = "select * from BOSS_CONFIG where CONFIG_KEY=?";
		
		try {
			List<Map<String, Object>> lists = bossDB.queryList(sql, new Object[]{ key});
			for(Map<String, Object> map : lists){
				JSONObject json = new JSONObject();
				json.put("CONFIG_ID", DbUtil.getLong(map.get("CONFIG_ID")));
				json.put("CONFIG_KEY", DbUtil.getString(map.get("CONFIG_KEY")));
				json.put("CONFIG_VALUE", DbUtil.getString(map.get("CONFIG_VALUE")));
				json.put("CONFIG_CREATE_TIME", DbUtil.getDate(map.get("CONFIG_CREATE_TIME")));
				json.put("CONFIG_UPDATE_TIME", DbUtil.getDate(map.get("CONFIG_UPDATE_TIME")));
				json.put("CONFIG_COMMENT", DbUtil.getString(map.get("CONFIG_COMMENT")));
				return json;
			}
		} catch (Exception e) {
			logger.error("DB error", e);
		}
		return null;
	}
	/**
	 * 获取配置记录
	 * @param agencyId
	 * @param key
	 * @return
	 */
	public String getConfigValue(String key) {
		
		String sql = "select CONFIG_VALUE from BOSS_CONFIG where CONFIG_KEY=?";
		try {
			Map<String, Object> map = bossDB.queryMap(sql, new Object[]{key});
			
			if(map!=null){
				return DbUtil.getString(map.get("CONFIG_VALUE"));
			}
		} catch (Exception e) {
			logger.error("DB error", e);
		}
		return null;
	}
	
	/**
	 * 更新配置
	 * @param agencyId
	 * @param key
	 * @param value
	 * @param comment
	 * @return
	 */
	public int updateConfigRec( final String key, final String value, final String comment) {
		String sql = "select CONFIG_ID from BOSS_CONFIG where CONFIG_KEY=?";
		long configId = 0;
		try {
			Map<String, Object> map = bossDB.queryMap(sql, new Object[]{ key});
			
			if(map!=null){
				configId = DbUtil.getLong(map.get("CONFIG_ID"));
			}
			
			if(configId > 0 ){
				sql = "UPDATE BOSS_CONFIG SET \n" + 
						"CONFIG_VALUE = ?, \n" + 
						"CONFIG_COMMENT = ?, \n" + 
						"CONFIG_UPDATE_TIME = now() \n" + 
						"WHERE CONFIG_ID = ? AND CONFIG_KEY=? \n";

				return bossDB.update(sql, new Object[]{value, comment, configId, key});
			}else{
				sql = "INSERT INTO BOSS_CONFIG \n" + 
						"( \n" + 
						"	CONFIG_ID,  CONFIG_KEY, CONFIG_VALUE, CONFIG_COMMENT ,  \n" + 
						"	CONFIG_CREATE_TIME, CONFIG_UPDATE_TIME \n" + 
						") \n" + 
						"VALUES \n" + 
						"( \n" + 
						"	BOSS_CONFIG.nextval, ?, ?,  ?, \n" + 
						"	now(), now() \n" + 
						") \n";

				return bossDB.update(sql, new Object[]{key, value, comment});
			}
		} catch (Exception e) {
			logger.error("DB error", e);
			return 0;
		}

	}
}
