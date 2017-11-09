package com.vtradex.wms.server.service.interval;

import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.itms.IntervalLog;
import com.vtradex.wms.server.model.itms.IntervalTimes;

public interface IntervalManager extends BaseManager{
	/**新建*/
	void storeInterval(IntervalLog iLog);
	/**选择截止时间点*/
	void confirmBySerialNo(IntervalTimes iTimes);
	
	void deleteInterval(IntervalLog iLog);
	/**生效*/
	void activeInterval(IntervalLog iLog);
	/**完成*/
	void finishInterval(IntervalLog iLog);
}
