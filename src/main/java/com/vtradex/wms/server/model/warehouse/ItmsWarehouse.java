package com.vtradex.wms.server.model.warehouse;

import java.util.Set;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.base.BaseStatus;
import com.vtradex.wms.server.model.base.Contact;

/**
 * @category 服务器管理
 * @version 1.0
 * @created 15-二月-2011 10:04:39
 */
public class ItmsWarehouse extends Entity{	
	/** */
	private static final long serialVersionUID = -6153326314279660931L;
	/** IP*/
	@UniqueKey
	private String code;
	/** 名称*/
	private String name;
	/**扩展字段*/
	private Contact contact;
	private String image_url;
	private Integer x_Pos = 0;
	private Integer y_Pos = 0;
	/** 描述*/
	private String description;
	/** 
	 * 状态
	 * {@link BaseStatus}
	 * */
	private String status;	
	private Set<ItmsSid> warehouseAreas;		
	
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

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<ItmsSid> getWarehouseAreas() {
		return warehouseAreas;
	}

	public void setWarehouseAreas(Set<ItmsSid> warehouseAreas) {
		this.warehouseAreas = warehouseAreas;
	}

	public Integer getX_Pos() {
		return x_Pos;
	}

	public void setX_Pos(Integer x_Pos) {
		this.x_Pos = x_Pos;
	}

	public Integer getY_Pos() {
		return y_Pos;
	}

	public void setY_Pos(Integer y_Pos) {
		this.y_Pos = y_Pos;
	}

	public ItmsWarehouse(){

	}

	public void finalize() throws Throwable {

	}

}