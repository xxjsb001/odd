package com.vtradex.wms.server.utils;

public class ItmsUserTabs {

	/***系统默认账号 sysUsers.txt*/
	public static String SYS_USERS = "'SCOTT','OWBSYS','APEX_030200','APEX_PUBLIC_USER','FLOWS_FILES,MGMT_VIEW',"+
					"'SYSMAN','SPATIAL_CSW_ADMIN_USR','SPATIAL_WFS_ADMIN_USR','MDDATA',"+
					"'OWBSYS_AUDIT','MDSYS','SI_INFORMTN_SCHEMA','ORDPLUGINS',"+
					"'ORDDATA','ORDSYS','OLAPSYS','ANONYMOUS',"+
					"'XDB','CTXSYS','EXFSYS','XS$NULL',"+
					"'WMSYS','APPQOSSYS','DBSNMP','ORACLE_OCM',"+
					"'DIP','OUTLN','SYSTEM','SYS'";
	/**系统默认空间*/
	public static String SYS_TABLESPACE = "'SYSAUX','SYSTEM','USERS','UNDOTBS1'";
	
	public static final String DBA_USERS = "DBA_USERS";
	public static final String V$TABLESPACE = "v$tablespace";
	public static final String V$DATAFILE = "v$datafile";
	public static final String DBA_DATA_FILES = "dba_data_files";
	public static final String DBA_FREE_SPACE = "dba_free_space";
	
	public static final String ALL_TABLES = "ALL_TABLES";
	public static final String ALL_SYNONYMS = "SYS.ALL_SYNONYMS";
	public static final String USER_LOG_TABLE = "USER_LOG_TABLE";
	
	public static final String ALL_VIEWS = "all_views";
	
	public static final String ALL_USERS = "all_users";
	
	public static final String DBA_DB_LINKS = "dba_db_links";
	
}
