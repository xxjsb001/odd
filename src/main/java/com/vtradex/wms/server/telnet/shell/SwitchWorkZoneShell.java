package com.vtradex.wms.server.telnet.shell;

import java.io.IOException;
import java.util.List;

import net.wimpi.telnetd.net.Connection;

import com.vtradex.kangaroo.component.support.PropertyOptionDisplayer;
import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.thorn.server.web.security.UserHolder;
import com.vtradex.wms.server.model.warehouse.ItmsWarehouse;
import com.vtradex.wms.server.model.warehouse.ItmsUsers;
import com.vtradex.wms.server.telnet.base.WmsWarehouseRFManager;
import com.vtradex.wms.server.web.filter.ItmsWarehouseHolder;
import com.vtradex.wms.server.web.filter.WmsWorkerHolder;

/**
 * @author: 李炎
 */
public class SwitchWorkZoneShell extends Thorn4BaseShell {

	protected WmsWarehouseRFManager wmsWarehouseRFManager;
	
	public SwitchWorkZoneShell(WmsWarehouseRFManager wmsWarehouseRFManager) {
		this.wmsWarehouseRFManager = wmsWarehouseRFManager;
	}

	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		
		List<ItmsWarehouse> warehouses = wmsWarehouseRFManager.getWmsAvailableWarehousesByUserId();
		
		// 绑定仓库
		ItmsWarehouse warehouse = (ItmsWarehouse)getListField("warehouse", warehouses, new PropertyOptionDisplayer("name"));
		ItmsWarehouseHolder.setWmsWarehouse(warehouse);
		
		// 绑定工作区
//		List<ItmsTable> workAreas = wmsWarehouseRFManager.getWorkAreaByDefaultWarehouse();
//		ItmsTable workArea = (ItmsTable)getListField("workArea", workAreas, new PropertyOptionDisplayer("code"));
//		WmsWorkAreaHolder.setWmsWorkArea(workArea);
		
		// 绑定工作人员
		ItmsUsers user = wmsWarehouseRFManager.getWmsWorker(UserHolder.getUser().getId(), ItmsWarehouseHolder.getWmsWarehouse().getId());
		WmsWorkerHolder.setWmsWorker(user);
		
		// 绑定工作区扩展信息
//		WmsWorkAreaExtDTO workAreaExt = wmsWarehouseRFManager.getWmsWorkAreaExt(workArea);
//		WmsWorkAreaExtHolder.setWmsWorkAreaExt(workAreaExt);
				
		forward(getDefaultNextShell());
	}
}
