package com.vtradex.wms.server.service.formatter;

import java.util.List;

import com.vtradex.thorn.server.format.Formatter;

public class AlterValueFormatter implements Formatter{

	public String format(String property, Object cellValue, List origenData,
			String referenceModel, String locale) {
		String value = cellValue == null ? "" : cellValue.toString();
//		String end = StringUtils.substringAfterLast(value, ".");
		String table = "<TABLE WIDTH='100%' STYLE='border:1px dotted #a3bae9'>" +
				"<TR>" +
				"<TD ALGIN='LEFT' WIDTH='100%' height='25' STYLE='border:1px dotted #a3bae9;white-space:nowrap;'><font color='blue' style='font-weight:bold;'>"+value+"</font></TD>" +
				"</TR>" +
				"</TABLE>";
		String result = null;
		String div1 = "";
		String div2 = "</div>";
		div1 = "<div title = '"+value+"'> ";
		result = div1 + table + div2;
		return result;
	}

	public String exportFormat(String property, Object cellValue,
			List origenData, String referenceModel, String locale) {
		// TODO Auto-generated method stub
		return cellValue == null ? "" : cellValue.toString();
	}
	public String subStringHTML(String con) {
        String content = ""; 
        if(con!=null){
         content=con.replaceAll("</?[^>]+>","");//剔出了<html>的标签 
            content=content.replace("&nbsp;",""); 
            content=content.replace(".",""); 
            content=content.replace("\"","‘");
            content=content.replace("'","‘");
        }
        return content;
  }

}
