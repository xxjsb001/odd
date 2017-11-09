package com.vtradex.wms.server.model.webservice;

import java.util.Date;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.VersionalEntity;
import com.vtradex.wms.server.model.base.BaseStatus;
/**webservive 接口方法信息*/
public class ItmsWebArgument extends VersionalEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ItmsWebArgument() {
		// TODO Auto-generated constructor stub
	}
	@UniqueKey
	private ItmsWebUrl url;
	@UniqueKey
	private String method;
	/**报文头*/
	private String jsonKey;
	private String description;//备注
	/**读取表 [必须字段:ID NUMBER(19) not null;
	STATUS NUMBER(2) default 1;CREATED_TIME TIMESTAMP(6) default sysdate]*/
	private String extTableName;
	/**执行账号 @ItmsSqlTaskType*/
	private String dataSource;
	
	/**是否参与job计划*/
	private Boolean isJob = false;
	/** 执行频率(分钟) */
	private Integer repeatTimes = 0;
	/** 上一次时间 */
	private Date lastTime;
	
	/** 
	 * 状态
	 * {@link BaseStatus}
	 * */
	private String status;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getLastTime() {
		return lastTime;
	}
	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}
	public Boolean getIsJob() {
		return isJob;
	}
	public void setIsJob(Boolean isJob) {
		this.isJob = isJob;
	}
	public Integer getRepeatTimes() {
		return repeatTimes;
	}
	public void setRepeatTimes(Integer repeatTimes) {
		this.repeatTimes = repeatTimes;
	}
	public String getExtTableName() {
		return extTableName;
	}
	public void setExtTableName(String extTableName) {
		this.extTableName = extTableName;
	}
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	public ItmsWebUrl getUrl() {
		return url;
	}
	public void setUrl(ItmsWebUrl url) {
		this.url = url;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getJsonKey() {
		return jsonKey;
	}
	public void setJsonKey(String jsonKey) {
		this.jsonKey = jsonKey;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
