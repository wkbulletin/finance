package com.huacai.util;

import java.util.concurrent.atomic.AtomicInteger;

public class GenMerchantTjId {

	private static String appId = "19";//商户推荐码首字符
	
	
	private static final AtomicInteger AI = new AtomicInteger(1);
	
	/**
	 * 为选中的号码产生系统交易ID return Long 一个ID对象
	 */
	public synchronized static String genId() {
		Integer i = AI.getAndIncrement();
		if(i >= 9999) {
			AI.set(1);
		}
		String strOrderId =  appId +fillPreZero(i) ;
		return strOrderId;
	}
	
	private static String fillPreZero(int i) {
		String a = "0000" + i;
		return a.substring(a.length() - 4);
	}

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		
		 for(int i=0; i< 10 ; i++){
			 System.out.println(genId());
		 }
	}

}
