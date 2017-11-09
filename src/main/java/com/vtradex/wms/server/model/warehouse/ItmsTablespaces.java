package com.vtradex.wms.server.model.warehouse;

import java.util.Date;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.VersionalEntity;


/**
 * @category 表空间
 */
public class ItmsTablespaces extends VersionalEntity {
	private static final long serialVersionUID = -1045233584854409285L;
	
	/** 实例*/
	private ItmsSid warehouseArea;
	
	/** 
	 *
	 * {@link WmsLocationType}
	 */
	private String type;
	
	/** 编码*/
	@UniqueKey
	private String code;
	/** 
	 * */
	private String verifyCode;
	
	/** 总量(M)*/
	private Double zone = 0D;
	/** 总块数*/
	private Double line = 0D;
	/** 剩余总量(M)*/
	private Double column = 0D;
	/** 剩余块数*/
	private Double layer = 0D;
	/**空闲比例*/
	private Double usedRate=0D;
	
	/**
	 * 扩充基数
	 */
	private Integer routeNo = 1024;
	/**
	 * 说明：库位盘点锁
	 */
	private Boolean lockCount = Boolean.FALSE;
	
	/** 状态  ENABLED DISABLED*/
	private String status;	
	
    private String locationStatus= WmsLocationStatus.EMPTY;;
    
    private Date cycleDate;
    
    private Date touchDate;
    
    private Integer touchCount = 0;
    
	private  Integer palletQuantity=0;
	
	/**
	 */
	private Boolean exceptionFlag = Boolean.FALSE;
	
	/**
     * 路径
     */
    private String dbfPath;
	/**
     * 描述
     */
    private String description;
	
	public Double getZone() {
		return zone;
	}

	public void setZone(Double zone) {
		this.zone = zone;
	}

	public Double getLine() {
		return line;
	}

	public void setLine(Double line) {
		this.line = line;
	}

	public Double getColumn() {
		return column;
	}

	public void setColumn(Double column) {
		this.column = column;
	}

	public Double getLayer() {
		return layer;
	}

	public void setLayer(Double layer) {
		this.layer = layer;
	}

	public String getDbfPath() {
		return dbfPath;
	}

	public void setDbfPath(String dbfPath) {
		this.dbfPath = dbfPath;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getExceptionFlag() {
		return exceptionFlag;
	}

	public void setExceptionFlag(Boolean exceptionFlag) {
		this.exceptionFlag = exceptionFlag;
	}

	public Integer getPalletQuantity() {
		return palletQuantity;
	}

	public void setPalletQuantity(Integer palletQuantity) {
		this.palletQuantity = palletQuantity;
	}

	public Double getUsedRate() {
		return usedRate;
	}

	public void setUsedRate(Double usedRate) {
		this.usedRate = usedRate;
	}

	public String getLocationStatus() {
		return locationStatus;
	}

	public void setLocationStatus(String locationStatus) {
		this.locationStatus = locationStatus;
	}

	public Date getCycleDate() {
		return cycleDate;
	}

	public void setCycleDate(Date cycleDate) {
		this.cycleDate = cycleDate;
	}

	public Date getTouchDate() {
		return touchDate;
	}

	public void setTouchDate(Date touchDate) {
		this.touchDate = touchDate;
	}

	public Integer getTouchCount() {
		return touchCount;
	}

	public void setTouchCount(Integer touchCount) {
		this.touchCount = touchCount;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Boolean getLockCount() {
		return lockCount;
	}

	public void setLockCount(Boolean lockCount) {
		this.lockCount = lockCount;
	}

	public Integer getRouteNo() {
		return routeNo;
	}

	public void setRouteNo(Integer routeNo) {
		this.routeNo = routeNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ItmsSid getWarehouseArea() {
		return warehouseArea;
	}

	public void setWarehouseArea(ItmsSid warehouseArea) {
		this.warehouseArea = warehouseArea;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public ItmsTablespaces(){

	}

	public void finalize() throws Throwable {

	}
}