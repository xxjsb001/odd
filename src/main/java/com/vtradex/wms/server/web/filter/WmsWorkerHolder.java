package com.vtradex.wms.server.web.filter;

import com.vtradex.wms.server.model.warehouse.ItmsUsers;

/**
 * 
 * @category 
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1 $Date: 2015/10/22 08:03:18 $
 */
public class WmsWorkerHolder {
	
	private static ThreadLocal<ItmsUsers> wmsWorkerLocal = new InheritableThreadLocal<ItmsUsers>();

	public static ItmsUsers getWmsWorker() {
		return wmsWorkerLocal.get();
	}

	public static void setWmsWorker(ItmsUsers user) {
		wmsWorkerLocal.set(user);
	}
}
