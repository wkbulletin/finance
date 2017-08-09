package com.huacai.web.controller.merchant;

import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.huacai.util.DateUtil;
import com.huacai.util.RequestUtil;
import com.huacai.web.constant.MerchantConstants;
import com.huacai.web.controller.BaseController;
import com.huacai.web.dao.merchant.ExportDao;

import libcore.util.VarUtil;

/**
 * 商户管理
 * @author wangr
 *
 */
@Controller
@RequestMapping("/export")
@Scope("prototype")
public class ListExportController extends BaseController {
	protected final static Logger logger = LogManager.getLogger(ListExportController.class);

	@Autowired
	private ExportDao listDao;
	
	
 	/**
 	 * action 获取休市计划列表
 	 * @param model
 	 * @return
 	 */
 	@RequestMapping("day_list")
	public String getDayList(Map<String, Object> model) {
 		
 		String begin_time = RequestUtil.getStringParam(request, "begin_time",DateUtil.getDay(-7)).trim();
		String end_time = RequestUtil.getStringParam(request, "end_time",DateUtil.getDay(0)).trim();
		if(begin_time == null || begin_time.length()<1){
			begin_time = "2015-01-01";
		}
		if(end_time == null || end_time.length()< 1){
			end_time = "2200-01-01";
		}
 		String search_type = VarUtil.RequestStr(request, "search_type").trim();
		String keyword = VarUtil.RequestStr(request, "keyword").trim();
		
		String type = MerchantConstants.DAY_SERCHTYPE.get(search_type);
		model.put("type", type);
		model.put("key", keyword);
		model.put("begin_time", begin_time);
		model.put("end_time", end_time);
		
		int curPage = VarUtil.RequestInt(request, "curPage");
		if(curPage <= 0){
			curPage = 1;
		}
		JSONObject queryData = new JSONObject();
		queryData.put("begin_time", begin_time);
		queryData.put("end_time", end_time);
		queryData.put("search_type", search_type);
		queryData.put("keyword", keyword);
		
		model.putAll(queryData);
				
		JSONObject roleListData = listDao.getDayList(pageSize, curPage, queryData);
		
		
		if(roleListData!=null){
			logger.info("查询参数列表");
			JSONObject total = listDao.getDayListTotal(queryData);
				if(total != null){
					model.put("t1", total.getString("t1"));
					model.put("t2", total.getString("t2"));
					model.put("t3", total.getString("t3"));
					model.put("t4", total.getString("t4"));
					model.put("t5", total.getString("t5"));
					model.put("t6", total.getString("t6"));
				}
			model.put("logList", roleListData.getJSONArray("list"));
		}
	
		logger.info("查询参数列表成功");
		exportXls("/report/day.xls", "商户日统计.xls", model);
		return null;
	}
 
 	/**
 	 * action 获取休市计划列表
 	 * @param model
 	 * @return
 	 */
 	@RequestMapping("merchantAll_list")
	public String getAllList(Map<String, Object> model) {
 		
 		String ntreedeep = VarUtil.RequestStr(request, "ntreedeep").trim();
 		String begin_time = RequestUtil.getStringParam(request, "begin_time",DateUtil.getDay(-7)).trim();
		String end_time = RequestUtil.getStringParam(request, "end_time",DateUtil.getDay(0)).trim();
		if(begin_time == null || begin_time.length()<1){
			begin_time = "2015-01-01";
		}
		if(end_time == null || end_time.length()< 1){
			end_time = "2200-01-01";
		}
 		String search_type = VarUtil.RequestStr(request, "search_type").trim();
		String keyword = VarUtil.RequestStr(request, "keyword").trim();
		
		String type = MerchantConstants.DAY_SERCHTYPE.get(search_type);
		model.put("type", type);
		model.put("key", keyword);
		model.put("begin_time", begin_time);
		model.put("end_time", end_time);
		if(ntreedeep == null || ntreedeep.isEmpty()){
			model.put("ntreedeep", "");
		}else{
			model.put("ntreedeep", ntreedeep+"级商户");
		}
		
		JSONObject queryData = new JSONObject();
		queryData.put("begin_time", begin_time);
		queryData.put("ntreedeep", ntreedeep);
		queryData.put("end_time", end_time);
		queryData.put("search_type", search_type);
		queryData.put("keyword", keyword);
		
//		model.putAll(queryData);
				
		JSONObject roleListData = listDao.getAllList(queryData,begin_time,end_time);
		
		
		if(roleListData!=null){
			logger.info("查询参数列表");
			JSONObject total = listDao.getAllTotal(queryData,begin_time,end_time);
				if(total != null){
					model.put("t1", total.getString("t1")==null ? 0 : total.getString("t1"));
					model.put("t2", total.getString("t2")==null ? 0 : total.getString("t2"));
					model.put("t3", total.getString("t3")==null ? 0 : total.getString("t3"));
					model.put("t4", total.getString("t4")==null ? 0 : total.getString("t4"));
					model.put("t5", total.getString("t5")==null ? 0 : total.getString("t5"));
					model.put("t6", total.getString("t6")==null ? 0 : total.getString("t6"));
				}
				
				model.put("logList", roleListData.getJSONArray("list"));
		}
	
		exportXls("/report/month.xls", "商户汇总统计.xls", model);
		return null;
	}
 	
 	/**
 	 * action 获取休市计划列表
 	 * @param model
 	 * @return
 	 */
 	@RequestMapping("commission_list")
	public String getCommisionList(Map<String, Object> model) {
 		
 		String begin_time = VarUtil.RequestStr(request, "begin_time").trim();
		
 		model.put("title", begin_time);

 		if(!begin_time.isEmpty()){
 			JSONObject queryData = new JSONObject();
 			queryData.put("begin_time", begin_time);
 			
 			model.putAll(queryData);
 					
 			JSONObject roleListData = listDao.getCommissionList(begin_time);
 			
 			if(roleListData!=null){
 				logger.info("查询参数列表");
 				JSONObject total = listDao.getCommissionTotal(begin_time);
 				if(total != null && total.size() > 0){
 					model.put("t1", total.getString("t1")==null ? 0: total.getString("t1"));
 					model.put("t2", total.getString("t2")==null ? 0: total.getString("t2"));
 					model.put("t3", total.getString("t3")==null ? 0: total.getString("t3"));
 				}
 				model.put("logList", roleListData.getJSONArray("list"));
 			}
 		}
	
		exportXls("/report/commission.xls", "佣金结算.xls", model);
		return null;
	}
 	
}
