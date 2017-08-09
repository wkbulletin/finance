package com.huacai.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


/**
 * 字符串处理
 * 
 */
public class StringUtil {
	
	/**
	 * 在字符串头部添加字符，使原字符串达到指定的长度
	 * 
	 * @param source
	 *            源字符串
	 * @param filling
	 *            填充字符
	 * @param lastLen
	 *            填充后的总长度
	 * @return 如果源字符串长度大于lastLen，返回原字符串，否则用filling填充源字符串后返回结果
	 */
	public static String fillString(String source, char filling, int lastLen) {
		StringBuffer temp = new StringBuffer();
		if (source.length() < lastLen) {
			int fillLen = lastLen - source.length();
			for (int i = 0; i < fillLen; i++) {
				temp.append(filling);
			}
		}
		temp.append(source);
		return temp.toString();
	}

	/**
	 * 在字符串头部添加字符，使原字符串达到指定的长度
	 * 
	 * @param source
	 *            源字符串
	 * @param filling
	 *            填充字符
	 * @param lastLen
	 *            填充后的总长度
	 * @return 如果源字符串长度大于lastLen，返回原字符串，否则用filling填充源字符串后返回结果
	 */
	public static String fillString(int source, char filling, int lastLen) {
		return fillString(String.valueOf(source), filling, lastLen);
	}

	/**
	 * 在字符串头部添加字符，使原字符串达到指定的长度
	 * 
	 * @param source
	 *            源字符串
	 * @param filling
	 *            填充字符
	 * @param lastLen
	 *            填充后的总长度
	 * @return 如果源字符串长度大于lastLen，返回原字符串，否则用filling填充源字符串后返回结果
	 */
	public static String fillStringRight(String source, char filling, int lastLen) {
		StringBuffer temp = new StringBuffer();
		temp.append(source);
		if (source.length() < lastLen) {
			int fillLen = lastLen - source.length();
			for (int i = 0; i < fillLen; i++) {
				temp.append(filling);
			}
		}
		return temp.toString();
	}

	/**
	 * 格式化一个数字字符串为9，999，999.99的格式,如果字符串无法格式化返回0.00
	 * 
	 * @param money
	 *            数字字符串
	 * @return 格式化好的数字字符串
	 */
	public static String formatMoney(String money) {
		String formatMoney = "0.00";
		try {
			DecimalFormat myformat3 = new DecimalFormat();
			myformat3.applyPattern(",##0.00");
			double n = Double.parseDouble(money);
			formatMoney = myformat3.format(n);
		} catch (Exception ex) {
		}
		return formatMoney;
	}

	/***
	 * 分转换成元
	 * @param money
	 * @return
	 */
	public static String to_yuan(long money){
		if(money != 0){
			DecimalFormat myformat = new DecimalFormat("￥,##0.00");
			return myformat.format((double)money/100);
		}else{
			return "0";
		}
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
	/***
	 * 元转换为分，返回long
	 * @param money
	 * @return
	 */
	public static long to_fen(String money){
		Long result = null;
		if(!money.isEmpty()){
			result = new BigDecimal(money).multiply(new BigDecimal(100)).longValue();
		}
		return result;
	}
	/**
	 * 取随机数
	 * 
	 * @param max
	 *            最大值
	 * @return 随机数
	 */
	public static int random(int max) {
		Random ran;
		ran = new Random();
		int out = ran.nextInt(max);
		return out;
	}

	/**
	 * 从右边截取指定长度的字符串
	 * 
	 * @param src
	 *            源字符串
	 * @param len
	 *            长度
	 * @return 截取后的字符串
	 */
	public static String right(String src, int len) {
		String dest = src;
		if (src != null) {
			if (src.length() > len) {
				dest = src.substring(src.length() - len);
			}
		}
		return dest;
	}

	/**
	 * IP转换成10位数字
	 * 
	 * @param ip
	 *            IP
	 * @return 10位数字
	 */
	public static long ip2num(String ip) {
		long ipNum = 0;
		try {
			if (ip != null) {
				String ips[] = ip.split("\\.");
				for (int i = 0; i < ips.length; i++) {
					int k = Integer.parseInt(ips[i]);
					ipNum = ipNum + k * (1L << ((3 - i) * 8));
				}
			}
		} catch (Exception e) {
		}
		return ipNum;
	}

	// 将十进制整数形式转换成127.0.0.1形式的ip地址
	public static String num2ip(long longIp) {
		StringBuffer sb = new StringBuffer("");
		// 直接右移24位
		sb.append(String.valueOf((longIp >>> 24)));
		sb.append(".");
		// 将高8位置0，然后右移16位
		sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
		sb.append(".");
		// 将高16位置0，然后右移8位
		sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));
		sb.append(".");
		// 将高24位置0
		sb.append(String.valueOf((longIp & 0x000000FF)));
		return sb.toString();
	}

	/**
	 * 转化为页面时间控间形式的字符串
	 * 
	 * @param source
	 *            源字符串
	 * @return
	 */
	public static String getJsDateString(String source) {
		String result = null;
		if (source != null) {
			if (source.length() >= 8) {
				result = source.substring(0, 4) + "-" + source.substring(4, 6) + "-" + source.substring(6, 8);
			}
		}
		return result;
	}

	/**
	 * 转化js时间为整型
	 * 
	 * @param source
	 * @return
	 */
	public static int getDateString(String source) {
		int result = 0;
		if (source != null && !source.equals("")) {
			try {
				result = Integer.parseInt(source.replaceAll("-", ""));
			} catch (Exception e) {

			}
		}
		return result;
	}

	public static String getRandomNum(int num) {
		char[] c = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < num; i++) {
			sb.append(c[Math.abs(random.nextInt()) % c.length]);
		}
		return sb.toString();
	}

	/**
	 * 生成随机登录密码
	 * @return
	 */
	public static String getRandomNum() {
		char[] c = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 6; i++) {
			sb.append(c[Math.abs(random.nextInt()) % c.length]);
		}
		return sb.toString();
	}
	
	/*
	 * 返回对应的采种编号对应的中文说明
	 */
	public static String getPrizeStyle(int lottNum) {
		String back = "";
		if (lottNum == 101) {
			back = "双色球：01,02,03,04,05,06#01";
		} else if (lottNum == 102) {
			back = "七乐彩：01,02,03,04,05,06,07#08";
		} else if (lottNum == 103) {
			back = "福彩3D：1,2,3";
		} else if (lottNum == 104) {
			back = "时时乐：1,2,3";
		} else if (lottNum == 105) {
			back = "天天彩选4：1,2,3,4";
		} else if (lottNum == 106) {
			back = "15选5：01,02,03,04,05";
		} else if (lottNum == 107) {
			back = "东方6+1：1,2,3,4,5,6#鼠，特殊号码分别为：鼠、牛、虎、兔、龙、蛇、马、羊、猴、鸡、狗、猪";
		} else if (lottNum == 108) {
			back = "开乐彩";
		} else if (lottNum == 145) {
			back = "时时彩：1,2,3,4,5";
		} else if (lottNum == 146) {
			back = "排列三：1,2,3";
		} else if (lottNum == 147) {
			back = "排列五：1,2,3,4,5";
		} else if (lottNum == 148) {
			back = "七星彩：1,2,3,4,5,6,7";
		} else if (lottNum == 149) {
			back = "超级大乐透：01,02,03,04,05|01,02";
		} else if (lottNum == 150) {
			back = "十一运夺金：01,02,03,04,05";
		} 
		
		return back;
	}
	
	
	public static String arrayToStr(String[] oldStr){
		String result = "";
		for(int i=0;i<oldStr.length;i++){
			result += oldStr[i]+",";
		}
		result = result.substring(0,result.length()-1);
		return result;
	}


	// 字符串填充
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
	 * 取字符串的前toCount个字符
	 * 
	 * @param str
	 *                被处理字符串
	 * @param toCount
	 *                截取长度
	 * @param more
	 *                后缀字符串
	 * @version 2013-10-29
	 * @author Andyfoo
	 * @return String
	 */
	public static String CutStr(String str, int toCount, String more) {
		try {
			String reStr = "";
			if (str == null)
				return "";
			byte[] bte = str.getBytes("GBK");
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < bte.length && i < toCount; i++) {
				if((bte[i] & 0xFF) > 0xa0){
					sb.append(new String(new byte[]{bte[i], bte[i+1]}, "GBK"));
					i++;
				}else{
					sb.append((char)bte[i]);
				}
				
			}
			reStr = sb.toString();;
			if (reStr.getBytes().length < str.getBytes().length)
				reStr += more;
			return reStr;
		} catch (Exception e) {
			return str;
		}
	}

	/**
	 * @param num
	 *            转化数值
	 * @param moren
	 *            默认数值
	 * @return
	 */
	public static int convertInt(String snum, int moren) {
		int num = moren;
		try {
			num = Integer.parseInt(snum);
		} catch (NumberFormatException e) {
			return moren;
		}
		return num;
	}
	
	/**
	 *是否数字
	 *数字返回true
	 */
    public static boolean isNum(String str){
    	//Pattern pattern = Pattern.compile("[0-9]*");
    	Pattern pattern = Pattern.compile("([0-9]+[0-9]*|0*)(\\.[\\d]+)?");
    	//
    	Matcher isNum = pattern.matcher(str);
    	if(!isNum.matches() ){
    	     return false;
    	}
    	return true;	
    }
    
    public static String getDbSafeStr(String str) {
		if (str == null)
			return "";
		str = StringUtils.replace(str, "'", "‘");
		
		str = str.replaceAll("<", "＜");
		str = str.replaceAll(">", "＞");
		
		return str;
	}
    
    /**
	 * 鸿讯的加码
	 * 
	 * @param bsrc
	 * @return d
	 */
	public static String enCode(byte bsrc[]) {
		StringBuffer dest = new StringBuffer(1024);
		if (bsrc == null)
			return "";
		String str;
		byte bb;
		int num;
		for (int ii = 0; ii < bsrc.length; ii++) {
			bb = bsrc[ii];
			if (bb >= 0)
				num = bb;
			else
				num = (bb & 0x7f) + 128;
			str = Integer.toHexString(num);
			if (str.length() < 2)
				str = "0" + str;
			dest.append(str);
		}
		return dest.toString().toUpperCase();
	}

	/**
	 * 鸿讯的解码
	 * 
	 * @param src
	 * @return s
	 */
	public static byte[] deCode(String src) {
		// 还原
		if (src.length() < 2)
			return new byte[0];
		byte[] dest = new byte[src.length() / 2];
		byte rb;
		String str;
		Arrays.fill(dest, (byte) 0);
		int index = 0;
		for (int ii = 0; ii < src.length() - 1; ii++) {
			str = "#" + src.substring(ii, ii + 2);
			rb = (byte) Integer.decode(str).intValue();
			dest[index++] = rb;
			ii++;
		}
		return dest;
	}

	public static BigDecimal getYuanFormFen(long fen) {
		BigDecimal b = new BigDecimal(fen + "");
		b = b.divide(new BigDecimal("100")).setScale(2);
		return b;
	}
	
	/**
     * 将分为单位的转换为元 （除100)
     * 
     * ＠param amount
     * ＠return
     * ＠throws Exception 
     */ 
    public static String changeFToWY(String amount) throws Exception{ 
        if(!isNum(amount)) { 
            throw new Exception("金额格局有误"); 
        } 
        return BigDecimal.valueOf(Long.valueOf(amount)).divide(new BigDecimal(10000000)).toString(); 
    }  
    
    public static String formatFenMoney(long money) {
		return formatFenMoney(money, ",##0.00");
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
	
	public static String escapeSQLLike(String likeStr) {
		if (likeStr == null)
			return "";
		String str = likeStr;
		str = StringUtils.replace(str, "/", "//");
		str = StringUtils.replace(str, "_", "/_");
		str = StringUtils.replace(str, "%", "/%");
		str = StringUtils.replace(str, "％", "/％");
		str = StringUtils.replace(str, "'", "‘");

		//str = StringUtils.replace(str, "?", "_");
		//str = StringUtils.replace(str, "*", "%");
		return str;
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
	
	public static boolean isMatch(int[] arr, int state) {
		boolean boo = false;
		for (int tmp : arr) {
			if (tmp == state) {
				boo = true;
				break;
			}
		}
		return boo;
	}
	
	/**
	 * 返回地区编码
	 * @param response
	 * @param json
	 */
	public static int getAreaCode( int areaId_1, int areaId_2,int areaId_3) { 
		  int areaCode=-1;
			if(areaId_1==990000&&areaId_2>0){
				areaCode=areaId_2;
			}else{
			       if(areaId_1!=-1&&areaId_2==-1&&areaId_3==-1){
						areaCode=areaId_1/10000;
					}else if(areaId_1!=-1&&areaId_2!=-1&&areaId_3==-1){
						areaCode=areaId_2/100;
					}else if(areaId_1!=-1&&areaId_2!=-1&&areaId_3!=-1){
						areaCode=areaId_3;
					}else if(areaId_1==-1&&areaId_2==-1&&areaId_3==-1){
						areaCode=-1;
					}
			}
		return areaCode;
	}
	
	 /**
     * 返回两个JsonArray的合并后的字符串
     * @param mData
     * @param array
     * @return
     */
    public static String joinJSONArray(JSONArray mData, JSONArray array) {
        StringBuffer buffer = new StringBuffer();
        try {
          int len = mData.size();
          for (int i = 0; i < len; i++) {
            JSONObject obj1 = (JSONObject) mData.get(i);
            if (i == len - 1)
              buffer.append(obj1.toString());
            else
              buffer.append(obj1.toString()).append(",");
          }
          len = array.size();
          if (len > 0)
            buffer.append(",");
          for (int i = 0; i < len; i++) {
            JSONObject obj1 = (JSONObject) array.get(i);
            if (i == len - 1)
              buffer.append(obj1.toString());
            else
              buffer.append(obj1.toString()).append(",");
          }
//          buffer.insert(0, "[").append("]");
          return buffer.toString();
        } catch (Exception e) {
        }
        return null;
      }
	 public static String replaceBlank(String str) {
	        String dest = "";
	        if (str!=null) {
	            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	            Matcher m = p.matcher(str);
	            dest = m.replaceAll("");
	        }
	        return dest;
	    }
	public static void main(String[] args) {
//		 ArrayUtils.contains(Constants.LOW_FREQUENCY_LOT, 101);
//		JSONArray a1=new JSONArray();
//		a1={"12":"11"};
//		System.out.println(joinJSONArray());
	}
}
