package com.vtradex.wms.server.web.filter;

import com.vtradex.wms.server.model.warehouse.ItmsTable;
/**
 * 
 *
 * @category 
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1 $Date: 2015/10/22 08:03:18 $
 */
public class WmsWorkAreaHolder {
	
	private static ThreadLocal<ItmsTable> wmsWorkAreas = new InheritableThreadLocal<ItmsTable>();
	
	public static ItmsTable getWmsWorkArea() {
		return wmsWorkAreas.get();
	}

	public static void setWmsWorkArea(ItmsTable wmsWorkArea) {
		wmsWorkAreas.set(wmsWorkArea);
	}
}
