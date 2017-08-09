package com.huacai.util;

import java.util.HashMap;
/**
 * 查询账户流水数据,
 * @author zhangjian
 *
 */
public  class QueryUcmBillList{
	public static final int QUERYTYPE_ALL = 0;     // 全部
	public static final int QUERYTYPE_DEPOSIT = 1; // 充值数据
	public static final int QUERYTYPE_DRAW = 2;    // 提现数据
	public static final int QUERYTYPE_LOTTERY = 3; // 购彩数据
	public static final int QUERYTYPE_WIN = 4;     // 中奖数据
	
	public  static HashMap<Integer,String> QUERYTYPEMAP = new HashMap<Integer,String>();
	static {
		QUERYTYPEMAP.put(QUERYTYPE_ALL, "全部");
		QUERYTYPEMAP.put(QUERYTYPE_DEPOSIT, "充值数据");
		QUERYTYPEMAP.put(QUERYTYPE_DRAW, "提现数据");
		QUERYTYPEMAP.put(QUERYTYPE_LOTTERY, "购彩数据");
		QUERYTYPEMAP.put(QUERYTYPE_WIN, "中奖数据");
	}
	private  String memberId;    //会员编码
	private  int queryType = -1; //查询类型
	private  int pageIndex = 1;  //当前页数
	private  int pageSize = 15;  //每页条数
	private  String beginDate;   //开始时间 ，  时间 格式为: yyyy-MM-dd
	private  String endDate;     //截止时间 ，  时间 格式为: why-MM-dd
	
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public int getQueryType() {
		return queryType;
	}
	public void setQueryType(int queryType) {
		this.queryType = queryType;
	}
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}
