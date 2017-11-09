package com.vtradex.wms.server.service.bean;


import com.vtradex.thorn.server.service.bean.base.BaseParamBean;
import com.vtradex.wms.server.model.warehouse.ItmsWarehouse;
import com.vtradex.wms.server.web.filter.ItmsWarehouseHolder;

public class WmsWarehouseSession extends BaseParamBean{

	public ItmsWarehouse getServerGlobalParamValue() {
		return ItmsWarehouseHolder.getWmsWarehouse();
	}

	public Long getClientGlobalParamValue() {
		return ItmsWarehouseHolder.getWmsWarehouse().getId();
	}
}
