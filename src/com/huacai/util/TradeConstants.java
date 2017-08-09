package com.huacai.util;

import java.util.HashMap;
import java.util.Map;

public class TradeConstants {

	// 彩种定义
	public static final int LOT_SSQ = 101;// 双色球
	public static final int LOT_QLC = 102;// 七乐彩
	public static final int LOT_3D = 103;// 福彩3D
	
	public static final int LOT_CQSSC = 145;// 重庆时时彩

	public static final Map<Integer, String> LOT_INFO = new HashMap<Integer, String>();

	static {
		LOT_INFO.put(LOT_SSQ, "双色球");
		LOT_INFO.put(LOT_QLC, "七乐彩");
		LOT_INFO.put(LOT_3D, "3D");
		LOT_INFO.put(LOT_CQSSC, "重庆时时彩");
	}

	// 接入平台
	public static final int FROM_WEB = 1; // 互联网网站

	public static final int FROM_WAP = 2; // WAP网站

	public static final int FROM_MOBILE_CLIENT = 3; // 手机客户端

	public static final int FROM_PC_CLIENT = 4; // PC客户端

	public static final int FROM_IPTV = 5; // 数字电视(IPTV)

	public static final int FROM_AUTO_TERMINAL= 6; // 自动终端

	/**
	 * xml传输中投注号码的分割符
	 */
	public static final String SEPARATING_CHARACTER = "~";// xml传输中投注号码的分割符

	// 审核状态
	public static final int WIN_AUDIT_STATUS_ZC = 0;// 正常
	public static final int WIN_AUDIT_STATUS_DCL = 1;// 待处理
	public static final int WIN_AUDIT_STATUS_SHZ = 2;// 审核中
	public static final int WIN_AUDIT_STATUS_TG = 3;// 通过
	public static final int WIN_AUDIT_STATUS_BH = 4;// 驳回
	
	public static final Map<Integer, String> AUDIT_STATUS = new HashMap<Integer, String>();

	static {
		AUDIT_STATUS.put(WIN_AUDIT_STATUS_ZC, "正常");
		AUDIT_STATUS.put(WIN_AUDIT_STATUS_DCL, "待审核");
		AUDIT_STATUS.put(WIN_AUDIT_STATUS_SHZ, "审核中");
		AUDIT_STATUS.put(WIN_AUDIT_STATUS_TG, "审核通过");
		AUDIT_STATUS.put(WIN_AUDIT_STATUS_BH, "驳回");
	}

	/**
	 * 彩种预生成期数最大值
	 */
	public static final int ISSUE_NUMER_MAX = 15;// 彩种预生成期数最大值

	// 关于期次的状态定义
	// 期次销售定义
	public static final int LOT_WS = 0;// 未销售
	public static final int LOT_YS = 1;// 预售
	public static final int LOT_XS = 2;// 开始销售
	public static final int LOT_JZ = 3;// 截止销售
	public static final int LOT_KJ = 4;// 已开奖
	public static final int LOT_PJ = 5;// 已派奖
	public static final int LOT_QX = 6;// 取消
	public static final int LOT_ZT = 7;// 暂停

	// 投注相关// 期次与倍数之间分割符
	/**
	 * 投注相关// 期次与倍数之间分割符，期次与倍数组合中期次与倍数与另一组期次与倍数分割符
	 */
	public static final String ISSUEINFO_TOLEN_ONE = "\\|";// 期次与倍数组合中期次与倍数与另一组期次与倍数分割符

	/**
	 * 期次与倍数组合中
	 */
	public static final String ISSUEINFO_TOKEN_TOW = "#";// 期次与倍数组合中

	// 号码与玩法
	/**
	 * 订单投注号码之间分隔符
	 */
	public static final String ORDER_CONTENT_TOLEN_ONE = "\\|";// 订单投注号码之间分隔符

	/**
	 * 订单投注号码与玩法之间分隔符
	 */
	public static final String ORDER_CONTENT_TOLEN_TWO = "#";// 订单投注号码与玩法之间分隔符

	/**
	 * 号码之间的分隔符
	 */
	public static final String ORDER_CONTNENT_DOUHAO = ",";// 号码之间的分隔符

	/**
	 * 号码之间的分隔符
	 */
	public static final String ORDER_CONTNENT_FENHAO = ";";// 号码之间的分隔符

	/**
	 * 冒号
	 */
	public static final String ORDER_CONTNENT_MAOHAO = ":";// 冒号

	/**
	 * 胆拖之间分隔符
	 */
	public static final String ORDER_CONTNENT_DF = "\\$";// 胆拖之间分隔符

	/**
	 * 订单号码存储表中最大存储长度
	 */
	public static final int ORDER_LIMIT_LENGTH = 250;// 订单号码存储表中最大存储长度


	// 交易相关
	public static final int TRADE_STATUS_YTK = -7;// 已退款

	public static final int TRADE_STATUS_WKT = -6;// 未退款

	public static final int TRADE_STATUS_YGQ = -5;// 已过期

	public static final int TRADE_STATUS_QX = -4;// 交易取消

	public static final int TRADE_STATUS_GMSB = -3;// 购买失败

	public static final int TRADE_STATUS_WZF_YGQ = -2;// 未支付(已过期)

	public static final int TRADE_STATUS_WRK = -1; // 未入库（暂未出票）

	public static final int TRADE_STATUS_RK = 0;// 入库（待出票）

	public static final int TRADE_STATUS_WZF_KZF = 1; // 未支付(可以支付)

	public static final int TRADE_STATUS_YFS_JK = 2;// 已发送到接口

	public static final int TRADE_STATUS_YFS_WY = 3; // 已经发送到网银

	public static final int TRADE_STATUS_YZF = 4; // 已支付

	public static final int TRADE_STATUS_BFCG = 5;// 部分成功（部分购买）

	public static final int TRADE_STATUS_YGM = 6;// 交易成功（购买成功）

	public static final int TRADE_STATUS_ZJTZ = 7;// 中奖停止

	public static final int TRADE_STATUS_CXZH = 8;// 撤销追号

	public static final Map<Integer, String> TRADE_STATUS = new HashMap<Integer, String>();

	static {
		TRADE_STATUS.put(TRADE_STATUS_WZF_YGQ, "未支付(已过期)");
		TRADE_STATUS.put(TRADE_STATUS_WZF_KZF, "未支付");
		TRADE_STATUS.put(TRADE_STATUS_YZF, "已支付");
		TRADE_STATUS.put(TRADE_STATUS_RK, "待出票");
		TRADE_STATUS.put(TRADE_STATUS_YGM, "购买成功");
		TRADE_STATUS.put(TRADE_STATUS_GMSB, "购买失败");
		TRADE_STATUS.put(TRADE_STATUS_BFCG, "部分购买");
		TRADE_STATUS.put(TRADE_STATUS_QX, "交易取消");
		TRADE_STATUS.put(TRADE_STATUS_YTK, "已退款");
		TRADE_STATUS.put(TRADE_STATUS_WKT, "未退款");
		TRADE_STATUS.put(TRADE_STATUS_YGQ, "已过期");
		TRADE_STATUS.put(TRADE_STATUS_WRK, "暂未出票");
		TRADE_STATUS.put(TRADE_STATUS_ZJTZ, "中奖停止");
		TRADE_STATUS.put(TRADE_STATUS_YFS_JK, "已发送到接口");
		TRADE_STATUS.put(TRADE_STATUS_YFS_WY, "已经发送到网银");
		TRADE_STATUS.put(TRADE_STATUS_CXZH, "撤销追号");
	}

	// 中奖相关
	public static final int WIN_WKJ = 0;// 等待开奖

	public static final int WIN_WZJ = 1;// 未中奖

	public static final int WIN_YZJ = 2;// 已中奖

	public static final int WIN_WPJ = 3;// 未派奖

	public static final int WIN_YPJ = 4;// 已派奖

	public static final int WIN_ZDJ = 5;// 中大奖

	public static final int WIN_SH = 6;// 中奖金额计算有误需审核

	public static final int WIN_BH = 7;// 驳回中奖

	public static final Map<Integer, String> WIN_STATUS = new HashMap<Integer, String>();

	static {
		WIN_STATUS.put(WIN_WKJ, "等待开奖");
		WIN_STATUS.put(WIN_WZJ, "未中奖");
		WIN_STATUS.put(WIN_YZJ, "中奖");
		WIN_STATUS.put(WIN_WPJ, "未派奖");
		WIN_STATUS.put(WIN_YPJ, "已派奖");
		WIN_STATUS.put(WIN_ZDJ, "中大奖");
		WIN_STATUS.put(WIN_SH, "奖金审核中");
		WIN_STATUS.put(WIN_BH, "驳回中奖");
	}

	//订单类型
	public static final int ZHUIHAO_WZ = 0;// 未追号
	public static final int ZHUIHAO_LQ = 1;// 连期追号
	public static final int ZHUIHAO_TQ = 2;// 跳期追号
	public static final int ZHUIHAO_TC = 4;// 套餐追号

	public static HashMap<Integer, String> ZHUIHAO_TYPE = new HashMap<Integer, String>();
	static {
		ZHUIHAO_TYPE.put(ZHUIHAO_WZ, "单买");
		ZHUIHAO_TYPE.put(ZHUIHAO_LQ, "追号");
		ZHUIHAO_TYPE.put(ZHUIHAO_TQ, "追号");
		ZHUIHAO_TYPE.put(ZHUIHAO_TC, "套餐");
	}
	
	/**
	 * 中奖
	 */
	public static final int ZHONGJIANG = 1;// 中奖

	/**
	 * 未中奖
	 */
	public static final int WEIZHONGJIANG = -1;// 未中奖

	/** 中奖类型 */
	public static final int WIN_TYPE_WZJ = -1;// 中奖类型:未中奖
	public static final int WIN_TYPE_SMALL = 0;// 中奖类型:小奖
	public static final int WIN_TYPE_BIG = 1;// 中奖类型:大奖

	public static Map<Integer, String> WIN_TYPE = new HashMap<Integer, String>();
	static {
		WIN_TYPE.put(WIN_TYPE_WZJ, "未中奖");
		WIN_TYPE.put(WIN_TYPE_SMALL, "小奖");
		WIN_TYPE.put(WIN_TYPE_BIG, "大奖");
	}

	/**
	 * 中奖继续追号
	 */
	public static final int WIN_STOP_N = 0;// 中奖继续追号

	/**
	 * 中奖停止追号
	 */
	public static final int WIN_STOP_Y = 1;// 中奖停止追号

	/**
	 * 其它彩种无限制,默认为99
	 */
	public static final int LOT_TIMESBY_ORDER = 99;// 其它彩种无限制,默认为99

	// 开奖状态，默认值 0： 0 开奖号码待审核; 1 未计算; 2 已计算; 3 已兑奖; 4 已返奖 ; 5 计算中
	/**
	 * 开奖状态：0开奖号码待审核
	 */
	public static final int ISSUE_OPENPRZE_KJDSH = 0;

	/**
	 * 开奖状态：1 未计算
	 */
	public static final int ISSUE_OPENPRIZE_WJS = 1;

	/**
	 * 开奖状态：2 已计算
	 */
	public static final int ISSUE_OPENPRIZE_YJS = 2;

	/**
	 * 开奖状态：3 已兑奖
	 */
	public static final int ISSUE_OPENPRIZE_YDJ = 3;

	/**
	 * 开奖状态：4 已返奖
	 */
	public static final int ISSUE_OPENPRIZE_YFJ = 4;

	/**
	 * 开奖状态：5 计算中
	 */
	public static final int ISSUE_OPENPRIZE_JSZ = 5;

	public static final HashMap<Integer, String> ISSUE_OPENPRIZE_STATUS = new HashMap<Integer, String>();

	static {
		ISSUE_OPENPRIZE_STATUS.put(ISSUE_OPENPRZE_KJDSH, "开奖号码待审核");
		ISSUE_OPENPRIZE_STATUS.put(ISSUE_OPENPRIZE_WJS, "本地开奖未计算");
		ISSUE_OPENPRIZE_STATUS.put(ISSUE_OPENPRIZE_YJS, "本地开奖已计算");
		ISSUE_OPENPRIZE_STATUS.put(ISSUE_OPENPRIZE_YDJ, "本地开奖已兑奖");
		ISSUE_OPENPRIZE_STATUS.put(ISSUE_OPENPRIZE_YFJ, "已返奖");
		ISSUE_OPENPRIZE_STATUS.put(ISSUE_OPENPRIZE_JSZ, "本地开奖计算中");
	}
	
	// 中奖表：是否可以提现 0 不可以 1 可以
	public static final int WIN_CAN_DRAW = 0;

	public static final int WIN_CANNOT_DRAW = 1;
	
	
	
	// 中奖表：是否需要交税 0 不需要 1 需要
	public static final int WIN_IS_TAXED = 0;

	public static final int WIN_ISNOT_TAXED = 1;

	/**
	 * 待审核
	 */
	public static final int FOUNDER_DSH = 1;// 待审核

	/**
	 * 已通过
	 */
	public static final int FOUNDER_YTG = 2;// 已通过

	/**
	 * 未通过
	 */
	public static final int FOUNDER_WTG = 3;// 未通过

	public static final int KO_DSDG = 0;// 单式单买
	public static final int KO_FSDG = 1;// 复式单买
	public static final int KO_DTDG = 2;// 胆拖单买

	public static final HashMap<Integer, String> KO_TYPE = new HashMap<Integer, String>();

	static {
		KO_TYPE.put(KO_DSDG, "单式单买");
		KO_TYPE.put(KO_FSDG, "复式单买");
		KO_TYPE.put(KO_DTDG, "胆拖单买");
	}

	/**
	 * 套餐常量列表
	 */
	public static final int SETTYPE_YUE = 1;// 1 表示包月套餐
	public static final int SETTYPE_JIFU = 2;// 2 表示包季度套餐
	public static final int SETTYPE_BANNIAN = 3;// 3 包半年套餐
	public static final int SETTYPE_QUANNIAN = 4;// 4 包全年套餐
	public static final int SETTYPE_ZIDINGYI = 5;// 5 表示自定义套餐
	public static final HashMap<Integer, String> SETTYPE_MAP = new HashMap<Integer, String>();
	static {
		SETTYPE_MAP.put(SETTYPE_YUE, "包月套餐");
		SETTYPE_MAP.put(SETTYPE_JIFU, "包季度套餐");
		SETTYPE_MAP.put(SETTYPE_BANNIAN, "包半年套餐");
		SETTYPE_MAP.put(SETTYPE_QUANNIAN, "包全年套餐");
		SETTYPE_MAP.put(SETTYPE_ZIDINGYI, "自定义套餐");
	}

	public static final int SCHEMETYPE_JX = 1;// 机选
	public static final int SCHEMETYPE_ZX = 2;// 自选
	public static final HashMap<Integer, String> SCHEMETYPE_MAP = new HashMap<Integer, String>();
	static {
		SCHEMETYPE_MAP.put(SCHEMETYPE_JX, "机选");
		SCHEMETYPE_MAP.put(SCHEMETYPE_ZX, "自选");
	}
	
	public static final int STOP_WC = 1;// 1 完成方案终止
	public static final int STOP_ZJ = 2;// 2 中奖即停止
	public static final int STOP_ZJ_YI = 3;// 3 中一等奖即停止
	public static final int STOP_ZJ_ER = 4;// 4 中二等奖以上即停止

	public static final int LOT_TYPE_DS = 1;// 1 单式投注
	public static final int LOT_TYPE_FS = 2;// 2复式投注
	public static final int LOT_TYPE_DT = 3;// 3 胆拖投注

	public static final HashMap<Integer, String> LOT_TYPE = new HashMap<Integer, String>();

	static {
		LOT_TYPE.put(LOT_TYPE_DS, "单式投注");
		LOT_TYPE.put(LOT_TYPE_FS, "复式投注");
		LOT_TYPE.put(LOT_TYPE_DT, "胆拖投注");
	}

	public static int[] MIDDLE_TRADE_STATUS = new int[] { TRADE_STATUS_WRK, TRADE_STATUS_RK,
		TRADE_STATUS_WZF_KZF, TRADE_STATUS_YFS_JK, TRADE_STATUS_YFS_WY,
		TRADE_STATUS_YZF };
}
