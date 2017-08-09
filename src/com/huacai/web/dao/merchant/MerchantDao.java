package com.huacai.web.dao.merchant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huacai.web.common.DbCommon;
import com.huacai.web.db.datasource.BossDB;

@Repository("merchantDao")
@Configurable(preConstruction = true)
public class MerchantDao {

	private static final Logger log = LogManager.getLogger(MerchantDao.class);

	@Autowired
	private BossDB bossDB;

	/**
	 * 获取单条
	 * 
	 * @param pid
	 * @return
	 */
	public JSONObject getObject(String id) {

		String sql = "SELECT a.*, m.ctxbankcard ,m.CCARDOWNER, m.CTXBANKNAME, m.CTXBANKPROV ," + "  m.CTXBANKCITY, m.CTXBANKBRCH, m.CCONTACTNAME, m.CCONTACTTELE, " + "  m.CCONTACTMAIL, m.CCONTACTADDR, m.CCOMPANYURL1, m.CMANAGERNAME ," + "  m.COTHERSMEMO1, m.CIDCODENUMS FROM SHPT_AGENCYINFO a, SHPT_AGENCYMORE m  " + "  WHERE a.NAGENCYID = m.NAGENCYID and a.NAGENCYID = ?";
		JSONObject json = new JSONObject();
		try {
			json = DbCommon.field2json(bossDB.queryMap(sql, new Object[] { id }));
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return json;
	}

	/**
	 * 名称唯一性验证
	 * 
	 * @param pid
	 * @return
	 */
	public int countMerchantbyUserName(String name) {
		int i = 0;
		String sql = "SELECT 1 FROM SHPT_AGENCYINFO  WHERE CLOGINNAME=?";
		try {
			i = bossDB.count(sql, new Object[] { name });
		} catch (Exception e) {
			log.error("DB error", e);
			return 0;
		}
		return i;
	}

	/**
	 * 手机唯一性验证
	 * 
	 * @param pid
	 * @return
	 */
	public int countMerchantbyPhone(String name) {
		int i = 0;
		String sql = "SELECT 1 FROM SHPT_AGENCYMORE  WHERE CCONTACTTELE=?";
		try {
			i = bossDB.count(sql, new Object[] { name });
		} catch (Exception e) {
			log.error("DB error", e);
			return 0;
		}
		return i;
	}

	/**
	 * 登录名唯一性验证
	 * 
	 * @param pid
	 * @return
	 */
	public int countMerchantbyName(String name) {
		int i = 0;
		String sql = "SELECT 1 FROM SHPT_AGENCYINFO  WHERE CREGNAME=?";
		try {
			i = bossDB.count(sql, new Object[] { name });
		} catch (Exception e) {
			log.error("DB error", e);
			return 0;
		}
		return i;
	}

	/**
	 * 唯一性验证
	 * 
	 * @param pid
	 * @return
	 */
	public int countMerchantbyNameUpdate(String name, String nagencyid) {
		int i = 0;
		String sql = "SELECT 1 FROM SHPT_AGENCYINFO  WHERE CREGNAME=? and nagencyid !=? ";
		try {
			i = bossDB.count(sql, new Object[] { name, nagencyid });
		} catch (Exception e) {
			log.error("DB error", e);
			return 0;
		}
		return i;
	}

	/**
	 * 插入
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int insertObject(Map<String, Object> map, Map<String, Object> moreMap) {

		try {
			long id = bossDB.getSequenceNextval("S_AGENCY");
			map.put("nagencyid", id);

			moreMap.put("nagencyid", id);
			bossDB.sql_insert("SHPT_AGENCYINFO", map);
			bossDB.sql_insert("SHPT_AGENCYMORE", moreMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("DB error", e);
			log.info("insertObj===" + e.getMessage());
			log.info(e);
			return 0;
		}

		return 1;

	}

	/**
	 * 更新
	 * 
	 * @param id
	 * @param map
	 * @return
	 */
	public int updateObject(String id, Map<String, Object> map, Map<String, Object> moreMap) {
		try {
			bossDB.sql_update("SHPT_AGENCYINFO", map, "NAGENCYID=?", new Object[] { id });
			bossDB.sql_update("SHPT_AGENCYMORE", moreMap, "NAGENCYID=?", new Object[] { id });
			return 1;
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return 0;
	}

	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 */
	public int updateStatus(String id, String status) {
		try {
			String sql = "update SHPT_AGENCYINFO  set ESTATUS=?, DDISREGTIME=now(), DLOCKEDTIME=now() where NAGENCYID=?";
			return bossDB.update(sql, new Object[] { status, id });
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return 0;
	}

	/**
	 * 重置密码
	 * 
	 * @param id
	 * @return
	 */
	public int resetPwd(String id, String pwd) {
		try {
			// String sql = "update SHPT_AGENCYINFO set CLOGINPASS=? , ESTATUS=1
			// where NAGENCYID=?";
			String sql = "update SHPT_AGENCYINFO  set CLOGINPASS=?  where NAGENCYID=?";
			return bossDB.update(sql, new Object[] { pwd, id });
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return 0;
	}
	
	public List<Map<String, Object>> getManagernameList(){
		String sql = "select distinct(cmanagername) from SHPT_AGENCYMORE where cmanagername is not null";
		try {
			return bossDB.queryList(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}
	
	/**
	 * 获取列表
	 * @param pid
	 * @return
	 */
	public JSONObject getList(int pageSize, int curPage, JSONObject queryData) {
		/**
		 * 当按条件精确查询的时候，将flag置1，flag为1的时候将触发树形结构查询
		 */
		int flag=0 ;
		String sql = "SELECT  1 as px, a.dregisttime dtime, a.ntreedeep ndep, a.*, m.CCONTACTNAME as person, m.CCONTACTTELE as tel, m.CMANAGERNAME as managername"//
				+ " , (select nvl(max(CREGNAME),'--') from SHPT_AGENCYINFO where NAGENCYID = a.NAGENCYUP) as agencyup FROM SHPT_AGENCYINFO a, SHPT_AGENCYMORE m  WHERE  1=1 and a.NAGENCYID=m.NAGENCYID ";
		
		List<Object> param = new ArrayList<Object>();
		if(queryData!=null){
			
			String search_type = queryData.getString("search_type");
			if(search_type!=null&&search_type.length()>0){
				flag = 1;
				/**
				 * 关键字不在同一表中,根据前台输入关键字判断查询哪个表中的字段
				 */
				String keyword = queryData.getString("keyword");
				if("ccontactname".equals(search_type) || "ccontacttele".equals(search_type)){
					sql += " and m."+search_type+" = '"+keyword+"' ";
				}else{
					sql += " and a."+search_type+" = '"+keyword+"' ";
				}
			}
			
			String cmanagername = queryData.getString("cmanagername");
			if(cmanagername != null && cmanagername.length() >0){
				sql += " and m.cmanagername = '"+cmanagername+"' ";
			}
			
			String emaintype = queryData.getString("emaintype");
			if(emaintype!=null&&emaintype.length()>0){
				sql += " and a.emaintype=?";
				param.add(emaintype);
			}
			
			String ntreedeep = queryData.getString("ntreedeep");
			if(ntreedeep!=null&&ntreedeep.length()>0){
				sql += " and a.ntreedeep=?";
				param.add(ntreedeep);
			}
			
		}
		
		if(flag == 1){
			String sql1 = getTreeSql(queryData, true);
			String sql2 = getTreeSql(queryData,false);
			/**
			 * 将查询出来的父父级数据和子子级记录合并 
			 */
			sql = sql + " UNION  " + sql1 + " UNION " + sql2;
		}
		
		sql += " ORDER BY px asc ,ndep asc, dtime  desc ";
		
		JSONObject roleListData = new JSONObject();
		JSONArray jsonList = new JSONArray();
		try {
			int totalCount = bossDB.count(sql, param);
			roleListData.put("count", totalCount);
			if(totalCount>0){
				jsonList = DbCommon.field2json(bossDB.queryList(sql, param, pageSize, curPage));
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
	 * @param flag:
	 *            true,查父级记录；false：查子级记录
	 * @return
	 */
	public String getTreeSql(JSONObject queryData, boolean flag) {
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT 2 as px,a.dregisttime dtime, a.ntreedeep ndep,  a.*, m.CCONTACTNAME as person, m.CCONTACTTELE as tel, m.CMANAGERNAME as managername"//
				+ ", (select nvl(max(CREGNAME),'--') from SHPT_AGENCYINFO where NAGENCYID = a.NAGENCYUP) as agencyup FROM SHPT_AGENCYINFO a, SHPT_AGENCYMORE m  WHERE  1=1 and a.NAGENCYID=m.NAGENCYID");

		if (queryData != null) {

			String search_type = queryData.getString("search_type");
			if (search_type != null && search_type.length() > 0) {
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
				if (flag) {
					sql.append("connect by  prior a.NAGENCYUP= a.NAGENCYID");
				} else {
					sql.append("connect by  a.NAGENCYUP= prior  a.NAGENCYID");
				}

			}
		}
		return sql.toString();
	}

	/**
	 * 查询银行信息
	 * 
	 * @return
	 */
	public JSONArray getBank() {
		String sql = " select to_char(bankid) as bankid,bankname from shpt_BANKINFO ";

		JSONArray jsonList = new JSONArray();
		try {
			jsonList = DbCommon.field2json(bossDB.queryList(sql));
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return jsonList;
	}

	public JSONArray getArea2(String level) {
		String sql = " select areacode,areaname from SHPT_AREA " + " where AREALEVEL = ? ";

		JSONArray jsonList = new JSONArray();
		try {
			jsonList = DbCommon.field2json(bossDB.queryList(sql, new Object[] { level }));
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return jsonList;
	}

	public JSONArray getAreaByFcode(String fcode) {
		String sql = " select areacode,areaname from SHPT_AREA " + " where FCODE = ? ";

		JSONArray jsonList = new JSONArray();
		try {
			jsonList = DbCommon.field2json(bossDB.queryList(sql, new Object[] { fcode }));
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return jsonList;
	}

	public JSONArray getAreas(String fcode) {
		String sql = "select areacode,areaname from SHPT_AREA t where t.FCODE=?";

		JSONArray jsonList = new JSONArray();
		try {
			jsonList = DbCommon.field2json(bossDB.queryList(sql, new Object[] { fcode }));
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return jsonList;
	}

	/**
	 * 获取单条
	 * 
	 * @param pid
	 * @return
	 */
	public JSONObject getBankInfoById(String id) {
		String sql = "SELECT * FROM SHPT_BANKINFO WHERE BANKID = ?";

		JSONObject json = new JSONObject();
		try {
			json = DbCommon.field2json(bossDB.queryMap(sql, new Object[] { id }));
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return json;
	}

	/**
	 * 获取单条
	 * 
	 * @param pid
	 * @return
	 */
	public JSONObject getAreaById(String id) {
		String sql = "SELECT * FROM SHPT_AREA WHERE AREACODE = ?";

		JSONObject json = new JSONObject();
		try {
			json = DbCommon.field2json(bossDB.queryMap(sql, new Object[] { id }));
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return json;
	}

	/**
	 * 查询给定时间未登录商户（未使用）
	 * 
	 * @param dateNum
	 * @return
	 */
	public JSONArray getFreeMerchantList(int dateNum) {
		String sql = " select * from (select  nvl(d.channelMax, '2015-01-01') maxdate, t.nagencyid " + " from SHPT_AGENCYINFO t left join  (select max(a.CSTATDATE) channelMax,NAGENCYID  " + " from SHPT_DAILYSTATS a group by a.NAGENCYID) d on t.NAGENCYID = d.NAGENCYID " + " ) cc  where cc.maxdate <= to_char(add_months(?, -?), 'YYYY-MM-dd') ";

		JSONArray jsonList = new JSONArray();
		try {
			jsonList = DbCommon.field2json(bossDB.queryList(sql, new Object[] { new Date(), dateNum }));
		} catch (Exception e) {
			log.error("DB error", e);
		}
		return jsonList;
	}

	/**
	 * 批量更新（未使用）
	 * 
	 * @param batchArgs
	 * @return
	 */
	public int batchUpdate(List<Object[]> batchArgs) {
		int x = 0;
		DataSourceTransactionManager tran = bossDB.getTransManager();
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = tran.getTransaction(def);

		try {
			String sql = "update SHPT_AGENCYINFO  set ESTATUS=9 , DCLEANTIME=now() where NAGENCYID=? ";
			double s = System.currentTimeMillis();
			bossDB.batchUpdate(sql, batchArgs);
			double e = System.currentTimeMillis();
			log.info("------------------清除商户信息" + batchArgs.size() + "条," + "用时" + (e - s));
			tran.commit(status);
		} catch (Exception e) {
			tran.rollback(status);
			log.error("DB error", e);
			throw new RuntimeException("清除商户信息失败：");
		}
		return x;
	}

}
