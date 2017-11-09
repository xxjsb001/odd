package com.vtradex.wms.server.model.itms;

import com.vtradex.thorn.server.model.VersionalEntity;
//**统一门户**//
public class JacProjectUrl extends VersionalEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String url;
	private String description;
	/**所属单位-同义词前缀-必填项-枚举 @ ItmpDeptNumber*/
	private String synonymName;
	private String sortName;//别名
	
	public String getSortName() {
		return sortName;
	}
	public void setSortName(String sortName) {
		this.sortName = sortName;
	}
	public String getSynonymName() {
		return synonymName;
	}
	public void setSynonymName(String synonymName) {
		this.synonymName = synonymName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
