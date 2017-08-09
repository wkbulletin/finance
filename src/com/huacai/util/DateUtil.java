package com.huacai.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;


public class DateUtil {

	public static final Logger logger = Logger.getLogger(DateUtil.class);

	static {
		Properties props = System.getProperties();
		props.setProperty("user.timezone", "GMT+8");
	}

	/**
	 * 得到今天的时间,并格式化.
	 * 
	 * @param format
	 *            格式
	 * @return 今天的日期。
	 */
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

	public static String getUnixTime() {
		return String.valueOf(new Date().getTime() / 1000);
	}

	public static String yesterday(String format) {
		Date date = new Date();
		date.setTime(date.getTime() - 86400000);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.format(date);
	}

	public static String yesterday() {
		Date date = new Date();
		date.setTime(date.getTime() - 86400000);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormat.format(date);
	}

	public static String tomorrow() {
		Date date = new Date();
		date.setTime(date.getTime() + 86400000);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormat.format(date);
	}

	public static String tomorrow(String format) {
		Date date = new Date();
		date.setTime(date.getTime() + 86400000);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.format(date);
	}

	/**
	 * 根据一个时间戳(长整形字符串)生成指定格式时间字符串
	 * 
	 * @param time
	 *            时间戳(长整形字符串)
	 * @param format
	 *            格式字符串如yyyy-MM-dd
	 * @return 时间字符串
	 */
	public static String getDate(long time, String format) {
		Date d = new Date();
		d.setTime(time);
		DateFormat df = new SimpleDateFormat(format);
		return df.format(d);
	}

	/**
	 * 字符型时间变成时间类型
	 * 
	 * @param date
	 *            字符型时间 例如: "2008-08-08"
	 * @param format
	 *            格式化形式 例如: "yyyy-MM-dd"
	 * @return 出现异常时返回null
	 */
	public static Date getFormatDate(String date, String format) {

		Date d = null;
		if (date != null && date.length() > 0) {
			DateFormat df = new SimpleDateFormat(format);
			try {
				d = df.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return d;
	}

	/**
	 * 根据一个时间戳(长整形字符串)生成指定格式时间字符串
	 * 
	 * @param time
	 *            时间戳(长整形字符串)
	 * @param format
	 *            格式字符串如yyyy-MM-dd
	 * @return 时间字符串
	 */
	public static String getDate(String time, String format) {
		Date d = new Date();
		long t = Long.parseLong(time);
		d.setTime(t);
		DateFormat df = new SimpleDateFormat(format);
		return df.format(d);
	}

	/**
	 * 根据一个时间戳(长整形字符串)生成指定格式时间字符串
	 * 
	 * @param time
	 *            时间戳(长整形字符串)
	 * @param format
	 *            格式字符串如yyyy-MM-dd
	 * @return 时间字符串
	 */
	public static String getDate(Date date, String format) {
		String formatDate = "";
		if (date != null) {
			DateFormat df = new SimpleDateFormat(format);
			formatDate = df.format(date);
		}
		return formatDate;
	}

	/**
	 * 得到日期的时间戳
	 * 
	 * @param date
	 *            八位或十位日期，格式：yyyy-MM-dd或yyyyMMdd
	 * @return 时间戳
	 */
	public static long getTime(String date) {
		long time = 0;
		if (date == null || date.length() < 8) {
			return 0;
		}
		if (date.length() == 8) {
			date = DateUtil.format(date);
		}
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			time = df.parse(date).getTime();
		} catch (ParseException e) {
			logger.info("parse error " + e.getMessage());
		}
		return time;
	}

	/**
	 * 得到日期的时间戳
	 * 
	 * @param date
	 *            八位或十位日期，格式：yyyy-MM-dd或yyyyMMdd
	 * @return 时间戳
	 */
	public static Date getDate(String date) {
		Date returnDate = null;
		if (date == null || date.length() < 8) {
			return null;
		}
		if (date.length() == 8) {
			date = DateUtil.format(date);
		}
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			returnDate = df.parse(date);
		} catch (ParseException e) {
			logger.info("parse error " + e.getMessage());
		}
		return returnDate;
	}

	/**
	 * 得到日期的时间戳
	 * 
	 * @param date
	 *            日期
	 * @param format
	 *            日期格式
	 * @return 时间戳
	 */
	public static long getTime(String date, String format) {
		long time = 0;
		if (date == null || date.length() < 8) {
			return 0;
		}
		if (date.length() == 8) {
			date = DateUtil.format(date);
		}
		DateFormat df = new SimpleDateFormat(format);
		try {
			time = df.parse(date).getTime();
		} catch (ParseException e) {
			logger.info("parse error " + e.getMessage());
		}
		return time;
	}

	/**
	 * 日期的指定域加减
	 * 
	 * @param time
	 *            时间戳(长整形字符串)
	 * @param field
	 *            加减的域,如date表示对天进行操作,month表示对月进行操作,其它表示对年进行操作
	 * @param num
	 *            加减的数值
	 * @return 操作后的时间戳(长整形字符串)
	 */
	public static String addDate(String time, String field, int num) {
		int fieldNum = Calendar.YEAR;
		if (field.equals("month")) {
			fieldNum = Calendar.MONTH;
		}
		if (field.equals("date")) {
			fieldNum = Calendar.DATE;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(Long.parseLong(time));
		cal.add(fieldNum, num);
		return String.valueOf(cal.getTimeInMillis());
	}

	/**
	 * 日期的指定域加减
	 * 
	 * @param time
	 *            时间戳(长整形字符串)
	 * @param field
	 *            加减的域,如date表示对天进行操作,month表示对月进行操作,其它表示对年进行操作
	 * @param num
	 *            加减的数值
	 * @return 操作后的时间戳(长整形字符串)
	 */
	public static Date addDate(Date time, String field, int num) {
		int fieldNum = Calendar.YEAR;
		if (field.equals("month")) {
			fieldNum = Calendar.MONTH;
		}
		if (field.equals("date")) {
			fieldNum = Calendar.DATE;
		}
		if (field.equals("hour")) {
			fieldNum = Calendar.HOUR;
		}
		if (field.equals("minute")) {
			fieldNum = Calendar.MINUTE;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time.getTime());
		cal.add(fieldNum, num);
		return new Date(cal.getTimeInMillis());
	}

	/**
	 * 日期的指定域加减
	 * 
	 * @param time
	 *            时间戳(长整形字符串)
	 * @param field
	 *            加减的域,如date表示对天进行操作,month表示对月进行操作,其它表示对年进行操作
	 * @param num
	 *            加减的数值
	 * @return 操作后的时间戳(长整形字符串)
	 */
	public static long addDate(String field, int num) {
		int fieldNum = Calendar.YEAR;
		if (field.equals("month")) {
			fieldNum = Calendar.MONTH;
		}
		if (field.equals("date")) {
			fieldNum = Calendar.DATE;
		}
		if (field.equals("hour")) {
			fieldNum = Calendar.HOUR;
		}
		if (field.equals("minute")) {
			fieldNum = Calendar.MINUTE;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(new Date().getTime());
		cal.add(fieldNum, num);

		return cal.getTimeInMillis();
	}

	/**
	 * 得到现在的时间，格式时：分：秒
	 * 
	 * @param format
	 *            格式,如HH:mm:ss
	 * @return 返回现在的时间可用于插入数据库和显示
	 */
	public static String getNowTime(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new java.util.Date());
	}

	/**
	 * 得到今天的星期
	 * 
	 * @return 今天的星期
	 */
	public static String getWeek() {
		SimpleDateFormat sdf = new SimpleDateFormat("E");
		return sdf.format(new java.util.Date());
	}

	/**
	 * 得到今天的星期
	 * 
	 * @return 今天的星期
	 */
	public static String getWeek(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("E");
		return sdf.format(date);
	}

	/**
	 * 得到一个日期是否是上午
	 * 
	 * @param date
	 *            日期
	 * @return 日期为上午时返回true
	 */
	public static boolean isAm(Date date) {
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("H");
		if (sdf.format(date).compareTo("12") < 0) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 把八位的格式为yyyyMMdd的日期转换为十位的yyyy-MM-dd格式
	 * 
	 * @param date
	 *            八位的格式为yyyyMMdd的日期
	 * @return 十位的yyyy-MM-dd格式
	 */
	public static String format(String date) {
		String returnDate = null;
		if (date.length() == 8) {
			returnDate = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
		} else {
			returnDate = DateUtil.today("yyyy-MM-dd");
		}
		return returnDate;
	}

	/**
	 * 把八位的格式为yyyyMMdd的日期转换为十位的yyyy-MM-dd格式
	 * 
	 * @param date
	 *            八位的格式为yyyyMMdd的日期
	 * @return 十位的yyyy-MM-dd格式
	 */
	public static String format(Date date, String format) {
		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}

	public static final Date convertStrtoDateIsss(String dateStr) {
		DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = f.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static final Date convertStrtoDateIsss(String dateStr, String format) {
		DateFormat f = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = f.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 获得当前日期周的日期
	 * 
	 * @param date
	 *            当前周的日期
	 * @param week
	 *            星期，0表示星期日，1表示星期一，6表示星期六。
	 * @return 星期的日期
	 */
	public static Date getWeek(Date date, int week) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, week + 1);
		String dateFmt = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return new Date(DateUtil.getTime(dateFmt));
	}

	/**
	 * 获得下个星期的日期
	 * 
	 * @param date
	 *            当前周的日期
	 * @return 星期的日期
	 */
	public static Date getNextWeek(Date date) {
		date = new Date(date.getTime() + 604800000);
		String dateFmt = new SimpleDateFormat("yyyy-MM-dd").format(date);
		return new Date(DateUtil.getTime(dateFmt));
	}

	/**
	 * 获得上个星期的日期
	 * 
	 * @param date
	 *            当前周的日期
	 * @return 星期的日期
	 */
	public static Date getPreWeek(Date date) {
		date = new Date(date.getTime() - 604800000);
		String dateFmt = new SimpleDateFormat("yyyy-MM-dd").format(date);
		return new Date(DateUtil.getTime(dateFmt));
	}

	/**
	 * 获得前30天的日期
	 * 
	 * @param date
	 *            当前周的日期
	 * @return 星期的日期
	 */
	public static Date getPre30(Date date) {
		date = new Date(date.getTime() - 2592000000l);
		String dateFmt = new SimpleDateFormat("yyyy-MM-dd").format(date);
		return new Date(DateUtil.getTime(dateFmt));
	}

	/**
	 * 
	 */
	public static int getLongTime(String time) {
		int longTime = 0;
		String[] tmp = time.split(":");
		if (tmp.length > 1) {
			longTime += Integer.parseInt(tmp[0]) * 3600000;
			longTime += Integer.parseInt(tmp[1]) * 60000;
		}
		return longTime;
	}
	
	public static Date getDateTime(String date,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Date getObDate(Object obj) {
		if (obj == null)
			return null;
		return (Date) obj;
	}
	
	public static BigDecimal getBigDecimal(Object obj) {
		if (obj != null) {
			return (BigDecimal) obj;
		} else {
			return BigDecimal.ZERO;
		}
	}
	
	public static long getLong(Object object) {
		long value = 0;
		if (object != null) {
			value = ((BigDecimal) object).longValue();
		}
		return value;
	}
	
	/**
	 * 把对象转换成整数
	 * 
	 * @param object
	 *            对象
	 * @return 整数
	 */
	public static int getInt(Object object) {
		int value = 0;
		if (object != null) {
			if (object instanceof BigDecimal) {
				value = ((BigDecimal) object).intValue();
			} else if (object instanceof Integer) {
				value = ((Integer) object).intValue();
			} else if (object instanceof Long) {
				value = ((Long) object).intValue();
			} else{
				value = StringUtil.convertInt(object.toString(), value);
			}

		}
		return value;
	}
	
    public static boolean isNumeric(String str){  
        for (int i = str.length();--i>=0;){    
         if (!Character.isDigit(str.charAt(i))){  
          return false;  
         }  
        }  
        return true;  
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
	 * 前几天
	 * 
	 * @param day
	 *                =天数,正数为+多少天，负数为-多少天
	 * @return
	 */
	public static String getDay(int day, Date dt) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String da = df.format(dt);
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
}