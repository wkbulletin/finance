package com.huacai.web.sms;

import java.math.BigDecimal;

/**
 * 短信表
 */
public class SMS {
	int id;

	int memberId;

	String merchantId;

	String pid;

	String type;

	// 服务号码
	String from;

	// 节目标识
	String item;

	// 计费类型，1：免费，2：按条计费，3：定制包月计费（FeeType=3目前只针对联通有效）。默认：2
	String fee;

	String mobile;

	String message;

	String status;

	BigDecimal price;

	int num;

	String createTime;

	String sendTime;

	String updateTime;

	int level;

	String linkid;

	String valid;

	String confirm;

	String smsReturn;

	public SMS() {
		this.memberId = 0;
		this.merchantId = "0";
		this.pid = "";
		this.type = "IN";
		this.from = "";
		this.item = "";
		this.fee = "1";
		this.mobile = "";
		this.message = "";
		this.status = "99";
		this.price = new BigDecimal("0.00");
		this.num = 1;
		this.sendTime = "0";
		this.updateTime = "0";
		this.level = 5;
		this.linkid = "";
		this.valid = "Y";
		this.confirm = "N";
		this.smsReturn = "N";
	}

	/**
	 * @return 返回confirm。
	 */
	public String getConfirm() {
		return confirm;
	}

	/**
	 * @param confirm
	 *            设置confirm。
	 */
	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}

	/**
	 * @return 返回fee。
	 */
	public String getFee() {
		return fee;
	}

	/**
	 * @param fee
	 *            设置fee。
	 */
	public void setFee(String fee) {
		this.fee = fee;
	}

	/**
	 * @return 返回id。
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            设置id。
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return 返回item。
	 */
	public String getItem() {
		return item;
	}

	/**
	 * @param item
	 *            设置item。
	 */
	public void setItem(String item) {
		if (item == null) {
			item = "";
		}
		this.item = item;
	}

	/**
	 * @return 返回level。
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level
	 *            设置level。
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * @return 返回linkid。
	 */
	public String getLinkid() {
		return linkid;
	}

	/**
	 * @param linkid
	 *            设置linkid。
	 */
	public void setLinkid(String linkid) {
		if (linkid == null) {
			linkid = "";
		}
		this.linkid = linkid;
	}

	/**
	 * @return 返回message。
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            设置message。
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return 返回mobile。
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile
	 *            设置mobile。
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return 返回price。
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * @param price
	 *            设置price。
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * @return 返回sendTime。
	 */
	public String getSendTime() {
		return sendTime;
	}

	/**
	 * @param sendTime
	 *            设置sendTime。
	 */
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	/**
	 * @return 返回sp。
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @param from
	 *            设置sp。
	 */
	public void setFrom(String from) {
		if (from == null) {
			from = "";
		}
		this.from = from;
	}

	/**
	 * @return 返回status。
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            设置status。
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return 返回type。
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            设置type。
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return 返回updateTime。
	 */
	public String getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime
	 *            设置updateTime。
	 */
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @return 返回valid。
	 */
	public String getValid() {
		return valid;
	}

	/**
	 * @param valid
	 *            设置valid。
	 */
	public void setValid(String valid) {
		this.valid = valid;
	}

	/**
	 * @return 返回createTime。
	 */
	public String getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            设置createTime。
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return 返回memberId。
	 */
	public int getMemberId() {
		return memberId;
	}

	/**
	 * @param memberId
	 *            设置memberId。
	 */
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	/**
	 * @return 返回merchantId。
	 */
	public String getMerchantId() {
		return merchantId;
	}

	/**
	 * @param merchantId
	 *            设置merchantId。
	 */
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	/**
	 * @return 返回pid。
	 */
	public String getPid() {
		return pid;
	}

	/**
	 * @param pid
	 *            设置pid。
	 */
	public void setPid(String pid) {
		this.pid = pid;
	}

	/**
	 * @return 返回num。
	 */
	public int getNum() {
		return num;
	}

	/**
	 * @param num
	 *            设置num。
	 */
	public void setNum(int num) {
		this.num = num;
	}

	/**
	 * @return 返回smsReturn。
	 */
	public String getSmsReturn() {
		return smsReturn;
	}

	/**
	 * @param smsReturn
	 *            设置smsReturn。
	 */
	public void setSmsReturn(String smsReturn) {
		this.smsReturn = smsReturn;
	}

	public String toString() {
		StringBuffer message = new StringBuffer();
		message.append("ID=" + this.getId() + "\r\n");
		message.append("Type=" + this.getType() + "\r\n");
		message.append("SP=" + this.getFrom() + "\r\n");
		message.append("Item=" + this.getItem() + "\r\n");
		message.append("Fee_type=" + this.getFee() + "\r\n");
		message.append("Mobile=" + this.getMobile() + "\r\n");
		message.append("Message=" + this.getMessage() + "\r\n");
		message.append("Price=" + this.getPrice() + "\r\n");
		message.append("NUM=" + this.getNum() + "\r\n");
		return message.toString();
	}

}