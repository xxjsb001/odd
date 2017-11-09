package com.vtradex.wms.server.model.itms;

import com.vtradex.thorn.server.model.VersionalEntity;
//**优化记录 分区时间段**//
public class IntervalTimes extends VersionalEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IntervalLog iLog;
	private Boolean isDrop = Boolean.FALSE;
	private Integer line = 0;
	private String iTimes;//时间节点
	public IntervalLog getiLog() {
		return iLog;
	}
	public void setiLog(IntervalLog iLog) {
		this.iLog = iLog;
	}
	public Boolean getIsDrop() {
		return isDrop;
	}
	public void setIsDrop(Boolean isDrop) {
		this.isDrop = isDrop;
	}
	public Integer getLine() {
		return line;
	}
	public void setLine(Integer line) {
		this.line = line;
	}
	public String getiTimes() {
		return iTimes;
	}
	public void setiTimes(String iTimes) {
		this.iTimes = iTimes;
	}
	
}
