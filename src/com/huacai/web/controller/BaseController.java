package com.huacai.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import libcore.util.VarUtil;
import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.alibaba.fastjson.JSONObject;
import com.huacai.web.dao.syslog.LogOperateDao;
import com.huacai.web.filters.InitConfig;

public class BaseController {
	protected final static Logger logger = LogManager.getLogger(BaseController.class);
	
	@Autowired
	protected LogOperateDao logOperateDao;
	
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected HttpSession session;
	protected int pageSize = 10;

	@ModelAttribute
	public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		this.session = request.getSession();
	}

	/**
	 * 获取商户ID
	 * 
	 * @return
	 */
	protected String getWebAgencyId() {
		return (String) request.getAttribute("_WEB_AGENCY_ID_");
	}

	protected int getWebAgencyIdInt() {
		return VarUtil.intval((String) request.getAttribute("_WEB_AGENCY_ID_"));
	}

	/**
	 * 通用返回
	 * 
	 * @param pageStr
	 * @return
	 */
	protected void result(String out_str) {
		response.setContentType("text/html;charset=UTF-8");
		try {
			response.getWriter().write(out_str);
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	/**
	 * 通用返回
	 * 
	 * @param model
	 * @param data
	 * @return
	 */
	protected String result(Map<String, Object> model, String data) {
		model.put("out_data", data);
		return "/common/result";
	}

	/**
	 * 通用返回
	 * 
	 * @param pageStr
	 * @return
	 */
	protected void resultGbk(String out_str) {
		response.setContentType("text/html;charset=gb2312");
		try {
			response.getWriter().write(out_str);
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	/**
	 * 通用返回
	 * 
	 * @param pageStr
	 * @return
	 */
	protected void download(String filename, String content) {
		response.setContentType("application/x-msdownload");
		response.setHeader("Content-Disposition", "attachment;filename=" + filename);

		try {
			response.getWriter().write(content);
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	/**
	 * 跳转到指定url
	 * 
	 * @param pageStr
	 * @return
	 */
	protected void redirect(String url) {
		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	/**
	 * 为url追加参数
	 * 
	 * @param url
	 * @param parm
	 * @return
	 */
	protected String addUrlParm(String url, String parm) {
		return url.indexOf("?") > -1 ? url + "&" + parm : url + "?" + parm;
	}

	/**
	 * 获取返回信息
	 * 
	 * @param result
	 * @param message
	 * @return
	 */
	protected String getAjaxMessage(int result, String message) {
		return getAjaxMessage(result, message, null);
	}

	protected String getAjaxMessage(int result, String message, JSONObject data) {
		JSONObject json = new JSONObject();
		json.put("result", result);
		json.put("message", message);

		if (data != null) {
			json.putAll(data);
		}

		return json.toJSONString();
	}

	// //return getMessage(model,"出错了", "提示信息", "error", "/test.do");
	/**
	 * 小窗口错误提示
	 * 
	 * @param model
	 * @param icon
	 * @param title
	 * @param message
	 * @param url
	 * @param js
	 * @return
	 */
	protected String getIframeMessage(Map<String, Object> model, String message, String title, String icon, String url, String js) {
		model.put("message_icon", icon != null && icon.length() > 0 ? "message_" + icon : "message_alert");
		model.put("message_title", title != null && title.length() > 0 ? title : "提示信息");
		model.put("message_content", message);
		model.put("message_url", url);
		model.put("message_js", js);
		return "/common/message_iframe";
	}

	/**
	 * 小窗口错误提示
	 * 
	 * @param model
	 * @param message
	 * @param title
	 * @param icon
	 * @param url
	 * @return
	 */
	protected String getIframeMessage(Map<String, Object> model, String message, String title, String icon, String url) {
		return getIframeMessage(model, message, title, icon, url, "");
	}

	/**
	 * 小窗口错误提示
	 * 
	 * @param model
	 * @param message
	 * @param title
	 * @param url
	 * @return
	 */
	protected String getIframeMessage(Map<String, Object> model, String message, String title, String url) {
		return getIframeMessage(model, message, title, "", url, "");
	}

	/**
	 * 小窗口错误提示
	 * 
	 * @param model
	 * @param message
	 * @param url
	 * @return
	 */
	protected String getIframeMessage(Map<String, Object> model, String message, String url) {
		return getIframeMessage(model, message, "", "", url, "");
	}

	// //return getIframeMessage(model,"出错了", "提示信息", "error", "/test.do");
	/**
	 * 错误提示
	 * 
	 * @param model
	 * @param icon
	 * @param title
	 * @param message
	 * @param url
	 * @param js
	 * @return
	 */
	protected String getMessage(Map<String, Object> model, String message, String title, String icon, String url, String js) {
		model.put("message_icon", icon != null && icon.length() > 0 ? "message_" + icon : "message_alert");
		model.put("message_title", title != null && title.length() > 0 ? title : "提示信息");
		model.put("message_content", message);
		model.put("message_url", url);
		model.put("message_js", js);
		return "/common/message";
	}

	/**
	 * 错误提示
	 * 
	 * @param model
	 * @param message
	 * @param title
	 * @param icon
	 * @param url
	 * @return
	 */
	protected String getMessage(Map<String, Object> model, String message, String title, String icon, String url) {
		return getMessage(model, message, title, icon, url, "");
	}

	/**
	 * 错误提示
	 * 
	 * @param model
	 * @param message
	 * @param title
	 * @param url
	 * @return
	 */
	protected String getMessage(Map<String, Object> model, String message, String title, String url) {
		return getMessage(model, message, title, "", url, "");
	}

	/**
	 * 错误提示
	 * 
	 * @param model
	 * @param message
	 * @param url
	 * @return
	 */
	protected String getMessage(Map<String, Object> model, String message, String url) {
		return getMessage(model, message, "", "", url, "");
	}

	
	/**
	 * excel文件下载
	 * 
	 * @param response
	 *                响应
	 * @param templateFileName
	 *                excel模板文件
	 * @param destFileName
	 *                生成的excel文件
	 * @param m
	 *                参数值
	 */
	@SuppressWarnings("rawtypes")
	public void exportXls(String tplFileName, String destFileName, Map model) {

		String execlPath = InitConfig.tplPath + "export";

		XLSTransformer transformer = new XLSTransformer();
		try {
			logger.info("execlFile = " + execlPath + tplFileName);
			
			// 设置response的编码方式
			response.setContentType("application/x-msdownload");
			// 解决中文乱码
			destFileName = new String(destFileName.getBytes("UTF-8"), "ISO8859-1");

			response.setHeader("Content-Disposition", "attachment;filename=" + destFileName);
			// 从response对象中得到输出流,准备下载
			OutputStream myout = response.getOutputStream();
			// logger.info(tplPath + "/" + templateFileName);
			File file = new File(execlPath + tplFileName);

			InputStream is = new FileInputStream(file);

			Workbook workbook = transformer.transformXLS(is, model);
			workbook.write(myout);
			// 将写入到客户端的内存的数据,刷新到磁盘
			myout.flush();
			myout.close();
		} catch (ParsePropertyException e1) {
			logger.error("", e1);
		} catch (InvalidFormatException e1) {
			logger.error("", e1);
		} catch (IOException e1) {
			logger.error("", e1);
		}
	}
}
