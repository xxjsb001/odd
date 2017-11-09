package com.vtradex.wms.server.model.itms;

import com.vtradex.thorn.server.model.Entity;

public class ItmsInterfaceEmailRule extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String type;//业务类型(InterfaceType.java中邮件相关的类型)
	
	private String receiver;//收件人
	
	private String emailAddr;//邮件地址
	
	private String mobile;//手机号码(用于发短信)
	
	private String desc;//邮件备注
	
	/** 状态*/
	private String status;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getEmailAddr() {
		return emailAddr;
	}

	public void setEmailAddr(String emailAddr) {
		this.emailAddr = emailAddr;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}