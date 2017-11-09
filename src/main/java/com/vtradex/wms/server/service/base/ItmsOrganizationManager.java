package com.vtradex.wms.server.service.base;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.middle.ItmsAuthority;
import com.vtradex.wms.server.model.warehouse.ItmsIonizationTables;
import com.vtradex.wms.server.model.warehouse.ItmsTable;
import com.vtradex.wms.server.model.warehouse.ItmsUsers;


public interface ItmsOrganizationManager extends BaseManager {
	/**
	 * 账号保存
	 **/
	@Transactional
	void saveUsers(ItmsUsers user);
	/**
	 * 账号权限-增
	 **/
	@Transactional
	void insertRuleUpdate(Long workId,ItmsTable table);
	/**
	 * 账号权限-删
	 **/
	@Transactional
	void deleteRuleUpdate(Long workId,ItmsTable table);
	/**
	 * 账号权限-改
	 **/
	@Transactional
	void updateRuleUpdate(Long workId,ItmsTable table);
	/**
	 * 账号权限-查
	 **/
	@Transactional
	void selectRuleUpdate(Long workId,ItmsTable table);
	/**
	 * 账号权限增加
	 **/
	@Transactional
	void addWmsWorkerRule(Long workId,ItmsAuthority mce);
	/**
	 * 账号权限删除
	 **/
	@Transactional
	void removeWmsWorkerRule(ItmsAuthority mce);
	/**
	 * 账号权限生效
	 **/
	@Transactional
	void enableWorkUser(ItmsUsers wk);
	/**
	 * 账号权限收回
	 **/
	@Transactional
	void deleteMiddleCompanyExtends(ItmsAuthority mce);
	/**
	 * 简单的删除无用的账号以及对应表
	 **/
	@Transactional
	void deleteWorkUser(ItmsUsers wk);
	/**初始化游离表*/
	@Transactional
	Boolean ionizationTable(Map map);
	/**纠正游离表*/
	@Transactional
	void correctTable(ItmsUsers user);
	/**更新索引*/
	void alterIndexRebuild(Long id);
	
}
