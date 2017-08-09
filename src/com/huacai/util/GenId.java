package com.huacai.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class GenId {

	private static String appId = "10";
	
	
	private static final AtomicInteger AI = new AtomicInteger(1);
	
	/**
	 * 为选中的号码产生系统交易ID return Long 一个ID对象
	 */
	public synchronized static String genId() {
		Integer i = AI.getAndIncrement();
		if(i >= 9999) {
			AI.set(1);
		}
		DateFormat df = new SimpleDateFormat("yyMMddHHmmss");
		String date = df.format(new Date());
		String strOrderId =  date + fillPreZero(i) + appId;
		return strOrderId;
	}
	
	private static String fillPreZero(int i) {
		String a = "0000" + i;
		return a.substring(a.length() - 4);
	}

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		
		fillPreZero(3);
		System.out.println(System.currentTimeMillis() - start);
	}

}
