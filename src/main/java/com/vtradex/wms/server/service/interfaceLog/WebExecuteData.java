package com.vtradex.wms.server.service.interfaceLog;

import com.vtradex.wms.server.model.webservice.ItmsInterfaceColumn;
import com.vtradex.wms.server.model.webservice.ItmsWebArgument;
import com.vtradex.wms.server.model.webservice.ItmsWebColumn;

public interface WebExecuteData {
	
	void storeItmsWebArgument(ItmsWebColumn columns,Long id);
	
	void storeItmsInterfaceTable(ItmsInterfaceColumn columns,Long id);
	
	void sendJson(Long id);
	void testMethod(ItmsWebArgument methods);
}
