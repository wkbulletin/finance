package com.huacai.web.dao.merchant;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huacai.web.common.DbCommon;
import com.huacai.web.db.datasource.BossDB;


@Repository("listDao")
@Configurable(preConstruction=true)
public class ListDao {
	
	private static final Logger log=LogManager.getLogger(ListDao.class);
	
	@Autowired
	private BossDB bossDB;
	
	
	/**
	 * 获取商户 日统计列表
	 * @param pid
	 * @return
	 */
	public JSONObject getDayList(JSONObject queryData) {
		/**
		 * 当按条件精确查询的时候，将flag置1，flag为1的时候将触发树形结构查询
		 */
		int flag=0 ;
		String sql = " SELECT 1 px, a.CREGNAME, d.CSTATDATE, d.NNEWREGIST, d.NTOTALWIN , d.npaycount, d.NTOTALPAY,"
				   + " d.NNEWACTIVE, d.NTOTALXF  as xiaofei  FROM SHPT_AGENCYINFO a "
				   + " inner join   SHPT_DAILYSTATS d  on ( d.nagencyid = a.nagencyid)"
				   + " where 1=1 ";
		
		List<Object> param = new ArrayList<Object>();
		if(queryData!=null){
			String search_type = queryData.getString("search_type");
			if(search_type!=null&&search_type.length()>0){
				flag = 1;
				String keyword = queryData.getString("keyword");
				sql += " and a."+search_type+" = '"+keyword+"' ";
			}

			
			String begin_time =queryData.getString("begin_time");
			String end_time = queryData.getString("end_time");
			if(begin_time!=null&&begin_time.length()>0){
				
				sql += " and d.CSTATDATE >= ? ";
				
				param.add(begin_time );
			}
			if(end_time!=null&&end_time.length()>0){
				
				sql += " and d.CSTATDATE <= ? ";
				
				param.add(end_time );
			}
		}
		
		
		if(flag == 1){
			String sql1 = getDayTreeSql(queryData, true);
			String sql2 = getDayTreeSql(queryData,false);
			/**
			 * 将查询出来的父父级数据和子子级记录合并 
			 */
			sql = sql + " UNION  " + sql1 + " UNION " + sql2;
		}
		
		sql += " ORDER BY px ";
//		sql += " ORDER BY a.NAGENCYID, d.cstatdate  desc  ";
		
		JSONObject roleListData = new JSONObject();
		JSONArray jsonList = new JSONArray();
		try {
			int totalCount = bossDB.count(sql, param);
			roleListData.put("count", totalCount);
			if(totalCount>0){
				jsonList = DbCommon.field2json(bossDB.queryList(sql, param));
			}
			roleListData.put("list", jsonList);
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return roleListData;
	}
	
	
	/**
	 * 
	 * @param queryData
	 * @param flag true,查父级记录；false：查子级记录
	 * @return
	 */
	public String getDayTreeSql(JSONObject queryData, boolean flag){
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT 2 px, a.CREGNAME, d.CSTATDATE, d.NNEWREGIST, d.NTOTALWIN , d.npaycount, d.NTOTALPAY,"
				+ "  d.NNEWACTIVE, d.NTOTALXF  as xiaofei FROM SHPT_AGENCYINFO a  inner join  SHPT_DAILYSTATS d  "
				+ "  on ( d.nagencyid = a.nagencyid) where 1=1 ");
		
		if(queryData!=null){
			
			String search_type = queryData.getString("search_type");
			if(search_type!=null&&search_type.length()>0){
				String keyword = queryData.getString("keyword");
				/**
				 * 过滤查询条件本身记录
				 */
				sql.append(" and a."+search_type+" != '"+keyword+"' ");
				sql.append(" start with a.NAGENCYID=(select info.NAGENCYID from SHPT_AGENCYINFO info where info."+search_type+" = '"+keyword+"' "+")");
				if(flag){
					sql.append("connect by  prior a.NAGENCYUP= a.NAGENCYID");
				}else{
					sql.append("connect by  a.NAGENCYUP= prior  a.NAGENCYID");
				}
				
				String begin_time =queryData.getString("begin_time");
				String end_time = queryData.getString("end_time");
				if(begin_time!=null&&begin_time.length()>0){
					
					sql.append(" and d.CSTATDATE >= '"+begin_time+"'");
					
				}
				if(end_time!=null&&end_time.length()>0){
					
					sql.append(" and d.CSTATDATE <= '"+end_time+"'");
					
				}
			}
		}	
		return sql.toString();
	}
	
	/**
	 * 获取商户汇总统计列表
	 * @param pid
	 * @return
	 */
	public JSONObject getAllList(JSONObject queryData, String begin_time, String end_time) {
		/**
		 * 当按条件精确查询的时候，将flag置1，flag为1的时候将触发树形结构查询
		 */
		int flag=0 ;
		String sql = "SELECT 1 px, a.cloginname,a.CREGNAME,a.crefercode,a.ntreedeep,decode(b.s1,null,0, b.s1) s1,"
				   + " decode(b.s2,null,0, b.s2) s2, decode(b.s3,null,0, b.s3) s3,"
				   + " decode(b.s4,null,0, b.s4) s4, decode(b.s5,null,0, b.s5) s5,"
				   + " decode(b.s6,null,0, b.s6) s6  FROM SHPT_AGENCYINFO a inner join ("
				   + " select d.nagencyid as aid ,sum(d.NNEWREGIST) s1,sum(d.NNEWACTIVE) s2,sum(d.NPAYCOUNT) s3,"
				   + " sum(d.NTOTALPAY) s4, sum(d.NTOTALWIN) s5, sum(d.NTOTALXF) s6 "
				   + " from SHPT_DAILYSTATS d where d.cstatdate >=? and d.cstatdate <=? group by d.nagencyid "
				   + " ) b  on (a.nagencyid=b.aid)";
		
	
		List<Object> param = new ArrayList<Object>();
		param.add(begin_time);
		param.add(end_time);
		
		if(queryData!=null){
			String search_type = queryData.getString("search_type");
			if(search_type!=null&&search_type.length()>0){
				flag = 1;
				String keyword = queryData.getString("keyword");
				sql += " and a."+search_type+" = '"+keyword+"' ";
			}
			
			String ntreedeep = queryData.getString("ntreedeep");
			if(ntreedeep!=null&&ntreedeep.length()>0){
				sql += " and a.ntreedeep=?";
				param.add(ntreedeep);
			}

		}
		
		if(flag == 1){
			String sql1 = getMonthTreeSql(queryData, begin_time, end_time, true);
			String sql2 = getMonthTreeSql(queryData,begin_time, end_time,false);
			/**
			 * 将查询出来的父父级数据和子子级记录合并 
			 */
			sql = sql + " UNION  " + sql1 + " UNION " + sql2;
		}
		
		sql += " ORDER BY px ";
		
		JSONObject roleListData = new JSONObject();
		JSONArray jsonList = new JSONArray();
		try {
			int totalCount = bossDB.count(sql, param);
			roleListData.put("count", totalCount);
			if(totalCount>0){
				jsonList = DbCommon.field2json(bossDB.queryList(sql, param));
			}
			roleListData.put("list", jsonList);
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return roleListData;
	}
	
	/**
	 * 商户汇总统计
	 * @param queryData
	 * @param begin_time
	 * @param end_time
	 * @param flag true,查父级记录；false：查子级记录
	 * @return
	 */
	public String getMonthTreeSql(JSONObject queryData, String begin_time, String end_time,boolean flag){
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT  2 px, a.cloginname,a.CREGNAME,a.crefercode,a.ntreedeep,decode(b.s1,null,0, b.s1) s1,"
				   + " decode(b.s2,null,0, b.s2) s2, decode(b.s3,null,0, b.s3) s3,"
				   + " decode(b.s4,null,0, b.s4) s4, decode(b.s5,null,0, b.s5) s5,"
				   + " decode(b.s6,null,0, b.s6) s6  FROM SHPT_AGENCYINFO a inner join ("
				   + " select d.nagencyid as aid ,sum(d.NNEWREGIST) s1,sum(d.NNEWACTIVE) s2,sum(d.NPAYCOUNT) s3,"
				   + " sum(d.NTOTALPAY) s4, sum(d.NTOTALWIN) s5, sum(d.NTOTALXF) s6 "
				   + " from SHPT_DAILYSTATS d where d.cstatdate >= '"+begin_time+"'  and d.cstatdate <= '"+ end_time +"' group by d.nagencyid "
				   + " ) b  on (a.nagencyid=b.aid)" );
		
		
		if(queryData!=null){
			
			String search_type = queryData.getString("search_type");
			if(search_type!=null&&search_type.length()>0){
				String keyword = queryData.getString("keyword");
				sql.append(" where a."+search_type+" != '"+keyword+"' ");//过滤查询条件本身记录
				sql.append(" start with a.NAGENCYID=(select info.NAGENCYID from SHPT_AGENCYINFO info where info."+search_type+" = '"+keyword+"' "+")");
				if(flag){
					sql.append("connect by  prior a.NAGENCYUP= a.NAGENCYID");
				}else{
					sql.append("connect by  a.NAGENCYUP= prior  a.NAGENCYID");
				}
				
				String ntreedeep = queryData.getString("ntreedeep");
				if(ntreedeep!=null&&ntreedeep.length()>0){
					sql.append(" and a.ntreedeep=" +ntreedeep);
				}
				
			}
		}	
		return sql.toString();
	}
	
	/**
	 * 佣金列表
	 * @param pid
	 * @return
	 */
	public JSONObject getCommissionList(int pageSize, int curPage, String cdate) {
		String sql = " SELECT a.*, m.ccardowner,  ( select b.bankname from shpt_bankinfo b where b.bankid= "
				   + " case m.ctxbankname when m.ctxbankname then null else m.ctxbankname end ) ctxbankname, "
				   + " m.ctxbankcard,  d.NNEWREGIST as tuijian, d.NMONTHPAY as touzhu, d.NMONTHXF  as xiaofei "
				   + "  FROM SHPT_AGENCYINFO a  inner join SHPT_AGENCYMORE m on(a.nagencyid = m.nagencyid)"
				   + " left join  SHPT_MONTHSTATS d on ( d.nagencyid = a.nagencyid and d.NSTATMNTH = ? )"
				  // + " left join shpt_bankinfo b on ( b.bankid = m.ctxbankname) "
				   + " ORDER BY a.nagencyid desc";
		
		JSONObject roleListData = new JSONObject();
		JSONArray jsonList = new JSONArray();
		try {
			int totalCount = bossDB.count(sql, new Object[]{cdate});
			roleListData.put("count", totalCount);
			if(totalCount>0){
				jsonList = DbCommon.field2json(bossDB.queryList(sql, new Object[]{cdate} ,pageSize, curPage));
			}
			roleListData.put("list", jsonList);
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return roleListData;
	}
	
	/**
	 * 佣金统计
	 * @param pid
	 * @return
	 */
	public JSONObject getCommissionTotal(String cdate) {
		
		String sql = " SELECT sum(a.nnewregist) as t1, sum(a.nmonthpay) as t2, "
				   + " sum(a.NMONTHXF) as t3 FROM SHPT_MONTHSTATS a "
				   + " WHERE a.NSTATMNTH = ?";	 		
		JSONObject json = new JSONObject();
		try {
			json = DbCommon.field2json(bossDB.queryMap(sql, new Object[] {cdate}));
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return json;
	}
	
	
	/**
	 * 日统计
	 * @param pid
	 * @return
	 */
	public JSONObject getDayListTotal(JSONObject queryData) {
		int flag = 0;
		String sql = " SELECT sum(d.nnewregist) t1, sum(d.nnewactive) t2 , sum(d.NPAYCOUNT) t3, "
				   + " sum(d.ntotalpay) t4, sum(d.ntotalwin) t5 , "
				   + " sum(d.NTOTALXF )  t6  FROM SHPT_AGENCYINFO a "
				   + " inner join SHPT_DAILYSTATS d  on ( d.nagencyid = a.nagencyid)";
		
		List<Object> param = new ArrayList<Object>();
		if(queryData!=null){
			String search_type = queryData.getString("search_type");
			if(search_type!=null&&search_type.length()>0){
				flag = 1;
				String keyword = queryData.getString("keyword");
				sql += " and a."+search_type+" = '"+keyword+"' ";
			}

			
			String begin_time =queryData.getString("begin_time");
			String end_time = queryData.getString("end_time");
			if(begin_time!=null&&begin_time.length()>0){
				
				sql += " and d.CSTATDATE >= ? ";
				
				param.add(begin_time );
			}
			if(end_time!=null&&end_time.length()>0){
				
				sql += " and d.CSTATDATE <= ? ";
				
				param.add(end_time );
			}
		}	 	
		
		if(flag == 1){
			String sql1 = getDayTreeCountSql(queryData, true);
			String sql2 = getDayTreeCountSql(queryData,false);
			/**
			 * 将查询出来的父父级数据和子子级记录合并 
			 */
			sql = sql + " UNION  " + sql1 + " UNION " + sql2;
			/**
			 * 将查询出来的父父级数据和子子级记录合计 
			 */
			sql = "select sum(t1) t1 ,sum(t2) t2,sum(t3) t3,sum(t4) t4,sum(t5) t5,sum(t6) t6 from  (" +sql +") ";
		}
		
		JSONObject json = new JSONObject();
		try {
			json = DbCommon.field2json(bossDB.queryMap(sql, param));
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return json;
	}
	
	
	/**
	 * 日统计上下级查询
	 * @param queryData
	 * @param flag true,查父级记录；false：查子级记录
	 * @return
	 */
	public String getDayTreeCountSql(JSONObject queryData, boolean flag){
		StringBuilder sql = new StringBuilder();
		String begin_time =queryData.getString("begin_time");
		String end_time = queryData.getString("end_time");
		sql.append( " SELECT sum(d.nnewregist) t1, sum(d.nnewactive) t2 , sum(d.NPAYCOUNT) t3, "
				   + " sum(d.ntotalpay) t4, sum(d.ntotalwin) t5 , "
				   + " sum(d.NTOTALXF )  t6 FROM  SHPT_DAILYSTATS d  "
				   + " where d.cstatdate >= '"+begin_time+"' and d.cstatdate <= '"+end_time+"'"
				   + " and d.NAGENCYID in (select NAGENCYID from SHPT_AGENCYINFO a where  1= 1 "
				   );
		
		if(queryData!=null){
			
			String search_type = queryData.getString("search_type");
			if(search_type!=null&&search_type.length()>0){
				String keyword = queryData.getString("keyword");
				sql.append(" and a."+search_type+" != '"+keyword+"' ");//过滤查询条件本身记录
				sql.append(" start with a.NAGENCYID=(select info.NAGENCYID from SHPT_AGENCYINFO info where info."+search_type+" = '"+keyword+"' "+")");
				if(flag){
					sql.append("connect by  prior a.NAGENCYUP= a.NAGENCYID");
				}else{
					sql.append("connect by  a.NAGENCYUP= prior  a.NAGENCYID");
				}
				
				if(begin_time!=null&&begin_time.length()>0){
					
					sql.append(" and d.CSTATDATE >= '"+begin_time+"'");
					
				}
				if(end_time!=null&&end_time.length()>0){
					
					sql.append(" and d.CSTATDATE <= '"+end_time+"'");
					
				}
			}
		}	
		sql.append(")");
		return sql.toString();
	}
	
	/**
	 * 获取单条
	 * @param pid
	 * @return
	 */
	public JSONObject getAllTotal(JSONObject queryData, String begin_time, String end_time) {
		int flag = 0;
		String sql = " select sum(d.NNEWREGIST) t1,sum(d.NNEWACTIVE) t2,sum(d.NPAYCOUNT) t3,"
				   + " sum(d.NTOTALPAY) t4, sum(d.NTOTALWIN) t5, sum(d.NTOTALXF) t6 "
				   + " from SHPT_DAILYSTATS d inner join SHPT_AGENCYINFO a on(a.NAGENCYID=d.NAGENCYID) "
				   + " where d.cstatdate >=? and d.cstatdate <=? ";
		
		List<Object> param = new ArrayList<Object>();
		param.add(begin_time );
		param.add(end_time );
		
		if(queryData!=null){
			String search_type = queryData.getString("search_type");
			if(search_type!=null&&search_type.length()>0){
				flag = 1;
				String keyword = queryData.getString("keyword");
				sql += " and a."+search_type+" = '"+keyword+"' ";
			}
			
			String ntreedeep = queryData.getString("ntreedeep");
			if(ntreedeep!=null&&ntreedeep.length()>0){
				sql += " and a.ntreedeep=?";
				param.add(ntreedeep);
			}

		}
		
		if(flag == 1){
			String sql1 = getMonthTreeCountSql(queryData, begin_time, end_time, true);
			String sql2 = getMonthTreeCountSql(queryData,begin_time, end_time,false);
			/**
			 * 将查询出来的父父级数据和子子级记录合并 
			 */
			sql = sql + " UNION  " + sql1 + " UNION " + sql2;
			/**
			 * 将查询出来的父父级数据和子子级记录合计 
			 */
			sql = "select sum(t1) t1 ,sum(t2) t2,sum(t3) t3,sum(t4) t4,sum(t5) t5,sum(t6) t6 from  (" +sql +") ";

		}
		
		JSONObject json = new JSONObject();
		try {
			json = DbCommon.field2json(bossDB.queryMap(sql, param));
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return json;
	}
	
	/**
	 * 按月上下级统计
	 * flag true,查父级记录；false：查子级记录
	 */
	public String getMonthTreeCountSql(JSONObject queryData, String begin_time, String end_time,boolean flag){
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select sum(d.NNEWREGIST) t1,sum(d.NNEWACTIVE) t2,sum(d.NPAYCOUNT) t3,"
				   + " sum(d.NTOTALPAY) t4, sum(d.NTOTALWIN) t5, sum(d.NTOTALXF) t6 "
				   + " from SHPT_DAILYSTATS d   "
				   + " where d.cstatdate >= '"+begin_time+"' and d.cstatdate <= '"+end_time+"'"
				   + " and d.NAGENCYID in (select NAGENCYID from SHPT_AGENCYINFO a where  1= 1 " 
				);
		
		if(queryData!=null){
			
			String search_type = queryData.getString("search_type");
			if(search_type!=null&&search_type.length()>0){
				String keyword = queryData.getString("keyword");
				sql.append(" and a."+search_type+" != '"+keyword+"' ");//过滤查询条件本身记录
				sql.append(" start with a.NAGENCYID=(select info.NAGENCYID from SHPT_AGENCYINFO info where info."+search_type+" = '"+keyword+"' "+")");
				if(flag){
					sql.append("connect by  prior a.NAGENCYUP= a.NAGENCYID");
				}else{
					sql.append("connect by  a.NAGENCYUP= prior  a.NAGENCYID");
				}
				
				String ntreedeep = queryData.getString("ntreedeep");
				if(ntreedeep!=null&&ntreedeep.length()>0){
					sql.append(" and a.ntreedeep=" +ntreedeep);
				}
				
			}
		}	
		sql.append(")");
		return sql.toString();
	}
	
	
}
