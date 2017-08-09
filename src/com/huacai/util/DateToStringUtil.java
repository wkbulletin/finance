package com.huacai.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateToStringUtil {
	 private static DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
	 private static DateFormat dateFormatWithTime=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	 public static String getDateString(Date date){
		 if(date==null){
			 return "";
		 }
		 return dateFormat.format(date);
	 }
	 public static String getDateTimeString(Date date){
		 if(date==null){
			 return "";
		 }
		 return dateFormatWithTime.format(date);
	 }
}
