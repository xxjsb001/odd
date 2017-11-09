package com.vtradex.wms.server.service.base;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.warehouse.ItmsSid;

public interface ItmsWarehouseAreaManager extends BaseManager {
	/**
	 * 
	 */
	@Transactional
	void storeWarehouseArea(ItmsSid itmsSid);
}