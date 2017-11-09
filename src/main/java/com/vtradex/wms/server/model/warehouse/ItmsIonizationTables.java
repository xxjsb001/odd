package com.vtradex.wms.server.model.warehouse;

import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.base.BaseStatus;
/**游离表*/
public class ItmsIonizationTables extends Entity{
	
	
	private static final long serialVersionUID = 1L;

	public ItmsIonizationTables() {
		super();
	}
	public ItmsIonizationTables(ItmsUsers users, String tableName,
			String tablespaceName) {
		super();
		this.users = users;
		this.tableName = tableName;
		this.tablespaceName = tablespaceName;
	}
	@Override
	public String toString() {
		return "ItmsIonizationTables [users=" + users + ", tableName="
				+ tableName + ", tablespaceName=" + tablespaceName + "]";
	}
	/**所属账号*/
	private ItmsUsers users;
	/**表名*/
	private String tableName;
	/**当前表空间*/
	private String tablespaceName;
	
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
	public String getTablespaceName() {
		return tablespaceName;
	}
	public void setTablespaceName(String tablespaceName) {
		this.tablespaceName = tablespaceName;
	}
	
}
