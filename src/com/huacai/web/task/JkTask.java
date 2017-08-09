package com.huacai.web.task;

import libcore.util.MathUtil;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.huacai.util.ReadResource;
import com.huacai.util.ThreadRept;
import com.huacai.web.dao.merchant.MerchantDao;

@Component
public class JkTask {
	protected final Logger logger = LogManager.getLogger(JkTask.class);

	@Autowired
	private MerchantDao merchantDao;
	
	public static boolean isflag = true;
	
	public void start1() {
		if(isflag) {
			isflag = false;
		
			logger.info("begin");
			 String udp = ReadResource.get("BOSS_JK_UDP");
			
			 ThreadRept.setDown(true);
			 ThreadRept.setUdp(udp);
			 ThreadRept.setApp("merchantboss");
			 ThreadRept alarm=ThreadRept.make("merchantboss");
			 alarm.active(MathUtil.getRand(100, 999));
			 
			 logger.info("end");
			isflag = true;
		}
		

	}
}
