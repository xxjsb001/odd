package com.vtradex.wms.server.model.itms;

import com.vtradex.thorn.server.model.VersionalEntity;
import com.vtradex.wms.server.model.warehouse.ItmsSid;
import com.vtradex.wms.server.model.warehouse.ItmsWarehouse;
/**
 *记录用户登录退出的日志
 */
public class ItmsUserOnOff extends VersionalEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ItmsWarehouse warehouse;
	private ItmsSid itmsSid;
	private Long userLogId;
	private String userName;
	private String logTime;
	private String onOff;
	private String address;
	
	
	public ItmsUserOnOff() {
		super();
	}
	public ItmsUserOnOff(ItmsWarehouse warehouse, ItmsSid itmsSid,
			Long userLogId, String userName, String logTime, String onOff,
			String address) {
		super();
		this.warehouse = warehouse;
		this.itmsSid = itmsSid;
		this.userLogId = userLogId;
		this.userName = userName;
		this.logTime = logTime;
		this.onOff = onOff;
		this.address = address;
	}
	public ItmsWarehouse getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(ItmsWarehouse warehouse) {
		this.warehouse = warehouse;
	}
	public ItmsSid getItmsSid() {
		return itmsSid;
	}
	public void setItmsSid(ItmsSid itmsSid) {
		this.itmsSid = itmsSid;
	}
	public Long getUserLogId() {
		return userLogId;
	}
	public void setUserLogId(Long userLogId) {
		this.userLogId = userLogId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getLogTime() {
		return logTime;
	}
	public void setLogTime(String logTime) {
		this.logTime = logTime;
	}
	public String getOnOff() {
		return onOff;
	}
	public void setOnOff(String onOff) {
		this.onOff = onOff;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
}
