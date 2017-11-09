package com.vtradex.wms.server.model.warehouse;

import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.base.BaseStatus;
/**游离表索引*/
public class ItmsIonizationIndex extends Entity{
	
	
	private static final long serialVersionUID = 1L;

	public ItmsIonizationIndex() {
		super();
	}
	public ItmsIonizationIndex(ItmsUsers users, String tableName) {
		super();
		this.users = users;
		this.tableName = tableName;
	}
	
	/**所属账号*/
	private ItmsUsers users;
	/**表名*/
	private String tableName;
	/** 
	 * 状态
	 * {@link BaseStatus}
	 * */
	private String status = BaseStatus.ENABLED;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public ItmsUsers getUsers() {
		return users;
	}
	public void setUsers(ItmsUsers users) {
		this.users = users;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
}
