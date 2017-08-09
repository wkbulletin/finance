package com.huacai.web.controller.qrcode;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.huacai.util.PageUtil;
import com.huacai.web.controller.BaseController;
import com.huacai.web.dao.merchant.QRCodeDao;

import libcore.util.DateUtil;
import libcore.util.VarUtil;

/**
 * 二维码管理
 * @author 
 *
 */
@Controller
@RequestMapping("/qrCode")
@Scope("prototype")
public class QrcodeStatController extends BaseController {
	protected final static Logger logger = LogManager.getLogger(QrcodeStatController.class);

	@Autowired
	private QRCodeDao qRCodeDao;
	
 	@RequestMapping("stat_index")
	public String stat_index(Map<String, Object> model) {
 		Calendar c = Calendar.getInstance();
 		c.add(Calendar.DAY_OF_YEAR, -1);
 		String searchMaxDay = DateUtil.getDate(c.getTime(), "yyyy-MM-dd");
 		model.put("searchMaxDay", searchMaxDay);
 		int curPage = VarUtil.RequestInt(request, "curPage", 1);
 		String act = VarUtil.RequestStr(request, "act");
 		String begin_time = VarUtil.RequestStr(request, "begin_time").trim();
		String end_time = VarUtil.RequestStr(request, "end_time").trim();
		if("".equals(act)){
			end_time = searchMaxDay;
			c.add(Calendar.DAY_OF_YEAR, -6);
			begin_time = DateUtil.getDate(c.getTime(), "yyyy-MM-dd");
 		}
 		String search_type = VarUtil.RequestStr(request, "search_type").trim();
		String keyword = VarUtil.RequestStr(request, "keyword").trim();
		
		JSONObject queryData = new JSONObject();
		queryData.put("begin_time", begin_time);
		queryData.put("end_time", end_time);
		queryData.put("search_type", search_type);
		queryData.put("keyword", keyword);
		queryData.put("act", act);
		
		model.putAll(queryData);
				
		JSONObject statList = qRCodeDao.getDayList(queryData, curPage, pageSize);
		
		if(statList!=null){
			logger.info("查询参数列表");
			JSONObject total = qRCodeDao.getDayListTotal(queryData);
				if(total != null){
					model.put("t1", total.getString("t1")==null ? 0 : total.getString("t1"));
					model.put("t2", total.getString("t2")==null ? 0 : total.getString("t2"));
					model.put("t3", total.getString("t3")==null ? 0 : total.getString("t3"));
					model.put("t4", total.getString("t4")==null ? 0 : total.getString("t4"));
				}

			model.put("statList", statList.getJSONArray("list"));
			
			HashMap<String, Object> pagemodels = new HashMap<String, Object>();
			pagemodels.putAll(queryData);
			model.put("pageStr", PageUtil.page2("/qrCode/stat_index.do", curPage, pageSize, statList.getIntValue("count"), pagemodels,model));
		}
		if("export".equals(act)){
			if(begin_time.length() == 0){
				begin_time = "开始";
			}
			if(end_time.length() ==0){
				end_time = searchMaxDay;
			}
			model.put("export_time", begin_time + " 至 " +end_time);
			String search_type_name= VarUtil.RequestStr(request, "search_type_name");
			
			if(search_type_name.length() > 0 && !search_type_name.equals("请选择参数")){
				model.put("search_type_name", search_type_name);
				model.put("keyword", keyword);
			}
			exportXls("/qrcode/stat_index.xls", "推广数据.xls", model);
			return null;
		}else{
			return "qrcode/stat_index";
		}
	}
 	
 	
 	@RequestMapping("stat_info")
	public String stat_info(Map<String, Object> model) {
 		Calendar c = Calendar.getInstance();
 		c.add(Calendar.DAY_OF_YEAR, -1);
 		String searchMaxDay = DateUtil.getDate(c.getTime(), "yyyy-MM-dd");
 		model.put("searchMaxDay", searchMaxDay);
 		int curPage = VarUtil.RequestInt(request, "curPage", 1);
 		String act = VarUtil.RequestStr(request, "act");
 		String begin_time = VarUtil.RequestStr(request, "begin_time").trim();
		String end_time = VarUtil.RequestStr(request, "end_time").trim();
		if("".equals(act)){
			end_time = searchMaxDay;
			c.add(Calendar.DAY_OF_YEAR, -6);
			begin_time = DateUtil.getDate(c.getTime(), "yyyy-MM-dd");
 		}
 		String search_type = VarUtil.RequestStr(request, "search_type").trim();
		String keyword = VarUtil.RequestStr(request, "keyword").trim();
		
		JSONObject queryData = new JSONObject();
		queryData.put("begin_time", begin_time);
		queryData.put("end_time", end_time);
		queryData.put("search_type", search_type);
		queryData.put("keyword", keyword);
		queryData.put("act", act);
		
		model.putAll(queryData);
				
		JSONObject statList = qRCodeDao.getStatInfoList(queryData, curPage, pageSize);
		if(statList.containsKey("err")){
			model.put("err", "-1");
			return "qrcode/stat_info";
		}
		if(statList!=null){
			logger.info("查询参数列表");
			JSONObject total = qRCodeDao.getStatInfoTotal(queryData);
				if(total != null){
					model.put("t1", total.getString("t1")==null ? 0 : total.getString("t1"));
					model.put("t2", total.getString("t2")==null ? 0 : total.getString("t2"));
					model.put("t3", total.getString("t3")==null ? 0 : total.getString("t3"));
					model.put("t4", total.getString("t4")==null ? 0 : total.getString("t4"));
				}

			model.put("statList", statList.getJSONArray("list"));
			
			HashMap<String, Object> pagemodels = new HashMap<String, Object>();
			pagemodels.putAll(queryData);
			model.put("pageStr", PageUtil.page2("/qrCode/stat_info.do", curPage, pageSize, statList.getIntValue("count"), pagemodels,model));
		}
		if("export".equals(act)){
			if(begin_time.length() == 0){
				begin_time = "开始";
			}
			if(end_time.length() ==0){
				end_time = searchMaxDay;
			}
			model.put("export_time", begin_time + " 至 " +end_time);
			String search_type_name= VarUtil.RequestStr(request, "search_type_name");
			
			if(search_type_name.length() > 0 && !search_type_name.equals("请选择参数")){
				model.put("search_type_name", search_type_name);
				model.put("keyword", keyword);
			}
			exportXls("/qrcode/stat_info.xls", "推广数据明细.xls", model);
			return null;
		}else{
			return "qrcode/stat_info";
		}
	}
}
