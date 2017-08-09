package com.huacai.web.controller.syslog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
import com.huacai.web.common.SysLogConstants;
import com.huacai.web.controller.BaseController;
import com.huacai.web.dao.syslog.MOptLogDao;

@Controller
@RequestMapping("/syslog")
@Scope("prototype")
public class OptLogController extends BaseController {
	protected final static Logger logger = LogManager.getLogger(OptLogController.class);
	private int pageSize = 20;
	@Autowired
	private MOptLogDao mOptLogDao;

	@RequestMapping("opt_log_list")
	public String opt_log_list(Map<String, Object> model) {

		String logs_admin_loginname = VarUtil.RequestStr(request, "logs_admin_loginname").trim();
		String logs_admin_realname = VarUtil.RequestStr(request, "logs_admin_realname").trim();
		String logs_app = VarUtil.RequestStr(request, "logs_app").trim();
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
		queryData.put("logs_app", logs_app);
		queryData.put("begin_time", begin_time);
		queryData.put("end_time", end_time);
		
		model.putAll(queryData);
		JSONObject logListData = mOptLogDao.getLogList(pageSize, curPage, queryData);

		if (logListData != null) {

			model.put("curPage", curPage);
			model.put("pageSize", pageSize);
			model.put("logList", logListData.getJSONArray("list"));

			HashMap<String, Object> pageParams = new HashMap<String, Object>();
			pageParams.putAll(queryData);
			PageUtil.page2("/syslog/opt_log_list.do", curPage, pageSize, logListData.getIntValue("count"), pageParams,model);
		}
		List<Map<String, Object>> appNames = new ArrayList<Map<String, Object>>();
		Map<String, Object> appName;
		Iterator<?> it = SysLogConstants.BOSS_APP.keySet().iterator();
		while (it.hasNext()) {
			Integer key = (Integer)it.next();
			String val = SysLogConstants.BOSS_APP.get(key);
			
			appName = new HashMap<String, Object>();
			appName.put("key", key.toString());
			appName.put("val", val);
			appNames.add(appName);
		}
		model.put("appNames", appNames);

		return "/syslog/opt_log_list";
	}
	@RequestMapping("opt_log_export")
	public String opt_log_export(Map<String, Object> model) {
		String logs_admin_loginname = VarUtil.RequestStr(request, "logs_admin_loginname");
		String logs_admin_realname = VarUtil.RequestStr(request, "logs_admin_realname");
		String logs_app = VarUtil.RequestStr(request, "logs_app");
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
		queryData.put("logs_app", logs_app);
		queryData.put("begin_time", begin_time);
		queryData.put("end_time", end_time);

		model.putAll(queryData);
		JSONObject logListData = mOptLogDao.getLogExportList(queryData);

		if (logListData != null) {
			if(logListData.getIntValue("count") == -1){
				return getIframeMessage(model, "导出记录超出最大限制10000条，请按详情条件查询导出", "", "", "/syslog/opt_log_list.do", "");
			}
			model.put("info", "");
			JSONArray logList = logListData.getJSONArray("list");
			int size = logList.size();
			for(int i = 0; i < size; i++){
				logList.getJSONObject(i).put("logs_app_name", SysLogConstants.BOSS_APP.get(logList.getJSONObject(i).getIntValue("logs_app")));
				logList.getJSONObject(i).put("logs_op_type_name", SysLogConstants.OP_TYPE.get(logList.getJSONObject(i).getIntValue("logs_op_type")));
				logList.getJSONObject(i).put("logs_status_name", SysLogConstants.LOGIN_STATUS.get(logList.getJSONObject(i).getIntValue("logs_status")));
				logList.getJSONObject(i).put("logs_date", DateSimpleUtil.date("Y-m-d H:i:s", logList.getJSONObject(i).getDate("logs_create_time")));
			}
			
			model.put("logList", logList);
			
		}
		exportXls("/syslog/opt_log_list.xls", "操作日志.xls", model);
		return null;
	}
}
