package com.huacai.web.controller.syslog;

import java.util.HashMap;
import java.util.Map;

import libcore.util.DateSimpleUtil;
import libcore.util.VarUtil;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huacai.util.PageUtil;
import com.huacai.web.controller.BaseController;
import com.huacai.web.dao.syslog.MLotLogDao;

@Controller
@RequestMapping("/syslog")
@Scope("prototype")
public class LotLogController extends BaseController {
	protected final static Logger logger = LogManager.getLogger(LotLogController.class);
	private int pageSize = 15;
	@Autowired
	private MLotLogDao mLotLogDao;
	
	
	public static final Map<Integer, String> LOT_OP_TYPE;
	static{
		LOT_OP_TYPE = new HashMap<Integer, String>();
		LOT_OP_TYPE.put(1, "开启新期");
		LOT_OP_TYPE.put(2, "强制开新期");
		LOT_OP_TYPE.put(3, "暂停销售");
		LOT_OP_TYPE.put(4, "恢复销售");
		LOT_OP_TYPE.put(5, "关闭奖池");
		LOT_OP_TYPE.put(6, "录入北京销量");
		LOT_OP_TYPE.put(7, "录入开奖号码");
		LOT_OP_TYPE.put(8, "热线开奖/合并销量");
		LOT_OP_TYPE.put(9, "录入全国中奖数据");
		LOT_OP_TYPE.put(10, "录入亿元大派送");
		LOT_OP_TYPE.put(11, "状态回滚");
		LOT_OP_TYPE.put(12, "派奖");
		LOT_OP_TYPE.put(20, "非b2b销量统计");
		LOT_OP_TYPE.put(21, "派奖结果");
		LOT_OP_TYPE.put(22, "夜对结束");
		LOT_OP_TYPE.put(23, "强制夜对成功");
	}

	@RequestMapping("lot_log_list")
	public String lot_log_list(Map<String, Object> model) {

		String logs_admin_loginname = VarUtil.RequestStr(request, "logs_admin_loginname").trim();
		String logs_admin_realname = VarUtil.RequestStr(request, "logs_admin_realname").trim();
		String logs_lot_id = VarUtil.RequestStr(request, "logs_lot_id").trim();
		String log_status = VarUtil.RequestStr(request, "log_status").trim();
		String begin_time = VarUtil.RequestStr(request, "begin_time").trim();
		String end_time = VarUtil.RequestStr(request, "end_time").trim();
		
		if(begin_time.length()==0||end_time.length()==0){
			String[] dateArr = DateSimpleUtil.getDateRange("7day");
			begin_time = dateArr[0];
			end_time = dateArr[1];
		}

		int curPage = VarUtil.RequestInt(request, "curPage");
		if (curPage <= 0) {
			curPage = 1;
		}
		JSONObject queryData = new JSONObject();
		queryData.put("logs_admin_loginname", logs_admin_loginname);
		queryData.put("logs_admin_realname", logs_admin_realname);
		queryData.put("logs_lot_id", logs_lot_id);
		queryData.put("log_status", log_status);
		queryData.put("begin_time", begin_time);
		queryData.put("end_time", end_time);

		model.putAll(queryData);
		JSONObject logListData = mLotLogDao.getLogList(pageSize, curPage, queryData);

		if (logListData != null) {

			model.put("curPage", curPage);
			model.put("pageSize", pageSize);
			model.put("logList", logListData.getJSONArray("list"));

			HashMap<String, Object> pageParams = new HashMap<String, Object>();
			pageParams.putAll(queryData);
//			model.put("pageStr", PageUtil.page2("/syslog/lot_log_list.do", curPage, pageSize, logListData.getIntValue("count"), pageParams));
			PageUtil.page2("/syslog/lot_log_list.do", curPage, pageSize, logListData.getIntValue("count"), pageParams,model);
		}
//		List<Map<String, Object>> lotNames = new ArrayList<Map<String, Object>>();
//		Map<String, Object> lotName;
//		Iterator<?> it = SysLogConstants.LOT_ID.keySet().iterator();
//		while (it.hasNext()) {
//			Integer key = (Integer)it.next();
//			String val = SysLogConstants.LOT_ID.get(key);
//			
//			lotName = new HashMap<String, Object>();
//			lotName.put("key", key.toString());
//			lotName.put("val", val);
//			lotNames.add(lotName);
//		}
		model.put("lotNames", mLotLogDao.getLotList());

		return "/syslog/lot_log_list";
	}
	@RequestMapping("lot_log_export")
	public String lot_log_export(Map<String, Object> model) {
		String logs_admin_loginname = VarUtil.RequestStr(request, "logs_admin_loginname");
		String logs_admin_realname = VarUtil.RequestStr(request, "logs_admin_realname");
		String logs_lot_id = VarUtil.RequestStr(request, "logs_lot_id");
		String log_status = VarUtil.RequestStr(request, "log_status");
		String begin_time = VarUtil.RequestStr(request, "begin_time");
		String end_time = VarUtil.RequestStr(request, "end_time");
		
		if(begin_time.length()==0||end_time.length()==0){
			String[] dateArr = DateSimpleUtil.getDateRange("7day");
			begin_time = dateArr[0];
			end_time = dateArr[1];
		}


		JSONObject queryData = new JSONObject();
		queryData.put("logs_admin_loginname", logs_admin_loginname);
		queryData.put("logs_admin_realname", logs_admin_realname);
		queryData.put("logs_lot_id", logs_lot_id);
		queryData.put("log_status", log_status);
		queryData.put("begin_time", begin_time);
		queryData.put("end_time", end_time);

		model.putAll(queryData);
		JSONObject logListData = mLotLogDao.getLogExportList(queryData);
		
		JSONArray lotArr = mLotLogDao.getLotList();
		Map<Integer, String> lotMap = new HashMap<Integer, String> ();
		for(int i = 0 ; i < lotArr.size();i++){
			lotMap.put(lotArr.getJSONObject(i).getInteger("key"), lotArr.getJSONObject(i).getString("val"));
		}

		if (logListData != null) {
			if(logListData.getIntValue("count") == -1){
				return getIframeMessage(model, "导出记录超出最大限制10000条，请按详情条件查询导出", "", "", "/syslog/lot_log_list.do", "");
			}
			model.put("info", "");
			JSONArray logList = logListData.getJSONArray("list");
			JSONArray logList2 = new JSONArray();
			int size = logList.size();
			for(int i = 0; i < size; i++){
				JSONObject row = logList.getJSONObject(i);


				row.put("logs_lot_system_name", "电话系统");
				row.put("logs_lot_id_name", lotMap.get(row.getIntValue("gameid")));
				row.put("logs_op_type_name", LOT_OP_TYPE.get(row.getIntValue("opertype")));
				row.put("logs_date", DateSimpleUtil.date("Y-m-d H:i:s", row.getDate("opertime")));
				if(row.getIntValue("operid") == 0){
					row.put("admin_loginname", "系统");
				}
				
				logList2.add(row);
				
//				logList.getJSONObject(i).put("logs_lot_system_name", SysLogConstants.LOT_SYSTEM.get(logList.getJSONObject(i).getIntValue("logs_lot_system")));
//				logList.getJSONObject(i).put("logs_lot_id_name", SysLogConstants.LOT_ID.get(logList.getJSONObject(i).getIntValue("logs_lot_id")));
//				logList.getJSONObject(i).put("logs_status_name", SysLogConstants.LOT_OP_STATUS.get(logList.getJSONObject(i).getIntValue("logs_status")));
//				logList.getJSONObject(i).put("logs_date", DateSimpleUtil.date("Y-m-d H:i:s", logList.getJSONObject(i).getDate("logs_create_time")));
			}
			
			model.put("logList", logList2);
			
		}
		exportXls("/syslog/lot_log_list.xls", "控审日志.xls", model);
		return null;
	}
}
