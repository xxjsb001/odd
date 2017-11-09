package com.vtradex.wms.server.model.webservice;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.VersionalEntity;
import com.vtradex.wms.server.model.base.BaseStatus;
/**外部访问接口表*/
public class ItmsInterfaceTable extends VersionalEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**读取表*/
	@UniqueKey
	private String extTableName;
	/**执行账号 @ItmsSqlTaskType*/
	private String dataSource;
	
	private String description;//备注
	/** 
	 * 状态
	 * {@link BaseStatus}
	 * */
	private String status;
	
	public ItmsInterfaceTable() {
		super();
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
}
