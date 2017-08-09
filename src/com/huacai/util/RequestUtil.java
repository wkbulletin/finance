package com.huacai.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

public class RequestUtil {

	public static int getIntParam(HttpServletRequest request, String pName, int defValue) {
		int result = 0;
		if (pName == null || request == null) {
			result = defValue;
		}
		String value = request.getParameter(pName);
		try {
			result = Integer.parseInt(value);
		} catch (Exception e) {
			result = defValue;
		}
		return result;
	}

	public static Integer getIntParam(HttpServletRequest request, String pName, Integer defValue) {
		int result = 0;
		if (pName == null || request == null) {
			result = defValue;
		}
		String value = request.getParameter(pName);
		try {
			result = Integer.parseInt(value);
		} catch (Exception e) {
			result = defValue;
		}
		return result;
	}
	
	public static Integer getIntParam(HttpServletRequest request, String pName) {
		Integer result = null;
		String value = request.getParameter(pName);
		try {
			if(value != null && !value.equals("")){
				result = Integer.parseInt(value);
			}
		} catch (Exception e) {
		}
		return result;
	}
	
	public static Double getDoubleParam(HttpServletRequest request, String pName) {
		Double result = null;
		String value = request.getParameter(pName);
		try {
			if(value != null && !value.equals("")){
				result = Double.parseDouble(value);
			}
		} catch (Exception e) {
		}
		return result;
	}
	
	
	public static double getDoubleParam(HttpServletRequest request, String p, double d) {
		double result = 0;
		if (p == null || request == null) {
			result = d;
		}
		String value = request.getParameter(p);
		try {
			result = Double.parseDouble(value);
		} catch (Exception e) {
		}
		return result;
	}

	public static long getLongParam(HttpServletRequest request, String p, long d) {
		long result = 0;
		if (p == null || request == null) {
			result = d;
		}
		String value = request.getParameter(p);
		try {
			result = Long.parseLong(value);
		} catch (Exception e) {
			result = d;
		}
		return result;
	}

	public static Long getLongParam(HttpServletRequest request, String pName) {
		Long result = null;
		String value = request.getParameter(pName);
		try {
			result = Long.parseLong(value);
		} catch (Exception e) {
		}
		return result;
	}
	

	public static String getStringParam(HttpServletRequest request, String p, String d) {
		String result = "";
		if (p == null || request == null) {
			result = d;
		}
		String value = request.getParameter(p);
		result = null == value ? d : value.trim();
		return result;
	}
	
	public static String getStringParam(HttpServletRequest request, String p) {
		String value = request.getParameter(p);
		if(value == null || value.trim().equals("")) {
			return null;
		}
		return value.trim();
	}

	public static String[] getStringArrayParam(HttpServletRequest request, String p) {
		String[] result = null;
		if (p == null || request == null) {
			result = null;
		}
		result = request.getParameterValues(p);
		return result;
	}

	public static Integer[] getIntArrayParam(HttpServletRequest request, String p) {
		Integer[] result = null;
		if (p == null || request == null) {
			result = null;
		}
		String[] arr = getStringArrayParam(request, p);
		if (null != arr) {
			ArrayList<Integer> list = new ArrayList<Integer>();
			for (String str : arr) {
				try {
					list.add(Integer.parseInt(str));
				} catch (Exception e) {

				}
			}
			result = list.toArray(new Integer[0]);
		}
		return result;
	}

	public static Float[] getFloatArrayParam(HttpServletRequest request, String p) {
		Float[] result = null;
		if (p == null || request == null) {
			result = null;
		}
		String[] arr = getStringArrayParam(request, p);
		if (null != arr) {
			ArrayList<Float> list = new ArrayList<Float>();
			for (String str : arr) {
				try {
					list.add(Float.parseFloat(str));
				} catch (Exception e) {

				}
			}
			result = list.toArray(new Float[0]);
		}
		return result;
	}

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

	@SuppressWarnings("unchecked")
	public static String getUrl(HttpServletRequest request, String get) {
		String queryStr = request.getQueryString();
		String sPath = request.getServletPath();

		if (sPath == null) {
			sPath = "";
		} else {
			sPath = sPath.replace("/", "");
		}

		// System.out.println("qStr=" + qStr);
		System.out.println("sPath=" + sPath);
		String qStr = "";
		Enumeration paras = request.getParameterNames();
		if (paras != null) {
			while (paras.hasMoreElements()) {
				String name = (String) paras.nextElement();
				String value = request.getParameter(name);
				if (getContent(queryStr, name) != null) {
					if (get != null && get.length() > 0) {
						String v = getContent(get, name);
						if (v == null) {

						} else {
							value = "";
						}
					}
					if (value != null && value.length() > 0) {
						qStr += name + "=" + value + "&";
					}
				}
			}
		}
		System.out.println("qStr=" + qStr);

		return sPath + "?" + qStr;
	}

	public static void main(String[] args) {
		System.out.println(getContent("a=", "a"));
	}

	private static String getContent(String input, String para) {
		if (input.equals("") || para.equals("")) {
			return null;
		}
		String vv = null;
		StringTokenizer st = new StringTokenizer(input, "&");
		while (st.hasMoreElements()) {
			vv = st.nextToken();
			if (vv.indexOf(para) != -1 && vv.substring(0, vv.indexOf("=")).equals(para)) {
				vv = vv.substring(vv.indexOf("=") + 1);
				return vv;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, String> getParaMap(HttpServletRequest request) {
		Map<String, String> params = new HashMap<String, String>();
		Enumeration paras = request.getParameterNames();
		if (paras != null) {
			while (paras.hasMoreElements()) {
				String name = (String) paras.nextElement();
				if (name.indexOf('[') == -1) {
					String value = request.getParameter(name);
					params.put(name, value);
				}
			}
		}
		return params;
	}

	public static Map<String, String> getMapValue(Object obj) {

		Map<String, String> map = new HashMap<String, String>();

		Field[] fields = obj.getClass().getDeclaredFields();
		for (int i = 0, len = fields.length; i < len; i++) {
			String varName = fields[i].getName();
			try {
	
				boolean accessFlag = fields[i].isAccessible();

				fields[i].setAccessible(true);
	
				Object o = fields[i].get(obj);
				
				if(o != null && o instanceof Date) {
					map.put(varName, DateUtil.format((Date)o,"yyyy-MM-dd HH:mm:ss"));
				}
				else if (o != null) {
					map.put(varName, o.toString());
				}

				fields[i].setAccessible(accessFlag);
			} catch (IllegalArgumentException ex) {
				ex.printStackTrace();
			} catch (IllegalAccessException ex) {
				ex.printStackTrace();
			}
		}
		return map;
	}
}
