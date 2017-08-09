package com.huacai.web.controller.privilege;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import libcore.util.ArrayUtil;
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
import com.huacai.web.constant.BossConstants;
import com.huacai.web.controller.BaseController;
import com.huacai.web.dao.privilege.PrivilegeDao;

@Controller
@RequestMapping("/privilege")
@Scope("prototype")
public class PrivilegeController extends BaseController {
	protected final static Logger logger = LogManager.getLogger(PrivilegeController.class);
	
	@Autowired
	private PrivilegeDao privilegeDao;
	
	@RequestMapping("privilege_list")
	public String privilege_list(Map<String, Object> model) {
		String act = VarUtil.RequestStr(request, "act");
		int list_pid = VarUtil.RequestInt(request, "list_pid");
		
		String pl = VarUtil.RequestStr(request, "pri_level").trim();
		int pri_level= pl.length() > 0 ? VarUtil.intval(pl) : 1;

		
		if(act.equals("save")){
			
			//添加
			String privilege_name_new = VarUtil.RequestStr(request, "privilege_name_new");
			int privilege_order_new = VarUtil.RequestInt(request, "privilege_order_new");
			int privilege_status_new = VarUtil.RequestInt(request, "privilege_status_new");
			int privilege_type_new = VarUtil.RequestInt(request, "privilege_type_new");
			
			List<Object[]> batchArgs;
			Object[] batchObject;
			if(privilege_name_new.length()>0){
				batchArgs = new ArrayList<Object[]>();
				
				String[] name_arr = privilege_name_new.split("\n");
				
				for(int i = 0; i < name_arr.length; i++){
					batchObject = new Object[6];
					batchObject[0] = list_pid;
					batchObject[1] = privilege_type_new;

					
					String[] name_arr2 = name_arr[i].split(",");
					name_arr2[0] = name_arr2[0].trim();
					batchObject[2] = name_arr2[0].trim();
					if(name_arr2.length>1){
						name_arr2[1] = name_arr2[1].trim();
						batchObject[3] = name_arr2[1];
					}else{
						batchObject[3] = "";
					}
					
					
					batchObject[4] = privilege_status_new;
					batchObject[5] = privilege_order_new+i;
					//batchObject[6] = pri_level;
					batchArgs.add(batchObject);
					
				}
				if(batchArgs.size()>0){
					privilegeDao.batchInsertPrivilege(batchArgs);
					
					logOperateDao.add(request, "添加权限", SysLogConstants.OP_TYPE_INSERT, SysLogConstants.OP_STATUS_SUCCESS, "");
				}
			}
			
			//删除
			String[] privilege_del_id = request.getParameterValues("privilege_del_id");
			if(privilege_del_id==null){
				privilege_del_id = new String[0];
			}
			if(privilege_del_id.length>0){
				batchArgs = new ArrayList<Object[]>();
				//筛选所有当前ID和子ID
				List<Integer> delIds = getPrivSubs(privilege_del_id);
				for(int i = 0; i < delIds.size(); i++){
					batchArgs.add(new Object[]{delIds.get(i)});
				}
				if(batchArgs.size()>0){
					privilegeDao.batchDelPrivilege(batchArgs);
					
					logOperateDao.add(request, "删除权限", SysLogConstants.OP_TYPE_DEL, SysLogConstants.OP_STATUS_SUCCESS, "");
				}
			}
			
			
			
			
			//更新
			String[] privilege_id = request.getParameterValues("privilege_id");
			String[] privilege_pid = request.getParameterValues("privilege_pid");
			String[] privilege_name = request.getParameterValues("privilege_name");
			String[] privilege_desc = request.getParameterValues("privilege_desc");
			String[] privilege_url = request.getParameterValues("privilege_url");
			String[] privilege_alias = request.getParameterValues("privilege_alias");
			String[] privilege_order = request.getParameterValues("privilege_order");
			if(privilege_id!=null&&privilege_id.length>0){
				batchArgs = new ArrayList<Object[]>();
				for(int i = 0; i < privilege_id.length; i++){
					//排除删除的ID
					if(ArrayUtil.inArray(privilege_id[i], privilege_del_id)){
						continue;
					}
					batchObject = new Object[9];
					batchObject[0] = VarUtil.intval(privilege_pid[i]);
					batchObject[1] = VarUtil.RequestInt(request, "privilege_type_"+privilege_id[i]);
					batchObject[2] = privilege_alias[i].trim();
					batchObject[3] = privilege_name[i].trim();
					batchObject[4] = privilege_url[i].trim();
					batchObject[5] = privilege_desc[i].trim();
					batchObject[6] = VarUtil.RequestInt(request, "privilege_status_"+privilege_id[i]);
					batchObject[7] = VarUtil.intval(privilege_order[i]);
					
					batchObject[8] = VarUtil.intval(privilege_id[i]);
					
					batchArgs.add(batchObject);
				}
				if(batchArgs.size()>0){
					privilegeDao.batchUpdatePrivilege(batchArgs);
					
					logOperateDao.add(request, "修改权限", SysLogConstants.OP_TYPE_UPDATE, SysLogConstants.OP_STATUS_SUCCESS, "");
				}
			}
			
			return "redirect:/privilege/privilege_list.do?list_pid="+list_pid;
		}
		Object[] privNav = getPrivNav(list_pid);
		if(list_pid>0&&(Integer)privNav[0]==0){//判断pid是否有效
			return "redirect:/privilege/privilege_list.do?list_pid=0";
		}
		
		
		JSONArray privList = privilegeDao.getPrivilegeList(list_pid);
		logOperateDao.add(request, "查询权限", SysLogConstants.OP_TYPE_QUERY, SysLogConstants.OP_STATUS_SUCCESS, "");
		
		model.put("priv_nav", (String)privNav[1]);
		model.put("priv_level", (Integer)privNav[0]);
		
		model.put("list_pid", list_pid);
		model.put("privList", privList);
		
		model.put("prilevels", BossConstants.getSystemLevels());
		model.put("pri_level", pri_level);
		
		
		return "/privilege/privilege_list2";
	}
	
	@RequestMapping("privilege_all_list")
	public String privilege_all_list(Map<String, Object> model) {
		String pri_level = VarUtil.RequestStr(request, "pri_level").trim();

		JSONArray privList = privilegeDao.getAllPrivilegeList();
		JSONObject privChilds = LoginCommon.privChildList(privList);
		
		StringBuffer sb = new StringBuffer();
		privilege_all_list_child(privChilds, sb, "0", 0);
		return result(model, sb.toString());
	}
	/**
	 * 将权限分级显示
	 * @param privChilds
	 * @param sb
	 * @param pid
	 * @param level
	 */
	public void privilege_all_list_child(JSONObject privChilds, StringBuffer sb,String pid, int level) {
		JSONArray rows = privChilds.getJSONArray(pid);
		if(rows!=null&&rows.size()>0 ){
			JSONObject row;
			for(int i = 0; i < rows.size(); i++){
				row = rows.getJSONObject(i);
				sb.append(StringUtil.strRepeat("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", level));
				if(level==0){
					sb.append("<strong>");
				}else if(level==1&&row.getString("privilege_type").equals("0")){
					sb.append("<span style='color:#999'>");
				}else if(level==1&&row.getString("privilege_type").equals("1")){
					sb.append("<span style='color:#0000ff'>");
				}else if(level==2&&row.getString("privilege_type").equals("0")){
					sb.append("<span style='color:#AAA'>");
				}else if(level==2&&row.getString("privilege_type").equals("1")){
					sb.append("<span style='color:#0000AA'>");
				}else if(row.getString("privilege_type").equals("1")){
					sb.append("<span style='color:#FF0000'>");
				}else{
					sb.append("<span style='color:#CCC'>");
				}
				sb.append(row.getString("privilege_name"));
				
				sb.append("[id="+row.getString("privilege_id")+"]");
				sb.append("[menu="+row.getString("privilege_type")+"]");
				sb.append(VarUtil.strval(row.getString("privilege_url")));
				
				if(level==0){
					sb.append("</strong>");
				}else{
					sb.append("</span>");
				}
				sb.append("<br/>");
				sb.append("\r\n");
				privilege_all_list_child(privChilds,sb,row.getString("privilege_id"), level+1);
			}
		}
		
		
	}
	
	
	/**
	 * 获取导航
	 * @param id
	 * @return
	 */
	public Object[] getPrivNav(int id){
		if(id == 0)return new Object[]{0,""};
		JSONArray privList = getPrivParents(id);
		StringBuffer nav = new StringBuffer();
		JSONObject priv;
		nav.append("<a href=\"privilege_list.do?list_pid=0\">根目录</a>");
		for(int i = privList.size() - 1; i >= 0; i--){
			priv = privList.getJSONObject(i);
			nav.append(" &gt; ");
			nav.append("<a href=\"privilege_list.do?list_pid=");
			nav.append(priv.getIntValue("privilege_id"));
			nav.append("\">");
			nav.append(priv.getString("privilege_name"));
			nav.append("</a>");
		}
		return new Object[]{privList.size(),nav.toString()};
	}
	/**
	 * 获取所有上级列表
	 * @param id
	 * @return
	 */
	public JSONArray getPrivParents(int pid){
		JSONArray privList = new JSONArray();
		JSONObject priv;
		do{
			priv = privilegeDao.getPrivilege(pid);
			if(priv!=null){
				pid = priv.getIntValue("privilege_pid");
				privList.add(priv);
			}else{
				pid = 0;
			}
		}while(pid>0);
		return privList;
	}
	
	/**
	 * 获取所有下级列表
	 * @param id
	 * @return
	 */
	public List<Integer> getPrivSubs(String[] ids){
		List<Integer> subIds = new ArrayList<Integer>();
		for(String id : ids){
			subIds.add(Integer.valueOf(id));
			subIds.addAll(getPrivSubs(Integer.valueOf(id)));
		}
		return subIds;
	}
	/**
	 * 获取所有下级列表
	 * @param id
	 * @return
	 */
	public List<Integer> getPrivSubs(int id){
		List<Integer> subIds = new ArrayList<Integer>();
		int[] subids = privilegeDao.getSubPrivilegeId(id);
		for(int subid : subids){
			subIds.add(subid);
			subIds.addAll(getPrivSubs(subid));
		}
		return subIds;
	}
}
