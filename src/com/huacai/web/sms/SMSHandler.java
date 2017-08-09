package com.huacai.web.sms;

import java.util.Date;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.session.IoSession;

import com.huacai.util.ReadResource;
import com.huacai.util.StringUtil;

/**
 * 接收到短信的处理
 * 
 */
public class SMSHandler extends Thread {
	protected final Log logger = LogFactory.getLog(getClass());

	IoSession session;
	String message;

	public SMSHandler(IoSession session, String message) {
		super("SMS SMSHandler");
		this.session = session;
		this.message = message;
	}

	public void run() {
		this.handler();
	}

	public void handler() {
		String commandStr = (String) message;
		if (commandStr != null) {
			try {
				commandStr = commandStr.trim();
				String command = "";
				command = this.getContent(commandStr, "v_cmd");
				SMS sms = this.parseResult(command, commandStr);
				if (command.equalsIgnoreCase("deliver")) {
					// 接收到上行短信
					logger.info("接收到上行短信 =" + sms.getMobile() + " " + sms.getMessage());

					String content = sms.getMessage().trim();
					content = content.replaceAll("　", " ");
					// 去掉多余的空格
					content = content.replaceAll(" +", " ");
					content = content.toLowerCase();
					content = content.replaceAll("×", "*");
					content = content.replaceAll("＃", "#");
					content = content.replaceAll("；", ";");
					content = content.replaceAll("？", "?");
					content = content.toLowerCase();
					// 以#为结束符
					int finishIndex = content.lastIndexOf('#');
					if (content.length() > 1 && finishIndex == content.length() - 1) {
						content = content.substring(0, content.length() - 1);
					}

					session.write("v_cmd=deliver_resp&v_msgid=" + sms.getPid());
					SMSClient.setActive(new Date());

				} else if (command.equalsIgnoreCase("activetest")) {
					// 发送连接测试
					logger.info("短信客户端接收到连接测试指令");
					session.write(commandStr);
					SMSClient.setActive(new Date());
				} else if (command.equalsIgnoreCase("login_resp")) {
					String result = this.getContent(commandStr, "v_result");
					if (result.equals("0")) {
						SMSClient.setLogin(true);
						logger.info("连接短信中心成功");
					} else {
						SMSClient.setLogin(false);
						logger.info("连接短信中心失败");
					}
					SMSClient.setActive(new Date());
				}
			} catch (Exception e) {
				logger.error("SMSHandler Exception");
				e.printStackTrace();
			}
		} else {
		}
	}

	/**
	 * 解析从服务器端接收到数据
	 * 
	 * @param command
	 *            命令
	 * @param deliverStr
	 *            接收的数据
	 * @return 短信信息
	 */
	private SMS parseResult(String command, String deliverStr) {
		SMS sms = null;
		if (command.equalsIgnoreCase("deliver")) {
			// 接收的短信信息
			sms = new SMS();
			String time = String.valueOf(new Date().getTime() / 1000);
			sms.setPid(this.getContent(deliverStr, "v_msgid"));
			sms.setType(MessageStatus.TYPE_INCOMING);
			sms.setMobile(this.getContent(deliverStr, "v_mobile"));
			sms.setMessage(new String(StringUtil.deCode(this.getContent(deliverStr, "v_message"))));
			sms.setFrom(this.getContent(deliverStr, "v_sp"));

			sms.setStatus(MessageStatus.NON_READ);
			sms.setSendTime(time);
			sms.setLinkid(this.getContent(deliverStr, "v_linkid"));
			sms.setItem(this.getContent(deliverStr, "v_item"));
			sms.setFee("1");

		} else if (command.equalsIgnoreCase("submit_resp")) {
			// v_cmd Max(16) 字符串 必填 短信命令，发送短信时为status
			// v_msgid Max(16) 字符串 必填 短信编号
			// v_status Max(2) 字符串 必填 短信状态00 表示发送成功12 表示发送失败

			sms = new SMS();
			String result = this.getContent(deliverStr, "v_result");

			logger.info("发送响应 ID=" + this.getContent(deliverStr, "v_msgid") + " result=" + result);

		}
		// else if (command.equalsIgnoreCase("status")) {
		// // 接收的发送报告
		// // v_cmd Max(16) 字符串 必填 短信命令，发送短信时为status
		// // v_msgid Max(16) 字符串 必填 短信编号
		// // v_status Max(2) 字符串 必填 短信状态00 表示发送成功12 表示发送失败
		// sms = new SMS();
		// String status = this.getContent(deliverStr, "v_status");
		// Debug.println("ID=" + this.getContent(deliverStr, "v_msgid")
		// + " status=" + status);
		// if (status.equals("00")) {
		// status = MessageStatus.SEND_SUCCEED;
		// } else {
		// status = MessageStatus.SEND_FAILED;
		// }
		// }

		return sms;
	}

	/**
	 * 得到字符串中参数的值
	 * 
	 * @param input
	 *            字符串
	 * @param para
	 *            参数名称
	 * @return 参数的值
	 */
	private String getContent(String input, String para) {
		if (input.equals("") || para.equals("")) {
			return "";
		}
		String vv = "";
		StringTokenizer st = new StringTokenizer(input, "&");
		while (st.hasMoreElements()) {
			vv = st.nextToken();
			if (vv.indexOf("=") != -1) {
				if (vv.indexOf(para) != -1 && vv.substring(0, vv.indexOf("=")).equals(para)) {
					vv = vv.substring(vv.indexOf("=") + 1);
					return vv;
				}
			}
		}
		return "";
	}

	/**
	 * 是否为短信猫号码
	 * 
	 * @param sp
	 * @return
	 */
	public boolean isModem(String sp) {
		boolean flag = false;
		if (sp != null && sp.trim().length() == 11) {
			if (sp.substring(0, 2).equals("13")) {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * @return 返回 message。
	 */
	public Object getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            要设置的 message。
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	public static void main(String[] args) {
		String content = "A123#";

		content = content.toLowerCase();
		content = content.replaceAll("　", " ");
		// 去掉多余的空格
		content = content.replaceAll(" +", " ");
		content = content.replaceAll("×", "*");
		content = content.replaceAll("＃", "#");
		content = content.replaceAll("；", ";");
		content = content.replaceAll("？", "?");
		content = content.toLowerCase();
		// 以#为结束符
		int finishIndex = content.lastIndexOf('#');

		ReadResource resource = new ReadResource();
//		resource.init("D:/workspace2/lotteryserver/WebRoot");

		// ILotHandlerBO bean = (ILotHandlerBO) InitializeBean.getCtx().getBean(
		// "lotOrderBO");
		// bean.getMobileHandler("", "");

	}

}