package com.huacai.web.controller.cp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import libcore.util.EncodeUtil;
import libcore.util.StringUtil;
import libcore.util.VarUtil;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huacai.web.common.LoginCommon;
import com.huacai.web.common.SysLogConstants;
import com.huacai.web.controller.BaseController;
import com.huacai.web.dao.cp.CpDao;

@Controller
@RequestMapping("/cp")
@Scope("prototype")
public class MyInfoController extends BaseController {
	protected final static Logger logger = LogManager.getLogger(MyInfoController.class);
	@Autowired
	private CpDao cpDao;
		
	
	/**
	 * 个人信息
	 * @param model
	 * @return
	 */
	@RequestMapping("/my_info")
	public String my_info(Map<String, Object> model) {
		String act = VarUtil.RequestStr(request, "act");
		String where = VarUtil.RequestStr(request, "where");
		long admin_id = LoginCommon.getAdminId(request);
		if(act.equals("save")){
			String admin_realname = VarUtil.RequestStr(request, "admin_realname");
			String admin_department = VarUtil.RequestStr(request, "admin_department");
			String admin_tel = VarUtil.RequestStr(request, "admin_tel");
			String admin_mobile = VarUtil.RequestStr(request, "admin_mobile");
			String admin_email = VarUtil.RequestStr(request, "admin_email");
			int admin_sex = VarUtil.RequestInt(request, "admin_sex");
			
			Map<String, Object> data = new HashMap<String, Object>();
			
			data.put("admin_realname", admin_realname);
			data.put("admin_department", admin_department);
			data.put("admin_tel", admin_tel);
			data.put("admin_mobile", admin_mobile);
			data.put("admin_email", admin_email);
			data.put("admin_sex", admin_sex);
			
			data.put("admin_update_time", new Date());
			
			if(cpDao.updateAdmin(admin_id, data)>0){
				logOperateDao.add(request, "登录管理员修改信息", SysLogConstants.OP_TYPE_UPDATE, SysLogConstants.OP_STATUS_SUCCESS, "");
				return result(model, getAjaxMessage(1, "修改成功"));
			}else{
				logOperateDao.add(request, "登录管理员修改信息", SysLogConstants.OP_TYPE_UPDATE, SysLogConstants.OP_STATUS_FAILED, "");
				return result(model, getAjaxMessage(0, "修改失败"));
			}
		}else if(act.equals("save_pwd")){
			String admin_password_hash_old = VarUtil.RequestStr(request, "admin_password_hash_old");
			
			//验证旧密码
			JSONObject adminData = cpDao.getAdmin(admin_id);
			String admin_password = VarUtil.RequestStr(request, "admin_password_hash");
			String admin_password_old = adminData.getString("admin_password");
			String admin_password_salt_old = adminData.getString("admin_password_salt");
			if(!EncodeUtil.MD5(admin_password_hash_old+admin_password_salt_old).equals(admin_password_old)){
				logOperateDao.add(request, "登录管理员修改密码", SysLogConstants.OP_TYPE_UPDATE, SysLogConstants.OP_STATUS_FAILED, "");
				return result(model, getAjaxMessage(0, "旧密码错误"));
			}
			if(EncodeUtil.MD5(admin_password_hash_old+admin_password_salt_old).equals(EncodeUtil.MD5(admin_password+admin_password_salt_old))){
				logOperateDao.add(request, "登录管理员修改密码", SysLogConstants.OP_TYPE_UPDATE, SysLogConstants.OP_STATUS_FAILED, "");
				return result(model, getAjaxMessage(0, "新密码不能与旧密码相同"));
			}
			
			
			String admin_password_salt = StringUtil.getRandomString(6, true);
			
			if(cpDao.updateAdminPwd(admin_id, EncodeUtil.MD5(admin_password+admin_password_salt), admin_password_salt)>0){
				//修改完密码更新登录session中的用户信息
				JSONObject json = cpDao.getAdmin(admin_id);
				LoginCommon.regAdminInfoSession(request, cpDao, json);
				
				logOperateDao.add(request, "登录管理员修改密码", SysLogConstants.OP_TYPE_UPDATE, SysLogConstants.OP_STATUS_SUCCESS, "");
				
				LoginCommon.clearLoginSession(request);
				return result(model, getAjaxMessage(1, "密码修改成功，请重新登录"));
			}else{
				logOperateDao.add(request, "登录管理员修改密码", SysLogConstants.OP_TYPE_UPDATE, SysLogConstants.OP_STATUS_FAILED, "");
				return result(model, getAjaxMessage(0, "密码修改失败"));
				
			}
		}
		JSONObject adminData = cpDao.getAdmin(admin_id);
		if(adminData != null){
			model.putAll(adminData);
		}
		if("info".equals(where)){
			return "/cp/my_info_info";
		}else{
			return "/cp/my_info_pwd";
		}
		
	}
	
	/**
	 * action 修改参数
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("parm_site")
	public String getList(Map<String, Object> model) {
		try {
			String act = VarUtil.RequestStr(request, "act");
			if (act.equals("pwd_save")) {
				int idx = VarUtil.RequestInt(request, "idx");
				String oldPwdD = VarUtil.RequestStr(request, "oldPwdD");
				String repeatPwdD = VarUtil.RequestStr(request, "repeatPwdD");
				//密码种类
				String ptype = VarUtil.RequestStr(request, "ptype");
				//处理充值密码
				if(ptype.equals("1")){
					//充值库中密码更新
					String crealPwd = VarUtil.RequestStr(request, "crealPwd");
					if(EncodeUtil.MD5(oldPwdD).equals(crealPwd)){				
						Map<String, Object> data = new HashMap<String, Object>();			
						data.put("cpwd", EncodeUtil.MD5(repeatPwdD));
//						int res = agentmanageDao.updateObject(idx, data);
//						if (res == 1) {
//							logOperateDao.add(request, "充值密码修改", SysLogConstants.OP_TYPE_UPDATE, SysLogConstants.OP_STATUS_SUCCESS, "");
//							return result(model, getAjaxMessage(1, "修改成功"));
//						} else {
//							logOperateDao.add(request, "充值密码修改", SysLogConstants.OP_TYPE_UPDATE, SysLogConstants.OP_STATUS_FAILED, "");
//							return result(model, getAjaxMessage(0, "修改失败"));
//						}
						return null;
					}else{
						return result(model, getAjaxMessage(0, "充值原密码不正确"));
					}
				}else{
					//充值库中密码更新
					String trealPwd = VarUtil.RequestStr(request, "trealPwd");
					if(EncodeUtil.MD5(oldPwdD).equals(trealPwd)){				
						Map<String, Object> data = new HashMap<String, Object>();			
						data.put("tpwd", EncodeUtil.MD5(repeatPwdD));
//						int res = agentmanageDao.updateObject(idx, data);
//						if (res == 1) {
//							logOperateDao.add(request, "提现密码修改", SysLogConstants.OP_TYPE_UPDATE, SysLogConstants.OP_STATUS_SUCCESS, "");
//							return result(model, getAjaxMessage(1, "修改成功"));
//						} else {
//							logOperateDao.add(request, "提现密码修改", SysLogConstants.OP_TYPE_UPDATE, SysLogConstants.OP_STATUS_FAILED, "");
//							return result(model, getAjaxMessage(0, "修改失败"));
//						}
						return null;
					}else{
						return result(model, getAjaxMessage(0, "提现原密码不正确"));
					}
				}
			}else{
	 	 		//获取可用渠道列表
				JSONArray datas = new JSONArray();
//				datas = agentmanageDao.getPayAgent();
//	 	 		model.put("agentList", datas);
				// 非入库,跳转渠道修改页面
				return "/cp/parm_site";
			}
		} catch (Exception e) {
			logger.error("执行账户充值失败");
			e.printStackTrace();
			return null;
		}
	
	}
	
	/**
	 * action 修改参数
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("parm_siteT")
	public String getListT(Map<String, Object> model) {
		try {
			String act = VarUtil.RequestStr(request, "act");
			if (act.equals("pwd_save")) {
				//充值密码更新
				String repeatPwdD = VarUtil.RequestStr(request, "repeatPwdD");
				String realPwd = VarUtil.RequestStr(request, "realPwd");
				String oldPwdD = VarUtil.RequestStr(request, "oldPwdD");
				if(EncodeUtil.MD5(oldPwdD).equals(realPwd)){
					int idx = VarUtil.RequestInt(request, "idx");				
					Map<String, Object> data = new HashMap<String, Object>();			
					data.put("config_value", EncodeUtil.MD5(repeatPwdD));
					data.put("config_update_time", new Date());
					int res = cpDao.updateConfig(idx,data);
					if (res == 1) {
						logOperateDao.add(request, "充值提现密码修改", SysLogConstants.OP_TYPE_UPDATE, SysLogConstants.OP_STATUS_SUCCESS, "");
						return result(model, getAjaxMessage(1, "修改成功"));
					} else {
						logOperateDao.add(request, "充值提现密码修改", SysLogConstants.OP_TYPE_UPDATE, SysLogConstants.OP_STATUS_FAILED, "");
						return result(model, getAjaxMessage(0, "修改失败"));
					}	
				}else{
					return result(model, getAjaxMessage(0, "原密码不正确"));
				}				
			}else{
	 	 		//获取可用渠道列表
	 	 		JSONArray configList = new JSONArray();
	 	 		configList = cpDao.getConfigList();
	 	 		model.put("configList", configList);
				// 非入库,跳转渠道修改页面
				return "/cp/parm_site";
			}
		} catch (Exception e) {
			logger.error("执行账户充值失败");
			e.printStackTrace();
			return null;
		}
	
	}
}
