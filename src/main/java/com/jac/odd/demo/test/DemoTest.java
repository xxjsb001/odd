package com.jac.odd.demo.test;

import java.util.HashMap;
import java.util.Map;
import com.vtradex.wms.server.utils.ItmsFileUtils;
import net.sf.json.JSONObject;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
public class DemoTest {
	private static String URL = "http://192.168.10.92:8080/odd_server/odd_server";
	private static String URL_HEAD = "?interfaceType=sqlQuery&fileName=test_name&airm=insert&fileSql=";
	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("order_no", "订单号");
		map.put("dealer_code", "经销商编码");
		map.put("driver_name", "司机姓名");
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.putAll(map);
		String postData = jsonObject.toString();
		System.out.println("NO1:"+postData);
		
		postData = ItmsFileUtils.toUnicodeString(postData);
		System.out.println("NO2:"+postData);
		
		String html = GetResponseData(URL+URL_HEAD+postData, postData);
		System.out.println("NO4:"+html);
	}
	
	public static String GetResponseData(String url, String postData){
		String data = null;
		try {
			System.out.println("NO3:"+url);
			URL dataUrl = new URL(url);
			HttpURLConnection con = (HttpURLConnection) dataUrl.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Proxy-Connection", "Keep-Alive");
			con.setDoOutput(true);
			con.setDoInput(true);
			OutputStream os = con.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			dos.write(postData.getBytes());
			dos.flush();
			dos.close();
			InputStream is = con.getInputStream();
			DataInputStream dis = new DataInputStream(is);
			byte d[] = new byte[dis.available()];
			dis.read(d);
			data = new String(d);
			con.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	/**
	NO1:{"driver_name":"司机姓名","dealer_code":"经销商编码","order_no":"订单号"}
	NO2:{"driver_name":"\u53f8\u673a\u59d3\u540d","dealer_code":"\u7ecf\u9500\u5546\u7f16\u7801","order_no":"\u8ba2\u5355\u53f7"}
	NO3:http://192.168.10.92:8080/odd_server/odd_server?interfaceType=sqlQuery&fileName=test_name&airm=insert&fileSql={"driver_name":"\u53f8\u673a\u59d3\u540d","dealer_code":"\u7ecf\u9500\u5546\u7f16\u7801","order_no":"\u8ba2\u5355\u53f7"}
	NO4:success
	 */
}
