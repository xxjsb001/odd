package com.vtradex.wms.server.service.security;

import java.util.List;
import java.util.Map;

import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.warehouse.ItmsTablespaces;
import com.vtradex.wms.server.model.warehouse.ItmsWarehouse;

public interface ItmsWarehouseManager extends BaseManager {
	/** 自动得到登陆用户名 */
	ItmsWarehouse getDefaultLoginWmsWarehouse();
	
	Map getThisWarehouse(Map m);
	
	Map getWmsWareHousesForThorn(Map map);
	
	/**
	 * 跟据用户Id获得用户全部的仓库列表
	 * @param userId
	 * @return
	 */
	List<ItmsWarehouse> getWmsAvailableWarehousesByUserId();

	/**
	 * 获取指定仓库的盘点库位
	 * @param warehouse
	 * @return
	 */
	ItmsTablespaces getCountLocationByWarehouse(ItmsWarehouse warehouse);
	
	List<ItmsWarehouse> getWareHouse(Long userId);
	
}