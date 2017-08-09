package com.huacai.web.dao.syslog;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Repository;

import com.huacai.util.ReadResource;
import com.huacai.web.db.datasource.BossDB;

import libcore.util.VarUtil;


/**
 * 登录日志
 * @author fuhua
 *
 */
@Repository("loginLogDao")
@Configurable(preConstruction=true)
public class LogLoginDao{
	private static final Logger log=LogManager.getLogger(LogLoginDao.class);
	
	@Autowired
	private BossDB bossDB;
	
	/**
	 * 应用ID
	 */
	private static int BOSS_APP_ID;
	static{
		BOSS_APP_ID = VarUtil.intval(ReadResource.get("BOSS_APP_ID"));
	}
	
	
	//demo
	//logLoginDao.add(adminInfo.getAdminId(),adminInfo.getAdminLoginname(),adminInfo.getAdminRealname(), SysLogConstants.LOGIN_STATUS_SUCCESS, "登录成功", ip);
	/**
	 * 插入登录日志
	 * @param adminId 管理员ID
	 * @param adminLoginname 登录名
	 * @param adminRealname 真实姓名
	 * @param status 登录状态
	 * @param desc 描述
	 * @param ip 登录IP
	 * @return
	 */
	public int add(long adminId, String adminLoginname,String adminRealname, int type, int status, String desc, String ip) {
		try {
			String sql = "insert into BOSS_LOGS_LOGIN " +
					"(LOGS_ADMIN_ID, LOGS_ADMIN_LOGINNAME, LOGS_ADMIN_REALNAME, LOGS_APP, LOGS_DESC,LOGS_TYPE, LOGS_STATUS, LOGS_IP, LOGS_CREATE_TIME) " +
					"values " +
					"( ?, ?, ?, ?, ?, ?, ?, ?, now())";
			
			
			return bossDB.update(sql, new Object[]{adminId, adminLoginname, adminRealname, BOSS_APP_ID, desc,type, status, ip});
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return 0;
	}
}
