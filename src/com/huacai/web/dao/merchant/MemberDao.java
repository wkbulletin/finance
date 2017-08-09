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


@Repository("memberDao")
@Configurable(preConstruction=true)
public class MemberDao {
	
	private static final Logger log=LogManager.getLogger(MemberDao.class);
	
	@Autowired
	private BossDB bossDB;
	
	
	/**
	 * 获取商户 日统计列表
	 * @param pid
	 * @return
	 */
	public JSONObject getMemberList(int pageSize, int curPage, JSONObject queryData) {
		StringBuilder sql = new StringBuilder(" select t.cuserxing , t.dregist ,SUBSTR( t.cusername,1,2) cusername, "
				   + " substr(c.CREGMOBILE,0,3 )||'****'||substr(c.CREGMOBILE,8,11 ) s9mobile, t.cusercode "
				   + " from shpt_custominfo t, yxpt_base.sync_customs_s9 c "
				   + " where c.usercode=t.cusercode  ");
		int flag=0 ;
		List<Object> param = new ArrayList<Object>();
		if(queryData!=null){
			String search_type = queryData.getString("search_type");
			if(search_type!=null&&search_type.length()>0){
				/**
				 * 关键字不在同一表中,根据前台输入关键字判断查询哪个表中的字段
				 */
				String keyword = queryData.getString("keyword");
				if("cregmobile".equals(search_type)){
					sql.append(" and c."+search_type+" = '"+keyword+"' ");
				}else{
					sql.append(" and t."+search_type+" = '"+keyword+"' ");
				}
			}

			String crefercode = queryData.getString("crefercode");
			if(crefercode!=null&&crefercode.length()>0){
				sql.append(" and t.NAGENCYID=(select a.NAGENCYID from SHPT_AGENCYINFO a where a.CREFERCODE=? )");
				param.add(crefercode);
			}
			
			/**
			 * 当按条件精确查询的时候，将flag置1，flag为1的时候将触发树形结构查询
			 */
			String nagencyid = queryData.getString("nagencyid");
			if(nagencyid!=null&&nagencyid.length()>0){
				flag = 1;
//				sql.append(" and t.nagencyid=?");
//				param.add(nagencyid);
			}
			
			String begin_time =queryData.getString("begin_time");
			String end_time = queryData.getString("end_time");
			if(begin_time!=null&&begin_time.length()>0){
				
				sql.append(" and t.DREGIST >= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')");
				
				param.add(begin_time + " 00:00:00");
			}
			if(end_time!=null&&end_time.length()>0){
				
				sql.append(" and t.DREGIST <= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')");
				
				param.add(end_time + " 23:59:59");
			}	
		}
		
		
		if(flag == 1){
			/**
			 * 根据查询条件的商户ID筛选所有彩发信息
			 */
			String sql1 = getTreeSql(queryData);
			sql.append(" and t.NAGENCYID in ("  + sql1 + ")");
		}
		
		sql.append(" ORDER BY t.DREGIST desc  ");
		
		JSONObject roleListData = new JSONObject();
		JSONArray jsonList = new JSONArray();
		try {
			int totalCount = bossDB.count(sql.toString(), param);
			roleListData.put("count", totalCount);
			if(totalCount>0){
				jsonList = DbCommon.field2json(bossDB.queryList(sql.toString(), param,pageSize, curPage));
			}
			roleListData.put("list", jsonList);
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return roleListData;
	}

	/**
	 * 查询所有下级商户的商户id
	 * @param queryData
	 * @return
	 */
	public String getTreeSql(JSONObject queryData){
		StringBuilder sql = new StringBuilder();
		
		sql.append("select a.nagencyid  FROM SHPT_AGENCYINFO a where 1=1 ");
		
		if(queryData!=null){
			
			String nagencyid = queryData.getString("nagencyid");
			if(nagencyid!=null&&nagencyid.length()>0){
				sql.append(" start with a.NAGENCYID="+nagencyid);
				sql.append("connect by   a.NAGENCYUP= prior a.NAGENCYID  ");
				
			}
		}	
		return sql.toString();
	}
	
	public JSONArray getMerchantListByLevel(String level) {
		String sql = "SELECT NAGENCYID, CREGNAME FROM SHPT_AGENCYINFO WHERE NTREEDEEP=? ";
		sql += " ORDER BY NAGENCYID ";
		
		JSONArray jsonList = new JSONArray();
		try {
			jsonList = DbCommon.field2json(bossDB.queryList(sql, new Object[]{level}));
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return jsonList;
	}
	
	
	public JSONArray getMerchantListByCreferCode(String creferCode) {
		String sql = "SELECT NAGENCYID, CREGNAME  FROM SHPT_AGENCYINFO WHERE NAGENCYUP=? ";
		sql += " ORDER BY NAGENCYID ";
		
		JSONArray jsonList = new JSONArray();
		try {
			jsonList = DbCommon.field2json(bossDB.queryList(sql, new Object[]{creferCode}));
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return jsonList;
	}
	
	
	/**
	 * 获取单条彩发信息
	 * @param pid
	 * @return
	 */
	public JSONObject getObject(String id) {
		StringBuilder sql = new  StringBuilder();
		sql.append("select bqd.bill_id , act.active_type, act.active_name ,bqd.bill_activeid ,bqd.bill_createtime , s9.usercode, s9.cloginname,"
				+ " s9.cregmobile,s9.dregdate,win.win_winruleid,win.win_prizeid,win.win_prizetype,"
				+ " win.win_prizevalue, win.win_days,win.win_awardstatus,win.win_awardflownum from boss_qd_bill bqd"
				+ " inner join base_custom_s9 s9 on bqd.bill_memberid = s9.usercode "
				+ " inner join boss_yxpt_active act on bqd.bill_activeid = act.active_id "
				+ " left join boss_yxpt_win win on win.win_id =  bqd.bill_id where bqd.bill_id=?");	 		
		JSONObject json = new JSONObject();
		try {
			json = DbCommon.field2json(bossDB.queryMap(sql.toString(), new Object[] {id}));
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return json;
	}
	
	
	/**
	 * 查询昨天信息
	 * @param pid
	 * @return
	 */
	public JSONObject getCustomerYes(String usercode) {
		StringBuilder sql = new  StringBuilder("select sum(bqd.nxfmoney) s1 from SHPT_CUSTOMSTAT bqd where "
				 + " bqd.CSTATDATE=to_char(now() - 1, 'yyyy-mm-dd') and bqd.CUSERCODE=?");	 		
		JSONObject json = new JSONObject();
		try {
			json = DbCommon.field2json(bossDB.queryMap(sql.toString(), new Object[]{usercode}));
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return json;
	}
	
	
	/**
	 * 查询当月信息
	 * @param pid
	 * @return
	 */
	public JSONObject getCustomerMon(String usercode) {
		StringBuilder sql = new  StringBuilder("select sum(bqd.nxfmoney) s2 from SHPT_CUSTOMSTAT bqd where "
				 + " to_char(trunc(to_date(bqd.CSTATDATE,'yyyy-mm-dd')),'yyyymm')=to_char(trunc(now()),'yyyymm') and bqd.CUSERCODE=?");	
		JSONObject json = new JSONObject();
		try {
			json = DbCommon.field2json(bossDB.queryMap(sql.toString(), new Object[] {usercode}));
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return json;
	}
	
	
	/**
	 * 查询上月信息
	 * @param pid
	 * @return
	 */
	public JSONObject getCustomerCur(String usercode) {
		StringBuilder sql = new  StringBuilder("select sum(bqd.nxfmoney) s3 from SHPT_CUSTOMSTAT bqd where "
				 + " to_char(trunc(to_date(bqd.CSTATDATE,'yyyy-mm-dd')),'yyyymm')=to_char(add_months(trunc(now()),-1),'yyyymm')"
				 + " and bqd.CUSERCODE=?");	
		JSONObject json = new JSONObject();
		try {
			json = DbCommon.field2json(bossDB.queryMap(sql.toString(), new Object[] {usercode}));
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return json;
	}
	
	
	/**
	 * 查询累计消费信息
	 * @param pid
	 * @return
	 */
	public JSONObject getCustomerLji(String usercode) {
		StringBuilder sql = new  StringBuilder("select sum(bqd.nxfmoney) s4 from "
				 + " SHPT_CUSTOMSTAT bqd where  bqd.CUSERCODE=?");	
		JSONObject json = new JSONObject();
		try {
			json = DbCommon.field2json(bossDB.queryMap(sql.toString(), new Object[] {usercode}));
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return json;
	}
	
	
}
