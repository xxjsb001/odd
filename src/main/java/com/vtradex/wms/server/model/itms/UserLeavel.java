package com.vtradex.wms.server.model.itms;
/**账号级别*/
public interface UserLeavel {
	/** 普通账号*/
	public static String NORMAL = "normal";
	/** 运维管理员*/
	public static String OPERATION = "operation";
	/** DBA管理员*/
	public static String DBA = "dba";
}
