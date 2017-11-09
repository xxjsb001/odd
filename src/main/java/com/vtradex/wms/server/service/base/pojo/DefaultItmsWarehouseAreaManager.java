package com.vtradex.wms.server.service.base.pojo;

import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.model.warehouse.ItmsSid;
import com.vtradex.wms.server.service.base.ItmsWarehouseAreaManager;
import com.vtradex.wms.server.web.filter.ItmsWarehouseHolder;

public class DefaultItmsWarehouseAreaManager extends DefaultBaseManager implements ItmsWarehouseAreaManager {
	
	protected WorkflowManager workflowManager;
	
	public DefaultItmsWarehouseAreaManager(WorkflowManager workflowManager) {
		this.workflowManager = workflowManager;
	}
	
	public void storeWarehouseArea(ItmsSid itmsSid) {
		itmsSid.setWarehouse(ItmsWarehouseHolder.getWmsWarehouse());
		commonDao.store(itmsSid);
	}

}