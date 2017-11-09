package com.vtradex.wms.server.model.warehouse;

import com.vtradex.thorn.server.model.Entity;
/**
 * @category 表
 * @version 1.0
 * @created 15-二月-2011 10:04:39
 */
public class ItmsTable extends Entity{

	private static final long serialVersionUID = -646975712324028524L;
	/**
	 * 所属账号
	 */
    private ItmsUsers user;
    /**
     * 表名
     */
    private String code;
    /**
     * 行数
     */
    private Integer numRows = 0;
    /**
     * 类型 @ CheckInventoryType
     */
    private String type;
    
    /**
     * 状态
     */
    private String status;
    /**
     * 描述
     */
    private String description;
    /**同义词表,当对表授权之后,系统会创建一个同义词,用来提供对外访问的表*/
	private String synonymName;
    
	public String getSynonymName() {
		return synonymName;
	}
	public void setSynonymName(String synonymName) {
		this.synonymName = synonymName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getNumRows() {
		return numRows;
	}
	public void setNumRows(Integer numRows) {
		this.numRows = numRows;
	}
	public ItmsUsers getUser() {
		return user;
	}
	public void setUser(ItmsUsers user) {
		this.user = user;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
    
}
 