package com.huacai.web.sms;

/**
 * 短信状态常量
 */
public class MessageStatus { 
	// 收件箱
	public static final String TYPE_INCOMING = "IN";

	// 发件箱
	public static final String TYPE_OUTGOING = "OUT";

	// 信息发送成功
	public static final String SEND_SUCCEED = "00";

	// 信息发送失败
	public static final String SEND_FAILED = "12";

	// 信息正在发送
	public static final String PROCESS_SEND = "90";

	// 信息等待发送
	public static final String WAIT_SEND = "99";

	// 信息未读
	public static final String NON_READ = "59";

	// 信息已读并且处理成功
	public static final String READED_SUCCEED = "50";

	// 0网关接收成功
	// 1帐户密码错误
	// 2接收号码错误
	// 3短信内容过长或包含非法内容
	// 4帐户过期
	// 5帐户余额不足
	// 6IP地址验证错误
	// 7超出每日短信发送量
	// 8短信流水号错误
	// 9系统错误
	public static String SMS_SEND_SUCCEED = "0";

	public static String SMS_ACCOUNT_ERROR = "1";

	public static String SMS_MOBILE_ERROR = "2";

	public static String SMS_MESSAGE_ERROR = "3";

	public static String SMS_ACCOUNT_DISABLED_ERROR = "4";

	public static String SMS_ACCOUNT_BALANCE_ERROR = "5";

	public static String SMS_IP_ERROR = "6";

	public static String SMS_NUMBER_ERROR = "7";

	public static String SMS_SN_ERROR = "8";

	public static String SMS_SYSTEM_ERROR = "9";
}