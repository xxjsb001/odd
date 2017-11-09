package com.vtradex.wms.server.model.webservice;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.VersionalEntity;
/**webservive 接口字段信息*/
public class ItmsWebColumn extends VersionalEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ItmsWebColumn() {
		// TODO Auto-generated constructor stub
	}
	@UniqueKey
	private ItmsWebArgument methods;
	@UniqueKey
	private String jsonC;
	
	private String jsonV;

	public ItmsWebArgument getMethods() {
		return methods;
	}

	public void setMethods(ItmsWebArgument methods) {
		this.methods = methods;
	}

	public String getJsonC() {
		return jsonC;
	}

	public void setJsonC(String jsonC) {
		this.jsonC = jsonC;
	}

	public String getJsonV() {
		return jsonV;
	}

	public void setJsonV(String jsonV) {
		this.jsonV = jsonV;
	}

}
