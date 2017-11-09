package com.vtradex.wms.server.service.interfaceLog;

import java.lang.reflect.InvocationTargetException;

public interface EdiExecuteSql {
	void executeTxtJob() throws InvocationTargetException;
	void executeTxt();
	/**处理外部接口文件*/
	void executeInterfaceFile();
	/**WEB接口任务生成*/
	void executeWeb();
	void executeSql(Long taskId);
	
	String getSqlParame(String airm,String fileName,String fileSql);
	
	String getSqlParame(Object[] objs);
	
	String getSqlParame(String fileName,String fileSql);
	
	void checkIpJob();
	
}
