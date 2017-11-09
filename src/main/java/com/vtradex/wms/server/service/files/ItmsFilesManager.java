package com.vtradex.wms.server.service.files;

import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jxl.Cell;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.inventory.ItmsFiles;
import com.vtradex.wms.server.model.itms.ItmsInterfaceEmailRule;
import com.vtradex.wms.server.model.itms.ItmsSqlDir;
import com.vtradex.wms.server.model.itms.ItmsSqlExecute;
import com.vtradex.wms.server.model.itms.JacProjectUrl;

public interface ItmsFilesManager extends BaseManager{
	/**文件上传至服务器**/
	@Transactional
	void importFileBySmb(File file,String fileType,String note);
	/**文件下载至本地**/
	@Transactional
	String exportFile(Long misId,String type);
	/**回写次数,并删除文件**/
	@Transactional
	void updateExportTimes(Long misId,String type);
	/**修改文件**/
	@Transactional
	void editFile(ItmsFiles mis);
	/**公共分享**/
	@Transactional
	void pShare(ItmsFiles mis);
	/**取消公共分享**/
	@Transactional
	void unShare(ItmsFiles mis);
	@Transactional
	void sendAdvancedTimingEmail(ItmsFiles f,ItmsInterfaceEmailRule email);
	/**删除文件**/
	@Transactional
	void deleteFiles(ItmsFiles mis);
	/**保存门户**/
	@Transactional
	void storeJacProjectUrl(JacProjectUrl url);
	/**删除门户**/
	@Transactional
	void deleteJacProjectUrl(JacProjectUrl url);
	
	//========================//
	/**生效文件夹*/
	@Transactional
	void activeDir(ItmsSqlDir dir);
	/**生效文件*/
	@Transactional
	void activeSqlFile(ItmsSqlExecute sqlFile);
	/**SQL文件上传至服务器*/
	@Transactional
	void importSqlFileBySmb(ItmsSqlExecute sqlfile,File file);
	/**初始化sql文件到itms*/
	void initAllSqlFiles();
	/**远程删除文件*/
	void deleteSqlFile(ItmsSqlExecute sqlFile);
	/**远程删除文件夹*/
	@Transactional
	void deleteSqlDir(ItmsSqlDir dir);
	
	Map<String,List<String>> getFileData(Map params);
	Map readSqL(Map params);
}
