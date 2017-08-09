package com.huacai.web.constant;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import libcore.util.DateSimpleUtil;

import com.alibaba.fastjson.JSONObject;
import com.huacai.util.ReadResource;

/**
 * 模板全局变量
 * @author fuhua
 *
 */
public class TplViewConstants{
	
	
	public static void reg(HttpServletRequest request, HttpServletResponse response, Map<String, Object> model) {
		//全局变量
		JSONObject gjson = new JSONObject();
		gjson.put("now_time", System.currentTimeMillis());
		gjson.put("now_date", DateSimpleUtil.date("Y-m-d H:i:s"));
		
		model.put("_GLOBAL", gjson);
		
		
		model.put("SITE_NAME", ReadResource.get("SITE_NAME"));
		
		model.put("request", request);
		model.put("response", response);
	}


}