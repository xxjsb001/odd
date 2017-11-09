package com.vtradex.wms.server.service.bean;

import com.vtradex.thorn.server.service.bean.ClientDisplayInfo;
import com.vtradex.thorn.server.util.LocalizedMessage;
import com.vtradex.thorn.server.web.security.UserHolder;
import com.vtradex.wms.server.web.filter.ItmsWarehouseHolder;

public class WarehouseInfoInClient implements ClientDisplayInfo {

	public String getAllDisplayInfo() {
		return LocalizedMessage.getLocalizedMessage("current.warehouse",
				UserHolder.getReferenceModel(),UserHolder.getLanguage())+
				ItmsWarehouseHolder.getWmsWarehouse().getName();
	}

}
