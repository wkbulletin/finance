package libcore.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.management.IntrospectionException;

/**
 * 
 * 
 * 功能说明：请补充
 */
public class StringUtil {
	
	/**
	 * 生成随机字符串
	 * @param length 生成字符串的长度
	 * @param randCase 是否随机大小写
	 * @return
	 */
	public static String getRandomString(int length, boolean randCase) {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		if(randCase){
			base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		}
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
	public static String getRandomString(int length) {
		return getRandomString(length, false);
	}

	/**
	 * 显示隐藏后的字符
	 * 
	 * @param str
	 * @param start
	 * @param num
	 * @return
	 */
	public static String getHideStr(String str, int start, int num) {
		StringBuffer sb = new StringBuffer();
		byte[] b = str.getBytes();
		int end = start + num;
		if (num < 0) {
			end = b.length + end - 1;
		}

		for (int i = 0; i < b.length; i++) {
			if (i >= start && i <= end) {
				sb.append("*");
			} else {
				sb.append((char) b[i]);
			}
		}

		return sb.toString();
	}

	/**
	 * 生成option列表
	 * 
	 * @param map
	 * @return
	 */
	public static String mapToOptions(Map<Integer, String> map) {
		StringBuffer sb = new StringBuffer();

		Iterator<?> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			Integer key = (Integer) iter.next();
			String val = map.get(key);

			sb.append("<option value=\"");
			sb.append(key);
			sb.append("\">");
			sb.append(val);
			sb.append("</option>\r\n");
		}

		return sb.toString();
	}

	/**
	 * 字符串填充
	 * @param str
	 * @param fillLen
	 * @param fillStr
	 * @return
	 */
	public static String strPad(String str, int fillLen, String fillStr) {
		if (fillLen == 0)
			return str;
		int len = str.length();
		for (int i = len; i < fillLen; i++) {
			str = fillStr + str;
		}
		return str;
	}
	/**
	 * 重复字符串
	 * @param str
	 * @param count
	 * @return
	 */
	public static String strRepeat(String str, int count) {
		if (count == 0 || str==null)
			return "";
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i <= count; i++) {
			sb.append(str);
		}
		return sb.toString();
	}
	
	public static String formatRate(String sp) {
		sp = sp.replace(",", "");
		return formatRate(Double.valueOf(sp), "##0.00");
	}

	public static String formatRate(String sp, String format) {
		sp = sp.replace(",", "");
		return formatRate(Double.valueOf(sp), format);
	}

	public static String formatRate(double sp) {
		return formatRate(sp, "##0.00");
	}

	public static String formatRate(double sp, String format) {
		DecimalFormat myformat3 = new DecimalFormat();
		myformat3.applyPattern(format);
		return myformat3.format(sp);
	}

	public static String formatMoney(String money) {
		money = money.replace(",", "");
		return formatMoney(Double.valueOf(money), ",##0.00");
	}

	public static String formatMoney(String money, String format) {
		money = money.replace(",", "");
		return formatMoney(Double.valueOf(money), format);
	}

	public static String formatMoney(double money, String format) {
		String formatMoney = "0.00";
		try {
			DecimalFormat myformat3 = new DecimalFormat();
			myformat3.applyPattern(format);
			formatMoney = myformat3.format(money);
		} catch (Exception ex) {
		}
		return formatMoney;
	}

	/**
	 * 转换 以分为单位的 金额格式
	 * 
	 * @param money
	 * @param format
	 * @return
	 */
	public static String formatFenMoney(long money, String format) {
		BigDecimal b = new BigDecimal(money + "");
		b = b.divide(new BigDecimal("100")).setScale(2);
		String formatMoney = "0.00";
		if (format != null && format.length() > 0) {
			try {
				DecimalFormat myformat3 = new DecimalFormat();
				myformat3.applyPattern(format);
				formatMoney = myformat3.format(b.doubleValue());
			} catch (Exception ex) {
			}
			return formatMoney;
		} else {
			return b.toString();
		}
	}

	public static BigDecimal getYuanFormFen(long fen) {
		BigDecimal b = new BigDecimal(fen + "");
		b = b.divide(new BigDecimal("100")).setScale(2);
		return b;
	}
	public static String formatFenMoney(BigDecimal money) {
		 
		return formatFenMoney(money.setScale(0, BigDecimal.ROUND_HALF_UP).longValue(), ",##0.00");
	}
	public static String formatFenMoney(long money) {
		return formatFenMoney(money, ",##0.00");
	}

	public static String formatFenMoney(String money) {
		money = money.replace(",", "");
		return formatFenMoney(Long.parseLong(money), "");
	}

	public static String formatFenMoney(String money, String format) {
		money = money.replace(",", "");
		return formatFenMoney(Long.parseLong(money), format);
	}

	/**
	 * 将html转换为字符串
	 * 
	 * @param str
	 * @param str
	 * @return
	 */
	public static String html2js(String str) {
		str = str.replaceAll("\r\n", " ");
		str = str.replaceAll("\n", " ");
		str = str.replaceAll("\\\"", "\\\\\"");
		return str;
	}

	/**
	 * 取字符串的前toCount个字符
	 * 
	 * @param str
	 *                被处理字符串
	 * @param toCount
	 *                截取长度
	 * @param more
	 *                后缀字符串
	 */
	public static String cutStr(String str, int toCount, String more) {
		try {
			String reStr = "";
			if (str == null)
				return "";
			byte[] bte = str.getBytes("GBK");
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < bte.length && i < toCount; i++) {
				if ((bte[i] & 0xFF) > 0xa0) {
					sb.append(new String(new byte[] { bte[i], bte[i + 1] }, "GBK"));
					i++;
				} else {
					sb.append((char) bte[i]);
				}

			}
			reStr = sb.toString();
			;
			if (reStr.getBytes().length < str.getBytes().length)
				reStr += more;
			return reStr;
		} catch (Exception e) {
			return str;
		}
	}

	/**
	 * 连接字符串
	 * @param s1
	 * @param s2
	 * @param s3
	 * @return
	 */
	public static String concat(String s1, String s2, String s3) {
		return s1.concat(s2).concat(s3);
	}
	/**
	 * 连接字符串
	 * @param strings
	 * @return
	 */
	public static String concat(String... strings) {
		int len = 0;
		int i;
		final int size;
		i = size = strings.length;
		while (i != 0) {
			--i;
			len += strings[i].length();
		}
		StringBuilder sb = new StringBuilder(len);
		for (i = 0; i < size; i++) {
			sb.append(strings[i]);
		}
		return sb.toString();
	}

	/**
	 * 字符串是否为空
	 * @param cs
	 * @return
	 */
	public static boolean isEmpty(CharSequence cs) {
		return (cs == null) || (cs.length() == 0);
	}

	/**
	 * 字符串是否不为空
	 * @param cs
	 * @return
	 */
	public static boolean isNotEmpty(CharSequence cs) {
		return !isEmpty(cs);
	}
	/**
	 * 替换字符串
	 * @param buf
	 * @param searchString
	 * @param replacement
	 */
	public static void replace(StringBuilder buf, String searchString, String replacement) {
		int start = 0;
		int end = buf.indexOf(searchString, start);
		if (end == -1) {
			return;
		}
		int replLength = searchString.length();
		while (end != -1) {
			buf.replace(end, end + replLength, replacement);
			start = end + replLength;
			end = buf.indexOf(searchString, start);
		}
	}

	/**
	 * 替换字符串
	 * @param text
	 * @param searchString
	 * @param replacement
	 * @return
	 */
	public static String replace(String text, String searchString, String replacement) {
		if ((isEmpty(text)) || (isEmpty(searchString)) || (replacement == null)) {
			return text;
		}
		int start = 0;
		int end = text.indexOf(searchString, start);
		if (end == -1) {
			return text;
		}
		int replLength = searchString.length();
		int increase = replacement.length() - replLength;
		increase = increase < 0 ? 0 : increase;
		increase *= 64;
		StringBuilder buf = new StringBuilder(text.length() + increase);
		while (end != -1) {
			buf.append(text.substring(start, end)).append(replacement);
			start = end + replLength;
			end = text.indexOf(searchString, start);
		}
		buf.append(text.substring(start));
		return buf.toString();
	}
	
    /** 
     * 将一个 JavaBean 对象转化为一个  Map 
     * @param bean 要转化的JavaBean 对象 
     * @return 转化出来的  Map 对象 
     * @throws IntrospectionException 如果分析类属性失败 
     * @throws IllegalAccessException 如果实例化 JavaBean 失败 
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败 
     */  
    @SuppressWarnings({ "rawtypes", "unchecked" })  
    public static Map convertBean(Object bean)  
            throws IntrospectionException, IllegalAccessException, InvocationTargetException {  
        Class type = bean.getClass();  
        Map returnMap = new HashMap();  
        BeanInfo beanInfo=null;
		try {
			beanInfo = Introspector.getBeanInfo(type);
		} catch (java.beans.IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
  
        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();  
        for (int i = 0; i< propertyDescriptors.length; i++) {  
            PropertyDescriptor descriptor = propertyDescriptors[i];  
            String propertyName = descriptor.getName();  
            if (!propertyName.equals("class")) {  
                Method readMethod = descriptor.getReadMethod();  
                Object result = readMethod.invoke(bean, new Object[0]);  
                if (result != null) {  
                    returnMap.put(propertyName, result);  
                } 
//                else {  
//                    returnMap.put(propertyName, "");  
//                }  
            }  
        }  
        return returnMap;  
    }  
    
    /***
	 * 分转换成元 格式0.00，不带逗号
	 * @param money
	 * @return
	 */
	public static String to_yuan_edit(long money){
		if(money != 0){
			DecimalFormat myformat = new DecimalFormat("##0.00");
			return myformat.format((double)money/100);
		}else{
			return "0";
		}
	}
    
}
