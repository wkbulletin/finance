package com.huacai.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import libcore.util.FileUtil;
import libcore.util.VarUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 读取多商户资源文件属性
 * 
 */
public class ReadResourceAgency {
	protected final Log logger = LogFactory.getLog(getClass());

	// 配置信息
	protected Properties ht = new Properties();

	public static String basePath;

	protected String filename;
	protected File file;
	protected long fileLastModifyTime = 0;

	public String configPath;

	public ReadResourceAgency(String agencyId) {
		String activeCfg = basePath + agencyId + "/active.cfg";
		String activePath = "";
		if (FileUtil.isExists(activeCfg)) {
			activePath = VarUtil.strval(fileRead(activeCfg));
		}
		if (activePath.length() > 0) {
			configPath = basePath + agencyId + "/" + activePath + "/";
			filename = configPath + agencyId + "_config.properties";
		} else {
			configPath = basePath + agencyId + "/";
			filename = configPath + agencyId + "_config.properties";
		}

		setConfig();
	}
	public static String fileRead(String filename) {
		int ch;
		StringBuffer strb = new StringBuffer();
		try {
			BufferedReader brIn = new BufferedReader(new FileReader(filename));
			while ((ch = brIn.read()) > -1) {
				strb.append((char) ch);
			}
			brIn.close();
		} catch (Exception e) {
			
		}
		return strb.toString();
	}
	/**
	 * 读取配置文件，存放到Hashtable中
	 * 
	 */
	public void setConfig() {
		long t = System.currentTimeMillis();
		logger.info("开始加载配置:" + filename);
		FileInputStream fin = null;
		InputStreamReader reader = null;
		ht.clear();
		try {
			if (file == null) {
				file = new File(filename);
			}
			fileLastModifyTime = file.lastModified();
			fin = new FileInputStream(file);
			reader = new InputStreamReader(fin, "UTF-8");
			if (fin != null) {
				ht.load(reader);
			}
		} catch (FileNotFoundException ex) {
			logger.error("配置文件没有找到：" + ex.getMessage());
		} catch (IOException ex) {
			logger.error("配置文件读取错误：" + ex.getMessage());
		} finally {
			if (fin != null) {
				try {
					fin.close();
				} catch (IOException e) {
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
		logger.info("加载配置结束:" + (System.currentTimeMillis() - t));
	}

	/**
	 * 读取配置信息
	 * 
	 * @param name
	 *                信息名称
	 * @return 配置信息
	 */
	public String get(String name) {
		if (file != null && file.lastModified() != fileLastModifyTime) {
			setConfig();
		}
		String value = ht.getProperty(name);
		if (value != null) {
			value = value.trim();
		}
		return value;
	}

	/**
	 * 读取配置信息
	 * 
	 * @param name
	 *                信息名称
	 * @param defaultValue
	 *                为空时的默认值
	 * @return 配置信息
	 */
	public String get(String name, String defaultValue) {
		String value = get(name);
		if (value == null || value.length() == 0) {
			value = defaultValue;
		}
		return value;
	}
	
	public int getInt(String name) {
		return VarUtil.intval(get(name));
	}
	public int getInt(String name, int defaultValue) {
		String value = get(name);
		if (value == null || value.length() == 0) {
			return defaultValue;
		}
		return VarUtil.intval(value);
	}
	public long getLong(String name) {
		return VarUtil.longval(get(name));
	}
	public long getLong(String name, long defaultValue) {
		String value = get(name);
		if (value == null || value.length() == 0) {
			return defaultValue;
		}
		return VarUtil.longval(value);
	}
	
	public float getFloat(String name) {
		return VarUtil.floatval(get(name));
	}
	
	public float getFloat(String name, float defaultValue) {
		String value = get(name);
		if (value == null || value.length() == 0) {
			return defaultValue;
		}
		return VarUtil.floatval(value);
	}
	
	public double getDouble(String name) {
		return VarUtil.doubleval(get(name));
	}
	public double getDouble(String name, double defaultValue) {
		String value = get(name);
		if (value == null || value.length() == 0) {
			return defaultValue;
		}
		return VarUtil.doubleval(value);
	}
	
	public boolean getBoolean(String name) {
		String value = get(name);
		return value != null && (value.toLowerCase().equals("true") || value.equals("1"));
	}
	public boolean getBoolean(String name, boolean defaultValue) {
		String value = get(name);
		if (value == null || value.length() == 0) {
			return defaultValue;
		}
		return value.toLowerCase().equals("true") || value.equals("1");
	}


	public static void main(String[] agrs) {
		ReadResourceAgency r = new ReadResourceAgency("40340");

		System.out.println(r.get("UnAndroidLoginUrl"));
		System.out.println(r.get("UnAndroidLoginUrl22"));

	}
}