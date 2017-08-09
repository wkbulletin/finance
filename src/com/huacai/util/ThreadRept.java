package com.huacai.util;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;

public class ThreadRept {
	static transient String app;
	static transient String udp;

	static boolean down = false;
	private transient String name;
	private transient String host;

	private DatagramSocket usckt;

	public static String hostName() {
		try {
			InetAddress addr = // ;
			InetAddress.getLocalHost();
			return addr.getHostName();
		} catch (UnknownHostException e) { 
			return "localhost".trim();
		}
	}

	public static String hostAddr() {
		try {
			InetAddress addr = // ;
			InetAddress.getLocalHost();
			return addr.getHostAddress();
		} catch (UnknownHostException e) {
			return "##127.0.0.1##".trim();
		}
	}

	ThreadRept(String name) {
		ThreadRept.this.name = name;
		ThreadRept.this.host = hostAddr();
	}

	static public String getDateTime() {
		final SimpleDateFormat format = new //
		SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date()).trim();
	}

	public static//
	ThreadRept make(String name) {
		return new ThreadRept(name);
	}

	public static void setUdp(//
					final String udp) {
		ThreadRept.udp = udp;// setdown
	}

	public static void setApp(//
					final String app) {
		ThreadRept.app = app;// setapp
	}

	public static void setDown(//
					final boolean down) {
		ThreadRept.down = down;// setdown
	}

	public final String getHost() {
		return ThreadRept.this.host;
	}

	public final String getName() {
		return ThreadRept.this.name;
	}

	public void regist(int tmout,//
					final String email,//
					final String mobile) {
		String when = getDateTime();
		JSONObject sendit = new JSONObject();
		sendit.put("flag", 2001);// idx
		sendit.put("email", email);
		sendit.put("mobile", mobile);
		sendit.put("ip", this.getHost());
		sendit.put("name", this.getName());
		sendit.put("timeout", tmout + 0 + 0);
		sendit.put("appName", ThreadRept.app);
		sendit.put("date", when.toLowerCase());

		ThreadRept.this.offerSend(sendit);//
	}

	public final void active(int tmout) {
		String when = getDateTime();
		JSONObject sendit = new JSONObject();

		sendit.put("flag", 2002);// idx
		sendit.put("ip", this.getHost());
		sendit.put("name", this.getName());
		sendit.put("tags", "thread-Heart");
		sendit.put("appName", ThreadRept.app);
		sendit.put("date", when.toLowerCase());
		long now = System.currentTimeMillis();
		sendit.put("valid", now + 0 + tmout);

		ThreadRept.this.offerSend(sendit);
	}

	public void alarm(final int level,//
					final String email,//
					final String mobile,//
					final String content) {
		String when = getDateTime();
		JSONObject sendit = new JSONObject();

		sendit.put("flag", 2003);// idx
		sendit.put("email", email);
		sendit.put("mobile", mobile);
		sendit.put("ip", this.getHost());
		sendit.put("name", this.getName());
		sendit.put("tags", "thread-Alarm");
		sendit.put("appName", ThreadRept.app);
		sendit.put("date", when.toLowerCase());

		sendit.put("content", content);// data

		ThreadRept.this.offerSend(sendit);//

	}

	private void offerSend(JSONObject sendit) {
		sendUdp(sendit.toJSONString(), udp);
	}

	boolean sendUdp(String data, String addr) {
		addr = addr.substring(6);
		byte[] bs = data.getBytes();
		String[] part = addr.split(":");

		try {
			if (usckt == null) {// 未创建
				usckt = new DatagramSocket();
				usckt.setBroadcast(false);
				usckt.setSendBufferSize(10240);
			}

			int len = bs.length;

			SocketAddress sckt = null;
			DatagramPacket pack = null;

			int port = 1234;// 默认tcp端口

			if (addr.indexOf(":") != -1/**/) {
				port = Integer.valueOf(part[1]);
			}

			sckt = new InetSocketAddress(//
							part[0], port);

			pack = new DatagramPacket(//
							bs, len, sckt);

			if (this.usckt != null) {// 已创建
				this.usckt.send(pack);// 发送
			}

			System.out.println("发送(UDP)监控消息成功["//
							+ addr + "]=>" + data);

			return Boolean.TRUE.booleanValue();

		} catch (final Exception throwed) {
			System.out.println("发送(UDP)监控消息失败["//
							+ addr + "]=>" + data);

			return Boolean.FALSE.booleanValue();
		}
	}
}
