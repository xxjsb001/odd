package com.vtradex.wms.server.model.base;

import com.vtradex.thorn.server.model.DomainModel;

/**
 * @category 联系方式
 * @author peng.lei
 * @version 1.0
 * @created 15-二月-2011 10:06:35
 */
public class Contact extends DomainModel{
	/** */
	private static final long serialVersionUID = 1L;
	/** 联系人*/
	private String contactName;
	/** 联系电话*/
	private String telephone;
	/** 手机号*/
	private String mobile;
	/** 传真号码*/
	private String fax;
	/** 邮箱账号*/
	private String email;
	/** 国家*/
	private String country;
	/** 省份*/
	private String province;
	/** 城市*/
	private String city;
	/** 联系地址*/
	private String address;
	/** 邮编*/
	private String postCode;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Contact() {}

	public void finalize() throws Throwable {

	}

}