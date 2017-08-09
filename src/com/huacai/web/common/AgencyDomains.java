package com.huacai.web.common;

import java.io.File;
import java.util.concurrent.ConcurrentSkipListMap;

import libcore.util.ClassPathUtil;
import libcore.util.FileUtil;
import libcore.util.VarUtil;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class AgencyDomains {
	protected static final Logger logger = LogManager.getLogger(AgencyDomains.class);
	private final static ConcurrentSkipListMap<String, AgencyDomainsData> domainMap = new ConcurrentSkipListMap<String, AgencyDomainsData>();
	private static String filePath = ClassPathUtil.webPath() + "WEB-INF/agency_domains/";

	private static String defaultAgencyId = "10000";
	private static String defaultDomain = "www.huacai.com";

	/**
	 * 取域名配置
	 * 
	 * @return
	 */
	public static JSONObject getAgencyInfo(String domain) {
		AgencyDomainsData domainData = domainMap.get(domain);
		if (domainData != null && !domainData.isModified() && domainData.data != null) {
			return domainData.data;
		}

		String outStr = null;
		if (domain.length() > 0) {
			String filename = filePath + domain + ".cfg";
			File file = new File(filename);
			if (file.exists()) {
				outStr = FileUtil.fileRead(file);
				if (domainData == null) {
					domainData = new AgencyDomainsData();
				}
				domainData.file = file;
				domainData.lastModified = file.lastModified();
			} else {
				logger.error("域名配置不存在:" + domain);
				filename = filePath + defaultDomain + ".cfg";
				outStr = FileUtil.fileRead(filename);
			}
		}
		if (outStr == null || outStr.length() == 0) {
			return null;
		}
		JSONObject data = JSON.parseObject(outStr);
		
		if (domainData != null && data != null) {
			domainData.data = data;
			domainMap.put(domain, domainData);
		}
		return data;
	}

	/**
	 * 获取域名对应ID
	 * 
	 * @param domain
	 * @return
	 */
	public static String getAgencyId(String domain) {
		JSONObject data = getAgencyInfo(domain);
		if (data != null) {
			return data.getString("AgencyId");
		}
		return defaultAgencyId;
	}

	public static int getAgencyIdInt(String domain) {
		return VarUtil.intval(getAgencyId(domain));
	}

	/**
	 * 生成配置
	 * 
	 * @param AgencyUrl
	 * @param AgencyId
	 * @param CompanyName
	 * @param AgencyType
	 * @return
	 */
	public static String createAgencyInfo(String AgencyUrl, String AgencyId, String CompanyName, String AgencyType) {
		JSONObject agencyInfo = new JSONObject();

		agencyInfo.put("AgencyId", AgencyId);
		agencyInfo.put("CompanyName", CompanyName);
		agencyInfo.put("AgencyUrl", AgencyUrl);
		agencyInfo.put("AgencyType", AgencyType);

		//String sign = EncodeUtil.MD5(AgencyUrl + AgencyId + AgencyType + domainKey);
		//agencyInfo.put("sign", sign);

		return agencyInfo.toJSONString();
	}
	public static void writeAgencyInfo(String AgencyUrl, String AgencyId, String CompanyName, String AgencyType) {
		String filename = filePath + AgencyUrl + ".cfg";
		FileUtil.fileWrite(filename, createAgencyInfo(AgencyUrl, AgencyId, CompanyName, AgencyType));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String AgencyUrl = "www.huacai.com";
		String AgencyId = "10000";
		String CompanyName = "华彩网";
		String AgencyType = "0";

		writeAgencyInfo(AgencyUrl, AgencyId, CompanyName, AgencyType);

	}

}

class AgencyDomainsData {
	public File file;
	public long lastModified;
	public JSONObject data;

	public boolean isModified() {
		return lastModified != file.lastModified();
	}
}
