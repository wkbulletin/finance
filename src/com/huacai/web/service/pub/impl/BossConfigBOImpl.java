package com.huacai.web.service.pub.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.huacai.web.dao.sys.impl.BossConfigDaoImpl;


@Service("agencyConfigBO")
public class BossConfigBOImpl {
	protected final Log logger = LogFactory.getLog(getClass());

	private BossConfigDaoImpl dao;
	
	@Autowired
	public void setAgencyConfigDao(BossConfigDaoImpl dao) {
		this.dao = dao;
	}

	
	/**
	 * 获取配置记录
	 * @param agencyId
	 * @param key
	 * @return
	 */
	public JSONObject getConfigRec(String key) {
		return this.dao.getConfigRec(key);
	}
	/**
	 * 获取配置记录
	 * @param agencyId
	 * @param key
	 * @return
	 */
	public String getConfigValue(String key) {
		return this.dao.getConfigValue(key);
	}
	
	/**
	 * 更新配置
	 * @param agencyId
	 * @param key
	 * @param value
	 * @param comment
	 * @return
	 */
	public int updateConfigRec( final String key, final String value, final String comment) {
		return this.dao.updateConfigRec(key, value, comment);
	}
}
