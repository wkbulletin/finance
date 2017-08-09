package com.huacai.util;

import java.math.BigDecimal;

/**
 * @description 转换金额工具
 * @author shijf
 * @date 2013-1-31 下午02:59:07
 * @version 1.0
 */
public class ConvertMoneyUtil {

	/**
	 * 把元转成分
	 * @param yuan
	 * @return
	 */
	public static long yun2Fen(Object yuan) {
		long units = 100;
		long money = 0;
		if(yuan instanceof Integer) {
			money = Integer.parseInt(yuan+"")*units;
		}
		else if(yuan instanceof Long) {
			money = Long.parseLong(yuan+"")*units;
		}
		else if(yuan instanceof BigDecimal) {
			money = new BigDecimal(yuan+"").longValue()*units;
		}
		else if(yuan instanceof String) {
			money = Long.parseLong(yuan+"")*units;
		}
		return money;
	}
	
	/**
	 * 把分转成元
	 * @param fen
	 * @return
	 */
	public static long fen2Yuan(Object fen) {
		long units = 100;
		long money = 0;
		if(fen instanceof Integer) {
			money = Integer.parseInt(fen+"")/units;
		}
		else if(fen instanceof Long) {
			money = Long.parseLong(fen+"")/units;
		}
		else if(fen instanceof BigDecimal) {
			money = new BigDecimal(fen+"").longValue()/units;
		}
		else if(fen instanceof String) {
			money = Long.parseLong(fen+"")/units;
		}
		return money;
	}
}
