package com.vtradex.wms.server.web.filter;

import com.vtradex.thorn.server.security.acegi.holder.SecurityContextHolder;
import com.vtradex.wms.server.model.warehouse.ItmsWarehouse;
import com.vtradex.wms.server.web.servlet.WMSLoginServlet;

public class ItmsWarehouseHolder {
	
	private static ThreadLocal<ItmsWarehouse> wmsWarehouses = new InheritableThreadLocal<ItmsWarehouse>();

	public static ItmsWarehouse getWmsWarehouse() {
		if (SecurityContextHolder.getCurrentSession() == null) {
			return wmsWarehouses.get();
		}
		return (ItmsWarehouse) SecurityContextHolder.getCurrentSession().getAttribute(WMSLoginServlet.WMS_SESSION_WAREHOUSE);
	}

	public static void setWmsWarehouse(ItmsWarehouse itmsWarehouse) {
		wmsWarehouses.set(itmsWarehouse);
	}

}
