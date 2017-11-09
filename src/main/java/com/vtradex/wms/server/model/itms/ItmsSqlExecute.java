package com.vtradex.wms.server.model.itms;

import java.util.Date;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.VersionalEntity;
import com.vtradex.wms.server.model.base.ItmsBaseStatus;
/**sql 文件 yc*/
public class ItmsSqlExecute extends VersionalEntity{
	public static final String EDN_SQL = ".SQL";
	public static final String EDN_JSON = ".JSON";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**所属文件夹*/
	private ItmsSqlDir dir;
	/** 文件名  全局唯一*/
	@UniqueKey
	private String filename;
	
	/**是否按时*/
	private Boolean isDoScheduler = false;
	/**每天几时执行 目前支持到小时 @ ScheduleTime*/
	private String schedulerTime;
	/** 执行频率(分钟) */
	private Integer repeatTimes = 0;
	/** 上一次时间 */
	private Date lastTime;
	private String description;//备注
	private String status = ItmsBaseStatus.OPEN;
	private String type = ItmsSqlTaskType.EDI_SQL_FILE;
	
	public Boolean getIsDoScheduler() {
		return isDoScheduler;
	}

	public void setIsDoScheduler(Boolean isDoScheduler) {
		this.isDoScheduler = isDoScheduler;
	}

	public String getSchedulerTime() {
		return schedulerTime;
	}

	public void setSchedulerTime(String schedulerTime) {
		this.schedulerTime = schedulerTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ItmsSqlDir getDir() {
		return dir;
	}

	public void setDir(ItmsSqlDir dir) {
		this.dir = dir;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Integer getRepeatTimes() {
		return repeatTimes;
	}

	public void setRepeatTimes(Integer repeatTimes) {
		this.repeatTimes = repeatTimes;
	}

	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ItmsSqlExecute() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
