package com.huacai.util;

import java.util.HashMap;
import java.util.Map;


/**
 * @description 数据管理常量类
 * @author shijf
 * @date 2013-5-7 下午01:55:18
 * @version 1.0
 */
public class DataConstants {

	/**
	 * 封存类型,期次
	 */
	public static final int ARCHIVE_TYPE_TERM = 1;
	public static final String ARCHIVE_TYPE_TERM_DESP="期次信息封存";
	/**
	 * 封存类型,交易数据
	 */
	public static final int ARCHIVE_TYPE_TRADE = 2;
	public static final String ARCHIVE_TYPE_TRADE_DESP = "交易信息封存";
	
	public static Map<Integer,String> ARCHIVE_TYPE_MAP = new HashMap<Integer,String>();
	static {
		ARCHIVE_TYPE_MAP.put(ARCHIVE_TYPE_TERM,ARCHIVE_TYPE_TERM_DESP);
		ARCHIVE_TYPE_MAP.put(ARCHIVE_TYPE_TRADE,ARCHIVE_TYPE_TRADE_DESP);
	}
	
	/**
	 * 期次信息备份
	 */
	public static final int BACKUP_TERM = 3;
	public static final String BACKUP_TERM_DESP="期次信息备份";
	/**
	 * 交易信息备份
	 */
	public static final int BACKUP_TRADE = 4;
	public static final String BACKUP_TRADE_DESP="交易信息备份";
	
	public static Map<Integer,String> BACKUP_TYPE_MAP = new HashMap<Integer,String>();
	static {
		BACKUP_TYPE_MAP.put(BACKUP_TERM,BACKUP_TERM_DESP);
		BACKUP_TYPE_MAP.put(BACKUP_TRADE,BACKUP_TRADE_DESP);
	}
	
	/**
	 * 交易信息归集
	 */
	public static final int COLLECTION_TRADE = 5;
	public static final String COLLECTION_TRADE_DESP="交易信息归集";
	
	public static Map<Integer,String> COLLECTION_MAP = new HashMap<Integer,String>();
	static {
		COLLECTION_MAP.put(COLLECTION_TRADE,COLLECTION_TRADE_DESP);
	}
	
	
	public static final Map<Integer,String> DATA_TYPE_MAP = new HashMap<Integer,String>();
	static {
		DATA_TYPE_MAP.put(ARCHIVE_TYPE_TERM,ARCHIVE_TYPE_TERM_DESP);
		DATA_TYPE_MAP.put(ARCHIVE_TYPE_TRADE,ARCHIVE_TYPE_TRADE_DESP);
		DATA_TYPE_MAP.put(BACKUP_TERM,BACKUP_TERM_DESP);
		DATA_TYPE_MAP.put(BACKUP_TRADE, BACKUP_TRADE_DESP);
		DATA_TYPE_MAP.put(COLLECTION_TRADE, COLLECTION_TRADE_DESP);
	}
	
	/**
	 * 0 否
	 * 1 是
	 */
	public static final Map<Integer,String> YON = new HashMap<Integer,String>();
	public static final int N = 0;
	public static final int Y = 1;
	static{
		YON.put(N,"否");
		YON.put(Y,"是");
	}
	
	/**
	 * 审核状态
	 */
	public static final int AUDIT_STATUS_ZC = 1;//正常
	public static final int AUDIT_STATUS_YC = 2;//异常
	
	public static Map<Integer,String> AUDIT_STATUS_MAP = new HashMap<Integer,String>();
	static {
		AUDIT_STATUS_MAP.put(AUDIT_STATUS_ZC, "正常");
		AUDIT_STATUS_MAP.put(AUDIT_STATUS_YC, "异常");
	}
	
	/**
	 * 稽核类型
	 */
	public static final int AUDIT_TYPE_JYSJ = 1;//交易数据
	public static final int AUDIT_TYPE_ZJSJ = 2;//中奖数据
	public static final int AUDIT_TYPE_CZSJ = 3;//充值数据
	public static final int AUDIT_TYPE_TXSJ = 4;//提现数据
	public static final int AUDIT_TYPE_YHSJ = 5;//用户数据
	
	public static Map<Integer,String> AUDIT_TYPE_MAP = new HashMap<Integer,String>();
	static {
		AUDIT_TYPE_MAP.put(AUDIT_TYPE_JYSJ, "交易数据");
		AUDIT_TYPE_MAP.put(AUDIT_TYPE_ZJSJ, "中奖数据");
		AUDIT_TYPE_MAP.put(AUDIT_TYPE_CZSJ, "充值数据");
		AUDIT_TYPE_MAP.put(AUDIT_TYPE_TXSJ, "提现数据");
		AUDIT_TYPE_MAP.put(AUDIT_TYPE_YHSJ, "用户数据");
	}
	
	public static final int LOT_SSQ = 101;// 双色球
	public static final int LOT_QLC = 102;// 七乐彩
	public static final int LOT_3D = 103;// 福彩3D
	public static final int LOT_CQSSC = 145;// 重庆时时彩
	
	public static final Map<Integer,String> GAME_MAP = new HashMap<Integer,String>();
	static {
		GAME_MAP.put(LOT_SSQ, "双色球");
		GAME_MAP.put(LOT_QLC, "七乐彩");
		GAME_MAP.put(LOT_3D, "3D");
		GAME_MAP.put(LOT_CQSSC,"重庆时时彩");
	}
	
}
