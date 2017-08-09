/**
 * 类名      		PageUtil.java
 * 说明   			分页显示工具类
 * 创建日期 			2008-7-13
 * 作者  			davidwang
 * 版权  			huacai.cn 2008-2100 All Copyright(C) 
 */
package com.huacai.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.antlr.runtime.tree.Tree;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.huacai.web.constant.Constants;
import com.huacai.web.constant.LotteryInfo;

/**
 * @author Administrator
 * 
 * 
 *         功能说明：监控ios设备推送批次
 */

public class PushMonitor {
	protected final static Logger logger = LogManager
			.getLogger(PushMonitor.class);
	private static PushMonitor pushMonitor = null;

	private PushMonitor() {

	}

	public static PushMonitor getInstance() {
		if (pushMonitor == null) {
			pushMonitor = new PushMonitor();
		}
		return pushMonitor;
	}

	private static Set<String> iosPushMonitorSet = null;

	private static Map<String,String> iosPushMonitorMap = null;
	
	
	/**
	 * 
	 * @param iosPushInfo
	 */
	public void pushIosPushFlag(String iosPushInfo) {
		synchronized (this) {
			if (iosPushMonitorSet == null) {
				iosPushMonitorSet = new HashSet<String>();
			}
			iosPushMonitorSet.add(iosPushInfo);
		}
	}
	/**
	 * 
	 * @param iosPushInfo
	 */
	public void pushIosPushFlagMap(String key,String val) {
		synchronized (this) {
			if (iosPushMonitorMap == null) {
				iosPushMonitorMap = new HashMap<String,String>();
			}
			iosPushMonitorMap.put(key, val);
		}
	}
	/**
	 * 如果返回true 标识可以进行push推送，返回false 表示还有推送线程未执行完
	 * 
	 * @return
	 */
	public boolean isPushDo() {
		synchronized (this) {
			if (iosPushMonitorSet == null) {
				return true;
			} else if (iosPushMonitorSet.isEmpty()) {
				return true;
			} else {
				return false;
			}
		}
	}
	/**
	 * 如果返回true 标识可以进行push推送，返回false 表示还有推送线程未执行完
	 * 
	 * @return
	 */
	public boolean isPushDo(String keyPush) {
		logger.info("检查进程==keyPush=="+keyPush);
		synchronized (this) {
			if (iosPushMonitorMap == null) {
				return true;
			} else if (iosPushMonitorMap.get(keyPush)!=null) {
				logger.info("存在未发送完push进程==keyPush=="+keyPush);
				return false;
			} else {
				return true;
			}
		}
	}
/**
 * 移除成功返回true
 * @param o
 * @return
 */
	public boolean removePushFlag(String o) {
		boolean isRemove = false;
		synchronized (this) {
			if (iosPushMonitorSet != null) {
				isRemove = iosPushMonitorSet.remove(o);
			}
		}
		return isRemove;
	}
	/**
	 * 移除成功返回true
	 * @param o
	 * @return
	 */
		public String removePushFlagMap(String o) {
			String isRemove = null;
			synchronized (this) {
				if (iosPushMonitorMap != null) {
					isRemove = iosPushMonitorMap.remove(o);
				}
			}
			return isRemove;
		}

	// 当前页前后条数
	private static int pageNumLen = 3;

	private static int pageNumLen2 = 2;
	public static void  main(String []ss){
		PushMonitor pushMonitor=PushMonitor.getInstance();
		pushMonitor.pushIosPushFlagMap("a","b");
//		System.out.println("ss=="+pushMonitor.isPushDo());
		//pushMonitor.removePushFlagMap("a");
		System.out.println("ss1=="+pushMonitor.isPushDo("a"));
		
//		for (int i = 0; i < 10; i++) {
//			
//		   System.out.println(i);
//		}
		
		
	}
}
