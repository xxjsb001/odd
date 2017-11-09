package com.vtradex.wms.server.service.interfaceLog;

public interface AppExecuteData {
	String appJson(String filter);
	
	String List(int limit, int page,int start);
	String click(int id);
}
