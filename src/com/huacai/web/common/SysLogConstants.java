package com.huacai.web.common;

import java.util.HashMap;
import java.util.Map;


/**
 * 日志操作相关常量
 * 
 * 
 */
public class SysLogConstants {
 
	/**
	 * 应用：系统名称
	 */
	public static final Map<Integer, String> BOSS_APP;
	static{
		BOSS_APP = new HashMap<Integer, String>();
		BOSS_APP.put(1, "运营支撑应用");
		BOSS_APP.put(2, "财务结算应用");
		BOSS_APP.put(3, "大奖领奖应用");
		BOSS_APP.put(4, "网点开户应用");
		BOSS_APP.put(5, "客服中心应用");
		BOSS_APP.put(7, "大数据分析应用");
		BOSS_APP.put(8, "大屏监控应用");
		BOSS_APP.put(9, "代理商后台应用");
		BOSS_APP.put(10, "监控中心应用");
		BOSS_APP.put(11, "业务管理应用");
		BOSS_APP.put(15, "超大奖领奖应用");
	}

	
	/**
	 * 登录日志--登录类型：退出
	 */
	public static final int LOGIN_TYPE_LOGOUT = 0;
	
	/**
	 * 登录日志--登录类型：登录
	 */
	public static final int LOGIN_TYPE_LOGIN = 1;
	
	/**
	 * 登录日志--登录类型
	 */
	public static final Map<Integer, String> LOGIN_TYPE;
	static{
		LOGIN_TYPE = new HashMap<Integer, String>();
		LOGIN_TYPE.put(LOGIN_TYPE_LOGOUT, "退出");
		LOGIN_TYPE.put(LOGIN_TYPE_LOGIN, "登录");
	}
	
	/**
	 * 登录日志--登录状态：成功
	 */
	public static final int LOGIN_STATUS_SUCCESS = 1;
	
	/**
	 * 登录日志--登录状态：失败
	 */
	public static final int LOGIN_STATUS_FAILED = 0;
	
	/**
	 * 登录日志--登录状态
	 */
	public static final Map<Integer, String> LOGIN_STATUS;
	static{
		LOGIN_STATUS = new HashMap<Integer, String>();
		LOGIN_STATUS.put(LOGIN_STATUS_SUCCESS, "成功");
		LOGIN_STATUS.put(LOGIN_STATUS_FAILED, "失败");
	}
	
	
	
	/**
	 * 控审日志/核心操作日志--开奖系统：全国
	 */
	public static final int LOT_SYSTEM_QG = 0;
	
	/**
	 * 控审日志/核心操作日志--开奖系统：电话系统
	 */
	public static final int LOT_SYSTEM_TEL = 1;
	
	/**
	 * 控审日志/核心操作日志--开奖系统：北京系统
	 */
	public static final int LOT_SYSTEM_BEIJIN = 1;
	
	
	/**
	 * 控审日志/核心操作日志--开奖系统
	 */
	public static final Map<Integer, String> LOT_SYSTEM;
	static{
		LOT_SYSTEM = new HashMap<Integer, String>();
		LOT_SYSTEM.put(LOT_SYSTEM_QG, "全国");
		LOT_SYSTEM.put(LOT_SYSTEM_TEL, "电话系统");
		LOT_SYSTEM.put(LOT_SYSTEM_BEIJIN, "北京系统");
		
	}
	
	
	
	/**
	 * 控审日志/核心操作日志--玩法：双色球
	 */
	public static final int LOT_ID_SSQ = 101;
	/**
	 * 控审日志/核心操作日志--玩法：3D
	 */
	public static final int LOT_ID_3D = 103;
	/**
	 * 控审日志/核心操作日志--玩法：七乐彩
	 */
	public static final int LOT_ID_QLC = 102;
	/**
	 * 控审日志/核心操作日志--玩法：快乐8
	 */
	public static final int LOT_ID_KL8 = 161;
	/**
	 * 控审日志/核心操作日志--玩法：PK拾
	 */
	public static final int LOT_ID_PK10 = 162;

	
	
	/**
	 * 控审日志/核心操作日志--玩法
	 */
	public static final Map<Integer, String> LOT_ID;
	static{
		LOT_ID = new HashMap<Integer, String>();
		LOT_ID.put(LOT_ID_SSQ, "双色球");
		LOT_ID.put(LOT_ID_3D, "3D");
		LOT_ID.put(LOT_ID_QLC, "七乐彩");
		LOT_ID.put(LOT_ID_KL8, "快乐8");
		LOT_ID.put(LOT_ID_PK10, "PK拾");
		
	}
	
	/**
	 * 控审日志/核心操作日志--玩法-操作类型：开奖
	 */
	public static final int LOT_OP_TYPE_PRIZE = 1;
	/**
	 * 控审日志/核心操作日志--玩法-操作类型：开奖报表
	 */
	public static final int LOT_OP_TYPE_PRIZE_REPORT = 2;
	/**
	 * 控审日志/核心操作日志--玩法-操作类型：派奖记录
	 */
	public static final int LOT_OP_TYPE_PRIZE_HIS = 3;
	/**
	 * 控审日志/核心操作日志--玩法-操作类型：状态回滚
	 */
	public static final int LOT_OP_TYPE_ROLLBACK = 4;
	/**
	 * 控审日志/核心操作日志--玩法-操作类型：夜对
	 */
	public static final int LOT_OP_TYPE_NIGHT_CHECKOUT = 5;

	/**
	 * 控审日志/核心操作日志--玩法-操作类型：FTP文件处理（开奖）
	 */
	public static final int LOT_OP_TYPE_PRIZE_FTP = 5;
	
	
	/**
	 * 控审日志/核心操作日志--玩法-操作类型
	 */
	public static final Map<Integer, String> LOT_OP_TYPE;
	static{
		LOT_OP_TYPE = new HashMap<Integer, String>();
		LOT_OP_TYPE.put(LOT_OP_TYPE_PRIZE, "开奖");
		LOT_OP_TYPE.put(LOT_OP_TYPE_PRIZE_REPORT, "开奖报表");
		LOT_OP_TYPE.put(LOT_OP_TYPE_PRIZE_HIS, "派奖记录");
		LOT_OP_TYPE.put(LOT_OP_TYPE_ROLLBACK, "状态回滚");
		LOT_OP_TYPE.put(LOT_OP_TYPE_NIGHT_CHECKOUT, "夜对");
		LOT_OP_TYPE.put(LOT_OP_TYPE_PRIZE_FTP, "FTP文件处理（开奖）");
	}
	
	
	
	/**
	 * 控审日志/核心操作日志--玩法操作状态：成功
	 */
	public static final int LOT_OP_STATUS_SUCCESS = 1;
	
	/**
	 * 控审日志/核心操作日志--玩法操作状态：失败
	 */
	public static final int LOT_OP_STATUS_FAILED = 0;
	
	/**
	 * 控审日志/核心操作日志--玩法操作状态
	 */
	public static final Map<Integer, String> LOT_OP_STATUS;
	static{
		LOT_OP_STATUS = new HashMap<Integer, String>();
		LOT_OP_STATUS.put(LOT_OP_STATUS_SUCCESS, "成功");
		LOT_OP_STATUS.put(LOT_OP_STATUS_FAILED, "失败");
		
	}
	
	
	/**
	 * 监控日志/系统日志--操作类型：查询列表
	 */
	public static final int OP_TYPE_QUERY = 0;
	
	/**
	 * 监控日志/系统日志--操作类型：详情
	 */
	public static final int OP_TYPE_DETAIL = 1;
	
	/**
	 * 监控日志/系统日志--操作类型：添加
	 */
	public static final int OP_TYPE_INSERT = 2;
	
	/**
	 * 监控日志/系统日志--操作类型：修改
	 */
	public static final int OP_TYPE_UPDATE = 3;
	
	/**
	 * 监控日志/系统日志--操作类型：删除
	 */
	public static final int OP_TYPE_DEL = 4;
	
	
	/**
	 * 监控日志/系统日志--操作类型
	 */
	public static final Map<Integer, String> OP_TYPE;
	static{
		OP_TYPE = new HashMap<Integer, String>();
		OP_TYPE.put(OP_TYPE_QUERY, "查询列表");
		OP_TYPE.put(OP_TYPE_DETAIL, "详情");
		OP_TYPE.put(OP_TYPE_INSERT, "添加");
		OP_TYPE.put(OP_TYPE_UPDATE, "修改");
		OP_TYPE.put(OP_TYPE_DEL, "删除");
	}
	
	
	/**
	 * 监控日志/系统日志--操作状态：成功
	 */
	public static final int OP_STATUS_SUCCESS = 1;
	
	/**
	 * 监控日志/系统日志--玩法操作状态：失败
	 */
	public static final int OP_STATUS_FAILED = 0;
	
	/**
	 * 监控日志/系统日志--玩法操作状态
	 */
	public static final Map<Integer, String> OP_STATUS;
	static{
		OP_STATUS = new HashMap<Integer, String>();
		OP_STATUS.put(OP_STATUS_SUCCESS, "成功");
		OP_STATUS.put(OP_STATUS_FAILED, "失败");
		
	}
		
	
	
	
	
	
}
