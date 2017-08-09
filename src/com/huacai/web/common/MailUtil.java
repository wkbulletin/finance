package com.huacai.web.common;

import java.util.Date;
import java.util.Properties;
import java.util.Vector;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



@SuppressWarnings("unused")
public class MailUtil {
	protected final Log logger = LogFactory.getLog(getClass());
	private String smtphost;
	private String from;
	private String to;
	private String cc;
	private String bcc;
	private String subject;
	private StringBuffer messagetext = new StringBuffer();
	private String filename;
	private boolean debug;
	private boolean isHtml;
	private boolean auth;
	private String username;
	private String password;
	@SuppressWarnings("rawtypes")
	private Vector vfile;

	/**
	 * 电子邮件发送的构造函数
	 */
	@SuppressWarnings("rawtypes")
	public MailUtil() {
		messagetext.setLength(0);
		debug = false;
		isHtml = false;
		auth = false;
		vfile = new Vector(10, 10);
	}

	/**
	 * 电子邮件发送的构造函数
	 * 
	 * @param smtphost
	 *            smtp服务器的网址或IP地址
	 * @param from
	 *            发信地址
	 * @param to
	 *            目的地址
	 * @param cc
	 *            抄送
	 * @param bcc
	 *            密送
	 * @param subject
	 *            电子邮件的标题
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public MailUtil(String smtphost, String from, String to, String cc, String bcc, String subject) {
		messagetext.setLength(0);
		debug = false;
		isHtml = false;
		auth = false;
		vfile = new Vector(10, 10);
		this.smtphost = smtphost;
		this.from = from;
		this.to = to;
		this.cc = cc;
		this.bcc = bcc;
		this.subject = subject;
	}

	/**
	 * 电子邮件发送的构造函数(支持SMTP认证)
	 * 
	 * @param smtphost
	 *            smtp服务器的网址或IP地址
	 * @param from
	 *            发信地址
	 * @param to
	 *            目的地址
	 * @param cc
	 *            抄送
	 * @param bcc
	 *            密送
	 * @param subject
	 *            电子邮件的标题
	 * @param username
	 *            SMTP认证的username
	 * @param password
	 *            SMTP认证的password
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public MailUtil(String smtphost, String from, String to, String cc, String bcc, String subject, String username, String password) {
		messagetext.setLength(0);
		debug = false;
		isHtml = false;
		vfile = new Vector(10, 10);
		this.smtphost = smtphost;
		this.from = from;
		this.to = to;
		this.cc = cc;
		this.bcc = bcc;
		this.subject = subject;
		auth = true;
		this.username = username;
		this.password = password;
	}

	/**
	 * 设置SMTP服务器的IP地址或域名
	 * 
	 * @param smtphost
	 *            SMTP服务器的IP地址或域名
	 */
	public void setSmtpHost(String smtphost) {
		this.smtphost = smtphost;
	}

	/**
	 * 设置发信地址
	 * 
	 * @param from
	 *            发信地址
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * 设置收信地址
	 * 
	 * @param to
	 *            收信地址
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * 设置抄送
	 * 
	 * @param cc
	 *            抄送的E-MAIL
	 */
	public void setCc(String cc) {
		this.cc = cc;
	}

	/**
	 * 设置密送
	 * 
	 * @param bcc
	 *            密送的E-MAIL
	 */
	public void setBcc(String bcc) {
		this.bcc = bcc;
	}

	/**
	 * 设置邮件标题
	 * 
	 * @param subject
	 *            邮件标题
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * 设置是否进行SMTP验证的标志
	 * 
	 * @param auth
	 *            进行SMTP验证的标志
	 */
	public void setAuth(boolean auth) {
		this.auth = auth;
	}

	/**
	 * 设置SMTP验证的用户
	 * 
	 * @param username
	 *            用户
	 */
	public void setUserName(String username) {
		this.username = username;
	}

	/**
	 * 设置SMTP验证的密码
	 * 
	 * @param password
	 *            密码
	 */
	public void setPassWord(String password) {
		this.password = password;
	}

	/**
	 * 设置邮件正文
	 * 
	 * @param msg
	 *            邮件正文
	 */
	public void setMessage(String msg) {
		messagetext.append(msg);
	}

	/**
	 * 增加附件
	 * 
	 * @param fname
	 *            附件的文件名
	 */
	@SuppressWarnings("unchecked")
	public void attachfile(String fname) {
		vfile.addElement(fname);
	}

	/**
	 * 设置Debug的标志
	 * 
	 * @param debug
	 *            Debug的标志
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	/**
	 * 设置是否发送HTML类型的E-MAIL
	 * 
	 * @param isHtml
	 *            boolean
	 */
	public void setisHtml(boolean isHtml) {
		this.isHtml = isHtml;
	}

	/**
	 * 邮件发送
	 * 
	 * @return boolean
	 */
	public boolean Send() {
		Properties props = System.getProperties();
		props.put("mail.smtp.host", smtphost);
		if (auth) {
			props.put("mail.smtp.auth", "true");
		}
		Session session = Session.getDefaultInstance(props, null);
		session.setDebug(debug);
		try {
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from));
			if (to != null) {
				msg.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(to));
			}
			if (cc != null) {
				msg.setRecipients(javax.mail.Message.RecipientType.CC, InternetAddress.parse(cc));
			}
			if (bcc != null) {
				msg.setRecipients(javax.mail.Message.RecipientType.BCC, InternetAddress.parse(bcc));
			}
			msg.setSubject(subject, "gb2312");
			Multipart mp = new MimeMultipart();
			if (!isHtml) {
				msg.setText(messagetext.toString(), "utf-8");
				msg.setHeader("Content-Type", "text/plain;\n  charset=\"utf-8\"");
			} else {
				msg.setText(messagetext.toString(), "utf-8");
				msg.setHeader("Content-Type", "text/html;\n  charset=\"utf-8\"");
			}
			msg.setHeader("Content-Transfer-Encoding", "base64");
			msg.setHeader("MIME-Version", "1.0");
			msg.setSentDate(new Date());
			if (auth) {
				Transport transport = session.getTransport("smtp");
				transport.connect(smtphost, username, password);
				transport.sendMessage(msg, msg.getAllRecipients());
			} else {
				Transport.send(msg);
			}
		} catch (MessagingException mex) {
			logger.error("发送邮件错误");

			mex.printStackTrace();

			Exception ex = null;
			if ((ex = mex.getNextException()) != null) {
				ex.printStackTrace();
			}
			boolean flag = false;
			return flag;
		}
		return true;
	}

	public boolean isHtml() {
		return isHtml;
	}

	public void setHtml(boolean isHtml) {
		this.isHtml = isHtml;
	}

	public static void main(String[] args) {
		for (int i = 0; i < 1; i++) {
			MailUtil mail = new MailUtil("mail.kehu.cn", "ynx@kehu.cn", "fh@kehu.cn", "", "", "邮件测试 !!!! ", "ynx@kehu.cn", "123456");

			mail.setMessage("<style type=\"text/css\">");
			mail.setMessage("<!--");
			mail.setMessage("body,td,p,th{font-size:14px;line-height:180%;}");
			mail.setMessage("input{font-size:12px;}");
			mail.setMessage("-->");
			mail.setMessage("</style>");
			mail.setMessage("<!--模板开始-->");
			mail.setMessage("<!--partshowertemp_html.htm-->");
			mail.setMessage("<!--模板结束-->");
			mail.setMessage("<HTML> <HEAD> <!-- Notice: If this text is displayed, your email client cannot display properly the format we've sent you. You may want to consider upgrading to a more recent version of your email client. If you would like to receive only plain text messages, please reply to this message and put \"Change to text\" in the subject.-->");
			mail.setMessage("</HEAD><BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight=0 marginwidth=0><META http-equiv=Content-Type content=\"text/html; charset=GB2312\"><TITLE>出价有好礼！每天5次整点开奖</TITLE><STYLE type=text/css>.style10 { FONT-SIZE: 12px } .style6 { COLOR: #339900 } .style11 { FONT-SIZE: 13px } .style4 { FONT-WEIGHT: bold; COLOR: #ff00ff } .style12 { FONT-SIZE: 12px; LINE-HEIGHT: 18px } .unn { FONT-SIZE: 14px; LINE-HEIGHT: 150% } .unn1 { FONT-SIZE: 14px; LINE-HEIGHT: 150% }</STYLE><META content=\"MSHTML 6.00.2900.2722\" name=GENERATOR><TABLE cellSpacing=0 cellPadding=0 width=601 align=center border=0> <TBODY> <TR> <TD vAlign=center align=middle width=600 height=37> <DIV align=left><FONT face=\"Verdana, Arial, Helvetica, sans-serif\" color=#666666 size=1><STRONG>易趣将此邮件发送给 wwq( weiwenqi1972).</STRONG><BR>为协助您确认此邮件确系易趣发送，我们在邮件中特别注明了你注册时登记的姓名。 <A href=http://click4.ebay.com/73620607.24255.0.21399>了解详情.</A> <HR> </FONT></DIV></TD></TR></TBODY>");
			mail.setMessage("</TABLE><TABLE class=unn1 cellSpacing=0 cellPadding=0 width=601 align=center border=0> <TBODY> <TR> <TD width=154 height=51> <DIV align=center><A href=http://click4.ebay.com/73620607.24255.0.5448 target=_blank><IMG height=40 src=\"http://emailpics4.ebay.com/164237216/images/mignewlogo1-1.gif\" width=140 border=0></A></DIV></TD> <TD width=447> <DIV align=center><IMG height=9 src=\"http://emailpics4.ebay.com/164237216/images/1122-3c-arrow-1.gif\" width=10><A href=http://click4.ebay.com/73620607.24255.0.700 target=_blank><FONT color=#663333>我的易趣</FONT></A><IMG height=9 src=\"http://emailpics4.ebay.com/164237216/images/1122-3c-arrow-1.gif\" width=10><A href=http://click4.ebay.com/73620607.24255.0.701 target=_blank><FONT color=#663333>卖东西</FONT></A><IMG height=9 src=\"http://emailpics4.ebay.com/164237216/images/1122-3c-arrow-1.gif\" width=10><A href=http://click4.ebay.com/73620607.24255.0.702 target=_blank><FONT color=#ff3300>忘记密码</FONT></A>　 图片不能正常显示？");
			mail.setMessage("<A href=http://click4.ebay.com/73620607.24255.0.22340 target=_blank>点此查看</A></DIV></TD></TR></TBODY></TABLE><TABLE class=unn cellSpacing=0 cellPadding=0 align=center border=0> <TBODY> <TR class=unn111 align=middle> <TD width=56 background=\"http://emailpics4.ebay.com/164237216/images/050301-3c-pic1-1.gif\" height=27><A href=http://click4.ebay.com/73620607.24255.0.4001 target=_blank><FONT color=#654525>手机</FONT></A></TD> <TD width=56 background=\"http://emailpics4.ebay.com/164237216/images/050301-3c-pic1-1.gif\"><A href=http://click4.ebay.com/73620607.24255.0.4002 target=_blank><FONT color=#654525>电脑</FONT></A><A href=http://click4.ebay.com/73620607.24255.0.5770 target=_blank></A></TD> <TD width=56 background=\"http://emailpics4.ebay.com/164237216/images/050301-3c-pic1-1.gif\"><A href=http://click4.ebay.com/73620607.24255.0.10543 target=_blank><FONT color=#654525>牛仔裤</FONT></A><A href=http://click4.ebay.com/73620607.24255.0.16370 target=_blank></A>");
			mail.setMessage("<A href=http://click4.ebay.com/73620607.24255.0.2756 target=_blank></A></TD> <TD width=66 background=\"http://emailpics4.ebay.com/164237216/images/050301-3c-pic2-1.gif\"><A href=http://click4.ebay.com/73620607.24255.0.2757 target=_blank><FONT color=#654525>数码相机</FONT></A></TD> <TD width=56 background=\"http://emailpics4.ebay.com/164237216/images/050301-3c-pic1-1.gif\"><A href=http://click4.ebay.com/73620607.24255.0.2757 target=_blank></A><A href=http://click4.ebay.com/73620607.24255.0.21264 target=_blank><FONT color=#654525>男装</FONT></A></TD> <TD width=56 background=\"http://emailpics4.ebay.com/164237216/images/050301-3c-pic1-1.gif\"><A href=http://click4.ebay.com/73620607.24255.0.2762 target=_blank><FONT color=#654525>运动鞋</FONT></A></TD> <TD width=56 background=\"http://emailpics4.ebay.com/164237216/images/050301-3c-pic1-1.gif\"><A href=http://click4.ebay.com/73620607.24255.0.11461 target=_blank><FONT color=#654525>女装</FONT></A>");
			mail.setMessage("<A href=http://click4.ebay.com/73620607.24255.0.10543 target=_blank></A><A href=http://click4.ebay.com/73620607.24255.0.2762 target=_blank></A><A href=http://click4.ebay.com/73620607.24255.0.9292 target=_blank></A></TD> <TD width=56 background=\"http://emailpics4.ebay.com/164237216/images/050301-3c-pic1-1.gif\"><A href=http://click4.ebay.com/73620607.24255.0.13524 target=_blank><FONT color=#654525>女鞋</FONT></A></TD> <TD width=56 background=\"http://emailpics4.ebay.com/164237216/images/050301-3c-pic1-1.gif\"><A href=http://click4.ebay.com/73620607.24255.0.21121 target=_blank><FONT color=#654525>箱包</FONT></A></TD> <TD width=68><A href=http://click4.ebay.com/73620607.24255.0.20851 target=_blank><IMG height=27 src=\"http://emailpics4.ebay.com/164237216/images/050908-fem-pic17-1.gif\" width=67 border=0></A></TD></TR></TBODY></TABLE><DIV align=center><TABLE class=unn1 cellSpacing=0 cellPadding=0 border=0> <TBODY> <TR> <TD bgColor=#aeaeae height=5>");
			mail.setMessage("<IMG height=11 src=\"http://emailpics4.ebay.com/164237216/images/050823-d-pic1-1.gif\" width=600></TD></TR> <TR> <TD class=unn1 height=29>weiwenqi1972:<A href=http://click4.ebay.com/73620607.24255.0.22315 target=_blank><FONT color=#663333>快来赢取1000元现金、免费索尼PSP、最炫iPod Nano……缤纷好礼每天送不停</FONT></A></TD></TR></TBODY></TABLE><TABLE cellSpacing=0 cellPadding=0 width=623 border=0> <TBODY> <TR> <TD><A href=http://click4.ebay.com/73620607.24255.0.22315 target=_blank><IMG height=83 src=\"http://emailpics4.ebay.com/164237216/images/051010_pic01-1.gif\" width=623 border=0></A></TD></TR></TBODY></TABLE><TABLE cellSpacing=0 cellPadding=0 width=623 border=0> <TBODY> <TR> <TD width=27><IMG height=105 src=\"http://emailpics4.ebay.com/164237216/images/051010_pic02-1.gif\" width=27></TD> <TD class=style12 bgColor=#fffbcd>2005年10月11日至10月23日，每天中午12点到晚上8点，每2小时就有大礼送！每天5次整点准时开奖。你只要在对应时间段内出价并填写申请单，就有机会赢大奖！<BR>");
			mail.setMessage("<IMG height=31 src=\"http://emailpics4.ebay.com/164237216/images/051010_pic10-1.gif\" width=359 useMap=#Map2 border=0><BR>成功介绍朋友参加活动,如果他/她被抽中，你将得到同样奖品！<BR></TD> <TD bgColor=#fffbcd></TD> <TD width=218><IMG height=105 src=\"http://emailpics4.ebay.com/164237216/images/051010_pic03-1.gif\" width=218 useMap=#Map3 border=0></TD></TR> <TR> <TD><IMG height=491 src=\"http://emailpics4.ebay.com/164237216/images/051010_pic04-1.gif\" width=27></TD> <TD vAlign=top> <TABLE cellSpacing=0 cellPadding=0 width=100% border=0> <TBODY> <TR> <TD><IMG height=69 src=\"http://emailpics4.ebay.com/164237216/images/051010_pic05-1.gif\" width=359></TD></TR> <TR> <TD align=middle bgColor=#eeeeee height=388> <TABLE cellSpacing=0 cellPadding=2 width=100% border=0> <TBODY> <TR class=style12 align=middle> <TD><A href=http://click4.ebay.com/73620607.24255.0.22341 target=_blank><IMG height=100 src=\"http://emailpics4.ebay.com/164237216/images/051010_item10-1.gif\" width=100 border=0></A></TD> <TD>");
			mail.setMessage("<A href=http://click4.ebay.com/73620607.24255.0.22342 target=_blank><IMG height=100 src=\"http://emailpics4.ebay.com/164237216/images/051010_item12-1.gif\" width=100 border=0></A></TD> <TD><A href=http://click4.ebay.com/73620607.24255.0.22343 target=_blank><IMG height=100 src=\"http://emailpics4.ebay.com/164237216/images/051010_item18-1.gif\" width=100 border=0></A></TD></TR> <TR class=style12 align=middle> <TD><A href=http://click4.ebay.com/73620607.24255.0.22341 target=_blank><FONT color=#663333>最in丝绒/花呢元素</FONT></A><BR></TD> <TD><A href=http://click4.ebay.com/73620607.24255.0.22342 target=_blank><FONT color=#663333>手机带音乐视频</FONT></A></TD> <TD><A href=http://click4.ebay.com/73620607.24255.0.22343 target=_blank><FONT color=#663333>小魔女中筒靴￥98</FONT></A></TD></TR> <TR class=style12 align=middle> <TD><A href=http://click4.ebay.com/73620607.24255.0.22344 target=_blank><IMG height=100 src=\"http://emailpics4.ebay.com/164237216/images/051010_item15-1.gif\" width=100 border=0>");
			mail.setMessage("</A></TD> <TD><A href=http://click4.ebay.com/73620607.24255.0.22345 target=_blank><IMG height=100 src=\"http://emailpics4.ebay.com/164237216/images/051010_item14-1.gif\" width=100 border=0></A></TD> <TD><A href=http://click4.ebay.com/73620607.24255.0.22346 target=_blank><IMG height=100 src=\"http://emailpics4.ebay.com/164237216/images/051010_item13-1.gif\" width=100 border=0></A></TD></TR> <TR class=style12 align=middle> <TD><A href=http://click4.ebay.com/73620607.24255.0.22344 target=_blank><FONT color=#663333>美白好时机￥45</FONT></A><BR></TD> <TD><A href=http://click4.ebay.com/73620607.24255.0.22345 target=_blank><FONT color=#663333>进口MP3统统￥150</FONT></A><BR></TD> <TD><A href=http://click4.ebay.com/73620607.24255.0.22346 target=_blank><FONT color=#663333>时尚帆布包</FONT></A></TD></TR> <TR class=style12 align=middle> <TD><A href=http://click4.ebay.com/73620607.24255.0.22347 target=_blank><IMG height=100 src=\"http://emailpics4.ebay.com/164237216/images/051010_item16-1.gif\" ");
			mail.setMessage("width=100 border=0></A></TD> <TD><A href=http://click4.ebay.com/73620607.24255.0.22348 target=_blank><IMG height=100 src=\"http://emailpics4.ebay.com/164237216/images/051010_item17-1.gif\" width=100 border=0></A></TD> <TD><A href=http://click4.ebay.com/73620607.24255.0.22322 target=_blank><IMG height=100 src=\"http://emailpics4.ebay.com/164237216/images/051010_item04-1.gif\" width=100 border=0></A></TD></TR> <TR class=style12 align=middle> <TD><A href=http://click4.ebay.com/73620607.24255.0.22347 target=_blank><FONT color=#663333>北欧简约家饰￥35</FONT></A><BR></TD> <TD><A href=http://click4.ebay.com/73620607.24255.0.22348 target=_blank><FONT color=#663333>暖意床品新款￥68</FONT></A></TD> <TD><A href=http://click4.ebay.com/73620607.24255.0.22322 target=_blank><FONT color=#663333>超人气idog</FONT></A></TD></TR></TBODY></TABLE></TD></TR> <TR> <TD><IMG height=34 src=\"http://emailpics4.ebay.com/164237216/images/051010_pic07-1.gif\" width=359></TD></TR></TBODY></TABLE></TD> <TD width=19>");
			mail.setMessage("<IMG height=491 src=\"http://emailpics4.ebay.com/164237216/images/051010_pic06-1.gif\" width=19></TD> <TD vAlign=top> <TABLE cellSpacing=0 cellPadding=0 width=100% border=0> <TBODY> <TR> <TD><IMG height=290 src=\"http://emailpics4.ebay.com/164237216/images/051010_pic08-1.gif\" width=218 useMap=#Map4 border=0></TD></TR> <TR> <TD><IMG height=201 src=\"http://emailpics4.ebay.com/164237216/images/051010_pic09-1.gif\" width=218 useMap=#Map border=0></TD></TR></TBODY></TABLE></TD></TR></TBODY></TABLE><MAP name=Map><AREA shape=RECT target=_blank coords=6,129,215,184 href=http://click4.ebay.com/73620607.24255.0.19301><AREA shape=RECT target=_blank coords=6,1,215,56 href=http://click4.ebay.com/73620607.24255.0.22325><AREA shape=RECT target=_blank coords=6,65,215,120 href=http://click4.ebay.com/73620607.24255.0.22349></MAP><MAP name=Map2><AREA shape=RECT target=_blank coords=253,3,354,28 href=http://click4.ebay.com/73620607.24255.0.22315></MAP><MAP name=Map3><AREA shape=POLY target=_blank ");
			mail.setMessage("coords=26,41,22,104,106,104,111,47 href=http://click4.ebay.com/73620607.24255.0.22315></MAP><MAP name=Map4><AREA shape=POLY target=_blank coords=175,81,165,1,210,1,215,81 href=http://click4.ebay.com/73620607.24255.0.22315><AREA shape=POLY target=_blank coords=18,43,13,125,83,126,97,53 href=http://click4.ebay.com/73620607.24255.0.22315><AREA shape=POLY target=_blank coords=156,119,151,176,212,179,211,121 href=http://click4.ebay.com/73620607.24255.0.22315><AREA shape=POLY target=_blank coords=17,160,13,234,66,241,63,164 href=http://click4.ebay.com/73620607.24255.0.22315><AREA shape=RECT target=_blank coords=101,224,215,259 href=http://click4.ebay.com/73620607.24255.0.22315></MAP></DIV><BR><TABLE cellSpacing=0 cellPadding=0 width=600 align=center border=0> <TBODY> <TR> <TD class=style9 background=\"http://emailpics4.ebay.com/164237216/images/050906_find15-1.gif\" height=49> <TABLE cellSpacing=0 cellPadding=0 width=95% align=center border=0> <TBODY> <TR> <TD class=unn1><STRONG>[社区热门]");
			mail.setMessage("</STRONG><A href=http://click4.ebay.com/73620607.24255.0.22327 target=_blank><FONT color=#663333>易趣卖家的奥斯卡大奖赛--首届易趣“镇店之宝”大赛开始啦</FONT></A><A href=http://click4.ebay.com/73620607.24255.0.20851 target=_blank></A></TD></TR></TBODY></TABLE></TD></TR> <TR> <TD height=28> <DIV class=unn1 align=center><SPAN class=\"style7 style17\" style=\"FONT-WEIGHT: normal; COLOR: #ff0000; TEXT-DECORATION: none\" #invalid_attr_id=20px>【eBay易趣小提示】</SPAN><SPAN class=style18>您的用户名是 weiwenqi1972: 忘了密码? 请</SPAN><SPAN class=un><A href=http://click4.ebay.com/73620607.24255.0.702 target=_blank><SPAN class=style17>点击这里&gt;&gt;</SPAN></A></SPAN></DIV></TD></TR></TBODY></TABLE><BR><TABLE cellSpacing=0 cellPadding=0 width=600 align=center border=0> <TBODY> <TR align=middle> <TD vAlign=bottom width=311 height=46><A href=http://click4.ebay.com/73620607.24255.0.5448 target=_blank><IMG height=44 src=\"http://emailpics4.ebay.com/164237216/images/050405-3c-pic29-1.gif\" width=240 border=0>");
			mail.setMessage("</A></TD> <TD vAlign=bottom width=277><A href=http://click4.ebay.com/73620607.24255.0.700 target=_blank><IMG height=44 src=\"http://emailpics4.ebay.com/164237216/images/050405-3c-pic30-1.gif\" width=201 border=0></A></TD></TR></TBODY></TABLE><TABLE cellSpacing=0 cellPadding=0 width=600 align=center border=0> <TBODY> <TR> <TD vAlign=top col=1> <CENTER><FONT color=#666666><FONT face=Arial><BR><FONT face=宋体><SPAN style=\"FONT-SIZE: 9pt; FONT-FAMILY: 宋体\">您收到这封</SPAN><FONT face=Arial><SPAN lang=EN-US style=\"FONT-SIZE: 9pt; FONT-FAMILY: Arial\">e-mail</SPAN></FONT><FONT face=宋体><SPAN style=\"FONT-SIZE: 9pt; FONT-FAMILY: 宋体\">是因为您的</SPAN></FONT><FONT face=Arial><SPAN lang=EN-US style=\"FONT-SIZE: 9pt; FONT-FAMILY: Arial\">eBay</SPAN></FONT><FONT face=宋体><SPAN style=\"FONT-SIZE: 9pt; FONT-FAMILY: 宋体\">易趣帐户习惯设定表明<BR>您愿意收到</SPAN></FONT><FONT face=Arial><SPAN lang=EN-US style=\"FONT-SIZE: 9pt; FONT-FAMILY: Arial\">eBay</SPAN></FONT><FONT face=宋体>");
			mail.setMessage("<SPAN style=\"FONT-SIZE: 9pt; FONT-FAMILY: 宋体\">易趣关于特别优惠，活动，网站通知的电子邮件．<BR></SPAN></FONT></FONT><FONT size=1><FONT face=宋体><SPAN style=\"FONT-SIZE: 9pt; FONT-FAMILY: 宋体\">您订阅的邮件地址是</SPAN></FONT><FONT face=Arial><SPAN lang=EN-US style=\"FONT-SIZE: 9pt; FONT-FAMILY: Arial\">:</SPAN></FONT><FONT face=宋体><SPAN style=\"FONT-SIZE: 9pt; FONT-FAMILY: 宋体\">weiwenqi@yeah.net</SPAN></FONT><BR><FONT face=宋体><SPAN style=\"FONT-SIZE: 9pt; FONT-FAMILY: 宋体\">如果您不想继续收到此类邮件</SPAN></FONT><FONT face=Arial><SPAN lang=EN-US style=\"FONT-SIZE: 9pt; FONT-FAMILY: Arial\"><A href=http://click4.ebay.com/73620607.24255.0.703 target=_blank>请点击这里</A>.</SPAN></FONT><FONT face=宋体><SPAN style=\"FONT-SIZE: 9pt; FONT-FAMILY: 宋体\">或回复本邮件</SPAN></FONT><FONT face=Arial><SPAN lang=EN-US style=\"FONT-SIZE: 9pt; FONT-FAMILY: Arial\">(</SPAN></FONT><FONT face=宋体><SPAN style=\"FONT-SIZE: 9pt; FONT-FAMILY: 宋体\">请在邮件标题中注明</SPAN></FONT><FONT face=Arial>");
			mail.setMessage("<SPAN lang=EN-US style=\"FONT-SIZE: 9pt; FONT-FAMILY: Arial\">\"</SPAN></FONT><FONT face=宋体><SPAN style=\"FONT-SIZE: 9pt; FONT-FAMILY: 宋体\">退订</SPAN></FONT><FONT face=Arial><SPAN lang=EN-US style=\"FONT-SIZE: 9pt; FONT-FAMILY: Arial\">\"</SPAN></FONT><FONT face=宋体><SPAN style=\"FONT-SIZE: 9pt; FONT-FAMILY: 宋体\">字样</SPAN></FONT><FONT face=Arial><SPAN lang=EN-US style=\"FONT-SIZE: 9pt; FONT-FAMILY: Arial\">)<BR></SPAN></FONT><FONT face=宋体><SPAN style=\"FONT-SIZE: 9pt; FONT-FAMILY: 宋体\">请注意</SPAN></FONT><FONT face=Arial><SPAN lang=EN-US style=\"FONT-SIZE: 9pt; FONT-FAMILY: Arial\">:</SPAN></FONT><FONT face=宋体><SPAN style=\"FONT-SIZE: 9pt; FONT-FAMILY: 宋体\">处理您的退订最长可能需要</SPAN></FONT><FONT face=Arial><SPAN lang=EN-US style=\"FONT-SIZE: 9pt; FONT-FAMILY: Arial\">14</SPAN></FONT><FONT face=宋体><SPAN style=\"FONT-SIZE: 9pt; FONT-FAMILY: 宋体\">天的时间</SPAN></FONT><FONT face=Arial><SPAN lang=EN-US style=\"FONT-SIZE: 9pt; FONT-FAMILY: Arial\">.</SPAN></FONT>");
			mail.setMessage("<FONT face=宋体><SPAN style=\"FONT-SIZE: 9pt; FONT-FAMILY: 宋体\">如果您有任何问题</SPAN></FONT><FONT face=Arial><SPAN lang=EN-US style=\"FONT-SIZE: 9pt; FONT-FAMILY: Arial\">,</SPAN></FONT><FONT face=宋体><SPAN style=\"FONT-SIZE: 9pt; FONT-FAMILY: 宋体\">请查看<A href=http://click4.ebay.com/73620607.24255.0.1179 target=_blank>隐私权保护规则</A>和<A href=http://click4.ebay.com/73620607.24255.0.1180 target=_blank>用户协议</A></SPAN></FONT><FONT face=Arial><SPAN lang=EN-US style=\"FONT-SIZE: 9pt; FONT-FAMILY: Arial\">.<BR><BR>200</SPAN></FONT><FONT color=#666666><FONT face=Arial><FONT size=1><FONT face=Arial><SPAN lang=EN-US style=\"FONT-SIZE: 9pt; FONT-FAMILY: Arial\">5</SPAN></FONT></FONT></FONT></FONT></FONT></FONT><FONT face=Arial><FONT size=1><FONT face=Arial><SPAN lang=EN-US style=\"FONT-SIZE: 9pt; FONT-FAMILY: Arial\"> eBay Inc.</SPAN></FONT><FONT face=宋体><SPAN style=\"FONT-SIZE: 9pt; FONT-FAMILY: 宋体\">版权所有　 本公司保留所有权利<BR></SPAN></FONT></FONT><FONT face=宋体 size=1>");
			mail.setMessage("<SPAN style=\"FONT-SIZE: 9pt; FONT-FAMILY: 宋体\">所有商标与品牌皆为各自所有者的财产<BR></SPAN></FONT><FONT size=1><FONT face=宋体><SPAN style=\"FONT-SIZE: 9pt; FONT-FAMILY: 宋体\">易趣与易趣标志都是</SPAN></FONT><FONT face=Arial><SPAN lang=EN-US style=\"FONT-SIZE: 9pt; FONT-FAMILY: Arial\">eBay Inc.</SPAN></FONT><FONT face=宋体><SPAN style=\"FONT-SIZE: 9pt; FONT-FAMILY: 宋体\">的商标</SPAN></FONT></FONT></FONT></FONT> </CENTER></TD></TR></TBODY></TABLE>");
			mail.setMessage("<img src=\"http://click4.ebay.com/73620607.24255.0.-3\"  WIDTH=\"1\" HEIGHT=\"1\">");
			mail.setMessage("</BODY></HTML>");
			mail.isHtml = true;
			boolean flag = mail.Send();
			System.out.println("发送成功" + flag);
		}

	}
}