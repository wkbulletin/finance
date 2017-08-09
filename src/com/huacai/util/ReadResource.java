package com.huacai.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import libcore.util.VarUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 读取资源文件属性
 * 
 */
public class ReadResource {
	protected final static Log logger = LogFactory.getLog(ReadResource.class);

	// 配置信息
	static Properties ht = new Properties();

	static File f;
	static long fileLastModifyTime = 0;

	static {
		setConfig();
	}

	/**
	 * 读取配置文件，存放到Hashtable中
	 * 
	 */
	public static void setConfig() {
		long t = System.currentTimeMillis();
		logger.info("重新加载配置信息.");
		FileInputStream fin = null;
		InputStreamReader reader = null;
		ht.clear();
		try {
			if (f == null) {
				f = new File(ReadResource.class.getClassLoader().getResource("config.properties")
						.getPath());
			}
			fileLastModifyTime = f.lastModified();
			fin = new FileInputStream(f);
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
		logger.info("重新加载配置信息结束:" + (System.currentTimeMillis() - t));
	}

	/**
	 * 读取配置信息
	 * 
	 * @param name
	 *                信息名称
	 * @return 配置信息
	 */
	public static String get(String name) {
		if (f != null && f.lastModified() != fileLastModifyTime) {
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
	public static String get(String name, String defaultValue) {
		String value = get(name);
		if (value == null || value.length() == 0) {
			value = defaultValue;
		}
		return value;
	}
	
	
	public static int getInt(String name) {
		return VarUtil.intval(get(name));
	}
	public static int getInt(String name, int defaultValue) {
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
}