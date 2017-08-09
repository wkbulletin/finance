package com.huacai.web.common;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class InitializeData {

	private static Map pushSessionMap = new HashMap(); // 资源文件
	
	private static Date klcPrizeTime = new Date();// 开乐彩的开奖时间
	
	public static boolean FORMAL_SVR = true;//当前服务器状态是否为正式状态
	
	// 邮件模板
	public static Map<String, String> EmailTemplate = new HashMap<String, String>();
	
	//发邮件配置数据
	public static final String CONFIG_SMTP_HOST = "mail.huacai.com";
	public static final String CONFIG_SMTP_USER = "support";
	public static final String CONFIG_SMTP_PASSWORD = "huacai12098613";
	public static final String CONFIG_SMTP_FROM = "support@huacai.com";
	static{
		EmailTemplate.put("OrdinaryMail","您好\n\n,{content}.");
	}
	public static final String SMSTemplate = "您好,{content}!";

	private InitializeData() {
	}

	public static Map getPushSessionMap() {
		return pushSessionMap;
	}

	public static void setPushSessionMap(Map pushSessionMap) {
		InitializeData.pushSessionMap = pushSessionMap;
	}

	public static Date getKlcPrizeTime() {
		return klcPrizeTime;
	}
	

	public static void setKlcPrizeTime(Date klcPrizeTime) {
		InitializeData.klcPrizeTime = klcPrizeTime;
	}

}
