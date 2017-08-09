package com.huacai.web.sms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class SessionHandler extends IoHandlerAdapter {
	protected final Log logger = LogFactory.getLog(getClass());
	
	public SessionHandler() {
	}


	public void sessionOpened(IoSession session) throws Exception {
		String loginStr = "v_cmd=login&v_name=" + SMSClient.Name + "&v_pwd="
				+ SMSClient.Password;
		session.write(loginStr);
		logger.info("连接短信中心");
	}

	public void messageReceived(IoSession session, Object message) {
		System.out.println("message=" + message);
		try {
			SMSClient.pool.execute(new SMSHandler(session, String
					.valueOf(message)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void exceptionCaught(IoSession session, Throwable cause) {
		SMSClient.setLogin(false);
		session.close();
	}

	public void sessionClosed(IoSession session) throws Exception {
		SMSClient.setLogin(false);
		logger.info("短信中心断开");
		session.close();
	}
}
