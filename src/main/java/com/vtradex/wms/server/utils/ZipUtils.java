package com.vtradex.wms.server.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.log4j.Logger;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

/**
 * 解压工具类
 * 
 * @author <a href="mailto:ningbo.pan@vtradex.net">潘宁波</a>
 * @since 2010-7-14
 * @version v 4.0
 */
public class ZipUtils {
	
	private static Logger log = Logger.getLogger(ZipUtils.class);
	
	@SuppressWarnings("unchecked")
	public static boolean unZip(String zipPath, String storePath) {
		ZipFile zip = null;
		FileOutputStream output = null;
		InputStream input = null;
		File storeFile = new File(storePath);
		if (storeFile.exists() == false) {
			storeFile.mkdirs();
		}
		try {
			zip = new ZipFile(new File(zipPath));
			Enumeration<ZipArchiveEntry> entrys = zip.getEntries();
			while(entrys.hasMoreElements()) {
				ZipArchiveEntry entry = entrys.nextElement();
				output = new FileOutputStream(storePath + File.separator + entry.getName());
				input = zip.getInputStream(entry);
				IOUtils.copy(input, output);
				input.close();
				output.flush();
				output.close();
			}
			zip.close();
			return true;
		} catch (IOException e) {
			log.error("解压失败", e);
			return false;
		} finally {
			try {
				if (input != null) {
					input.close();
				}
				if (output != null) {
					output.flush();
					output.close();
				}
				if (zip != null) {
					zip.close();
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
				log.error("关闭解压流失败", ioe);
				return false;
			}
		}
	}
	 /**
	  * 压缩文件zip-Apache中ant所带包
	  * @功能信息 :
	  * @参数信息 :
	  * @返回值信息 :
	  * @异常信息 :
	  * */
	 public static void getZip(List list,String path,String fileName) throws Exception{
	   byte[] buffer = new byte[1024];
	    
	   String strZipName = fileName + ".zip";
	   ZipOutputStream out = new ZipOutputStream(
			   new FileOutputStream(path + strZipName));
	   for (int j = 0; j < list.size(); j++) {
		   String name = list.get(j).toString();
		   FileInputStream fis = new FileInputStream(path + name);//D:\export2011-08-21ERR.txt
		   out.putNextEntry(new ZipEntry(name));
		   int len;
		   while ((len = fis.read(buffer)) > 0) {
		     out.write(buffer, 0, len);
		   }
		   out.closeEntry();
		   fis.close();
	   }
	   out.close();
	   System.out.println("生成Demo.zip成功");
	  }
	public static void main(String[] args) {
		ZipUtils.unZip("D:\\pod\\admin-PODS.zip", "D:\\pod\\bak\\");
	}
}
