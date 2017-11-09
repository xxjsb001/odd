package com.vtradex.wms.server.model.itms;

import java.util.Date;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.VersionalEntity;
import com.vtradex.wms.server.model.warehouse.ItmsSid;

public class ItmsDbLinks extends VersionalEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ItmsDbLinks() {
		// TODO Auto-generated constructor stub
	}
	/** 实例*/
	@UniqueKey
	private ItmsSid sid;
	/** 所有者*/
	private String owner;
	/** db_lilnk*/
	@UniqueKey
	private String dbLilnk;
	/** 用户名*/
	private String userName;
	/** 地址*/
	private String host;
	
	/** 用户新建时使用*/
	private String passWord;
	/** db_lilnk日期*/
    private Date created;
    
	private String description;
	/** 2*/
	private String column1;
	/** 3*/
	private String column2;
	
	public ItmsSid getSid() {
		return sid;
	}

	public void setSid(ItmsSid sid) {
		this.sid = sid;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getDbLilnk() {
		return dbLilnk;
	}

	public void setDbLilnk(String dbLilnk) {
		this.dbLilnk = dbLilnk;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getColumn1() {
		return column1;
	}

	public void setColumn1(String column1) {
		this.column1 = column1;
	}

	public String getColumn2() {
		return column2;
	}

	public void setColumn2(String column2) {
		this.column2 = column2;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
