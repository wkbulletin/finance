package com.huacai.web.constant;

import java.util.HashMap;

public class Playinfo {

	// *******************双色球******************************/
	/**
	 * 双色球:SSQ单式
	 */
	public static final int SSQZXDS = 101001;// SSQ单式
	/**
	 * 双色球:SSQ复式
	 */
	public static final int SSQZXFS = 101002; // SSQ复式
	/**
	 * 双色球:SSQ胆拖
	 */
	public static final int SSQZXDT = 101003; // SSQ胆拖

	// *******************七乐彩******************************/
	/**
	 * 七乐彩:QLC单式
	 */
	public static final int QLCZXDS = 102001;// QLC单式
	/**
	 * 七乐彩:QLC复式
	 */
	public static final int QLCZXFS = 102002;// QLC复式
	/**
	 * 七乐彩:QLC胆拖
	 */
	public static final int QLCDT = 102003;// QLC胆拖

	// *******************3D******************************/
	/**
	 * 3D直选单式
	 */
	public static final int SDZXDS = 103001;// 3D直选单式
	/**
	 * 3D直选复式
	 */
	public static final int SDZXFS = 103002;// 3D直选复式
	/**
	 * 3D直选和值
	 */
	public static final int SDZXHZ = 103004;// 3D直选和值
	/**
	 * 3D直选包号
	 */
	public static final int SDZXBH = 103005;// 3D直选包号
	/**
	 * 3D组三单式
	 */
	public static final int SDZ3DS = 103006;// 3D组三单式
	/**
	 * 3D组三复式 
	 */
	public static final int SDZ3FS = 103007;// 3D组三复式
	/**
	 * 3D组三和值
	 */
	public static final int SDZ3HZ = 103008;// 3D组三和值
	/**
	 * 3D组三包号
	 */
	public static final int SDZ3BH = 103009;// 3D组三包号
	/**
	 * 3D组六单式
	 */
	public static final int SDZ6DS = 103010;// 3D组六单式
	/**
	 * 3D组六复式
	 */
	public static final int SDZ6FS = 103011;// 3D组六复式
	/**
	 * 3D组六和值
	 */
	public static final int SDZ6HZ = 103012;// 3D组六和值
	/**
	 * 3D组六包号
	 */
	public static final int SDZ6BH = 103013;// 3D组六包号
	
	// ******************重庆时时彩******************************/
	
	// 五星通选(单式)
	/**
	 * 时时彩：五星通选[五星通选](注数：n*n*n*n*n)
	 */
	public static final int TSSC_5X_TONGXUAN = 145001; // 五星通选[五星通选](注数：n*n*n*n*n)

	// 五星(单式、上传||复式)
	/**
	 * 时时彩:五星单式[单选五星](注数：1*1*1*1*1)
	 */
	public static final int TSSC_5X_DS = 145002; // 五星单式[单选五星](注数：1*1*1*1*1)
	/**
	 * 时时彩:五星组合[组合五星](注数：n*n*n*n*n)
	 */
	public static final int TSSC_5X_ZH = 145003; // 五星组合[组合五星](注数：n*n*n*n*n)
	/**
	 * 时时彩:五星复式[复选五星](当选号为多注时做拆分处理)(注数：n*n*n*n*n*4)
	 */
	public static final int TSSC_5X_FS = 145004; // 五星复式[复选五星](当选号为多注时做拆分处理)(注数：n*n*n*n*n*4)

	// 三星直选(单式、和值、上传||复式)
	/**
	 * 三星单式[单选三星](注数：1*1*1)
	 */
	public static final int TSSC_3X_ZHIXUAN_DS = 145005; // 三星单式[单选三星](注数：1*1*1)
	/**
	 * 三星组合[组合三星](注数：n*n*n)
	 */
	public static final int TSSC_3X_ZHIXUAN_ZH = 145006; // 三星组合[组合三星](注数：n*n*n)
	/**
	 * 三星复式[复选三星](注数：1*1*1*3)
	 */
	public static final int TSSC_3X_ZHIXUAN_FS = 145007; // 三星复式[复选三星](注数：1*1*1*3)
	/**
	 * 三星组合复式[三星直选组合复式](注数：n*n*n*3)
	 */
	public static final int TSSC_3X_ZHIXUAN_ZHFS = 145008; // 三星组合复式[三星直选组合复式](注数：n*n*n*3)
	/**
	 * 三星和值[三星包点](显示和值时不显示复式选择框)
	 */
	public static final int TSSC_3X_ZHIXUAN_HZ = 145009; // 三星和值[三星包点](显示和值时不显示复式选择框)

	// 三星组选(组选三、组选六、和值、包胆)
	/**
	 * 三星组三复式[三星组三复式]
	 */
	public static final int TSSC_3X_ZUXUAN_3 = 145010; // 三星组三复式[三星组三复式]
	/**
	 * 三星组六复式[三星组六复式]
	 */
	public static final int TSSC_3X_ZUXUAN_6 = 145011; // 三星组六复式[三星组六复式]
	/**
	 * 三星组选和值[三星组选包点]
	 */
	public static final int TSSC_3X_ZUXUAN_HZ = 145012; // 三星组选和值[三星组选包点]
	/**
	 * 三星组选包胆[三星组选包胆]
	 */
	public static final int TSSC_3X_ZUXUAN_BD = 145013; // 三星组选包胆[三星组选包胆]

	// 二星直选(单式、和值、上传||复式)
	/**
	 * 二星单式[单选二星]
	 */
	public static final int TSSC_2X_ZHIXUAN_DS = 145014; // 二星单式[单选二星]
	/**
	 * 二星组合[组合二星]
	 */
	public static final int TSSC_2X_ZHIXUAN_ZH = 145015; // 二星复式[组合二星]
	/**
	 * 二星复式[复选一星](当选号为多注时做拆分处理)
	 */
	public static final int TSSC_2X_ZHIXUAN_FS = 145016; // 二星组合[复选一星](当选号为多注时做拆分处理)
	/**
	 * 二星和值[两星包点]
	 */
	public static final int TSSC_2X_ZHIXUAN_HZ = 145017; // 二星和值[两星包点]

	// 二星组选(单式、复式、和值、包胆)
	/**
	 * 二星组选单式[二星组选单式]
	 */
	public static final int TSSC_2X_ZUXUAN_DS = 145018; // 二星组选单式[二星组选单式]
	/**
	 * 二星组选组合[二星组选复式]
	 */
	public static final int TSSC_2X_ZUXUAN_ZH = 145019; // 二星组选组合[二星组选复式]
	/**
	 * 二星组选分组[二星组选分组]
	 */
	public static final int TSSC_2X_ZUXUAN_FS = 145020; // 二星组选分组[二星组选分组]
	/**
	 * 二星组选和值[二星组选和值]
	 */
	public static final int TSSC_2X_ZUXUAN_HZ = 145021; // 二星组选和值[二星组选和值]
	/**
	 * 二星组选包胆[二星组选包胆]
	 */
	public static final int TSSC_2X_ZUXUAN_BD = 145022; // 二星组选包胆[二星组选包胆]

	// 一星(单式、上传)
	/**
	 * 一星单式[单选一星]
	 */
	public static final int TSSC_1X_DS = 145023; // 一星单式[单选一星]

	// 大小单双(单式)
	/**
	 * 大小单双[猜大小单双]
	 */
	public static final int TSSC_DXDS = 145024; // 大小单双[猜大小单双]
	/**
	 * 三星组三单式
	 */
	public static final int TSSC_Z3_DS = 145025; // 三星组三单式
	/**
	 * 三星组六单式
	 */
	public static final int TSSC_Z6_DS = 145026; // 三星组六单式
	
	/**
	 * 时时彩：五星通选复式(拆注后145001)
	 */
	public static final int TSSC_5X_TONGXUAN_FS = 145101; // 五星通选[五星通选](注数：n*n*n*n*n)
	/**
	 * 时时彩：五星组合复式(145004)
	 */
	public static final int TSSC_5X_FS_FS = 145104;//五星组合复式(145004)
	/**
	 * 时时彩：三星组合复式(145007)
	 */
	public static final int TSSC_3X_ZHIXUAN_FS_FS = 145107;//三星组合复式(145007)
	/**
	 * 时时彩：二星组合复式(145016)
	 */
	public static final int TSSC_2X_ZHIXUAN_FS_FS = 145116;//二星组合复式(145016)

	public static HashMap<Integer, String> PlayInfoMap = new HashMap<Integer, String>();
	static {

		PlayInfoMap.put(SSQZXDS, "单式");
		PlayInfoMap.put(SSQZXFS, "复式");
		PlayInfoMap.put(SSQZXDT, "胆拖");
		
		PlayInfoMap.put(QLCZXDS, "单式");
		PlayInfoMap.put(QLCZXFS, "复式");
		PlayInfoMap.put(QLCDT, "胆拖");
		
		PlayInfoMap.put(SDZXDS, "直选单式");
		PlayInfoMap.put(SDZXFS, "直选复式");
		PlayInfoMap.put(SDZXHZ, "直选和值");
		PlayInfoMap.put(SDZXBH, "直选包号");
		
		PlayInfoMap.put(SDZ3DS, "组三单式");
		PlayInfoMap.put(SDZ3FS, "组三复式");
		PlayInfoMap.put(SDZ3HZ, "组三和值");
		PlayInfoMap.put(SDZ3BH, "组三包号");
		
		PlayInfoMap.put(SDZ6DS, "组六单式");
		PlayInfoMap.put(SDZ6FS, "组六复式");
		PlayInfoMap.put(SDZ6HZ, "组六和值");
		PlayInfoMap.put(SDZ6BH, "组六包号");
		
		PlayInfoMap.put(TSSC_5X_TONGXUAN, "五星通选");// [五星通选](注数：n*n*n*n*n)
		PlayInfoMap.put(TSSC_5X_DS, "五星单式");// [单选五星](注数：1*1*1*1*1)
		PlayInfoMap.put(TSSC_5X_ZH, "五星复式");// [组合五星](注数：n*n*n*n*n)
		PlayInfoMap.put(TSSC_5X_FS, "五星组合");// [复选五星](当选号为多注时做拆分处理)(注数：n*n*n*n*n*4)
		PlayInfoMap.put(TSSC_3X_ZHIXUAN_DS, "三星单式");// [单选三星](注数：1*1*1)
		PlayInfoMap.put(TSSC_3X_ZHIXUAN_ZH, "三星复式");// [组合三星](注数：n*n*n)
		PlayInfoMap.put(TSSC_3X_ZHIXUAN_FS, "三星组合");// [复选三星](注数：1*1*1*3)
		PlayInfoMap.put(TSSC_3X_ZHIXUAN_ZHFS, "三星组合复式");// [三星直选组合复式](注数：n*n*n*3)
		PlayInfoMap.put(TSSC_3X_ZHIXUAN_HZ, "三星和值");// [三星包点](显示和值时不显示复式选择框)
		PlayInfoMap.put(TSSC_3X_ZUXUAN_3, "三星组三复式");// [三星组三复式]
		PlayInfoMap.put(TSSC_3X_ZUXUAN_6, "三星组六复式");// [三星组六复式]
		PlayInfoMap.put(TSSC_3X_ZUXUAN_HZ, "三星组选和值");// [三星组选包点]
		PlayInfoMap.put(TSSC_3X_ZUXUAN_BD, "三星组选包胆");// [三星组选包胆]
		PlayInfoMap.put(TSSC_2X_ZHIXUAN_DS, "二星单式");// [单选二星]
		PlayInfoMap.put(TSSC_2X_ZHIXUAN_ZH, "二星复式");// [组合二星]
		PlayInfoMap.put(TSSC_2X_ZHIXUAN_FS, "二星组合");// [复选一星](当选号为多注时做拆分处理)
		PlayInfoMap.put(TSSC_2X_ZHIXUAN_HZ, "二星和值");// [两星包点]
		PlayInfoMap.put(TSSC_2X_ZUXUAN_DS, "二星组选单式");// [二星组选单式]
		PlayInfoMap.put(TSSC_2X_ZUXUAN_ZH, "二星组选组合");// [二星组选复式]
		PlayInfoMap.put(TSSC_2X_ZUXUAN_FS, "二星组选分组");// [二星组选分组]
		PlayInfoMap.put(TSSC_2X_ZUXUAN_HZ, "二星组选和值");// [二星组选和值]
		PlayInfoMap.put(TSSC_2X_ZUXUAN_BD, "二星组选包胆");// [二星组选包胆]
		PlayInfoMap.put(TSSC_1X_DS, "一星单式");// 一星单式[单选一星]
		PlayInfoMap.put(TSSC_DXDS, "大小单双");// 大小单双[猜大小单双]
		PlayInfoMap.put(TSSC_Z3_DS, "三星组三单式");// [猜大小单双]
		PlayInfoMap.put(TSSC_Z6_DS, "三星组六单式");// [猜大小单双]
		PlayInfoMap.put(TSSC_5X_TONGXUAN_FS, "五星通选复式");// 五星通选复式(拆注后145001)
		PlayInfoMap.put(TSSC_5X_FS_FS, "五星组合复式");// 五星组合复式(145004)
		PlayInfoMap.put(TSSC_3X_ZHIXUAN_FS_FS, "三星组合复式");// 三星组合复式(145007)
		PlayInfoMap.put(TSSC_2X_ZHIXUAN_FS_FS, "二星组合复式");// 二星组合复式(145016)
	}

}
