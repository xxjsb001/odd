package com.vtradex.wms.server.service.email;

import java.net.MalformedURLException;

import org.apache.commons.mail.EmailException;
import org.springframework.transaction.annotation.Transactional;

public interface EmailManager {
	public static final String BEAN = "emailManager.";
	
	@Transactional
	void createAndSendAdvancedTimingEmail();
	/**发送带附件的邮件**/
	@Transactional
	void sendFileByEmail(String filePath,String emails[],String title,String contextStr);
	/**JOB异常邮件*/
	static final String SEND_JOB_EXCEPTION = "sendJobException";
	void sendJobException(Long id);
	/**断网邮件
	 * @throws MalformedURLException
	 * @throws EmailException */
	static final String SEND_NET_EXCEPTION = "sendNetEmail";
	void sendNetEmail() throws EmailException, MalformedURLException;
	/**JOB异常邮件(fileName#exception#sql)
	 * @throws MalformedURLException
	 * @throws EmailException */
	static final String SEND_SQL_EXCEPTION = "sendSqlJobErrorEmail";
	void sendSqlJobErrorEmail(String params) throws EmailException, MalformedURLException;
	/**文档附件邮件*/
	static final String SEND_FILE_EMAIL = "sendFileEmail";
	void sendFileEmail(Long taskId);
}
