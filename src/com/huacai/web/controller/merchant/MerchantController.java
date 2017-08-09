package com.huacai.web.controller.merchant;

import java.util.Date;
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
import com.huacai.util.MD5Util;
import com.huacai.util.PageUtil;
import com.huacai.util.RequestUtil;
import com.huacai.web.common.LoginCommon;
import com.huacai.web.common.SysLogConstants;
import com.huacai.web.constant.MerchantConstants;
import com.huacai.web.controller.BaseController;
import com.huacai.web.dao.merchant.MemberDao;
import com.huacai.web.dao.merchant.MerchantDao;
import com.huacai.web.db.datasource.BossDB;

import libcore.util.VarUtil;

/**
 * 商户管理
 * @author wangr
 *
 */
@Controller
@RequestMapping("/merchant")
@Scope("prototype")
public class MerchantController extends BaseController {
	protected final static Logger logger = LogManager.getLogger(MerchantController.class);

	@Autowired
	private MerchantDao merchantDao;
	
	@Autowired
	private MemberDao memberDao;
	
	@Autowired
	private BossDB bossDB;
	
	/**
	 * 新建商户默认密码
	 */
	private String pwd = "666666";
		
 	/**
 	 * action 新增-保存休市计划
 	 * @param model
 	 * @return
 	 * @throws Exception 
 	 */
	@RequestMapping("merchant_add")
	public String addObject(Map<String, Object> model) throws Exception{
		
		String act = VarUtil.RequestStr(request, "act");
		logger.info("商户平台操作处理===="+act);
		//保存入库操作
		if(act.equals("save")){
			
			String names = "";
				//获取页面数据
			String ccontactname = VarUtil.RequestStr(request, "ccontactname").trim();
			String cregname = VarUtil.RequestStr(request, "cregname").trim();
			String emaintype = VarUtil.RequestStr(request, "emaintype").trim();
			String cloginname = VarUtil.RequestStr(request, "cloginname").trim();
			String ccontacttele = VarUtil.RequestStr(request, "ccontacttele").trim();
			int bzxsyncflag = VarUtil.RequestInt(request, "bzxsyncflag");
			if(!ccontacttele.isEmpty()){
				if(merchantDao.countMerchantbyPhone(ccontacttele) > 0){
					return result(model, getAjaxMessage(0, "新增失败，联系电话已存在"));
				}
			}
			
			/**
			 * 根据所先商户类型，将输入名称赋值给相字段；个人类型时，用户名就是商户 名
			 */
			if(String.valueOf(MerchantConstants.BOSS_MERCHANT_EMAINTYPE_PSERSON).equals(emaintype)){
				names = ccontactname;
			}else{
				names= cregname;
			}
			
			if(merchantDao.countMerchantbyUserName(cloginname) > 0){
				return result(model, getAjaxMessage(0, "新增失败，登录账号已存在"));
			}
			
			if(merchantDao.countMerchantbyName(names) > 0){
				String msg;
				if(String.valueOf(MerchantConstants.BOSS_MERCHANT_EMAINTYPE_PSERSON).equals(emaintype)){
					msg = "新增失败，用户姓名已存在";
				}else{
					msg = "新增失败，公司名称已存在";
				}
				return result(model, getAjaxMessage(0, msg));
			}
			
			String esumwhich = VarUtil.RequestStr(request, "esumwhich").trim();
			String eratemode = VarUtil.RequestStr(request, "eratemode").trim();
			String nfixedrate = VarUtil.RequestStr(request, "nfixedrate").trim();
			String nnuserrate = VarUtil.RequestStr(request, "nnuserrate").trim();
			String nphase1topv = VarUtil.RequestStr(request, "nphase1topv").trim();
			String nphase2topv = VarUtil.RequestStr(request, "nphase2topv").trim();
			String nphase3topv = VarUtil.RequestStr(request, "nphase3topv").trim();
			String nphase4topv = VarUtil.RequestStr(request, "nphase4topv").trim();
			String nphase5topv = VarUtil.RequestStr(request, "nphase5topv").trim();
			String nphase1rate = VarUtil.RequestStr(request, "nphase1rate").trim();
			String nphase2rate = VarUtil.RequestStr(request, "nphase2rate").trim();
			String nphase3rate = VarUtil.RequestStr(request, "nphase3rate").trim();
			String nphase4rate = VarUtil.RequestStr(request, "nphase4rate").trim();
			String nphase5rate = VarUtil.RequestStr(request, "nphase5rate").trim();
			String nphase6rate = VarUtil.RequestStr(request, "nphase6rate").trim();
				// 推广链接状态
			String eurlstatus = VarUtil.RequestStr(request, "eurlstatus").trim();

			Map<String, Object> map = new HashMap<String, Object>();
				/**
				 * 根据所先商户类型，将输入名称赋值给相字段；个人类型时，用户名就是商户 名
				 */
				if(String.valueOf(MerchantConstants.BOSS_MERCHANT_EMAINTYPE_PSERSON).equals(emaintype)){
					map.put("cregname", ccontactname);
				}else{
					map.put("cregname", cregname);
				}
				
				/**
				 * 当选择固定阶梯时，录入固定阶梯费率字段
				 */
				if(String.valueOf(MerchantConstants.BOSS_MERCHANT_ERATEMODE_G).equals(eratemode)){
					 map.put("nfixedrate", nfixedrate);
				}
				
			   long crefercode = bossDB.getSequenceNextval("S_AGENCY_CREFERCODE");
			   map.put("emaintype", emaintype);
			   map.put("crefercode", crefercode);
			   map.put("cloginname", cloginname);
			   map.put("cloginpass", MD5Util.MD5Encode(pwd));
			   if(!eratemode.isEmpty() && eratemode != null){
				   map.put("eratemode", eratemode);
			   }
			   if(!esumwhich.isEmpty() && esumwhich != null){
				   map.put("esumwhich", esumwhich);
			   }
			  
			   if(!nnuserrate.isEmpty() && nnuserrate != null){
				   map.put("nnuserrate", nnuserrate);
			   }
			   map.put("nphase1topv", nphase1topv);
			   map.put("nphase2topv", nphase2topv);
			   map.put("nphase3topv", nphase3topv);
			   map.put("nphase4topv", nphase4topv);
			   map.put("nphase5topv", nphase5topv);
			   map.put("nphase1rate", nphase1rate);
			   map.put("nphase2rate", nphase2rate);
			   map.put("nphase3rate", nphase3rate);
			   map.put("nphase4rate", nphase4rate);
			   map.put("nphase5rate", nphase5rate);
			   map.put("nphase6rate", nphase6rate);
			   map.put("eurlstatus", eurlstatus);
			   map.put("bzxsyncflag", bzxsyncflag);
			   map.put("estatus", MerchantConstants.BOSS_MERCHANT_STATUS_1);
			   map.put("ntreedeep", 1);
			   map.put("dchangerate", new Date());
			   
			String cidcodenums = VarUtil.RequestStr(request, "cidcodenums");
			String ctxbankcard = VarUtil.RequestStr(request, "ctxbankcard");
			String ctxbankname = VarUtil.RequestStr(request, "ctxbankname");
			String ctxbankprov = VarUtil.RequestStr(request, "ctxbankprov");
			String ctxbankcity = VarUtil.RequestStr(request, "ctxbankcity");
			String ctxbankbrch = VarUtil.RequestStr(request, "ctxbankbrch");
			String ccontactname2 = VarUtil.RequestStr(request, "ccontactname2");
			String ccontactmail = VarUtil.RequestStr(request, "ccontactmail");
			String ccontactaddr = VarUtil.RequestStr(request, "ccontactaddr");
			String ccompanyurl1 = VarUtil.RequestStr(request, "ccompanyurl1");
			String cmanagername = VarUtil.RequestStr(request, "cmanagername");
			String cothersmemo1 = VarUtil.RequestStr(request, "cothersmemo1");
			String ccardowner = VarUtil.RequestStr(request, "ccardowner");
			
			Map<String, Object> moreMap = new HashMap<String, Object>();
				moreMap.put("emaintype", emaintype);
				moreMap.put("cidcodenums", cidcodenums);
				moreMap.put("ctxbankcard", ctxbankcard);
				moreMap.put("ctxbankname", ctxbankname);
				moreMap.put("ctxbankprov", ctxbankprov);
				moreMap.put("ctxbankcity", ctxbankcity);
				moreMap.put("ctxbankbrch", ctxbankbrch);
				moreMap.put("ccontactname", ccontactname2);
				moreMap.put("ccontacttele", ccontacttele);
				moreMap.put("ccardowner", ccardowner);
				moreMap.put("ccontactmail", ccontactmail);
				moreMap.put("ccontactaddr", ccontactaddr);
				moreMap.put("ccompanyurl1", ccompanyurl1);
				moreMap.put("cmanagername", cmanagername);
				moreMap.put("cothersmemo1", cothersmemo1);
				moreMap.put("bzxsyncflag", bzxsyncflag);
logger.info("moreMap=="+com.alibaba.fastjson.JSON.toJSON(moreMap));
logger.info("map=="+com.alibaba.fastjson.JSON.toJSON(map));		
				logger.info("==emaintype=="+emaintype);
				int resR = merchantDao.insertObject(map , moreMap);
				logger.info("==resR=="+resR);
				
				
				if(resR>0){
					logOperateDao.add(request, LoginCommon.getAdminInfo(request).getAdminLoginname()+" 添加商户："+cregname+"成功", SysLogConstants.OP_TYPE_INSERT, SysLogConstants.OP_STATUS_SUCCESS, "");
					return result(model, getAjaxMessage(1, "新增成功"));
				}else{
					return result(model, getAjaxMessage(0, "新增失败"));
				}
			
		}
		
		model.put("area2list", merchantDao.getArea2("1"));
		model.put("banklist", merchantDao.getBank());
		
		return "/merchant/merchant_add";
	}
	
	
	/**
 	 * action 新增子商户 -保存休市计划
 	 * @param model
 	 * @return
	 * @throws Exception 
 	 */
	@RequestMapping("merchant_sonAdd")
	public String addSonObject(Map<String, Object> model) throws Exception{
		
		String act = VarUtil.RequestStr(request, "act");
		String nagencyid = VarUtil.RequestStr(request, "nagencyid");
		String nagencyup = VarUtil.RequestStr(request, "nagencyup");
		//保存入库操作
		if(act.equals("save")){
			String names = "";
			String cregname = VarUtil.RequestStr(request, "cregname").trim();
			String emaintype = VarUtil.RequestStr(request, "emaintype").trim();
			String cloginname = VarUtil.RequestStr(request, "cloginname").trim();
			String ccontactname = VarUtil.RequestStr(request, "ccontactname").trim();
			
			if(String.valueOf(MerchantConstants.BOSS_MERCHANT_EMAINTYPE_PSERSON).equals(emaintype)){
				names = ccontactname;
			}else{
				names= cregname;
			}
			
			if(merchantDao.countMerchantbyUserName(cloginname) > 0){
				return result(model, getAjaxMessage(0, "新增失败，登录账号已存在"));
			}
			
			if(merchantDao.countMerchantbyName(names) > 0){
				String msg;
				if(String.valueOf(MerchantConstants.BOSS_MERCHANT_EMAINTYPE_PSERSON).equals(emaintype)){
					msg = "新增失败，用户姓名已存在";
				}else{
					msg = "新增失败，公司名称已存在";
				}
				return result(model, getAjaxMessage(0, msg));
			}
				//获取页面数据
			
			int bzxsyncflag = VarUtil.RequestInt(request, "bzxsyncflag");
			// 推广链接状态
						String eurlstatus = VarUtil.RequestStr(request, "eurlstatus").trim();
						
			int ntreedeep = VarUtil.RequestInt(request, "ntreedeep");
			String cidcodenums = VarUtil.RequestStr(request, "cidcodenums");
			String ctxbankcard = VarUtil.RequestStr(request, "ctxbankcard");
			String ctxbankname = VarUtil.RequestStr(request, "ctxbankname");
			String ctxbankprov = VarUtil.RequestStr(request, "ctxbankprov");
			String ctxbankcity = VarUtil.RequestStr(request, "ctxbankcity");
			String ctxbankbrch = VarUtil.RequestStr(request, "ctxbankbrch");
			String ccontactname2 = VarUtil.RequestStr(request, "ccontactname2");
			String ccontacttele = VarUtil.RequestStr(request, "ccontacttele");
			String ccontactmail = VarUtil.RequestStr(request, "ccontactmail");
			String ccontactaddr = VarUtil.RequestStr(request, "ccontactaddr");
			String ccompanyurl1 = VarUtil.RequestStr(request, "ccompanyurl1");
			String cmanagername = VarUtil.RequestStr(request, "cmanagername");
			String cothersmemo1 = VarUtil.RequestStr(request, "cothersmemo1");
			String ccardowner = VarUtil.RequestStr(request, "ccardowner");
			
			Map<String, Object> map = new HashMap<String, Object>();
			if("2".equals(emaintype)){
				map.put("cregname", ccontactname);
			}else{
				map.put("cregname", cregname);
			}
		   long crefercode = bossDB.getSequenceNextval("S_AGENCY_CREFERCODE");
			 
		   map.put("emaintype", emaintype);
		   map.put("nagencyup", nagencyid);
		   map.put("crefercode", crefercode);
		   map.put("cloginname", cloginname);
		   map.put("cloginpass", MD5Util.MD5Encode(pwd));
		   map.put("estatus", 1);
		   map.put("ntreedeep", ntreedeep+1);//计算此商户ID的上级商户
		   map.put("dchangerate", new Date());
		   map.put("bzxsyncflag", bzxsyncflag);
		   map.put("eurlstatus", eurlstatus);
			
		   
		   Map<String, Object> moreMap = new HashMap<String, Object>();
			moreMap.put("emaintype", emaintype);
			moreMap.put("cidcodenums", cidcodenums);
			moreMap.put("ctxbankcard", ctxbankcard);
			moreMap.put("ctxbankname", ctxbankname);
			moreMap.put("ctxbankprov", ctxbankprov);
			moreMap.put("ctxbankcity", ctxbankcity);
			moreMap.put("ctxbankbrch", ctxbankbrch);
			moreMap.put("ccardowner", ccardowner);
			moreMap.put("ccontactname", ccontactname2);
			moreMap.put("ccontacttele", ccontacttele);
			
			moreMap.put("ccontactmail", ccontactmail);
			moreMap.put("ccontactaddr", ccontactaddr);
			moreMap.put("ccompanyurl1", ccompanyurl1);
			moreMap.put("cmanagername", cmanagername);
			moreMap.put("cothersmemo1", cothersmemo1);
			moreMap.put("bzxsyncflag", bzxsyncflag);
			try {
				
				int resR = merchantDao.insertObject(map , moreMap);
				
				if(resR>0){
					logOperateDao.add(request, LoginCommon.getAdminInfo(request).getAdminLoginname()+" 添加商户："+cregname+"成功", SysLogConstants.OP_TYPE_INSERT, SysLogConstants.OP_STATUS_SUCCESS, "");
					return result(model, getAjaxMessage(1, "新增成功"));
				}else{
					return result(model, getAjaxMessage(0, "新增失败"));
				}
			} catch (Exception e) {
					e.printStackTrace();
					return result(model, getAjaxMessage(0, "新增失败"));
			}
			
		}
		
		JSONObject policy =merchantDao.getObject(nagencyid);
		
		if(policy != null){
			model.put("supcregname", policy.getString("cregname"));
			model.putAll(policy);
		}
		
		model.put("nagencyid", nagencyid);
		model.put("nagencyup", nagencyup);
		model.put("area2list", merchantDao.getArea2("1"));
		model.put("banklist", merchantDao.getBank());
		return "/merchant/merchant_sonAdd";
	}
	
	/**
 	 * action 修改休市计划
 	 * @param model
 	 * @return
 	 */
	@RequestMapping("merchant_update")
	public String updateObject(Map<String, Object> model){
		
		String act = VarUtil.RequestStr(request, "act");
		String nagencyid = VarUtil.RequestStr(request, "nagencyid");
		//保存入库操作
		if(act.equals("save")){
				//获取页面数据
			
			String names = "";
			String cregname = VarUtil.RequestStr(request, "cregname").trim();
			String emaintype = VarUtil.RequestStr(request, "emaintype").trim();
			String ccontactname = VarUtil.RequestStr(request, "ccontactname").trim();
			
			if(String.valueOf(MerchantConstants.BOSS_MERCHANT_EMAINTYPE_PSERSON).equals(emaintype)){
				names = ccontactname;
			}else{
				names= cregname;
			}
			
			if(merchantDao.countMerchantbyNameUpdate(names, nagencyid) > 0){
				String msg;
				if(String.valueOf(MerchantConstants.BOSS_MERCHANT_EMAINTYPE_PSERSON).equals(emaintype)){
					msg = "修改失败，用户姓名已存在";
				}else{
					msg = "修改失败，公司名称已存在";
				}
				return result(model, getAjaxMessage(0, msg));
			}
			
			String esumwhich = VarUtil.RequestStr(request, "esumwhich");
			String eratemode = VarUtil.RequestStr(request, "eratemode");
			String nfixedrate = VarUtil.RequestStr(request, "nfixedrate");
			String nnuserrate = VarUtil.RequestStr(request, "nnuserrate");
			String nphase1topv = VarUtil.RequestStr(request, "nphase1topv");
			String nphase2topv = VarUtil.RequestStr(request, "nphase2topv");
			String nphase3topv = VarUtil.RequestStr(request, "nphase3topv");
			String nphase4topv = VarUtil.RequestStr(request, "nphase4topv");
			String nphase5topv = VarUtil.RequestStr(request, "nphase5topv");
			String nphase1rate = VarUtil.RequestStr(request, "nphase1rate");
			String nphase2rate = VarUtil.RequestStr(request, "nphase2rate");
			String nphase3rate = VarUtil.RequestStr(request, "nphase3rate");
			String nphase4rate = VarUtil.RequestStr(request, "nphase4rate");
			String nphase5rate = VarUtil.RequestStr(request, "nphase5rate");
			String nphase6rate = VarUtil.RequestStr(request, "nphase6rate");
			// 推广链接状态
		   String eurlstatus = VarUtil.RequestStr(request, "eurlstatus").trim();
			Map<String, Object> map = new HashMap<String, Object>();
				if(String.valueOf(MerchantConstants.BOSS_MERCHANT_EMAINTYPE_PSERSON).equals(emaintype)){
					map.put("cregname", ccontactname);
				}else{
					map.put("cregname", cregname);
				}
				if(String.valueOf(MerchantConstants.BOSS_MERCHANT_ERATEMODE_G).equals(eratemode)){
					 map.put("nfixedrate", nfixedrate);
				}
			   map.put("esumwhich", esumwhich);
			   map.put("eratemode", eratemode);
			   map.put("nnuserrate", nnuserrate);
			   map.put("nphase1topv", nphase1topv);
			   map.put("nphase2topv", nphase2topv);
			   map.put("nphase3topv", nphase3topv);
			   map.put("nphase4topv", nphase4topv);
			   map.put("nphase5topv", nphase5topv);
			   map.put("nphase1rate", nphase1rate);
			   map.put("nphase2rate", nphase2rate);
			   map.put("nphase3rate", nphase3rate);
			   map.put("nphase4rate", nphase4rate);
			   map.put("nphase5rate", nphase5rate);
			   map.put("nphase6rate", nphase6rate);
			   map.put("eurlstatus", eurlstatus);
			String cidcodenums = VarUtil.RequestStr(request, "cidcodenums");
			String ctxbankcard = VarUtil.RequestStr(request, "ctxbankcard");
			String ctxbankname = VarUtil.RequestStr(request, "ctxbankname");
			String ctxbankprov = VarUtil.RequestStr(request, "ctxbankprov");
			String ctxbankcity = VarUtil.RequestStr(request, "ctxbankcity");
			String ctxbankbrch = VarUtil.RequestStr(request, "ctxbankbrch");
			String ccontacttele = VarUtil.RequestStr(request, "ccontacttele");
			String ccontactmail = VarUtil.RequestStr(request, "ccontactmail");
			String ccontactaddr = VarUtil.RequestStr(request, "ccontactaddr");
			String ccompanyurl1 = VarUtil.RequestStr(request, "ccompanyurl1");
			String cmanagername = VarUtil.RequestStr(request, "cmanagername");
			String cothersmemo1 = VarUtil.RequestStr(request, "cothersmemo1");
			String ccontactname2 = VarUtil.RequestStr(request, "ccontactname2");
			String ccardowner = VarUtil.RequestStr(request, "ccardowner");
				
			Map<String, Object> moreMap = new HashMap<String, Object>();
				moreMap.put("cidcodenums", cidcodenums);
				moreMap.put("ctxbankcard", ctxbankcard);
				moreMap.put("ctxbankname", ctxbankname);
				moreMap.put("ctxbankprov", ctxbankprov);
				moreMap.put("ctxbankcity", ctxbankcity);
				moreMap.put("ctxbankbrch", ctxbankbrch);
				moreMap.put("ccontacttele", ccontacttele);
				moreMap.put("ccontactname", ccontactname2);
				moreMap.put("ccardowner", ccardowner);
				moreMap.put("ccontactmail", ccontactmail);
				moreMap.put("ccontactaddr", ccontactaddr);
				moreMap.put("ccompanyurl1", ccompanyurl1);
				moreMap.put("cmanagername", cmanagername);
				moreMap.put("cothersmemo1", cothersmemo1);
		
				
				int res = merchantDao.updateObject(nagencyid ,map , moreMap);
				
				if(res==1){
					logOperateDao.add(request, LoginCommon.getAdminInfo(request).getAdminLoginname()+" 修改商户："+cregname+"成功", SysLogConstants.OP_TYPE_UPDATE, SysLogConstants.OP_STATUS_SUCCESS, "");
					return result(model, getAjaxMessage(1, "操作成功"));
				}else{
					return result(model, getAjaxMessage(0, "操作失败"));
				}				
		
		}
		
		
		JSONObject policy =merchantDao.getObject(nagencyid);
		
		if(policy != null){
			JSONObject supMer =merchantDao.getObject(policy.getString("nagencyup"));
			if(supMer != null){
				model.put("supMer", supMer.getString("cregname"));
			}
			model.putAll(policy);
			model.put("citylist", merchantDao.getAreaByFcode(policy.getString("ctxbankprov")));
		}
		
		model.put("area2list", merchantDao.getArea2("1"));
		model.put("banklist", merchantDao.getBank());
		//非保存入库操作,跳转到修改页面
		return "/merchant/merchant_edit";
	}
	
	
	@RequestMapping("merchant_detail")
	public String params_detail(Map<String, Object> model) {
		String nagencyid = VarUtil.RequestStr(request, "nagencyid");
		
		JSONObject policy =merchantDao.getObject(nagencyid);
		
		if(policy != null){
			
			JSONObject supMer =merchantDao.getObject(policy.getString("nagencyup"));
			if(supMer != null){
				model.put("supMer", supMer.getString("cregname"));
			}
			
			StringBuilder yinhang = new StringBuilder();
			StringBuilder rate = new StringBuilder();
			
			String ctxbankid=policy.getString("ctxbankname").trim();
			String ctxbankprov=policy.getString("ctxbankprov").trim();
			String ctxbankcity=policy.getString("ctxbankcity").trim();
			String ctxbankbrch=policy.getString("ctxbankbrch");
			
			/**
			 * 拼接银行和用户地区信息
			 */
			if(ctxbankid != null && !ctxbankid.isEmpty()){
				JSONObject obank = merchantDao.getBankInfoById(ctxbankid);
				if(obank != null){
					yinhang.append(obank.getString("bankname"));
					yinhang.append(",");
				}
			}
			
			if(ctxbankprov != null && !ctxbankprov.isEmpty()){
				JSONObject prov = merchantDao.getAreaById(ctxbankprov);
				if(prov != null){
					yinhang.append(prov.getString("areaname"));
					yinhang.append(",");
				}
			}
			if(ctxbankcity != null && !ctxbankcity.isEmpty()){
				JSONObject city = merchantDao.getAreaById(ctxbankcity);
				if(city != null){
					yinhang.append(city.getString("areaname"));
					yinhang.append(",");
				}
			}
			
			if(ctxbankbrch != null && !ctxbankbrch.isEmpty()){
				yinhang.append(ctxbankbrch);
			}
			
			if(yinhang != null && !yinhang.equals("null")){
				model.put("yinghang", yinhang);
			}
			
			
			String nphase1topv=policy.getString("nphase1topv");
			String nphase2topv=policy.getString("nphase2topv");
			String nphase3topv=policy.getString("nphase3topv");
			String nphase4topv=policy.getString("nphase4topv");
			String nphase5topv=policy.getString("nphase5topv");
			String nphase1rate=policy.getString("nphase1rate");
			String nphase2rate=policy.getString("nphase2rate");
			String nphase3rate=policy.getString("nphase3rate");
			String nphase4rate=policy.getString("nphase4rate");
			String nphase5rate=policy.getString("nphase5rate");
			
			/**
			 * 拼接用户费率信息
			 */
			if(nphase1topv != null && !nphase1topv.isEmpty() ){
				rate.append("0-"+nphase1topv+"万,"+nphase1rate+"%;");
				if(nphase2topv != null && !nphase2topv.isEmpty() ){
					rate.append(nphase1topv+"-"+nphase2topv+"万,"+nphase2rate+"%;");
					if(nphase3topv != null && !nphase3topv.isEmpty() ){
						rate.append(nphase2topv+"-"+nphase3topv+"万,"+nphase3rate+"%;");
						if(nphase4topv != null && !nphase4topv.isEmpty() ){
							rate.append(nphase3topv+"-"+nphase4topv+"万,"+nphase4rate+"%;");
							if(nphase5topv != null && !nphase5topv.isEmpty() ){
								rate.append(nphase4topv+"-"+nphase5topv+"万,"+nphase5rate+"%");
							}
						}
					}
				}
			}
			
			//费率信息不为空时显示
			if(!rate.equals("null")){
				model.put("ratestr", rate);
			}
			
			model.putAll(policy);
		}
		
		return "/merchant/merchant_detail";
	}
	
 	
 	/**
 	 * action 获取休市计划列表
 	 * @param model
 	 * @return
 	 */
 	@RequestMapping("merchant_list")
	public String getList(Map<String, Object> model) {
 		
 		String emaintype = VarUtil.RequestStr(request, "emaintype").trim();
 		String ntreedeep = VarUtil.RequestStr(request, "ntreedeep").trim();
		
		String search_type = VarUtil.RequestStr(request, "search_type").trim();
		String keyword = VarUtil.RequestStr(request, "keyword").trim();
		
		String cmanagername = VarUtil.RequestStr(request, "cmanagername").trim();
 		
		int curPage = VarUtil.RequestInt(request, "curPage");
		if(curPage <= 0){
			curPage = 1;
		}
		JSONObject queryData = new JSONObject();
		queryData.put("emaintype", emaintype);
		queryData.put("ntreedeep", ntreedeep);
		queryData.put("keyword", keyword);
		queryData.put("search_type", search_type);
		queryData.put("cmanagername", cmanagername);
		
		model.putAll(queryData);
				
		JSONObject roleListData = merchantDao.getList(pageSize, curPage, queryData);
		model.put("mangernameList", merchantDao.getManagernameList());
		
		if(roleListData!=null){
			logger.info("查询参数列表");
			
			model.put("curPage", curPage);
			model.put("pageSize", pageSize);
			model.put("roleList", roleListData.getJSONArray("list"));
			
			HashMap<String, Object> pagemodels = new HashMap<String, Object>();
			pagemodels.putAll(queryData);
			PageUtil.page2("/merchant/merchant_list.do", curPage, pageSize, roleListData.getIntValue("count"), pagemodels,model);
		}
		
		logger.info("查询参数列表成功");
		return "/merchant/merchant_list";
	}
 
 	/**
	 * 解除或锁定
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("updateMasterState")
	public String updateMasterState(Map<String, Object> model) {

		String nagencyid = VarUtil.RequestStr(request, "nagencyid");
		String type = VarUtil.RequestStr(request, "type");

		int ret = merchantDao.updateStatus(nagencyid, type);
		if (ret > 0) {
			logOperateDao.add(request, LoginCommon.getAdminInfo(request).getAdminLoginname()+" 修改商户："+nagencyid+"状态为："+type, SysLogConstants.OP_TYPE_UPDATE, SysLogConstants.OP_STATUS_SUCCESS, "");
			return result(model, getAjaxMessage(1, "操作成功"));
		} else {
			return result(model, getAjaxMessage(0, "操作失败"));
		}
	}
	
	
	/**
	 * 重置站主密码
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("resetMasterPwd")
	public String resetPwd(Map<String, Object> model) {

		String nagencyid = VarUtil.RequestStr(request, "nagencyid");

		if (merchantDao.resetPwd(nagencyid, MD5Util.MD5Encode(pwd)) > 0) {
			logOperateDao.add(request, LoginCommon.getAdminInfo(request).getAdminLoginname()+" 重置商户："+nagencyid+"密码成功", SysLogConstants.OP_TYPE_UPDATE, SysLogConstants.OP_STATUS_SUCCESS, "");
			return result(model, getAjaxMessage(1, "操作成功"));
		} else {
			return result(model, getAjaxMessage(0, "操作失败"));
		}

	}
	
	
	@RequestMapping("getCityList")
	public String getWeekList(Map<String, Object> model) {
 		
 		String fcode = VarUtil.RequestStr(request, "fcode").trim();

		JSONArray roleList = merchantDao.getAreas(fcode);
		
    	return result(model,roleList.toJSONString());
	}
	
	
	/**
 	 * action 获取休市计划列表
 	 * @param model
 	 * @return
 	 */
 	@RequestMapping("member_list")
	public String getMemberInfoList(Map<String, Object> model) {
 		String begin_time = RequestUtil.getStringParam(request, "begin_time",DateUtil.getDay(-7)).trim();
		String end_time = RequestUtil.getStringParam(request, "end_time",DateUtil.getDay(0)).trim();
		
 		String crefercode = VarUtil.RequestStr(request, "crefercode").trim();
 		String nagencyid = VarUtil.RequestStr(request, "nagencyid").trim();
		
		String search_type = VarUtil.RequestStr(request, "search_type").trim();
		String keyword = VarUtil.RequestStr(request, "keyword").trim();
		
		String area1 = VarUtil.RequestStr(request, "admin_area1").trim();
		String area2 = VarUtil.RequestStr(request, "admin_area2").trim();
		String area3 = VarUtil.RequestStr(request, "admin_area3").trim();
		
		/**
		 * 根据用户选择最后一级商户查询
		 */
		if(!area3.isEmpty()){
			nagencyid = area3;
		}else if(!area2.isEmpty()){
			nagencyid = area2;
		}else if(!area1.isEmpty()){
			nagencyid = area1;
		}else{
			
		}
		
		int curPage = VarUtil.RequestInt(request, "curPage");
		if(curPage <= 0){
			curPage = 1;
		}
		JSONObject queryData = new JSONObject();
		/**
		 * 当查询条件精确到指定记录时，只按查询参数显示，不再按其他条件查询
		 */
		if(search_type != null && !search_type.isEmpty()){
			queryData.put("keyword", keyword);
			queryData.put("search_type", search_type);
			model.put("area1", "");
			model.put("area2", "");
			model.put("area3", "");
		}else{
			queryData.put("begin_time", begin_time);
			queryData.put("end_time", end_time);
			queryData.put("crefercode", crefercode);
			queryData.put("nagencyid", nagencyid);
			model.put("area1", area1);
			model.put("area2", area2);
			model.put("area3", area3);
		}
		
		model.putAll(queryData);
				
		JSONObject roleListData = memberDao.getMemberList(pageSize, curPage, queryData);
		
		
		if(roleListData!=null){
			logger.info("查询参数列表");
			
			JSONArray list = new JSONArray();
			JSONArray js = roleListData.getJSONArray("list");
			for(int i=0; i< js.size(); i++){
				JSONObject ob = (JSONObject) js.get(i);
				String usercode = ob.getString("cusercode");
				JSONObject yes = memberDao.getCustomerYes(usercode);//昨天
				JSONObject mon = memberDao.getCustomerMon(usercode);//本月
				JSONObject cur = memberDao.getCustomerCur(usercode);//上月
				JSONObject lji = memberDao.getCustomerLji(usercode);//累计
				ob.put("t1", yes.getString("s1")==null?0:yes.getString("s1"));
				ob.put("t2", mon.getString("s2")==null?0:mon.getString("s2"));
				ob.put("t3", cur.getString("s3")==null?0:cur.getString("s3"));
				ob.put("t4", lji.getString("s4")==null?0:lji.getString("s4"));
				list.add(ob);
			}
			model.put("curPage", curPage);
			model.put("pageSize", pageSize);
			model.put("roleList", list);
			
			HashMap<String, Object> pagemodels = new HashMap<String, Object>();
			pagemodels.putAll(queryData);
			PageUtil.page2("/merchant/member_list.do", curPage, pageSize, roleListData.getIntValue("count"), pagemodels,model);
		}
	
		logger.info("查询参数列表成功");
		return "/merchant/member_list";
	}
 	
 	
 	@RequestMapping("getMerchantByLevel")
	public String getRoleListByLevel(Map<String, Object> model) {
		String level = VarUtil.RequestStr(request, "level").trim();
		JSONArray roleList = memberDao.getMerchantListByLevel(level); 
		return result(model,roleList.toJSONString());
	}
 	
 	@RequestMapping("getMerchantByCode")
	public String getMerchantListByCreferCode(Map<String, Object> model) {
		String creferCode = VarUtil.RequestStr(request, "creferCode").trim();
		JSONArray roleList = memberDao.getMerchantListByCreferCode(creferCode); 
		return result(model,roleList.toJSONString());
	}
 
}
