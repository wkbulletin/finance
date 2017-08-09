package com.huacai.web.constant;


/**
 * 
 * 
 * 功能说明：定义web层程序引用到的基本常量信息或公共信息
 */
public class Constants {

	// 用户登录信息session名称
	public static final String USERCONTEXT = "USERCONTEXT";

	// 密码错误次数session
	public static final String SESSION_LOGIN_ERROR = "SESS_LOGIN_ERROR";

	// 验证码session
	public static final String SESSION_LOGIN_VCODE = "SESS_LOGIN_VCODE";

	// 用户ID信息
	public static final String SESSION_MEMBER_ID = "SESS_MEMBER_ID";

	/*
	 * 自定义常量，或者继承此类进行特殊处理
	 */
	// 默认的每页记录数
	public static final int MINI_PAGESIZE = 5;

	public static final int SMALL_PAGESIZE = 10;

	public static final int PAGESIZE = 15;
 
	
}
