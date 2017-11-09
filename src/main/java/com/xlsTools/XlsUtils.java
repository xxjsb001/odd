package com.xlsTools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Pattern;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.read.biff.BiffException;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import jxl.write.Number;
/**yc min*/
public class XlsUtils{
	/**border:是否加边框*/
	public static void writeXls(String[] title, Integer[] length, String filePath, 
			List<Object[]> objs,Boolean border)
	  {
	    WritableWorkbook book = null;
	    try {
	      book = Workbook.createWorkbook(new File(filePath));
	      WritableSheet sheet1 = book.createSheet("sheet1", 0);
	      WritableCellFormat titleFormat = getTitleFormat0(10);
	      int line = 0;
	      for (int i = line; i < title.length; i++) {
	        Label excelTitle = new Label(i - line, line, title[(i - line)], titleFormat);
	        sheet1.addCell(excelTitle);
	        sheet1.setColumnView(i, length[i].intValue());
	      }
	      line++;
	      
	      WritableFont wfont = new WritableFont(WritableFont.ARIAL, 10, 
			      WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, 
			      Colour.BLACK);
	      WritableCellFormat columnFormat = getColumnFormat(wfont,border,Boolean.FALSE);
	      for (int i = 0; i < objs.size(); i++) {
	        Object[] obj = (Object[])objs.get(i);
	        for (int j = 0; j < obj.length; j++) {
	        	if(isNumeric1(obj[j].toString())){
	        		//整型数据
		            Number label2 = new Number(j, line, Double.valueOf(obj[j].toString()), columnFormat);
		            sheet1.addCell(label2);
	        	}else{
	        		Label lab = new Label(j, line, obj[j].toString(), columnFormat);
	  	          	sheet1.addCell(lab);
	        	}
	        }
	        line++;
	      }
	    }catch (IOException e) {
			e.printStackTrace();
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}finally{
			try {
				if ((book != null) && (book.getSheets().length > 0)) {
					book.write();
					book.close();
				}
			} catch (WriteException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	  }
	/**border:是否加边框,mergeCell:是否合并,wrapMap:是否换行**/
	public static void writeXls(String[] title1,String[] title2,Integer[] length, String filePath,
			List<Object[]> objs,Boolean border,Boolean mergeCell,Map<Integer,Alignment> aligns,int width,
			Map<Integer,Boolean> wrapMap)
	  {
	    WritableWorkbook book = null;
	    try {
	      book = Workbook.createWorkbook(new File(filePath));
	      WritableSheet sheet1 = book.createSheet("sheet1", 0);
	      WritableCellFormat titleFormat = getTitleFormat0(width);
	      int line = 0;
	      for (int i = line; i < title1.length; i++) {
	        Label excelTitle = new Label(i - line, line, title1[(i - line)], titleFormat);
	        sheet1.addCell(excelTitle);
	        sheet1.setColumnView(i,length[i].intValue());
	      }
	      if(mergeCell && title2!=null && title2.length>0){
	    	  //col1:合并后位置所在列,row1:合并后位置所在行,col2:合并到位置所在列,row2:合并到位置所在行
	    	  sheet1.mergeCells(line, line, title2.length-1, line);
	      }
	      if(title1!=null && title1.length>0){//title1有值时才换行
	    	  line++;
	      }
	      for(int i = 0; i < title2.length; i++) {
	    	  sheet1.setColumnView(i, length[i].intValue());
	    	  Label excelTitle = new Label(i, line, title2[(i)], titleFormat);
	    	  sheet1.addCell(excelTitle);
		  }
	      if(title2!=null && title2.length>0){//title2有值时才换行
	    	  line++;
	      }
	      //定义每一列的format
	      Map<Integer,WritableCellFormat> colAligns = new HashMap<Integer, WritableCellFormat>();
	      WritableFont wfont = new WritableFont(WritableFont.createFont("宋体"), width, 
			      WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, 
			      Colour.BLACK);
	      for (int i = 0; i < objs.size(); i++) {
	    	  Object[] obj = (Object[])objs.get(i);
	    	  for (int j = 0; j < obj.length; j++) {
	    		  if(aligns!=null && aligns.size()>0 && aligns.get(j)!=null){
	    			  colAligns.put(j, getColumnFormat(wfont, border, aligns.get(j),wrapMap==null?false:wrapMap.containsKey(j)));
	    		  }else{
	    			  colAligns.put(j, getColumnFormat(wfont,border,wrapMap==null?false:wrapMap.containsKey(j)));
	    		  }
	    	  }
	    	  break;
	      }
	      //设置每一列的format
	      for (int i = 0; i < objs.size(); i++) {
	    	  Object[] obj = (Object[])objs.get(i);
	    	  for (int j = 0; j < obj.length; j++) {
	    		  if(isNumeric1(obj[j]==null?"-":obj[j].toString())){
	    			  //整型数据
	    			  Number label2 = new Number(j, line, Double.valueOf(obj[j].toString()), colAligns.get(j));//, colAligns.get(j)
	    			  sheet1.addCell(label2);
	    		  }else{
	    			  Label lab = new Label(j, line, obj[j]==null?"-":obj[j].toString(), colAligns.get(j));//, colAligns.get(j)
	  	          	  sheet1.addCell(lab);
	    		  }
	    		  sheet1.setColumnView(j, length[j].intValue());
	    	  }
	    	  line++;
	      }
	      //设置自动大小
//	      CellView cellView = new CellView();  
//	      cellView.setAutosize(true);
	      //设置字体为Arial，30号，加粗  
//	      byte[] bstrLength = new String("汉字的长度是英文的2倍").getBytes(); //中文字符算两个字节
//	      Label label = new Label(7, 7, "汉字的长度是英文的2倍");  
//	      sheet1.addCell(label);
//	      sheet1.setColumnView(7, cellView);//根据内容自动设置列宽 
	      
	      //设置字体为Arial，30号，加粗  
//	      label = new Label(6, 5, "汉字的长度是英文的2倍");  
//	      sheet1.addCell(label);  
//	      sheet1.setColumnView(6, bstrLength.length);  
	    }catch (IOException e) {
			e.printStackTrace();
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}finally{
			try {
				if ((book != null) && (book.getSheets().length > 0)) {
					book.write();
					book.close();
				}
			} catch (WriteException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	  }
	
	/**border:是否加边框,mergeCell:是否合并,wrapMap:是否换行,additional:是否追加(在原有excel上增加sheet)
	 * (支持顶部头标题分栏,自定义sheet名称,文件追加sheet)**/
	public static void writeXlsT(String[] title1,String[] title2,Integer[] length, String filePath,
			List<Object[]> objs,Boolean border,Boolean mergeCell,Map<Integer,Alignment> aligns,int width,
			Map<Integer,Boolean> wrapMap,Map<Integer,Integer> title1C,String sheetname,int sheetIndex,
			Boolean additional)
	  {
		Workbook wb = null;
	    WritableWorkbook book = null;
	    try {
	    	if(additional){
	    		wb = Workbook.getWorkbook(new File(filePath));
	    		book = Workbook.createWorkbook(new File(filePath),wb);
	    	}else{
	    		book = Workbook.createWorkbook(new File(filePath));
	    	}
	      
	      WritableSheet sheet1 = book.createSheet(sheetname, sheetIndex);
	      WritableCellFormat titleFormat = getTitleFormat0(width);
	      
	      int line = 0;
	      if(title1C!=null && title1C.size()>0){
	    	  int c = 0;
	    	  Iterator<Entry<Integer, Integer>> iter = title1C.entrySet().iterator();
	    	  while(iter.hasNext()){
	    		  Entry<Integer, Integer> e = iter.next();
	    		  Label excelTitle = new Label(e.getKey(), line, title1[c], titleFormat);
		          sheet1.addCell(excelTitle);
		          c++;
	    	  }
	      }
	      
	      if(mergeCell && title2!=null && title2.length>0){
	    	  //col1:合并后位置所在列,row1:合并后位置所在行,col2:合并到位置所在列,row2:合并到位置所在行
//	    	  sheet1.mergeCells(line, line, title2.length-1, line);
	    	  if(title1C!=null && title1C.size()>0){
	    		  Iterator<Entry<Integer, Integer>> iter = title1C.entrySet().iterator();
		    	  while(iter.hasNext()){
		    		  Entry<Integer, Integer> e = iter.next();
		    		  sheet1.mergeCells(e.getKey(), line, e.getValue(), line);
		    	  }
	    	  }
	      }
	      if(title1!=null && title1.length>0){//title1有值时才换行
	    	  line++;
	      }
	      for(int i = 0; i < title2.length; i++) {
	    	  sheet1.setColumnView(i, length[i].intValue());
	    	  Label excelTitle = new Label(i, line, title2[(i)], titleFormat);
	    	  sheet1.addCell(excelTitle);
		  }
	      if(title2!=null && title2.length>0){//title2有值时才换行
	    	  line++;
	      }
	      //定义每一列的format
	      Map<Integer,WritableCellFormat> colAligns = new HashMap<Integer, WritableCellFormat>();
	      WritableFont wfont = new WritableFont(WritableFont.createFont("宋体"), width, 
			      WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, 
			      Colour.BLACK);
	      for (int i = 0; i < objs.size(); i++) {
	    	  Object[] obj = (Object[])objs.get(i);
	    	  for (int j = 0; j < obj.length; j++) {
	    		  if(aligns!=null && aligns.size()>0 && aligns.get(j)!=null){
	    			  colAligns.put(j, getColumnFormat(wfont, border, aligns.get(j),wrapMap==null?false:wrapMap.containsKey(j)));
	    		  }else{
	    			  colAligns.put(j, getColumnFormat(wfont,border,wrapMap==null?false:wrapMap.containsKey(j)));
	    		  }
	    	  }
	    	  break;
	      }
	      //设置每一列的format
	      for (int i = 0; i < objs.size(); i++) {
	    	  Object[] obj = (Object[])objs.get(i);
	    	  for (int j = 0; j < obj.length; j++) {
	    		  if(isNumeric(obj[j]==null?"-":obj[j].toString())){
	    			  //整型数据
	    			  Number label2 = new Number(j, line, Double.valueOf(obj[j].toString()), colAligns.get(j));//, colAligns.get(j)
	    			  sheet1.addCell(label2);
	    		  }else{
	    			  Label lab = new Label(j, line, obj[j]==null?"-":obj[j].toString(), colAligns.get(j));//, colAligns.get(j)
	  	          	  sheet1.addCell(lab);
	    		  }
	    		  sheet1.setColumnView(j, length[j].intValue());
	    	  }
	    	  line++;
	      }
	      //设置自动大小
//	      CellView cellView = new CellView();  
//	      cellView.setAutosize(true);
	      //设置字体为Arial，30号，加粗  
//	      byte[] bstrLength = new String("汉字的长度是英文的2倍").getBytes(); //中文字符算两个字节
//	      Label label = new Label(7, 7, "汉字的长度是英文的2倍");  
//	      sheet1.addCell(label);
//	      sheet1.setColumnView(7, cellView);//根据内容自动设置列宽 
	      
	      //设置字体为Arial，30号，加粗  
//	      label = new Label(6, 5, "汉字的长度是英文的2倍");  
//	      sheet1.addCell(label);  
//	      sheet1.setColumnView(6, bstrLength.length);  
	    }catch (IOException e) {
			e.printStackTrace();
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if ((book != null) && (book.getSheets().length > 0)) {
					book.write();
					book.close();
				}
			} catch (WriteException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	  }
	  public static WritableCellFormat getTitleFormat(int width) {
		  
		  WritableFont wfont = new WritableFont(WritableFont.ARIAL, width, 
				      WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, 
				      Colour.BLACK);
		  WritableCellFormat titleFormat = new WritableCellFormat(wfont);
		  try {
		      titleFormat.setAlignment(Alignment.CENTRE);
		  } catch (WriteException e) {
		      e.printStackTrace();
		  }
		  return titleFormat; 
	 }
	  //border:是加边框
	  public static WritableCellFormat getColumnFormat(WritableFont wfont,Boolean border,Boolean isWrap) {
		  
//		  WritableFont wfont = new WritableFont(WritableFont.ARIAL, width, 
//				      WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, 
//				      Colour.BLACK);
		  WritableCellFormat titleFormat = new WritableCellFormat(wfont);
		  try {
			  titleFormat.setAlignment(Alignment.CENTRE);
			  //是否自动换行
		      titleFormat.setWrap(isWrap);
		      if(border){
		    	  titleFormat.setBorder(Border.ALL, BorderLineStyle.THIN);//边框
		      }
		  } catch (WriteException e) {
		      e.printStackTrace();
		  }
		  return titleFormat; 
	 }
	  //border:是加边框,
	  public static WritableCellFormat getColumnFormat(WritableFont wfont,Boolean border,
			  Alignment alig,Boolean isWrap) {
		  WritableCellFormat titleFormat = new WritableCellFormat(wfont);
		  try {
			  titleFormat.setAlignment(alig);//Alignment.CENTRE
			  //是否自动换行
		      titleFormat.setWrap(isWrap);
		      if(border){
		    	  titleFormat.setBorder(Border.ALL, BorderLineStyle.THIN);//边框
		      }
		  } catch (WriteException e) {
		      e.printStackTrace();
		  }
		  return titleFormat; 
	 } 
	
	  public static WritableCellFormat getTitleFormat0(int width) {
		    WritableFont wfont0 = new WritableFont(WritableFont.createFont("宋体"), width, 
				  WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, 
				  Colour.BLACK);
		    WritableCellFormat titleFormat0 = new WritableCellFormat(wfont0);
		    try {
		      titleFormat0.setAlignment(Alignment.CENTRE);
		      //是否自动换行
		      titleFormat0.setWrap(true);
		      titleFormat0.setBackground(Colour.GRAY_25);
		      titleFormat0.setBorder(Border.ALL, BorderLineStyle.THIN);
		    } catch (WriteException e) {
		      e.printStackTrace();
		    }
		    return titleFormat0;
	  }
	  public static boolean isNumeric1(String str){
		  Pattern pattern = Pattern.compile("[0-9]*");
		  return pattern.matcher(str).matches();
	  }
	  public static boolean isNumeric(String str){
		  Pattern pattern = Pattern.compile("^\\d+$|-\\d+$"); // 就是判断是否为整数
		  Pattern pattern2 = Pattern.compile("\\d+\\.\\d+$|-\\d+\\.\\d+$");//判断是否为小数
		  return (pattern.matcher(str).matches() || pattern2.matcher(str).matches());
	  }
	  //生成excel文件
	  public static void  writeExcel() throws IOException{
	     try{
	             String Divpath = "d:\\ledShow";//文件保存路径
	          File dirFile = new File(Divpath);
	          if(!dirFile.exists()){//文件路径不存在时，自动创建目录
	            dirFile.mkdir();
	          }
	          String path = Divpath+"\\test.xls";//文件名字
	         //创建一个可写入的excel文件对象
	             WritableWorkbook workbook = Workbook.createWorkbook(new File(path));
	            //使用第一张工作表，将其命名为“测试”
	             WritableSheet sheet = workbook.createSheet("测试", 0);
	                                
	             //设置字体种类和格式
	              WritableFont bold = new WritableFont(WritableFont.ARIAL, 16, WritableFont.BOLD);
	              WritableCellFormat wcfFormat = new WritableCellFormat(bold);
	              wcfFormat.setAlignment(jxl.format.Alignment.CENTRE);//单元格中的内容水平方向居中
	             
	             //单元格是字符串格式！第一个是代表列数,第二是代表行数，第三个代表要写入的内容,第四个代表字体格式  （0代表excel的第一行或者第一列）   
	              Label label01 = new Label(0, 0, "测试数据：",wcfFormat); //这里的（0,0）表示第一行第一列的表格       
	              sheet.addCell(label01);
	              Label label02 = new Label(1, 0, "测试的结果是成功的");            
	              sheet.addCell(label02);
	             
	             //合并单元格,合并既可以是横向的，也可以是纵向的       
	          //这里的第一个数据代表第二列，第二个数据代表第一行，第三个数据代表第四列，第四个数据代表第二行
	           sheet.mergeCells(1, 0, 3, 1);
	           //设置第2行的高度
	              sheet.setRowView(1,400,false);       
	              //设置列宽
	              sheet.setColumnView(0, 15);
	              sheet.setColumnView(1, 40);
	             
	             //插入图片
	              File file=new File("d:\\ledShow\\test.png");            
	             //WritableImage前面四个参数的类型都是double，依次是 x, y, width, height,这里的宽和高可不是图片的宽和高，而是图片所要占的单位格的个数
	              WritableImage image=new WritableImage(1, 3, 1, 3,file);
	              sheet.addImage(image);
	             
	              //整型数据
	              Number label2 = new Number(0, 1,31415926);
	              sheet.addCell(label2);
	            
	             //添加带有formatting的Number对象
	              NumberFormat nf = new NumberFormat("#.##");
	              WritableCellFormat wcfN = new WritableCellFormat(nf);
	              Number labelNF = new Number(0, 3, 3.1415926, wcfN);
	              sheet.addCell(labelNF);
	              //boolean型数据
	              jxl.write.Boolean label3 = new jxl.write.Boolean(0,4,true);
	              sheet.addCell(label3);
	            
	              //添加DateTime对象
	              DateTime labelDT = new DateTime(0, 5, new Date());
	              sheet.addCell(labelDT);
	              //添加带有formatting的DateFormat对象
	              DateFormat df = new DateFormat("yyyy-MM-dd HH:mm:ss"); //HH是24小时制，hh是12小时制
	              WritableCellFormat wcfDF = new WritableCellFormat(df);
	              DateTime labelDTF = new DateTime(4, 1, new Date(), wcfDF);
	              sheet.addCell(labelDTF);
	              //关闭对象，释放资源
	              workbook.write();
	              workbook.close();
	            
	     }catch(Exception e){
	            e.printStackTrace();
	          }
	   
	  }
	  public static void main(String[] args) {
		String[] title1 = {"title1","title2","title3"};//"title1"
		String[] title2 = {"column1","column2","column3","column4","column5"};
		Integer[] length = {8,8,8,8,8};//10,5,5,5
		String filePath = "D:/ledShow/test.xls";
		//指定列对齐方式
		Map<Integer,Alignment> aligns = new HashMap<Integer, Alignment>();
		aligns.put(0, Alignment.LEFT);
		//是否自动换行
		Map<Integer,Boolean> wrapMap = new HashMap<Integer, Boolean>();
		wrapMap.put(0, Boolean.TRUE);
		wrapMap.put(1, Boolean.TRUE);
		//头标题占列<起始列,截止列>,0开头
	     Map<Integer,Integer> title1C = new TreeMap<Integer, Integer>();
	     title1C.put(0, 1);
	     title1C.put(2, 3);
	     title1C.put(4, 4);
		//指定自适应大小列
//		Map<Integer,Boolean> autosizes = new HashMap<Integer, Boolean>();
//		autosizes.put(0, Boolean.TRUE);
//		autosizes.put(1, Boolean.TRUE);
//		autosizes.put(2, Boolean.TRUE);
		
		List  objs = new ArrayList();
		for(int i = 0 ; i<title2.length ;i++){
			objs.add(new Object[]{
					title2[0]+i,title2[1]+i,title2[2]+i,title2[3]+i,title2[4]+i
			});
		}
//		XlsUtils.writeXls(title2, length, filePath, objs, true);
		XlsUtils.writeXlsT(title1,title2,length,filePath,objs,true,true,aligns,12,null,title1C,"1",0,false);
		XlsUtils.writeXlsT(title1,title2,length,filePath,objs,true,true,aligns,12,null,title1C,"2",1,true);
		  
//	  try {
//	   writeExcel();
//	  } catch (IOException e) {
//	   e.printStackTrace();
//	  }
		System.out.println("...............");
	}
}