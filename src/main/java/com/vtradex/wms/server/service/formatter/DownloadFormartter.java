package com.vtradex.wms.server.service.formatter;

import java.util.List;
import com.vtradex.thorn.server.format.Formatter;
public class DownloadFormartter implements Formatter{

	public String format(String property, Object cellValue, List origenData,
			String referenceModel, String locale) {
		Long id = (Long) origenData.get(0);
		//"192.168.0.116:8086/jac_itms/";//localhost:8086/jac_itms/
//		String value = GlobalParamUtils.getGloableStringValue("IP_ADDRESS");
		String s = null ;
//		s = "<a href=\"http://"+value+"*.downloadFile?id="+id+"\" target='_blank'>下载文件</a>";
		String icon = "<img src=\"images/icon_down_over.gif\"/>";
		s = "<a href=*.downloadFile?id="+id+"&type="+cellValue+" target='_blank'>"+icon+"</a>";
		return s;
	}

	public String exportFormat(String property, Object cellValue,
			List origenData, String referenceModel, String locale) {
		// TODO Auto-generated method stub
		return cellValue == null ? "" : cellValue.toString();
	}
}
