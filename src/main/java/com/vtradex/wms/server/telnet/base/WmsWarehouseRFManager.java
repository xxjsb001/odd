package com.vtradex.wms.server.telnet.base;

import java.util.List;

import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.warehouse.ItmsTable;
import com.vtradex.wms.server.model.warehouse.ItmsWarehouse;
import com.vtradex.wms.server.model.warehouse.ItmsSid;
import com.vtradex.wms.server.model.warehouse.ItmsUsers;
import com.vtradex.wms.server.telnet.dto.WmsWorkAreaExtDTO;

/**
 * @author: 李炎
 */
public interface WmsWarehouseRFManager extends BaseManager {

	
	List<ItmsWarehouse> getWmsAvailableWarehousesByUserId();
	
	/**
	 * 获得仓库下工作区
	 */
	List<ItmsTable> getWorkAreaByDefaultWarehouse();
	
	ItmsUsers getWmsWorker(Long userId, Long warehouseId);
	
	ItmsSid getWmsWarehouseArea(Long warehouseAreaId);
	
	WmsWorkAreaExtDTO getWmsWorkAreaExt(ItmsTable workArea);
}
