package com.huacai.util;

import java.util.HashMap;
import java.util.Map;


/**
 * 
 * 
 * 功能说明：定义web层程序引用到的基本常量信息或公共信息
 */
public class ActiveConstants {

	/**
	 * 用户页面刷新
	 */
	public static final String SX =  "<script>parent.cp_body_reload();</script>";
	
	
	// 规则状态 0 表示不启用  1表示启用
	public static final int GAMERESP_RULE_STATUS_BQY = 0;
	public static final int GAMERESP_RULE_STATUS_QY = 1;
	/***
	 * 规则状态
	 */
	
	public static Map<Integer,String> GAMERESP_RULE_STATUS = new HashMap<Integer,String>();
	static{
		GAMERESP_RULE_STATUS.put(GAMERESP_RULE_STATUS_BQY, "不启用");
		GAMERESP_RULE_STATUS.put(GAMERESP_RULE_STATUS_QY, "启用");
	}
	// 用户级别限制   1基本账户、2手机密令、3数字证书、4U盾 每个级别的配置只能有一个
	public static final int USERGRADE_BASE = 1;
	public static final int USERGRADE_MOBILE = 2;
	public static final int USERGRADE_PASSPORT = 3;
	public static final int USERGRADE_UD = 4;
	/***
	 * 用户级别限制 
	 */
	
	public static Map<Integer,String> USERGRADE_MAP = new HashMap<Integer,String>();
	static{
		USERGRADE_MAP.put(USERGRADE_BASE, "基本账户");
		USERGRADE_MAP.put(USERGRADE_MOBILE, "手机密令");
		USERGRADE_MAP.put(USERGRADE_PASSPORT, "数字证书");
		USERGRADE_MAP.put(USERGRADE_UD, "U盾");
	}
 
	//会员交易类型 ALL(0,"全部"),RECHARGE(1,"充值数据"),TRANSACTION(2,"提现数据"),DRAW(3,"交易数据"),AWARD(4,"中奖数据");
	public static final int USERBILL_TYPE_ALL = 0;
	public static final int USERBILL_TYPE_RECHARGE = 1;
	public static final int USERBILL_TYPE_DRAW = 2;
	public static final int USERBILL_TYPE_SALE = 3;
	public static final int USERBILL_TYPE_WIN = 4;
	
	/***
	 * 会员交易类型
	 */
	public static Map<Integer,String> USERBILL_TYPE_MAP = new HashMap<Integer,String>();
	static{
		USERBILL_TYPE_MAP.put(USERBILL_TYPE_ALL, "全部");
		USERBILL_TYPE_MAP.put(USERBILL_TYPE_RECHARGE, "充值数据");
		USERBILL_TYPE_MAP.put(USERBILL_TYPE_DRAW, "提现数据");
		USERBILL_TYPE_MAP.put(USERBILL_TYPE_SALE, "交易数据");
		USERBILL_TYPE_MAP.put(USERBILL_TYPE_WIN, "中奖数据");
	}
	/***
	 * bill表中的订单类型
	 */
	public static final int BILL_ORDERTYPE_DG = 0; // 代购
	public static final int BILL_ORDERTYPE_ZH = 1; // 追号
	public static final int BILL_ORDERTYPE_TC = 2; // 套餐
	public static final int BILL_ORDERTYPE_CZ = 3; // 充值
	public static final int BILL_ORDERTYPE_TX = 4; // 提现
	public static final int BILL_ORDERTYPE_QT = 5; // 其它
	
	public static HashMap<Integer, String> ORDERTYPE_DESC = new HashMap<Integer, String>();
	static {
		ORDERTYPE_DESC.put(BILL_ORDERTYPE_DG, "代购");
		ORDERTYPE_DESC.put(BILL_ORDERTYPE_ZH, "追号");
		ORDERTYPE_DESC.put(BILL_ORDERTYPE_TC, "套餐");
		ORDERTYPE_DESC.put(BILL_ORDERTYPE_CZ, "充值");
		ORDERTYPE_DESC.put(BILL_ORDERTYPE_TX, "提现");
		ORDERTYPE_DESC.put(BILL_ORDERTYPE_QT, "其它");
	}

	/***
	 * 用户账户操作类型
	 */
	public static final int ACCOUNT_OP_TYPE_PLUS = 1;
	public static final int ACCOUNT_OP_TYPE_MINUS = 2;
	public static HashMap<Integer, String> ACCOUNT_OP_TYPE = new HashMap<Integer, String>();
	static {
		ACCOUNT_OP_TYPE.put(ACCOUNT_OP_TYPE_PLUS, "账户加钱");
		ACCOUNT_OP_TYPE.put(ACCOUNT_OP_TYPE_MINUS, "账户减钱");
	}
	public static final int INTEGRALACCOUNT_OP_TYPE_PLUS = 1;
	public static final int INTEGRALACCOUNT_OP_TYPE_MINUS = 2;
	public static HashMap<Integer, String> INTEGRALACCOUNT_OP_TYPE = new HashMap<Integer, String>();
	static {
		INTEGRALACCOUNT_OP_TYPE.put(INTEGRALACCOUNT_OP_TYPE_PLUS, "增加积分");
		INTEGRALACCOUNT_OP_TYPE.put(INTEGRALACCOUNT_OP_TYPE_MINUS, "扣除积分");
	}
	/***
	 *  账户类型
	 */
	public static final int ACCOUNT_TYPE_XJZH = 1;  // 现金账户
	public static final int ACCOUNT_TYPE_JJZH = 2;  // 奖金账户
	public static final int ACCOUNT_TYPE_CJZH = 3;  // 彩金账户
	public static final int ACCOUNT_TYPE_JFZH = 4;  // 积分账户
	public static HashMap<Integer, String> ACCOUNT_TYPE = new HashMap<Integer, String>();
	static {
		ACCOUNT_TYPE.put(ACCOUNT_TYPE_XJZH, "现金账户");
		ACCOUNT_TYPE.put(ACCOUNT_TYPE_JJZH, "奖金账户");
		ACCOUNT_TYPE.put(ACCOUNT_TYPE_CJZH, "彩金账户");
		ACCOUNT_TYPE.put(ACCOUNT_TYPE_JFZH, "积分账户");
	}
	/***
	 * 子账户是否是默认账户
	 */
	public static final int ISDEFAULT_YES = 1; //表示为默认子账户,该账户金额没有过期时间限制
	public static final int ISDEFAULT_NO = 0;  //表示有有效期限制
	public static HashMap<Integer, String> ACCOUNT_IS_DEFAULT = new HashMap<Integer, String>();
	static {
		ACCOUNT_IS_DEFAULT.put(ISDEFAULT_YES, "默认子账户");
		ACCOUNT_IS_DEFAULT.put(ISDEFAULT_NO, "有效期限制账户");
	}
	/**
	 * 锁定账户
	 */
	public final static int LOCK = 1;   
	/**
	 * 解锁（正常）
	 */
	public final static int UNLOCK = 2;
	
	/**
	 * 冻结账户
	 */
	public final static int FROZE = 3;
	
	/**
	 * 注销账户
	 */
	public final static int ANNUL = 4;
	public static HashMap<Integer, String> ACCOUNT_STATUS_OPTYPE = new HashMap<Integer, String>();
	static {
		ACCOUNT_STATUS_OPTYPE.put(LOCK, "锁定");
		ACCOUNT_STATUS_OPTYPE.put(UNLOCK, "正常");
		ACCOUNT_STATUS_OPTYPE.put(FROZE, "冻结");
		ACCOUNT_STATUS_OPTYPE.put(ANNUL, "注销");
	}
	/***
	 * 促销活动
	 */
	public static final int ACTIVE_TYPE_REGISTER = 1;
	public static final int ACTIVE_TYPE_DEPOSIT = 2;
	public static final int ACTIVE_TYPE_TRADE = 3;
	public static final int ACTIVE_TYPE_WIN = 4;
	
	public static final Map<Integer, String> activeTypeMap = new HashMap<Integer, String>();
	static {
		activeTypeMap.put(ACTIVE_TYPE_REGISTER, "注册送彩金");
		activeTypeMap.put(ACTIVE_TYPE_DEPOSIT, "充值送彩金");
		activeTypeMap.put(ACTIVE_TYPE_TRADE, "投注送彩金");
		activeTypeMap.put(ACTIVE_TYPE_WIN, "中奖送彩金");
	}
	
		public static final int DRAW_STATUS_START = 0;
		public static final int DRAW_STATUS_SUCCESS = 1;
		public static final int DRAW_STATUS_FAIL = 2;
		/***
		 * 用户级别限制 
		 */
		
		public static Map<Integer,String> DRAWSTATUS_MAP = new HashMap<Integer,String>();
		static{
			DRAWSTATUS_MAP.put(DRAW_STATUS_START, "发起");
			DRAWSTATUS_MAP.put(DRAW_STATUS_SUCCESS, "成功");
			DRAWSTATUS_MAP.put(DRAW_STATUS_FAIL, "失败");
		}
		
		public static final long COMMISSION_TIME_DAY=  1;//天
		
		public static final long COMMISSION_TIME_zhou = 2; //周
		
		public static final long COMMISSION_TIME_yue = 3; //月
		
		public static final long COMMISSION_TIME_jidu = 4; //季度
		
		public static final long COMMISSION_TIME_year = 5; //年
		
		
		public static final Map<Long,String>  COMMISSION_TIME_MAP= new HashMap<Long,String>();
		static{
			COMMISSION_TIME_MAP.put(COMMISSION_TIME_DAY, "天");
			COMMISSION_TIME_MAP.put(COMMISSION_TIME_zhou, "周");
			COMMISSION_TIME_MAP.put(COMMISSION_TIME_yue, "月");
			COMMISSION_TIME_MAP.put(COMMISSION_TIME_jidu, "季度");
			COMMISSION_TIME_MAP.put(COMMISSION_TIME_year, "年");
		}	
}
