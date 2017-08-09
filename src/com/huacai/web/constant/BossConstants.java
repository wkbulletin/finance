package com.huacai.web.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class BossConstants {
	
	
	public static final TreeMap<Integer, String> SYSTEM_LEVEL;
	static {
		SYSTEM_LEVEL = new TreeMap<Integer, String>();
		
		SYSTEM_LEVEL.put(1, "全国中心");
		SYSTEM_LEVEL.put(2, "省中心");
		SYSTEM_LEVEL.put(3, "市、县中心");
		SYSTEM_LEVEL.put(4, "网点");
		
	}
	
	public static List<Map<String,String>> getSystemLevels() {
		Iterator<Integer> iter = SYSTEM_LEVEL.keySet().iterator();
		Integer key;
		String val;
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Map<String,String> row;
		while (iter.hasNext()) {
			key = iter.next();
			val = SYSTEM_LEVEL.get(key);
			row = new HashMap<String, String>();
			row.put("key", key.toString());
			row.put("val", val);
			list.add(row);
		}
		return list;
	}
	
	
	/**
	 * 状态：可用
	 */
	public static final int BOSS_STATUS_OK = 1;
	
	/**
	 * 状态：不可用
	 */
	public static final int BOSS_STATUS_DEL = 0;
	
	/**
	 * 游戏编号：双色球
	 */
	public static final int BOSS_GAME_Double = 101;
	
	/**
	 * 游戏编号：3D
	 */
	public static final int BOSS_GAME_3D = 103;
	
	/**
	 * 游戏编号：7乐彩
	 */
	public static final int BOSS_GAME_7L = 102;
	
	/**
	 * 游戏编号：快乐8
	 */
	public static final int BOSS_GAME_8HAPPY = 161;
	
	/**
	 * 游戏编号：PK是拾
	 */
	public static final int BOSS_GAME_PK = 162;
	
	/**
	 * 游戏编号：快三
	 */
	public static final int BOSS_GAME_H3 = 163;
	
	/**
	 * 促销类型：奖等设置
	 */
	public static final int BOSS_PROMOT_PRIZE = 1;
	
	/**
	 * 促销类型：促销信息
	 */
	public static final int BOSS_PROMOT_PROMOT = 2;
	
	/**
	 * 促销类型：促销信息类型-双色球
	 */
	public static final int BOSS_PROMOT_PROMOTDOUBLE = 1;
	
	/**
	 * 促销类型：促销信息类型-3D
	 */
	public static final int BOSS_PROMOT_PROMOT3D = 2;
	
	/**
	 * 促销类型：促销信息-七乐彩
	 */
	public static final int BOSS_PROMOT_PROMOT7L = 3;
	
	/**
	 * 促销类型：促销信息-快乐8
	 */
	public static final int BOSS_PROMOT_PROMOT8HAPPY = 4;
	
	/**
	 * 促销类型：促销信息-PK拾
	 */
	public static final int BOSS_PROMOT_PROMOTPK = 5;
	
	/**
	 * 促销类型：促销信息-双色球幸运派奖
	 */
	public static final int BOSS_PROMOT_PROMOTDL = 6;
	
	/**
	 * 促销类型：促销信息-快三派奖
	 */
	public static final int BOSS_PROMOT_PROMOTH3 = 7;
	
	/**
	 * 促销信息状态-未开始
	 */
	public static final int BOSS_PROMOT_UN = 1;
	
	/**
	 * 促销信息状态-进行中
	 */
	public static final int BOSS_PROMOT_IN = 2;
	
	/**
	 * 促销信息状态-已结束
	 */
	public static final int BOSS_PROMOT_END = 3;
	
	/**
	 * 渠道游戏授权-支持预接单
	 */
	public static final int BOSS_AGENT_GAMEPRE = 1;
	
	/**
	 * 渠道游戏授权- 不支持预接单
	 */
	public static final int BOSS_AGENT_GAMEUNPRE = 0;
	
	/**
	 * 渠道默认初始密码
	 */
	public static final String BOSS_AGENT_PASS = "123456";
	
	/**
	 * 渠道默认初始充值密码
	 */
	public static final String BOSS_AGENT_CPWD = "ff92a240d11b05ebd392348c35f781b2";
	
	/**
	 * 渠道默认初始提款密码
	 */
	public static final String BOSS_AGENT_TPWD = "ff92a240d11b05ebd392348c35f781b2";
	
	/**
	 * 渠道流水类型-充值帐户充值
	 */
	public static final int BOSS_BILLTYPE_DEPOSIT = 103;
	
	/**
	 * 渠道流水类型-促销帐户充值
	 */
	public static final int BOSS_BILLTYPE_PROMOT = 104;
	
	/**
	 * 渠道流水类型-帐户提现
	 */
	public static final int BOSS_BILLTYPE_PRIZE = 105;
	
	/**
	 * 渠道默认初始密码
	 */
	public static final String BOSS_INFO_DEPOSIT = "充值账户充值";
	
	/**
	 * 渠道默认初始密码
	 */
	public static final String BOSS_INFO_PROMOT = "促销账户充值";
	
	/**
	 * 渠道默认初始密码
	 */
	public static final String BOSS_INFO_PRIZE = "奖金账户提现";
	
	//彩民查询类型标识
	/**
	 * 投注账号
	 */
	public static final int BETTCCOUNT_TYPE=0;//投注账号
	
	/**
	 * 身份证号
	 */
	public static final int IDCARD_TYPE=1;    //身份证号
	
	/**
	 * 手机号
	 */
	public static final int PHONE_TYPE=2;     //手机号
	
	/**
	 * 银行/直扣账号
	 */
	public static final int BANKCARD_TYPE=3;  //银行/直扣账号
	
	/**
	 * 外部编号
	 */
	public static final int OUTSIDE_TYPE=4;
	
	/**
	 * 系统级别
	 */
	public static final int SYS_LEVEL=1;
}
