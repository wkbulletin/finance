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
 * 
 * @author Administrator
 *
 */
@Repository("qRCodeDao")
@Configurable(preConstruction=true)
public class QRCodeDao {
	private static final Logger log=LogManager.getLogger(QRCodeDao.class);
	@Autowired
	private BossDB bossDB;
	
	/**
	 * 日统计
	 * @param pid
	 * @return
	 */
	public JSONObject getDayListTotal(JSONObject queryData) {
		int flag = 0;
		String sql = " SELECT sum(d.NPAGEVIEW) t1, sum(d.NTEMPREGT) t2 , sum(d.NCOMPLREG) t3, "
				   + " sum(d.NEXPIRENUM) t4 FROM SHPT_AGENCYINFO a inner join SHPT_AGENCYMORE m  on a.NAGENCYID=m.NAGENCYID "
				   + " inner join "+getQrcodeDailyStatTable(queryData)+" d  on ( d.nagencyid = a.nagencyid)";
		
		List<Object> param = new ArrayList<Object>();
		if(queryData!=null){
			String search_type = queryData.getString("search_type");
			if(search_type!=null&&search_type.length()>0){
				flag = 1;
				String keyword = queryData.getString("keyword");
				
				if ("ccontactname".equals(search_type) || "ccontacttele".equals(search_type)) {
					sql += " and m."+search_type+" = '"+keyword+"' ";
				} else {
					sql += " and a."+search_type+" = '"+keyword+"' ";
				}
				
			}

			/*
			String begin_time =queryData.getString("begin_time");
			String end_time = queryData.getString("end_time");
			if(begin_time.length()>0){
				sql += " and d.DDAILYTIME >= to_date(?,'yyyy-MM-dd') ";
				param.add(begin_time );
			}
			if(end_time.length()>0){
				sql += " and d.DDAILYTIME <= to_date(?,'yyyy-MM-dd') ";
				param.add(end_time );
			}*/
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
			sql = "select sum(t1) t1 ,sum(t2) t2,sum(t3) t3,sum(t4) t4 from  (" +sql +") ";
		}
		
		JSONObject json = new JSONObject();
		try {
			json = DbCommon.field2json(bossDB.queryMap(sql, param));
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return json;
	}
	private String getQrcodeDailyStatTable(JSONObject queryData){
		String sql = "select NAGENCYID,sum(NPAGEVIEW) npageview ,sum(NTEMPREGT) ntempregt,sum(NCOMPLREG) ncomplreg,sum(NEXPIRENUM) nexpirenum from SHPT_QRCODE_DAILYSTAT where 1=1 ";
		
		String begin_time =queryData.getString("begin_time");
		String end_time = queryData.getString("end_time");
		if(begin_time.length() > 0 ){
			sql += " and DDAILYTIME >= to_date('"+begin_time+"','yyyy-MM-dd')";
		}
		if(end_time.length() > 0 ){
			sql += " and DDAILYTIME <= to_date('"+end_time+"','yyyy-MM-dd')";
		}
		sql += " group by NAGENCYID";
		return "( " + sql +" )";
	}
	
	/**
	 * 日统计上下级查询
	 * @param queryData
	 * @param flag true,查父级记录；false：查子级记录
	 * @return
	 */
	public String getDayTreeCountSql(JSONObject queryData, boolean flag){
		StringBuilder sql = new StringBuilder();
		
		sql.append( " SELECT sum(d.NPAGEVIEW) t1, sum(d.NTEMPREGT) t2 , sum(d.NCOMPLREG) t3, "
				   + " sum(d.NEXPIRENUM) t4 FROM SHPT_AGENCYINFO a inner join SHPT_AGENCYMORE m  on a.NAGENCYID=m.NAGENCYID "
				   + " inner join "+getQrcodeDailyStatTable(queryData)+" d  on ( d.nagencyid = a.nagencyid)");
		
		if(queryData!=null){
			
			String search_type = queryData.getString("search_type");
			if(search_type!=null&&search_type.length()>0){
				String keyword = queryData.getString("keyword");
				String sqls = "";
				if ("ccontactname".equals(search_type) || "ccontacttele".equals(search_type)) {
					sqls = "  m." + search_type + " = '" + keyword + "' ";
					sql.append(" and  m." + search_type + " != '" + keyword + "' ");// 过滤查询条件本身记录
				} else {
					sqls = " a." + search_type + " = '" + keyword + "'  ";
					sql.append(" and  a." + search_type + " != '" + keyword + "'  ");// 过滤查询条件本身记录
				}
				sql.append(" start with a.NAGENCYID in (select a.NAGENCYID from SHPT_AGENCYINFO a ,SHPT_AGENCYMORE m where a.NAGENCYID=m.NAGENCYID and " + sqls + ")");
				if(flag){
					sql.append("connect by  prior a.NAGENCYUP= a.NAGENCYID");
				}else{
					sql.append("connect by  a.NAGENCYUP= prior  a.NAGENCYID");
				}
				/*
				String begin_time =queryData.getString("begin_time");
				String end_time = queryData.getString("end_time");
				if(begin_time.length()>0){
					sql.append("  and d.DDAILYTIME >= to_date('"+begin_time+"','yyyy-MM-dd') ");
				}
				if(end_time.length() >0){
					sql.append("  and d.DDAILYTIME <= to_date('"+end_time+"','yyyy-MM-dd')");
				}
				*/
			}
		}	
		return sql.toString();
	}
	
	/**
	 * 获取商户 日统计列表
	 * @param pid
	 * @return
	 */
	public JSONObject getStatInfoList(JSONObject queryData, int curPage, int pageSize) {

		String sql = "select 1 from SHPT_AGENCYINFO a , SHPT_AGENCYMORE m where a.NAGENCYID = m.NAGENCYID  ";
		boolean isall = true;
		if(queryData!=null){
			String search_type = queryData.getString("search_type");
			if(search_type!=null&&search_type.length()>0){
				String keyword = queryData.getString("keyword");
				if("ccontactname".equals(search_type) || "ccontacttele".equals(search_type)){
					sql += " and m."+search_type+" = '"+keyword+"' ";
				}else{
					sql += " and a."+search_type+" = '"+keyword+"' ";
				}
				isall = false;
			}
		}
		JSONObject statList = new JSONObject();
		try {
		int count = bossDB.count(sql);
		if(count != 1 && !isall){
			statList.put("err", "-1");
			return statList;
		}
		
		String statsql = "select * from (select to_char(d.DDAILYTIME,'yyyy-mm-dd') stattime ,sum(NPAGEVIEW) npageview ,sum(NTEMPREGT) ntempregt,sum(NCOMPLREG) ncomplreg,sum(NEXPIRENUM) nexpirenum from SHPT_QRCODE_DAILYSTAT d where 1=1 ";
		
		String begin_time =queryData.getString("begin_time");
		String end_time = queryData.getString("end_time");
		if(begin_time.length() > 0 ){
			statsql += " and d.DDAILYTIME >= to_date('"+begin_time+"','yyyy-MM-dd')";
		}
		if(end_time.length() > 0 ){
			statsql += " and d.DDAILYTIME <= to_date('"+end_time+"','yyyy-MM-dd')";
		}
		statsql += "and exists ("+sql+" and a.NAGENCYID = d.NAGENCYID ) group by DDAILYTIME) order by stattime desc ";	
		
		JSONArray jsonList = new JSONArray();
			int totalCount = bossDB.count(statsql);
			statList.put("count", totalCount);
			if(totalCount>0){
				if(queryData.getString("act").equals("export")){
					jsonList = DbCommon.field2json(bossDB.queryList(statsql));
				}else{
					jsonList = DbCommon.field2json(bossDB.queryList(statsql, pageSize, curPage));
				}
				
			}
			statList.put("list", jsonList);
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return statList;
	}
	
	public JSONObject getStatInfoTotal(JSONObject queryData) {
		String sqlstr = "select 1 from SHPT_AGENCYINFO a , SHPT_AGENCYMORE m where a.NAGENCYID = m.NAGENCYID  ";
		if(queryData!=null){
			String search_type = queryData.getString("search_type");
			if(search_type!=null&&search_type.length()>0){
				String keyword = queryData.getString("keyword");
				if("ccontactname".equals(search_type) || "ccontacttele".equals(search_type)){
					sqlstr += " and m."+search_type+" = '"+keyword+"' ";
				}else{
					sqlstr += " and a."+search_type+" = '"+keyword+"' ";
				}
			}
		}
		
		
		String sql = " SELECT sum(d.NPAGEVIEW) t1, sum(d.NTEMPREGT) t2 , sum(d.NCOMPLREG) t3, "
				   + " sum(d.NEXPIRENUM) t4 FROM SHPT_QRCODE_DAILYSTAT d where 1=1 ";
		
		String begin_time =queryData.getString("begin_time");
		String end_time = queryData.getString("end_time");
		if(begin_time.length() > 0 ){
			sql += " and d.DDAILYTIME >= to_date('"+begin_time+"','yyyy-MM-dd')";
		}
		if(end_time.length() > 0 ){
			sql += " and d.DDAILYTIME <= to_date('"+end_time+"','yyyy-MM-dd')";
		}
		sql += "and exists ("+sqlstr+" and a.NAGENCYID = d.NAGENCYID )";
		
		JSONObject json = new JSONObject();
		try {
			json = DbCommon.field2json(bossDB.queryMap(sql));
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return json;
	}
	
	/**
	 * 获取商户 日统计列表
	 * @param pid
	 * @return
	 */
	public JSONObject getDayList(JSONObject queryData, int curPage, int pageSize) {
		/**
		 * 当按条件精确查询的时候，将flag置1，flag为1的时候将触发树形结构查询
		 */
		int flag=0 ;
		String sql = " SELECT 1 px, a.CREGNAME, (select nvl(max(CREGNAME),'--') from SHPT_AGENCYINFO where NAGENCYID = a.NAGENCYUP) as agencyup, d.*  FROM SHPT_AGENCYINFO a  inner join SHPT_AGENCYMORE m  on a.NAGENCYID=m.NAGENCYID  "
				   + " inner join   "+getQrcodeDailyStatTable(queryData)+" d  on ( d.nagencyid = a.nagencyid)"
				   + " where 1=1 ";
		
		List<Object> param = new ArrayList<Object>();
		if(queryData!=null){
			String search_type = queryData.getString("search_type");
			if(search_type!=null&&search_type.length()>0){
				flag = 1;
				String keyword = queryData.getString("keyword");
				if("ccontactname".equals(search_type) || "ccontacttele".equals(search_type)){
					sql += " and m."+search_type+" = '"+keyword+"' ";
				}else{
					sql += " and a."+search_type+" = '"+keyword+"' ";
				}
			}

			/*
			String begin_time =queryData.getString("begin_time");
			String end_time = queryData.getString("end_time");
			if(begin_time.length() >0){
				sql += " and d.DDAILYTIME >= to_date(?,'yyyy-MM-dd') ";
				param.add(begin_time );
			}
			if(end_time.length() >0){
				sql += " and d.DDAILYTIME <=  to_date(?,'yyyy-MM-dd') ";
				param.add(end_time );
			}
			*/
		}
		
		
		if(flag == 1){
			String sql1 = getDayTreeSql(queryData, true);
			String sql2 = getDayTreeSql(queryData,false);
			/**
			 * 将查询出来的父父级数据和子子级记录合并 
			 */
			sql = sql + " UNION  " + sql1 + " UNION " + sql2;
		}
		
		sql = "select * from ( " + sql+ ") ORDER BY NPAGEVIEW desc,  CREGNAME asc ";
//		sql += " ORDER BY a.NAGENCYID, d.cstatdate  desc  ";
		
		JSONObject statList = new JSONObject();
		JSONArray jsonList = new JSONArray();
		try {
			int totalCount = bossDB.count(sql, param);
			statList.put("count", totalCount);
			if(totalCount>0){
				if(queryData.getString("act").equals("export")){
					jsonList = DbCommon.field2json(bossDB.queryList(sql, param));
				}else{
					jsonList = DbCommon.field2json(bossDB.queryList(sql, param, pageSize, curPage));
				}
			}
			statList.put("list", jsonList);
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return statList;
	}
	
	
	/**
	 * 
	 * @param queryData
	 * @param flag true,查父级记录；false：查子级记录
	 * @return
	 */
	public String getDayTreeSql(JSONObject queryData, boolean flag){
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT 2 px, a.CREGNAME, (select nvl(max(CREGNAME),'--') from SHPT_AGENCYINFO where NAGENCYID = a.NAGENCYUP) as agencyup, d.*   FROM SHPT_AGENCYINFO a  inner join SHPT_AGENCYMORE m  on a.NAGENCYID=m.NAGENCYID  inner join  "+getQrcodeDailyStatTable(queryData)+" d  "
				+ "  on ( d.nagencyid = a.nagencyid) where 1=1 ");
		
		if(queryData!=null){
			
			String search_type = queryData.getString("search_type");
			if(search_type!=null&&search_type.length()>0){
				String keyword = queryData.getString("keyword");
				String sqls = "";
				if ("ccontactname".equals(search_type) || "ccontacttele".equals(search_type)) {
					sqls = "  m." + search_type + " = '" + keyword + "' ";
					sql.append(" and  m." + search_type + " != '" + keyword + "' ");// 过滤查询条件本身记录
				} else {
					sqls = " a." + search_type + " = '" + keyword + "'  ";
					sql.append(" and  a." + search_type + " != '" + keyword + "'  ");// 过滤查询条件本身记录
				}
				sql.append(" start with a.NAGENCYID in (select a.NAGENCYID from SHPT_AGENCYINFO a ,SHPT_AGENCYMORE m where a.NAGENCYID=m.NAGENCYID and " + sqls + ")");
				
				/**
				 * 过滤查询条件本身记录
				 */
				//sql.append(" and a."+search_type+" != '"+keyword+"' ");
				//sql.append(" start with a.NAGENCYID in (select info.NAGENCYID from SHPT_AGENCYINFO info where info."+search_type+" = '"+keyword+"' "+")");
				if(flag){
					sql.append("connect by  prior a.NAGENCYUP= a.NAGENCYID");
				}else{
					sql.append("connect by  a.NAGENCYUP= prior  a.NAGENCYID");
				}
				/*
				String begin_time =queryData.getString("begin_time");
				String end_time = queryData.getString("end_time");
				if(begin_time.length() > 0){
					sql.append("  and d.DDAILYTIME >= to_date('"+begin_time+"','yyyy-MM-dd') ");
				}
				if(end_time.length() > 0){
					sql.append("  and d.DDAILYTIME <= to_date('"+end_time+"','yyyy-MM-dd')");
				}
				*/
			}
		}	
		return sql.toString();
	}
	
	
	
	/**
	 * 获取 账户 详细 信息
	 * @param pid
	 * @return
	 */
	public JSONObject  getQRAgencyInfo(long id) {
		                                                  
		String sql ="select t.*,o.clinkurl,o.elinkstat,o.csrthost from shpt_qrcode_mapping t, shpt_qrcode_oriurls o where  o.elinkstat=2  and  t.emapstat not in(3,4)  and t.llinkcode=o.llinkcode t.nagencyid=?";
		 
		JSONObject json = new JSONObject();
		try {
			json = DbCommon.field2json(bossDB.queryMap(sql, new Object[] {id}));
			if(json == null) json = new JSONObject();
				
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return json;
	}
	/**
	 * 获取url配置信息
	 * @param pid
	 * @return
	 */
	public JSONObject  getJstco2oQrcodeOriurls(long id) {
		String sql ="";
		JSONObject json = new JSONObject();
		try {
		           if(id==-1){
		       		 sql =" select  *   from shpt_qrcode_oriurls o  where  o.elinkstat=2";
		 			json = DbCommon.field2json(bossDB.queryMap(sql));
		           }  else{
		       		 sql =" select  *   from shpt_qrcode_oriurls o where   o.elinkstat=2 and t.llinkcode=?";
		 			json = DbCommon.field2json(bossDB.queryMap(sql, new Object[] {id}));
		           }                                    
			if(json == null) 
				json = new JSONObject();
				
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return json;
	}
}
