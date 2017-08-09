package libcore.util;

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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * @author Andyfoo
 * 
 * 
 *         功能说明：日期相关工具函数
 */
public class DateUtil {
	protected final static Logger logger = LogManager.getLogger(DateUtil.class);

	// private static String defaultDatePattern = "yyyyMMdd HH:mm:ss";
	// private static String timePattern = "HH:mm";

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
		DateFormat df = new SimpleDateFormat(format, Locale.US);
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
	public static String dateToDate(String date, String format) {
		return getDate(getTime(date),format);
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
		return getTime(date, "");
	}

	/**
	 * 获取指定日期时间（毫秒）
	 * 
	 * @return long
	 */
	public static long getTime(String date, String format) {
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

		DateFormat df = new SimpleDateFormat(format, Locale.US);
		try {
			time = df.parse(date).getTime();
		} catch (ParseException e) {
			logger.error("", e);
		}
		return time;
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

	public static void main(String[] args) {

		System.out.println(getDate("a"));

	}

	 /**
     * <li>功能描述：时间相减得到天数
     * @param beginDateStr
     * @param endDateStr
     * @return
     * long 
     * @author qzm
     */
    public static long getDaySub(String beginDateStr,String endDateStr)
    {
        long day=0;
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");    
        java.util.Date beginDate;
        java.util.Date endDate;
        try
        {
            beginDate = format.parse(beginDateStr);
            endDate= format.parse(endDateStr);    
            day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);    
            //System.out.println("相隔的天数="+day);   
        } catch (ParseException e)
        {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }   
        return day;
    } 
}
