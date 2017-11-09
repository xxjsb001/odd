package com.vtradex.wms.server.model.webservice;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.VersionalEntity;
/**外部访问接口表-字段*/
public class ItmsInterfaceColumn extends VersionalEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@UniqueKey
	private ItmsInterfaceTable interfaceTable;
	@UniqueKey
	private String jsonC;
	private String jsonV;
	
	public ItmsInterfaceColumn() {
		super();
	}
	public ItmsInterfaceTable getInterfaceTable() {
		return interfaceTable;
	}
	public void setInterfaceTable(ItmsInterfaceTable interfaceTable) {
		this.interfaceTable = interfaceTable;
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
