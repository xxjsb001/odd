package com.vtradex.wms.server.utils;
import org.apache.commons.httpclient.Header;  
import org.apache.commons.httpclient.HttpClient;  
import org.apache.commons.httpclient.NameValuePair;  
import org.apache.commons.httpclient.methods.PostMethod;  
public class SendMsg {

	/**
	 * http://blog.csdn.net/huasuan26/article/details/19813515
	 * http://blog.csdn.net/javaGirlOne/article/details/42965071
	 * http://blog.csdn.net/csh624366188/article/details/7183457
	 * http://blog.csdn.net/u012689336/article/details/52738112
	 * http://javawangbaofeng.iteye.com/blog/2209223
	 GBK编码发送接口地址：
http://gbk.sms.webchinese.cn/?Uid=本站用户名&Key=接口安全密码&smsMob=手机号码&smsText=短信内容 
	UTF-8编码发送接口地址：
http://utf8.sms.webchinese.cn/?Uid=本站用户名&Key=接口安全密码&smsMob=手机号码&smsText=短信内容
	获取短信数量接口地址(UTF8)：
http://sms.webchinese.cn/web_api/SMS/?Action=SMS_Num&Uid=本站用户名&Key=接口安全密码
	获取短信数量接口地址(GBK)：
http://sms.webchinese.cn/web_api/SMS/GBK/?Action=SMS_Num&Uid=本站用户名&Key=接口安全密码
	 */
	public static void main(String[] args) throws Exception{
		HttpClient client = new HttpClient();  
        PostMethod post = new PostMethod("http://utf8.api.smschinese.cn");  
        post.addRequestHeader("Content-Type",  
                "application/x-www-form-urlencoded;charset=utf-8");// 在头文件中设置转码  
        NameValuePair[] data = { new NameValuePair("Uid", "xiangrikui"),  
                new NameValuePair("Key", "df31f3d086075997f0bb"),  
                new NameValuePair("smsMob", "15855171638"),  
                new NameValuePair("smsText", "超时提醒:你的作业时效已超时,请及时跟进作业任务,本短信收费标准:0.1元/条") };  
        post.setRequestBody(data);  
  
        client.executeMethod(post);  
        Header[] headers = post.getResponseHeaders();  
        int statusCode = post.getStatusCode();  
        System.out.println("statusCode:" + statusCode);  
        for (Header h : headers) {  
            System.out.println(h.toString());  
        }  
        String result = new String(post.getResponseBodyAsString().getBytes(  
                "gbk"));  
        System.out.println(result);  
  
        post.releaseConnection();

	}

}
