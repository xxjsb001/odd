package com.vtradex.wms.server.model.itms;

public interface ItmsSqlTaskType {
	/**默认的,对应数据源 jdbcExtendDataNo1*/
	public static String EDI_SQL_FILE = "dataNo1";
	/**对应数据源 jdbcExtendDataNo2*/
	public static String SCL_BMS_FILE = "dataNo2";
	/**对应数据源 jdbcExtendDataNo3*/
	public static String PCL_BMS_FILE = "dataNo3";
	/**对应数据源 jdbcExtendDataNo4*/
	public static String SCL_FILE = "dataNo4";
	/**对应数据源 jdbcExtendDataNo5*/
	public static String FDJ_MIDDLETABLE_FILE = "dataNo5";
	/**对应数据源 jdbcExtendDataNo6*/
	public static String PCL_FILE = "dataNo6";
	
	
	/**JOB邮件*/
	public static final String EDI_JOB_EMAIL = "EDI_JOB_EMAIL";
	/**web 接口*/
	public static final String EDI_JOB_WEB = "EDI_JOB_WEB";
	/**更新索引*/
	public static final String EDI_JOB_INDEX_REBUILD = "EDI_JOB_INDEX_REBUILD";
}
