package com.vtradex.wms.server.model.itms;

import com.vtradex.thorn.server.model.VersionalEntity;
import com.vtradex.wms.server.model.warehouse.ItmsSid;
import com.vtradex.wms.server.model.warehouse.ItmsWarehouse;
public class ItmsSynonyms extends VersionalEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ItmsWarehouse warehouse;
	private ItmsSid itmsSid;
	private String owner;
	private String synonymName;
	private String tableOwner;
	private String tableName;

	public ItmsSynonyms() {
		super();
	}
	public ItmsSynonyms(ItmsWarehouse warehouse, ItmsSid itmsSid, String owner,
			String synonymName, String tableOwner, String tableName) {
		super();
		this.warehouse = warehouse;
		this.itmsSid = itmsSid;
		this.owner = owner;
		this.synonymName = synonymName;
		this.tableOwner = tableOwner;
		this.tableName = tableName;
	}



	public ItmsSid getItmsSid() {
		return itmsSid;
	}

	public void setItmsSid(ItmsSid itmsSid) {
		this.itmsSid = itmsSid;
	}

	public ItmsWarehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(ItmsWarehouse warehouse) {
		this.warehouse = warehouse;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getSynonymName() {
		return synonymName;
	}

	public void setSynonymName(String synonymName) {
		this.synonymName = synonymName;
	}

	public String getTableOwner() {
		return tableOwner;
	}

	public void setTableOwner(String tableOwner) {
		this.tableOwner = tableOwner;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	

}
