package com.huacai.web.controller.privilege;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.collections.MapUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huacai.util.PageUtil;
import com.huacai.web.common.LoginCommon;
import com.huacai.web.common.SysLogConstants;
import com.huacai.web.constant.BossConstants;
import com.huacai.web.controller.BaseController;
import com.huacai.web.dao.privilege.AdminDao;
import com.huacai.web.dao.privilege.AdminMyDao;

import libcore.util.EncodeUtil;
import libcore.util.StringUtil;
import libcore.util.VarUtil;

@Controller
@RequestMapping("/privilege")
@Scope("prototype")
public class AdminController extends BaseController {
	protected final static Logger logger = LogManager.getLogger(AdminController.class);
	private int pageSize = 15;
	@Autowired
	private AdminDao adminDao;
	@Autowired
	private AdminMyDao adminMyDao;

	private static TreeMap<Integer, String> adminTypes;
	static {
		adminTypes = new TreeMap<Integer, String>();
		
		adminTypes.put(0, "管理员");
//		adminTypes.put(1, "业务管理员");
	}
	
	
	@RequestMapping("admin_list")
	public String admin_list(Map<String, Object> model) {
		String admin_loginname = VarUtil.RequestStr(request, "admin_loginname").trim();
		String admin_realname = VarUtil.RequestStr(request, "admin_realname").trim();
		String admin_status = VarUtil.RequestStr(request, "admin_status").trim();
		String admin_type = VarUtil.RequestStr(request, "admin_type").trim();
		String admin_level = VarUtil.RequestStr(request, "admin_level").trim();
//		if(admin_status.length()==0){
//			admin_status = "1";
//		}else if(admin_status.equals("*")){
//			admin_status = "";
//		}

		int curPage = VarUtil.RequestInt(request, "curPage");
		if (curPage <= 0) {
			curPage = 1;
		}
		JSONObject queryData = new JSONObject();
		queryData.put("admin_loginname", admin_loginname);
		queryData.put("admin_realname", admin_realname);
		queryData.put("admin_status", admin_status);
		queryData.put("admin_type", admin_type);
		//queryData.put("admin_level", admin_level.length() > 0 ? VarUtil.intval(admin_level) : 1);

		model.putAll(queryData);
		
		model.put("adminlevels", BossConstants.getSystemLevels());
		//model.put("admin_level", admin_level.length() > 0 ? VarUtil.intval(admin_level) : 1);
		
		model.put("adminTypes", getAdminTypes());
		model.put("admin_type", admin_type.length() > 0 ? VarUtil.intval(admin_type) : -1);

		
		JSONObject adminListData = adminDao.getAdminList(pageSize, curPage, queryData);

		if (adminListData != null) {
			logOperateDao.add(request, "查询管理员", SysLogConstants.OP_TYPE_QUERY, SysLogConstants.OP_STATUS_SUCCESS, "");
			JSONArray rows = adminListData.getJSONArray("list");
			for(int i = 0; i < rows.size(); i++){
				rows.getJSONObject(i).put("admin_type_name", adminTypes.get(rows.getJSONObject(i).getIntValue("admin_type")));
				rows.getJSONObject(i).put("login_status", LoginCommon.checkUserLoginStatus(request,rows.getJSONObject(i).getString("admin_loginname")));
				//System.out.println(rows.getJSONObject(i).getBooleanValue("login_status"));
			}
			
			model.put("curPage", curPage);
			model.put("pageSize", pageSize);
			model.put("adminList", rows);

			HashMap<String, Object> pageParams = new HashMap<String, Object>();
			pageParams.putAll(queryData);
			//model.put("pageStr", PageUtil.page("/privilege/admin_list.do", curPage, pageSize, adminListData.getIntValue("count"), pageParams));
			PageUtil.page2("/privilege/admin_list.do", curPage, pageSize, adminListData.getIntValue("count"), pageParams,model);
		}

		return "/privilege/admin_list";
	}

	@RequestMapping("admin_print")
	public String admin_print(Map<String, Object> model) {
		String admin_loginname = VarUtil.RequestStr(request, "admin_loginname");
		String admin_realname = VarUtil.RequestStr(request, "admin_realname");
		String admin_status = VarUtil.RequestStr(request, "admin_status");

		int curPage = 1;

		JSONObject queryData = new JSONObject();
		queryData.put("admin_loginname", admin_loginname);
		queryData.put("admin_realname", admin_realname);
		queryData.put("admin_status", admin_status);
		//queryData.put("admin_level", BossConstants.SYS_LEVEL);

		model.putAll(queryData);

		JSONObject adminListData = adminDao.getAdminList(pageSize, curPage, queryData);
		if (adminListData != null) {
			model.put("adminList", adminListData.getJSONArray("list"));

		}
		// 如果打印记录过多可以提示用户错误，return "/common/print_error";
		return "/privilege/admin_print";
	}

	@RequestMapping("admin_export")
	public void admin_export(Map<String, Object> model) {
		String admin_loginname = VarUtil.RequestStr(request, "admin_loginname");
		String admin_realname = VarUtil.RequestStr(request, "admin_realname");
		String admin_status = VarUtil.RequestStr(request, "admin_status");

		int curPage = 1;

		JSONObject queryData = new JSONObject();
		queryData.put("admin_loginname", admin_loginname);
		queryData.put("admin_realname", admin_realname);
		queryData.put("admin_status", admin_status);
		//queryData.put("admin_level", BossConstants.SYS_LEVEL);

		model.putAll(queryData);

		JSONObject adminListData = adminDao.getAdminList(pageSize, curPage, queryData);
		if (adminListData != null) {
			model.put("info", "管理员基本信息");
			model.put("adminList", adminListData.getJSONArray("list"));

		}
		exportXls("/privilege/admin_list.xls", "系统用户列表.xls", model);

	}

	@RequestMapping("admin_add")
	public String admin_add(Map<String, Object> model) {
		String act = VarUtil.RequestStr(request, "act");
		if (act.equals("save")) {
			String admin_loginname = VarUtil.RequestStr(request, "admin_loginname");
			int admin_status = VarUtil.RequestInt(request, "admin_status");
			String admin_password = VarUtil.RequestStr(request, "admin_password_hash");
			String admin_password_salt = StringUtil.getRandomString(6, true);
			String admin_realname = VarUtil.RequestStr(request, "admin_realname");
			String admin_department = VarUtil.RequestStr(request, "admin_department");
			String admin_tel = VarUtil.RequestStr(request, "admin_tel");
			String admin_mobile = VarUtil.RequestStr(request, "admin_mobile");
			String admin_email = VarUtil.RequestStr(request, "admin_email");
			int admin_sex = VarUtil.RequestInt(request, "admin_sex");
			int admin_type = VarUtil.RequestInt(request, "admin_type");
			int admin_level = VarUtil.RequestInt(request, "admin_level");
			
			String areaCode = VarUtil.RequestStr(request, "areaCode");
			int areaLevel = VarUtil.RequestInt(request, "areaLevel");

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("admin_loginname", admin_loginname.toLowerCase());
			data.put("admin_status", admin_status);
			data.put("admin_password", EncodeUtil.MD5(admin_password + admin_password_salt));
			data.put("admin_password_salt", admin_password_salt);
			data.put("admin_realname", admin_realname);
			data.put("admin_department", admin_department);
			data.put("admin_tel", admin_tel);
			data.put("admin_mobile", admin_mobile);
			data.put("admin_email", admin_email);
			data.put("admin_sex", admin_sex);
			data.put("admin_type", admin_type);

			data.put("admin_create_time", new Date());
			data.put("admin_update_time", new Date());
			//data.put("admin_level", admin_level);
			

			long adminId =0;
			adminMyDao.insertAdmin(data);
			adminId = MapUtils.getLongValue(data,"adminId");
			if (adminId > 0) {
				String[] role_ids = request.getParameterValues("role_ids");

				List<Object[]> batchArgs = new ArrayList<Object[]>();
				Object[] batchObject;
				int roleIdInt;
				if (role_ids != null && role_ids.length > 0) {
					for (String roleId : role_ids) {
						roleIdInt = VarUtil.intval(roleId);
						if (roleIdInt > 0) {
							batchObject = new Object[] { adminId, roleIdInt };
							batchArgs.add(batchObject);
						}
					}
					adminDao.batchUpdateAdminRole(adminId, batchArgs);
				}
			}
			if (adminId > 0) {
				logOperateDao.add(request, "添加管理员", SysLogConstants.OP_TYPE_INSERT, SysLogConstants.OP_STATUS_SUCCESS, "");
				return result(model, getAjaxMessage(1, "添加成功"));
			} else {
				logOperateDao.add(request, "添加管理员", SysLogConstants.OP_TYPE_INSERT, SysLogConstants.OP_STATUS_FAILED, "");
				return result(model, getAjaxMessage(1, "添加失败"));
			}

		}
		model.put("admin_id", 0);
		model.put("admin_status", 1);

		JSONArray roleList = adminDao.getRoleList(-1);
		model.put("roleList", roleList);
		//model.put("admin_level", 1);
		model.put("adminlevels", BossConstants.getSystemLevels());
		model.put("admin_type", 1);
		model.put("adminTypes", getAdminTypes());
		

		return "/privilege/admin_add";
	}
	
	@RequestMapping("getRoleListByLevel")
	public String getRoleListByLevel(Map<String, Object> model) {
		int level = VarUtil.RequestInt(request, "level");
		JSONArray roleList = adminDao.getRoleList(-1); 
		return result(model,roleList.toJSONString());
	}

	@RequestMapping("admin_edit")
	public String admin_edit(Map<String, Object> model) {
		String act = VarUtil.RequestStr(request, "act");
		int admin_id = VarUtil.RequestInt(request, "admin_id");
		if (act.equals("save")) {

			int admin_status = VarUtil.RequestInt(request, "admin_status");
			String admin_password = VarUtil.RequestStr(request, "admin_password_hash");
			String admin_password_salt = StringUtil.getRandomString(6, true);
			String admin_realname = VarUtil.RequestStr(request, "admin_realname");
			String admin_department = VarUtil.RequestStr(request, "admin_department");
			String admin_tel = VarUtil.RequestStr(request, "admin_tel");
			String admin_mobile = VarUtil.RequestStr(request, "admin_mobile");
			String admin_email = VarUtil.RequestStr(request, "admin_email");
			int admin_sex = VarUtil.RequestInt(request, "admin_sex");
			int admin_type = VarUtil.RequestInt(request, "admin_type");

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("admin_status", admin_status);
			if (admin_password.length() > 0) {
				data.put("admin_password", EncodeUtil.MD5(admin_password + admin_password_salt));
				data.put("admin_password_salt", admin_password_salt);
			}
			data.put("admin_realname", admin_realname);
			data.put("admin_department", admin_department);
			data.put("admin_tel", admin_tel);
			data.put("admin_mobile", admin_mobile);
			data.put("admin_email", admin_email);
			data.put("admin_sex", admin_sex);
			data.put("admin_type", admin_type);

			data.put("admin_update_time", new Date());

			if (adminDao.updateAdmin(admin_id, data) > 0) {
				logOperateDao.add(request, "修改管理员", SysLogConstants.OP_TYPE_UPDATE, SysLogConstants.OP_STATUS_SUCCESS, "");
				return result(model, getAjaxMessage(1, "编辑成功"));
			} else {
				logOperateDao.add(request, "修改管理员", SysLogConstants.OP_TYPE_UPDATE, SysLogConstants.OP_STATUS_FAILED, "");
				return result(model, getAjaxMessage(0, "编辑失败"));

			}

		}
		JSONObject adminData = adminDao.getAdmin(admin_id);
		model.putAll(adminData);
		model.put("adminTypes", getAdminTypes());
		return "/privilege/admin_edit";
	}

	@RequestMapping("admin_pwd")
	public String admin_pwd(Map<String, Object> model) {
		String act = VarUtil.RequestStr(request, "act");
		int admin_id = VarUtil.RequestInt(request, "admin_id");
		if (act.equals("save")) {
			String admin_password = VarUtil.RequestStr(request, "admin_password_hash");
			String admin_password_salt = StringUtil.getRandomString(6, true);

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("admin_password", EncodeUtil.MD5(admin_password + admin_password_salt));
			data.put("admin_password_salt", admin_password_salt);
			data.put("admin_pwd_updates", 0);

			data.put("admin_update_time", new Date());

			if (adminDao.updateAdmin(admin_id, data) > 0) {
				logOperateDao.add(request, "修改管理员密码", SysLogConstants.OP_TYPE_UPDATE, SysLogConstants.OP_STATUS_SUCCESS, "");
				return result(model, getAjaxMessage(1, "编辑成功"));
			} else {
				logOperateDao.add(request, "修改管理员密码", SysLogConstants.OP_TYPE_UPDATE, SysLogConstants.OP_STATUS_FAILED, "");
				return result(model, getAjaxMessage(0, "编辑失败"));

			}
		}
		JSONObject adminData = adminDao.getAdmin(admin_id);
		model.putAll(adminData);
		return "/privilege/admin_pwd";
	}

	@RequestMapping("admin_detail")
	public String admin_detail(Map<String, Object> model) {
		int admin_id = VarUtil.RequestInt(request, "admin_id");
		JSONObject adminData = adminDao.getAdmin(admin_id);
		model.putAll(adminData);
		model.put("admin_type_name", adminTypes.get(adminData.getIntValue("admin_type")));
		
		return "/privilege/admin_detail";
	}

	@RequestMapping("admin_role")
	public String admin_role(Map<String, Object> model) {
		String act = VarUtil.RequestStr(request, "act");
		int admin_id = VarUtil.RequestInt(request, "admin_id");
		if (act.equals("save")) {
			String[] role_ids = request.getParameterValues("role_ids");

			List<Object[]> batchArgs = new ArrayList<Object[]>();
			Object[] batchObject;
			int roleIdInt;
			if (role_ids != null && role_ids.length > 0) {
				for (String roleId : role_ids) {
					roleIdInt = VarUtil.intval(roleId);
					if (roleIdInt > 0) {
						batchObject = new Object[] { admin_id, roleIdInt };
						batchArgs.add(batchObject);
					}
				}
			}
			adminDao.batchUpdateAdminRole(admin_id, batchArgs);
			logOperateDao.add(request, "修改管理员角色", SysLogConstants.OP_TYPE_UPDATE, SysLogConstants.OP_STATUS_SUCCESS, "");

			return result(model, getAjaxMessage(1, "编辑成功"));
		}
		JSONObject adminData = adminDao.getAdmin(admin_id);
		model.putAll(adminData);

		JSONArray roleList = adminDao.getRoleList(admin_id);
		model.put("roleList", roleList);

		return "/privilege/admin_role";
	}

	@RequestMapping("admin_del")
	public String admin_del(Map<String, Object> model) {
		int admin_id = VarUtil.RequestInt(request, "admin_id");

		if (adminDao.delAdmin(admin_id) > 0) {
			logOperateDao.add(request, "删除管理员", SysLogConstants.OP_TYPE_DEL, SysLogConstants.OP_STATUS_SUCCESS, "");
		} else {
			logOperateDao.add(request, "删除管理员", SysLogConstants.OP_TYPE_DEL, SysLogConstants.OP_STATUS_FAILED, "");
		}

		String url = "/privilege/admin_list.do";
		String refer = request.getHeader("referer");
		if (refer != null && refer.length() > 0 && refer.indexOf(url + "?") > 0) {
			url = refer;
		}
		return "redirect:" + url;
	}

	@RequestMapping("admin_status")
	public String admin_status(Map<String, Object> model) {
		int admin_id = VarUtil.RequestInt(request, "admin_id");

		if (adminDao.updateAdminStauts(admin_id) > 0) {
			logOperateDao.add(request, "更新管理员状态", SysLogConstants.OP_TYPE_UPDATE, SysLogConstants.OP_STATUS_SUCCESS, "");
		} else {
			logOperateDao.add(request, "更新管理员状态", SysLogConstants.OP_TYPE_UPDATE, SysLogConstants.OP_STATUS_FAILED, "");
		}
		JSONObject roleData = adminDao.getAdmin(admin_id);
		JSONObject result = new JSONObject();
		result.put("admin_status", roleData.get("admin_status"));

		return result(model, getAjaxMessage(1, "修改成功", result));
	}

	@RequestMapping("admin_check")
	@ResponseBody
	public String admin_check(Map<String, Object> model) {
		String act = VarUtil.RequestStr(request, "act");
		if (act.equals("loginname")) {
			int admin_id = VarUtil.RequestInt(request, "admin_id");
			int admin_level = VarUtil.RequestInt(request, "admin_level");
			String admin_loginname = VarUtil.RequestStr(request, "admin_loginname");
			if (admin_id < 1) {
				admin_id = -1;
			}
			int result = adminDao.checkAdminLoginNameIsExists(admin_id, admin_loginname);
			if (result == 1) {
				return "false";
				//return result(model, getAjaxMessage(2, "名称已存在"));
			} else if (result == 0) {
				return "true";
				//return result(model, getAjaxMessage(1, "名称可用"));
			} else {
				return "false";
				//return result(model, getAjaxMessage(0, "查询失败"));
			}
		}
		return "false";
		//return result(model, getAjaxMessage(0, "查询失败"));
	}
	
	
	public List<Map<String,String>> getAdminTypes() {
		Iterator<Integer> iter = adminTypes.keySet().iterator();
		Integer key;
		String val;
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Map<String,String> row;
		while (iter.hasNext()) {
			key = iter.next();
			val = adminTypes.get(key);
			row = new HashMap<String, String>();
			row.put("key", key.toString());
			row.put("val", val);
			list.add(row);
		}
		return list;
	}
}
