package com.vtradex.wms.server.web.filter;

import com.vtradex.thorn.server.security.acegi.holder.SecurityContextHolder;
import com.vtradex.wms.server.web.servlet.WMSLoginServlet;

public class ItmsLogInIpHolder {

	public static String getLogInIp() {
		return (String) SecurityContextHolder.getCurrentSession().getAttribute(WMSLoginServlet.SESSION_LOGIN_IP);
	}

}
