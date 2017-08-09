/**
 * 导航配置读取器
 */
package com.huacai.web.common;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import libcore.util.FileUtil;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @author Andyfoo
 *
 */
public class Nav {
	protected final static Logger logger = LogManager.getLogger(Nav.class);
	
	public static String tplPath;
	public static ConcurrentSkipListMap<String, NavFile> navMap = new ConcurrentSkipListMap<String, NavFile>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		long lasting = System.currentTimeMillis();
		tplPath = "E:/workspace_jee/huacai/huacai_v4/huacai_v4/WebContent/tpl/";
		JSONObject item;

		item = getValue("10000", "index");
		System.out.println("index_is_empty:" + item.isEmpty());
		System.out.println("index_title:" + item.getString("title"));
		System.out.println("index_key:" + item.getString("key"));

		item = getValue("10000", "login");
		System.out.println("");
		System.out.println("login_is_empty:" + item.isEmpty());
		System.out.println("index_title:" + item.getString("title"));
		System.out.println("index_key:" + item.getString("key"));

		System.out.println("运行时间：" + (System.currentTimeMillis() - lasting) + " 毫秒");
	}
 

	/**
	 * 注册模板变量
	 * @param model
	 * @param AgencyId
	 * @param platform
	 * @param k
	 */
	public static void setTplVars(Map<String, Object> model, String AgencyId, int platform, String k) {
		JSONObject nav = getValue(AgencyId, platform, k);
		model.put("nav_title", nav.getString("title"));
		model.put("nav_key", nav.getString("key"));
		model.put("nav_desc", nav.getString("desc"));
		model.put("nav_nav", nav.getString("nav"));
		model.put("nav_uri", nav.getString("uri"));
	}

	/**
	 * 读取配置
	 * @param AgencyId
	 */
	public static String readConfig(String AgencyId, int platform) {
		try {
			String DefFile = tplPath + "default/nav.json";
			String AgencyFile = tplPath + AgencyId + "/nav.json";
			if(platform >= 0){
				DefFile = tplPath + "default/nav_" + platform + ".json";
				AgencyFile = tplPath + AgencyId + "/nav_" + platform + ".json";
			}
			String key = "default_"+platform;
			String filename = DefFile;
			File test = new File(AgencyFile);
			
			//判断是否存在商户文件
			if (test.exists()) {
				filename = AgencyFile;
				key = AgencyId+"_"+platform;
			}
			logger.info("读取文件:" + filename);
			
			//打开文件属性
			File f = new File(filename);
			NavFile navFile = navMap.get(key);
			//判断文件是否过期
			if(navFile!=null&&navFile.lastModified==f.lastModified()){
				return key;
			}
			JSONObject json = JSON.parseObject(FileUtil.fileRead(f));
			if(json == null)return key;
			
			//读取文件
			navFile = new NavFile();
			navFile.agencyId = AgencyId;
			navFile.platform = platform;
			navFile.lastModified = f.lastModified();
			navFile.data = json;
			
			navMap.put(key, navFile);
			return key;
		} catch (Exception e) {
			logger.error("", e);
			return null;
		}
	}

	/**
	 * 取一个标签值
	 * @param AgencyId
	 * @param platForm
	 * @param k
	 * @return
	 */
	public static JSONObject getValue(String AgencyId, int platform, String k) {
		String key = readConfig(AgencyId, platform);
		if (k == null || k.length() == 0) {
			k = "index";
		}
		NavFile navFile = navMap.get(key);
		if(navFile == null){
			return new JSONObject();
		}
		JSONObject nItem = navFile.data.getJSONObject(k);
		if (nItem != null) {
			return nItem;
		} else {
			return new JSONObject();
		}
	}
	public static JSONObject getValue(String AgencyId, String k) {
		return getValue(AgencyId, -1, k);
	}
}
class NavFile {
	public String agencyId;
	public int platform;
	
	
	public long lastModified;
	public JSONObject data;
	
}