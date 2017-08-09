package com.huacai.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.regex.Pattern;

public abstract class DbUtil {

	//这是判断yyyy-mm-dd这种格式的，基本上把闰年咄1�7�等的情况都考虑进去亄1�7,加了时间验证的1�7 2009-02-28 23:30:59  2009-02-29 23:30:59
	private final static String varPatternStr6 = "^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-)) (20|21|22|23|[0-1]?\\d):[0-5]?\\d:[0-5]?\\d$";
	public static boolean isValidateDateTime(String date){
		boolean b1 = Pattern.matches(varPatternStr6, date);
		System.out.println(b1);
		
		return b1;
	}
	public static void main(String[] args){
		System.out.println(isValidateDateTime("2013-02-29 23:14:56"));
	}
	
	public static int getInt(Object obj) {
		int value = 0;
		if (obj != null) {
			if (obj instanceof BigDecimal) {
				value = ((BigDecimal) obj).intValue();
			} else if (obj instanceof Integer) {
				value = ((Integer) obj).intValue();
			} else if (obj instanceof String) {
				return Integer.valueOf((String) obj);
			} else if (obj instanceof Long) {
				value = ((Long) obj).intValue();
			}

		}
		return value;
	}

	public static long getLong(Object obj) {
		long value = 0;
		if (obj != null) {
			if (obj instanceof BigDecimal) {
				value = ((BigDecimal) obj).longValue();
			} else if (obj instanceof Long) {
				value = ((Long) obj).longValue();
			} else if (obj instanceof String) {
				return Long.valueOf((String) obj);
			} else if (obj instanceof Integer) {
				value = ((Integer) obj).longValue();
			} else {
				value = Long.valueOf((String) obj);
			}

		}
		return value;
	}

	public static String getString(Object obj) {
		String value = null;
		if (obj != null) {
			value = String.valueOf(obj);
			if ("null".equals(value)) {
				value = null;
			}
		}
		return value;
	}

	public static String getString(byte[] bytes, String encode) {
		String value = null;
		if (bytes != null) {
			try {
				value = new String(bytes, encode);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if ("null".equals(value)) {
				value = null;
			}
		}
		return value;
	}

	public static String getString(byte[] bytes) {
		return getString(bytes, "gbk");
	}

	public static double getDouble(Object obj) {
		if (obj != null) {
			if (obj instanceof BigDecimal) {
				return ((BigDecimal) obj).doubleValue();
			} else if (obj instanceof Integer) {
				return ((Integer) obj).doubleValue();
			} else if (obj instanceof String) {
				return Double.valueOf((String) obj);
			} else if (obj instanceof Long) {
				return ((Long) obj).doubleValue();
			}
			return (Double) obj;
		} else {
			return 0;
		}
	}

	public static float getFloat(Object obj) {
		if (obj != null) {
			if (obj instanceof BigDecimal) {
				return ((BigDecimal) obj).floatValue();
			} else if (obj instanceof Integer) {
				return ((Integer) obj).floatValue();
			} else if (obj instanceof String) {
				return Float.valueOf((String) obj);
			} else if (obj instanceof Long) {
				return ((Long) obj).floatValue();
			}
			return (Float) obj;
		} else {
			return 0;
		}
	}

	public static BigDecimal getBigDecimal(Object obj) {
		if (obj != null) {
			return (BigDecimal) obj;
		} else {
			return BigDecimal.ZERO;
		}
	}

	public static Date getDate(Object obj) {
		if (obj == null)
			return null;
		return (Date) obj;
	}
	
	/**
	 * 
	 * @param ss  格式化小数点长度为5
	 */
	public static String  getNum(String t,int length) {
		if(t==null||!StringUtil.isNum(t)){
			return"";
		}
		if(length<=0){
			length=5;
		}
		DecimalFormat df = new DecimalFormat();
		  df.setMaximumFractionDigits(length);
		  String tt =df.format(new BigDecimal(t));
		  if(tt!=null&&tt.indexOf(",")!=-1){
			  tt=tt.replaceAll(",", "");
		  }
		 return  tt;
	}
}
