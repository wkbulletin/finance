package com.huacai.web.sms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.huacai.util.ReadResource;


public class SMSService {
	protected final Log logger = LogFactory.getLog(getClass());
	static SMSClient client = null;

	
	/**
	 * 启动服务
	 * 
	 * @return 是否启动成功
	 */
	public static boolean start() {
		boolean flag = false;
		String status = ReadResource.get("SMS_STATUS");
		System.out.println("开始启动短信服务" + status);
		if (status != null && "1".equals(status)) {
			// 设置服务器地址、端口号和消息监听器
			String clientID = ReadResource.get("SMS_CLIENT_ID");
			String password = ReadResource.get("SMS_CLIENT_PASSWORD");
			String IP = ReadResource.get("SMS_CLIENT_IP");
			int port = Integer.parseInt(ReadResource
					.get("SMS_CLIENT_PORT"));

			client = new SMSClient(clientID, password, IP, port);
			client.start();

			// 最大尝试的连接次数
			int i = 5;
			while (!SMSClient.isLogin() && i > 0) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
				}
				i--;
			}
			flag = SMSClient.isLogin();
		}
		return flag;
	}

	/**
	 * 发送信息
	 * 
	 * @param packet
	 *            包信息
	 * @return 响应信息
	 */
	public static void doSender(String mobile, String content) {
//		if (client != null) {
//			if (content != null) {
//				client.sendSms(mobile, content);
//			}
//		}
	}

	/**
	 * 停止服务
	 */	
	public static void stop() {
		if (client != null) {
			client.setExitSign(true);
		}
	}

	
}
