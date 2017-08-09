package com.huacai.web.filters;

import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoader;

import com.huacai.web.common.Nav;

public class InitConfig extends ContextLoader implements ServletContextListener {
	protected final Logger logger = LogManager.getLogger(getClass());

	private static final ConcurrentHashMap<String, Object> Cache_Map = new ConcurrentHashMap<>();

	public Object getObjFromCacheMap(String key) {
		return Cache_Map.get(key);
	}

	public ConcurrentHashMap<String, Object> setObjToCacheMap(String key, Object val) {
		Cache_Map.put(key, val);
		return Cache_Map;
	}

	private ServletContext context = null;
	public static String basePath;
	public static String tplPath;
	public static String tplWebPath;

	public void contextInitialized(ServletContextEvent event) {
		setContext(event.getServletContext());
		// 获取项目basePath
		URL s = InitConfig.class.getClassLoader().getResource("config.properties");
		String path = s.getPath();

		basePath = path.replace("WEB-INF/classes/config.properties", "");
		tplPath = basePath + "tpl/";
		tplWebPath = context.getContextPath() + "/tpl/";

		// 设置seo 类的路径
		Nav.tplPath = tplPath;

		logger.info("项目根目录:" + basePath);

		
	}

	public void contextDestroyed(ServletContextEvent event) {

	}

	public void setContext(ServletContext context) {
		this.context = context;
	}

	public ServletContext getContext() {
		return context;
	}
}