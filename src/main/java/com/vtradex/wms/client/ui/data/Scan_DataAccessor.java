package com.vtradex.wms.client.ui.data;

import java.util.HashMap;
import java.util.Map;

import com.vtradex.thorn.client.data.DataAccessor;
import com.vtradex.thorn.client.message.IMessagePage;
import com.vtradex.wms.client.businessObject.BusinessNode;
import com.vtradex.wms.client.filePage.EditInitServiceFile;
import com.vtradex.wms.client.filePage.GridGroupingSampleFiles;

public class Scan_DataAccessor extends DataAccessor{
	public Map result = new HashMap();
	public Scan_DataAccessor(IMessagePage page) {
		super(page);
	}
	public void getFileData(Map params) {
		this.remoteCall(GridGroupingSampleFiles.INFO, EditInitServiceFile.SAVE_MANAGER, GridGroupingSampleFiles.GET_FILE_DATA, params);
	}
	public void readSqL(Map params) {
		this.remoteCall(GridGroupingSampleFiles.INFO, EditInitServiceFile.SAVE_MANAGER, GridGroupingSampleFiles.READ_SQL, params);
	}
	public void onSuccess(String message, Map result) {
		if(GridGroupingSampleFiles.INFO.equals(message)){
			this.result = result;
			this.sendMessage(result.get(BusinessNode.MSG).toString());
		}
	}

}
