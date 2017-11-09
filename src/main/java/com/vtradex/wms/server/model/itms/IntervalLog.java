package com.vtradex.wms.server.model.itms;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.VersionalEntity;
import com.vtradex.wms.server.model.move.WmsMoveDocStatus;
import com.vtradex.wms.server.model.warehouse.ItmsTable;
//**优化记录 **//
public class IntervalLog extends VersionalEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static String RANGE_NAME = "created_time";
	@UniqueKey
	private String code;
	private ItmsTable zone;
	private Boolean isBf = Boolean.FALSE;//是否备份
	private String bakTableName;//备份表名
	private String iTimes;//时间节点
	//@link WmsMoveDocStatus
	private String status = WmsMoveDocStatus.OPEN;//打开,生效,完成
	/**自定义别名 ORA-00972: 标识符过长时可以人为指定分区表名,但是也要确保唯一性*/
	private String interName;
	/**归档截点字段,默认 created_time*/
	private String rangeName = RANGE_NAME;
	
	public String getRangeName() {
		return rangeName;
	}
	public void setRangeName(String rangeName) {
		this.rangeName = rangeName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public ItmsTable getZone() {
		return zone;
	}
	public void setZone(ItmsTable zone) {
		this.zone = zone;
	}
	public Boolean getIsBf() {
		return isBf;
	}
	public void setIsBf(Boolean isBf) {
		this.isBf = isBf;
	}
	public String getBakTableName() {
		return bakTableName;
	}
	public void setBakTableName(String bakTableName) {
		this.bakTableName = bakTableName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getiTimes() {
		return iTimes;
	}
	public void setiTimes(String iTimes) {
		this.iTimes = iTimes;
	}
	public String getInterName() {
		return interName;
	}
	public void setInterName(String interName) {
		this.interName = interName;
	}
}
