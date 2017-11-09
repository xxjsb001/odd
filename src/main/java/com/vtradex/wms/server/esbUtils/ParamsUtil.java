package com.vtradex.wms.server.esbUtils;

import java.util.HashMap;
import java.util.Map;

public class ParamsUtil {
	
	public static Map<String,Object> getParamMap(Map<String,Object> toSignparams,Map<String,Object> unsignedParams,String secretKey) throws Exception
	{
		toSignparams=getParamsAfterSign(toSignparams,secretKey);
		toSignparams.putAll(unsignedParams);
		return toSignparams;	
	}
	
	public static Map<String,Object>getParamsAfterSign(Map<String,Object> toSignparams,String secretKey) throws Exception
	{
		String sign = MessageUtil.sign(toSignparams,secretKey);
		toSignparams.put("sign", sign);
		return toSignparams;
	}
	
	public static Map<String,String> convertObjectToStringParams(Map<String,Object> paramMap)
	{
		Map<String,String>postMap = new HashMap<String,String>();
		for(String key:paramMap.keySet())
		{
			postMap.put(key, paramMap.get(key).toString());
		}
		return postMap;
		
	}

}
