package com.vtradex.wms.server.model.itms;

import java.io.File;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.VersionalEntity;
import com.vtradex.wms.server.model.base.ItmsBaseStatus;
import com.vtradex.wms.server.model.warehouse.ItmsWarehouse;
import com.vtradex.wms.server.web.filter.ItmsWarehouseHolder;
/**文件夹 yc*/
public class ItmsSqlDir extends VersionalEntity{
	public static final String DIR = "/jobs/";//一定是要共享的文件夹
	/**D:\jobs*/
	public static final String LOC_DIR = "D:"+File.separator+"jobs";//一定是要共享的文件夹
	/**JOB文件管理专用,外部接口禁用*/
	public static final String ITMS_FILE = "itms_file";
	/**JOB文件管理禁用,外部接口专用文件夹*/
	public static final String INTERFACE_FILE = "interface_file";
	/**按照年月分组的文件夹*/
	public static final String DATE_GROUP_FILE = "date_group_file";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@UniqueKey
	private String dirName;//文件夹名称
	private String description;//备注
	private String status = ItmsBaseStatus.OPEN;
	/** 所属服务器 */
	@UniqueKey
	private ItmsWarehouse warehouse = ItmsWarehouseHolder.getWmsWarehouse();
	/** 执行频率(分钟) */
	private Integer repeatTimes = 10;
	/**执行账号 @ItmsSqlTaskType*/
	private String dataSource;
	
	public Integer getRepeatTimes() {
		return repeatTimes;
	}

	public void setRepeatTimes(Integer repeatTimes) {
		this.repeatTimes = repeatTimes;
	}

	public ItmsWarehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(ItmsWarehouse warehouse) {
		this.warehouse = warehouse;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDirName() {
		return dirName;
	}

	public void setDirName(String dirName) {
		this.dirName = dirName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public ItmsSqlDir() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
