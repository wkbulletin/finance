package libcore.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class DbUtil {
	static String safeFilter = "'|(and|or)\\b.+?(>|<|=|in|like)|\\/\\*.+?\\*\\/|<\\s*script\\b|\\bEXEC\\b|UNION.+?SELECT|UPDATE.+?SET|INSERT\\s+INTO.+?VALUES|(SELECT|DELETE).+?FROM|(CREATE|ALTER|DROP|TRUNCATE)\\s+(TABLE|DATABASE)";
	static Pattern safeFilterPattern = Pattern.compile(safeFilter, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

	public static void main(String argc[]) {
		// System.out.println(getSafeStr("asfasdf'sadfasf and in and sad UNION a SELECT * FROM  V_UC_MEMBER  where M_LOGINNAME ='andyfoo';s"));

		// System.out.println(htmlspecialchars("\"'&<afas>\"'&<afas>\"'&<afas>"));
		// System.out.println(htmlspecialchars_decode(htmlspecialchars("\"'&<afas>\"'&<afas>\"'&<afas>")));

		System.out.println(getQuerySafeStr("\"'&<afas\\>\"'&<afas>\"'&<afas>"));
	}

	/**
	 * 检测变量
	 * 
	 * @param str
	 * @return true 字符正常 false 有非法字符
	 */
	public static boolean checkSafeStr(String str) {
		Matcher m = safeFilterPattern.matcher(str);
		if (m.find()) {
			return false;
		}
		return true;
	}

	/*
	 * sql需要加 ESCAPE '/'： SELECT * FROM BOSS_USER_MESSAGE WHERE
	 * MESSAGE_TITLE LIKE '%/%%' ESCAPE '/'
	 */
	public static String escapeSQLLike(String likeStr) {
		String str = likeStr;
		str = str.trim();
		str = StringUtil.replace(str, "/", "//");
		str = StringUtil.replace(str, "_", "/_");
		str = StringUtil.replace(str, "%", "/%");
		str = StringUtil.replace(str, "％", "/％");
		str = StringUtil.replace(str, "'", "‘");

		// str = StringUtils.replace(str, "?", "_");
		// str = StringUtils.replace(str, "*", "%");
		return str;
	}

	/**
	 * 返回安全的字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String getQuerySafeStr(String str) {
		if (str == null)
			return "";

		// return escapeSQLLike(str.replaceAll(safeFilter, ""));
		return escapeSQLLike(str);
	}

	/**
	 * 返回安全的字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String getDbSafeStr(String str) {
		if (str == null)
			return "";
		str = StringUtil.replace(str, "'", "‘");

		str = StringUtil.replace(str, "<", "＜");
		str = StringUtil.replace(str, ">", "＞");

		return str;
	}

	/**
	 * 编码字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String htmlspecialchars(String str) {
		if (str == null)
			return "";
		str = StringUtil.replace(str, "&", "&amp;");
		str = StringUtil.replace(str, "\"", "&quot;");
		str = StringUtil.replace(str, "'", "&#039;");
		str = StringUtil.replace(str, "<", "&lt;");
		str = StringUtil.replace(str, ">", "&gt;");
		return str;
	}

	/**
	 * 解码字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String htmlspecialchars_decode(String str) {
		if (str == null)
			return "";
		str = StringUtil.replace(str, "&amp;", "&");
		str = StringUtil.replace(str, "&quot;", "\"");
		str = StringUtil.replace(str, "&#039;", "'");
		str = StringUtil.replace(str, "&lt;", "<");
		str = StringUtil.replace(str, "&gt;", ">");
		return str;
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
}
