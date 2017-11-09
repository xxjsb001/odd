package com.vtradex.wms.server.service.reflect;

import org.springframework.transaction.annotation.Transactional;

public interface ReflectMethodManager {
	/**
	 * 人工执行TASK方法
	 */
	void itmsTaskMethod(String taskIds) throws Exception;
	/**
	 * 接口方法调用
	 */
	void invokeMethod(String subscriber) throws Exception;
	
	Object invokeMethod(String subscriber,String params);
}
