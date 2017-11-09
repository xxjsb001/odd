package com.vtradex.wms.server.web.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vtradex.thorn.server.dao.CommonDao;
import com.vtradex.thorn.server.exception.AuthenticationException;
import com.vtradex.thorn.server.exception.ThornServerRuntimeException;
import com.vtradex.thorn.server.model.security.User;
import com.vtradex.thorn.server.security.acegi.holder.SecurityContextHolder;
import com.vtradex.thorn.server.service.security.SecurityFilter;
import com.vtradex.thorn.server.servlet.LoginServlet;
import com.vtradex.thorn.server.util.Constant;
import com.vtradex.thorn.server.util.LocalizedMessage;
import com.vtradex.wms.server.model.warehouse.ItmsWarehouse;
import com.vtradex.wms.server.service.security.ItmsWarehouseManager;
import com.vtradex.wms.server.utils.IpUtils;

public class WMSLoginServlet extends LoginServlet {
	private static final long serialVersionUID = 7483681481829020964L;
	/** 当前服务*/
	public static final String WMS_SESSION_WAREHOUSE = "WMS_SESSION_WAREHOUSE"; //key=SESSION_WAREHOUSE
	public static final String SESSION_WAREHOUSE_NAME = "SESSION_WAREHOUSE_NAME";
	public static String SESSION_ORGANIZATION_CODE = "SESSION_ORGANIZATION_CODE";
	public static String SESSION_LOGIN_IP = "SESSION_LOGIN_IP";
	public static String SESSION_USER_NAME = "SESSION_USER_NAME";
	
	protected CommonDao commonDao;
	
	public void init(ServletConfig sc) throws ServletException {
		super.init(sc);
		commonDao = (CommonDao)ac.getBean("commonDao");
	}
	
	protected void doPost(final HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		String loginName = req.getParameter("login_name");
		String password = req.getParameter("password");
		final String referenceModel = req.getParameter("referenceModel");
		final String locale = req.getParameter("locale");
		
		loginName = decodeParams(loginName);
		password = decodeParams(password);
		User loginUser = null;
		try {
			loginUser = authenticateUser(loginName,password,referenceModel,locale);
			
		} catch (AuthenticationException e) {
//			e.printStackTrace();
			String hintMsg = LocalizedMessage.getLocalizedMessage(e.getClass().getSimpleName(),referenceModel,locale);
			res.getOutputStream().write(hintMsg.getBytes("UTF-8"));
			return;
		}
		
		SecurityFilter sf = buildSecurityFilter(loginUser,referenceModel,locale);
		sf.addHql("switchWareHousePage.loadWarehouseDefault", "FROM ItmsWarehouse wh WHERE 1=1 AND wh.status = 'ENABLED'");
		req.getSession().setAttribute(Constant.SECURITY_FILTER_IN_SESSION , sf);
		req.getSession().setAttribute(Constant.USER_IN_SESSION , loginUser);
		req.getSession().setAttribute(SESSION_USER_NAME, loginUser.getName());
		req.getSession().setAttribute(Constant.MODELTYPE , referenceModel);
		req.getSession().setAttribute(Constant.LOCALE , locale);
		req.getSession().setAttribute(Constant.MODULE , "wms");
		req.getSession().setAttribute(SESSION_ORGANIZATION_CODE, checkOrganizationCode(loginUser.getStrExtend1()));
		SecurityContextHolder.addSecurityContext(req.getSession(false));
		
		final ItmsWarehouse wh = queryWarehouse(loginName);
		req.getSession().setAttribute(WMS_SESSION_WAREHOUSE, wh);//当前的服务
		req.getSession().setAttribute("SESSION_WAREHOUSE_NAME", wh.getName());
		req.getSession().setAttribute(SESSION_LOGIN_IP, IpUtils.getIpAddress(req));
		
		/**
		 * 绑定属性:字符类型
		 * 无法时默太为NULL:Constant.NULL
		 */
		res.setContentType("text/html; charset=utf-8");
		res.getOutputStream().write("success".getBytes());
		
	}
	
	private ItmsWarehouse queryWarehouse(String loginName) {
		ItmsWarehouseManager itmsWarehouseManager = (ItmsWarehouseManager) ac.getBean("itmsWarehouseManager");
		ItmsWarehouse wh = itmsWarehouseManager.getDefaultLoginWmsWarehouse();
		if (wh == null) {
			throw new ThornServerRuntimeException("No warehouse avaliable for user [" + loginName + "].");
		}
		return wh ;
	}
	//yc.min
	private String checkOrganizationCode(String code){
		if(code==null || "".equals(code) || " ".equals(code)){
			code = "-1";
		}
		return code;
	}
}
