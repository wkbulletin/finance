package com.huacai.web.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 发送邮件的线程
 */
public class MailSendThread extends Thread {
	protected final Log logger = LogFactory.getLog(getClass());
	private String smtphost;// Email服务器的网址或IP地址
	private String username;// Email认证的用户名
	private String password;	// Email认证的密码
	private String from;// 发信地址
	private String to;	// 目的地址
	private String cc;// 抄送
	private String bcc;// 密送
	private String subject;	// 电子邮件的标题
	private String content;	// 邮件内容
	private boolean flag;	// 是否发送成功

	public MailSendThread() {
		// Email服务器的网址或IP地址
		smtphost = InitializeData.CONFIG_SMTP_HOST;
		// Email认证的用户名
		username = InitializeData.CONFIG_SMTP_USER;
		// Email认证的密码
		password = InitializeData.CONFIG_SMTP_PASSWORD;
	}

	public void run() {
		MailUtil mailUtil = new MailUtil(smtphost, from, to, "", "", subject, username, password);
		mailUtil.setMessage(content);
		mailUtil.setHtml(true);
		flag = mailUtil.Send();
		System.out.println("发送邮件是否成功:" + flag);
	}

	public String getBcc() {
		return bcc;
	}

	public void setBcc(String bcc) {
		this.bcc = bcc;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSmtphost() {
		return smtphost;
	}

	public void setSmtphost(String smtphost) {
		this.smtphost = smtphost;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public static void main(String[] args) {

		String subject = "欢迎注册成为华彩会员";
		String content = "zczxcZXCZXc=" + "123" + " ZXC点此激活,激活码为 ";
		String account = "lizurong@chinalotsynergy.com";
		// 发送注册邮件
		String from = InitializeData.CONFIG_SMTP_FROM;
		String to = account;

		MailSendThread mailSendThread = new MailSendThread();
		mailSendThread.setFrom(from);
		mailSendThread.setTo(to);
		mailSendThread.setSubject(subject);
		mailSendThread.setContent(content);
		mailSendThread.start();
	}

}
