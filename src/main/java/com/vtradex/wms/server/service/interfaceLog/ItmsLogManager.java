package com.vtradex.wms.server.service.interfaceLog;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.wms.server.model.itms.ItmsJobLog;
import com.vtradex.wms.server.model.itms.ItmsTask;

public interface ItmsLogManager {
	/**String type, String subscriber, Long messageId*/
	@Transactional
	ItmsTask saveItmsTask(String[] obj);
	@Transactional
	ItmsJobLog saveItmsJobLog(String type,String operName,String operException,String operExceptionMess);
	
	ItmsTask saveItmsTask(String type,String subscriber,Long messageId,String objName);
	/**dirName:路径或路径+文件全名,objName:执行对象名,encoding:编码格式或其他备注*/
	ItmsTask saveItmsTask(String type,String method,String dirName,String objName,String encoding);
	
	
}
