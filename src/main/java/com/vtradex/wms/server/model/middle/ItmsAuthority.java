package com.vtradex.wms.server.model.middle;

import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.organization.TypeOfBill;
import com.vtradex.wms.server.model.warehouse.ItmsTable;
import com.vtradex.wms.server.model.warehouse.ItmsUsers;
/**用户表权限关系*/
public class ItmsAuthority extends Entity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**用户*/
	private ItmsUsers worker;
	/**表*/
	private ItmsTable table;
	/**
	 * 权限：增删改查
	 * 
	 * {@link TypeOfBill}
	 */
	private String sender;
	/**同义词表,当对表授权之后,系统会创建一个同义词,用来提供对外访问的表*/
	private String synonymName;
	//备注
	private String description;
	
	public String getSynonymName() {
		return synonymName;
	}

	public void setSynonymName(String synonymName) {
		this.synonymName = synonymName;
	}

	public ItmsUsers getWorker() {
		return worker;
	}

	public void setWorker(ItmsUsers worker) {
		this.worker = worker;
	}

	public ItmsTable getTable() {
		return table;
	}

	public void setTable(ItmsTable table) {
		this.table = table;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
