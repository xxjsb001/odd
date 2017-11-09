package com.vtradex.wms.server.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
/**yc.mim*/
public class JsonTools{

	public JsonTools(){
	}

	public static String getCreateJson(String key, Object value){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(key, value);

		return jsonObject.toString();
	}
	
	public static String getCreateJson(Map map){
		JSONObject jsonObject = new JSONObject();
		jsonObject.putAll(map);

		return jsonObject.toString();
		//{"key1":"value01","key2":"value02","key3":"value03"}
	}
	
	//{"code":0,"message":"","data":{"message":"数据格式不正确","status":101}}
	public static Boolean getJsonKey(String jsondata){
		JSONObject jsonObject = JSONObject.fromObject(jsondata);  

    	String code = jsonObject.getString("code");
        if(!code.equals("0")){
        	return false;
        }
        
        String data = jsonObject.getString("data");
        JSONObject dataObject = JSONObject.fromObject(data);
        String status = dataObject.getString("status");
//        String message = dataObject.getString("message");
        if(!status.equals("0")){
        	return false;
        }
		return true;
    
	}
	public static List<Map<String, Object>> getListMap(){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 1; i <= 5; i++)
		{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("zaction", i);
			map.put("budat", i);
			map.put("zdoc_no", i);
			map.put("zdoc_item", i);
			map.put("gernr", "hacker13A" + i);

			list.add(map);
		}
		return list;
		/***
		 * {
				"ListMap":
				[
					{"UserName":"hacker13A1","UserId":1},
					{"UserName":"hacker13A2","UserId":2},
					{"UserName":"hacker13A3","UserId":3},
					{"UserName":"hacker13A4","UserId":4},
					{"UserName":"hacker13A5","UserId":5}
				]
			}
		 */
	}
	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("jacyl_orderscheduling", "调度单号");
		map.put("jacyl_carnum", "车牌号");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("key1", map);
		
		map.clear();
		map.put("k1", "v1");
		map.put("k2", "v2");
		jsonObject.put("key2", map);
		
		String data = jsonObject.toString();
		System.out.println(data);
		
		JSONObject jsonObject1 = JSONObject.fromObject(data);  
    	String key1 = jsonObject1.getString("key1");
    	String key2 = jsonObject1.getString("key2");
    	System.out.println(key1+"\n"+key2);
    	
    	JSONObject dataObject = JSONObject.fromObject(key2);
        String k1 = dataObject.getString("k1");
        String k2 = dataObject.getString("k2");
        System.out.println(k1+"\n"+k2);
    	System.out.println("------------------2-----------------");
    	System.out.println(getCreateJson("ListMap", getListMap()));
	}
}