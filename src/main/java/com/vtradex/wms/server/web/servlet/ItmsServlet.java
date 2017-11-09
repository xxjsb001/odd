package com.vtradex.wms.server.web.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.vtradex.wms.server.esbUtils.MessageUtil;
import com.vtradex.wms.server.esbUtils.ParamsUtil;
import com.vtradex.wms.server.service.interfaceLog.EdiExecuteSql;
import com.vtradex.wms.server.service.interfaceLog.ItmsServletType;
import com.vtradex.wms.server.utils.ItmsFileUtils;
import com.vtradex.wms.server.utils.MyUtils;

/**接口 接收统一路径**/
public class ItmsServlet extends HttpServlet {

	/**
	 * http://192.168.10.92:8080/jac_itms_edi/itms_server?interfaceName=jacSql&file_Name=xxx&file_Sql=yyy
	 */
	private static final long serialVersionUID = 1L;
	protected static ApplicationContext ac;
	protected final String CHARACTER = "utf-8";
	protected EdiExecuteSql ediExecuteSql;
	protected final Log logger = LogFactory.getLog(getClass());
	
	
	
	static String ACTION = "test_name";
	static String APPLICATION = "app63925646350352384";//更换成我们提供的application
	static String SECRET_KEY = "Q7P910RY08";//更换成我们提供的签名秘钥
	
	public ItmsServlet() {
		// TODO Auto-generated constructor stub
	}
	public void init(ServletConfig sc) throws ServletException {
		super.init(sc);
		ac = WebApplicationContextUtils.getRequiredWebApplicationContext(sc.getServletContext());
		ediExecuteSql = (EdiExecuteSql) ac.getBean("ediExecuteSql");
	}
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException,ServletException  {
		doPost(req , res);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding(CHARACTER);
		
		final String interfaceType = request.getParameter("interfaceType");
		final String interfaceName = request.getParameter("interfaceName");
		
		final String application = request.getParameter("application");
		final String action = request.getParameter("api_code");
		System.out.println("application="+application+","+"api_code="+action);
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("odd", "Welcome to ODD_HUB");
		String responseContext = MyUtils.font("Welcome to ODD_HUB");
		JSONObject jsonObject = new JSONObject();
		jsonObject.putAll(dataMap);
		
		responseContext = jsonObject.toString();
		
		Map<String, Object> toSignparams = new HashMap<String, Object>();
		toSignparams.put("application", application);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = dateFormat.format(new Date());	
		toSignparams.put("server_time", date);
		
		Map<String, Object> dataParams = new HashMap<String, Object>();
		dataParams.put("bstnk", "VOM_ORDER_NO");//VOM订单号
		dataParams.put("posex", "NO_120");//VOM订单行项目号
		dataParams.put("type", "S");//消息类型
		dataParams.put("message", "成功");//信息
		toSignparams.put("data",ParamsUtil.convertObjectToStringParams(dataParams));
		
//		JSONObject jsonDataObject = new JSONObject();
//		jsonDataObject.putAll(dataParams);
//		toSignparams.put("data", jsonDataObject.toString());
		
		System.out.println("NIO.sign=="+request.getParameter("sign"));
		
		//ODD侧校验NIO给的是否一致,此时ODD侧制定接口权限信息
		/**
		 * 	WL-0000:成功
			WL-20001：签名验证失败
			WL-20010：服务器超时
			WL-20009：系统内部错误
			WL-20002：业务数据验证失败   (具体见data数据体下:message)
		 */
		if(!APPLICATION.equals(application)){
			//成功消息接口:
			toSignparams.put("result_code", "WL-20001");
			toSignparams.put("result_msg", "签名验证失败");
		}else{
			//成功消息接口:
			toSignparams.put("result_code", "WL-0000");
			toSignparams.put("result_msg", "success");
		}
		
		
		//返回值根据NIO侧规范生成校验码
		//ODD侧生成的sign=application=xxx&data=[]&result_code=WL-0000&result_msg=success&server_time=2017-10-29 21:15:13Q7P910RY08
		String sign = null;
		try {
			sign = MessageUtil.sign(toSignparams,SECRET_KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		toSignparams.put("sign", sign);
		System.out.println(toSignparams.get("server_time"));
		JSONObject jsonParamObject = new JSONObject();
		jsonParamObject.putAll(toSignparams);
		responseContext = jsonParamObject.toString();
		responseContext = ItmsFileUtils.toUnicodeString(responseContext);
		
		//http://xxx:80/odd/odd_server?interfaceType=sqlQuery&fileName=xxx&airm=insert&fileSql={"key1":"value01","key2":"value02","key3":"value03"}
		if(interfaceType!=null){
			if(ItmsServletType.SQL_QUERY.equals(interfaceType)){
				final String fileName = request.getParameter("fileName");
				final String fileSql = request.getParameter("fileSql");
				final String airm = request.getParameter("airm");
				if(fileName==null || fileSql==null || airm==null){
					responseContext = "URL IS ERROR";
					logger.error(responseContext+"-----"+ new Date()); 
				}else{
					responseContext = ediExecuteSql.getSqlParame(new Object[]{
							airm,fileName,fileSql
					});
//					responseContext = ediExecuteSql.getSqlParame(fileName,fileSql);
				}
			}else if(ItmsServletType.JAC_SQL.equals(interfaceName)){
				//http://220.178.49.203:8978/jac_itms_edi/itms_server?interfaceName=jacSql&file_Name=cycbj_onway&file_Sql=INSERT INTO cycbj_onway (scode,gpscode,onway_time,onway_position,flag) VALUES ('1','70000694',to_date('2016-12-20 20:47:00','yyyy-mm-dd hh24:mi:ss'),'安徽省合肥市蜀山区 距青龙潭路/汤口路(路口)约283米 安徽省合肥市蜀山区合安路68号旁约1米处',0)
				final String fileName = request.getParameter("file_Name");
				final String fileSql = request.getParameter("file_Sql");
				
				responseContext = ediExecuteSql.getSqlParame(fileName,fileSql);
			}else if(ItmsServletType.ACTION_QUERY.equals(interfaceType)){
				//http://192.168.10.92:8080/odd_server/odd_server?interfaceType=actionQuery&action=test_name&airm=insert&data={"driver_name":"\u53f8\u673a\u59d3\u540d","dealer_code":"\u7ecf\u9500\u5546\u7f16\u7801","order_no":"\u8ba2\u5355\u53f7"}
				final String fileName = request.getParameter("action");
				final String fileSql = request.getParameter("data");
				final String airm = request.getParameter("airm");
				if(fileName==null || fileSql==null || airm==null){
					responseContext = "{\"code\":202,\"message\":\"URL IS ERROR！\"}";
					logger.error(responseContext+"-----"+ new Date()); 
				}else{
					responseContext = ediExecuteSql.getSqlParame(new Object[]{
							airm,fileName,fileSql
					});
//					responseContext = ediExecuteSql.getSqlParame(fileName,fileSql);
				}
			}
		}
		else{
			logger.error(responseContext+"-----"+ new Date()); 
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
