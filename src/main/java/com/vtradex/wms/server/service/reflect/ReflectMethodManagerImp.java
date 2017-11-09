package com.vtradex.wms.server.service.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.model.itms.ItmsTask;
/**yc*/
public class ReflectMethodManagerImp extends DefaultBaseManager implements ReflectMethodManager{

	
	public ReflectMethodManagerImp() {
		// TODO Auto-generated constructor stub
	}
	@Transactional
	public void itmsTaskMethod(String taskIds) throws Exception {
		String[] ids = taskIds.split(",");
		for(String id : ids){
			ItmsTask task = commonDao.load(ItmsTask.class,Long.parseLong(id.trim()));
			if(task!=null){
				doHandleSubscriber(task.getSubscriber(),task.getMessageId());
			}
		}
	}
	protected void doHandleSubscriber(String type,Long id) throws Exception{
		String managerName = StringUtils.substringBefore(type,".");
		String methodName = StringUtils.substringAfter(type,".");
		Object[] params = new Object[]{id};
		Object manager = applicationContext.getBean(managerName);
		Assert.notNull(manager,type + " message not found managerName is not find " + managerName);
		Method method = getExactlyMethod(manager,methodName,params);
		Assert.notNull(method,type + " message not found methodName is not find  " + methodName);
		Object obj = method.invoke(manager,params);
		if(!(null == obj || "".equals(obj.toString()))){
			System.out.println("--------------managerName="+managerName+",id="+id+",methodName="+methodName+",type="+type);
		}
	}
	private Method getExactlyMethod(Object manager, String methodName, Object...args) throws NoSuchMethodException {
		logger.warn("The args passing to  Manager [" + manager + "] method [" + methodName + "] contains null arg(s)");
		for(Method method : manager.getClass().getMethods()){
			if(method.getName().equals(methodName) && method.getParameterTypes().length == args.length){
				return method;
			}
		}
		return null;
	}
	public void invokeMethod(String subscriber) throws Exception {
		String managerName =  StringUtils.substringBefore(subscriber.trim(),".");;
		String methodName = StringUtils.substringAfter(subscriber.trim(),".");
		Object manager = applicationContext.getBean(managerName);
		Boolean isError = Boolean.FALSE;
		String message = "";
		try {
			Method method = manager.getClass().getMethod(methodName, new Class[]{});
			Object obj = method.invoke(manager,new Object[]{});
		} catch (IllegalArgumentException e) {
			isError = Boolean.TRUE;
			e.printStackTrace();
		} catch (SecurityException e) {
			isError = Boolean.TRUE;
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			isError = Boolean.TRUE;
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			isError = Boolean.TRUE;
			Throwable targetEx = ((InvocationTargetException) e)
                    .getTargetException();
			if (targetEx != null){
				message = targetEx.getMessage();
			}else{
				message = e.getMessage();
			}
			throw new BusinessException(message);//e.getCause()
		} catch (NoSuchMethodException e) {
			isError = Boolean.TRUE;
			e.printStackTrace();
		}
	}
	public Object invokeMethod(String subscriber,String params) {
		String managerName =  StringUtils.substringBefore(subscriber.trim(),".");;
		String methodName = StringUtils.substringAfter(subscriber.trim(),".");
		Object manager = applicationContext.getBean(managerName);
		Object obj = null;
		String message = "";
		try {
			Method method = manager.getClass().getMethod(methodName, new Class[]{String.class});
			obj = method.invoke(manager,new Object[]{params});
		} 
		catch (IllegalArgumentException e) {
			throw new BusinessException(e.getMessage());
		} catch (SecurityException e) {
			throw new BusinessException(e.getMessage());
		} catch (IllegalAccessException e) {
			throw new BusinessException(e.getMessage());
		} catch (InvocationTargetException e) {
			Throwable targetEx = ((InvocationTargetException) e)
                    .getTargetException();
			if (targetEx != null){
				message = targetEx.getMessage();
			}else{
				message = e.getMessage();
			}
			throw new BusinessException(message);//e.getCause()
		} catch (NoSuchMethodException e) {
			throw new BusinessException(e.getMessage());
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		return obj;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
