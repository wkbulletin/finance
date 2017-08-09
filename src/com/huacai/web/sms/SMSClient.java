package com.huacai.web.sms;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.huacai.util.StringUtil;

public class SMSClient extends Thread {

	protected final Log logger = LogFactory.getLog(getClass());

	private String HOSTNAME = "localhost";

	private int PORT = 8080;

	public static String Name = "";

	public static String Password = "";

	private static final int CONNECT_TIMEOUT = 30; // seconds

	boolean exitSign = false;

	int maxWaitTime = 10;

	private Object wtLock = new Object();

	IoSession session = null;

	SessionHandler handler = null;

	private static boolean login;

	
	public static final int MAX_CLIENT = 5; // 业务线程数

	
	public static final ThreadPoolExecutor pool = new ThreadPoolExecutor(MAX_CLIENT, MAX_CLIENT * 2, 60,
			TimeUnit.SECONDS, new LinkedBlockingQueue(), new ThreadPoolExecutor.DiscardPolicy());

	private static Date active = new Date();

	NioSocketConnector connector = new NioSocketConnector();

	public SMSClient(String name, String password, String ip, int port) {
		super("Huacai SMSClient");
		this.HOSTNAME = ip;
		this.PORT = port;
		SMSClient.Name = name;
		SMSClient.Password = password;

		connector.setConnectTimeoutMillis(CONNECT_TIMEOUT);

		TextLineCodecFactory textLineCodecFactory = new TextLineCodecFactory();
		textLineCodecFactory.setDecoderMaxLineLength(8192);

		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(textLineCodecFactory));

		connector.getSessionConfig().setTcpNoDelay(true);
		connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 60);
		connector.getSessionConfig().setReadBufferSize(2048 * 5000);// 发送缓冲区10M
		connector.getSessionConfig().setReceiveBufferSize(2048 * 5000);// 接收缓冲区10M

		handler = new SessionHandler();
		connector.setHandler(handler);

	}

	public void con() {
		login = false;
		if (session != null) {
			session.close(true);
		}

		try {
			ConnectFuture future = connector.connect(new InetSocketAddress(this.HOSTNAME, this.PORT));
			future.awaitUninterruptibly();
			if (future.isConnected()) {
				session = future.getSession();
				active = new Date();
			}
			Thread.sleep(1000);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		this.con();
		Date oldTime, curTime;
		oldTime = new Date();
		while (!exitSign) {
			try {
				curTime = new Date();
				if (curTime.getTime() - oldTime.getTime() < maxWaitTime * 1000) {
					synchronized (wtLock) {
						wtLock.wait(maxWaitTime * 1000 - (curTime.getTime() - oldTime.getTime()));
					}
				}
				oldTime = new Date();
				if (isLogin()) {
					if (oldTime.getTime() - active.getTime() > 600 * 1000) {
						
						this.session.close();
					}
				} else {
					this.con();
				}

			} catch (Exception ex) {
				logger.error("Send run Error��" + ex.getMessage());
				ex.printStackTrace();
			}
		}
	}

	
	public void sendSms(String mobile, String content) {
		if (mobile != null && content != null) {
			SMS sms = new SMS();
			sms.setMobile(mobile);
			sms.setMessage(content);
			this.sendSms(sms);
		}
	}

	
	public void sendSms(SMS sms) {
		String sendstr = this.getCommand(sms);
		if (sendstr != null && sendstr.length() > 0) {
			if (this.session != null) {
				session.write(sendstr);
			}
		}
	}

	
	private String getCommand(SMS sms) {

		StringBuffer command = new StringBuffer();
		if (!sms.getFrom().equals("batch")) {
			command.append("v_cmd=submit&");
			command.append("v_sp=" + sms.getFrom() + "&");
			command.append("v_item=" + sms.getItem() + "&");
			command.append("v_type=" + sms.getFee() + "&");
			command.append("v_mobile=" + sms.getMobile() + "&");
			command.append("v_message=" + StringUtil.enCode(sms.getMessage().getBytes()) + "&");
			command.append("v_msgid=" + sms.getId() + "&");
			command.append("v_linkid=" + sms.getLinkid() + "&");
			command.append("v_level=" + sms.getLevel() + "&");
		}
		return command.toString();
	}

	
	public static boolean isLogin() {
		return login;
	}

	
	public static void setLogin(boolean login) {
		SMSClient.login = login;
	}

	
	public static Date getActive() {
		return active;
	}

	
	public static void setActive(Date active) {
		SMSClient.active = active;
	}

	public boolean isExitSign() {
		return exitSign;
	}

	public void setExitSign(boolean exitSign) {
		this.exitSign = exitSign;
	}

	public static void main(String[] args) throws Throwable {
		String clientID = "0003";
		String password = "fb014a49437dbb3b3eab92c45691b963";
		String IP = "sms.huacai.com";
		int port = 18888;
		SMSClient client = new SMSClient(clientID, password, IP, port);
		client.start();
		System.out.println("11111111111");
	}

}
