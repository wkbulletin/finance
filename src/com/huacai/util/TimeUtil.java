package com.huacai.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import jodd.datetime.JDateTime;

import com.bootcore.toolkits.CallUtil;

public class TimeUtil {
	private TimeUtil() {
	}

	// /////////////////////////////////////////////////////////////////
	static public int eatin() {
		try {
			return System.in.read();
		} catch (Exception e) {
			return -1;// ???????
		}
	}

	static public void block() {
		System.err.println("------------------- ??????????????????????????------------------- ");

		System.err.println(CallUtil.dumpStack());

		block(Integer.MAX_VALUE);
	}

	static public boolean block(//
					final long time) {
		if (time > 0/* ??????��?? */) {
			try {
				Thread.sleep(time);
				return true;// ???????
			} catch (Exception ex) {
				return true;// ???????
			}
		} else {
			return false;// ??��????�ʦ�??
		}
	}

	static public boolean nblock(//
					final long time) {
		if (time > 0/* ??????��?? */) {
			try {
				TimeUnit//
				.NANOSECONDS//
				.sleep(time + 0);
				return true;// ???????
			} catch (Exception ex) {
				return true;// ???????
			}
		} else {
			return false;// ??��????�ʦ�??
		}
	}

	static public boolean ublock(//
					final long time) {
		if (time > 0/* ??????��?? */) {
			try {
				TimeUnit//
				.MICROSECONDS//
				.sleep(time + 0);
				return true;// ???????
			} catch (Exception ex) {
				return true;// ???????
			}
		} else {
			return false;// ??��????�ʦ�??
		}
	}

	static public void waiton(//
					Object object) {
		if (object != (Object) null) {
			waiton(object, Long.MAX_VALUE);
		}
	}

	static public boolean waiton(//
					Object object,//
					final long time) {
		if (object == null || time < 0) {
			return false;// ??��???�ʦ�??
		} else {
			try {
				synchronized (object) {
					object.wait(time);
					return true;// ???????
				}
			} catch (Exception ex) {
				return true;// ???????GOOD
			}
		}
	}

	static public void wakeup(//
					Object object) {
		if (object != null) {
			object.notify();
		}
	}

	static public void wakeups(//
					Object object) {
		if (object != null) {
			object.notifyAll();
		}
	}

	// //////////////////////////////////////////////////////////////////
	/**
	 * ??????????
	 */
	final static public String[] DaysOfWeek;

	/**
	 * 2K????????
	 */
	static final private int Time2000 = 946656000;

	/**
	 * ?????????
	 */
	static final public int TimeOfDay = 86400000;

	final static SimpleDateFormat F_Time06, F_Time08;
	final static SimpleDateFormat F_Time12, F_Date08;
	final static SimpleDateFormat F_Date10, F_DTAS14;
	final static SimpleDateFormat F_DTAS19, F_DTAS23;

	final static SimpleDateFormat P_Date08, P_Date10;
	final static SimpleDateFormat P_DTAS14, P_DTAS19;

	static {
		F_Time06 = new SimpleDateFormat("HHmmss");
		F_Time08 = new SimpleDateFormat("HH:mm:ss");
		F_Time12 = new SimpleDateFormat("HH:mm:ss.SSS");

		F_Date08 = new SimpleDateFormat("yyyyMMdd");
		F_Date10 = new SimpleDateFormat("yyyy-MM-dd");

		F_DTAS14 = new SimpleDateFormat("yyyyMMddHHmmss");
		F_DTAS19 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		F_DTAS23 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

		P_Date08 = new SimpleDateFormat("yyyyMMdd");
		P_Date10 = new SimpleDateFormat("yyyy-MM-dd");
		P_DTAS14 = new SimpleDateFormat("yyyyMMddHHmmss");
		P_DTAS19 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		DaysOfWeek = new String[] { "??", "?", "??", "??", "??", "??", "??" };
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * yyyy-MM-dd HH:mm:ss.SSS
	 */
	static public String format(//
					final Date date,//
					final String format) {
		SimpleDateFormat simple = null;
		simple = new SimpleDateFormat(format);
		return simple.format(date).toString();
	}

	/**
	 * yyyy-MM-dd HH:mm:ss.SSS
	 */
	static public String format(//
					final long date,//
					final String format) {
		return format(new Date(date), format);
	}

	/**
	 * DDD day of year<br>
	 * YYYY-MM-DD hh:mm:ss.mss<br>
	 * W,WW,WWW week of month,year,pre W
	 * 
	 * @see http://jodd.org/doc/jdatetime.html
	 */
	static public String format2(//
					final Date date,//
					final String format) {
		JDateTime time = new JDateTime(date);
		return time.toString(format).trim();
	}

	/**
	 * DDD day of year<br>
	 * YYYY-MM-DD hh:mm:ss.mss<br>
	 * W,WW,WWW week of month,year,pre W
	 * 
	 * @see http://jodd.org/doc/jdatetime.html
	 */
	static public String format2(//
					final long date,//
					final String format) {
		JDateTime time = new JDateTime(date);
		return time.toString(format).trim();
	}

	/**
	 * ????????LONG???????
	 */
	static public long now() {
		return System.currentTimeMillis();
	}

	/**
	 * ???????,yyyy-MM-dd
	 */
	static public String getDate() {
		return TimeUtil.getDate(new Date());
	}

	/**
	 * ???????,yyyy-MM-dd
	 */
	static public String getDate(long time) {
		return getDate(new Date(time + 0));
	}

	/**
	 * ???????,yyyy-MM-dd
	 */
	static public String getDate(Calendar cal) {
		return TimeUtil.getDate(cal.getTime());
	}

	/**
	 * ???????,yyyy-MM-dd
	 */
	static public String getDate(final Date date) {
		return globalFormat(date, TimeUtil.F_Date10);
	}

	/**
	 * ????????
	 */
	static public int getWeek() {
		int f = Calendar.DAY_OF_WEEK + 0;
		Calendar c = Calendar.getInstance(); 
		return (c.get(f) + 6) % 7 + 1;// adj
	}

	/**
	 * ??????,HH:mm:ss
	 */
	static public String getTime() {
		return TimeUtil.getTime(new Date());
	}

	/**
	 * ??????,HH:mm:ss
	 */
	static public String getTime(long time) {
		return getDate(new Date(time + 0));
	}
	
	/**
	 * ??????,HH:mm:ss
	 */
	static public String getTime(Calendar cal) {
		return TimeUtil.getTime(cal.getTime());
	}

	/**
	 * ??????,HH:mm:ss
	 */
	static public String getTime(final Date date) {
		return globalFormat(date, TimeUtil.F_Time08);
	}

	/**
	 * ???????,yyyyMMdd
	 */
	static public String getDateEx() {
		return TimeUtil.getDateEx(new Date());
	}

	/**
	 * ???????,yyyyMMdd
	 */
	static public String getDateEx(long date) {
		return getDateEx(new Date(date + 0));
	}

	/**
	 * ???????,yyyyMMdd
	 */
	static public String getDateEx(Calendar cal) {
		return TimeUtil.getDateEx(cal.getTime());
	}

	/**
	 * ???????,yyyyMMdd
	 */
	static public String getDateEx(final Date date) {
		return globalFormat(date, TimeUtil.F_Date08);
	}

	/**
	 * ??????,HHmmss
	 */
	static public String getTimeEx() {
		return TimeUtil.getTimeEx(new Date());
	}

	/**
	 * ??????,HHmmss
	 */
	static public String getTimeEx(long date) {
		return getTimeEx(new Date(date + 0));
	}

	/**
	 * ??????,HHmmss
	 */
	static public String getTimeEx(Calendar cal) {
		return TimeUtil.getTimeEx(cal.getTime());
	}

	/**
	 * ??????,HHmmss
	 */
	static public String getTimeEx(final Date date) {
		return globalFormat(date, TimeUtil.F_Time06);
	}

	/**
	 * ???????????,yyyy-MM-dd HH:mm:ss
	 */
	static public String getDateTime() {
		return TimeUtil.getDateTime(new Date());
	}

	/**
	 * ???????????,yyyy-MM-dd HH:mm:ss
	 */
	static public String getDateTime(long time) {
		return getDateTime(new Date(time + 0));
	}

	/**
	 * ???????????,yyyy-MM-dd HH:mm:ss
	 */
	static public String getDateTime(Calendar cal) {
		return TimeUtil.getDateTime(cal.getTime());
	}

	/**
	 * ???????????,yyyy-MM-dd HH:mm:ss
	 */
	static public String getDateTime(final Date date) {
		return globalFormat(date, TimeUtil.F_DTAS19);
	}

	/**
	 * ???????????,yyyyMMddHHmmss
	 */
	static public String getDateTimeEx() {
		return TimeUtil.getDateTimeEx(new Date());
	}

	/**
	 * ???????????,yyyyMMddHHmmss
	 */
	static public String getDateTimeEx(long time) {
		return getDateTimeEx(new Date(time + 0));
	}

	/**
	 * ???????????,yyyyMMddHHmmss
	 */
	static public String getDateTimeEx(Calendar cal) {
		return TimeUtil.getDateTimeEx(cal.getTime());
	}

	/**
	 * ???????????,yyyyMMddHHmmss
	 */
	static public String getDateTimeEx(final Date date) {
		return globalFormat(date, TimeUtil.F_DTAS14);
	}

	static private String globalFormat(Date date,//
					final SimpleDateFormat format) {
		synchronized (format) {// ???????????????
			return format.format(date);// ??????
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * ???,yyyy-MM-dd
	 */
	static public Date getDate(final String date) {
		return globalParase(date, TimeUtil.P_Date10);
	}

	/**
	 * ???,yyyyMMdd
	 */
	static public Date getDateEx(final String date) {
		return globalParase(date, TimeUtil.P_Date08);
	}

	/**
	 * ???,yyyy-MM-dd HH:mm:ss
	 */
	static public Date getDateTime(final String date) {
		return globalParase(date, TimeUtil.P_DTAS19);
	}

	/**
	 * ???,yyyyMMddHHmmss
	 */
	static public Date getDateTimeEx(String date) {
		return globalParase(date, TimeUtil.P_DTAS14);
	}

	static private Date globalParase(String date,//
					final SimpleDateFormat format) {
		synchronized (format) {// ???????????????
			try {
				return format.parse(date);// ??????
			} catch (final ParseException ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * ???????(ms)??
	 */
	static public int oneday() {
		return 24 * 60 * 60 * 1000;
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 1970.01.01 00.00.00??????(??��=??)
	 */
	static public Date toDate(int time) {
		if (time <= 0) {
			return new Date();
		}

		return new Date(time * 1000L);
	}

	/**
	 * 1970.01.01 00.00.00??????(??��=??)
	 */
	static public int toTime(long time) {
		return TimeUtil.toTime(new Date());
	}

	/**
	 * 1970.01.01 00.00.00??????(??��=??)
	 */
	static public int toTime(Date date) {
		return (int) (date.getTime() / 1000);
	}

	/**
	 * 1970.01.01 00.00.00?????? <br>
	 * yyyy-MM-dd HH:mm:ss(??��=??)
	 */
	static public String toDateStr(int time) {
		return getDateTime(TimeUtil.toDate(time));
	}

	/**
	 * 2000.01.01 00.00.00??????(??��=??)
	 */
	static public Date to2KDate(int time) {
		if (time > 0) {
			long offset = time + Time2000;
			return new Date(offset * 1000);
		} else {
			return new Date(Time2000 * 1000);
		}
	}

	/**
	 * 2000.01.01 00.00.00??????(??��=??)
	 */
	static public int to2KTime(int time) {
		return toTime(time) - Time2000;
	}

	/**
	 * 2000.01.01 00.00.00??????(??��=??)
	 */
	static public int to2KTime(Date date) {
		return toTime(date) - Time2000;
	}

	/**
	 * 2000.01.01 00.00.00??????<br>
	 * yyyy-MM-dd HH:mm:ss(??��=??)
	 */
	static public String to2KDateStr(int time) {
		return getDateTime(TimeUtil.to2KDate(time));
	}
}
