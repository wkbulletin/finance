package com.huacai.util;

import java.util.Calendar;
import java.util.Date;

/**
 * @description
 * @author shijf
 * @date 2012-12-16 下午02:12:54
 * @version 1.0
 */
public class CalendarUtil {

	public static int getWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int week = cal.get(Calendar.DAY_OF_WEEK)-1;
		//西方第一天从周日开始，把周日设置为7
		if(week == 0) {
			week = 7;
		}
		return week;
	}
	
	public static Date addDateByDate(Date date,int value) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE,value);
		return cal.getTime();
	}
}