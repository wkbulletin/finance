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

/**
 *  导出操作sql 同listDao中sql， 只是在原先基础上加入导出条数限制
 * @author dell
 *
 */
@Repository("exportDao")
@Configurable(preConstruction=true)
public class ExportDao {
	
	private static final Logger log=LogManager.getLogger(ExportDao.class);
	
	@Autowired
	private BossDB bossDB;
	
	
	/**
	 * 获取商户 日统计列表
	 * @param pid
	 * @return
	 */
	public JSONObject getDayList(int pageSize, int curPage, JSONObject queryData) {
		int maxNum = 10000;
		int flag=0 ;
		String sql = " SELECT 1 px, a.CREFERCODE ,a.CREGNAME, d.CSTATDATE, d.NNEWREGIST, d.NTOTALWIN/100.0  as  NTOTALWIN ,d.npaycount, "
				   + " d.NTOTALPAY/100.0  as NTOTALPAY, d.NNEWACTIVE, d.NTOTALXF/100.0  as xiaofei FROM SHPT_AGENCYINFO a "
				   + " inner join   SHPT_DAILYSTATS d  on ( d.nagencyid = a.nagencyid)";
		
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
			
			sql = sql + " UNION  " + sql1 + " UNION " + sql2;
		}
		
		sql += " ORDER BY px, CREFERCODE,CSTATDATE ";
		
		JSONObject roleListData = new JSONObject();
		JSONArray jsonList = new JSONArray();
		try {
			int totalCount = bossDB.count(sql,param);
			if(totalCount<=maxNum){
				roleListData.put("count", totalCount);
				if(totalCount>0){
					jsonList = DbCommon.field2json(bossDB.queryList(sql, param, maxNum));
				}
				roleListData.put("list", jsonList);
			}else{
				roleListData.put("count", -1);
			}
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return roleListData;
	}
	
	public String getDayTreeSql(JSONObject queryData, boolean flag){
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT 2 px,a.CREFERCODE , a.CREGNAME, d.CSTATDATE, d.NNEWREGIST, d.NTOTALWIN/100.0 as NTOTALWIN , d.npaycount,"
				+ "  d.NTOTALPAY/100.0 as NTOTALPAY,  d.NNEWACTIVE, d.NTOTALXF/100.0  "
				+ "  as xiaofei FROM SHPT_AGENCYINFO a   inner join   SHPT_DAILYSTATS d  "
				+ "  on ( d.nagencyid = a.nagencyid) where 1=1 ");
		
		if(queryData!=null){
			
			String search_type = queryData.getString("search_type");
			if(search_type!=null&&search_type.length()>0){
				String keyword = queryData.getString("keyword");
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
		int maxNum = 10000;
		int flag=0 ;
		String sql = "SELECT 1 px, a.cloginname,a.CREGNAME,a.crefercode,a.ntreedeep,decode(b.s1,null,0, b.s1) s1,"
				   + " decode(b.s2,null,0, b.s2) s2, decode(b.s3,null,0, b.s3) s3,"
				   + " decode(b.s4,null,0, b.s4/100.0) s4, decode(b.s5,null,0, b.s5/100.0) s5,"
				   + " decode(b.s6,null,0, b.s6/100.0) s6  FROM SHPT_AGENCYINFO a inner join ("
				   + " select d.nagencyid as aid ,sum(d.NNEWREGIST) s1,sum(d.NNEWACTIVE) s2,sum(d.NPAYCOUNT) s3,"
				   + " sum(d.NTOTALPAY) s4, sum(d.NTOTALWIN) s5, sum(d.NTOTALXF) s6 "
				   + " from SHPT_DAILYSTATS d where d.cstatdate >=? and d.cstatdate <=? group by d.nagencyid "
				   + " ) b  on (a.nagencyid=b.aid)";
		
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
			String sql1 = getMonthTreeSql(queryData, begin_time, end_time, true);
			String sql2 = getMonthTreeSql(queryData,begin_time, end_time,false);
			
			sql = sql + " UNION  " + sql1 + " UNION " + sql2;
		}
		
		sql += " ORDER BY px,crefercode ";
		
		JSONObject roleListData = new JSONObject();
		JSONArray jsonList = new JSONArray();
		try {
			int totalCount = bossDB.count(sql,param);
			if(totalCount<=maxNum){
				roleListData.put("count", totalCount);
				if(totalCount>0){
					jsonList = DbCommon.field2json(bossDB.queryList(sql, param, maxNum));
				}
				roleListData.put("list", jsonList);
			}else{
				roleListData.put("count", -1);
			}
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return roleListData;
	}
	
	public String getMonthTreeSql(JSONObject queryData, String begin_time, String end_time,boolean flag){
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT  2 px, a.cloginname,a.CREGNAME,a.crefercode,a.ntreedeep,decode(b.s1,null,0, b.s1) s1,"
				   + " decode(b.s2,null,0, b.s2) s2, decode(b.s3,null,0, b.s3) s3,"
				   + " decode(b.s4,null,0, b.s4/100.0) s4, decode(b.s5,null,0, b.s5/100.0) s5,"
				   + " decode(b.s6,null,0, b.s6/100.0) s6  FROM SHPT_AGENCYINFO a inner join ("
				   + " select d.nagencyid as aid ,sum(d.NNEWREGIST) s1,sum(d.NNEWACTIVE) s2,sum(d.NPAYCOUNT) s3,"
				   + " sum(d.NTOTALPAY) s4, sum(d.NTOTALWIN) s5, sum(d.NTOTALXF) s6 "
				   + " from SHPT_DAILYSTATS d where d.cstatdate >= '"+begin_time+"'  and d.cstatdate <= '"+ end_time +"' group by d.nagencyid "
				   + " ) b  on (a.nagencyid=b.aid)" );
		
		
		if(queryData!=null){
			
			String search_type = queryData.getString("search_type");
			if(search_type!=null&&search_type.length()>0){
				String keyword = queryData.getString("keyword");
				sql.append(" where a."+search_type+" != '"+keyword+"' ");
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
	 * 获取列表
	 * @param pid
	 * @return
	 */
	public JSONObject getCommissionList(String cdate) {
		int maxNum = 10000;
		String sql = " SELECT a.cregname, a.crefercode, a.nnuserrate, d.nfixedrate, decode (d.ESUMWHICH, 1, decode(d.ERATEMODE, 1, d.NFIXEDRATE/100.0, d.CCALCRULES/100.0), '') ratea"
				   + " , decode (d.ESUMWHICH, 2, decode(d.ERATEMODE, 1, d.NFIXEDRATE/100.0, d.CCALCRULES/100.0),'' ) rateb,"
				   + "  m.ccardowner, ( select b.bankname from shpt_bankinfo b where b.bankid= "
				   + " case m.ctxbankname when m.ctxbankname then null else m.ctxbankname end ) ctxbankname,"
				   + " ar.AREANAME n1, ae.AREANAME n2,  m.ctxbankcard,  d.NNEWREGIST as tuijian, d.NMONTHPAY/100.0 as touzhu,"
				   + " d.NMONTHXF/100.0 as xiaofei "
				   + "  FROM SHPT_AGENCYINFO a  inner join SHPT_AGENCYMORE m on(a.nagencyid = m.nagencyid)"
				   + " left join  SHPT_MONTHSTATS d on ( d.nagencyid = a.nagencyid and d.NSTATMNTH = ? )"
				   + " left join shpt_area ar on (ar.AREACODE = m.CTXBANKPROV)"
				   + " left join shpt_area ae on (ae.AREACODE = m.CTXBANKCITY)"
				   + " ORDER BY a.nagencyid desc";
		
		JSONObject roleListData = new JSONObject();
		JSONArray jsonList = new JSONArray();
		try {
			int totalCount = bossDB.count(sql, new Object[]{cdate});
			if(totalCount<=maxNum){
				roleListData.put("count", totalCount);
				if(totalCount>0){
					jsonList = DbCommon.field2json(bossDB.queryList(sql, new Object[]{cdate} ,maxNum));
				}
				roleListData.put("list", jsonList);
			}else{
				roleListData.put("count", -1);
			}
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return roleListData;
	}
	
	/**
	 * 获取单条
	 * @param pid
	 * @return
	 */
	public JSONObject getCommissionTotal(String cdate) {
		
		String sql = " SELECT sum(a.nnewregist) as t1, sum(a.nmonthpay)/100.0 as t2, "
				   + " sum(a.NMONTHXF)/100.0 as t3 FROM SHPT_MONTHSTATS a "
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
	 * 获取单条
	 * @param pid
	 * @return
	 */
	public JSONObject getDayListTotal(JSONObject queryData) {
		int flag = 0;
		String sql = " SELECT sum(d.nnewregist) t1, sum(d.nnewactive) t2 , sum(d.NPAYCOUNT) t3, "
				   + " sum(d.ntotalpay)/100.0 t4, sum(d.ntotalwin)/100.0 t5 , "
				   + " sum(d.NTOTALXF)/100.0  t6  FROM SHPT_AGENCYINFO a "
				   + " inner join SHPT_DAILYSTATS d  on ( d.nagencyid = a.nagencyid) "
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
			String sql1 = getDayTreeCountSql(queryData, true);
			String sql2 = getDayTreeCountSql(queryData,false);
			
			sql = sql + " UNION  " + sql1 + " UNION " + sql2;
			
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
	
	public String getDayTreeCountSql(JSONObject queryData, boolean flag){
		StringBuilder sql = new StringBuilder();
		
		sql.append( " SELECT sum(d.nnewregist) t1, sum(d.nnewactive) t2 , sum(d.NPAYCOUNT) t3, "
				   + " sum(d.ntotalpay)/100.0  t4, sum(d.ntotalwin)/100.0  t5 , "
				   + " sum(d.NTOTALXF)/100.0 t6  FROM SHPT_AGENCYINFO a "
				   + " inner join SHPT_DAILYSTATS d  on ( d.nagencyid = a.nagencyid)");
		
		if(queryData!=null){
			
			String search_type = queryData.getString("search_type");
			if(search_type!=null&&search_type.length()>0){
				String keyword = queryData.getString("keyword");
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
	 * 获取单条
	 * @param pid
	 * @return
	 */
	public JSONObject getAllTotal(JSONObject queryData, String begin_time, String end_time) {
		int flag = 0;
		String sql = " select sum(d.NNEWREGIST) t1,sum(d.NNEWACTIVE) t2,sum(d.NPAYCOUNT) t3,"
				   + " sum(d.NTOTALPAY)/100.0 t4, sum(d.NTOTALWIN)/100.0 t5, "
				   + " sum(d.NTOTALXF)/100.0  t6 "
				   + " from SHPT_DAILYSTATS d inner join SHPT_AGENCYINFO a on(a.NAGENCYID=d.NAGENCYID) "
				   + " where d.cstatdate >=? and d.cstatdate <=? ";
		
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
			String sql1 = getMonthTreeCountSql(queryData, begin_time, end_time, true);
			String sql2 = getMonthTreeCountSql(queryData,begin_time, end_time,false);
			
			sql = sql + " UNION  " + sql1 + " UNION " + sql2;
			
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
	
	public String getMonthTreeCountSql(JSONObject queryData, String begin_time, String end_time,boolean flag){
		StringBuilder sql = new StringBuilder();
		
		begin_time = begin_time.concat(" 00:00:00");
		end_time = end_time.concat(" 23:59:59");
		
		sql.append(" select sum(d.NNEWREGIST) t1,sum(d.NNEWACTIVE) t2,sum(d.NPAYCOUNT) t3,"
				   + " sum(d.NTOTALPAY)/100.0  t4, sum(d.NTOTALWIN)/100.0  t5, "
				   + " sum(d.NTOTALXF/100.0) t6"
				   + " from SHPT_DAILYSTATS d inner join SHPT_AGENCYINFO a on(a.NAGENCYID=d.NAGENCYID) "
				   + " where d.cstatdate >= '"+begin_time+"' and d.cstatdate <= '"+end_time+"'");
		
		if(queryData!=null){
			
			String search_type = queryData.getString("search_type");
			if(search_type!=null&&search_type.length()>0){
				String keyword = queryData.getString("keyword");
				sql.append(" and a."+search_type+" != '"+keyword+"' ");
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
}
