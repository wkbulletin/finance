/**
 * 类名      		PageUtil.java
 * 说明   			分页显示工具类
 * 创建日期 			2008-7-13
 * 作者  			davidwang
 * 版权  			huacai.cn 2008-2100 All Copyright(C) 
 */
package com.huacai.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import libcore.util.PaginationContext;


/**
 * @author Administrator
 * 
 * 
 *         功能说明：显示页面号码字符串
 */

public class PageUtil {
	protected final static Logger logger = LogManager.getLogger(PageUtil.class);

	// 当前页前后条数
	private static int pageNumLen = 3;
	
	private static int pageNumLen2 = 2;

	/**
	 * 
	 * @param url
	 * @param curPage
	 * @param totalPage
	 * @param totalNum
	 * @param params
	 *                传递的参数
	 * @return
	 */
	public static String page(String url, int curPage, int pageSize, int totalNum, HashMap<String, Object> params) {
		
		StringBuilder sb = new StringBuilder("<span class='page_total'>共 <span class='font_red'>" + totalNum + "</span> 条记录</span>");
		
		int totalPage = totalNum / pageSize + (totalNum % pageSize == 0 ? 0 : 1);
		if (totalPage == 0) {
			return "<span class='page_end'>	&lt; </span><span class='cur_page'><span>1</span></span><span class='page_end'> <span>&gt;</span> </span>";
		}
		//-----计算------------
		// 当前页左侧开始数
		int left = 0;
		// 当前页
		int center = 0;
		// 当前页右侧结束数
		int right = 0;
		// 每个元素间隔符号
		String blank = "";

		if (totalPage < 1)
			totalPage = 1;
		if (curPage < 1)
			curPage = 1;
		if (curPage > totalPage)
			curPage = totalPage;
		center = curPage;
		left = curPage - pageNumLen;
		right = curPage + pageNumLen;
		if (left < 1) {
			left = 1;
			if ((right - left) < 2 * pageNumLen) {
				right = 2 * pageNumLen + left;
				right = right > totalPage ? totalPage : right;
			}
		}
		if (right > totalPage) {
			right = totalPage;
			if ((right - left) < 2 * pageNumLen) {
				left = right - 2 * pageNumLen;
				left = left < 1 ? 1 : left;
			}
		}
		//-----计算------------

		if (curPage == 1) {
			sb.append("<span class='page_end'> <span>&lt;</span> </span>" + blank);
		} else {
			params.put("curPage", (curPage - 1) + "");
			sb.append("<span class='page_next'><a href='" + parseParam(url, params) + "'> &lt; </a></span>" + blank);
		}
		if (left > 1) {
			params.put("curPage", 1 + "");
			if (curPage == 1) {
				sb.append("<span class='cur_page'><a href='" + parseParam(url, params) + "'> 1 </a></span>" + blank);
			} else {
				sb.append("<span class='page_num'><a href='" + parseParam(url, params) + "'> 1 </a></span>" + blank);
			}
			if (left > 2)
				sb.append("<span class='page_more'>...</span>");
		}
		// 生成页数列表
		for (int i = left; i <= right; i++) {
			params.put("curPage", i + "");
			if (i == curPage) {
				sb.append("<span class='cur_page'><a href='" + parseParam(url, params) + "'>" + center + "</a></span>" + blank);
			} else
				sb.append("<span class='page_num'><a href='" + parseParam(url, params) + "'>" + i + "</a></span>" + blank);
		}
		if (right == totalPage) {
			if (curPage == totalPage) {
				sb.append("<span class='page_end'> <span>&gt;</span> </span>" + blank);
			} else {
				params.put("curPage", (curPage + 1) + "");
				sb.append("<span class='page_next'><a href='" + parseParam(url, params) + "'> &gt; </a></span>" + blank);
			}
		} else {
			if (right < totalPage - 1)
				sb.append("<span class='page_more'>...</span>");
			params.put("curPage", totalPage + "");
			sb.append("<span class='page_num'><a href='" + parseParam(url, params) + "'> " + totalPage + " </a></span>" + blank);
			params.put("curPage", (curPage + 1) + "");
			sb.append("<span class='page_next'><a href='" + parseParam(url, params) + "'> &gt; </a></span>" + blank);
		}

		return sb.toString();
	}

	/**
	 * 
	 * @param url
	 * @param curPage
	 * @param totalPage
	 * @param totalNum
	 * @param params
	 *                传递的参数
	 * @return
	 */
	public static String page2(String url, int curPage, int pageSize, int totalNum, HashMap<String, Object> params,Map<String, Object> model) {
		
		int totalPage = totalNum / pageSize + (totalNum % pageSize == 0 ? 0 : 1);
		
		StringBuilder sb = new StringBuilder("<div class=\"col-sm-6\">共 "+totalNum+" 条记录，每页 "+pageSize+" 条，分 "+totalPage+" 页</div>");
		if (totalPage <= 1) {
			sb.append("<div class=\"col-sm-6\"><div class=\"data_table_paginate\"><ul class=\"pagination\"><li class=\"paginate_button active\"><a href=\"#\">1</a></li></ul></div></div>");
			model.put("pageStr", sb.toString());
			return sb.toString();
		}
		sb.append("<div class=\"col-sm-6\"><div class=\"data_table_paginate\"><ul class=\"pagination\">");
		
		
		//-----计算------------
		// 当前页左侧开始数
		int left = 0;
		// 当前页右侧结束数
		int right = 0;

		if (totalPage < 1)
			totalPage = 1;
		if (curPage < 1)
			curPage = 1;
		if (curPage > totalPage)
			curPage = totalPage;
		left = curPage - pageNumLen2;
		right = curPage + pageNumLen2;
		if (left < 1) {
			left = 1;
			if ((right - left) < 2 * pageNumLen2) {
				right = 2 * pageNumLen2 + left;
				right = right > totalPage ? totalPage : right;
			}
		}
		if (right > totalPage) {
			right = totalPage;
			if ((right - left) < 2 * pageNumLen2) {
				left = right - 2 * pageNumLen2;
				left = left < 1 ? 1 : left;
			}
		}
		//-----计算------------

		if (curPage == 1) {
			sb.append("<li class=\"paginate_button previous disabled\"><a href=\"#\">上一页</a></li>");
		} else {
			params.put("curPage", (curPage - 1) + "");
			sb.append("<li class=\"paginate_button previous\"><a href=\"" + parseParam(url, params) + "\">上一页</a></li>");
		}
		if (left > 1) {
			params.put("curPage", 1 + "");
			if (curPage == 1) {
				sb.append("<li class=\"paginate_button active\"><a href=\"" + parseParam(url, params) + "\">1</a></li>");
			} else {
				sb.append("<li class=\"paginate_button\"><a href=\"" + parseParam(url, params) + "\">1</a></li>");
			}
		}
		// 生成页数列表
		for (int i = left; i <= right; i++) {
			params.put("curPage", i + "");
			if (i == curPage) {
				sb.append("<li class=\"paginate_button active\"><a href=\"" + parseParam(url, params) + "\">"+i+"</a></li>");
			} else
				sb.append("<li class=\"paginate_button\"><a href=\"" + parseParam(url, params) + "\">"+i+"</a></li>");
		}
		if (right == totalPage) {
			if (curPage == totalPage) {
				sb.append("<li class=\"paginate_button next disabled\"><a href=\"#\">下一页</a></li>");
			} else {
				params.put("curPage", (curPage + 1) + "");
				sb.append("<li class=\"paginate_button next\"><a href=\"" + parseParam(url, params) + "\">下一页</a></li>");
			}
		} else {
			params.put("curPage", totalPage + "");
			sb.append("<li class=\"paginate_button\"><a href=\"" + parseParam(url, params) + "\">"+totalPage+"</a></li>");
			params.put("curPage", (curPage + 1) + "");
			sb.append("<li class=\"paginate_button next\"><a href=\"" + parseParam(url, params) + "\">下一页</a></li>");
		}

		model.put("pageStr", sb.toString());
		
		return sb.toString();
	}
	/**
	 * 
	 * @param url
	 * @param curPage
	 * @param totalPage
	 * @param totalNum
	 * @param params
	 *                传递的参数
	 * @return
	 */
	public static String page2(String url, long curPage, long pageSize, long totalNum, HashMap<String, Object> params) {
		
		long totalPage = totalNum / pageSize + (totalNum % pageSize == 0 ? 0 : 1);
		
		StringBuilder sb = new StringBuilder("<div class=\"col-sm-6\">共 "+totalNum+" 条记录，每页 "+pageSize+" 条，分 "+totalPage+" 页</div>");
		if (totalPage <= 1) {
			sb.append("<div class=\"col-sm-6\"><div class=\"data_table_paginate\"><ul class=\"pagination\"><li class=\"paginate_button active\"><a href=\"#\">1</a></li></ul></div></div>");
			return sb.toString();
		}
		sb.append("<div class=\"col-sm-6\"><div class=\"data_table_paginate\"><ul class=\"pagination\">");
		
		
		//-----计算------------
		// 当前页左侧开始数
		long left = 0;
		// 当前页右侧结束数
		long right = 0;

		if (totalPage < 1)
			totalPage = 1;
		if (curPage < 1)
			curPage = 1;
		if (curPage > totalPage)
			curPage = totalPage;
		left = curPage - pageNumLen2;
		right = curPage + pageNumLen2;
		if (left < 1) {
			left = 1;
			if ((right - left) < 2 * pageNumLen2) {
				right = 2 * pageNumLen2 + left;
				right = right > totalPage ? totalPage : right;
			}
		}
		if (right > totalPage) {
			right = totalPage;
			if ((right - left) < 2 * pageNumLen2) {
				left = right - 2 * pageNumLen2;
				left = left < 1 ? 1 : left;
			}
		}
		//-----计算------------

		if (curPage == 1) {
			sb.append("<li class=\"paginate_button previous disabled\"><a href=\"#\">上一页</a></li>");
		} else {
			params.put(PaginationContext.PAGE_NUM, (curPage - 1) + "");
			sb.append("<li class=\"paginate_button previous\"><a href=\"" + parseParam(url, params) + "\">上一页</a></li>");
		}
		if (left > 1) {
			params.put(PaginationContext.PAGE_NUM, 1 + "");
			if (curPage == 1) {
				sb.append("<li class=\"paginate_button active\"><a href=\"" + parseParam(url, params) + "\">1</a></li>");
			} else {
				sb.append("<li class=\"paginate_button\"><a href=\"" + parseParam(url, params) + "\">1</a></li>");
			}
		}
		// 生成页数列表
		for (long i = left; i <= right; i++) {
			params.put(PaginationContext.PAGE_NUM, i + "");
			if (i == curPage) {
				sb.append("<li class=\"paginate_button active\"><a href=\"" + parseParam(url, params) + "\">"+i+"</a></li>");
			} else
				sb.append("<li class=\"paginate_button\"><a href=\"" + parseParam(url, params) + "\">"+i+"</a></li>");
		}
		if (right == totalPage) {
			if (curPage == totalPage) {
				sb.append("<li class=\"paginate_button next disabled\"><a href=\"#\">下一页</a></li>");
			} else {
				params.put(PaginationContext.PAGE_NUM, (curPage + 1) + "");
				sb.append("<li class=\"paginate_button next\"><a href=\"" + parseParam(url, params) + "\">下一页</a></li>");
			}
		} else {
			params.put(PaginationContext.PAGE_NUM, totalPage + "");
			sb.append("<li class=\"paginate_button\"><a href=\"" + parseParam(url, params) + "\">"+totalPage+"</a></li>");
			params.put(PaginationContext.PAGE_NUM, (curPage + 1) + "");
			sb.append("<li class=\"paginate_button next\"><a href=\"" + parseParam(url, params) + "\">下一页</a></li>");
		}

		return sb.toString();
	}
	/**
	 * 组装URL
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String parseParam(String url, Map<String, Object> params) {
		if (null == url) {
			return null;
		}
		int type = 0;
		if (url.endsWith("?")) {
			type = 0;
		} else if (url.indexOf('?') > -1) {
			type = 1;
		} else
			type = 2;
		StringBuilder sb = new StringBuilder(url);
		int i = 0;
		for (String key : params.keySet()) {
			switch (type) {
			case 0:
				if (i != 0) {
					sb.append("&");
				}
				i++;
				break;
			case 1:
				sb.append("&");
				i++;
				break;
			case 2:
				if (i == 0) {
					sb.append("?");
				} else {
					sb.append("&");
				}
				i++;
				break;
			}
			sb.append(key);
			sb.append("=");
			// decode 参数值
			Object v = params.get(key);
			if(v != null){
				sb.append(encode(v.toString()));
			}
			
		}
		return sb.toString();
	}

	private static String encode(String value) {
		// ....
		if (null == value)
			return "";
		else
			try {
				value = URLEncoder.encode(value, "utf-8");
			} catch (UnsupportedEncodingException e) {
				logger.error("", e);
			}
		return value;
	}
	public static void main(String[] argc) {
		System.out.println();
	}
	/**
	 * 生成下来选择
	 * selected  为-100是显示 全部，当为-200时没有全部选项 
	 * isAll 为false 表示 无全部 为true 有全部
	 */
	public  static String  getSelectInfo(int selected,String paras,Map<Integer, String> mapInfo,String width,boolean isAll) {
		String tmp =null;
		String  selected_="";
		if(width==null||width.length()==0)
			width="150";
		Set<Integer>  lotterySet= mapInfo.keySet();
	    Iterator<Integer> iterator=lotterySet.iterator();
	    StringBuffer sb=new StringBuffer("<select name=\"");
	    sb.append(paras).append("\"  id=\"").append(paras).append("\"   style=\"width:").append(width).append("px\" class=\"form-control\">");
	    int i=0;
			while (iterator.hasNext()) {
				if(i==0&&isAll){
					sb.append("<option value=\"").append("\"");
				    sb.append(" >");
	                sb.append("全部</option>");
				}else{
					Integer integer = (Integer) iterator.next();
				       sb.append("<option value=\"").append(integer.toString()).append("\"");
				       if(selected==integer.intValue()){
				    	   sb.append("  selected   >");
				       }else{
				    	   sb.append(" >");
				       }
	                   sb.append(mapInfo.get(integer)).append("</option>");
				}
                   i++;
			}
			sb.append("</select> ");
		return sb.toString();
	}

}
