package com.vtradex.wms.server.service.interfaceLog;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.model.base.BaseStatus;
import com.vtradex.wms.server.model.base.ItmsLogType;
import com.vtradex.wms.server.model.itms.ItmsSqlDir;
import com.vtradex.wms.server.model.itms.ItmsSqlExecute;
import com.vtradex.wms.server.model.itms.ItmsSqlTaskType;
import com.vtradex.wms.server.model.itms.ItmsTask;
import com.vtradex.wms.server.model.organization.TypeOfBill;
import com.vtradex.wms.server.model.webservice.ItmsInterfaceColumn;
import com.vtradex.wms.server.model.webservice.ItmsInterfaceTable;
import com.vtradex.wms.server.model.webservice.ItmsWebArgument;
import com.vtradex.wms.server.service.email.EmailManager;
import com.vtradex.wms.server.service.reflect.ReflectMethodManager;
import com.vtradex.wms.server.utils.IpUtils;
import com.vtradex.wms.server.utils.ItmsFileUtils;
import com.vtradex.wms.server.utils.JavaTools;
import com.vtradex.wms.server.utils.MyUtils;
import com.vtradex.wms.server.utils.StringHelper;
/** yc */
public class EdiExecuteSqlImp extends DefaultBaseManager implements EdiExecuteSql{
	
	/**\r\n 换行*/
	public static String enter = "\r\n";
	public static final String method_job = "ediExecuteSql.executeSql";
	protected ItmsLogManager itmsLogManager;
	private ReflectMethodManager reflectMethodManager;
	public EdiExecuteSqlImp(ItmsLogManager itmsLogManager,ReflectMethodManager reflectMethodManager) {
		this.itmsLogManager = itmsLogManager;
		this.reflectMethodManager = reflectMethodManager;
	}
	public void executeTxtJob() throws InvocationTargetException{
		while(true){
			try {
				executeTxt();
				executeInterfaceFile();
				executeWeb();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			
			try {
				Thread.sleep(1000*20);//20'
			} catch (InterruptedException e) {
				logger.error("", e);
			}
		}
	}
	//ediExecuteSql.executeTxt
	@SuppressWarnings("unchecked")
	public void executeTxt(){
		String dir_path = ItmsSqlDir.LOC_DIR+File.separator+ItmsSqlDir.ITMS_FILE;
		//以ODD数据为准,以JOB文件名称去找sql文件,并执行
		List<ItmsSqlExecute> scheduler = new ArrayList<ItmsSqlExecute>();
		List<ItmsSqlExecute> repeat = new ArrayList<ItmsSqlExecute>();
		List<ItmsSqlExecute> sqlFiles = commonDao.findByQuery("FROM ItmsSqlExecute f WHERE f.status = 'ENABLED'");
		System.out.println("executeTxt_odd_file["+sqlFiles.size()+"]....."+JavaTools.format(new Date(), JavaTools.dmy_hms));
		for(ItmsSqlExecute f : sqlFiles){
			if(f.getIsDoScheduler()){
				scheduler.add(f);
			}else{
				repeat.add(f);
			}
		}sqlFiles.clear();
		
		String dirName = "";
		double margin = 0D;
		Date lastTime = null;
		for(ItmsSqlExecute f : repeat){
			ItmsSqlDir dir = commonDao.load(ItmsSqlDir.class,f.getDir().getId());
			dirName = dir_path+File.separator+dir.getDirName();
			
			Integer repeatTimes = f.getRepeatTimes();
			if(repeatTimes==null || repeatTimes<=0){
				continue;
			}
			lastTime = f.getLastTime();
			if(lastTime!=null){
				//比较当前日期与上一次执行时间是否在设定频率范围内
				margin = JavaTools.getDoubleMargin(JavaTools.format(new Date(),JavaTools.dmy_hms), 
						JavaTools.format(lastTime,JavaTools.dmy_hms));
			}else{//默认第一次
				margin = repeatTimes;
			}
			if(margin>=repeatTimes){
				//new task...
				ItmsTask task = itmsLogManager.saveItmsTask(dir.getDataSource(),method_job,dirName, f.getFilename(),ItmsFileUtils.GBK);
				f.setLastTime(task.getCreatedTime());
				commonDao.store(f);
			}
		}repeat.clear();
		
		for(ItmsSqlExecute f : scheduler){
			ItmsSqlDir dir = commonDao.load(ItmsSqlDir.class,f.getDir().getId());;
			dirName = dir_path+File.separator+dir.getDirName();
			
			lastTime = f.getLastTime();
			if(lastTime!=null){
				//一年中每天在指定时间段只允许执行一次,如2017-01-01执行过,那么2017-01-01不允许再次执行
				//如果没执行,那么看当前时间段是否等于指定执行时间,比如指定执行时间01点,当前时间03点,那么不执行
				margin = JavaTools.compare_date(JavaTools.format(lastTime, JavaTools.y_m_d),
						JavaTools.format(new Date(), JavaTools.y_m_d), JavaTools.y_m_d);
				if(margin>0 || margin<0){
					margin = JavaTools.compare_date(JavaTools.format(new Date(), JavaTools.hh),
							f.getSchedulerTime(), JavaTools.hh);
				}else{
					margin = 2;
				}
			}else{//默认第一次,与指定时间匹配
				margin = JavaTools.compare_date(JavaTools.format(new Date(), JavaTools.hh),
						f.getSchedulerTime(), JavaTools.hh);
			}
			if(margin==0){
				//new task...
				ItmsTask task = itmsLogManager.saveItmsTask(dir.getDataSource(),method_job,dirName, f.getFilename(),ItmsFileUtils.GBK);
				f.setLastTime(task.getCreatedTime());
				commonDao.store(f);
			}
		}scheduler.clear();
	}
	//ediExecuteSql.executeInterfaceFile
	@SuppressWarnings("unchecked")
	public void executeInterfaceFile(){
		//外部接口,通过URL直接产生文件的接口
		String dir_path = ItmsSqlDir.LOC_DIR+File.separator+ItmsSqlDir.INTERFACE_FILE;
		File interfaceDir = ItmsFileUtils.mkdir(dir_path);
		File[] interfaceFiles = interfaceDir.listFiles();
		System.out.println("executeTxt_interface_file["+interfaceFiles.length+"]....."+JavaTools.format(new Date(), JavaTools.dmy_hms));
		if (interfaceFiles != null && interfaceFiles.length > 0) {
			//当前文件夹下文件按照年月分组
			dir_path = ItmsSqlDir.LOC_DIR+File.separator+ItmsSqlDir.DATE_GROUP_FILE;
			String dateDirName = dir_path+
					File.separator+JavaTools.format(new Date(), JavaTools.ym);
			ItmsFileUtils.mkdir(dateDirName);
			String sourceFileName = "";
			for (File sourceFile : interfaceFiles) {
				if(sourceFile.isFile()){//接口文件统一接收保存路径
					sourceFileName = sourceFile.getName();//insert_test_table_170601074156360.json
					if(sourceFileName.toUpperCase().endsWith(ItmsSqlExecute.EDN_JSON)){
						String sqlParameter = ItmsFileUtils.readStrTxt(sourceFile,ItmsFileUtils.UTF_8);
						//文件命名规范:insert_tablename_ymdhms.json,insert_table_name_ymdhms.json,insert_x_y_z_ymdhms.json
						String extTableName = StringUtils.substringAfter(StringUtils.substringBeforeLast(sourceFileName,MyUtils.spilt_),
								MyUtils.spilt_);
						ItmsInterfaceTable table = null;
						List<ItmsInterfaceTable> tables = commonDao.findByQuery("FROM ItmsInterfaceTable t WHERE upper(t.extTableName) =:extTableName", 
								new String[]{"extTableName"},new Object[]{extTableName.toUpperCase()});
						if(tables==null || tables.size()<=0){
							itmsLogManager.saveItmsJobLog(ItmsLogType.ERROR, extTableName, extTableName+".is.not.defined.on.odd", 
									sqlParameter);
						}else{
							table = tables.get(0);
							if(table.getStatus().equals(BaseStatus.DISABLED)){
								itmsLogManager.saveItmsJobLog(ItmsLogType.ERROR, extTableName, extTableName+".is.not.enabled.on.odd", 
										sqlParameter);
								table = null;
							}
						}
						if(table!=null){
							List<ItmsInterfaceColumn> columns = commonDao.findByQuery("FROM ItmsInterfaceColumn c WHERE c.interfaceTable.id =:interfaceTableId", 
									new String[]{"interfaceTableId"},new Object[]{table.getId()});
							if(columns==null || columns.size()<=0){
								itmsLogManager.saveItmsJobLog(ItmsLogType.ERROR, extTableName, extTableName+".is.not.defined.column.on.odd", 
										sqlParameter);
							}else{
								String airm = StringUtils.substringBefore(sourceFileName,MyUtils.spilt_);
								StringBuffer row = new StringBuffer();
								String sql1 = "",sql2 = "";
								if(TypeOfBill.INSERT.equals(airm)){
									sql1 = "INSERT INTO "+extTableName+" (";
									sql2 = " VALUES (";
									JSONObject jsonObject = JSONObject.fromObject(sqlParameter);
									for(ItmsInterfaceColumn c : columns){
										sql1 += c.getJsonC()+MyUtils.comma;
										sql2 += "'"+jsonObject.getString(c.getJsonC())+"'"+MyUtils.comma;
									}
									sql1 = StringUtils.substringBeforeLast(sql1,MyUtils.comma);
									sql2 = StringUtils.substringBeforeLast(sql2,MyUtils.comma);
									sql1 += ")";
									sql2 += ")";
									row.append(sql1).append(sql2);
//									System.out.println(row.toString());
								}else if(TypeOfBill.DELETE.equals(airm)){
									
								}else if(TypeOfBill.UPDATE.equals(airm)){
									
								}else if(TypeOfBill.SELECT.equals(airm)){
									
								}
								createInterfaceFile(table.getDataSource(), dateDirName, sourceFileName, row.toString());
							}
						}
						//move
						File destFile = new File(dateDirName+File.separator+sourceFileName);
						ItmsFileUtils.moveSrcFile(sourceFile, destFile);
					}else if(sourceFileName.toUpperCase().endsWith(ItmsSqlExecute.EDN_SQL)){
						File destFile = new File(dateDirName+File.separator+sourceFileName);
						ItmsFileUtils.moveSrcFile(sourceFile, destFile);
						//new task...
						itmsLogManager.saveItmsTask(ItmsSqlTaskType.EDI_SQL_FILE,method_job,dateDirName, sourceFileName,ItmsFileUtils.UTF_8);
					}
				}
			}
		}
	}
	/**目标路径创建.sql文件+产生task任务*/
	private void createInterfaceFile(String dataSource,String dateDirName,String sourceFileName,String sqlParameter){
		sourceFileName = StringUtils.substringBefore(sourceFileName,MyUtils.spot);
		String file = dateDirName+File.separator+sourceFileName+MyUtils.spot+"sql";
		StringBuffer row = new StringBuffer();
		row.append(sqlParameter);
		ItmsFileUtils.createTxt(file, row, ItmsFileUtils.UTF_8);
		//new task...
		itmsLogManager.saveItmsTask(dataSource,method_job,dateDirName, sourceFileName+MyUtils.spot+"sql",ItmsFileUtils.UTF_8);
	}
	//ediExecuteSql.executeWeb
	@SuppressWarnings("unchecked")
	public void executeWeb(){
		String hql = "FROM ItmsWebArgument c WHERE c.repeatTimes is not null and c.repeatTimes>0 and c.status = 'ENABLED'";
		List<ItmsWebArgument> cc = commonDao.findByQuery(hql);
		int size = cc==null?0:cc.size();
		System.out.println("executeWeb["+size+"]....."+JavaTools.format(new Date(), JavaTools.dmy_hms));
		if(size>0){
			for(ItmsWebArgument c : cc){
				Integer repeatTimes = 0;
				double margin = 0D;
				
				repeatTimes = c.getRepeatTimes();
				Date lastTime = c.getLastTime();
				if(lastTime!=null){
					//比较当前日期与上一次执行时间是否在设定频率范围内
					margin = JavaTools.getDoubleMargin(JavaTools.format(new Date(),JavaTools.dmy_hms), 
							JavaTools.format(lastTime,JavaTools.dmy_hms));
				}else{//默认第一次
					margin = repeatTimes;
				}
				if(margin>=repeatTimes){
					ItmsTask task = itmsLogManager.saveItmsTask(ItmsSqlTaskType.EDI_JOB_WEB, WebExecuteDataImp.BEAN+"."+WebExecuteDataImp.SEND_JSON, 
							c.getId(),c.getExtTableName());
					
					c.setLastTime(task.getCreatedTime());
					commonDao.store(c);
				}
			}
		}
	}
	
	public void executeSql(Long taskId){
		ItmsTask task = commonDao.load(ItmsTask.class, taskId);
		String fileName = null;
		String sqlParameter = null;
		
		Boolean isError = false;
		String errorMes = "";
		try {
			fileName = task.getExtend2();//StringUtils.substringAfterLast(task.getType(), File.separator);
			File file = new File(task.getExtend1()+File.separator+fileName);
			if(!file.exists() || !file.isFile()){
				isError = true;
				errorMes = "指定文件不存在:"+task.getExtend1()+File.separator+fileName;
			}
			if(!isError){
				String type = task.getType();//type匹配数据源
				sqlParameter = ItmsFileUtils.readStrTxt(file,task.getExtend3()==null?ItmsFileUtils.GBK:task.getExtend3());
				String[] sqls = sqlParameter.split(";");
				if(sqls.length>0){
					for(String sql : sqls){
						if(!StringUtils.isEmpty(sql)){
							reflectMethodManager.invokeMethod(JdbcExtendDataExtImp.BEAN+"."+type+JdbcExtendDataExtImp.EXT,sql);
						};
					}
				}else{
					isError = true;
					errorMes = "SQL	is null";
				}
			}
		} catch (Exception e) {
			isError = true;
			errorMes = e.getMessage();
		}finally{
			if(isError){
				Boolean state = errorMes.contains("Io 异常") || errorMes.contains("Socket read timed out") 
						|| errorMes.contains("Already closed") || errorMes.contains("Cannot open connection");
				if(!state){//不属于网络问题
					try {
						reflectMethodManager.invokeMethod(EmailManager.BEAN+EmailManager.SEND_SQL_EXCEPTION,
								fileName+MyUtils.spilt1+errorMes+MyUtils.spilt1+sqlParameter);
					}catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
//				ItmsJobLog log = itmsLogManager.saveItmsJobLog(ItmsLogType.ERROR, fileName, errorMes, 
//						sqlParameter);
//				//发邮件task
//				itmsLogManager.saveItmsTask(ItmsSqlTaskType.EDI_JOB_EMAIL,EmailManager.BEAN+EmailManager.SEND_JOB_EXCEPTION, log.getId(),fileName);
			}
//			else{
//				itmsLogManager.saveItmsJobLog(ItmsLogType.SUCCESS, fileName, errorMes, 
//						sqlParameter);
//			}
		}
	}
	/**
	airm:代表本次访问目的(insert,delete,update,select);
	fileName:代表访问表名称;
	fileSql:代表访问内容(
		insert=={"key1":"value01","key2":"value02","key3":"value03"}
		delete,update,select=={"content":{"c1":"v1","c2":"v2"},"condition":{"k1":"v1","k2":"v2"}}
		,JSON格式,其中key代表访问表的字段,value代表访问表的字段值);
	insert:代表直接将值插入对应字段;
	delete:代表根据字段值删除数据;
	update:代表根据字段值更新数据;
	select:代表根据字段值查询数据,返回该表的所有字段值;
	内容全部 decodeUnicode
	 * */
	public String getSqlParame(Object[] objs){
		String airm = (String) objs[0];//TypeOfBill
		String fileName = (String) objs[1];
		String fileSql = (String) objs[2];
		
		if(StringHelper.in(airm, new String[]{
				TypeOfBill.INSERT,TypeOfBill.DELETE,TypeOfBill.UPDATE,TypeOfBill.SELECT
		})){
			return getSqlParame(airm,fileName, fileSql);
		}else{
			return "airm is not defined";
		}
	}
	public String getSqlParame(String airm,String fileName,String fileSql){
		fileSql = ItmsFileUtils.decodeUnicode(fileSql);
		if(StringUtils.isEmpty(fileName) || fileName.toUpperCase().equals("NULL")){
			return "fileName is null";
		}else if(StringUtils.isEmpty(fileSql) || fileSql.toUpperCase().equals("NULL")){
			return "fileSql is null";
		}
		
		String path = ItmsSqlDir.LOC_DIR;
		ItmsFileUtils.mkdir(path);
		path += File.separator+ItmsSqlDir.INTERFACE_FILE;
		ItmsFileUtils.mkdir(path);
		String createDate = new SimpleDateFormat("yyMMddHHmmssSSS").format(new Date());
		
		String file = path+File.separator+airm+MyUtils.spilt_+fileName+MyUtils.spilt_+createDate+MyUtils.spot+"json";
		StringBuffer row = new StringBuffer();
		row.append(fileSql);
		ItmsFileUtils.createTxt(file, row, ItmsFileUtils.UTF_8);
		
		return ItmsServletType.SUCCESS;
	}
	public String getSqlParame(String fileName,String fileSql){
		fileSql = ItmsFileUtils.decodeUnicode(fileSql);
		if(StringUtils.isEmpty(fileName) || fileName.toUpperCase().equals("NULL")){
			return "file_Name is null";
		}else if(StringUtils.isEmpty(fileSql) || fileSql.toUpperCase().equals("NULL")){
			return "file_Sql is null";
		}
		String path = ItmsSqlDir.LOC_DIR+File.separator+ItmsSqlDir.INTERFACE_FILE;
		ItmsFileUtils.mkdir(path);
		String createDate = new SimpleDateFormat("yyMMddHHmmssSSS").format(new Date());
		String file = path+File.separator+fileName+MyUtils.spilt_+createDate+MyUtils.spot+"sql";
		
		List<Object[]> values = new ArrayList<Object[]>();
		String[] ss = fileSql.toString().split(";");
		for(String s : ss){
			values.add(new Object[]{s+";"});
		}
		ItmsFileUtils.createTxt(values,1,file,"\r\n","",ItmsFileUtils.UTF_8);
		values.clear();
		return ItmsServletType.SUCCESS;
	}
	public void checkIpJob(){
		while(true){
			InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("itms.properties");
			Properties p = new Properties();
			boolean state = true;
			try {
				p.load(inputStream);
				String ip = p.getProperty("dataip");
				System.out.println("ediExecuteSql[checkIpJob]["+ip+"]-----"+JavaTools.format(new Date(), JavaTools.dmy_hms));
				state = IpUtils.iPTest(ip);
			} catch (UnknownHostException e1) {
				state = false;
			}catch (IOException e) {
				state = false;
			}
			Long sleep = 1000*60L;//60'
			if(!state){
				try {
					reflectMethodManager.invokeMethod(EmailManager.BEAN+EmailManager.SEND_NET_EXCEPTION);
				}catch (Exception e) {
					System.out.println(e.getMessage());
				}
				sleep = 1000*60*2L;
			}
			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				logger.error("", e);
			}
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String s = "安徽省合肥市蜀山区 距金寨南路/石门路(路口)约465米 安徽省合肥市蜀山区经开区繁华大道315号(欢乐岛西侧)南约121米处";
//		String s1 = JavaTools.encoder("''");
//		String s2 = JavaTools.decoder(s1);
//		System.out.println(s1+"\n"+s2);
		String theString = ItmsFileUtils.toUnicodeString(s);
		System.out.println(theString);
		System.out.println(ItmsFileUtils.decodeUnicode(theString));
	}
}
//File itmsDir = new File(dir_path);
//if (!itmsDir.exists()) {
//	itmsDir.mkdirs();
//}
//File[] itmsDirs = itmsDir.listFiles();
//System.out.println("executeTxt_odd_file["+itmsDirs.length+"]....."+JavaTools.format(new Date(), JavaTools.dmy_hms));
//if (itmsDirs != null && itmsDirs.length > 0) {
//	String sourceFileName = "",dirName = "";
//	for (File sourceDir : itmsDirs) {
//		if(sourceDir.isDirectory()){//用自定义文件夹方式区分业务
//			File[] sourceFiles = sourceDir.listFiles();
//			if (sourceFiles == null || sourceFiles.length <= 0) {
//				continue;
//			}
//			dirName = sourceDir.getAbsolutePath();
//			for (File sourceFile : sourceFiles) {
//				if (sourceFile.isFile()) {//目前暂支持到2级目录
//					sourceFileName = sourceFile.getName();
//					Integer repeatTimes = 0;
//					double margin = 0D;
//					//获取文件对应的频率,找不到ItmsSqlExecute或者维护频率为空或者小于等于0的都不执行
//					String hql = "FROM ItmsSqlExecute f WHERE f.filename =:filename";
//					ItmsSqlExecute f = (ItmsSqlExecute) commonDao.findByQueryUniqueResult(hql, 
//							new String[]{"filename"}, new Object[]{sourceFileName});
//					if(f!=null){
//						repeatTimes = f.getRepeatTimes();
//					}
//					if(repeatTimes==null || repeatTimes<=0){
//						continue;
//					}
//					Date lastTime = f.getLastTime();
//					if(lastTime!=null){
//						//比较当前日期与上一次执行时间是否在设定频率范围内
//						margin = JavaTools.getDoubleMargin(JavaTools.format(new Date(),JavaTools.dmy_hms), 
//								JavaTools.format(lastTime,JavaTools.dmy_hms));
//					}else{//默认第一次
//						margin = repeatTimes;
//					}
//					if(margin>=repeatTimes){
//						ItmsSqlDir dir = commonDao.load(ItmsSqlDir.class, f.getDir().getId());
//						//new task...
//						ItmsTask task = itmsLogManager.saveItmsTask(dir.getDataSource(),method_job,dirName, sourceFileName,ItmsFileUtils.GBK);
//						f.setLastTime(task.getCreatedTime());
//						commonDao.store(f);
//					}
//				}
//			}
//		}
//	}
//}
