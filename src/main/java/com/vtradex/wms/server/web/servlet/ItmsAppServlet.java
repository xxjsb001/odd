package com.vtradex.wms.server.web.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.vtradex.wms.server.service.interfaceLog.AppExecuteData;
import com.vtradex.wms.server.utils.MyUtils;

public class ItmsAppServlet extends HttpServlet{

	/**
	 * http://192.168.10.92:8080/jac_itms_edi/itms_server?interfaceName=jacSql&file_Name=xxx&file_Sql=yyy
	 */
	private static final long serialVersionUID = 1L;
	protected static ApplicationContext ac;
	protected final String CHARACTER = "utf-8";
	protected AppExecuteData appExecuteData;
	public ItmsAppServlet() {
		// TODO Auto-generated constructor stub
	}
	public void init(ServletConfig sc) throws ServletException {
		super.init(sc);
		ac = WebApplicationContextUtils.getRequiredWebApplicationContext(sc.getServletContext());
		appExecuteData = (AppExecuteData) ac.getBean("appExecuteData");
	}
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException,ServletException  {
		doPost(req , res);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding(CHARACTER);
		
		final String interfaceName = request.getParameter("interfaceName");
		String responseContext = "Not defined interfaceName="+interfaceName;
		if("appJson".equals(interfaceName)){
			//http://192.168.1.115:8086/jac_itms/itms_server?interfaceName=appJson
			String callback = request.getParameter("callback");
			
			String filter = request.getParameter("filter");//[{"property":"book_name","value":"ss"}]
			if(filter!=null){
				filter = StringUtils.substringBetween(filter, "[", "]");
				try {
					JSONObject jsonObject = new JSONObject(filter);
					String property = jsonObject.getString("property");//通过键值名获取字符串
					String value = jsonObject.getString("value");//通过键值名获取字符串
					System.out.println(property+"=="+value);
					
					filter = value;
					
					if("ALL".equals(value)){
						filter = MyUtils.fc;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println(filter);
				
			}else{
				filter = MyUtils.fc;
			}
			if(StringUtils.isEmpty(callback)){
				responseContext = appExecuteData.appJson(filter);
			}else{
				responseContext = callback+"("+appExecuteData.appJson(filter)+")";
			}
			response.setHeader("Access-Control-Allow-Origin", "*");
		    response.setHeader("Access-Control-Allow-Headers","X-Requested-With");
		}else if("stPost".equals(interfaceName)){
//			http://localhost:8086/odd/app_server?interfaceName=stPost&page=1&start=0&limit=7&callback=Ext.data.JsonP.callback1
			String callback = request.getParameter("callback");
			int limit=Integer.valueOf(request.getParameter("limit")==null?"1":request.getParameter("limit"));
			int page=Integer.valueOf(request.getParameter("page")==null?"1":request.getParameter("page"));
			int start=Integer.valueOf(request.getParameter("start")==null?"1":request.getParameter("start"));
			if(StringUtils.isEmpty(callback)){
				responseContext = appExecuteData.List(limit,page,start);
			}else{
				responseContext = callback+"("+appExecuteData.List(limit,page,start)+")";
			}
			response.setHeader("Access-Control-Allow-Origin", "*");
		    response.setHeader("Access-Control-Allow-Headers","X-Requested-With");
		    response.setContentType("text/html;charset=utf-8");
		}else if("click".equals(interfaceName)){
//			http://localhost:8086/jac_itms/itms_server?interfaceName=click&page=1&start=0&limit=7&callback=Ext.data.JsonP.callback1
			String callback = request.getParameter("callback");
			if(request.getParameter("id")!=null){
				int id=Integer.valueOf(request.getParameter("id"));
				responseContext = callback+"("+appExecuteData.click(id)+")";
			}else{
				responseContext = "参数id为空";
			}
			response.setHeader("Access-Control-Allow-Origin", "*");
		    response.setHeader("Access-Control-Allow-Headers","X-Requested-With");
		    response.setContentType("text/html;charset=utf-8");
		}
//		System.out.println(interfaceName+"\n"+responseContext);
		returnRequest(response,responseContext);
	}
	/** 响应请求 */
	protected void returnRequest(HttpServletResponse response,String returnRequest) throws IOException {
		response.setCharacterEncoding(CHARACTER);
		response.getWriter().write(returnRequest);
		response.getWriter().flush();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
