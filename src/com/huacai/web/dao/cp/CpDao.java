package com.huacai.web.dao.cp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import libcore.util.DbUtil;
import libcore.util.EncodeUtil;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huacai.util.ReadResource;
import com.huacai.web.common.DbCommon;
import com.huacai.web.common.LoginCommon;
import com.huacai.web.common.ViewCommon;
import com.huacai.web.constant.BossConstants;
import com.huacai.web.db.datasource.BossDB;

@Repository("cpDao")
@Configurable(preConstruction=true)
public class CpDao{
	private static final Logger log=LogManager.getLogger(CpDao.class);
	
	@Autowired
	private BossDB bossDB;
	

	/**
	 * 检测登录信息
	 * @return
	 */
	public JSONObject checkLogin(HttpServletRequest request, String loginname, String password) {
		if(loginname == null || loginname.length() < 4 || password == null || password.length() != 32){
			return ViewCommon.getAjaxMessage(0, "用户名或密码错误");
		}
		try {
			loginname = loginname.toLowerCase();
			JSONObject json = getAdminInfo(loginname);
			if(json == null){
				return ViewCommon.getAjaxMessage(0, "用户名或密码错误");
			}
			Long adminId = json.getLong("admin_id");
			if(adminId == null){
				return ViewCommon.getAjaxMessage(0, "用户名或密码错误");
			}
			if(json != null){
				String passwordSalt = json.getString("admin_password_salt");
				String newPassword = EncodeUtil.MD5(password+passwordSalt);
				String realPassword = json.getString("admin_password");
				int adminStatus = json.getIntValue("admin_status");
				int isRoot = json.getIntValue("admin_root");
				
				//检查密码
				if(!newPassword.equals(realPassword)){
					return ViewCommon.getAjaxMessage(0, "用户名或密码错误");
				}
				
				if(adminStatus != 1){
					return ViewCommon.getAjaxMessage(0, "账户已禁用");
				}
				
				
				String appId = ReadResource.get("BOSS_APP_ID");
				if (appId == null) {
					appId = "";
				}
				//限制超级管理员只能登录业务管理系统
				if(!appId.equals("11") && isRoot == 1){
					return ViewCommon.getAjaxMessage(0,  "用户 "+loginname+" 只允许登录运营支撑系统");
				}
				
				
				//检查是否被其它人登录
				if(LoginCommon.checkUserLoginStatus(request, loginname)){
					return ViewCommon.getAjaxMessage(0, "用户已经在其它地方登录");
				}
				
				
				//检查登录时间
				//检查登录IP
				
				//更新最后登录时间
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("admin_login_time", new Date());
				updateAdmin(adminId, data);
				
				return ViewCommon.getAjaxMessage(1, "登录成功", json);
			}
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return ViewCommon.getAjaxMessage(0, "用户名或密码错误");
	}
	
	/**
	 * 获取用户信息
	 * @return
	 */
	public JSONObject getAdminInfo(String loginname) {
		String sql = "SELECT * FROM BOSS_ADMIN_USER WHERE ADMIN_LOGINNAME = ?";
		try {
			return DbCommon.field2json(bossDB.queryMap(sql, new Object[] {loginname}));
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return null;
	}
	/**
	 * 获取用户权限列表
	 * @return
	 */
	public JSONArray getPrivList(long admin_id) {
		String sql = "SELECT t1.* FROM BOSS_ADMIN_PRIVILEGE t1 " +
				"where t1.PRIVILEGE_STATUS=1 " +
				"and t1.PRIVILEGE_ID in ( " +
					"SELECT t2.RELATION_PRIV_ID FROM BOSS_ADMIN_RELATION_RP t2,BOSS_ADMIN_RELATION_RU t3,BOSS_ADMIN_ROLE t4 " +
					"where t3.RELATION_ADMIN_ID = ? " +
					"and t2.RELATION_ROLE_ID=t3.RELATION_ROLE_ID " +
					"and t2.RELATION_ROLE_ID=t4.ROLE_ID " +
					"and t4.ROLE_STATUS=1 " +
					//"and t4.ROLE_LEVEL=? " +
				" )"+
				"ORDER BY t1.privilege_pid,t1.privilege_order ";
				//" and ROWNUM < 100  ORDER BY t1.privilege_pid,t1.privilege_order ";

		
		JSONArray jsonList = new JSONArray();
		try {
			jsonList = DbCommon.field2json(bossDB.queryList(sql, new Object[]{admin_id}));
			//过滤上下级关系
			jsonList = LoginCommon.filterPrivListMenu(jsonList);
			JSONObject privChilds = LoginCommon.privChildList(jsonList);
			jsonList = new JSONArray();
			LoginCommon.filterPrivList(privChilds, "0", jsonList);
			
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return jsonList;
	}
	
	/**
	 * 获取所有权限列表
	 * @return
	 */
	public JSONArray getAllPrivList() {
		//String sql = "SELECT t1.* FROM ADMIN_PRIVILEGE t1 where PRIVILEGE_STATUS=1 and PRIVILEGE_LEVEL="+BossConstants.SYS_LEVEL;
		String sql = "SELECT t1.* FROM BOSS_ADMIN_PRIVILEGE t1 where PRIVILEGE_STATUS=1";
		//超级管理员限制只有系统管理权限
		sql += " and ((PRIVILEGE_ID = 1 and PRIVILEGE_PID=101) or PRIVILEGE_PID<>101)";
		sql += "ORDER BY t1.privilege_pid,t1.privilege_order ";

		
		JSONArray jsonList = new JSONArray();
		try {
			jsonList = DbCommon.field2json(bossDB.queryList(sql));
			//过滤上下级关系
			jsonList = LoginCommon.filterPrivListMenu(jsonList);
			JSONObject privChilds = LoginCommon.privChildList(jsonList);
			jsonList = new JSONArray();
			LoginCommon.filterPrivList(privChilds, "0", jsonList);
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return jsonList;
	}
	
	/**
	 * 获取管理员信息
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
	 * 更新管理员信息
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
	 * 更新管理员密码
	 * @param id
	 * @param map
	 * @return
	 */
	public int updateAdminPwd(long id, String pwd, String salt) {
		try {
			String sql = "UPDATE BOSS_admin_user SET admin_password=?, admin_password_salt=?, admin_pwd_updates=admin_pwd_updates+1,admin_update_time=now() WHERE admin_id=?";
			return bossDB.update(sql, new Object[] {pwd,salt,id});
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return 0;
	}
	
	/**
	 * 更新管理员密码
	 * @param id
	 * @param map
	 * @return
	 */
	public int updateConfig(int idx, Map<String, Object> map) {
		try {
			return bossDB.sql_update("BOSS_CONFIG", map, "CONFIG_ID=?", new Object[] {idx});
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return 0;
	}
	
	/**
	 * 获取列表
	 * @param pid
	 * @return
	 */
	public JSONArray getConfigList() {
		String sql = "SELECT * FROM BOSS_CONFIG ORDER BY CONFIG_UPDATE_TIME";		
		JSONArray jsonList = new JSONArray();
		try {
			jsonList = DbCommon.field2json(bossDB.queryList(sql));
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return jsonList;
	}

	/**
	 * 获取管理员信息
	 * @param pid
	 * @return
	 */
	public JSONObject getBoss(long id) {
		String sql = "SELECT * FROM BOSS_CONFIG WHERE CONFIG_ID = ?";		
		JSONObject json = new JSONObject();
		try {
			json = DbCommon.field2json(bossDB.queryMap(sql, new Object[] {id}));
		} catch (Exception e) {
			log.error("DB error", e);
		}	
		return json;
	}
	
	/**
	 * 获取用户角色名
	 * @param pid
	 * @return
	 */
	public JSONObject getAdminRoleNameByAdminId(long adminId) {

		String sql = "SELECT (\n" +
				"		select WMSYS.WM_CONCAT(ROLE_NAME) AS ROLE_NAME from BOSS_ADMIN_ROLE,BOSS_ADMIN_RELATION_RU where RELATION_ROLE_ID=ROLE_ID and RELATION_ADMIN_ID=ADMIN_ID \n" +
				"		) AS ROLE_NAME\n" +
				"FROM BOSS_ADMIN_USER t1 \n" +
				"WHERE 1=1 and t1.admin_id=?";
		
		JSONObject json = new JSONObject();
		try {
			json = DbCommon.field2json(bossDB.queryMap(sql, new Object[] {adminId}));
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return json;
	}
}
