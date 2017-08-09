package com.huacai.web.common;

import java.net.URL;
import java.util.concurrent.ConcurrentSkipListMap;

import com.huacai.util.ReadResourceAgency;

public class AgencyConfig {
	private final static ConcurrentSkipListMap<String, AgencyConfigDbContent> DbConfigMap = new ConcurrentSkipListMap<String, AgencyConfigDbContent>();

	/**
	 * 微信配置
	 */
	public static ReadResourceAgency weixinCfg;
	static{
		init();
	}
	
	public static String getBasePath() {
		//获取项目basePath
		URL s = AgencyConfig.class.getClassLoader().getResource("lottery.properties");
		String path = s.getPath();
		return path.replace("WEB-INF/classes/lottery.properties", "");
	}
	
	public static void init() {
		//代理商配置
		init(getBasePath());
	}
	
	public static void init(String basePath) {
		//ReadResourceAgency.basePath = basePath+"WEB-INF/config/";
		ReadResourceAgency.basePath = basePath+"WEB-INF/classes/config/";
		
		weixinCfg = new ReadResourceAgency("weixin");
		//icbcCfg.getConfigInfo("dsf");
	}
	

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());

	}

}
class AgencyConfigDbContent {
	public String value;
	public long lastTime;
	public AgencyConfigDbContent(){
		value="";
		lastTime=0;
	}
}
