package com.huacai.web.bean.privilege;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.NameValuePair;

import com.alibaba.fastjson.JSONArray;

public class AdminInfo {
	private long adminId;
	private boolean adminRoot;
	private String adminLoginname;
	private String adminRealname;
	private int adminType;

	private String adminRole;

	private JSONArray privilegeList;
	private Map<String, List<List<NameValuePair>>> privMap;
	private Set<String> buttonPrivSet;
	private Date adminLoginTime;

	private String sessionId;
	private long lastTime;

	private int adminPwdUpdates;

	private String province;

	private String city;

	private String county;

	private String level;

	private String areaLevel;

	public Set<String> getButtonPrivSet() {
		return buttonPrivSet;
	}

	public void setButtonPrivSet(Set<String> buttonPrivSet) {
		this.buttonPrivSet = buttonPrivSet;
	}

	public String getAreaLevel() {
		return areaLevel;
	}

	public void setAreaLevel(String areaLevel) {
		this.areaLevel = areaLevel;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public long getAdminId() {
		return adminId;
	}

	public void setAdminId(long adminId) {
		this.adminId = adminId;
	}

	public String getAdminLoginname() {
		return adminLoginname;
	}

	public void setAdminLoginname(String adminLoginname) {
		this.adminLoginname = adminLoginname;
	}

	public String getAdminRealname() {
		return adminRealname;
	}

	public void setAdminRealname(String adminRealname) {
		this.adminRealname = adminRealname;
	}

	public JSONArray getPrivilegeList() {
		return privilegeList;
	}

	public void setPrivilegeList(JSONArray privilegeList) {
		this.privilegeList = privilegeList;
	}

	 

	public Map<String, List<List<NameValuePair>>> getPrivMap() {
		return privMap;
	}

	public void setPrivMap(Map<String, List<List<NameValuePair>>> privMap) {
		this.privMap = privMap;
	}

	public boolean isAdminRoot() {
		return adminRoot;
	}

	public void setAdminRoot(boolean adminRoot) {
		this.adminRoot = adminRoot;
	}

	public int getAdminType() {
		return adminType;
	}

	public void setAdminType(int adminType) {
		this.adminType = adminType;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public long getLastTime() {
		return lastTime;
	}

	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}

	public int getAdminPwdUpdates() {
		return adminPwdUpdates;
	}

	public void setAdminPwdUpdates(int adminPwdUpdates) {
		this.adminPwdUpdates = adminPwdUpdates;
	}

	public Date getAdminLoginTime() {
		return adminLoginTime;
	}

	public void setAdminLoginTime(Date adminLoginTime) {
		this.adminLoginTime = adminLoginTime;
	}

	public String getAdminRole() {
		return adminRole;
	}

	public void setAdminRole(String adminRole) {
		this.adminRole = adminRole;
	}
}
