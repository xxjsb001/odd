package com.vtradex.wms.server.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.util.FileUtils;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;
import com.vtradex.thorn.server.exception.BusinessException;
/**
 * 
 * @Description :   文件工具类
 * @Author      :    <a href='yongcheng.min@vtradex.com'>闵永成</a>
 * @CreateDate  :    2011-4-19
 */
public class ItmsFileUtils {
	public static String enter = "\r\n";
	public static final String GBK="GBK";
	public static final String UTF_8="UTF-8";
	public static String localPath = ".."+File.separator;
	//JavaTools.smbPut("smb://administrator:Passw0rd@192.168.10.92/vtradex/test", "D:/test/test.xls");
	public static void smbPut(String remoteUrl, String localFilePath) {  
	    InputStream in = null ;  
	    OutputStream out = null ;  
	    try  {  
	        File localFile = new  File(localFilePath);  
	        String fileName = localFile.getName();  
	        SmbFile remoteFile = new  SmbFile(remoteUrl +  "/"  + fileName);  
	        in = new  BufferedInputStream( new  FileInputStream(localFile));  
	        out = new  BufferedOutputStream( new  SmbFileOutputStream(remoteFile));  
	        byte [] buffer =  new   byte [ 1024 ];  
	        while  (in.read(buffer) != - 1 ) {  
	            out.write(buffer);  
	            buffer = new   byte [ 1024 ];  
	        }  
	    } catch  (Exception e) {  
	        e.printStackTrace();  
	    } finally  {  
	        try  { 
	        	if(out!=null){
	        		out.close();
	        	}
	        	if(in!=null){
	        		in.close(); 
	        	}
	        } catch  (IOException e) {  
	            e.printStackTrace();  
	        }  
	    }  
	}  
	//JavaTools.smbPutLocal("smb://administrator:Passw0rd@192.168.10.92/vtradex/test", "bmsTemplate.txt");
	public static void smbPutLocal(String remoteUrl, String localPath,String srcFileName,String descFileName){
		InputStream in = null ;
	    OutputStream out = null ;
		try {
			SmbFile remoteFile = new  SmbFile(remoteUrl +  "/"  + srcFileName);
			in = new  BufferedInputStream( new  SmbFileInputStream(remoteFile));  
	        out = new  BufferedOutputStream( new  FileOutputStream(localPath+descFileName));  
	        byte [] buffer =  new   byte [ 1024 ];  
	        while  (in.read(buffer) != - 1 ) {  
	            out.write(buffer);  
	            buffer = new   byte [ 1024 ];  
	        }  
		} catch (Exception e) {  
	        System.out.println("not exits:"+remoteUrl +  "/"  + srcFileName);
	    }finally  {  
	        try  { 
	        	if(out!=null){
	        		out.close();
	        	}
	        	if(in!=null){
	        		in.close(); 
	        	}
	        } catch  (IOException e) {  
	            e.printStackTrace();  
	        }  
	    }  
	}
	
	public static void smbDeleteFile(String remoteUrl,String fileName){
		try {
			SmbFile remoteFile = new  SmbFile(remoteUrl +  "/"  + fileName);
			try {
				if(remoteFile.isFile()){
					remoteFile.delete();
				}
			} catch (SmbException e) {
				e.printStackTrace();
				throw new BusinessException(e.getMessage());
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
	}
	public static void smbCreateDir(String remoteUrl,String dirName){
		SmbFile remoteDir;
		try {
			remoteDir = new  SmbFile(remoteUrl +  "/"  + dirName);
			if(!remoteDir.exists()){
				remoteDir.mkdir();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		} catch (SmbException e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
		
	}
	
	public static void smbDeleteDir(String remoteUrl,String dirName){
		SmbFile remoteDir;
		try {
			remoteDir = new  SmbFile(remoteUrl + dirName +"/");
			if(remoteDir.exists()){
				SmbFile[] remoteFiles = remoteDir.listFiles();
				if(remoteFiles!=null && remoteFiles.length>0){
					smbDeleteFile(remoteFiles);
				}
				remoteDir.delete();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		} catch (SmbException e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
	}
	private static void smbDeleteFile(SmbFile[] remoteFiles){
		try {
			for(SmbFile file : remoteFiles){
				if(file.isFile()){
					file.delete();
				}else if(file.isDirectory()){
					SmbFile[] files = file.listFiles();
					smbDeleteFile(files);
				}
			}
		}catch (SmbException e) {
			e.printStackTrace();
			throw new BusinessException(e.getMessage());
		}
	}
	public static String smbReadTxt(String remotePath,String fileName,String encoding){

		InputStreamReader read = null ;
		BufferedReader br = null;
    	String sql = "";
    	StringBuffer sb = new StringBuffer();
		try {
			SmbFile remoteFile = new  SmbFile(remotePath +  "/"  + fileName);
			if(remoteFile.exists() && remoteFile.isFile()){
				read = new InputStreamReader(new  SmbFileInputStream(remoteFile), encoding);
				br = new BufferedReader(read);
	    		String temp = null;
	    		while ((temp = br.readLine()) != null) {
	    			if("".equals(temp)){
	    				continue;
	    			}
	    			StringTokenizer st = new StringTokenizer(temp);
	    			StringBuffer sbTemp = new StringBuffer();
	    			while(st.hasMoreElements()){
	    				String num = st.nextToken("\t").trim();
	    				sbTemp.append(num);
	    			}
	    			if(sbTemp.length()>0){
	    				sb.append(sbTemp+enter);
	    			}
	    		}
	    		sql = StringUtils.substringBeforeLast(sb.toString(), enter);
			}
			 
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(null != br){
					br.close();
				}
				if(read!=null){
					read.close();
	        	}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sql;
		
	}
	//===============================================local
    public static void createTxt(String file,StringBuffer row,String character){
    	OutputStreamWriter osw = null;
    	try {
    		osw = new OutputStreamWriter(new FileOutputStream(file,true),character);
    		osw.write(row.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
	       	 if(osw!=null)//if(osw!=null)
	             try {
	             	osw.close();//osw.close();
	             } catch (IOException e) {
	                 e.printStackTrace();
	             }
	    }
    }
    public static File mkdir(String path){
    	File file =new File(path);    
    	//如果文件夹不存在则创建    
    	if  (!file .exists()  || !file .isDirectory()){       
    	    file .mkdir();    
    	}
    	return file;
    }
    /**删除指定天数之前的文件*/
    public static Integer moveFileToDel(String fromDir,int deleteDays){
		File srcDir = new File(fromDir);
		if (!srcDir.exists()) {
			return 0;
		}
		File[] files = srcDir.listFiles();
		if (files == null || files.length <= 0) {
			return 0;
		}
		int l = 0;
		Date today = new Date();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				try {
					File ff = files[i];
			        long time=ff.lastModified();
				    Calendar cal=Calendar.getInstance();   
			        cal.setTimeInMillis(time);   
			        Date lastModified = cal.getTime();
			      //(int)(today.getTime() - lastModified.getTime())/86400000;
			        long days = JavaTools.getDistDates(today, lastModified);
			        if(days>=deleteDays){
			        	files[i].delete();
			        	l++;
			        }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return l;
	}
    /**移动文件*/
    public static void moveSrcFile(File sourceFile, File destFile){
    	try {
			FileUtils.newFileUtils().copyFile(sourceFile, destFile);
			sourceFile.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
  	public static void deleteFile(File file){ 
  	   if(file.exists()){                         
  		    if(file.isFile()){                     //判断是否是文件
  		        file.delete();                     
  		    }else if(file.isDirectory()){          //否则如果它是一个目录
  		        File files[] = file.listFiles();          
  		        for(int i=0;i<files.length;i++){           
  		            deleteFile(files[i]);             
  		        } 
  		    } 
  	   }
  	} 
  	
  	public static String readStrTxt(File file,String encoding){
    	//jt.readTxt(new File("C:/Users/Administrator/Desktop/aaaaa.txt"));
    	BufferedReader br = null;
    	String sql = "";
    	StringBuffer sb = new StringBuffer();
    	try {
    		InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
    		br = new BufferedReader(read);
    		String temp = null;
    		while ((temp = br.readLine()) != null) {
    			if("".equals(temp)){
    				continue;
    			}
    			StringTokenizer st = new StringTokenizer(temp);
    			StringBuffer sbTemp = new StringBuffer();
    			while(st.hasMoreElements()){
    				String num = st.nextToken("\t").trim();
    				sbTemp.append(num);
    			}
    			if(sbTemp.length()>0){
    				sb.append(sbTemp+enter);
    			}
    		}
    		sql = StringUtils.substringBeforeLast(sb.toString(), enter);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(null != br){
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sql;
    }
  	public static String readByte(String localPath,String fileName){
  		InputStream in = null ;OutputStream out = null ;
  		StringBuffer sf = new StringBuffer();
  		try  {  
	        File localFile = new  File(localPath+fileName);  
	        in = new  BufferedInputStream( new  FileInputStream(localFile));  
	        byte [] buffer =  new   byte [ 1024 ];  
	        while  (in.read(buffer) != - 1 ) {  
	        	out = new ByteArrayOutputStream(1024);
	        	out.write(buffer);
	        	sf.append(out);
//	        	System.out.println(out.toString());
	            buffer = new   byte [ 1024 ];  
	        }  
	    } catch  (Exception e) {  
	        e.printStackTrace();  
	    } finally  {  
	        try  { 
	        	if(out!=null){
	        		out.close();
	        	}
	        	if(in!=null){
	        		in.close(); 
	        	}
	        } catch  (IOException e) {  
	            e.printStackTrace();  
	        }  
	    }
		return sf.toString();
  	}
	 //================================解析txt档并写入新的txt档=================================================
   //jt.createTxt(excuteReadTxt("C:/Users/Administrator/Desktop/test001.txt"), 7, "C:/Users/Administrator/Desktop/test001.txt");
   /**解析txt 有BUG:map无序的,将会导致读取的文本内容无序展示*/
   public static Map<Integer,List<Object>>  readTxt(File file){
   	//jt.readTxt(new File("C:/Users/Administrator/Desktop/aaaaa.txt"));
   	BufferedReader br = null;
   	Map<Integer,List<Object>> map = new HashMap<Integer, List<Object>>();
   	List<Object> values = null;
   	int key = 1;
   	try {
   		InputStreamReader read = new InputStreamReader(new FileInputStream(file), "GBK");
   		br = new BufferedReader(read);
   		String temp = null;
   		while ((temp = br.readLine()) != null) {
   			if("".equals(temp)){
   				continue;
   			}
   			values = new ArrayList<Object>();
   			StringTokenizer st = new StringTokenizer(temp);
   			while(st.hasMoreElements()){
   				String num = st.nextToken("\t").trim();
   				values.add(num);
   			}
   			map.put(key, values);
   			key++;
   		}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(null != br){
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return map;
   }
 //068
   /**执行readTxt方法*/
   public static List<Object[]> excuteReadTxt(String filePath){
		Map<Integer,List<Object>> map = readTxt(new File(filePath));
		List<Object[]> objs = new ArrayList<Object[]>();
		Iterator<Entry<Integer, List<Object>>> iterator = map.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<Integer, List<Object>> entery = iterator.next();
			int key = entery.getKey();
			List<Object> values = entery.getValue();
			Object[] ss = new Object[values.size()+10];
			for(int i = 0 ; i<values.size() ; i++){
				ss[i] = values.get(i);
			}
			objs.add(ss);
		}
		return objs;
   }
 //069
   /**生成txt 
    * endLine:结尾符(换行用"\r\n")
    * spilt:指定分割符(tab键用"\t")
    * character:字符("GBK","utf-8")*/
   public static void createTxt(List<Object[]> values,int objSize,String file
   		,String endLine,String spilt,String character){
   	StringBuffer row=null;
   	OutputStreamWriter osw = null;
   	try {
   		osw = new OutputStreamWriter(new FileOutputStream(file,true),character);
   		for(int i=0 ; i<values.size() ; i++){
   			row = new StringBuffer();
   			Object[] obj = values.get(i);
   			for(int j = 0 ; j<objSize ; j++){
   				if(objSize-j == 1){
   					row.append(obj[j]+endLine);//换行
   				}else{
   					row.append(obj[j]+spilt);//"\t":tab键
   				}
   			}
   			osw.write(row.toString());
   		}
   		values.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
      	 if(osw!=null)//if(osw!=null)
            try {
            	osw.close();//osw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
     }
   }
 //070
 //================================解析txt档并写入新的txt档=================================================
 //================================类型转换=================================================
   /**string转list<Object[]>
    * @param requestContent='xx,{a=1,b=2,c=3......}'
    * @param requestContent:用一个逗号分隔的字符串,第二个元素是map,用{}包含了若干个a=1组合形式的数据*/
   public static List<Object[]> string2Obj(String requestContent){
   	List<Object[]> values = new ArrayList<Object[]>();
   	String xx = StringUtils.substringBefore(requestContent, ",");
		String map = StringUtils.substringAfter(requestContent, ",");//{A=1.0, B=2.0, C=3.0}
		String m = StringUtils.substringBetween(map, "{", "}");//A=1.0,B=2.0,C=3.0
		String[] ss = m.split(",");//[A=1.0, B=2.0, C=3.0]
		for(int i = 0;i<ss.length;i++){
			String a = StringUtils.substringBefore(ss[i], "=");
			String quitity = StringUtils.substringAfter(ss[i], "=");
			String[] obj = new String[]{
//					xx+","+a+","+quitity+","+"-"
					xx.trim()+"\t"+a.trim()+"\t"+quitity.trim()+"\t"+"-"
			};
			values.add(obj);
		}
		return values;
   }
   //071
   public static void markTxt(String message,String txtPath){
		List<Object[]> values = new ArrayList<Object[]>();
		values.add(new Object[]{
				message+JavaTools.format(new Date(), "yyyy-MM-dd HH:mm:ss")
		});
		createTxt(values, 
				values.size(), txtPath, JavaTools.enter, "\t", "utf-8");
	}
   
   public static String toUnicodeString(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 0 && c <= 255) {
				sb.append(c);
			} else {
				sb.append("\\u"+Integer.toHexString(c));
			}
		}
		return sb.toString();
	}
	public static String decodeUnicode(String theString){
		char aChar;
		int len= theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for(int x=0;x<len;){
			aChar = theString.charAt(x++);
			if(aChar=='\\'){
				aChar=theString.charAt(x++);
				if(aChar=='u'){
					int value=0;
			
					for(int i=0;i<4;i++){
						aChar = theString.charAt(x++);
						switch(aChar){
							case'0':
							case'1':
							case'2':
							case'3':
							case'4':
							case'5':
							case'6':
							case'7':
							case'8':
							case'9':
								value=(value<<4)+aChar-'0';
								break;
							case'a':
							case'b':
							case'c':
							case'd':
							case'e':
							case'f':
								value=(value<<4)+10+aChar-'a';
								break;
							case'A':
							case'B':
							case'C':
							case'D':
							case'E':
							case'F':
								value=(value<<4)+10+aChar-'A';
								break;
							default:
								throw new IllegalArgumentException("Malformed\\uxxxxencoding.");
						}
			
					}
					outBuffer.append((char)value);
				}else{
					if(aChar=='t')
						aChar='\t';
					else if(aChar=='r')
						aChar='\r';
					else if(aChar=='n')
						aChar='\n';
					else if(aChar=='f')
						aChar='\f';
					outBuffer.append(aChar);
				}
			} else {
				outBuffer.append(aChar);
			}
		}
		return outBuffer.toString();
	}
	public static String createSignKey(String apitest,String app_secret,String data,String method,String timestamp) {
		StringBuffer signBuff = new StringBuffer();
		signBuff.append(app_secret);
		signBuff.append("app_key");
		signBuff.append(apitest);
		signBuff.append("data");
		signBuff.append(data);
		signBuff.append("method");
		signBuff.append(method);
		signBuff.append("timestamp");
		signBuff.append(timestamp);
		signBuff.append(app_secret);
		
		return Md5Utils.md5(signBuff.toString()).toUpperCase();
	}
	public static byte[] streamToByteArray(InputStream in) throws IOException {
        byte[] buf = new byte[100];
        byte[] dest = new byte[0];
        int len = -1;
        while ((len = in.read(buf)) != -1) {
            byte[] tmp = new byte[dest.length + len];
            System.arraycopy(dest, 0, tmp, 0, dest.length);
            System.arraycopy(buf, 0, tmp, dest.length, len);
            dest = tmp;
        }
        return dest;
    }
	public ItmsFileUtils() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String s1 = toUnicodeString("张三丰");
		System.out.println(s1);
		System.out.println(decodeUnicode(s1));
		System.out.println(decodeUnicode("\ufeff\u5f20\u4e09\u4e30"));
//		readByte("D:\\jobs\\", "localTest.txt");
//		String s = readByte("D:\\jobs\\itms_file\\scl_bms_job\\", "LFCS_FD_REFER2_INIT.sql");
//		s = readByte("D:\\jobs\\itms_file\\scl_job\\", "init_ scl_Routine_work_delivered.sql");
//		
//		String keyword = "insert,into,select,name,as,from,on,where,is,group,order,by,and,not,null,delete,update,inner,drop,table,alter,having,sysdate,desc,asc,truncate,case,when,else";
//		String func = "count,left,join,sum,max,to_char,to_date,like,round,default,min,avg,first,last,upper,distinct,end";
//		String[] keywords = keyword.split(",");
//		String[] funcs = func.split(",");
//		List<String> keyword_blue = new ArrayList<String>();
//		List<String> func_red = new ArrayList<String>();
//		for(String k : keywords){
//			keyword_blue.add(k);
//		}
//		for(String f : funcs){
//			func_red.add(f);
//		}
//		
//		String[] spil = s.split(";");
//		String sqlP = "";
//		for(String sql : spil){
//			if(!StringUtils.isEmpty(sql.trim())){
//				sqlP = matcherS(sql.trim());
////				sqlP = new SQLFormatter().format(sql.trim());
//				System.out.println(sqlP.trim()+";");
//				String[] spilNull = sqlP.split(" ");
//				for(String column : spilNull){
//					if(keyword_blue.contains(column)){
//						column = "<SPAN class=keyword><b>"+column+"</b></SPAN>";
//					}else if(func_red.contains(column)){
//						column = "<SPAN class=func>"+column+"</SPAN>";
//					}else{
//						column ="<SPAN>"+column+"</SPAN>";
//					}
//					System.out.println(column);
//				}
//			}
//		}
	}
	/**多个空格替换成一个空格*/
	public static String matcherS(String str){
		if(StringUtils.isEmpty(str)){
			return null;
		}
		Pattern p = Pattern.compile("\\s+");
		Matcher m = p.matcher(str);
		return m.replaceAll(" ");
	}

}