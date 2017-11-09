package com.vtradex.wms.server.service.formatter;

import java.util.List;

import com.vtradex.thorn.server.format.Formatter;
import com.vtradex.wms.server.model.count.ItmpDeptNumber;

public class URLFormartter implements Formatter {

	public String format(String property, Object cellValue, List origenData,
			String referenceModel, String locale) {
		String synonymName = (String) origenData.get(1);
		String sortName = (String) origenData.get(3);
		String value = matches(synonymName,sortName);
//		User user = UserHolder.getUser();
		String s = null ;
		String logIn = null;
//				+"?login_name=" + user.getLoginName() + "&password=" + user.getPassword() + "&locale=" 
//				+ user.getLocale().getLanguage() + "&referenceModel=origen&userId="+user.getLoginName();
		
		String table = "<TABLE WIDTH='100%' STYLE='border:1px dotted #a3bae9'>" +
				"<TR>" +
				"<TD ALGIN='LEFT' WIDTH='100%' height='25' STYLE='border:1px dotted #a3bae9;white-space:nowrap;'><font color='blue' style='font-weight:bold;'>"+value+"</font></TD>" +
				"</TR>" +
				"</TABLE>";
		String div1 = "";
		String div2 = "</div>";
		div1 = "<div title = '"+value+"'> ";
		String result = div1 + table + div2;
		if(cellValue==null || cellValue.toString().length()<=10){
			logIn = "SSOError.html";
			s = "<a href="+logIn+" target='_blank'>"+result+"</a>";
		}else{
			logIn = cellValue.toString();
			s = "<a href="+logIn+" target='_blank'>"+result+"</a>";
		}
		return s;
	}

	public String exportFormat(String property, Object cellValue,
			List origenData, String referenceModel, String locale) {
		// TODO Auto-generated method stub
		return cellValue == null ? "" : cellValue.toString();
	}
	private String link = " ✚";
	public String matches(String synonymName,String sortName){
//		String value = synonymName+link;
//		if(ItmpDeptNumber.FDJ.equals(synonymName)){
//			value = "发动机-"+sortName+link;
//		}else if(ItmpDeptNumber.SCL.equals(synonymName)){
//			value = "商储-"+sortName+link;
//		}else if(ItmpDeptNumber.PCL.equals(synonymName)){
//			value = "乘储-"+sortName+link;
//		}else if(ItmpDeptNumber.IT_DEP.equals(synonymName)){
//			value = "技术中心-"+sortName+link;
//		}else if(ItmpDeptNumber.HT.equals(synonymName)){
//			value = "重卡-"+sortName+link;
//		}else if(ItmpDeptNumber.BJ.equals(synonymName)){
//			value = "备件-"+sortName+link;
//		}else if(ItmpDeptNumber.PROJECT.equals(synonymName)){
//			value = "项目组-"+sortName+link;
//		}else if(ItmpDeptNumber.OTHER.equals(synonymName)){
//			value = sortName+link;
//		}else{
//			value = "个人-"+sortName+link;
//		}
		return sortName+link;
	}
}
