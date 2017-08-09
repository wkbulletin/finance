package com.huacai.web.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.huacai.web.dao.task.TaskMyDao;
import com.huacai.web.model.MailModel;

@Service
public class EmailServiceImpl {
	private static Logger logger = Logger.getLogger(EmailServiceImpl.class);

	private String excelPath = "d://";

	@Resource
	private JavaMailSender javaMailSender;

	@Resource
	private SimpleMailMessage simpleMailMessage;

	@Autowired
	private TaskMyDao taskMyDao;

	public void sendMail(HttpServletRequest request, int[] templateIds) {
		String task_name = (String) request.getAttribute("task_name");
		if (StringUtils.isEmpty(task_name)) {
			logger.info("发送邮件取消 , task_name为空");
			return;
		}
		for (int templateId : templateIds) {
			Boolean EmailTemplate_id = (Boolean) request.getAttribute("EmailTemplate_" + templateId);
			if (EmailTemplate_id == null || EmailTemplate_id == false) {
				logger.info("忽略发送任务 : " + templateId);
				continue;
			}
			Map<String, Object> template = taskMyDao.getTemplateById(templateId);
			if (MapUtils.getIntValue(template, "template_status") != 1) {
				logger.info("忽略发送任务 : " + templateId + " 该模板已禁用");
				continue;
			}
			String template_privileges = MapUtils.getString(template, "template_privileges");
			if (StringUtils.isEmpty(template_privileges)) {
				template_privileges = (String) request.getAttribute("EmailTemplate_pa");
			}
			String toMails = taskMyDao.getTemplateEmails(template_privileges);
			if (!StringUtils.isEmpty(toMails)) {
				MailModel mail = new MailModel();
				mail.setSubject("任务状态通知邮件");
				mail.setToEmails(toMails);
				// 内容
				String content = MapUtils.getString(template, "template_content");

				content = content.replaceAll("\\{任务名称\\}", task_name);
				mail.setContent(content);
				try {
					sendEmail(mail);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			} else {
				logger.info("发送邮件取消 , 收件人列表为空");
			}
		}
	}

	public void emailManage() throws Exception {
		MailModel mail = new MailModel();
		// 主题
		mail.setSubject("清单");

		// 附件
		Map<String, String> attachments = new HashMap<String, String>();
		attachments.put("清单.xlsx", excelPath + "清单.xlsx");
		mail.setAttachments(attachments);

		// 内容
		StringBuilder builder = new StringBuilder();
		builder.append("<html><body>你好！<br />");
		builder.append("&nbsp&nbsp&nbsp&nbsp附件是个人清单。<br />");
		builder.append("&nbsp&nbsp&nbsp&nbsp其中人信息；<br />");
		builder.append("</body></html>");
		String content = builder.toString();

		mail.setContent(content);

		sendEmail(mail);
	}

	/**
	 * 发送邮件
	 * 
	 * @author chenyq
	 * @date 2016-5-9 上午11:18:21
	 * @throws Exception
	 */
	public void sendEmail(MailModel mail) throws Exception {
		// 建立邮件消息
		MimeMessage message = javaMailSender.createMimeMessage();

		MimeMessageHelper messageHelper;
		try {
			messageHelper = new MimeMessageHelper(message, true, "UTF-8");
			// 设置发件人邮箱
			if (mail.getEmailFrom() != null) {
				messageHelper.setFrom(mail.getEmailFrom());
			} else {
				messageHelper.setFrom(simpleMailMessage.getFrom());
			}

			// 设置收件人邮箱
			if (mail.getToEmails() != null) {
				String[] toEmailArray = mail.getToEmails().split(";");
				List<String> toEmailList = new ArrayList<String>();
				if (null == toEmailArray || toEmailArray.length <= 0) {
					throw new Exception("收件人邮箱不得为空！");
				} else {
					for (String s : toEmailArray) {
						if (s != null && !s.equals("")) {
							toEmailList.add(s);
						}
					}
					if (null == toEmailList || toEmailList.size() <= 0) {
						throw new Exception("收件人邮箱不得为空！");
					} else {
						toEmailArray = new String[toEmailList.size()];
						for (int i = 0; i < toEmailList.size(); i++) {
							toEmailArray[i] = toEmailList.get(i);
						}
					}
				}
				messageHelper.setTo(toEmailArray);
			} else {
				messageHelper.setTo(simpleMailMessage.getTo());
			}

			// 邮件主题
			if (mail.getSubject() != null) {
				messageHelper.setSubject(mail.getSubject());
			} else {

				messageHelper.setSubject(simpleMailMessage.getSubject());
			}

			// true 表示启动HTML格式的邮件
			messageHelper.setText(mail.getContent(), true);

			// 添加图片
			if (null != mail.getPictures()) {
				for (Iterator<Map.Entry<String, String>> it = mail.getPictures().entrySet().iterator(); it.hasNext();) {
					Map.Entry<String, String> entry = it.next();
					String cid = entry.getKey();
					String filePath = entry.getValue();
					if (null == cid || null == filePath) {
						throw new RuntimeException("请确认每张图片的ID和图片地址是否齐全！");
					}

					File file = new File(filePath);
					if (!file.exists()) {
						throw new RuntimeException("图片" + filePath + "不存在！");
					}

					FileSystemResource img = new FileSystemResource(file);
					messageHelper.addInline(cid, img);
				}
			}

			// 添加附件
			if (null != mail.getAttachments()) {
				for (Iterator<Map.Entry<String, String>> it = mail.getAttachments().entrySet().iterator(); it.hasNext();) {
					Map.Entry<String, String> entry = it.next();
					String cid = entry.getKey();
					String filePath = entry.getValue();
					if (null == cid || null == filePath) {
						throw new RuntimeException("请确认每个附件的ID和地址是否齐全！");
					}

					File file = new File(filePath);
					if (!file.exists()) {
						throw new RuntimeException("附件" + filePath + "不存在！");
					}

					FileSystemResource fileResource = new FileSystemResource(file);
					messageHelper.addAttachment(cid, fileResource);
				}
			}
			messageHelper.setSentDate(new Date());
			// 发送邮件
			javaMailSender.send(message);
			logger.info("------------发送邮件完成----------");
			logger.info(mail);

		} catch (MessagingException e) {

			e.printStackTrace();
		}
	}

}