package com.vtradex.wms.server.service.files.pojo;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFileChooser;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.tools.ant.util.FileUtils;
import org.springframework.context.ApplicationContextAware;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.exception.OriginalBusinessException;
import com.vtradex.thorn.server.model.Entity;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.model.exception.ExceptionLog;
import com.vtradex.thorn.server.model.message.TaskStatus;
import com.vtradex.thorn.server.model.security.User;
import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.util.BeanUtils;
import com.vtradex.thorn.server.util.LocalizedMessage;
import com.vtradex.thorn.server.web.security.UserHolder;
import com.vtradex.wms.client.businessObject.BusinessNode;
import com.vtradex.wms.client.filePage.EditInitServiceFile;
import com.vtradex.wms.client.filePage.GridGroupingSampleFiles;
import com.vtradex.wms.server.model.base.BaseStatus;
import com.vtradex.wms.server.model.base.Contact;
import com.vtradex.wms.server.model.inventory.ItmsFiles;
import com.vtradex.wms.server.model.itms.ItmsInterfaceEmailRule;
import com.vtradex.wms.server.model.itms.ItmsSqlDir;
import com.vtradex.wms.server.model.itms.ItmsSqlExecute;
import com.vtradex.wms.server.model.itms.ItmsSqlTaskType;
import com.vtradex.wms.server.model.itms.JacProjectUrl;
import com.vtradex.wms.server.model.move.WmsMoveDocType;
import com.vtradex.wms.server.model.warehouse.WmsLocationStatus;
import com.vtradex.wms.server.model.warehouse.WmsLocationType;
import com.vtradex.wms.server.model.warehouse.ItmsWarehouse;
import com.vtradex.wms.server.model.warehouse.ItmsSid;
import com.vtradex.wms.server.service.GlobalParamUtils;
import com.vtradex.wms.server.service.base.ItmsLocationManager;
import com.vtradex.wms.server.service.bean.WarehouseInfoInClient;
import com.vtradex.wms.server.service.email.EmailManager;
import com.vtradex.wms.server.service.files.ItmsFilesManager;
import com.vtradex.wms.server.service.interfaceLog.EdiExecuteSqlImp;
import com.vtradex.wms.server.service.interfaceLog.ItmsLogManager;
import com.vtradex.wms.server.utils.DateUtil;
import com.vtradex.wms.server.utils.DoubleUtils;
import com.vtradex.wms.server.utils.IpUtils;
import com.vtradex.wms.server.utils.ItmsFileUtils;
import com.vtradex.wms.server.utils.JavaTools;
import com.vtradex.wms.server.utils.MyUtils;
import com.vtradex.wms.server.utils.SQLFormatter;
import com.vtradex.wms.server.utils.WmsStringUtils;
import com.vtradex.wms.server.web.filter.ItmsLogInIpHolder;
import com.vtradex.wms.server.web.filter.ItmsWarehouseHolder;

@SuppressWarnings("all")
public class DefaultItmsFilesManager extends DefaultBaseManager implements
		ItmsFilesManager, ApplicationContextAware {
	/**500*/
	public static Integer PAGE_NUMBER = 500;
	protected WorkflowManager workflowManager;
	protected ItmsLocationManager locationManager;
	protected ItmsLogManager itmsLogManager;

	public DefaultItmsFilesManager(WorkflowManager workflowManager,ItmsLocationManager locationManager,
			ItmsLogManager itmsLogManager) {
		this.workflowManager = workflowManager;
		this.locationManager = locationManager;
		this.itmsLogManager = itmsLogManager;
	}

	public DefaultItmsFilesManager() {
	}
	
	public void importFileBySmb(File file,String fileType,String note){
		String fileName = JavaTools.decoder(file.getName());
		String newFileName = StringUtils.substringAfter(StringUtils.substringBeforeLast(fileName, "-"),"-");
		ItmsWarehouse service = ItmsWarehouseHolder.getWmsWarehouse();
		String ip = service.getCode();
		Contact contact = service.getContact();
		String user = contact.getContactName();
		String pass = contact.getAddress();
		//move
		File destFile = moveFileOther(file, newFileName);
		//copy to services
		//smb://administrator:Passw0rd@192.168.10.92/vtradex/test
		String remotePath = ip+ItmsFiles.DIR;
		ItmsFileUtils.smbPut("smb://"+user+":"+pass+"@"+remotePath, destFile.getPath());
		destFile.delete();
		//save
		ItmsFiles mis = null;
		List<Long> thids = commonDao.findByQuery("SELECT th.id FROM ItmsFiles th WHERE th.supCode =:supCode", 
				new String[]{"supCode"}, new Object[]{newFileName});
		if(thids!=null && thids.size()>0){
			mis  = commonDao.load(ItmsFiles.class, thids.get(0));
		}else{
			mis  = EntityFactory.getEntity(ItmsFiles.class);
		}
		mis.setSupCode(newFileName);//文件名称
		mis.setNote(note);//文件概要
		mis.setType(fileType);//文件类型
		mis.setSupName(remotePath);//文件路径
		mis.setWarehouse(service);//上传服务器
		mis.setStatus(BaseStatus.ENABLED);
		commonDao.store(mis);
	}
	private File moveFileOther(File file,String newFileName){
		String otherFileDir = GlobalParamUtils.getGloableStringValue("tempFileDir");
		File destFile = new File(otherFileDir+ File.separator + newFileName);
		try {
			FileUtils.newFileUtils().copyFile(file, destFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		file.delete();
		
		return destFile;
	}
	private static String SQL_FILE = "sql_file";
	private static String DOC_FILE = "doc_file";
	public String exportFile(Long misId,String type){
		String filePath = "";
		if(SQL_FILE.equals(type)){
			ItmsSqlExecute f = commonDao.load(ItmsSqlExecute.class, misId);
			if(f != null){
				ItmsWarehouse service = f.getDir().getWarehouse();
				String fileName = f.getFilename();
				filePath = filePath(service, fileName,fileName,ItmsSqlDir.DIR+ItmsSqlDir.ITMS_FILE+File.separator+f.getDir().getDirName());
			}
		}else if(DOC_FILE.equals(type)){
			ItmsFiles f = commonDao.load(ItmsFiles.class, misId);
			if(f!=null){
				ItmsWarehouse service = f.getWarehouse();
				String fileName = f.getSupCode();
				filePath = filePath(service,fileName,fileName,ItmsFiles.DIR);
			}
		}
		return filePath;
	}
	/**将srcFileName拷贝到当前路径下descFileName*/
	private String filePath(ItmsWarehouse service,String srcFileName,String descFileName,String dir){
		String ip = service.getCode();
		Contact contact = service.getContact();
		String user = contact.getContactName();
		String pass = contact.getAddress();
		String remotePath = ip+dir;
		
		//将文件拷贝到当前路径下
		ItmsFileUtils.smbPutLocal("smb://"+user+":"+pass+"@"+remotePath,ItmsFileUtils.localPath,srcFileName,descFileName);
		return ItmsFileUtils.localPath+descFileName;
	}
	public void updateExportTimes(Long misId,String type){
		if(ItmsSqlTaskType.EDI_SQL_FILE.equals(type)){
			ItmsSqlExecute f = commonDao.load(ItmsSqlExecute.class, misId);
			if(f != null){
				String fileName = f.getFilename();
				deleteRootFile(fileName);
			}
		}else{
			ItmsFiles f = commonDao.load(ItmsFiles.class, misId);
			if(f!=null){
				f.setQuantity(f.getQuantity()+1);
				commonDao.store(f);
				String fileName = f.getSupCode();
				deleteRootFile(fileName);
			}
		}
	}
	private void deleteRootFile(String fileName){
		File file = new File(ItmsFileUtils.localPath+fileName);
		file.delete();
	}
	public void editFile(ItmsFiles mis){
		commonDao.store(mis);
	}
	public void pShare(ItmsFiles mis){
		mis.setIsPublicShare(true);
		commonDao.store(mis);
	}
	public void unShare(ItmsFiles mis){
		mis.setIsPublicShare(false);
		commonDao.store(mis);
	}
	public void sendAdvancedTimingEmail(ItmsFiles f,ItmsInterfaceEmailRule emailRule) {
		ItmsWarehouse service = f.getWarehouse();
		String createDate = new SimpleDateFormat("HHmmssSSS").format(new Date());
		String[] srcFileName = f.getSupCode().split("\\.");
		String descFileName = UserHolder.getUser().getName()+MyUtils.spilt1+createDate+MyUtils.spilt1+srcFileName[0]+"."+srcFileName[1];
		String filePath = filePath(service,f.getSupCode(),descFileName,ItmsFiles.DIR);
		//发邮件task 
		itmsLogManager.saveItmsTask(ItmsSqlTaskType.EDI_JOB_EMAIL,EmailManager.BEAN+EmailManager.SEND_FILE_EMAIL,
				filePath, emailRule.getEmailAddr(),emailRule.getReceiver());
	}
	public void deleteFiles(ItmsFiles mis){
		Boolean sucess = UserHolder.getUser().getName().equals(mis.getUpdateInfo().getCreator());
		if(sucess){
			//远程删除
			ItmsWarehouse service = mis.getWarehouse();
			String ip = service.getCode();
			Contact contact = service.getContact();
			String user = contact.getContactName();
			String pass = contact.getAddress();
			String remotePath = ip+ItmsFiles.DIR;
			String fileName = mis.getSupCode();
			String localPath = ".."+File.separator;
			ItmsFileUtils.smbDeleteFile("smb://"+user+":"+pass+"@"+remotePath, fileName);
			commonDao.delete(mis);
		}else{
			LocalizedMessage.setMessage(MyUtils.font("您无权删除此文件!"));
		}
	}
	public void deleteSqlFile(ItmsSqlExecute sqlFile){
		Boolean sucess = UserHolder.getUser().getName().equals(sqlFile.getUpdateInfo().getCreator());
		if(sucess){
			//验证是否存在待执行的任务
			List<Long> taskIds = readyItmsTask(sqlFile.getFilename(),sqlFile.getDir().getDataSource());
			if(taskIds==null || taskIds.size()<=0){
				ItmsWarehouse service = ItmsWarehouseHolder.getWmsWarehouse();
				String ip = service.getCode();
				Contact contact = service.getContact();
				String user = contact.getContactName();
				String pass = contact.getAddress();
				String remotePath = ip+ItmsSqlDir.DIR+ItmsSqlDir.ITMS_FILE+File.separator+sqlFile.getDir().getDirName();
				String fileName = sqlFile.getFilename();
				
				ItmsFileUtils.smbDeleteFile("smb://"+user+":"+pass+"@"+remotePath, fileName);
				
				commonDao.delete(sqlFile);
			}else{
				throw new BusinessException(sqlFile.getFilename()+",存在未完成任务,不允许删除");
			}
		}else{
			throw new BusinessException("您无权删除此文件!");
		}
	}
	public void deleteSqlDir(ItmsSqlDir dir){
		String hql = "FROM ItmsSqlExecute f WHERE f.dir.id =:dir";
		List<ItmsSqlExecute> sqls = commonDao.findByQuery(hql, 
				new String[]{"dir"}, new Object[]{dir.getId()});
		
		Boolean sucess = UserHolder.getUser().getName().equals(dir.getUpdateInfo().getCreator());
		String message = "";
		if(sucess){
			for(ItmsSqlExecute sqlFile : sqls){
				sucess = UserHolder.getUser().getName().equals(sqlFile.getUpdateInfo().getCreator());
				if(!sucess){
					message ="您无权删除此文件夹下的文件:"+sqlFile.getFilename(); 
					break;
				}
				//验证是否存在待执行的任务
				List<Long> taskIds = readyItmsTask(sqlFile.getFilename(),sqlFile.getDir().getDataSource());
				if(taskIds!=null && taskIds.size()>0){
					sucess = false;
					message =sqlFile.getFilename()+",存在未完成任务,不允许删除"; 
					break;
				}
			}
			
		}else{
			message = "您无权删除此文件夹!";
		}
		if(sucess){
			ItmsWarehouse service = ItmsWarehouseHolder.getWmsWarehouse();
			String ip = service.getCode();
			Contact contact = service.getContact();
			String user = contact.getContactName();
			String pass = contact.getAddress();
			String remotePath = ip+ItmsSqlDir.DIR+ItmsSqlDir.ITMS_FILE+"/";
			String dirName = dir.getDirName();
			ItmsFileUtils.smbDeleteDir("smb://"+user+":"+pass+"@"+remotePath, dirName);
			
			commonDao.deleteAll(sqls);
			commonDao.delete(dir);
		}else{
			LocalizedMessage.setMessage(MyUtils.font(message));
		}
		
	}
	private List<Long> readyItmsTask(String filename,String type){
		String hql = "SELECT t.id FROM ItmsTask t WHERE t.extend2 =:extend2 AND t.type =:type AND t.status =:status";
		List<Long> taskIds = commonDao.findByQuery(hql, 
				new String[]{"extend2","type","status"}, 
				new Object[]{filename,type,TaskStatus.STAT_READY});
		return taskIds;
	}
	public void activeDir(ItmsSqlDir dir){
		ItmsWarehouse service = dir.getWarehouse();
		String ip = service.getCode();
		Contact contact = service.getContact();
		String user = contact.getContactName();
		String pass = contact.getAddress();
		String remotePath = ip+ItmsSqlDir.DIR;
		ItmsFileUtils.smbCreateDir("smb://"+user+":"+pass+"@"+remotePath, ItmsSqlDir.ITMS_FILE);
		remotePath = ip+ItmsSqlDir.DIR+ItmsSqlDir.ITMS_FILE+"/";
		String dirName = dir.getDirName();
		ItmsFileUtils.smbCreateDir("smb://"+user+":"+pass+"@"+remotePath, dirName);
	}
	public void activeSqlFile(ItmsSqlExecute sqlFile){
//		ItmsSqlDir dir = sqlFile.getDir();
//		ItmsWarehouse service = dir.getWarehouse();
//		String ip = service.getCode();
//		Contact contact = service.getContact();
//		String user = contact.getContactName();
//		String pass = contact.getAddress();
//		String remotePath = ip+ItmsSqlDir.DIR;
//		String fileName = dir.getDirName();
	}
	public void importSqlFileBySmb(ItmsSqlExecute sqlfile,File file){
		String fileName = JavaTools.decoder(file.getName());
		String newFileName = StringUtils.substringAfter(StringUtils.substringBeforeLast(fileName, "-"),"-");
		
		Boolean isGo = true;
		if(!newFileName.toUpperCase().endsWith(ItmsSqlExecute.EDN_SQL)){
			isGo = false;
			LocalizedMessage.setMessage(MyUtils.font("文件必须是.sql结尾的"));
		}
		if(isGo){
			ItmsWarehouse service = ItmsWarehouseHolder.getWmsWarehouse();
			String ip = service.getCode();
			Contact contact = service.getContact();
			String user = contact.getContactName();
			String pass = contact.getAddress();
			//move
			File destFile = moveFileOther(file, newFileName);
			//copy to services
			//smb://administrator:Passw0rd@192.168.10.92/vtradex/test
			String remotePath = ip+ItmsSqlDir.DIR+ItmsSqlDir.ITMS_FILE+File.separator+sqlfile.getDir().getDirName();
			ItmsFileUtils.smbPut("smb://"+user+":"+pass+"@"+remotePath, destFile.getPath());
			destFile.delete();
			sqlfile.setFilename(newFileName);
			commonDao.store(sqlfile);
		}
	}
	public void initAllSqlFiles(){
		ItmsWarehouse service = ItmsWarehouseHolder.getWmsWarehouse();
		String ip = service.getCode();
		Contact contact = service.getContact();
		String user = contact.getContactName();
		String pass = contact.getAddress();
		//StringUtils.substringBeforeLast(ItmsSqlDir.DIR, "/")
		String remotePath = "smb://"+user+":"+pass+"@"+ip+ItmsSqlDir.DIR+ItmsSqlDir.ITMS_FILE+"/";
		
		String hql ="FROM ItmsSqlDir dir WHERE dir.dirName =:dirName AND dir.warehouse.id =:warehouseId";
		String hqlf = "FROM ItmsSqlExecute f WHERE f.filename =:filename";
		try {
			SmbFile remoteFile = new  SmbFile(remotePath);
			try {
				SmbFile[] files = remoteFile.listFiles();
				for(SmbFile file : files){
					if(file.isDirectory()){
						SmbFile[] dirFiles = file.listFiles();
						if(dirFiles.length>0){
							String dirName = StringUtils.substringBeforeLast(file.getName(), "/");
							ItmsSqlDir dir = (ItmsSqlDir) commonDao.findByQueryUniqueResult(hql, 
									new String[]{"dirName","warehouseId"}, 
									new Object[]{dirName,service.getId()});
							if(dir==null){
								dir = EntityFactory.getEntity(ItmsSqlDir.class);
								dir.setDirName(dirName);
								dir.setWarehouse(service);
								dir.setDescription("系统同步");
								commonDao.store(dir);
							}
							for(SmbFile dirFile : dirFiles){
								if(dirFile.isFile()){//目前暂支持到2级目录
									if(!dirFile.getName().toUpperCase().endsWith(ItmsSqlExecute.EDN_SQL)){
										continue;
									}
									ItmsSqlExecute f = (ItmsSqlExecute) commonDao.findByQueryUniqueResult(hqlf, 
											new String[]{"filename"}, new Object[]{dirFile.getName()});
									if(f==null){
										f = EntityFactory.getEntity(ItmsSqlExecute.class);
										f.setDir(dir);
										f.setFilename(dirFile.getName());
										f.setRepeatTimes(dir.getRepeatTimes());//默认给文件夹执行频率
										f.setDescription("系统同步");
										commonDao.store(f);
									}
								}
							}
						}
					}
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

	public Map<String, List<String>> getFileData(Map params) {
		Map result  = new HashMap();
		
		String localIp = IpUtils.localIp();//服务所在服务器IP
		String logInIp = ItmsLogInIpHolder.getLogInIp();//登录IP
		Map<String,List<String>> data = new HashMap<String,List<String>>();
		List<String> values = null;
		if(IpUtils.isLocalIp()){//如果要模拟调试远程服务器,则将这里的条件值为false
			File srcDir = new File(ItmsSqlDir.LOC_DIR+File.separator+ItmsSqlDir.ITMS_FILE);
			File[] files = srcDir.listFiles();
			if(files.length>0){
				for(int line = 0;line<files.length;line++){
					values = new ArrayList<String>();
					File f1 = files[line];
					if(f1.isDirectory()){
						File[] ff = f1.listFiles();
						if(ff.length>0){
							for(int col=0;col<ff.length;col++){
								File f2 = ff[col];
								if(f2.isFile()){//目前暂支持到2级目录
									if(!f2.getName().toUpperCase().endsWith(ItmsSqlExecute.EDN_SQL)){
										continue;
									}
									values.add(f2.getName());
								}
							}
						}
					}
					if(values!=null && values.size()>0){
						data.put(f1.getName(), values);
					}
				}
			}
		}else{
			ItmsWarehouse service = ItmsWarehouseHolder.getWmsWarehouse();
			String ip = service.getCode();
			Contact contact = service.getContact();
			String user = contact.getContactName();
			String pass = contact.getAddress();
			String remotePath = "smb://"+user+":"+pass+"@"+ip+ItmsSqlDir.DIR+ItmsSqlDir.ITMS_FILE+"/";
			try {
				SmbFile remoteFile = new  SmbFile(remotePath);
				try {
					SmbFile[] files = remoteFile.listFiles();
					for(SmbFile f1 : files){
						if(f1.isDirectory()){
							SmbFile[] dirFiles = f1.listFiles();
							if(dirFiles.length>0){
								values = new ArrayList<String>();
								String dirName = StringUtils.substringBeforeLast(f1.getName(), "/");
								for(SmbFile f2 : dirFiles){
									if(f2.isFile()){//目前暂支持到2级目录
										if(!f2.getName().toUpperCase().endsWith(ItmsSqlExecute.EDN_SQL)){
											continue;
										}
										values.add(f2.getName());
									}
								}
								if(values!=null && values.size()>0){
									data.put(dirName, values);
								}
							}
						}
					}
					
				} catch (SmbException e) {
					String error = e.getMessage();
					if("The network name cannot be found.".equals(error)){
						error = "远程服务器job文件夹必须共享";
					}
					
					e.printStackTrace();
					throw new BusinessException(error);
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
				throw new BusinessException(e.getMessage());
			}
		}
		
		
		result.put(BusinessNode.MSG, BusinessNode.ON_SUCCESS);
		result.put(GridGroupingSampleFiles.KEY, data);
		result.put(GridGroupingSampleFiles.MESSAGE, GridGroupingSampleFiles.INIT_FILE);//告知那段方法被执行
		return result;
	}

	public Map readSqL(Map params) {
		Map result  = new HashMap();
		
		String parameter = (String) params.get(GridGroupingSampleFiles.KEY);
		String[] values = parameter.split(GridGroupingSampleFiles.SPILT);
		String fileName = values[0];
		String fileDir = values[1];
		String sqlParameter = null;
		
		String localIp = IpUtils.localIp();//服务所在服务器IP
		String logInIp = ItmsLogInIpHolder.getLogInIp();//登录IP
		if(IpUtils.isLocalIp()){
			File file = new File(ItmsSqlDir.LOC_DIR+File.separator+ItmsSqlDir.ITMS_FILE+File.separator+fileDir+File.separator+fileName);
			if(file.exists() && file.isFile()){
				sqlParameter = ItmsFileUtils.readStrTxt(file,ItmsFileUtils.GBK);
			}
		}else{
			ItmsWarehouse service = ItmsWarehouseHolder.getWmsWarehouse();
			String ip = service.getCode();
			Contact contact = service.getContact();
			String user = contact.getContactName();
			String pass = contact.getAddress();
			String remotePath = "smb://"+user+":"+pass+"@"+ip+ItmsSqlDir.DIR+ItmsSqlDir.ITMS_FILE+File.separator+fileDir;
			sqlParameter = ItmsFileUtils.smbReadTxt(remotePath, fileName,ItmsFileUtils.UTF_8);
		}
		String sql = "",line = "---------------this is spilt line---------------";
		if(StringUtils.isEmpty(sqlParameter)){
			sql = "警告:空文件会导致接口异常,请删除或完善";
		}else{
			String[] sp = sqlParameter.split(";");
			for(String s : sp){
				sql += new SQLFormatter().format(s.trim())+";"+"\n";
				sql += line+"\n";
			}
			sql = StringUtils.substringBeforeLast(sql,line);
		}
		result.put(BusinessNode.MSG, GridGroupingSampleFiles.READ_FILE);
		result.put(GridGroupingSampleFiles.KEY,sql);
		result.put(GridGroupingSampleFiles.MESSAGE, GridGroupingSampleFiles.READ_FILE);//告知那段方法被执行
		return result;
	}
	public void storeJacProjectUrl(JacProjectUrl url){
		commonDao.store(url);
	}
	public void deleteJacProjectUrl(JacProjectUrl url){
		commonDao.delete(url);
	}

}