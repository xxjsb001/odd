package com.vtradex.wms.server.service.interfaceLog;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.model.base.ItmsLogType;
import com.vtradex.wms.server.model.itms.ItmsJobLog;
import com.vtradex.wms.server.model.itms.ItmsSqlTaskType;
import com.vtradex.wms.server.model.webservice.ItmsInterfaceColumn;
import com.vtradex.wms.server.model.webservice.ItmsInterfaceTable;
import com.vtradex.wms.server.model.webservice.ItmsWebArgument;
import com.vtradex.wms.server.model.webservice.ItmsWebColumn;
import com.vtradex.wms.server.model.webservice.ItmsWebUrl;
import com.vtradex.wms.server.service.email.EmailManager;
import com.vtradex.wms.server.service.reflect.ReflectMethodManager;
import com.vtradex.wms.server.utils.ItmsFileUtils;
import com.vtradex.wms.server.utils.JavaTools;
import com.vtradex.wms.server.utils.JsonTools;

public class WebExecuteDataImp extends DefaultBaseManager implements WebExecuteData {
	
	public static final String BEAN = "webExecuteData";
	public static final String SEND_JSON = "sendJson";
	
	private JdbcTemplate jdbcDataSource;
	private ReflectMethodManager reflectMethodManager;
	
	protected ItmsLogManager itmsLogManager;
	public WebExecuteDataImp(ItmsLogManager itmsLogManager,ReflectMethodManager reflectMethodManager) {
		this.itmsLogManager = itmsLogManager;
		this.reflectMethodManager = reflectMethodManager;
	}
	public final static String CHARSET = "utf-8";
	//生产库环境
	protected static String GPS_INTERFACE_URL = "http://g7s.api.huoyunren.com/interface/index.php";
	protected static String app_key  = "jacnew_api";
	protected static String app_secret = "fac8fd6d46bb1167a43c88df45ecbedf";
	//测试库环境
	protected static String GPS_TEST_URL = "http://test.api.g7s.chinawayltd.com/interface/index.php";
	protected static String app_key_test  = "jacapi";
	protected static String app_secret_test = "ed1a33da62224d8bd8f9411127f19ab1";
	
	
	protected static String MAIN_DATA = "ctr.addtaskdata.addData";
	protected String EXCEPTION_DEAL = "truck.alarm.oper_alarm";
	public static void testInvokeG7(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("jacyl_orderscheduling", "调度单号");
		map.put("jacyl_carnum", "车牌号");
		map.put("jacyl_begintime", "开始时间");
		map.put("jacyl_startorgcode", "出发城市编码");
		map.put("jacyl_dealercode", "经销商编码");
		map.put("jacyl_drivername", "司机姓名");
		map.put("jacyl_devicenum", "设备号");
		map.put("jacyl_carriercode", "承运商编码");
		String data = JsonTools.getCreateJson("jqwllbj1", map);
		System.out.println(data);
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.putAll(map);
		data = jsonObject.toString();
		System.out.println(data);
		
		data = ItmsFileUtils.toUnicodeString(data);
		String timestamp = JavaTools.format(new Date(), JavaTools.dmy_hms);
		String sign = ItmsFileUtils.createSignKey(app_key,app_secret,data,MAIN_DATA,timestamp);
		
		String sendData = requestData(app_key,MAIN_DATA,timestamp,sign,data);
		System.out.println(sendData);
		
		String response = null;
		try {
			//{"code":0,"data":{"message":"数据格式不正确","status":101}}
			//{"code":403,"message":"Exception:  app_key在系统中不存在或未分配机构,app_key:jacnew_api"}
			response = ItmsFileUtils.decodeUnicode(requestGps(GPS_INTERFACE_URL,sendData,CHARSET,CHARSET));
			System.out.println(response);
			
			JsonTools.getJsonKey(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static String requestData(String appKey,String method,String timestamp,String sign,String data) {
		StringBuffer urlBuf = new StringBuffer();
		urlBuf.append("app_key=");
		urlBuf.append(appKey);
		urlBuf.append("&method=");
		urlBuf.append(method);
		urlBuf.append("&data=");
		urlBuf.append(data);
		urlBuf.append("&timestamp=");
		// PHP处理需要将空格进行转换
		urlBuf.append(timestamp);
		urlBuf.append("&sign=");
		urlBuf.append(sign);
		return urlBuf.toString();
	}
	/** 发送一个REQUERT请求 */
	private static String requestGps(String webUrl,String sendData,String reqCharset,String respCharset) throws Exception {
        DataOutputStream wr = null;
        DataInputStream rd = null;
        HttpURLConnection conn = null;
        try{
            URL url = new URL(webUrl);
            //System.out.println("url:"+url+"?"+sendData);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(100000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Charset", reqCharset);
            conn.setRequestMethod("POST");
            conn.connect();
            
            wr = new DataOutputStream(conn.getOutputStream());
            
            wr.write(sendData.getBytes(reqCharset));
            wr.flush();
            wr.close();
            
            DataInputStream dis = new DataInputStream(conn.getInputStream());
			byte[] aryZlib = ItmsFileUtils.streamToByteArray(dis);
			if (dis != null) {
				dis.close();
				dis = null;
			}
            return new String(aryZlib, respCharset);
        } catch(ConnectException e) {
        	return "{\"code\":404,\"message\":\"网络连接超时！\"}";
        } finally {
            if (rd != null) {
                rd.close();
                rd = null;
            }
            if (wr != null) {
                wr.close();
                wr = null;
            }
            if (conn != null) {
            	conn.disconnect();
                conn = null;
            }
        }
    }
	
	public void storeItmsWebArgument(ItmsWebColumn columns,Long id){
		ItmsWebArgument methods = commonDao.load(ItmsWebArgument.class, id);
		columns.setMethods(methods);
		commonDao.store(columns);
	}
	public void storeItmsInterfaceTable(ItmsInterfaceColumn columns,Long id){
		ItmsInterfaceTable interfaceTable = commonDao.load(ItmsInterfaceTable.class, id);
		columns.setInterfaceTable(interfaceTable);
		commonDao.store(columns);
	}
	//itmstask 调用
	public void sendJson(Long id){
		ItmsWebArgument methods = commonDao.load(ItmsWebArgument.class, id);
		if(methods!=null){
			testMethod(methods);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void testMethod(ItmsWebArgument methods){
//		if(methods.getRepeatTimes()==null || methods.getRepeatTimes()<=0 || methods.getStatus().equals(BaseStatus.DISABLED)){
//			return;
//		}
		List<ItmsWebColumn> columns = commonDao.findByQuery("FROM ItmsWebColumn c WHERE c.methods.id =:id",
				"id", methods.getId());
		if(columns!=null && columns.size()>0){
			String tableName = methods.getExtTableName();
			String type = methods.getDataSource();//type匹配数据源
			String sql = "SELECT * FROM"+
				"("+
				"SELECT A.*, ROWNUM RN "+
				"FROM (SELECT * FROM "+tableName+") A "+
				"WHERE STATUS = 1 AND ROWNUM <= 100"+
				") "+
				"WHERE RN >= 1";
			List l = null;
			Boolean isSuccess = true,isError = false;;
			String response = null;
			String errorMes = "";
			
			List<Long> successIds = new ArrayList<Long>();
			Long nowId = 0L;
			try {
//				type = "dataNo1QueryForList";//test
				Object obj = reflectMethodManager.invokeMethod(JdbcExtendDataExtImp.BEAN+"."+type+JdbcExtendDataExtImp.QFL,sql);
				l = obj==null?new ArrayList():(List) obj;
				
				Iterator iterator = l.iterator();
				while (iterator.hasNext()) {
					Map m = (Map) iterator.next();
					nowId = Long.parseLong(m.get("ID").toString());
					Map<String, Object> map = new HashMap<String, Object>();
					for(ItmsWebColumn c : columns){
						map.put(c.getJsonC(),m.get(c.getJsonC()));
					}
					String data = null;
					if(StringUtils.isEmpty(methods.getJsonKey())){
						data = JsonTools.getCreateJson(map);
					}else{
						data = JsonTools.getCreateJson(methods.getJsonKey(), map);
					}
//					System.out.println(data);
					errorMes = data;
					
					ItmsWebUrl url = commonDao.load(ItmsWebUrl.class, methods.getUrl().getId());
					data = ItmsFileUtils.toUnicodeString(data);
					String timestamp = JavaTools.format(new Date(), JavaTools.dmy_hms);
					String sign = ItmsFileUtils.createSignKey(url.getAppKey(),url.getAppSecret(),data,methods.getMethod(),timestamp);
					
					String sendData = requestData(url.getAppKey(),methods.getMethod(),timestamp,sign,data);
//					System.out.println(sendData);
					
					response = ItmsFileUtils.decodeUnicode(requestGps(url.getUrl(),sendData,CHARSET,CHARSET));
//					System.out.println(response);
					isSuccess = JsonTools.getJsonKey(response);
					
					successIds.add(nowId);
					itmsLogManager.saveItmsJobLog(isSuccess?ItmsLogType.SUCCESS:ItmsLogType.ERROR, 
							tableName,response,errorMes);
				}
			}
			catch (Exception e) {
				isError = true;
				errorMes = e.getMessage();
			}finally{
				if(isError){
					sql = "UPDATE "+tableName+" g SET g.status = 0 WHERE g.id = "+nowId;
					dealExtUp(type, sql, tableName);
					ItmsJobLog log = itmsLogManager.saveItmsJobLog(ItmsLogType.ERROR, tableName, 
							errorMes,errorMes);
					//发邮件task 
					itmsLogManager.saveItmsTask(ItmsSqlTaskType.EDI_JOB_EMAIL,EmailManager.BEAN+EmailManager.SEND_JOB_EXCEPTION, 
							log.getId(),tableName);
				}
				if(successIds!=null && successIds.size()>0){
					sql = "UPDATE "+tableName+" g SET g.status = 3 WHERE g.id in("+
							StringUtils.substringBetween(successIds.toString(), "[", "]")+")";
					dealExtUp(type, sql, tableName);
					successIds.clear();
				}
			}
		}
	}
	private void dealExtUp(String type,String sql,String tableName){
		try {
			reflectMethodManager.invokeMethod(JdbcExtendDataExtImp.BEAN+"."+type+JdbcExtendDataExtImp.EXT,sql);
		} catch (Exception e2) {
			ItmsJobLog log = itmsLogManager.saveItmsJobLog(ItmsLogType.ERROR, tableName, 
					e2.getMessage(),e2.getMessage());
			//发邮件task 
			itmsLogManager.saveItmsTask(ItmsSqlTaskType.EDI_JOB_EMAIL,EmailManager.BEAN+EmailManager.SEND_JOB_EXCEPTION, 
					log.getId(),tableName);
		}
	}
	public static void main(String[] args) {
		WebExecuteDataImp.testInvokeG7();
	}
	public JdbcTemplate getJdbcDataSource() {
		return jdbcDataSource;
	}
	public void setJdbcDataSource(JdbcTemplate jdbcDataSource) {
		this.jdbcDataSource = jdbcDataSource;
	}
}
