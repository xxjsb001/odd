package com.vtradex.wms.server.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.creatCode.CreatTwoDecemensionCodeImageUtil;
import com.vtradex.thorn.server.util.BeanUtils;
import com.vtradex.wms.server.model.base.TypeOfExtendPropC1;
import com.vtradex.wms.server.model.move.WmsMoveDocType;

/**yc min*/
public class MyUtils {
	private static Log log = LogFactory.getLog(MyUtils.class);
	public static SimpleDateFormat yy = new SimpleDateFormat("yyyyMMdd");
	public static SimpleDateFormat ymd_hms = new SimpleDateFormat("yyyyMMdd HHmmss");
	
	/**"yyyy-MM-dd HH:mm*/
	public static String dmy_hm = "yyyy-MM-dd HH:mm";
	/**yyyyMMdd*/
	public static String dmy = "yyyyMMdd";
	
	/** 处理间隔时间 默认3s */
	public static int receiverMsgInterval = 3000;
	/**####*/
	public static String spilt4 = "####";
	/**#*/
	public static String spilt1 = "#";
	/**_*/
	public static String spilt_ = "_";
	/**.*/
	public static String spot = ".";
	/**,*/
	public static String comma = ",";
	/**\r\n 换行*/
	public static String enter = "\r\n";
	/**loc*/
	public static String loc = "loc";
	/**locSup*/
	public static String locSup = "locSup";
	
	/**一级包材*/
	public static String first = "first";
	/**二级包材*/
	public static String second = "second";
	/**辅材*/
	public static String fc = "fc";
	
	public static final String ymdPattern3 = "^\\d{4}\\d{2}\\d{2}$";
	/**8位数字*/
	public static final String yyPattern = "^\\d{8}$";
	
	/**备份路径*/
	public static String ITMS_BACK = "D:\\itms";
	
	 public static int getSize(int size,int PAGE_NUMBER){  
        int j = size / PAGE_NUMBER;  
        if((size % PAGE_NUMBER) > 0){  
            j += 1;  
        }  
        return j;  
    }  
    public static int getIndex(int k,int size,int PAGE_NUMBER){  
        int toIndex = ((k + 1) * PAGE_NUMBER);  
        if(toIndex > size){  
            toIndex = size;  
        }  
        return toIndex;  
    }  
    public static List<Object> getList(List<Object> list,int k,int toIdnex,int PAGE_NUMBER){  
        List<Object> ret = list.subList((k * PAGE_NUMBER), toIdnex);  
        return ret;  
    }  
    /**return List<Object[]>*/
    public static List<Object[]> getListObj(List<Object[]> list,int k,int toIndex,int PAGE_NUMBER){  
        List<Object[]> ret = list.subList((k * PAGE_NUMBER), toIndex);  
        return ret;  
    }
    /**return List<Object>*/
    public static List<Object> getListObjs(List<Object> list,int k,int toIndex,int PAGE_NUMBER){  
        List<Object> ret = list.subList((k * PAGE_NUMBER), toIndex);  
        return ret;  
    }
    /**return string(yyyyMMdd)*/
    public static String formatDateYYToStr(Date date) {
		try {
			return yy.format(date);
		} catch (Exception e) {
			log.debug("MyUtils.formatDateYYToStr():" + e.getMessage());
			return null;
		}
	}
    /**return string(yyyy-MM-dd HH:mm)*/
    public static String formatDateymdhm(Date date) {
		try {
			SimpleDateFormat ymd_hm = new SimpleDateFormat(dmy_hm);
			return ymd_hm.format(date);
		} catch (Exception e) {
			log.debug("MyUtils.formatDateYYToStr():" + e.getMessage());
			return null;
		}
	}
    
    /**
	 * 传入格式为日期的字符, 格式为：yyyy-MM-dd HH:mm 的日期字符串形式返回
	 */
	public static Date formatStrYmdhm(String str) {
		try {
			SimpleDateFormat ymd_hm = new SimpleDateFormat(dmy_hm);
			return ymd_hm.parse(str);
		} catch (Exception e) {
			log.debug("MyUtils.formatStrToDate():" + e.getMessage());
			return null;
		}

	}
	/**
	 * 传入格式为日期的字符, 格式为：yyyyMMdd HHmmss 的日期字符串形式返回
	 */
	public static Date formatStrToDate(String str) {
		try {
			return ymd_hms.parse(str);
		} catch (Exception e) {
			log.debug("MyUtils.formatStrToDate():" + e.getMessage());
			return null;
		}

	}
	/**  
     * @param startDate  
     * @param endDate  
     * @return  
     * @throws ParseException  
     */    
    public static long getDistDates(Date startDate,Date endDate){    
        long totalDate = 0;    
        Calendar calendar = Calendar.getInstance();    
        calendar.setTime(startDate);    
        long timestart = calendar.getTimeInMillis();    
        calendar.setTime(endDate);    
        long timeend = calendar.getTimeInMillis();    
        totalDate = Math.abs((timeend - timestart))/(1000*60*60*24);    
        return totalDate;    
    } 
    /**返回相差秒*/
    public static long getDistDatesMi(Date startDate,Date endDate){    
        long totalDate = 0;    
        Calendar calendar = Calendar.getInstance();    
        calendar.setTime(startDate);    
        long timestart = calendar.getTimeInMillis();    
        calendar.setTime(endDate);    
        long timeend = calendar.getTimeInMillis();    
        totalDate = Math.abs((timeend - timestart))/(1000);    
        return totalDate;    
    } 
	
    /**objSize:每行大小
     * spilt:分割符(\t==tab键)*/
    public static void createTxt(List<Object[]> values,int objSize,String file,String spilt){
    	StringBuffer row=null;  
        OutputStreamWriter osw = null;  
        try {  
            osw = new OutputStreamWriter(new FileOutputStream(file,true),"GBK");  
            for(int i=0 ; i<values.size() ; i++){  
                row = new StringBuffer();  
                Object[] obj = values.get(i);  
                for(int j = 0 ; j<objSize ; j++){  
                    if(objSize-j == 1){  
                        row.append(obj[j]+"\r\n");//换行  
                    }else{  
                        row.append(obj[j]+spilt);//tab键  
                    }  
                }  
                osw.write(row.toString());  
            }  
            values.clear();  
		} catch (Exception e) {  
		    e.printStackTrace();  
		}finally{  
		         if(osw!=null)
		           try {  
		            osw.close();
		           } catch (IOException e) {  
		               e.printStackTrace();  
		           }  
		    } 
		}
    public static String font(String mesg){
    	String font = "<font color=\"red\" size=\"1\">"+mesg+"</font>";
		return font;
    }
    public static String fontByBlue(String mesg){
    	String font = "<font color=\"blue\" size=\"2\">"+mesg+"</font>";
		return font;
    }
    public static String getTask(){
    	String hql = "FROM WmsTask task WHERE task.originalBillCode =:billCode"
				+ " AND task.type =:type AND task.status =:status";
    	return hql;
    }
    public static String getTaskByType(){
    	String hql = "FROM WmsTask task WHERE task.originalBillCode =:billCode"
				+ " AND task.type =:type";
    	return hql;
    }
    public static String getUnShipTask(){
    	String hql = "FROM WmsTask task WHERE task.originalBillCode =:billCode"
				+ " AND task.type =:type AND task.movedQuantityBU-task.tiredMovedQuantityBU>0";
    	return hql;
    }
    /**返回 按照参数指定补齐到指定长度的字符串*/
    public static String newString(String oldString,String args,int lengh){
    	for(int i=0 ; i<lengh ; i++){
    		oldString = args+oldString;
    	}
		return oldString;
    }
    public static String[] sourceAry(String[] sourceAry){
//    	String[] sourceAry = {"2","6","4","8","3","1"};
    	for (int i = 1; i < sourceAry.length; i++) {
	    	for (int j = 0; j < i; j++) {
		    	if(sourceAry[i].compareTo(sourceAry[j])>0){
			    	String temp = sourceAry[i];
			    	sourceAry[i]=sourceAry[j];
			    	sourceAry[j]=temp;
		    	}
	    	}
    	}
		return sourceAry; 
    }
    public static Object[] dateAry(Object[] sourceAry){
//    	Date[] sourceAry = {"2","6","4","8","3","1"};
    	for (int i = 1; i < sourceAry.length; i++) {
	    	for (int j = 0; j < i; j++) {
		    	if(((Date) sourceAry[i]).compareTo((Date) sourceAry[j])>0){
		    		Date temp = (Date) sourceAry[i];
			    	sourceAry[i]=sourceAry[j];
			    	sourceAry[j]=temp;
		    	}
	    	}
    	}
		return sourceAry; 
    }
    public static void sort(int[]a) { 
	   	 int temp=0; 
	   	 for(int i=a.length-1;i>0;--i) {
	   		 System.out.println("1:"+a[i]);
	   	   for(int j=0;j<i;++j) { 
	   		System.out.println("2:"+a[j]+","+a[j+1]);
	   	     if(a[j+1]<a[j]) { 
	   	    	System.out.println("3:"+a[j+1]);
	   	        temp=a[j]; 
	   	        a[j]=a[j+1]; 
	   	        a[j+1]=temp; 
	   	     } 
	   	   } 
	   	 } 
   	} 
    public static void getInventoryHashCode(Long locationId, Long itemKeyId, 
    		Long packageUnitId, String inventoryStatus) {
    	String hashCode = BeanUtils.getFormat(locationId, itemKeyId, packageUnitId, inventoryStatus);
    	System.out.println(hashCode);
    }
    public static String getTypeOfExtendPropC1(String extendPropC1){
    	String value = "-";
    	if("TypeOfExtendPropC1.NEWBEST".equals(extendPropC1)){
    		value = "最新";
    	}else if("TypeOfExtendPropC1.NEW".equals(extendPropC1)){
    		value = "新";
    	}else if("TypeOfExtendPropC1.OLD".equals(extendPropC1)){
    		value = "老";
    	}else if("TypeOfExtendPropC1.OLDBEST".equals(extendPropC1)){
    		value = "最老";
    	}
    	return value;
    }
    public static String checkExtendPropc1(String extendPropC1){
		if(TypeOfExtendPropC1.NEW.equals(extendPropC1)){
			extendPropC1 = "新";
		}else if(TypeOfExtendPropC1.NEWBEST.equals(extendPropC1)){
			extendPropC1 = "最新";
		}else if(TypeOfExtendPropC1.OLD.equals(extendPropC1)){
			extendPropC1 = "老";
		}else if(TypeOfExtendPropC1.OLDBEST.equals(extendPropC1)){
			extendPropC1 = "最老";
		}else if(TypeOfExtendPropC1.NULLVALUE.equals(extendPropC1)){
			extendPropC1 = "-";
		}else{
			extendPropC1 = null;
		}
		return extendPropC1;
	}
    public static String checkExtendPropc1Back(String extendPropC1){
		if("新状态".equals(extendPropC1)||"新".equals(extendPropC1)){
			extendPropC1 = TypeOfExtendPropC1.NEW;
		}else if("最新状态".equals(extendPropC1)||"最新".equals(extendPropC1)){
			extendPropC1 = TypeOfExtendPropC1.NEWBEST;
		}else if("老状态".equals(extendPropC1)||"老".equals(extendPropC1)){
			extendPropC1 = TypeOfExtendPropC1.OLD;
		}else if("最老状态".equals(extendPropC1)||"最老".equals(extendPropC1)){
			extendPropC1 = TypeOfExtendPropC1.OLDBEST;
		}else if("-".equals(extendPropC1)){
			extendPropC1 = TypeOfExtendPropC1.NULLVALUE;
		}else{
			extendPropC1 = null;
		}
		return extendPropC1;
	}
    public static String getMoveType(String type){
    	if(WmsMoveDocType.MV_PICKTICKET_PICKING.equals(type)){
    		type = "发货单";
    	}else if(WmsMoveDocType.MV_WAVE_PICKING.equals(type)){
    		type = "波次单";
    	}else if(WmsMoveDocType.MV_REPLENISHMENT_MOVE.equals(type)){
    		type = "补货单";
    	}else if(WmsMoveDocType.MV_MOVE.equals(type)){
    		type = "移位单";
    	}else if(WmsMoveDocType.MV_PUTAWAY.equals(type)){
    		type = "上架单";
    	}else if(WmsMoveDocType.MV_PROCESS_PICKING.equals(type)){
    		type = "加工单";
    	}else if(WmsMoveDocType.MV_QUALITY_MOVE.equals(type)){
    		type = "质检单";
    	}
    	return type;
    }
    public static void main(String[] args) {
//    	MyUtils.getInventoryHashCode(20007999L, 2564030L, 2390845L, "NORMAL");
    	CreatTwoDecemensionCodeImageUtil handler = new CreatTwoDecemensionCodeImageUtil();
    	handler.encoderQRCode("http://www.baidu.com", "d:/barcode/h2.jpg", "png");
    	
    	handler.encoderQRCode("http://verp5.vtradex.net/", "d:/barcode/h3.jpg", "png", 8, "d:/barcode/logo.jpg");
	}
    /**rexp:正则表达式,s:待匹配字符串*/
    public static Boolean isPattern(String rexp,String s) {  
    	Boolean isPattern = true;
        Pattern p = Pattern.compile(rexp);  
        Matcher m = p.matcher(s);  
        if (m.matches()) { 
        	isPattern = true;  
        } else {  
        	isPattern = false;  
        }
		return isPattern;  
          
    }  
    public static void groupString (String rexp,String s) {  
        Pattern p = Pattern.compile(rexp);   
        Matcher m = p.matcher(s);   
        boolean result = m.find();   
        System.out.println("该次查找获得匹配组的数量为："+m.groupCount());   
        while (result) {  
            System.out.println(m.group(1));  
            System.out.println(m.group(2));  
            result = m.find();   
        }  
              
              
    }
}