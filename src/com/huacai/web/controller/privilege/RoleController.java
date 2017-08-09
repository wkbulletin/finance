package com.huacai.web.controller.privilege;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import libcore.util.VarUtil;

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
import com.huacai.web.common.SysLogConstants;
import com.huacai.web.constant.BossConstants;
import com.huacai.web.controller.BaseController;
import com.huacai.web.dao.privilege.RoleDao;

@Controller
@RequestMapping("/privilege")
@Scope("prototype")
public class RoleController extends BaseController {
	protected final static Logger logger = LogManager.getLogger(RoleController.class);
	private int pageSize = 10;
	@Autowired
	private RoleDao roleDao;
	
	@RequestMapping("role_list")
	public String role_list(Map<String, Object> model) {
		
		String role_name = VarUtil.RequestStr(request, "role_name").trim();
		String role_status = VarUtil.RequestStr(request, "role_status");
		//String role_level = VarUtil.RequestStr(request, "role_level").trim();
		
		int curPage = VarUtil.RequestInt(request, "curPage");
		if(curPage <= 0){
			curPage = 1;
		}
		JSONObject queryData = new JSONObject();
		queryData.put("role_name", role_name);
		queryData.put("role_status", role_status);
		//queryData.put("role_level", role_level.length() > 0 ? VarUtil.intval(role_level) : 1);
		
		model.putAll(queryData);
		
		model.put("rolelevels", BossConstants.getSystemLevels());
		//model.put("role_level", role_level.length() > 0 ? VarUtil.intval(role_level) : 1);
		
		JSONObject roleListData = roleDao.getRoleList(pageSize, curPage, queryData);
		
		
		if(roleListData!=null){
			logOperateDao.add(request, "查询角色", SysLogConstants.OP_TYPE_QUERY, SysLogConstants.OP_STATUS_SUCCESS, "");
			
			model.put("curPage", curPage);
			model.put("pageSize", pageSize);
			model.put("roleList", roleListData.getJSONArray("list"));
			
			HashMap<String, Object> pageParams = new HashMap<String, Object>();
			pageParams.putAll(queryData);
			PageUtil.page2("/privilege/role_list.do", curPage, pageSize, roleListData.getIntValue("count"), pageParams,model);
			//model.put("pageStr", PageUtil.page("/privilege/role_list.do", curPage, pageSize, roleListData.getIntValue("count"), pageParams));
		}
	
		
		
		return "/privilege/role_list";
	}
	
	@RequestMapping("role_add")
	public String role_add(Map<String, Object> model) {
		String act = VarUtil.RequestStr(request, "act");
		//String role_level = VarUtil.RequestStr(request, "role_level").trim();
		if(act.equals("save")){
			String role_name = VarUtil.RequestStr(request, "role_name");
			int role_status = VarUtil.RequestInt(request, "role_status");
			String role_desc = VarUtil.RequestStr(request, "role_desc");
			
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("role_name", role_name);
			data.put("role_desc", role_desc);
			data.put("role_status", role_status);
			data.put("role_create_time", new Date());
			data.put("role_update_time", new Date());
			//data.put("role_level", role_level);
			
			if(roleDao.insertRole(data)>0){
				logOperateDao.add(request, "添加角色", SysLogConstants.OP_TYPE_INSERT, SysLogConstants.OP_STATUS_SUCCESS, "");
			}else{
				logOperateDao.add(request, "添加角色", SysLogConstants.OP_TYPE_INSERT, SysLogConstants.OP_STATUS_FAILED, "");
			}
			return result(model, getAjaxMessage(1, "添加成功"));
		}
		model.put("role_id", 0);
		model.put("role_status", 1);
		
		model.put("rolelevels", BossConstants.getSystemLevels());
		//model.put("role_level", role_level.length() > 0 ? VarUtil.intval(role_level) : 1);
		return "/privilege/role_add";
	}
	
	@RequestMapping("role_edit")
	public String role_edit(Map<String, Object> model) {
		String act = VarUtil.RequestStr(request, "act");
		int role_id = VarUtil.RequestInt(request, "role_id");
		if(act.equals("save")){
			
			
			String role_name = VarUtil.RequestStr(request, "role_name");
			int role_status = VarUtil.RequestInt(request, "role_status");
			String role_desc = VarUtil.RequestStr(request, "role_desc");
			
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("role_name", role_name);
			data.put("role_desc", role_desc);
			data.put("role_status", role_status);
			data.put("role_update_time", new Date());
			
			if(roleDao.updateRole(role_id, data)>0){
				logOperateDao.add(request, "修改角色", SysLogConstants.OP_TYPE_UPDATE, SysLogConstants.OP_STATUS_SUCCESS, "");
			}else{
				logOperateDao.add(request, "修改角色", SysLogConstants.OP_TYPE_UPDATE, SysLogConstants.OP_STATUS_FAILED, "");
			}
			
			return result(model, getAjaxMessage(1, "修改成功"));
		}
		JSONObject roleData = roleDao.getRole(role_id);
		model.putAll(roleData);
		return "/privilege/role_edit";

	}
	
	@RequestMapping("role_status")
	public String role_status(Map<String, Object> model) {
		int role_id = VarUtil.RequestInt(request, "role_id");
		
		if(roleDao.updateRoleStauts(role_id)>0){
			logOperateDao.add(request, "修改角色状态", SysLogConstants.OP_TYPE_UPDATE, SysLogConstants.OP_STATUS_SUCCESS, "");
		}else{
			logOperateDao.add(request, "修改角色状态", SysLogConstants.OP_TYPE_UPDATE, SysLogConstants.OP_STATUS_FAILED, "");
		}
		JSONObject roleData = roleDao.getRole(role_id);
		JSONObject result = new JSONObject();
		result.put("role_status", roleData.get("role_status"));
		
		return result(model, getAjaxMessage(1, "修改成功", result));
	}
	
	@RequestMapping("role_priv")
	public String role_priv(Map<String, Object> model) {
		String act = VarUtil.RequestStr(request, "act");
		int role_id = VarUtil.RequestInt(request, "role_id");
		if(act.equals("save")){
			String privilege_list = VarUtil.RequestStr(request, "privilege_list");
			
			String[] privArr = privilege_list.split(",");
			List<Object[]> batchArgs = new ArrayList<Object[]>();
			Object[] batchObject;
			int privIdInt;
			for(String privId : privArr){
				privIdInt = VarUtil.intval(privId);
				if(privIdInt>0){
					batchObject = new Object[]{role_id, privIdInt};
					batchArgs.add(batchObject);
				}
			}
			
			if(roleDao.batchUpdateRolePriv(role_id, batchArgs)){
				logOperateDao.add(request, "修改角色权限", SysLogConstants.OP_TYPE_UPDATE, SysLogConstants.OP_STATUS_SUCCESS, "");
			}else{
				logOperateDao.add(request, "修改角色权限", SysLogConstants.OP_TYPE_UPDATE, SysLogConstants.OP_STATUS_FAILED, "");
			}
			return result(model, getAjaxMessage(1, "修改成功"));
		}
		JSONObject roleData = roleDao.getRole(role_id);
		model.putAll(roleData);
		
		JSONArray jsonList = roleDao.getRolePrivList(role_id);
		JSONArray ztreeData = new JSONArray();
		JSONObject ztreeRow,row;
		Integer rid;
		for(int i = 0, size = jsonList.size(); i < size; i++){
			row = jsonList.getJSONObject(i);
			ztreeRow = new JSONObject();
			ztreeRow.put("id", row.getIntValue("privilege_id"));
			ztreeRow.put("pId", row.getIntValue("privilege_pid"));
			ztreeRow.put("name", row.getString("privilege_name"));
			ztreeRow.put("open", false);
			rid = row.getInteger("relation_priv_id");
			if(rid!=null&&rid.intValue()>0){
				ztreeRow.put("checked", true);
			}else{
				ztreeRow.put("checked", false);
			}
			ztreeData.add(ztreeRow);
		}
		model.put("treeNodes", ztreeData.toJSONString());
		
		return "/privilege/role_priv";
	}
	
	@RequestMapping("role_del")
	public String role_del(Map<String, Object> model) {
		int role_id = VarUtil.RequestInt(request, "role_id");
		if(roleDao.delRole(role_id)>0){
			logOperateDao.add(request, "删除角色", SysLogConstants.OP_TYPE_DEL, SysLogConstants.OP_STATUS_SUCCESS, "");
		}else{
			logOperateDao.add(request, "删除角色", SysLogConstants.OP_TYPE_DEL, SysLogConstants.OP_STATUS_FAILED, "");
		}
		
		String url = "/privilege/role_list.do";
		String refer = request.getHeader("referer");
		if(refer!=null&&refer.length()>0&&refer.indexOf(url+"?")>0){
			url = refer;
		}
		return "redirect:"+url;
	}
	
	
	@RequestMapping("role_check")
	@ResponseBody
	public String role_check(Map<String, Object> model) throws IOException{
		response.setContentType("text/html;charset=UTF-8");
		String act = VarUtil.RequestStr(request, "act");
		
		if(act.equals("name")){
			int role_id = VarUtil.RequestInt(request, "role_id");
			String role_name = VarUtil.RequestStr(request, "role_name");
			//int role_level = VarUtil.RequestInt(request, "role_level");
			if(role_id<1){
				role_id = -1;
			}
			int result = roleDao.checkRoleNameIsExists(role_id, role_name);
			if(result == 1){
				return "false";
				//return result(model, getAjaxMessage(2, "名称已存在"));
			}else if (result==0){
				return "true";
				//return result(model, getAjaxMessage(1, "名称可用"));
			}else{
				//return result(model, getAjaxMessage(0, "查询失败"));
				return "false";
			}
		}
		//return result(model, getAjaxMessage(0, "查询失败"));
		return "false";
	}
	
}
