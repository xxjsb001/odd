package com.vtradex.wms.server.service.interfaceLog;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.model.itms.ItmsJobLog;
import com.vtradex.wms.server.model.itms.ItmsTask;

public class ItmsLogManagerImp extends DefaultBaseManager implements ItmsLogManager{

	public ItmsLogManagerImp() {
		// TODO Auto-generated constructor stub
	}
	@Transactional
	public ItmsTask saveItmsTask(String[] obj) {
		ItmsTask task = new ItmsTask(obj[0], obj[1], Long.parseLong(obj[2]));
		this.commonDao.store(task);
		if(task.getMessageId()==null || task.getMessageId() == 0L){
			task.setMessageId(task.getId());
			commonDao.store(task);
		}
		return task;
	}
	
	public ItmsJobLog saveItmsJobLog(String type,String operName,String operException,String operExceptionMess){
		ItmsJobLog log = new ItmsJobLog(type, operName, operException, operExceptionMess);
		commonDao.store(log);
		return log;
	}
	
	public ItmsTask saveItmsTask(String type,String subscriber,Long messageId,String objName){
		String[] obj = null;
		obj = new String[]{
				type,subscriber,messageId+""
		};
		ItmsTask task = this.saveItmsTask(obj);
		task.setExtend2(objName);//执行对象名
		commonDao.store(task);
		return task;
	}
	
	public ItmsTask saveItmsTask(String type,String method,String dirName,String objName,String encoding){
		String[] obj = null;
		obj = new String[]{
				type,method,0L+""
		};
		ItmsTask task = this.saveItmsTask(obj);
		task.setExtend1(dirName);//路径或路径+文件全名
		task.setExtend2(objName);//执行对象名
		task.setExtend3(encoding);//编码格式或其他备注
		commonDao.store(task);
		return task;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}


}
