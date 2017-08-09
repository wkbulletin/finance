package com.huacai.web.common;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import libcore.util.StringUtil;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class DbCommon {
	protected static final Logger logger = LogManager.getLogger(DbCommon.class);
	
	
	/**
	 * 获取当前时间
	 * @return
	 */
	public static double getTime() {
		return System.currentTimeMillis();
	}
	
	//计算运行时间
	public static String runTime(double t) {
		return StringUtil.formatRate((getTime() - t) / 1000, "##0.000") + "s";
	}
	
	/**
	 * 将列表转换为json(字段名小写)
	 * @param lists
	 * @return
	 */
	public static JSONArray field2json(List<Map<String, Object>> lists) {
		JSONArray json = new JSONArray();
		for (Map<String, Object> map : lists) {
			json.add(field2json(map));
		}
		return json;
	}
	/**
	 * 将map转换为json(字段名小写)
	 * @param map
	 * @return
	 */
	public static JSONObject field2json(Map<String, Object> map) {
		if (map == null)
			return null;
		JSONObject json = new JSONObject();
		String key;
		Object val;
		Iterator<Entry<String, Object>> iter = map.entrySet().iterator();
		Map.Entry<String, Object> entry;
		while (iter.hasNext()) {
			entry = iter.next();
			key = entry.getKey();
			val = entry.getValue();
			json.put(key.toLowerCase(), val);
		}
		return json;
	}
	
	
	/**
	 * 将列表转换为json(大写小名转换，如user_list字段，这里变为 userList)
	 * @param lists
	 * @return
	 */
	public static JSONArray toJSON(List<Map<String, Object>> lists) {
		JSONArray json = new JSONArray();
		for (Map<String, Object> map : lists) {
			json.add(toJSON(map));
		}
		return json;
	}
	/**
	 * 将map转换为json(大写小名转换，如user_list字段，这里变为 userList)
	 * @param map
	 * @return
	 */
	public static JSONObject toJSON(Map<String, Object> map) {
		if (map == null)
			return null;
		JSONObject json = new JSONObject();
		String key;
		Object val;
		Iterator<Entry<String, Object>> iter = map.entrySet().iterator();
		Map.Entry<String, Object> entry;
		while (iter.hasNext()) {
			entry = iter.next();
			key = entry.getKey();
			val = entry.getValue();
			json.put(changeCase(key), val);

		}
		return json;
	}

	/**
	 * 
	 * 功能：将输入字符串的首字母改成大写
	 * 
	 * @param str
	 * 
	 * @return
	 */
	private static String changeCase(String str) {
		String[] arr = str.toLowerCase().split("_");
		StringBuffer sb = new StringBuffer();
		sb.append(arr[0]);
		int len = arr.length;
		for (int i = 1; i < len; i++) {
			sb.append(arr[i].substring(0, 1).toUpperCase());
			sb.append(arr[i].substring(1));
		}
		return sb.toString();
	}
}
