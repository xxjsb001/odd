package com.vtradex.wms.server.model.warehouse;

import java.util.Date;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.Entity;
import com.vtradex.thorn.server.model.security.User;
import com.vtradex.wms.server.model.count.WmsCountLockType;
import com.vtradex.wms.server.web.filter.ItmsWarehouseHolder;


/**
 * @category 账号
 */
public class ItmsUsers extends Entity{
	
	/** */
	private static final long serialVersionUID = -703316509180923640L;
	@UniqueKey
	private ItmsWarehouse warehouse;
	/**
	 */
	private ItmsTable currentWorkArea; 
	/** 表空间*/
	@UniqueKey
	private ItmsTablespaces location;
	/** 用户名*/
	@UniqueKey
	private String code;
	/** 密码*/
	private String name;
	/**所属单位-同义词前缀-必填项-枚举 @ ItmpDeptNumber*/
	private String synonymName;
	/** 对应操作员*/
	private User user;
	/** 描述*/
	private String description;
	/** 状态*/
	private String status;
	/**
	 * 资源类型
	 * {@link :WmsWorkerType}
	 */
	private String type;
	/** 岗位*/
	private String station;
	
	/** 所属班组*/
	private ItmsUsers worker;
	
	/**账号状态@ WmsCountLockType*/
	private String accountStatus = WmsCountLockType.OPEN;
	/**锁定日期*/
	private Date lockDate;
	/**失效日期*/
	private Date expiryDate;
	
	/** 账号级别@ UserLeavel*/
	private String userLeavel;
	
	public String getUserLeavel() {
		return userLeavel;
	}

	public void setUserLeavel(String userLeavel) {
		this.userLeavel = userLeavel;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	public Date getLockDate() {
		return lockDate;
	}

	public void setLockDate(Date lockDate) {
		this.lockDate = lockDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getSynonymName() {
		return synonymName;
	}

	public void setSynonymName(String synonymName) {
		this.synonymName = synonymName;
	}

	public ItmsTablespaces getLocation() {
		return location;
	}

	public void setLocation(ItmsTablespaces location) {
		this.location = location;
	}

	public ItmsUsers getWorker() {
		return worker;
	}

	public void setWorker(ItmsUsers worker) {
		this.worker = worker;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ItmsWarehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(ItmsWarehouse warehouse) {
		this.warehouse = warehouse;
	}
    
	public ItmsTable getCurrentWorkArea() {
		return currentWorkArea;
	}

	public void setCurrentWorkArea(ItmsTable currentWorkArea) {
		this.currentWorkArea = currentWorkArea;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ItmsUsers(){

	}

	public void finalize() throws Throwable {

	}

	@Override
	public void doSaveBefore() {
		if (this.isNew() && this.warehouse == null) {
			this.setWarehouse(ItmsWarehouseHolder.getWmsWarehouse());
		}
		super.doSaveBefore();
	}

}