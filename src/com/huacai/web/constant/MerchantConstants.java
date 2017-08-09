package com.huacai.web.constant;

import java.util.TreeMap;


public class MerchantConstants {
	
	public static final TreeMap<String, String> DAY_SERCHTYPE;
	static {
		DAY_SERCHTYPE = new TreeMap<String, String>();
		
		DAY_SERCHTYPE.put("cloginname", "登录账号");
		DAY_SERCHTYPE.put("crefercode", "推荐码");
		DAY_SERCHTYPE.put("cregname", "商户名称");
		
	}
	
	/**
	 * 状态：类型
	 */
	public static final int BOSS_MERCHANT_EMAINTYPE_PSERSON = 2;//个人
	public static final int BOSS_MERCHANT_EMAINTYPE_COMPANY = 1;//公司
	
	/**
	 * 状态：类型
	 */
	public static final int BOSS_MERCHANT_ERATEMODE_G = 1;//固定
	public static final int BOSS_MERCHANT_ERATEMODE_J = 2;//阶梯
	
	/**
	 * 状态：商户状态
	 */
	public static final int BOSS_MERCHANT_STATUS_1 = 1;//未激活
	public static final int BOSS_MERCHANT_STATUS_2 = 2;//使用中
	public static final int BOSS_MERCHANT_STATUS_7 = 7;//锁定中
	public static final int BOSS_MERCHANT_STATUS_8 = 8;//已清除
	public static final int BOSS_MERCHANT_STATUS_9 = 9;//已注销
	
	
	/**
	 * 清除商户月数
	 */
	public static final int BOSS_MERCHANT_CLEAR_NUM = 1;
	
}
