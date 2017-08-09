package com.huacai.web.controller.merchant;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huacai.util.DateUtil;
import com.huacai.util.PageUtil;
import com.huacai.util.RequestUtil;
import com.huacai.util.SearchUtil;
import com.huacai.web.controller.BaseController;
import com.huacai.web.dao.merchant.ListDao;

import libcore.util.VarUtil;

/**
 * 商户管理
 * @author wangr
 *
 */
@Controller
@RequestMapping("/list")
@Scope("prototype")
public class ListController extends BaseController {
	protected final static Logger logger = LogManager.getLogger(ListController.class);

	@Autowired
	private ListDao listDao;
	
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
		
		JSONObject queryData = new JSONObject();
		queryData.put("begin_time", begin_time);
		queryData.put("end_time", end_time);
		queryData.put("search_type", search_type);
		queryData.put("keyword", keyword);
		
		model.putAll(queryData);
				
		JSONObject roleListData = listDao.getDayList(queryData);
		
		
		if(roleListData!=null){
			logger.info("查询参数列表");
			JSONObject total = listDao.getDayListTotal(queryData);
				if(total != null){
					model.put("t1", total.getString("t1")==null ? 0 : total.getString("t1"));
					model.put("t2", total.getString("t2")==null ? 0 : total.getString("t2"));
					model.put("t3", total.getString("t3")==null ? 0 : total.getString("t3"));
					model.put("t4", total.getString("t4")==null ? 0 : total.getString("t4"));
					model.put("t5", total.getString("t5")==null ? 0 : total.getString("t5"));
					model.put("t6", total.getString("t6")==null ? 0 : total.getString("t6"));
				}

			model.put("roleList", roleListData.getJSONArray("list"));
			
			HashMap<String, Object> pagemodels = new HashMap<String, Object>();
			pagemodels.putAll(queryData);
//			PageUtil.page2("/list/day_list.do", curPage, pageSize, roleListData.getIntValue("count"), pagemodels,model);
		}
	
		logger.info("查询参数列表成功");
		return "/list/day_list";
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
		
		JSONObject queryData = new JSONObject();
		queryData.put("begin_time", begin_time);
		queryData.put("ntreedeep", ntreedeep);
		queryData.put("end_time", end_time);
		queryData.put("search_type", search_type);
		queryData.put("keyword", keyword);
		
		model.putAll(queryData);
				
		JSONObject roleListData = listDao.getAllList(queryData,begin_time,end_time);
		
		
		if(roleListData!=null){
			logger.info("查询参数列表");
			JSONObject total = listDao.getAllTotal(queryData,begin_time,end_time);
				if(total != null){
					model.put("t1", total.getString("t1")== null ? 0 : total.getString("t1"));
					model.put("t2", total.getString("t2")== null ? 0 : total.getString("t2"));
					model.put("t3", total.getString("t3")== null ? 0 : total.getString("t3"));
					model.put("t4", total.getString("t4")== null ? 0 : total.getString("t4"));
					model.put("t5", total.getString("t5")== null ? 0 : total.getString("t5"));
					model.put("t6", total.getString("t6")== null ? 0 : total.getString("t6"));
				}
		
			model.put("roleList", roleListData.getJSONArray("list"));
			
			HashMap<String, Object> pagemodels = new HashMap<String, Object>();
			pagemodels.putAll(queryData);
//			PageUtil.page2("/list/merchantAll_list.do", curPage, pageSize, roleListData.getIntValue("count"), pagemodels,model);
		}
	
		logger.info("查询参数列表成功");
		return "/list/merchantAll_list";
	}
 	
 	/**
 	 * action 获取休市计划列表
 	 * @param model
 	 * @return
 	 */
 	@RequestMapping("commission_list")
	public String getCommisionList(Map<String, Object> model) {
 		
 		String begin_time = VarUtil.RequestStr(request, "begin_time").trim();
		
 		JSONArray js = SearchUtil.getSearch(1);

 		if(!begin_time.isEmpty()){
 			int curPage = VarUtil.RequestInt(request, "curPage");
 			if(curPage <= 0){
 				curPage = 1;
 			}
 			JSONObject queryData = new JSONObject();
 			queryData.put("begin_time", begin_time);
 			
 			model.putAll(queryData);
 					
 			JSONObject roleListData = listDao.getCommissionList(pageSize, curPage, begin_time);
 			
 			if(roleListData!=null){
 				logger.info("查询参数列表");
 				JSONObject total = listDao.getCommissionTotal(begin_time);
 				if(total != null){
 					model.put("t1", total.getString("t1"));
 					model.put("t2", total.getString("t2"));
 					model.put("t3", total.getString("t3"));
 				}
 				model.put("curPage", curPage);
 				model.put("pageSize", pageSize);
 				model.put("roleList", roleListData.getJSONArray("list"));
 				
 				HashMap<String, Object> pagemodels = new HashMap<String, Object>();
 				pagemodels.putAll(queryData);
 				PageUtil.page2("/list/commission_list.do", curPage, pageSize, roleListData.getIntValue("count"), pagemodels,model);
 			}
 		}else{
 			HashMap<String, Object> pagemodels = new HashMap<String, Object>();
			PageUtil.page2("/list/commission_list.do", 1, pageSize, 0, pagemodels,model);
 		}
	
 		model.put("monthList", js);
		logger.info("查询参数列表成功");
		return "/list/commission_list";
	}
 	
 	
}
