package com.vtradex.wms.server.model.webservice;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.VersionalEntity;
/**webservive 接口信息*/
public class ItmsWebUrl extends VersionalEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ItmsWebUrl() {
		// TODO Auto-generated constructor stub
	}
	@UniqueKey
	private String company;
	@UniqueKey
	private String url;
	/**接口账号*/
	private String appKey;
	/**接口账号密匙*/
	private String appSecret;
	private String description;//备注
	
	
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getAppSecret() {
		return appSecret;
	}
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
