package libcore.util;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * 网络操作
 */
public class NetUtil {
	protected final static Logger logger = LogManager.getLogger(NetUtil.class);
	/**
	 * 获取当前域名下的全局cookie域名
	 * 
	 * @return Str
	 */
	public static String getCookieDomain(HttpServletRequest request) {
		String host = getCurrDomain(request);
		
		if (host.indexOf(".com.cn") > 0 || host.indexOf(".net.cn") > 0 || host.indexOf(".org.cn") > 0
				|| host.indexOf(".gov.cn") > 0 || host.indexOf(".js.cn") > 0) {
			String arr[] = host.split("\\.");
			host = "." + arr[arr.length - 3] + "." + arr[arr.length - 2] + "." + arr[arr.length - 1];
		} else if (host.indexOf(".com") > 0 || host.indexOf(".net") > 0 || host.indexOf(".org") > 0
				|| host.indexOf(".gov") > 0 || host.indexOf(".cn") > 0) {
			String arr[] = host.split("\\.");
			host = "." + arr[arr.length - 2] + "." + arr[arr.length - 1];
		} else {
			host = ".huacai.com";
		}
		return host;
	}

	/**
	 * 获取当前域名
	 * 
	 * @return Str
	 */
	public static String getCurrDomain(HttpServletRequest request) {
		String host = request.getHeader("X-Forwarded-Host");
		if (null == host || host.equals("")) {
			host = request.getHeader("host");
		}
		try {
			String[] urllist = host.split(",");
			if (urllist.length > 0)
				host = urllist[0];
		} catch (Exception e) {
			logger.error("获取用户访问url错误：" + e);
		}
		if (host.indexOf(":") > 0) {
			String[] arr = host.split(":");
			host = arr[0];
		}
		return host;
	}

 

	/**
	 * 获取客户ip
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}


	/**
	 * IP转换成10位数字
	 * 
	 * @param ip
	 *                IP
	 * @return 10位数字
	 */
	public static long ip2num(String ip) {
		long ipNum = 0;
		try {
			if (ip != null) {
				if (ip.indexOf(".") != -1) {
					String ips[] = ip.split("\\.");
					for (int i = 0; i < ips.length; i++) {
						int k = Integer.parseInt(ips[i]);
						ipNum = ipNum + k * (1L << ((3 - i) * 8));
					}
				} else {
					ipNum = Integer.parseInt(ip);
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
	 * 判断当前请求是否是ajax方式
	 * @param request
	 * @return
	 */
	public static boolean IsJsonRequest(HttpServletRequest request) {
		String xReqWith = request.getHeader("X-Requested-With");
		String accept = request.getHeader("Accept");
		if(xReqWith!=null&&accept!=null&& xReqWith.equals("XMLHttpRequest")&&accept.indexOf("application/json")>-1){
			return true;
		}
		return false;
	}	

	public static void main(String[] args) throws UnsupportedEncodingException {

	}
}
