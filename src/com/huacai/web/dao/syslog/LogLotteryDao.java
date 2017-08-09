package com.huacai.web.dao.syslog;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Repository;

import com.huacai.web.bean.privilege.AdminInfo;
import com.huacai.web.common.LoginCommon;
import com.huacai.web.db.datasource.BossDB;

import libcore.util.NetUtil;

/**
 * 控审日志/核心操作日志
 * @author fuhua
 *
 */
@Repository("logLotteryDao")
@Configurable(preConstruction=true)
public class LogLotteryDao{
	private static final Logger log=LogManager.getLogger(LogLotteryDao.class);
	
	@Autowired
	private BossDB bossDB;
	
	//demo
	//logLotteryDao.add(request, SysLogConstants.LOT_ID_SSQ, "111111", SysLogConstants.LOT_OP_TYPE_PRIZE, SysLogConstants.LOT_SYSTEM_QG, SysLogConstants.LOGIN_STATUS_SUCCESS, "开奖");
	/**
	 * 插入控审日志/核心操作日志
	 * @param adminId 管理员ID
	 * @param adminLoginname 登录名
	 * @param adminRealname 真实姓名
	 * @param lotId 玩法
	 * @param issue 期次
	 * @param opType 操作类型
	 * @param system 开奖系统
	 * @param status 状态
	 * @param desc 描述
	 * @param ip 操作IP
	 * @return
	 */
	public int add(long adminId, String adminLoginname,String adminRealname, int lotId, String issue, int opType, int system, int status, String desc, String ip) {
		try {
			String sql = "insert into BOSS_LOGS_LOTTERY " +
					"(  LOGS_ADMIN_ID, LOGS_ADMIN_LOGINNAME, LOGS_ADMIN_REALNAME, LOGS_LOT_SYSTEM,LOGS_LOT_ID, LOGS_LOT_ISSUE, LOGS_OP_TYPE,LOGS_DESC, LOGS_STATUS, LOGS_IP, LOGS_CREATE_TIME) " +
					"values " +
					"(  ?, ?, ?, ?, ?, ?, ?, ?, ?,?, now())";
			
			
			return bossDB.update(sql, new Object[]{adminId, adminLoginname, adminRealname, system,lotId,lotId,issue, desc, status, ip});
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return 0;
	}
	/**
	 * 插入控审日志/核心操作日志(根据ID查询用户信息)
	 * @param adminId 管理员ID,为0则不查询操作员信息
	 * @param lotId 玩法
	 * @param issue 期次
	 * @param opType 操作类型
	 * @param system 开奖系统
	 * @param status 状态
	 * @param desc 描述
	 * @param ip 操作IP
	 * @return
	 */
	public int add(long adminId, int lotId, String issue, int opType, int system, int status, String desc, String ip) {
		//查询操作员信息
		String adminLoginname="";
		String adminRealname="";
		if(adminId>0){
			Map<String, Object> adminInfo = getAdminInfo(adminId);
			if(adminInfo!=null){
				adminLoginname = (String)adminInfo.get("ADMIN_LOGINNAME");
				adminRealname = (String)adminInfo.get("ADMIN_REALNAME");
			}
		}
		return add(adminId, adminLoginname, adminRealname, lotId, issue, opType, system, status, desc, ip);
	}
	
	
	/**
	 * 插入控审日志/核心操作日志(session中获取用户信息)
	 * @param request HttpServletRequest
	 * @param lotId 玩法
	 * @param issue 期次
	 * @param opType 操作类型
	 * @param system 开奖系统
	 * @param status 状态
	 * @param desc 描述
	 * @return
	 */
	public int add(HttpServletRequest request, int lotId, String issue, int opType, int system, int status, String desc) {
		//从session中查询操作员信息
		long adminId=0;
		String adminLoginname="";
		String adminRealname="";
		AdminInfo adminInfo = LoginCommon.getAdminInfo(request);
		if(adminInfo!=null){
			adminId = adminInfo.getAdminId();
			adminLoginname = adminInfo.getAdminLoginname();
			adminRealname = adminInfo.getAdminRealname();
		}
		String ip=NetUtil.getIpAddr(request);

		
		return add(adminId, adminLoginname, adminRealname, lotId, issue, opType, system, status, desc, ip);
	}
	
	
	
	/**
	 * 查询操作员信息
	 * @param adminId
	 * @return
	 */
	public Map<String, Object> getAdminInfo(long adminId) {
		try {
			//查询操作员信息
			String sql = "select ADMIN_ID, ADMIN_LOGINNAME, ADMIN_REALNAME from BOSS_ADMIN_USER where ADMIN_ID=?";
			return bossDB.queryMap(sql, new Object[]{adminId});
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return null;
	}
}
