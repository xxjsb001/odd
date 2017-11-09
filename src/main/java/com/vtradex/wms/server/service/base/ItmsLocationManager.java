package com.vtradex.wms.server.service.base;


import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.itms.ItmsDbLinks;
import com.vtradex.wms.server.model.itms.ItmsSynonyms;
import com.vtradex.wms.server.model.itms.ItmsUserOnOff;
import com.vtradex.wms.server.model.warehouse.ItmsTable;
import com.vtradex.wms.server.model.warehouse.ItmsTablespaces;

public interface ItmsLocationManager extends BaseManager {
	
	/**表信息初始化*/
	@Transactional
	void updateWorkArea(Long userId,String description);
	/**批量更新账号用户信息*/
	@Transactional
	void updateWorker(Long locationId,String description,String type);
	/**表空间批量更新*/
	@Transactional
	void updateWmsLocation(Long warehouseAreaId,String description);
	@Transactional
	void storeLocation(ItmsTablespaces itmsTablespaces);
	@Transactional
	String getSessionHomeCode(Map map);
	/**扩充数据文件*/
	@Transactional
	void addDbf(ItmsTablespaces itmsTablespaces);
	
	/**定义权限表*/
	@Transactional
	void synonymName(ItmsTable table);
	/**手工定义权限表*/
	@Transactional
	void synonymHand(ItmsTable table);
	/**更新权限表*/
	@Transactional
	void initSynonyms(Long sidId);
	/**删除权限表*/
	@Transactional
	void dropSynonyms(ItmsSynonyms syno);
	
	
	/**删除表*/
	@Transactional
	void deleteTable(ItmsTable table);
	/**创建db_link*/
	@Transactional
	void newDbLink(ItmsDbLinks link,String isPrivate,Long ownerId);
	/**删除db_link*/
	@Transactional
	void deleteDblink(ItmsDbLinks link);
	/**初始化db_link*/
	@Transactional
	void initDblink(Long sidId,String description);
	
	/**初始化用户出入日志*/
	void initUserOnOff(Long sidId);
	/**删除用户出入日志*/
	@Transactional
	void deleteUserOnOff(List<ItmsUserOnOff> ufos);
}