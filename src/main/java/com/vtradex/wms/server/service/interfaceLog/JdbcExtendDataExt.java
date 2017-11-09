package com.vtradex.wms.server.service.interfaceLog;

import java.util.List;

/**jdbc扩展数据源执行接口类*/
public interface JdbcExtendDataExt {
	
	List dataNo0QueryForList(String sql);
	List dataNo1QueryForList(String sql);
	List dataNo2QueryForList(String sql);
	List dataNo3QueryForList(String sql);
	List dataNo4QueryForList(String sql);
	List dataNo5QueryForList(String sql);
	List dataNo6QueryForList(String sql);
	
	void dataNo0Execute(String sql);
	void dataNo1Execute(String sql);
	void dataNo2Execute(String sql);
	void dataNo3Execute(String sql);
	void dataNo4Execute(String sql);
	void dataNo5Execute(String sql);
	void dataNo6Execute(String sql);
}
