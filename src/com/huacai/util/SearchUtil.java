package com.huacai.util;


import java.util.Calendar;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class SearchUtil {

	/**
	 * 搜索条件 按周 、月、年
	 * @yearNum 前N年的数据
	 * @return
	 */
	public static JSONArray getSearch(int yearNum){

		Calendar cal = Calendar.getInstance();
		JSONArray month=new JSONArray();
		int monthnum = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);
		int preYear = year -yearNum;
		
		
		for( int i=year ; i>=preYear; i--){
			for(int j=12; j>=1; j--){
				if(i == year ){
					if(j >= monthnum){
						continue;
					}
				}
				JSONObject item =new JSONObject();
				String code = i +""+fillPreZero(j);
				String name = i +"-"+j;
				
				item.put("code", code );
				item.put("name", name);
				
				month.add(item);
			}	
		}
		return month;
	}
	
	private static String fillPreZero(int i) {
		String a = "";
		if(i > 9){
			a = "" + i;
		}else{
			a = "0" + i;
		}
		return a;
	}
	
	public static void main(String[] args){
		JSONArray js = getSearch(1);
		
		System.out.println(js.size());
	}
}
