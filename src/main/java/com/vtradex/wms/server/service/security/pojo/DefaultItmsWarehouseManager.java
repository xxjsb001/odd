package com.vtradex.wms.server.service.security.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.vtradex.thorn.client.ui.table.RowData;
import com.vtradex.thorn.server.model.Entity;
import com.vtradex.thorn.server.security.acegi.holder.SecurityContextHolder;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.service.security.UserManager;
import com.vtradex.thorn.server.web.security.UserHolder;
import com.vtradex.wms.server.model.warehouse.ItmsTablespaces;
import com.vtradex.wms.server.model.warehouse.WmsLocationType;
import com.vtradex.wms.server.model.warehouse.ItmsWarehouse;
import com.vtradex.wms.server.service.security.ItmsWarehouseManager;
import com.vtradex.wms.server.web.filter.ItmsWarehouseHolder;

@SuppressWarnings("unchecked")
public class DefaultItmsWarehouseManager extends DefaultBaseManager implements ItmsWarehouseManager {
	protected  final UserManager userManager;
	
	public DefaultItmsWarehouseManager(UserManager userManager) {
		super();
		this.userManager = userManager;
	}
	
	@Override
	public <T extends Entity> List<T> load(Class<T> clazz, List<Long> ids) {
		return null;
	}
	public ItmsWarehouse getDefaultLoginWmsWarehouse() {
		return getWmsAvailableWarehousesByUserId().get(0);
	}
	
	public Map getThisWarehouse(Map m) {
		Map map = new HashMap();
		
	    map.put("1", ItmsWarehouseHolder.getWmsWarehouse().getName());
	    map.put("userId", UserHolder.getUser().getId());
	    map.put("userName", UserHolder.getUser().getName());
	    
		return map;
	}
	
	public Map getWmsWareHousesForThorn(Map map){
		Map result = new HashMap();
		
		List<ItmsWarehouse> warehouses = getWmsAvailableWarehousesByUserId();		
		List rowDatas = new ArrayList();
		
		for (ItmsWarehouse warehouse : warehouses) {
			RowData rowData = new RowData();
			
			rowData.addColumnValue(warehouse.getId());
			rowData.addColumnValue(warehouse.getName());
			
			rowDatas.add(rowData);
		}
		
		result.put("wareHouses.list", rowDatas);
		
		return result;
	}
	
	public List<ItmsWarehouse> getWmsAvailableWarehousesByUserId() {
		String hql = SecurityContextHolder.getSecurityFilterHql("switchWareHousePage.loadWarehouseDefault");
		
		if (StringUtils.isEmpty(hql)) {
			hql = "FROM ItmsWarehouse wh WHERE 1=1 AND wh.status = 'ENABLED'";
		}
		
		return commonDao.findByQuery(hql);
	}
	
	public ItmsTablespaces getCountLocationByWarehouse(ItmsWarehouse warehouse) {
		String hql = "FROM ItmsTablespaces l " +
				" WHERE l.type = :type " + 
				" AND l.warehouse.id=:warehouseId " +
				" AND l.status = 'ENABLED' " +
				" ORDER BY l.code";
		List<ItmsTablespaces> countLocations = commonDao.findByQuery(hql,
				new String[]{"type","warehouseId"},new Object[]{WmsLocationType.COUNT, 
				warehouse.getId()});
		if (countLocations == null || countLocations.size()<=0) {
			return null;
		}
		return countLocations.get(0);
	}

	public List<ItmsWarehouse> getWareHouse(Long userID) {
		
		return commonDao.findByQuery("FROM ItmsWarehouse wh WHERE 1=1 AND wh.status = 'ENABLED' " +
				"AND wh.id IN(select u.warehouse.id from ItmsUsers u where u.user.id=:userId)",
				new String []{"userId"},new Object[]{userID});
	}
}