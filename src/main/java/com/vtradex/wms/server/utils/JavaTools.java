package com.vtradex.wms.server.utils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
//import org.skife.csv.CSVReader;
//import org.skife.csv.SimpleReader;
import com.vtradex.thorn.server.config.globalparam.GlobalParam;
import com.vtradex.thorn.server.dao.hibernate.HibernateCommonDao;
import com.vtradex.thorn.server.exception.BusinessException;

/**
 * 
 * @Description :   常用工具类 WMS
 * @Author      :    <a href='yongcheng.min@vtradex.net'>闵永成</a>
 * @CreateDate  :    2011-4-19
 */
public class JavaTools extends HibernateCommonDao{
	//g001
	public static SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
	//g002
	public static final String numbers = "0123456789";
	//g003
	public static final String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	//g004
	public static final String mixings = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	//g005
	private static final String ENCODING="GBK";
	//g006
	public static final String ymdPattern2 = "^\\d{4}\\d{2}\\d{2}[-]\\d{4}\\d{2}\\d{2}$";//yyyyMMdd-yyyyMMdd
	//g007
	public static final String ymdPattern3 = "^\\d{4}\\d{2}\\d{2}$";//yyyyMMdd
	//g008
	 /** 
     * 定义分割常量 （#在集合中的含义是每个元素的分割，|主要用于map类型的集合用于key与value中的分割） 
     */  
    private static final String SEP1 = "#";  
  //g009
    private static final String SEP2 = "|";  
  //g010
    /**\r\n 换行*/
	public static String enter = "\r\n";
	//g011
	/**"yyyy-MM-dd HH:mm*/
	public static String dmy_hm = "yyyy-MM-dd HH:mm";
	//g012
	/**yyyyMMdd*/
	public static String dmy = "yyyyMMdd";
	//g013
	/**yyyy-MM-dd*/
	public static String y_m_d = "yyyy-MM-dd";
	//g014
	/**"yyyy-MM-dd HH:mm:ss*/
	public static String dmy_hms = "yyyy-MM-dd HH:mm:ss";
	//g015
	/**"HH:mm:ss*/
	public static String hms = "HH:mm:ss";
	//g016
	/**'	'tab*/
	public static String tab = "	";
	//g017
	/**yyyyMM*/
	public static String ym = "yyyyMM";
	//g018
	/**yyMMdd*/
	public static String yymd = "yyMMdd";
	//g019
	/**yyyy-MM-dd*/
	public static String patterString = "^\\d{4}[-]\\d{2}[-]\\d{2}$";
	//g020
	/**HHmmss*/
	public static String HMS = "HHmmss";
	//g021
	public static String reg="^[a-zA-Z]{2}$";
	//g022
	/**yyMMdd_HHmmss*/
	public static String ymd_Hms="yyMMdd_HHmmss";
	//g023
	/**yyyy-MM*/
	public static String y_m = "yyyy-MM";
	
	public static String hh = "HH";
	public static SimpleDateFormat ymd_hms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //000
    public static int getSize(int size,int PAGE_NUMBER){
		int j = size / PAGE_NUMBER;
		if((size % PAGE_NUMBER) > 0){
			j += 1;
		}
		return j;
    }
  //001
    public static int getIndex(int k,int size,int PAGE_NUMBER){
    	int toIndex = ((k + 1) * PAGE_NUMBER);
		if(toIndex > size){
			toIndex = size;
		}
		return toIndex;
    }
  //002
    public static List<Object> getList(List<Object> list,int k,int toIdnex,int PAGE_NUMBER){
    	List<Object> ret = list.subList((k * PAGE_NUMBER), toIdnex);
		return ret;
    }
  //003
    /**return List<Object[]>*/
    public static List<Object[]> getListObj(List<Object[]> list,int k,int toIndex,int PAGE_NUMBER){  
        List<Object[]> ret = list.subList((k * PAGE_NUMBER), toIndex);  
        return ret;  
    }
  //004
    public static List<String> getListStr(List<String> list,int k,int toIdnex,int PAGE_NUMBER){
    	List<String> ret = list.subList((k * PAGE_NUMBER), toIdnex);
		return ret;
    }
  //005
    public static List<Long> getListLong(List<Long> list,int k,int toIdnex,int PAGE_NUMBER){
    	List<Long> ret = list.subList((k * PAGE_NUMBER), toIdnex);
		return ret;
    }
  //006
    public static String isNull(Object obj){
		return (obj == null) ? "" : obj.toString();
	}
  //007
    /**@nullback:为空值时返回值*/
    public static String valueOf(Object obj,String nullback){
    	return (obj == null) ? nullback : obj.toString();
    }
  //008
	public static String isNull(String Num){
		 if(Num == null||"".equals(Num)){
			Num = "0";
		 }
		 return Num;
	}
	//009
	public static Double isNull(Double Num){
		if(Num == null){
			Num = 0D;
		}
		return Num;
	}
	//010
	/**  
	 * stringToInteger
	 * @return  Integer
	 */
	public static Integer stringToInteger(String str) {
		if (null==str || str.trim().equals("")){
			return 0;
		}else{
			return Integer.parseInt(str.trim());
		}
	}
	//011
	 public static boolean isValidDate(String s){
		     DateFormat df = new SimpleDateFormat("yyyyMMdd");
	        try
	        {
	             df.parse(s);
	             return true;
	         }
	        catch (Exception e)
	        {
	            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
	            return false;
	        }
	 }
	//012
	/**
	 * stringToDate-minyongcheng
	 * @param string is xxxx-xx-xx
	 * @return yyyy-MM-dd
	 */
	 public static Date stringToDate(String str) {
		if (str == null || str.trim().equals("") || str.trim().equals("0"))
			return null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return df.parse(str.trim());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	 }
	//013
	 /**
		 * stringToDate-huangshaokang
		 * @param string is xxxxmmdd
		 * @return yyyyMMdd
		 */
		 public static Date stringToDate2(String str) {
			if (str == null || str.trim().equals("") || str.trim().equals("0"))
				return null;
			DateFormat df = new SimpleDateFormat("yyyyMMdd");
			try {
				return df.parse(str.trim());
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		 }
		//014
	 public static Long stringToLong(String str) {
			if (str == null || str.trim().equals(""))
				return null;
			else
				return Long.parseLong(str.trim());
	}
	//015
	 public static Double stringToDouble(String str) {
			if (null==str || str.trim().equals(""))
				return 0D;
			else
				return Double.parseDouble(str.trim());
	 }
	//016
	/**
	 * 长度单位转换
	 * cm->m
	 * @return double
	 */
	public static double unitConversion(double num){
		double result = 0;
		result = num * (1 / 100.0);
		return result;
	}
	//017
	/**
	 * 体积转换
	 * 立方厘米->立方米
	 */
	public static double unitConv(double num){
		double result = 0;
		result = num * (1 / 1000.0);
		return result;
	}
	//018
	/** yyyyMMdd*/
	public static String dateToString(Date date) {
		if(date == null){
			return null;
		}
		DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(date);
	}
	//019
	/** yyyy-MM-dd*/
	public static String dateYMDToStr(Date date){
		if(date==null){
			return null;
		}
		return ymd.format(date);
	}
	//020
	public static String getRandomUtils(int length){
		 StringBuffer sb = new StringBuffer(); 
	     Random random = new Random(); 
	     for (int i = 0; i < length; i++){
	    	 sb.append(numbers.charAt(random.nextInt(numbers.length())));
	     }
	     return sb.toString();

	}
	//021
	/**将字符串转换成符合规范的date  myc*/
	public static Date format(String time){
		Date date = null;
		if(time.contains("-")){
			date = DateUtil.getDate(
					time, "yyyy-MM-dd");
		}else if (time.contains("/")){
			date = DateUtil.getDate(
					time, "yyyy/MM/dd");
		}else{
			date = DateUtil.getDate(
					time, "yyyyMMdd");
		}
		return date;
	}
	//022
	/**
	 * 把一个Date 按照指定格式转换为String
	 * @param date
	 * @return
	 */
	public static String format(Date date,String format) {
		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}
	//023
	/** 
	  * 返回两个日期相差的天数,有一个时间为null返回-1 
	  * @param d1  长的时间 
	  * @param d2  短的时间 
	  * @return int 
	  */ 
	 public static int diff_in_date(Date d1, Date d2){
		 if(null == d1 || null == d2){ 
		   return -1; 
		 } 
		 return (int)(d1.getTime() - d2.getTime())/86400000; 
	 }
	//024
	 /**  
	     * 计算两个日期之间相差的天数  
	     * @param smdate 较小的时间 
	     * @param bdate  较大的时间 
	     * @return 相差天数 
	     * @throws ParseException  
	     */    
	    public static int daysBetween(Date smdate,Date bdate) throws ParseException    
	    {    
	    	if(null == smdate || null == bdate){ 
			   return -1; 
			 } 
	        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
	        smdate=sdf.parse(sdf.format(smdate));  
	        bdate=sdf.parse(sdf.format(bdate));  
	        Calendar cal = Calendar.getInstance();    
	        cal.setTime(smdate);    
	        long time1 = cal.getTimeInMillis();                 
	        cal.setTime(bdate);    
	        long time2 = cal.getTimeInMillis();         
	        long between_days=(time2-time1)/(1000*3600*24);  
	            
	       return Integer.parseInt(String.valueOf(between_days));           
	    }  
	  //025
	    /** 
	    *字符串的日期格式的计算 
	    */  
	        public static int daysBetween(String smdate,String bdate) throws ParseException{  
	        	if(null == smdate || null == bdate){ 
	 			   return -1; 
	 			 }
	            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
	            Calendar cal = Calendar.getInstance();    
	            cal.setTime(sdf.parse(smdate));    
	            long time1 = cal.getTimeInMillis();                 
	            cal.setTime(sdf.parse(bdate));    
	            long time2 = cal.getTimeInMillis();         
	            long between_days=(time2-time1)/(1000*3600*24);  
	                
	           return Integer.parseInt(String.valueOf(between_days));     
	        }  
	      //026
	/**某个日期加一天
	 * @today yyyyMMdd
	 * @return String yyyyMMdd*/
	public static String addOneday(String today){   
        SimpleDateFormat f =  new SimpleDateFormat("yyyyMMdd");   
        try   {   
            Date  d  =  new Date(f.parse(today).getTime()+24*3600*1000);     
              return  f.format(d);   
        }   
        catch(Exception ex) {   
            return   "输入格式错误";     
        }   
    }
	//027
	/**日期+天数得到相应日期
	 * @today yyyyMMdd
	 * @return String yyyyMMdd*/
	public static Date addNumday(Date d,long day){
		if(d == null){
			return null;
		}
		long time = d.getTime(); 
		day = day*24*60*60*1000; 
		time+=day; 
		return new Date(time); 
    }
	//028
	/**某个日期减一天
	 *@return String yyyyMMdd */
	public static String deleteOneday(String today){   
        SimpleDateFormat f =  new SimpleDateFormat("yyyyMMdd");   
        try   {   
            Date  d  =  new Date(f.parse(today).getTime()-24*3600*1000);     
              return  f.format(d);   
        }   
        catch(Exception ex) {   
            return   "输入格式错误";     
        }   
    }
	//029
	/**获得系统上一天的时间*/
	public static Date getAscendDay(){
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(Calendar.DAY_OF_MONTH, -1);
		return currentDate.getTime();
	}
	//030
	/**
     * 
     * @param dateTime 格式为"yyyy-mm-dd"的日期格式
     * @return string类型的前一天的日期yyyy-MM-dd
     * @throws ParseException
     */
	//031
    public static  String getFormerDate(String dateTime,Date date) throws ParseException{
        
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
        long dif = df.parse(dateTime).getTime()-86400*1000;
//        Date date=new Date(); 
        date.setTime(dif);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            
        return sdf.format(date);
     }
  //032
    /**
     * 
     * @param dateTime 格式为"yyyy-mm-dd"的日期格式
     * @return string类型的前一天的日期yyyyMMdd
     * @throws ParseException
     */
    public static  String getFormerDate2(String dateTime,Date date) throws ParseException{
        
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
        long dif = df.parse(dateTime).getTime()-86400*1000;
//        Date date=new Date(); 
        date.setTime(dif);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
            
        return sdf.format(date);
     }
  //033
	/**给日期增加几天 
	 * @return date*/
	public static Date addDays(int num){
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_YEAR);
		calendar.set(Calendar.DAY_OF_YEAR, day + num);
		return calendar.getTime();
	}
	//034
	/**比较日期大小*/
	 public static int compare_date(String DATE1, String DATE2,String format) {
        DateFormat df = new SimpleDateFormat(format);//"yyyy-MM-dd hh:mm"
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
//                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
//                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
	//035
	 public static int compare_date2(String DATE1, String DATE2) {
	        DateFormat df = new SimpleDateFormat("yyyyMMdd");
	        try {
	            Date dt1 = df.parse(DATE1);
	            Date dt2 = df.parse(DATE2);
	            if (dt1.getTime() > dt2.getTime()) {
//	                System.out.println("dt1 在dt2前");
	                return 1;
	            } else if (dt1.getTime() < dt2.getTime()) {
//	                System.out.println("dt1在dt2后");
	                return -1;
	            } else {
	                return 0;
	            }
	        } catch (Exception exception) {
	            exception.printStackTrace();
	        }
	        return 0;
	    }
	//036
	 public static int compareToYMD( Date dt1, Date dt2) {
	        try {
	            if (dt1.getTime() > dt2.getTime()) {
//	                System.out.println("dt1 在dt2前");
	                return 1;
	            } else if (dt1.getTime() < dt2.getTime()) {
//	                System.out.println("dt1在dt2后");
	                return -1;
	            } else {
	                return 0;
	            }
	        } catch (Exception exception) {
	            exception.printStackTrace();
	        }
	        return 0;
	    }
	//037
	 /**获取这批导入单据的标志 */
		public static String getMark(){
			Date today = new Date();
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String mark = sf.format(today);
			return mark;
		}
		//038
	//039
	 /**yyyy/MM/dd -> yyyy-MM-dd hh:mm:ss */
	 public static String parseTimes(String date) {
	        String d = date;
	        if (date.indexOf("/") > 0) {
	            d = date.replace("/", "-");
	        }
	        String temp = d + " 00:00:00";
	        try {
	            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	            Date time = df.parse(temp);
	            return String.valueOf(time);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	}
	//040
	 /**
		 * 将格林威治时间转成本地时间
		 */
		public Date getGMTTime(Date date){
			Date result = null;
			try {
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
				dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
				String dataStr = dateFormat.format(date);
				dateFormat.setTimeZone(TimeZone.getDefault());
				result = dateFormat.parse(dataStr);
			} catch (ParseException e) {
				result = new Date();
				throw new BusinessException("import.data.ParseException.error");
			}
			return result;
		} 
		//041
	 /**set对象属性值
		 * @throws ClassNotFoundException
		 * @throws NoSuchMethodException 
		 * @throws SecurityException 
		 * @throws InvocationTargetException 
		 * @throws IllegalAccessException 
		 * @throws IllegalArgumentException */
	@SuppressWarnings("unchecked")
	public static String copyField(Object obj,String value,String propertyName) throws ClassNotFoundException, SecurityException, NoSuchMethodException,
	             IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Class classType=obj.getClass();
//			Field[] fields=classType.getDeclaredFields();
        String fieldName=propertyName;   
        String stringLetter=fieldName.substring(0, 1).toUpperCase();   
//	      String getName="get"+stringLetter+fieldName.substring(1);   
        String setName="set"+stringLetter+fieldName.substring(1);   
           
        Method m = classType.getMethod(setName,new Class[]{Class.forName("java.lang.String")});
		m.invoke(obj,new Object[]{value}); 
		
		return propertyName;
	}
	//042
	/**set对象属性值
	 * obj 操作对像,value1 参数值1,propertyName1 字段属性1,value2  参数值2, propertyName2 字段属性2,num 后缀(可无)*/
	@SuppressWarnings("unchecked")
	public static String copyField(Object obj,String value1,String propertyName1,
			Double value2,String propertyName2,int num) throws ClassNotFoundException, SecurityException, NoSuchMethodException,
	             IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Class classType=obj.getClass();
//		Field[] fields=classType.getDeclaredFields();
        String stringLetter1=propertyName1.substring(0, 1).toUpperCase(); 
        String stringLetter2=propertyName2.substring(0, 1).toUpperCase(); 
//      String getName="get"+stringLetter+fieldName.substring(1);   
        String setName1="set"+stringLetter1+propertyName1.substring(1)+num;   //set通道1
        String setName2="set"+stringLetter2+propertyName2.substring(1)+num;
           
        Method m1 = classType.getMethod(setName1,new Class[]{Class.forName("java.lang.String")});
		m1.invoke(obj,new Object[]{value1}); 
		Method m2 = classType.getMethod(setName2,new Class[]{Class.forName("java.lang.Double")});
		m2.invoke(obj,new Object[]{value2});
		
		return null;
	}
	//043
	/** 获得全局端口号*/
	public int getPort() throws SQLException {
		int port = 0;
		String hql = "from GlobalParam gp where gp.paramId=:IP_ADDRESS";
		GlobalParam  gp = (GlobalParam) this.findByQueryUniqueResult(hql, new String[]{"IP_ADDRESS"}, new Object[]{"IP_ADDRESS"});
		String value = gp.getValue();
		String[] str2 = value.split(":");
		port = Integer.parseInt(str2[1].substring(0,4));
		return port;
	}
	//044
	/**
	 * 保留位数:num,保留位数
	 */
	public static String formatTest(double a,int num){
		String s = "0";
		DecimalFormat df = new DecimalFormat("###.00");
		if(a != 0){
			 if(num == 1){
	        	 df = new DecimalFormat("###.0");
			 }else if(num == 2){
			 	 df = new DecimalFormat("###.00");
			 }else if(num == 3){
				 df = new DecimalFormat("###.000");
			 }
	         s = df.format(a);
		}
		return s;
	}
	//045
	/**
	 * 相除保留位数:num,保留位数
	 */
	public static String formatTest(double a,double b,int num){
		String s = "0";
		DecimalFormat df = new DecimalFormat("###.00");
		if(a != 0){
			 if(num == 1){
	        	 df = new DecimalFormat("###.0");
			 }else if(num == 2){
			 	 df = new DecimalFormat("###.00");
			 }else if(num == 3){
				 df = new DecimalFormat("###.000");
			 }
			 s = df.format(a/b);
		}
		return s;
	}
	//046
	/**
	 * 相乘保留位数:num,保留位数
	 */
	public static String formatMultiply(double a,double b,int num){
		String s = "0";
		DecimalFormat df = new DecimalFormat("###.00");
		if(a != 0){
			 if(num == 1){
	        	 df = new DecimalFormat("###.0");
			 }else if(num == 2){
			 	 df = new DecimalFormat("###.00");
			 }else if(num == 3){
				 df = new DecimalFormat("###.000");
			 }
			 s = df.format(a*b);
		}
		return s;
	}
	//047
	/** 
     * String转换Map 
     *  
     * @param mapText 
     *            :需要转换的字符串 
     * @param KeySeparator 
     *            :字符串中的分隔符每一个key与value中的分割 
     * @param ElementSeparator 
     *            :字符串中每个元素的分割 
     * @return Map<?,?> 
     */  
	//048
    public static Map<String, Object> StringToMap(String mapText) {  
  
        if (mapText == null || mapText.equals("")) {  
            return null;  
        }  
        mapText = mapText.substring(1);  
  
//        mapText = EspUtils.DecodeBase64(mapText);  
  
        Map<String, Object> map = new HashMap<String, Object>();  
        String[] text = mapText.split("\\" + SEP2); // 转换为数组  
        for (String str : text) {  
            String[] keyText = str.split(SEP1); // 转换key与value的数组  
            if (keyText.length < 1) {  
                continue;  
            }  
            String key = keyText[0]; // key  
            String value = keyText[1]; // value  
            if (value.charAt(0) == 'M') {  
                Map<?, ?> map1 = StringToMap(value);  
                map.put(key, map1);  
            } else if (value.charAt(0) == 'L') {  
                List<?> list = StringToList(value);  
                map.put(key, list);  
            } else {  
                map.put(key, value);  
            }  
        }  
        return map;  
    }  
  //049
	/** 
     * String转换List 
     *  
     * @param listText 
     *            :需要转换的文本 
     * @return List<?> 
     */  
    public static List<Object> StringToList(String listText) {  
        if (listText == null || listText.equals("")) {  
            return null;  
        }  
        listText = listText.substring(1);  
  
//        listText = EspUtils.DecodeBase64(listText);  
  
        List<Object> list = new ArrayList<Object>();  
        String[] text = listText.split(SEP1);  
        for (String str : text) {  
            if (str.charAt(0) == 'M') {  
                Map<?, ?> map = StringToMap(str);  
                list.add(map);  
            } else if (str.charAt(0) == 'L') {  
                List<?> lists = StringToList(str);  
                list.add(lists);  
            } else {  
                list.add(str);  
            }  
        }  
        return list;  
    }  
  //050
    /**把出库入库集中放入MAP 相加->minyongcheng **/
	public static Map<Long,Double> getAddMap(Map<Long, Double> map1,Map<Long, Double> map2) {
		Map<Long,Double> cc = new HashMap<Long,Double>();
			Iterator<Entry<Long, Double>> iter1 = map1.entrySet().iterator();
			while (iter1.hasNext()) {
				Map.Entry<Long,Double> entry = (Map.Entry<Long,Double>) iter1.next();
				if(cc.containsKey(entry.getKey())){
					Double temp=(Double) entry.getValue();
					cc.put(entry.getKey(),cc.get(entry.getKey())+temp);
				}else{
					cc.put(entry.getKey(),entry.getValue());
				}
			}
			Iterator<Entry<Long, Double>> iter2 = map2.entrySet().iterator();
			while (iter2.hasNext()) {
				Map.Entry<Long,Double> entry = (Map.Entry<Long,Double>) iter2.next();
				if(cc.containsKey(entry.getKey())){
					Double temp=(Double) entry.getValue();
					cc.put(entry.getKey(),cc.get(entry.getKey())+temp);
				}else{
					cc.put(entry.getKey(),entry.getValue());
				}
			}
		
		return cc;
	}
	//051
	/**map1,map2 合并至map1->minyongcheng **/
	@SuppressWarnings("unchecked")
	public static Map<Long,List> getQMap(Map<Long, List> map1,Map<Long, Double> map2) {
		Iterator<Entry<Long, Double>> iter2 = map2.entrySet().iterator();
		while (iter2.hasNext()) {
			Map.Entry<Long,Double> entry = (Map.Entry<Long,Double>) iter2.next();
			if(map1.containsKey(entry.getKey())){
				//合并
			   Double temp=(Double) entry.getValue();
			   List newlIST = map1.get(entry.getKey());
			   newlIST.remove(1);
			   newlIST.add(1,temp);
			   map1.put(entry.getKey(),newlIST);
			}else{
				//新增,同时设置list第一列为0
				List listrece = new ArrayList();
				listrece.add(0D);
				listrece.add(entry.getValue());
				map1.put(entry.getKey(),listrece);
			}
		}
		return map1;
	}
	//052
	/**map1,map2合并 迭代Long 型的map1 返回<long string>型MAP*/
	public static Map<Long,String> getQMapByLong(Map<Long, Long> map1,
			Map<Long, String> map2) {
		Map<Long,String> newMap = new HashMap<Long,String>();
		Iterator<Entry<Long, Long>> iter1 = map1.entrySet().iterator();
		while (iter1.hasNext()) {
			Map.Entry<Long,Long> entry = (Map.Entry<Long,Long>) iter1.next();
			if(map2.containsKey(entry.getKey())){
				//合并
			   String map2Value = map2.get(entry.getKey());
			   Long temp = entry.getValue();
			   newMap.put(temp,map2Value);
			}
		}
		return newMap;
	}
	//053
	/**合并list*/
	public static List<Object> mergeList(List<Object> l1,List<Object> l2){
		List<Object> temp = new ArrayList<Object>(l1);
		temp.retainAll(l2);
		l1.removeAll(temp);
		l2.removeAll(temp);
		List<Object> l3 = new ArrayList<Object>();
		l3.addAll(l1);
		l3.addAll(l2);
		l3.addAll(temp);
		return l3;
		
	}
	//054
	/**获得全局参数值 param标识*/
	@SuppressWarnings("unchecked")
	public  String getGlobalParamValue(String param) throws SQLException {
		String value = null;
		List<Long> gpIds = this.findByQuery("SELECT gp.id FROM GlobalParam gp WHERE gp.paramId=:param",
				new String[]{"param"}, new Object[]{param});
		if(gpIds.size() > 0){
			GlobalParam  gp = this.load(GlobalParam.class, gpIds.get(0));
			value = gp.getValue();
		}
		return value;
	}
	//056
	public static String encoder(String str) {
		try {
			return URLEncoder.encode(str , ENCODING);
//			return StringUtils.replace(s , "%" , "_VT_");
		} catch (UnsupportedEncodingException e) {
			return str;
		}
	}

	//057
	public static String decoder(String str) {
		try {
//			str = StringUtils.replace(str, "_VT_" , "%");
			return URLDecoder.decode(str , ENCODING);
		} catch (UnsupportedEncodingException e) {
			return str;
		}
	}
	public static String shaHex(String rawPass){
		return DigestUtils.shaHex(rawPass);
	}
	//058
	/**流水号 **/
	public static String numberCode(int length){
		Date date = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String ss = sdf.format(date);
		
    	String base = "0123456789";
        Random random = new Random();  
        StringBuffer sb = new StringBuffer();  
        for (int i = 0; i < length; i++) {  
            int number = random.nextInt(base.length());  
            sb.append(base.charAt(number));  
        }  
        return sb.toString();  
	}
	//059
	private static String formateHql(String hql){
		String hqlStatement = hql;
		int leftToenailIndex = hqlStatement.indexOf("(");
		if(leftToenailIndex != -1){
			hqlStatement = StringUtils.replace(hqlStatement,"("," ( ");
		}
		int rightToenailIndex = hqlStatement.indexOf(")");
		if(rightToenailIndex != -1){
			hqlStatement = StringUtils.replace(hqlStatement,")"," ) ");
		}
		int enterIndex = hqlStatement.indexOf("\n");
		if(enterIndex != -1){
			hqlStatement = StringUtils.replace(hqlStatement,"\n"," \n");
		}
		return hqlStatement;
	}
	//060
	//+表示1个或多个（如"3"或"225"），*表示0个或多个（[0-9]*）（如""或"1"或"22"），?表示0个或1个([0-9]?)(如""或"7")
	public static Boolean isNumber(String value){
		return value.matches("[0-9]+");
	}
	//061
	public static Boolean isContainLetter(String value){
		Pattern p = Pattern.compile(value);  
        Matcher m = p.matcher(reg);  
		return m.find();
	}
	//062 判断value是否为英文字母
	public static Boolean isLetter(String value){
		if(value==null || value.length()<=0){
			return false;
		}
		return value.matches("^[a-zA-Z]{"+value.length()+"}$");
	}
	//063
	public static String replaceAllNumber(String value){
		return value.replaceAll("\\d+","");
	}
	//064
	public static String removeStartsWithStr(String value,String remValue){
		while(value.startsWith(remValue)){
			value = StringUtils.substringAfter(value,remValue);
		}
		return value ;
	}
	//065
	public static boolean isNumeric1(String str){
		  Pattern pattern = Pattern.compile("[0-9]*");
		  return pattern.matcher(str).matches();
	}
	//066
		/**生成指定长度list 
		 * arg0:"1.5"=>list第1第5位为false,其余为true
		 * arg1:list长度*/
		public static List<Boolean> booleans(String djNO,int size){
			String[] ss = StringUtils.split(djNO, "//.");
			Map<String,Boolean> boof = new HashMap<String,Boolean>();
			for(int i = 0 ; i<ss.length ;i++){//{3=false, 2=false, 1=false, 5=false, 4=false}
				boof.put(ss[i], false);
			}
			List<Boolean> bool = new ArrayList<Boolean>();
			for(int i = 1 ; i<=size; i++){
				if(boof.containsKey(i+"")){
					bool.add(false);
				}else{
					bool.add(true);
				}
			}
			return bool;
			
		}
	    //072
	    /** 返回相差天数
	     * @param startDate 
	     * @param endDate 
	     * @return 
	     * @throws ParseException 
	     */  
	    public static long getDistDates(Date startDate,Date endDate)    
	    {  
	        long totalDate = 0;  
	        Calendar calendar = Calendar.getInstance();  
	        calendar.setTime(startDate);  
	        long timestart = calendar.getTimeInMillis();  
	        calendar.setTime(endDate);  
	        long timeend = calendar.getTimeInMillis();  
	        totalDate = Math.abs((timeend - timestart))/(1000*60*60*24);  
	        return totalDate;  
	    }
	    //075
	    public static double floor(double a){
	    	double b = Math.floor(a);
	    	if(a-b>=0.5){
	    		b += 1;
	    	}
			return b;
	    }
	  //076
	    public static Date beforeOneDate(Date beginDate){
	    	Calendar calendar = Calendar.getInstance();
			calendar.setTime(beginDate);
			calendar = DateUtils.truncate(calendar, Calendar.DATE);
//			Date currentDate = calendar.getTime();
			calendar.add(Calendar.DATE, -1);
			Date beforeOneDate = calendar.getTime();
			return beforeOneDate;
	    }
	  //077
	    public static List<String> charList(List<String> fromLis){
	    	List<String> toLis = new ArrayList<String>();
	    	if(fromLis!=null&&fromLis.size()>0){
				for(String s : fromLis){
					toLis.add("'"+s+"'");
				}
			}
			return toLis;
	    }
	  //078:返回当月的某一天,day=1返回yyyy-MM-01,day=6返回yyyy-MM-06
	    public static String mosaicDate(int day,Date someDay,String format){
	    	String[] value = JavaTools.format(someDay, format).split("-");//JavaTools.y_m_d
	    	String date = null;
	    	if(day>9){
	    		date = value[0]+"-"+value[1]+"-"+day;
	    	}else if(day>0 && day<10){
	    		date = value[0]+"-"+value[1]+"-0"+day;
	    	}
			return date;
	    }
	    
		//083 匹配是否为10位长度的日期类型 yyyy-MM-dd
		public static Boolean patternYMD(String value){
	    	Pattern pattern = Pattern.compile(patterString);
	    	Matcher m = pattern.matcher(value);
//	    	System.out.println(m.group());
	    	return m.find();
		}
		//084 自定义消息-红色
		public static String fontByRed(String mesg,int size){
	    	String font = "<font color=\"red\" size=\""+size+"\">"+mesg+"</font>";
			return font;
	    }
		//085 自定义消息-蓝色
	    public static String fontByBlue(String mesg,int size){
	    	String font = "<font color=\"blue\" size=\""+size+"\">"+mesg+"</font>";
			return font;
	    }
	    //086 自定义取字符串的第几位,首位取1
	    public static String getTrimByNum(String value,int end){
	    	String trimValue = null;
	    	if(StringUtils.isEmpty(value) || end==0 || value.length()<end){
	    		return trimValue;
	    	}
	    	trimValue = StringUtils.substring(value, end-1, end);
	    	return trimValue;
	    }
	  //088 获得系统日期的上一个月
	    public static Calendar getDateOfLastMonth(Calendar date) {  
	        Calendar lastDate = (Calendar) date.clone();  
	        lastDate.add(Calendar.MONTH, -1);  
	        return lastDate;  
	    }  
	  //089 获得指定日期的上一个月
	    //Date beforMonth = JavaTools.getDateOfLastMonth(JavaTools.format(new Date(), JavaTools.y_m_d), JavaTools.y_m_d).getTime();
	    //System.out.println(JavaTools.dateYMDToStr(beforMonth));
	    public static Calendar getDateOfLastMonth(String dateStr,String format) {  
	        SimpleDateFormat sdf = new SimpleDateFormat(format);  //"yyyyMMdd"
	        try {  
	            Date date = sdf.parse(dateStr);  
	            Calendar c = Calendar.getInstance();  
	            c.setTime(date);  
	            return getDateOfLastMonth(c);  
	        } catch (ParseException e) {  
	            throw new IllegalArgumentException("Invalid date format(yyyyMMdd): " + dateStr);  
	        }  
	    }
	    /**
		 * 比较两个日期相差的分钟
		 */
	    //JavaTools.getDoubleMargin("2016-12-01 09:18:30", "2016-12-01 09:17:59")
		public static double getDoubleMargin(String date1, String date2) {
			double margin;
			try {
				ParsePosition pos = new ParsePosition(0);
				ParsePosition pos1 = new ParsePosition(0);
				Date dt1 = ymd_hms.parse(date1, pos);
				Date dt2 = ymd_hms.parse(date2, pos1);
				long l = dt1.getTime() - dt2.getTime();
				margin = (l / (60 * 1000.00));
				return margin;
			} catch (Exception e) {
				return 0;
			}
		}
		public static String spiltLast(String exception,String spilt){
  			String[] errors =exception==null?null:exception.split(spilt);
			if(errors!=null && errors.length>0){
				exception = errors[errors.length-1];
			}
			return exception;
  		}
	    public static void main(String[] args) throws ParseException {
	    	System.out.println(JavaTools.getDoubleMargin("2016-12-01 09:18:30", "2016-12-01 09:17:59"));//0.5166666666666667
	    	
//	    	JavaTools.deleteFile(new File("D:\\itms\\92\\ORCL\\JACDATAMO\\EXP_MO_TMS_ORDER_TEMP.bat"));
	    	System.out.println(JavaTools.numberCode(3));
//	    	JavaTools.smbPutLocal("smb://administrator:Passw0rd@192.168.10.92/vtradex/test","../","bmsTemplate.txt");
//	    	JavaTools.smbDeleteFile("smb://administrator:Passw0rd@192.168.10.92/vtradex/test", "bmsTemplate.txt");
	    	
	    	/*String str = "a中";
			String a = encoder(str);
			System.out.println(a);
			System.out.println(decoder(a));	*/
	    	/*String nowHms = JavaTools.format(new Date(), JavaTools.hms);
	    	System.out.println(nowHms);
	    	
	    	System.out.println(36/10);
	    	System.out.println(JavaTools.getSize(29, 10));
	    	System.out.println(Math.ceil(3.6));
	    	//org.apache.commons.lang.StringUtils
	    	//(6ooo#~~~)第一个#开始截取(从左起)
	    	System.out.println(
	    			StringUtils.substringAfter("234556#6ooo#~~~", "#"));
	    	//(~~~)最后一个#开始截取
	    	System.out.println(
	    			StringUtils.substringAfterLast("234556#6bbb#~~~", "#"));
	    	//(234556)第一个#往左截取(从左起)
	    	System.out.println(
	    			StringUtils.substringBefore("234556#6bbb#~~~", "#"));
	    	//(234556#6bbb)最后一个#往左截取
	    	System.out.println(
	    			StringUtils.substringBeforeLast("234556#6bbb#~~~", "#"));
	    	//(6bbb)截取两个#之间的
	    	System.out.println(
	    			StringUtils.substringBetween("234556#6bbb#~~~", "#"));
	    	//(6bbb#~)截取第一个#与第一个$之间的(从左起)
	    	System.out.println(
	    			StringUtils.substringBetween("$234556#6bbb#~$~~$","#","$"));*/
	    	
	        // TODO Auto-generated method stub  
	       /* SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	        Date d1=sdf.parse("2012-09-08 10:10:10");  
	        Date d2=sdf.parse("2012-09-15 00:00:00");  
	        System.out.println(daysBetween(d1,d2));  
	  
	        System.out.println(daysBetween("2012-09-08 10:10:10","2012-09-15 00:00:00"));  */
	    } 
}
