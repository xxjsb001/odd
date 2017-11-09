package com.vtradex.wms.server.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import com.vtradex.wms.server.web.filter.ItmsLogInIpHolder;
/**
 * @Author :      yc.min              
 */
public class IpUtils {
	/**127.0.0.1*/
	public static String LOCAL_IP = "127.0.0.1";
	
	public static String localIp(){
		//服务的地址
		InetAddress address;
		String localIp = "";
		try {
			address = InetAddress.getLocalHost();
			localIp = address.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return localIp;
	}
	/***
	 * 获取用户真实IP地址,不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
	 * 可是,如果通过了多级反向代理的话,X-Forwarded-For的值并不止一个,而是一串IP值,究竟哪个才是真正的用户端的真实IP呢?
	 * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串.
	 * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130,192.168.1.100 
	 * 用户真实IP为： 192.168.1.110 
	 * @param request 
	 * @return 
	 */
	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		 return ip;
	}
	/**判断客户端与服务端是否是一个IP*/
	public static Boolean isLocalIp(){
		Boolean beSame = false;
		String localIp = localIp();//服务所在服务器IP
		String logInIp = ItmsLogInIpHolder.getLogInIp();//登录IP
		if(LOCAL_IP.equals(logInIp)//本地
				|| localIp.equals(logInIp)){//登录地址与本机一致
			beSame = true;
		}
		return beSame;
	}
	
	public static Boolean iPTest(String ip) throws UnknownHostException{
		boolean state = true;
		try {  
           InetAddress ad = InetAddress.getByName(ip);  
           state = ad.isReachable(60000);//测试是否可以达到该地址  
           if(!state) {
        	   System.err.println("连接失败:"+ ad.getHostAddress());  
           }
        }catch (IOException e) {
			e.printStackTrace();
		}
		return state;
		/*while(true){
			try {  
	           InetAddress ad = InetAddress.getByName("192.168.10.218");  
	           boolean state = ad.isReachable(60000);//测试是否可以达到该地址  
	           if(state) {
	        	   System.out.println("连接成功" + ad.getHostAddress());  
	           } else{
	        	   System.err.println("连接失败");  
	           }  
	        }catch (IOException e) {
				e.printStackTrace();
			} 
			try {
				Thread.sleep(60000);//9min=540000,1min=60000
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
		}*/
	}  
	public static void main(String[] args) {
		try {
			IpUtils.iPTest("192.168.10.92");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}

