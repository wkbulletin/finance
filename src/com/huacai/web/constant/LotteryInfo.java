package com.huacai.web.constant;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class LotteryInfo {
	// 彩种定义
	public static final int LOT_SSQ = 101;// 双色球

	public static final int LOT_QLC = 102;// 七乐彩

	public static final int LOT_3D = 103;// 福彩3D

	public static final int LOT_SSL = 104;// 时时乐

	public static final int LOT_TTC4 = 105;// 天天彩4D

	public static final int LOT_SWXW = 106;// 15选5

	public static final int LOT_DF61 = 107;// 东方6+1

	public static final int LOT_KLC = 108;// 开乐彩

	public static final int LOT_JSK3 = 141;// 江苏快三

	public static final int LOT_GD11X5 = 142;// 广东11选5

	public static final int LOT_JX11X5 = 143;// 江西11选5

	public static final int LOT_AHK3 = 144;// 安徽快三

	public static final int LOT_SSC = 145;// 时时彩

	/**
	 * 体彩
	 */
	public static final int LOT_PL3 = 146;// 排列三
	public static final int LOT_PL5 = 147;// 排列五
	public static final int LOT_QXC = 148;// 七星彩
	public static final int LOT_CJDLT = 149;// 超级大乐透

	public static final int LOT_11YDJ = 150;// 十一运夺金

	/**
	 * 足彩
	 */
	public static final int LOT_SFC = 151;// 胜负彩(14场)
	public static final int LOT_RJC = 152;// 任九场
	public static final int LOT_BQC = 153;// 6场半全场
	public static final int LOT_JQC = 154;// 4场进球彩

	public static final int LOT_JXSSC = 155;// 江西时时彩

	public static final int LOT_TSWXW = 156;// 天津15选5

	public static final int LOT_JLC = 157; // 157 即乐彩

	public static final int LOT_DLC = 158; // 158 多乐彩

	public static final int LOT_KL123 = 159; // 159 快乐123

	public static final int LOT_KLSF = 160; // 160 快乐十分 (幸运农场)

	public static final int LOT_KL8 = 161; // 161快乐8

	public static final int LOT_PK10 = 162; // PK拾

	public static final int LOT_TJSSC = 163; // 天津时时彩

	/**
	 * 竞彩足球
	 */
	public static final int LOT_JCZQ_SPF = 164; // 竞彩足球让球胜平负
	public static final int LOT_JCZQ_ZJQ = 165; // 竞彩足球总进球
	public static final int LOT_JCZQ_BF = 166; // 竞彩足球比分
	public static final int LOT_JCZQ_BQC = 167; // 竞彩足球半全场

	/**
	 * 竞彩篮球
	 */
	public static final int LOT_JCLQ_SF = 168; // 竞彩篮球胜负
	public static final int LOT_JCLQ_RFSF = 169; // 竞彩篮球让分胜负
	public static final int LOT_JCLQ_SFC = 170; // 竞彩篮球胜分差
	public static final int LOT_JCLQ_DXF = 171; // 竞彩篮球大小分

	/**
	 * 竞彩新增彩种
	 */
	public static final int LOT_JCZQ_BRQSPF = 172; // 竞彩足球胜平负
	public static final int LOT_JCZQ_HHGG = 173; // 竞彩足球混合过关
	public static final int LOT_JCLQ_HHGG = 174; // 竞彩篮球混合过关

	public static final int LOT_ZQ_GYJ = 175; // 竞彩足球冠亚军
	public static final int LOT_LQ_GYJ = 176; // 竞彩篮球冠亚军

	public static final Map<Integer, String> LOT_INFO = new HashMap<Integer, String>();
	public static final JSONArray LOT_LIST = new JSONArray();

	static {
		LOT_INFO.put(LOT_SSQ, "双色球");
		LOT_INFO.put(LOT_QLC, "七乐彩");
		LOT_INFO.put(LOT_3D, "福彩3D");
		LOT_INFO.put(LOT_SSL, "时时乐");
		LOT_INFO.put(LOT_TTC4, "天天彩选4");
		LOT_INFO.put(LOT_SWXW, "15选5");
		LOT_INFO.put(LOT_DF61, "东方6+1");
		LOT_INFO.put(LOT_KLC, "开乐彩");

		LOT_INFO.put(LOT_JSK3, "江苏快三");
		LOT_INFO.put(LOT_GD11X5, "广东11选5");
		LOT_INFO.put(LOT_JX11X5, "江西11选5");
		LOT_INFO.put(LOT_AHK3, "安徽快三");
		LOT_INFO.put(LOT_SSC, "重庆时时彩");
		LOT_INFO.put(LOT_PL3, "排列3");
		LOT_INFO.put(LOT_PL5, "排列5");
		LOT_INFO.put(LOT_QXC, "七星彩");
		LOT_INFO.put(LOT_CJDLT, "超级大乐透");
		LOT_INFO.put(LOT_11YDJ, "11运夺金");
		LOT_INFO.put(LOT_SFC, "胜负彩(14场)");
		LOT_INFO.put(LOT_RJC, "任九场");
		LOT_INFO.put(LOT_BQC, "6场半全场");
		LOT_INFO.put(LOT_JQC, "4场进球彩");
		LOT_INFO.put(LOT_JXSSC, "江西时时彩");
		LOT_INFO.put(LOT_TSWXW, "天津15选5");
		LOT_INFO.put(LOT_JLC, " 即乐彩");
		LOT_INFO.put(LOT_DLC, "多乐彩");
		LOT_INFO.put(LOT_KL123, "快乐123");
		LOT_INFO.put(LOT_KLSF, "快乐十分");
		LOT_INFO.put(LOT_KL8, "快乐8");
		LOT_INFO.put(LOT_PK10, "PK拾");
		LOT_INFO.put(LOT_TJSSC, "天津时时彩");

		LOT_INFO.put(LOT_JCZQ_SPF, "竞足让球胜平负");
		LOT_INFO.put(LOT_JCZQ_ZJQ, "竞足总进球");
		LOT_INFO.put(LOT_JCZQ_BF, "竞足比分");
		LOT_INFO.put(LOT_JCZQ_BQC, "竞足半全场");

		LOT_INFO.put(LOT_JCLQ_SF, "竞篮胜负");
		LOT_INFO.put(LOT_JCLQ_RFSF, "竞篮让分胜负");
		LOT_INFO.put(LOT_JCLQ_SFC, "竞篮胜分差");
		LOT_INFO.put(LOT_JCLQ_DXF, "竞篮大小分");

		LOT_INFO.put(LOT_JCZQ_BRQSPF, "竞足胜平负");
		LOT_INFO.put(LOT_JCZQ_HHGG, "竞足混合过关");
		LOT_INFO.put(LOT_JCLQ_HHGG, "竞篮混合过关");

		LOT_INFO.put(LOT_ZQ_GYJ, "竞足冠亚军");
		LOT_INFO.put(LOT_LQ_GYJ, "竞篮冠亚军");

		Iterator<Entry<Integer, String>> it = LOT_INFO.entrySet().iterator();
		Integer key;
		String value;
		JSONObject row = new JSONObject();
		while (it.hasNext()) {
			Entry<Integer, String> entry = (Entry<Integer, String>) it.next();
			key = entry.getKey();
			value = entry.getValue();
			row.put("lotId", key);
			row.put("lotName", value);
			LOT_LIST.add(row);
		}
	}

	/**
	 * 转换彩种名
	 * 
	 * @param lotId
	 * @return
	 */
	public static String getLotName(int lotId) {
		return LOT_INFO.get(lotId);
	}

	/**
	 * 获取彩种列表
	 * 
	 * @param lotId
	 * @return
	 */
	public static JSONArray getLotList() {
		return LOT_LIST;
	}
}
