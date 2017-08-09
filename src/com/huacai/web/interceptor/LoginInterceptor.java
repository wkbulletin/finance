package com.huacai.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.huacai.web.common.LoginCommon;
import com.huacai.web.common.ViewCommon;

public class LoginInterceptor  implements HandlerInterceptor  {
	protected final static Logger logger = LogManager.getLogger(LoginInterceptor.class);
//	@Autowired
//	private ViewResolver tplView;
//	
	
	
	/**
	 * 
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object obj, Exception e)
			throws Exception {
	}

	/**
	 * 
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj, ModelAndView model)
			throws Exception {
	
		
		
	}

	/**
	 * 判断登录状态
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String currUrl = request.getRequestURI();
		//System.out.println(currUrl);
		
		if(LoginCommon.checkNoLoginUrl(currUrl)){
			return true;
		}
		if(!LoginCommon.isLogin(request)){
			ViewCommon.redirect(response, "/login.do");
			return false;
		}
		//判断URL权限
		if(!LoginCommon.checkUrlPriv(request)){
			String xReqWith = request.getHeader("X-Requested-With");
			String accept = request.getHeader("Accept");
			if(xReqWith!=null&&accept!=null&& xReqWith.equals("XMLHttpRequest")&&accept.indexOf("application/json")>-1){
				ViewCommon.result(response, ViewCommon.getAjaxMessage(-1, "您没有此操作权限").toJSONString());
				return false;
			}
			ViewCommon.redirect(response, "/no_access.do");
			return false;
		}
		
		return true;
	}


}
