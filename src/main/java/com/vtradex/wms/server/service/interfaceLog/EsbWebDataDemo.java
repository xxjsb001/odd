package com.vtradex.wms.server.service.interfaceLog;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.vtradex.wms.server.esbUtils.JsonHelper;
import com.vtradex.wms.server.esbUtils.ParamsUtil;
import com.vtradex.wms.server.esbUtils.requestUtil;
import com.vtradex.wms.server.utils.JsonTools;

public class EsbWebDataDemo {
	public static String ACTION_NCO = "sap_intsd_vehicle_outOfLibrary_v1";//新车堆场出库
	public static String ACTION_VP = "sap_intsd_buy_jacVehicle_v1";//对JAC的整车采购
	public static String ACTION_NR = "sap_backInvoke_vehicle_productionNode_v1";//整车生产节点回传
	
	private static String URL_TEST = "http://10.110.3.88:48083/api/v1/sap/httpService";//测试地址,根据实际提供地址修改
	static String APPLICATION = "app63925646350352384";//更换成我们提供的application
	static String SECRET_KEY = "Q7P910RY08";//更换成我们提供的签名秘钥
	 
	 
	public static void main(String[] args) throws UnsupportedEncodingException {
//		test_nco();
//		test_vp();
		test_nr();
	}
	/**新车堆场出库 接口调用*/
	public static void test_nco() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// ---系统参数
		paramMap.put("api_version", "1");
		paramMap.put("api_code", ACTION_NCO);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = dateFormat.format(new Date());	
		paramMap.put("request_time", date);
		paramMap.put("application", APPLICATION);
		paramMap.put("track_id", UUID.randomUUID().toString().replace("-", ""));
		paramMap.put("request_id", UUID.randomUUID().toString().replace("-", ""));
		// ---业务参数
		String infoBodyStr = JsonTools.getCreateJson("t_item", getListMap_Nco());
		paramMap.put("data", infoBodyStr);// Json类型参数
		// ---esb系统参数
		Map<String, Object> unsignedParams = new HashMap<String, Object>();
		unsignedParams.put("action", ACTION_NCO);
		try {
			paramMap = ParamsUtil.getParamMap(paramMap, unsignedParams, SECRET_KEY);
			String result = requestUtil.post(ParamsUtil.convertObjectToStringParams(paramMap),
					URL_TEST);
			System.out.println("result="+result);
			//{application=app63925646350352384, server_time=2017-10-26 14:12:20, data=[{"gernr":"vin_1","type":"E","message":"Enter Plant"},{"gernr":"vin_2","type":"E","message":"Enter Plant"}], result_code=WL-0001, result_msg=Enter Plant;Enter Plant, sign=2A48EA089210977A0BBB907F9CF57AEE}
			Map<String, Object> resultMap = JsonHelper.parseToMap(result);
			/**
			 * 	错误码：
				WL-0000: 成功
				WL-20001：签名验证失败
				WL-20010：服务器超时
				WL-20009：系统内部错误
			 */
			String result_code = (String) resultMap.get("result_code");
			String result_msg = (String) resultMap.get("result_msg");
			System.out.println(result_code+":"+result_msg);
//			System.out.println(MessageUtil.verifySign(resultMap, SECRET_KEY));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**对JAC的整车采购 接口调用*/
	public static void test_vp() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// ---系统参数
		paramMap.put("api_version", "1");
		paramMap.put("api_code", ACTION_VP);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = dateFormat.format(new Date());	
		paramMap.put("request_time", date);
		paramMap.put("application", APPLICATION);
		paramMap.put("track_id", UUID.randomUUID().toString().replace("-", ""));
		paramMap.put("request_id", UUID.randomUUID().toString().replace("-", ""));
		// ---业务参数
		String infoBodyStr = JsonTools.getCreateJson("t_item", getListMap_Nco());
		paramMap.put("data", infoBodyStr);// Json类型参数
		// ---esb系统参数
		Map<String, Object> unsignedParams = new HashMap<String, Object>();
		unsignedParams.put("action", ACTION_VP);
		try {
			paramMap = ParamsUtil.getParamMap(paramMap, unsignedParams, SECRET_KEY);
			String result = requestUtil.post(ParamsUtil.convertObjectToStringParams(paramMap),
					URL_TEST);
			System.out.println("result="+result);
			//{application=app63925646350352384, server_time=2017-10-26 14:12:20, data=[{"gernr":"vin_1","type":"E","message":"Enter Plant"},{"gernr":"vin_2","type":"E","message":"Enter Plant"}], result_code=WL-0001, result_msg=Enter Plant;Enter Plant, sign=2A48EA089210977A0BBB907F9CF57AEE}
			Map<String, Object> resultMap = JsonHelper.parseToMap(result);
			/**
			 * 	错误码：
				WL-0000: 成功
				WL-20001：签名验证失败
				WL-20010：服务器超时
				WL-20009：系统内部错误
			 */
			String result_code = (String) resultMap.get("result_code");
			String result_msg = (String) resultMap.get("result_msg");
			System.out.println(result_code+":"+result_msg);
//				System.out.println(MessageUtil.verifySign(resultMap, SECRET_KEY));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**整车生产节点回传*/
	public static void test_nr() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// ---系统参数
		paramMap.put("api_version", "1");
		paramMap.put("api_code", ACTION_NR);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = dateFormat.format(new Date());	
		paramMap.put("request_time", date);
		paramMap.put("application", APPLICATION);
		paramMap.put("track_id", UUID.randomUUID().toString().replace("-", ""));
		paramMap.put("request_id", UUID.randomUUID().toString().replace("-", ""));
		// ---业务参数
		String infoBodyStr = JsonTools.getCreateJson("t_item", getListMap_nr());
		paramMap.put("data", infoBodyStr);// Json类型参数
		// ---esb系统参数
		Map<String, Object> unsignedParams = new HashMap<String, Object>();
		unsignedParams.put("action", ACTION_NR);
		try {
			paramMap = ParamsUtil.getParamMap(paramMap, unsignedParams, SECRET_KEY);
			String result = requestUtil.post(ParamsUtil.convertObjectToStringParams(paramMap),
					URL_TEST);
			System.out.println("result="+result);
			//{application=app63925646350352384, server_time=2017-10-26 14:12:20, data=[{"gernr":"vin_1","type":"E","message":"Enter Plant"},{"gernr":"vin_2","type":"E","message":"Enter Plant"}], result_code=WL-0001, result_msg=Enter Plant;Enter Plant, sign=2A48EA089210977A0BBB907F9CF57AEE}
			Map<String, Object> resultMap = JsonHelper.parseToMap(result);
			/**
			 * 	错误码：
				WL-0000: 成功
				WL-20001：签名验证失败
				WL-20010：服务器超时
				WL-20009：系统内部错误
			 */
			String result_code = (String) resultMap.get("result_code");
			String result_msg = (String) resultMap.get("result_msg");
			System.out.println(result_code+":"+result_msg);
//				System.out.println(MessageUtil.verifySign(resultMap, SECRET_KEY));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static List<Map<String, Object>> getListMap_nr(){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 1; i <= 1; i++)
		{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ebeln", "10000000");
			map.put("matnr", "ES80000000001");
			map.put("plwrk", "");
			map.put("sernr", "");
			map.put("menge", "0");
			map.put("psttr", null);
			map.put("psttr_end",null);
			map.put("state", "BSD");
			map.put("dttim", "2017-10-26 10:12:18");
			map.put("bdmng", "0");
			map.put("zmark1", "");
			map.put("zmark2", "");
			map.put("zmark3", "");
			list.add(map);
		}
		return list;
		/**
		 * 	{
			    "t_item":[
			        {
			            "ebeln":"10000000",//整车采购订单
			            "matnr":"ES80000000001",//整车物料号
			            "plwrk":"",//工厂
			            "sernr":"",//VIN号
			            "menge":"0",//订单数量
			            "psttr":null,//inf排产计划焊接开始上线日期
			            "psttr_end":null,//inf排产计划总装下线日期
			            "state":"BSD",//节点状态 如下对应编码
			            "dttim":"2017-10-26 10:12:18",//日期时间
			            "bdmng":"0",//确认数量
			            "zmark1":"",//备注说明1
			            "zmark2":"",//备注说明2
			            "zmark3":""//备注说明3
			        }
			    ]
			}
			//节点状态
			BSD	焊接下线
			BSU	焊接上线
			CTA	生成整车运输指令
			FDA	完成整车调度指令
			FIX	整车排产确认
			GAD	总装下线
			GAU	总装上线
			GR	整车入库
			PSD	涂装下线
			PSU	涂装上线
			TED	板车预计/实际入库
			VAR	整车预计/实际到达CDC
			VGI	整车实际出库
			VGR	CDC收整车时间
			VHD	双方实际交车
			VLD	整车预计/实际至装卸区
			VTT	整车在途跟踪
			WSC	等待排产
		 */
	}
	//新车堆场出库 data模拟器
	public static List<Map<String, Object>> getListMap_Nco(){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 1; i <= 2; i++)
		{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("zaction", "N");
			map.put("budat", "2017-10-26");
			map.put("zdoc_no", "3PL_"+i);
			map.put("zdoc_item", i);
			map.put("gernr", "vin_" + i);

			list.add(map);
		}
		return list;
		/***
		 * 	{
			 "t_item":
			 [
			  {
			   "zaction":"",//动作（"N"—New "C"--Cancel）
			   "budat":null,//过账日期 YYYY-mm-dd
			   "zdoc_no":"",//3PL 凭证号码
			   "zdoc_item":"",//3PL 凭证行项目号
			   "gernr":"a0001"//VIN号
			  },
			  {
			   "zaction":"",
			   "budat":null,
			   "zdoc_no":"",
			   "zdoc_item":"",
			   "gernr":"a0002"
			  }
			 ]
			}
		 */
	}

}	
