package com.huacai.web.common;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentSkipListMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huacai.util.ReadResource;
import com.huacai.web.bean.privilege.AdminInfo;
import com.huacai.web.controller.task.TaskController;
import com.huacai.web.dao.cp.CpDao;

public class LoginCommon {
	protected final static Logger logger = LogManager.getLogger(LoginCommon.class);

	public static String SESSION_KEY_LOGIN_VCODE = "__LOGIN_VCODE__";
	public static String SESSION_KEY_LOGIN_INFO = "__LOGIN_INFO__";

	/**
	 * 不需要登录的url（完整匹配）
	 */
	private static ConcurrentSkipListMap<String, Boolean> noCheckLoginUrlList;

	/**
	 * 不需要登录的url(开始字符匹配)
	 */
	private static Vector<String> noCheckLoginUrlList2;

	/**
	 * 不需要权限的url
	 */
	private static ConcurrentSkipListMap<String, Boolean> noCheckPrivUrlList;

	static {
		// 不需要登录的url
		noCheckLoginUrlList = new ConcurrentSkipListMap<String, Boolean>();
		noCheckLoginUrlList.put("/login.do", true);
		noCheckLoginUrlList.put("/vcode.do", true);
		noCheckLoginUrlList.put("/task/log.do", true);

		// 不需要登录的url
		noCheckLoginUrlList2 = new Vector<String>();
		// noLoginUrlList2.add("/help/");

		// 不需要权限的url
		noCheckPrivUrlList = new ConcurrentSkipListMap<String, Boolean>();
		noCheckPrivUrlList.put("/", true);
		noCheckPrivUrlList.put("/index.do", true);
		noCheckPrivUrlList.put("/sitemap.do", true);
		noCheckPrivUrlList.put("/no_access.do", true);
		noCheckPrivUrlList.put("/task/log.do", true);
		noCheckPrivUrlList.put("/task/viewlog.do", true);

		noCheckPrivUrlList.put("/login.do", true);
		noCheckPrivUrlList.put("/login_update.do", true);
		noCheckPrivUrlList.put("/login_status.do", true);
		noCheckPrivUrlList.put("/logout.do", true);

		noCheckPrivUrlList.put("/cp/my_info.do", true);
		noCheckLoginUrlList2.add("/demo");
		noCheckLoginUrlList2.add("/test");
		noCheckLoginUrlList2.add("/indexsysn/notice_list.do");
		noCheckLoginUrlList2.add("/indexsysn/notice_detail.do");
		noCheckLoginUrlList2.add("/sysnotice/sysnotice_list.do");
		noCheckLoginUrlList2.add("/indexinfo/getSystemEventList.do");

	}

	/**
	 * 登录用户列表
	 */
	private static ConcurrentSkipListMap<String, AdminInfo> loginUsers = new ConcurrentSkipListMap<String, AdminInfo>();

	/**
	 * 检查用户是否登录
	 * 
	 * @param request
	 * @param account
	 * @return
	 */
	public static boolean checkUserLoginStatus(HttpServletRequest request, String account) {
		AdminInfo adminInfo = loginUsers.get(account);
		long now = System.currentTimeMillis();
		long limitTime = 1 * 60 * 1000;
		if (adminInfo != null && adminInfo.getAdminId() > 0 && now - adminInfo.getLastTime() < limitTime) {
			return true;
		}
		return false;
	}

	/**
	 * 检查用户是否登录
	 * 
	 * @param account
	 * @return
	 */
	public static boolean checkUserLoginStatus(String account) {
		AdminInfo adminInfo = loginUsers.get(account);
		long now = System.currentTimeMillis();
		long limitTime = 1 * 60 * 1000;
		if (adminInfo != null && adminInfo.getAdminId() > 0 && now - adminInfo.getLastTime() < limitTime) {
			return true;
		}
		return false;
	}

	/**
	 * 删除用户登录
	 * 
	 * @param request
	 * @param account
	 * @return
	 */
	public static void delUserLoginStatus(HttpServletRequest request, String account) {
		loginUsers.remove(account);
	}

	/**
	 * 添加登录信息
	 * 
	 * @param request
	 * @param adminInfo
	 */
	public static void addUserLoginStatus(HttpServletRequest request, AdminInfo adminInfo) {
		adminInfo.setLastTime(System.currentTimeMillis());
		adminInfo.setSessionId(request.getSession().getId());
		loginUsers.put(adminInfo.getAdminLoginname(), adminInfo);
	}

	/**
	 * 更新登录信息
	 * 
	 * @param request
	 */
	public static AdminInfo updateUserLoginStatus(HttpServletRequest request) {
		AdminInfo adminInfo = getAdminInfo(request);
		if (adminInfo == null || adminInfo.getAdminId() < 1) {
			return null;
		}
		AdminInfo adminInfo2 = loginUsers.get(adminInfo.getAdminLoginname());
		if (adminInfo2 == null) {
			return null;
		}
		adminInfo2.setLastTime(System.currentTimeMillis());
		loginUsers.put(adminInfo.getAdminLoginname(), adminInfo2);
		return adminInfo2;
	}

	/**
	 * 判断是否登录
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isLogin(HttpServletRequest request) {
		AdminInfo adminInfo = getAdminInfo(request);
		if (adminInfo != null && adminInfo.getAdminId() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 注册登录session
	 * 
	 * @param request
	 * @param adminRec
	 */
	public static AdminInfo regAdminInfoSession(HttpServletRequest request, CpDao cpDao, JSONObject adminRec) {
		AdminInfo adminInfo = new AdminInfo();
		adminInfo.setAdminId(adminRec.getLongValue("admin_id"));
		adminInfo.setAdminLoginname(adminRec.getString("admin_loginname"));
		adminInfo.setAdminRealname(adminRec.getString("admin_realname"));
		adminInfo.setAdminType(adminRec.getIntValue("admin_type"));
		adminInfo.setAdminPwdUpdates(adminRec.getIntValue("admin_pwd_updates"));
		adminInfo.setAdminLoginTime(adminRec.getDate("admin_login_time"));

		int isRoot = adminRec.getIntValue("admin_root");
		adminInfo.setAdminRoot(isRoot == 1);
		if (adminInfo.isAdminRoot()) {
			adminInfo.setPrivilegeList(cpDao.getAllPrivList());
		} else {
			adminInfo.setPrivilegeList(cpDao.getPrivList(adminInfo.getAdminId()));
		}
		adminInfo.setPrivMap(getPrivMap(adminInfo.getPrivilegeList()));
		adminInfo.setButtonPrivSet(getButtonPrivMap(adminInfo.getPrivilegeList()));
		request.getSession(true).setAttribute(SESSION_KEY_LOGIN_INFO, adminInfo);
		return adminInfo;
	}

	public static Set<String> getButtonPrivMap(JSONArray privList) {
		Map<Integer, JSONObject> map = new HashMap<Integer, JSONObject>();
		int taskRootId = 0;
		int size = privList.size();
		for (int i = 0; i < size; i++) {
			JSONObject row = privList.getJSONObject(i);
			if (row.getString("privilege_name").equals(ReadResource.get("task_privilege_root_name"))) {
				taskRootId = row.getIntValue("privilege_id");
			}
			map.put(row.getIntValue("privilege_id"), row);
		}
		Set<JSONObject> buttonPrivSet = new HashSet<>();
		for (Entry<Integer, JSONObject> entry : map.entrySet()) {
			int id = entry.getKey();
			JSONObject item = entry.getValue();
			if (isTaskPriv(map, id, taskRootId, item)) {
				buttonPrivSet.add(item);
			}
		}
		logger.info("当前用户任务管理权限原始值: "+ buttonPrivSet);
		for (JSONObject curr : buttonPrivSet) {
			int pid = curr.getIntValue("privilege_pid");
			JSONObject pre = map.get(pid);
			StringBuilder sb = new StringBuilder(curr.getString("privilege_name"));
			while (pre != null && pid != taskRootId) {
				sb.insert(0, ".");
				sb.insert(0, pre.getString("privilege_name"));
				pid = pre.getIntValue("privilege_pid");
				pre = map.get(pid);
			}
			curr.put("buttonPrivName", sb.toString());
		}
		Set<String> result = new HashSet<>();
		for (JSONObject buttonPriv : buttonPrivSet) {
			if (buttonPriv.getString("buttonPrivName") != null)
				result.add(buttonPriv.getString("buttonPrivName"));
		}
		logger.info("当前用户任务管理权限包括: "+ JSON.toJSONString(result));
		return result;
	}

	private static boolean isTaskPriv(Map<Integer, JSONObject> bootmap, int id, int taskRootId, JSONObject item) {
		if (id == taskRootId) {
			item.put("isOk", true);
			return false;
		}
		int pid = item.getIntValue("privilege_pid");
		if (pid == 0) {
			item.put("isOk", false);
			return false;
		}
		if (pid == taskRootId) {
			item.put("isOk", true);
			return true;
		}
		JSONObject pitem = bootmap.get(pid);
		if (pitem.containsKey("isOk")) {
			boolean flag = pitem.getBooleanValue("isOk");
			item.put("isOk", flag);
			return flag;
		}

		boolean flag = isTaskPriv(bootmap, pid, taskRootId, pitem);
		item.put("isOk", flag);
		return flag;

	}

	/**
	 * 退出登录
	 * 
	 * @param request
	 */
	public static void clearLoginSession(HttpServletRequest request) {
		AdminInfo adminInfo = getAdminInfo(request);
		if (adminInfo != null) {
			LoginCommon.delUserLoginStatus(request, adminInfo.getAdminLoginname());
		}
		request.getSession(true).removeAttribute(SESSION_KEY_LOGIN_INFO);
	}

	/**
	 * 获取session用户信息
	 * 
	 * @param request
	 * @return
	 */
	public static AdminInfo getAdminInfo(HttpServletRequest request) {
		return (AdminInfo) request.getSession(true).getAttribute(SESSION_KEY_LOGIN_INFO);
	}

	/**
	 * 获取session用户ID
	 * 
	 * @param request
	 * @return
	 */
	public static long getAdminId(HttpServletRequest request) {
		AdminInfo adminInfo = getAdminInfo(request);
		return adminInfo != null ? adminInfo.getAdminId() : 0;
	}

	/**
	 * 获取用户类型
	 * 
	 * @param request
	 * @return
	 */
	public static int getAdminType(HttpServletRequest request) {
		AdminInfo adminInfo = getAdminInfo(request);
		return adminInfo != null ? adminInfo.getAdminType() : -1;
	}

	// 判断当前url是否需要登录
	public static boolean checkNoLoginUrl(String url) {
		for (String checkUrl : noCheckLoginUrlList2) {
			if (url.startsWith(checkUrl))
				return true;
		}
		return noCheckLoginUrlList.containsKey(url);
	}

	/**
	 * 检查用户对当前url的权限
	 * 
	 * @param request
	 * @return
	 */
	public static boolean checkUrlPriv(HttpServletRequest request) {
		AdminInfo adminInfo = getAdminInfo(request);
		if (adminInfo == null || adminInfo.getAdminId() < 1) {
			return false;
		}
		if (adminInfo.isAdminRoot()) {// 超级用户不验证
			return true;
		}
		String currUrl = request.getRequestURI().toLowerCase();
		 
		request.setAttribute(TaskController.Priv_Set, adminInfo.getButtonPrivSet());

		if (noCheckPrivUrlList.containsKey(currUrl)) {
			return true;
		}
		if (adminInfo.getPrivMap().containsKey(currUrl)) {
			List<List<NameValuePair>> privParList = adminInfo.getPrivMap().get(currUrl);
			if(privParList ==null || privParList.isEmpty()){
				return true;
			}
			boolean flag = false;
			for(List<NameValuePair> privPar: privParList ){
				if(flag){
					return true;
				}
				if (privPar == null || privPar.size() == 0) {// 未设置get变量参数
					flag =  true;
				}
				// 检查get变量
				List<NameValuePair> urlPar = URLEncodedUtils.parse(request.getQueryString().toLowerCase(), Charset.forName("UTF-8"));
				if (urlPar == null || urlPar.size() == 0 || urlPar.size() < privPar.size()) {// 当前访问url未加参数，认证失败
					flag =  false;
				}
				int right = 0;
				for (NameValuePair par1 : urlPar) {
					for (NameValuePair par2 : privPar) {
						if (par2.getName().equals(par1.getName()) && par2.getValue().equals(par1.getValue())) {
							right++;
						}
					}
				}
				if (right == privPar.size()) {// get变量权限相符
					flag = true;
				}
			}
			return flag;
		}
		return false;
	}

	/**
	 * 获取权限map
	 * 
	 * @param privList
	 * @return
	 */
	public static Map<String, List<List<NameValuePair>>> getPrivMap(JSONArray privList) {
		Map<String, List<List<NameValuePair>>> map = new HashMap<String, List<List<NameValuePair>>>();
		JSONObject row;
		String url;
		String[] aUrl;
		List<NameValuePair> aPar;
		int size = privList.size();
		for (int i = 0; i < size; i++) {
			row = privList.getJSONObject(i);
			url = row.getString("privilege_url");
			if (url != null && url.length() > 1) {
				url = url.toLowerCase();
				aUrl = url.split("\\?");
				if (aUrl.length > 1) {
					List<List<NameValuePair>> value = map.get(aUrl[0]);
					if(value == null){
						value = new ArrayList<>();
						map.put(aUrl[0], value);
					}
					aPar = URLEncodedUtils.parse(aUrl[1], Charset.forName("UTF-8"));
					value.add(aPar);
					//map.put(aUrl[0], aPar);
					// map.put(url, null);
				} else {
					map.put(aUrl[0], null);
				}

			}
		}
		return map;
	}

	/**
	 * 过滤本系统不需要的主菜单
	 * 
	 * @param privList
	 * @return
	 */
	public static JSONArray filterPrivListMenu(JSONArray privList) {
		String appMenu = ReadResource.get("BOSS_APP_MENU");
		if (appMenu == null || appMenu.length() == 0) {
			return privList;
		}

		JSONObject row;
		JSONObject rootRow = null;
		int rootRowIndex = 0;
		JSONArray rows = new JSONArray();
		for (int i = 0; i < privList.size(); i++) {
			row = privList.getJSONObject(i);
			if (row.getString("privilege_pid").equals("0") && appMenu.equals(row.getString("privilege_alias"))) {
				rootRow = row;
				rootRowIndex = i;

			}
		}
		if (privList.size() > 0)
			privList.remove(rootRowIndex);

		for (int i = 0; i < privList.size(); i++) {
			row = privList.getJSONObject(i);
			if (row.getString("privilege_pid").equals("0")) {
				continue;
			}
			if (row.getString("privilege_pid").equals(rootRow.getString("privilege_id"))) {
				row.put("privilege_pid", 0);
			}
			rows.add(row);

		}
		return rows;
	}

	/**
	 * 重新筛选权限，过滤掉没有级别关联的
	 * 
	 * @param privList
	 * @return
	 */
	public static void filterPrivList(JSONObject privChilds, String pid, JSONArray privList) {
		JSONArray rows = privChilds.getJSONArray(pid);
		if (rows != null && rows.size() > 0) {
			JSONObject row;
			for (int i = 0; i < rows.size(); i++) {
				row = rows.getJSONObject(i);
				privList.add(row);
				filterPrivList(privChilds, row.getString("privilege_id"), privList);
			}
		}
	}

	/**
	 * 返回层级权限
	 * 
	 * @param privList
	 * @return
	 */
	public static JSONObject privChildList(JSONArray privList) {
		JSONObject privChilds = new JSONObject();
		JSONArray rows;
		JSONObject row;
		for (int i = 0; i < privList.size(); i++) {
			row = privList.getJSONObject(i);
			rows = privChilds.getJSONArray(row.getString("privilege_pid"));
			if (rows == null) {
				rows = new JSONArray();
			}
			rows.add(row);
			privChilds.put(row.getString("privilege_pid"), rows);
		}
		return privChilds;
	}

	/**
	 * 获取session用户区域-省
	 * 
	 * @param request
	 * @return
	 */
	public static String getAdminProvince(HttpServletRequest request) {
		AdminInfo adminInfo = getAdminInfo(request);
		return adminInfo != null ? adminInfo.getProvince() : "0";
	}

	/**
	 * 获取session用户区域-市
	 * 
	 * @param request
	 * @return
	 */
	public static String getAdminCity(HttpServletRequest request) {
		AdminInfo adminInfo = getAdminInfo(request);
		return adminInfo != null ? adminInfo.getCity() : "0";
	}

	/**
	 * 获取session用户 区域-县
	 * 
	 * @param request
	 * @return
	 */
	public static String getAdminArea(HttpServletRequest request) {
		AdminInfo adminInfo = getAdminInfo(request);
		return adminInfo != null ? adminInfo.getCounty() : "0";
	}

}
