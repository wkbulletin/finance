package com.huacai.web.common;

import java.util.HashMap;
import java.util.Map;



/**
 * 
 * 
 * 功能说明：定义工作流任务实例名称
 */
public class ActivitiConstants {

	/**
	 * 期次年度初始化流程实例名
	 */
	public static final String TERM_INIT =  "sj02";
	
	/**
	 * 休市信息流程实例名
	 */
	public static final String GAME_CLOSE =  "sj03";
	
	/**
	 * 充值提现流程实例名
	 */
	public static final String BOSS_AUDIT_DRAW =  "sj04";
	
	/**
	 * 超级大奖流程实例名
	 */
	public static final String BOSS_AUDIT_SUPERAWARD =  "sj05";
	
	/**
	 * 游戏状态流程实例名
	 */
	public static final String BOSS_AUDIT_GAMESTATUS =  "sj06";
	
	
	/**
	 * 弃奖审核流程实例名
	 */
	public static final String BOSS_AUDIT_ABANDON_AWARD =  "sj07";
	
	/**
	 * 充值冲正流程实例名
	 */
	public static final String BOSS_AUDIT_DEPOSIT_CZ =  "sj08";
	
	
	/**
	 * 提现冲正流程实例名
	 */
	public static final String BOSS_AUDIT_DRAW_CZ =  "sj09";
	
	/**
	 * 充值补登流程实例名
	 */
	public static final String BOSS_AUDIT_DEPOSIT_BD =  "sj10";
	
	/**
	 * 提现补登流程实例名
	 */
	public static final String BOSS_AUDIT_DRAW_BD =  "sj11";
	
	/**
	 * 注销账户流程实例名
	 */
	public static final String BOSS_AUDIT_CANCEL_USER =  "sj12";
	
	/**
	 * 大奖解冻流程实例名
	 */
	public static final String BOSS_AUDIT_BIG_AWARD =  "sj13";
	
	/**
	 * 积分调整流程实例名
	 */
	public static final String BOSS_AUDIT_POINT_ADJUST =  "sj14";
	
	/**
	 * 佣金设定流程实例名
	 */
	public static final String BOSS_AUDIT_COMMISSION =  "sj15";
	
	/**
	 * 保证金设定流程实例名
	 */
	public static final String BOSS_AUDIT_DEPOSIT =  "sj16";
	
	/**
	 * 活动添加流程实例名
	 */
	public static final String BOSS_AUDIT_ACTIVE =  "sj17";
	
	/**
	 * 彩金有效期调节流程实例名
	 */
	public static final String BOSS_AUDIT_CJEX =  "sj18";
	
	/**
	 * 现金调节流程实例名
	 */
	public static final String BOSS_AUDIT_XJ =  "sj19";
	
	/**
	 * 彩金调节流程实例名
	 */
	public static final String BOSS_AUDIT_CJ =  "sj20";
	
	
	/**
	 * 商户开户流程实例名
	 */
	public static final String BOSS_AUDIT_MERCHANTKH =  "sj21";
	
	
	/**
	 * 开奖号码审核流程实例名
	 */
	public static final String BOSS_AUDIT_AWARD_NUM =  "sj22";
	
	
	/**
	 * 销售规则添加流程实例名
	 */
	public static final String BOSS_AUDIT_SALE_RULE_ADD =  "sj23";
	
	/**
	 * 销售规则修改流程实例名
	 */
	public static final String BOSS_AUDIT_SALE_RULE_UPDATE =  "sj24";
	
	/**
	 * 奖级信息审核流程实例名
	 */
	public static final String BOSS_AUDIT_SALE_AWARD_LEVEL =  "sj25";
	
	/**
	 * 期次奖级添加审核流程实例名
	 */
	public static final String BOSS_AUDIT_ISSUE_LEVEL_ADD =  "sj26";
	
	/**
	 * 期次奖级修改审核流程实例名
	 */
	public static final String BOSS_AUDIT_ISSUE_LEVEL_UPDATE =  "sj27";
	
	
	/**
	 * 节假日信息添加核流程实例名
	 */
	public static final String BOSS_AUDIT_HOLIDAY_ADD =  "sj28";
	
	
	
	/**
	 * BOSS审核类型
	 */
	public static final int BOSS_AUDIT_TYEP_DRAW =  1;//提现
	
	public static final int BOSS_AUDIT_TYEP_ACCOUNTROOT =  2;//主账户修改
	
	public static final int BOSS_AUDIT_TYEP_ACCOUNTSON =  3;//子账户修改
	
	public static final int BOSS_AUDIT_TYEP_GAMEKU =  4;//游戏库配置
	
	public static final int BOSS_AUDIT_TYEP_SUPBIG_AWARD =  5; //超级大奖兑付
	
	public static final int BOSS_AUDIT_TYEP_AWARD_NUM =  6;//开奖号码审核
	
	public static final int BOSS_AUDIT_TYEP_AWARD_LEVEL =  7;//奖级信息审核
	
	public static final int BOSS_AUDIT_TYEP_ISSUE_LEVEL_ADD =  8;//期次奖级添加审核
	
	public static final int BOSS_AUDIT_TYEP_ISSUE_LEVEL_UPDATE =  9;//期次奖级更新审核
	
	public static final int BOSS_AUDIT_TYEP_SALE_RULE_ADD =  10;//销售规则添加审核

	public static final int BOSS_AUDIT_TYEP_SALE_RULE_UPDATE =  11;//销售规则更新审核
	
	public static final int BOSS_AUDIT_TYEP_GAME_STATUS =  12;//游戏状态审核
	
	public static final int BOSS_AUDIT_TYEP_ABANDON_AWARD =  13;//弃奖审核
	
	public static final int BOSS_AUDIT_TYEP_DEPOSIT_CZ =  14;//充值冲正
	
	public static final int BOSS_AUDIT_TYEP_DRAW_CZ =  15;//提现冲正
	
	public static final int BOSS_AUDIT_TYEP_DEPOSIT_BD =  16;//充值补登
	
	public static final int BOSS_AUDIT_TYEP_DRAW_BD =  17;//提现补登
	
	public static final int BOSS_AUDIT_TYEP_CANCEL_USER =  18;//注销账户
	
	public static final int BOSS_AUDIT_TYEP_BIG_AWARD =  19;//大奖解冻
	
	public static final int BOSS_AUDIT_TYEP_POINT_ADJUST =  20;//积分调节
	
	
	public static final int BOSS_AUDIT_TYEP_COMMISSION =  21;//佣金
	
	public static final int BOSS_AUDIT_TYEP_DEPOSIT =  22;//保证金
	
	public static final int BOSS_AUDIT_TYEP_ACTIVE =  23;//活动
	
	//public static final int BOSS_AUDIT_TYEP_CJEX =  24;//彩金有效期
	
	//public static final int BOSS_AUDIT_TYEP_XJ =  25;//现金
	
	//public static final int BOSS_AUDIT_TYEP_CJ =  26;//彩金
	
	public static final int BOSS_AUDIT_TYEP_MERCHANTKH =  27;//商户开户
	
	public static final int BOSS_AUDIT_TYPE_HOLIDAY = 28; //节假日添加
	
	public static final int BOSS_AUDIT_TYPE_INITYEAR = 29; //年度初始化
	
	public static final int BOSS_AUDIT_TYPE_CLOSE = 30; //休市
	

	public static final Map<Integer,String> BOSS_AUDIT_NAME = new HashMap<Integer,String>();
	static{
		BOSS_AUDIT_NAME.put(BOSS_AUDIT_TYEP_DRAW, "提现审核流程");
		BOSS_AUDIT_NAME.put(BOSS_AUDIT_TYPE_INITYEAR, "期次年度初始化审核流程");
		BOSS_AUDIT_NAME.put(BOSS_AUDIT_TYPE_CLOSE, "休市审核流程");
		//BOSS_AUDIT_NAME.put(BOSS_AUDIT_TYEP_SUPBIG_AWARD, "超级大奖审核流程");
		BOSS_AUDIT_NAME.put(BOSS_AUDIT_TYEP_GAME_STATUS, "游戏状态变更审核流程");
		//BOSS_AUDIT_NAME.put(BOSS_AUDIT_TYEP_AWARD_NUM, "开奖号码审核流程");
		//BOSS_AUDIT_NAME.put(BOSS_AUDIT_TYEP_AWARD_LEVEL, "奖级信息审核流程");
		//BOSS_AUDIT_NAME.put(BOSS_AUDIT_TYEP_ISSUE_LEVEL_ADD, "期次奖级添加审核流程");
		//BOSS_AUDIT_NAME.put(BOSS_AUDIT_TYEP_ISSUE_LEVEL_UPDATE, "期次奖级修改审核流程");
		//BOSS_AUDIT_NAME.put(BOSS_AUDIT_TYEP_SALE_RULE_ADD, "销售计划添加审核流程");
		//BOSS_AUDIT_NAME.put(BOSS_AUDIT_TYEP_SALE_RULE_UPDATE, "销售计划修改审核流程");
		//BOSS_AUDIT_NAME.put(BOSS_AUDIT_TYEP_ABANDON_AWARD, "弃奖审核流程");
		BOSS_AUDIT_NAME.put(BOSS_AUDIT_TYEP_DEPOSIT_CZ, "充值冲正审核流程");
		BOSS_AUDIT_NAME.put(BOSS_AUDIT_TYEP_DRAW_CZ, "提现冲正审核流程");
		BOSS_AUDIT_NAME.put(BOSS_AUDIT_TYEP_DEPOSIT_BD, "充值补登审核流程");
		BOSS_AUDIT_NAME.put(BOSS_AUDIT_TYEP_DRAW_BD, "提现补登审核流程");
		BOSS_AUDIT_NAME.put(BOSS_AUDIT_TYEP_CANCEL_USER, "用户注销审核流程");
		//BOSS_AUDIT_NAME.put(BOSS_AUDIT_TYEP_BIG_AWARD, "大奖解冻流程");
		BOSS_AUDIT_NAME.put(BOSS_AUDIT_TYEP_POINT_ADJUST, "积分调整审核流程");
		BOSS_AUDIT_NAME.put(BOSS_AUDIT_TYEP_COMMISSION, "佣金设定流程");
		BOSS_AUDIT_NAME.put(BOSS_AUDIT_TYEP_DEPOSIT, "保证金设定审核流程");
		BOSS_AUDIT_NAME.put(BOSS_AUDIT_TYEP_ACTIVE, "活动添加审核流程");
		BOSS_AUDIT_NAME.put(BOSS_AUDIT_TYEP_ACCOUNTROOT, "主账户调节流程");
		BOSS_AUDIT_NAME.put(BOSS_AUDIT_TYEP_ACCOUNTSON, "子账户调节流程");
		//BOSS_AUDIT_NAME.put(BOSS_AUDIT_TYEP_CJ, "彩金调节审核流程");
		BOSS_AUDIT_NAME.put(BOSS_AUDIT_TYEP_MERCHANTKH, "商户开户审核流程");
		BOSS_AUDIT_NAME.put(BOSS_AUDIT_TYPE_HOLIDAY, "节假日添加审核流程");
	}
	
	
	public static final Map<Integer,String> BOSS_AUDIT_TYEP_MAP = new HashMap<Integer,String>();
	static{
		BOSS_AUDIT_TYEP_MAP.put(BOSS_AUDIT_TYEP_DRAW, BOSS_AUDIT_DRAW);
		BOSS_AUDIT_TYEP_MAP.put(BOSS_AUDIT_TYEP_ACCOUNTROOT, BOSS_AUDIT_XJ);
		BOSS_AUDIT_TYEP_MAP.put(BOSS_AUDIT_TYEP_ACCOUNTSON, BOSS_AUDIT_CJ);
		BOSS_AUDIT_TYEP_MAP.put(BOSS_AUDIT_TYPE_CLOSE, GAME_CLOSE);
		BOSS_AUDIT_TYEP_MAP.put(BOSS_AUDIT_TYEP_GAMEKU, BOSS_AUDIT_SUPERAWARD);
		BOSS_AUDIT_TYEP_MAP.put(BOSS_AUDIT_TYEP_SUPBIG_AWARD, BOSS_AUDIT_GAMESTATUS);
		BOSS_AUDIT_TYEP_MAP.put(BOSS_AUDIT_TYEP_AWARD_NUM, BOSS_AUDIT_AWARD_NUM);
		BOSS_AUDIT_TYEP_MAP.put(BOSS_AUDIT_TYEP_AWARD_LEVEL, BOSS_AUDIT_SALE_AWARD_LEVEL);
		BOSS_AUDIT_TYEP_MAP.put(BOSS_AUDIT_TYEP_ISSUE_LEVEL_ADD, BOSS_AUDIT_ISSUE_LEVEL_ADD);
		BOSS_AUDIT_TYEP_MAP.put(BOSS_AUDIT_TYEP_ISSUE_LEVEL_UPDATE, BOSS_AUDIT_ISSUE_LEVEL_UPDATE);
		BOSS_AUDIT_TYEP_MAP.put(BOSS_AUDIT_TYEP_SALE_RULE_ADD, BOSS_AUDIT_SALE_RULE_ADD);
		BOSS_AUDIT_TYEP_MAP.put(BOSS_AUDIT_TYEP_SALE_RULE_UPDATE, BOSS_AUDIT_SALE_RULE_UPDATE);
		BOSS_AUDIT_TYEP_MAP.put(BOSS_AUDIT_TYEP_GAME_STATUS, BOSS_AUDIT_GAMESTATUS);
		BOSS_AUDIT_TYEP_MAP.put(BOSS_AUDIT_TYEP_ABANDON_AWARD, BOSS_AUDIT_ABANDON_AWARD);
		BOSS_AUDIT_TYEP_MAP.put(BOSS_AUDIT_TYEP_DEPOSIT_CZ, BOSS_AUDIT_DEPOSIT_CZ);
		BOSS_AUDIT_TYEP_MAP.put(BOSS_AUDIT_TYEP_DRAW_CZ, BOSS_AUDIT_DRAW_CZ);
		BOSS_AUDIT_TYEP_MAP.put(BOSS_AUDIT_TYEP_DEPOSIT_BD, BOSS_AUDIT_DEPOSIT_BD);
		BOSS_AUDIT_TYEP_MAP.put(BOSS_AUDIT_TYEP_DRAW_BD, BOSS_AUDIT_DRAW_BD);
		BOSS_AUDIT_TYEP_MAP.put(BOSS_AUDIT_TYEP_CANCEL_USER, BOSS_AUDIT_CANCEL_USER);
		BOSS_AUDIT_TYEP_MAP.put(BOSS_AUDIT_TYEP_BIG_AWARD, BOSS_AUDIT_BIG_AWARD);
		BOSS_AUDIT_TYEP_MAP.put(BOSS_AUDIT_TYEP_POINT_ADJUST, BOSS_AUDIT_POINT_ADJUST);
		BOSS_AUDIT_TYEP_MAP.put(BOSS_AUDIT_TYEP_COMMISSION, BOSS_AUDIT_COMMISSION);
		BOSS_AUDIT_TYEP_MAP.put(BOSS_AUDIT_TYEP_DEPOSIT, BOSS_AUDIT_DEPOSIT);
		BOSS_AUDIT_TYEP_MAP.put(BOSS_AUDIT_TYEP_ACTIVE, BOSS_AUDIT_ACTIVE);
		BOSS_AUDIT_TYEP_MAP.put(BOSS_AUDIT_TYEP_MERCHANTKH, BOSS_AUDIT_MERCHANTKH);
		BOSS_AUDIT_TYEP_MAP.put(BOSS_AUDIT_TYPE_HOLIDAY, BOSS_AUDIT_HOLIDAY_ADD);
	}
	
	/**
	 * 游戏状态
	 * 0、暂停销售
	 * 1、正在销售
	 */
	public static final int GAME_STATUS_ZTXX = 0;
	public static final int GAME_STATUS_ZZXX = 1;
	
	public static final Map<Integer,String> GAME_STATUS_MAP = new HashMap<Integer,String>();
	static{
		GAME_STATUS_MAP.put(GAME_STATUS_ZTXX, "暂停销售");
		GAME_STATUS_MAP.put(GAME_STATUS_ZZXX, "正在销售");
	}
	
	/**
	 * boss审核状态 
	 */
	public static final int BOSS_AUDIT_TYEP_WATI =  0;//待审核
	public static final int BOSS_AUDIT_TYEP_SHZ =  1;//审核中
	public static final int BOSS_AUDIT_TYEP_SUCCESS =  2;//审核成功
	public static final int BOSS_AUDIT_TYEP_FAIL =  3;//审核驳回
	public static final int BOSS_AUDIT_TYEP_BACKFAIL =  4;//回调失败
	
	
	/**
	 * 工作流状态
	 * 0、暂停销售
	 * 1、正在销售
	 */
	public static final int ACTIVITI_STATUS_START = 1;
	public static final int ACTIVITI_STATUS_DRAW = 2;
	public static final int ACTIVITI_STATUS_DEPLAY = 3;
	public static final int ACTIVITI_STATUS_CONFIGURE = 4;
	public static final int ACTIVITI_STATUS_END = 5;
	
	public static final Map<Integer,String> ACTIVITI_STATUS_MAP = new HashMap<Integer,String>();
	static{
		ACTIVITI_STATUS_MAP.put(ACTIVITI_STATUS_START, "添加");
		ACTIVITI_STATUS_MAP.put(ACTIVITI_STATUS_DRAW, "绘制");
		ACTIVITI_STATUS_MAP.put(ACTIVITI_STATUS_DEPLAY, "部署");
		ACTIVITI_STATUS_MAP.put(ACTIVITI_STATUS_CONFIGURE, "配置");
		ACTIVITI_STATUS_MAP.put(ACTIVITI_STATUS_END, "完成");
	}
	
}
