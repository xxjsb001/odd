package com.vtradex.wms.server.model.inventory;

import java.util.Date;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.VersionalEntity;
import com.vtradex.thorn.server.util.BeanUtils;
import com.vtradex.wms.server.model.base.BaseStatus;
import com.vtradex.wms.server.model.base.ItmsBaseStatus;
import com.vtradex.wms.server.model.warehouse.ItmsWarehouse;
/***
 * 文件管理
 * @author myc
 *
 */
public class ItmsFiles extends VersionalEntity{
	public static final String DIR = "/vtradex/imoprtFiles-notdelete";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 所属服务器 */
	@UniqueKey
	private ItmsWarehouse warehouse;
	/**文件名称 */
	private String supCode;
	/**文件概要 */
	private String note;
	/** 文件路径*/
	private String supName;
	/**下载 */
	private String extendPropc1;
	/**下载次数 */
	private Double quantity = 0D;
	/**文件类型  @ItmsFileType*/
	private String type;
	/**是否功能分享*/
	private Boolean isPublicShare = false;
	//===============================================================
	
	private Date storageDate;
	
	private String itemCode;
	private String itemName;
	
	
	/**备注 */
	private String description;
	/**日结日期 */
	private Date orderDate;
	
	/**期初库存 */
	private Double beforeQuantity = 0D;
	/**和历史日结数据匹配*/
	private String hashCode;
	/** 
	 * 状态
	 * {@link BaseStatus}
	 * */
	private String status;
	
	public ItmsFiles() {
		super();
	}
	public void initHashcode(){
		this.hashCode = BeanUtils.getFormat(this.getWarehouse().getId(),this.getSupCode(),this.getItemCode()
				,this.getExtendPropc1(),this.getType());
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public ItmsWarehouse getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(ItmsWarehouse warehouse) {
		this.warehouse = warehouse;
	}
	public String getSupCode() {
		return supCode;
	}
	public void setSupCode(String supCode) {
		this.supCode = supCode;
	}
	public String getSupName() {
		return supName;
	}
	public void setSupName(String supName) {
		this.supName = supName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getStorageDate() {
		return storageDate;
	}
	public void setStorageDate(Date storageDate) {
		this.storageDate = storageDate;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	
	public Boolean getIsPublicShare() {
		return isPublicShare;
	}
	public void setIsPublicShare(Boolean isPublicShare) {
		this.isPublicShare = isPublicShare;
	}
	public String getExtendPropc1() {
		return extendPropc1;
	}
	public void setExtendPropc1(String extendPropc1) {
		this.extendPropc1 = extendPropc1;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Double getBeforeQuantity() {
		return beforeQuantity;
	}
	public void setBeforeQuantity(Double beforeQuantity) {
		this.beforeQuantity = beforeQuantity;
	}
	public String getHashCode() {
		return hashCode;
	}
	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}
	
}
