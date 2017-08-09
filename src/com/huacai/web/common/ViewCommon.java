package com.huacai.web.common;

import java.io.IOException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

public class ViewCommon {
	protected final static Logger logger = LogManager.getLogger(ViewCommon.class);

	
	/**
	 * 获取商户ID
	 * @param request
	 * @return
	 */
	public static String getWebAgencyId(HttpServletRequest request) {
		return (String)request.getAttribute("_WEB_AGENCY_ID_");
	}
			
			
	/**
	 * 返回错误信息View
	 * 
	 * @param result
	 * @param message
	 * @return
	 */
	public static String getErrorView(Map<String, Object> model, String title, String message) {
		model.put("title", title);
		model.put("message", message);
		return "/common/error";
	}
	public static void forward(HttpServletRequest request,  HttpServletResponse response, String url){
		try{
			RequestDispatcher rd = request.getRequestDispatcher(url);
			rd.forward(request, response);
		}catch(Exception e){
			logger.error("", e);
		}
	}
	public static void redirect(HttpServletResponse response, String url){
		try{
			response.sendRedirect(url);
		}catch(Exception e){
			logger.error("", e);
		}
	}
	
	
	
	public static JSONObject getAjaxMessage(int result, String message) {
		return getAjaxMessage(result, message, null);
	}

	public static JSONObject getAjaxMessage(int result, String message, JSONObject data) {
		JSONObject json = new JSONObject();
		json.put("result", result);
		json.put("message", message);

		if (data != null) {
			json.putAll(data);
		}

		return json;
	}
	
	/**
	 * 通用返回
	 * 
	 * @param pageStr
	 * @return
	 */
	public static void result(HttpServletResponse response, String out_str) {
		response.setContentType("text/html;charset=UTF-8");
		try {
			response.getWriter().write(out_str);
		} catch (IOException e) {
			logger.error("", e);
		}
	}
}
