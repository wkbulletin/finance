package com.huacai.util;

/*

 G 年代标志符
 y 年
 M 月
 d 日
 h 时 在上午或下午 (1~12)
 H 时 在一天中 (0~23)
 m 分
 s 秒
 S 毫秒
 E 星期
 D 一年中的第几天
 F 一月中第几个星期几
 w 一年中第几个星期
 W 一月中第几个星期
 a 上午 / 下午 标记符
 k 时 在一天中 (1~24)
 K 时 在上午或下午 (0~11)
 z 时区

 SimpleDateFormat myFmt=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
 SimpleDateFormat myFmt1=new SimpleDateFormat("yy/MM/dd HH:mm");
 SimpleDateFormat myFmt2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//等价于now.toLocaleString()
 SimpleDateFormat myFmt3=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒 E ");
 SimpleDateFormat myFmt4=new SimpleDateFormat("一年中的第 D 天 一年中第w个星期 一月中第W个星期 在一天中k时 z时区");
 效果：
 2004年12月16日 17时24分27秒
 04/12/16 17:24
 2004-12-16 17:24:27
 2004年12月16日 17时24分27秒 星期四
 一年中的第 351 天 一年中第51个星期 一月中第3个星期 在一天中17时 CST时区
 16 Dec 2004 09:24:27 GMT
 2004-12-16 17:24:27
 Thu Dec 16 17:24:27 CST 2004



 */
/**
 * @author Administrator
 * 
 * 
 * 功能说明：日期相关工具函数
 */
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import libcore.util.StringUtil;
import libcore.util.VarUtil;

import org.apache.log4j.Logger;

public class DateUtils {
	public static final Logger logger = Logger.getLogger(DateUtil.class);

	// private static String defaultDatePattern = "yyyyMMdd HH:mm:ss";
	// private static String timePattern = "HH:mm";

	public static String today(String format) {
		DateFormat df = new SimpleDateFormat(format);
		return df.format(new java.util.Date());
	}
	
	public static String today() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(new java.util.Date());
	}

	public static long todayTime() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return getTime(df.format(new java.util.Date()));
	}
	
	/**
	 * 字符型时间变成时间类型
	 * 
	 * @param date
	 *                字符型时间 例如: "2008-08-08"
	 * @param format
	 *                格式化形式 例如: "yyyy-MM-dd"
	 * @return 出现异常时返回null
	 */
	public static Date String2Date(String date, String format) {
		DateFormat df = new SimpleDateFormat(format);
		Date d = null;
		try {
			d = df.parse(date);
		} catch (ParseException e) {
			logger.error("", e);
		}
		return d;
	}

	/**
	 * 日期字符串转换为另一格式字符串
	 * 
	 */
	public static String date2date(String date, String format) {
		return date(format, time(date));
	}

	/**
	 * 日期的指定域加减
	 * 
	 * @param time
	 *                时间戳(长整形字符串)
	 * @param field
	 *                加减的域,如date表示对天进行操作,month表示对月进行操作,其它表示对年进行操作
	 * @param num
	 *                加减的数值
	 * @return 操作后的时间戳(长整形字符串)
	 */
	public static long addDate(long time, String field, int num) {
		int fieldNum = Calendar.YEAR;
		if (field.equals("month")) {
			fieldNum = Calendar.MONTH;
		}
		if (field.equals("date")) {
			fieldNum = Calendar.DATE;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		cal.add(fieldNum, num);
		return cal.getTimeInMillis();
	}

	/**
	 * 日期的指定域加减(对天操作)
	 * 
	 * @param time
	 *                时间戳(长整形字符串)
	 * @param num
	 *                加减的数值
	 * @return 操作后的时间戳(长整形字符串)
	 */
	public static long addDate(long time, int num) {
		return addDate(time, "date", num);
	}

	/**
	 * 获取当前Unix时间（秒）
	 * 
	 * @return long
	 */
	public static long getUnixTime() {
		return new Date().getTime() / 1000;
	}

	/**
	 * 获取当前时间（毫秒）
	 * 
	 * @return long
	 */
	public static long getTime() {
		return new Date().getTime();
	}

	/**
	 * 获取指定日期时间（毫秒）
	 * 
	 * @return long
	 */
	public static long getTime(String date) {
		return time(date, "") * 1000;
	}

	/**
	 * 获取指定日期时间（毫秒）
	 * 
	 * @return long
	 */
	public static long getTime(String date, String format) {
		return time(date, format) * 1000;
	}

	/**
	 * 获取当前时间（秒）
	 * 
	 * @return long
	 */
	public static long time() {
		return new Date().getTime() / 1000;
	}

	/**
	 * 获取指定日期时间（秒）
	 * 
	 * @return long
	 */
	public static long time(String date) {
		return time(date, "");
	}

	/**
	 * 得到日期的时间戳(秒),自动根据长度判断格式 示范： 2010-1-10; 2010-01-10; 2010-1-1;
	 * 2010-1-01; 2010-1-10 12:12:10; 2010-1-1 1:1:1; ......
	 * 
	 * @param date
	 *                日期，格式：yyyy-MM-dd或yyyy-MM-dd HH:mm:ss
	 * @return 时间戳
	 */
	public static long time(String date, String format) {
		long time = 0;
		if (date == null || date.trim().length() < 6) {
			return 0;
		}
		date = date.trim();

		if (format.length() == 0) {
			try {
				String arr[] = date.split(" ");
				String arr2[] = arr[0].split("-");
				String arr3[] = null;

				format += StringUtil.strPad("y", arr2[0].length(), "y");
				format += "-";
				format += StringUtil.strPad("M", arr2[1].length(), "M");
				format += "-";
				format += StringUtil.strPad("d", arr2[2].length(), "d");

				if (arr.length == 2) {
					arr3 = arr[1].split(":");
					if (arr3 != null) {
						format += " ";
						format += StringUtil.strPad("H", arr3[0].length(), "H");

						if (arr3.length > 1) {
							format += ":";
							format += StringUtil.strPad("m", arr3[1].length(), "m");
						}
						if (arr3.length > 2) {
							format += ":";
							format += StringUtil.strPad("s", arr3[2].length(), "s");
						}

					}
				}
			} catch (Exception e) {
				logger.error("日期错误：" + date, e);
				return 0;
			}
		}
		// System.out.println(format+" "+date);

		DateFormat df = new SimpleDateFormat(format);
		try {
			time = df.parse(date).getTime();
		} catch (ParseException e) {
			System.out.println("parse error " + e.getMessage());
		}
		return time / 1000;
	}

	/**
	 * 获取指定日期，指定格式的字符串时间
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static final String getDate(Date date, String format) {
		SimpleDateFormat df = null;
		String returnValue = "";

		if (date != null) {
			df = new SimpleDateFormat(format, Locale.US);
			returnValue = df.format(date);
		}
		return (returnValue);
	}
	
	public static final String getCnDate(Date date) {
		SimpleDateFormat df = null;
		String returnValue = "";

		if (date != null) {
			df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
			returnValue = df.format(date);
		}
		return (returnValue);
	}

	/**
	 * 获取指定时间戳（毫秒），指定格式的字符串时间
	 * 
	 * @param time
	 * @param format
	 * @return
	 */
	public static final String getDate(long time, String format) {
		Date d = new Date();
		d.setTime(time);

		return getDate(d, format);
	}

	/**
	 * 获取指定时间戳（毫秒），指定格式的字符串时间
	 * 
	 * @param time
	 * @param format
	 * @return
	 */
	public static final String getDate(String time, String format) {
		return getDate(VarUtil.longval(time), format);
	}

	/**
	 * 获取当前日期，指定格式的字符串时间
	 * 
	 * @param format
	 * @return
	 */
	public static final String getDate(String format) {
		SimpleDateFormat df = null;
		String returnValue = "";
		Date date = new Date();
		if (date != null) {
			df = new SimpleDateFormat(format, Locale.US);
			returnValue = df.format(date);
		}
		return (returnValue);
	}

	/**
	 * 获取当前日期 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param format
	 * @return
	 */
	public static final String getDate() {
		return getDate("yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 获取当前日期字符串 Y-m-d H:i:s
	 * 
	 * @return String
	 */
	public static final String date() {
		return date("Y-m-d H:i:s");
	}

	/**
	 * 获取当前日期指定格式字符串
	 * 
	 * @param format
	 * @return String
	 */
	public static final String date(String format) {
		return date(format, new Date());
	}

	/**
	 * 获取指定时间戳、指定格式的时间字符串
	 * 
	 * @param format
	 * @param time
	 *                时间（秒）
	 * @return
	 */
	public static final String date(String format, String time) {
		Date d = new Date();
		d.setTime(VarUtil.longval(time) * 1000);
		return date(format, d);
	}

	/**
	 * 获取指定时间戳、指定格式的时间字符串
	 * 
	 * @param format
	 * @param time
	 *                时间（秒）
	 * @return
	 */
	public static final String date(String format, long time) {
		Date d = new Date();
		d.setTime(time * 1000);
		return date(format, d);
	}

	/**
	 * 获取指定时间戳、指定格式的时间字符串(与php格式兼容)
	 * 
	 * @param format
	 *                格式 Y=年,m=月,d=日,H=小时,i=分,s=秒
	 * @param time
	 *                日期
	 * @return
	 */
	public static final String date(String format, Date d) {
		char fArr[] = format.toCharArray();
		StringBuffer format2 = new StringBuffer();
		for (int i = 0; i < fArr.length; i++) {
			if ('Y' == fArr[i]) {// 年=2010
				format2.append("yyyy");
			} else if ('y' == fArr[i]) {// 年=10
				format2.append("yy");
			} else if ('m' == fArr[i]) {// 月= 01
				format2.append("MM");
			} else if ('d' == fArr[i]) {// 日=03
				format2.append("dd");
			} else if ('H' == fArr[i]) {// 小时=02
				format2.append("HH");
			} else if ('i' == fArr[i]) {// 分钟=03
				format2.append("mm");
			} else if ('s' == fArr[i]) {// 秒=02
				format2.append("ss");
			} else if ('s' == fArr[i]) {// 秒=02
				format2.append("ss");
			} else if ('g' == fArr[i]) {// 时 在上午或下午 (1~12)
				format2.append("h");
			} else if ('a' == fArr[i]) {// a 小写的上午和下午值 am 或 pm
				format2.append(getDate(d, "a").equals("AM") ? "am" : "pm");
			} else if ('A' == fArr[i]) {// A 大写的上午和下午值 AM 或 PM
				format2.append(getDate(d, "a").equals("AM") ? "AM" : "PM");
			} else if ('z' == fArr[i]) {// 年份中的第几天 0 到 366
				format2.append("D");
			} else if ('W' == fArr[i]) {// 年份中的第几周
				format2.append("w");
			} else if ('T' == fArr[i]) {// 时区
				format2.append("z");
			} else if ('B' == fArr[i]) {// 毫秒
				format2.append("S");
			} else if ('w' == fArr[i]) {// 星期 0（表示星期天）到 6（表示星期六）
				format2.append(enWeek2numWeek(getDate(d, "E")));
			} else if ('D' == fArr[i]) {// 星期 Mon 到 Sun
				format2.append(getDate(d, "E"));
			} else if ('l' == fArr[i]) {// 星期几，完整的文本格式 Sunday 到
							// Saturday
				format2.append(getDate(d, "EEEE"));
			} else if ('M' == fArr[i]) {// 三个字母缩写表示的月份 Jan 到 Dec
				format2.append("MMM");
			} else if ('F' == fArr[i]) {// 月份，完整的文本格式，例如 January 或者
							// March January 到
							// December
				format2.append("MMMM");
			} else {
				format2.append(fArr[i]);
			}
		}

		// System.out.println(format2);
		return getDate(d, format2.toString());
	}

	/**
	 * 英文周名转换为数字的
	 * 
	 * @param str
	 * @return
	 */
	public static final String enWeek2numWeek(String str) {
		// 0（表示星期天）到 6（表示星期六）
		str = str.toLowerCase();
		if (str.equals("sun"))
			return "0";// 星期天
		if (str.equals("mon"))
			return "1";// 星期一
		if (str.equals("tue"))
			return "2";// 星期二
		if (str.equals("wed"))
			return "3";// 星期三
		if (str.equals("thu"))
			return "4";// 星期四
		if (str.equals("fri"))
			return "5";// 星期五
		if (str.equals("sat"))
			return "6";// 星期六
		return "6";// sat//星期六
	}

	/**
	 * 取日期范围
	 * 
	 * @param type
	 *                =last_week/last_week_2/last_month/curr_month/curr_week
	 *                /curr_week_2/today/yesterday/7day/3day/1month/2month/3
	 *                month/6month
	 * @return
	 */
	public static String[] getDateRange(String type) {
		String[] r = new String[2];
		if (type.equals("last_week")) {// 上周(周1起始)
			String[] week = getLastWeek(1);
			if (week != null) {
				r[0] = week[0] + " 00:00:00";
				r[1] = week[1] + " 23:59:59";
			}
		} else if (type.equals("last_week_2")) {// 上周(周2起始)
			String[] week = getLastWeek(2);
			if (week != null) {
				r[0] = week[0] + " 00:00:00";
				r[1] = week[1] + " 23:59:59";
			}
		} else if (type.equals("last_month")) {// 上月
			r[0] = getLastMonth(1) + " 00:00:00";
			r[1] = getLastMonth(-1) + " 23:59:59";
		} else if (type.equals("curr_month")) {// 本月
			r[0] = date("Y-m") + "-01 00:00:00";
			r[1] = date("Y-m-d H:i:s");
		} else if (type.equals("curr_week")) {// 本周(周1起始)
			r[0] = getCurrWeek(1) + " 00:00:00";
			r[1] = date("Y-m-d H:i:s");
		} else if (type.equals("curr_week_2")) {// 本周(周2起始)
			r[0] = getCurrWeek(2) + " 00:00:00";
			r[1] = date("Y-m-d H:i:s");
		} else if (type.equals("today")) {// 今天
			r[0] = date("Y-m-d") + " 00:00:00";
			r[1] = date("Y-m-d H:i:s");
		} else if (type.equals("yesterday")) {// 昨天
			r[0] = getDay(-1) + " 00:00:00";
			r[1] = getDay(-1) + " 23:59:59";
		} else if (type.equals("7day")) {// 7天前
			r[0] = getDay(-6) + " 00:00:00";
			r[1] = date("Y-m-d H:i:s");
		} else if (type.equals("3day")) {// 3天前
			r[0] = getDay(-3) + " 00:00:00";
			r[1] = date("Y-m-d H:i:s");
		} else if (type.equals("1month")) {// 1月前
			r[0] = getMonth(-1) + " 00:00:00";
			r[1] = date("Y-m-d H:i:s");
		} else if (type.equals("2month")) {// 2月前
			r[0] = getMonth(-2) + " 00:00:00";
			r[1] = date("Y-m-d H:i:s");
		} else if (type.equals("3month")) {// 3月前
			r[0] = getMonth(-3) + " 00:00:00";
			r[1] = date("Y-m-d H:i:s");
		} else if (type.equals("6month")) {// 6月前
			r[0] = getMonth(-6) + " 00:00:00";
			r[1] = date("Y-m-d H:i:s");
		} else {// 默认 n天前
			r[0] = getDay(0 - Integer.valueOf(type)) + " 00:00:00";
			r[1] = date("Y-m-d H:i:s");
		}
		return r;
	}
	
	/**
	 * 取日期范围
	 * 
	 * @param type
	 *                =last_week/last_week_2/last_month/curr_month/curr_week
	 *                /curr_week_2/today/yesterday/7day/3day/1month/2month/3
	 *                month/6month
	 * @return
	 */
	public static String[] getRange(String type) {
		String[] r = new String[2];
		if (type.equals("today")) {// 今天
//			r[0] = date("Y-m-d") + " 00:00:00";
//			r[1] = date("Y-m-d H:i:s");
			r[0] = getDay(0);
			r[1] = getDay(0);
		} else if (type.equals("yesterday")) {// 昨天
			r[0] = getDay(-1);
			r[1] = getDay(-1);
		} else if (type.equals("3day")) {// 最近3天
			r[0] = getDay(-2);
			r[1] = getDay(0);
		}else if (type.equals("curr_month")) {// 本月
			r[0] = date("Y-m") + "-01";
			r[1] = date("Y-m-d");
		} else if (type.equals("curr_week")) {// 本周(周1起始)
			r[0] = getCurrWeek(1) + " 00:00:00";
			r[1] = date("Y-m-d H:i:s");
		} else {// 默认 n天前
			r[0] = getDay(0 - Integer.valueOf(type)) + " 00:00:00";
			r[1] = date("Y-m-d H:i:s");
		}
		return r;
	}

	/**
	 * 前几天
	 * 
	 * @param day
	 *                =天数,正数为+多少天，负数为-多少天
	 * @return
	 */
	public static String getDay(int day) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String da = df.format(new Date());
			Calendar cal = Calendar.getInstance();
			Date date = df.parse(da);
			cal.setTime(date);
			cal.add(Calendar.DAY_OF_MONTH, day);

			String monday = df.format(cal.getTime());
			return monday;
		} catch (Exception e) {
			logger.error("getDay:", e);
			return "";
		}

	}

	/**
	 * 前几月
	 * 
	 * @param n
	 *                =月数,正数为+多少月，负数为-多少月
	 * @return
	 */
	public static String getMonth(int n) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String da = df.format(new Date());
			Calendar cal = Calendar.getInstance();
			Date date = df.parse(da);
			cal.setTime(date);
			cal.add(Calendar.MONTH, n);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			String monday = df.format(cal.getTime());
			return monday;
		} catch (Exception e) {
			logger.error("getCurrWeek:", e);
			return "";
		}

	}

	/**
	 * 获取本周几的日期字符串
	 * 
	 * @param day
	 *                =周几开始
	 * @return
	 */
	public static String getCurrWeek(int day) {
		return getCurrWeek(day, new Date());
	}

	public static String getCurrWeek(int day, Date d) {
		try {
			int w = Integer.valueOf(date("w", d));
			if (w == 0)
				w = 7;
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			if (w < day) {
				cal.add(Calendar.DAY_OF_WEEK, 0 - w + 1);
				cal.add(Calendar.DAY_OF_WEEK, 0 - 7);
			} else {
				cal.add(Calendar.DAY_OF_WEEK, 0 - w + 1);
			}

			cal.add(Calendar.DAY_OF_WEEK, day - 1);

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String monday = df.format(cal.getTime());
			return monday;
		} catch (Exception e) {
			logger.error("getCurrWeek:", e);
			return "";
		}

	}

	/**
	 * 上周时间范围
	 * 
	 * @param day
	 *                =一周几天 1-7
	 * @return
	 */
	public static String[] getLastWeek(int day) {
		return getLastWeek(day, new Date());
	}

	/**
	 * 上周时间范围
	 * 
	 * @param day
	 *                =一周几天 1-7
	 * @return
	 */
	public static String[] getLastWeek(int day, Date d) {
		try {
			int w = Integer.valueOf(date("w", d));
			if (w == 0)
				w = 7;
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.add(Calendar.DAY_OF_WEEK, 0 - w + 1);
			cal.add(Calendar.DAY_OF_WEEK, day - 1);

			cal.add(Calendar.DAY_OF_WEEK, 0 - 7);

			if (w < day) {
				cal.add(Calendar.DAY_OF_WEEK, 0 - 7);
			} else {
			}

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String r[] = new String[2];

			r[0] = df.format(cal.getTime());
			cal.add(Calendar.DATE, 6);
			r[1] = df.format(cal.getTime());

			return r;
		} catch (Exception e) {
			logger.error("getLastWeek:", e);
			return null;
		}
	}

	/**
	 * 上月几号
	 * 
	 * @param day
	 *                =上月几号(-1表示月末)
	 * @return
	 */
	public static String getLastMonth(int day) {
		return getLastMonth(day, new Date());
	}

	public static String getLastMonth(int day, Date d) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String da = df.format(d);
			Calendar cal = Calendar.getInstance();
			Date date = df.parse(da);
			cal.setTime(date);
			cal.add(Calendar.MONTH, -1);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			if (day == -1) {
				day = cal.getActualMaximum(Calendar.DATE);
				// System.out.println(day);
			}
			cal.add(Calendar.DAY_OF_MONTH, day - 1);
			String monday = df.format(cal.getTime());
			return monday;
		} catch (Exception e) {
			logger.error("getLastMonth:", e);
			return "";
		}

	}
	
	/**
	 * 获取自然周的日期范围
	 * 
	 * @param day
	 *            一年中的自然周数
	 * @return
	 */
	public static String[] getRealWeeks(int day) {
		try {
			String first = date("Y") + "-01-01";
			Date d = String2Date(first, "yyyy-MM-dd");

			int w = Integer.valueOf(date("w", d));
			if (w == 0)
				w = 7;

			Calendar cal = Calendar.getInstance();
			cal.setTime(d);

			if (w > 1 && day > 1) {
				cal.add(Calendar.DATE, 0 - w + 1);
			}
			cal.add(Calendar.DATE, (day - 1) * 7);

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String r[] = new String[2];

			r[0] = df.format(cal.getTime());

			if (w > 1 && day == 1) {
				cal.add(Calendar.DATE, 7 - w);

			} else {
				cal.add(Calendar.DATE, 6);
			}

			r[1] = df.format(cal.getTime());

			return r;
		} catch (Exception e) {
			logger.error("getLastWeek:", e);
			return null;
		}
	}
	
	public static String[] getStartTimeAndEndTime(){
		String[] times = new String[2];


		Date dt = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24);
		Date dt2 = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24*7);
		SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-dd");
		times[0] = matter1.format(dt2);
		times[1] = matter1.format(dt);

		
		return times;
	}	
	
	/**
	 * 计算一个月有多少天
	 * @param dyear
	 * @param dmouth
	 * @return
	 */
	public static int calDayByYearAndMonth(String dyear,String dmouth){
		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM");
		    Calendar rightNow = Calendar.getInstance();
		    try{
		    rightNow.setTime(simpleDate.parse(dyear+"/"+dmouth));
		    }catch(ParseException e){
		    e.printStackTrace();
		    }
		    return rightNow.getActualMaximum(Calendar.DAY_OF_MONTH);//根据年月 获取月份天数
		}
	  /** 
     * 获取某年最后一天日期 
     * @param year 年份 
     * @return Date 
     */  
    public static String getYearLast(int year){  
    	SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();  
        calendar.clear();  
        calendar.set(Calendar.YEAR, year);  
        calendar.roll(Calendar.DAY_OF_YEAR, -1);  
        Date currYearLast = calendar.getTime();  
          
        return matter1.format(currYearLast);  
    } 
	
	public static void main(String[] args) {

		// System.out.println(getCurrWeek(2));
		String[] d;

		/*
		 * d = getDateRange("last_week"); System.out.println(d[0]);
		 * System.out.println(d[1]);
		 */
		d = getDateRange("last_week_2");
		System.out.println(d[0]);
		System.out.println(d[1]);

		/*
		 * d = getDateRange("curr_week"); System.out.println(d[0]);
		 * System.out.println(d[1]);
		 */

		d = getDateRange("curr_week_2");
		System.out.println(d[0]);
		System.out.println(d[1]);

		/*
		 * d = getDateRange("last_month"); System.out.println(d[0]);
		 * System.out.println(d[1]);
		 * 
		 * 
		 * d = getDateRange("curr_month"); System.out.println(d[0]);
		 * System.out.println(d[1]);
		 */
		/*
		 * 
		 * d = getDateRange("curr_week"); System.out.println(d[0]);
		 * System.out.println(d[1]);
		 * 
		 * d = getDateRange("curr_week_2"); System.out.println(d[0]);
		 * System.out.println(d[1]);
		 */

		/*
		 * d = getDateRange("today"); System.out.println(d[0]);
		 * System.out.println(d[1]);
		 */

		d = getDateRange("12");
		System.out.println(d[0]);
		System.out.println(d[1]);
		
		String[] cond = { "today", "yesterday", "3day" };
		for (int i = 0; i < cond.length; i++) {
			d = getRange(cond[i]);
			System.out.println("=======" + cond[i]);
			System.out.println(d[0]);
			System.out.println(d[1]);
			
		}
		System.out.println(getRange("curr_month")[0]);
		System.out.println(getRange("curr_month")[1]);
		
		System.out.println("--------------------------");
		System.out.println(getStartTimeAndEndTime()[0]);
		System.out.println(getStartTimeAndEndTime()[1]);
		
		System.out.println("-----------------------");
		System.out.println(getCurrWeek(1));
		System.out.println(date("Y-m-d"));
		/*
		 * //System.out.println(getDate(new Date(), "EEEE"));
		 * System.out.println(date("Y-F-d H:i:s", new Date()));
		 * System.out.println(time("11-oct-1999", "dd-MMM-yyyy"));
		 * 
		 * String input = "1222-11-11"; SimpleDateFormat formatter = new
		 * SimpleDateFormat("yyyy-MM-dd", Locale.US); Date t = null;
		 * try{ t = formatter.parse(input); System.out.println(t);
		 * }catch(ParseException e){
		 * 
		 * System.out.println("unparseable using " + formatter); }
		 */
		String[]  str= getLastWeek(1, new Date());
		for (int i = 0; i < str.length; i++) {
			
			System.out.println("=="+str[i]);
		}
	}

}
