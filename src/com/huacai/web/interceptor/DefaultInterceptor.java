package com.huacai.web.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.huacai.util.ReadResource;
import com.huacai.util.SafeStringUtil;
import com.huacai.web.annotation.TaskSendMailTemplate;
import com.huacai.web.service.EmailServiceImpl;

import libcore.util.PaginationContext;

public class DefaultInterceptor implements HandlerInterceptor {
	protected final static Logger logger = LogManager.getLogger(DefaultInterceptor.class);
	@Autowired
	EmailServiceImpl emailService;

	/**
	 * 可以根据ex是否为null判断是否发生了异常，进行日志记录
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object obj, Exception e) throws Exception {
	}

	/**
	 * 可以修改ModelAndView
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj, ModelAndView model) throws Exception {
		if (obj instanceof HandlerMethod) {
			HandlerMethod method = (HandlerMethod) obj;
			Method m = method.getMethod();
			TaskSendMailTemplate module = m.getDeclaredAnnotation(TaskSendMailTemplate.class);
			if (module != null) {
				MethodParameter[] mps = method.getMethodParameters();
				for (MethodParameter mp : mps) {
					if ("actType".equals(mp.getParameterName())//
							&& !"save".equals(request.getParameter("actType"))) {
						return;
					}
				}

				if (model == null || !model.getModel().containsKey("error_msg")) {
					emailService.sendMail(request, module.value());
				}

			}
		}
		PaginationContext.removePageNum();
		PaginationContext.removePageSize();
	}

	/**
	 * 进行编码、安全控制等处理
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		/*
		 * WebApplicationContext wac =
		 * (WebApplicationContext)servletContext.getAttribute(
		 * WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		 * 
		 */
		String queryStr = request.getQueryString();

		String url = request.getRequestURI() + (queryStr == null ? "" : "?" + queryStr);
		if (!url.contains("ogin_status.do")) {
			logger.info(url);
		}

		// HandlerAdapter
		// 处理用户变量，如有非法关键词则停止执行
		if (!SafeStringUtil.checkRequest(request)) {
			logger.error("URL请求中包含非法字符");
			// response.sendRedirect("/error/error_warning.html");
			response.getWriter().write("URL request contains illegal characters");
			return false;
		}

		PaginationContext.setPageNum(getPageNum(request));
		PaginationContext.setPageSize(getPageSize(request));

		return true;
	}

	/**
	 * 获得pager.offset参数的值
	 * 
	 * @param request
	 * @return
	 */
	protected int getPageNum(HttpServletRequest request) {
		int pageNum = 1;
		try {
			String pageNums = request.getParameter(PaginationContext.PAGE_NUM);
			if (pageNums != null && StringUtils.isNumeric(pageNums)) {
				pageNum = Integer.parseInt(pageNums);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return pageNum;
	}

	/**
	 * 设置默认每页大小
	 * 
	 * @return
	 */
	protected int getPageSize(HttpServletRequest request) {
		int pageSize = ReadResource.getInt("pageSize", 10); // 默认每页10条记录
		try {
			String pageSizes = request.getParameter(PaginationContext.PAGE_SIZE);
			if (pageSizes != null && StringUtils.isNumeric(pageSizes)) {
				pageSize = Integer.parseInt(pageSizes);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return pageSize;
	}
}
