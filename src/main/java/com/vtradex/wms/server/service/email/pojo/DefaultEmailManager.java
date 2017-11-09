package com.vtradex.wms.server.service.email.pojo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.model.inventory.ItmsFiles;
import com.vtradex.wms.server.model.itms.ItmsInterfaceEmailRule;
import com.vtradex.wms.server.model.itms.ItmsJobLog;
import com.vtradex.wms.server.model.itms.ItmsTask;
import com.vtradex.wms.server.service.email.EmailManager;
import com.vtradex.wms.server.utils.IpUtils;
import com.vtradex.wms.server.utils.ItmsFileUtils;
import com.vtradex.wms.server.utils.JavaTools;
import com.vtradex.wms.server.utils.MyUtils;
import com.vtradex.thorn.server.resource.ResourceLoader;

import javax.mail.MessagingException;

public class DefaultEmailManager extends DefaultBaseManager implements EmailManager{
	
	private JavaMailSenderImpl mailSender ;
	protected ResourceLoader resourceLoader;
	private String encodeing = "UTF-8";
	
	private String host = "";
	private String userName = "";
	private String password = "";
	private String subject = "";
	private String from = "";
	public DefaultEmailManager(ResourceLoader resourceLoader){
		this.resourceLoader = resourceLoader;
	}
	
	public JavaMailSenderImpl getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSenderImpl mailSender) {
		this.mailSender = mailSender;
	}
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	private void setMailSender(){
		//设置 Mail Server
		mailSender.setHost(host);
		mailSender.setUsername(userName); // 根据自己的情况,设置username
		mailSender.setPassword(password==null?"":password); // 根据自己的情况, 设置password
		
		mailSender.setJavaMailProperties(getMailProperties());
	}
	
	protected Properties getMailProperties(){
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(resourceLoader.loadFile("mail.properties")));
//			properties.put("mail.smtp.auth", "true"); // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		properties.put("mail.smtp.auth", "true"); // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
//		prop.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); 
//		prop.setProperty("mail.smtp.socketFactory.fallback", "false"); 
//		properties.setProperty("mail.smtp.port", "25"); 
//		properties.setProperty("mail.smtp.socketFactory.port", "25"); 
		return properties;
	}
	protected void sender(String[] tos,String text , File file)throws MessagingException{
		//传送邮件
		mailSender.send(getMimeMessage(tos,text,file));
	}
	protected MimeMessage getMimeMessage(String[] tos,String text , File... files)throws MessagingException{
		MimeMessage msg = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg, true, encodeing);

		setMailSender();

		// 设置邮件消息:收件人,寄件人,主题与正文
		helper.setTo(tos);
		helper.setFrom(from==null?"":from);
		helper.setSubject(subject==null?"":subject);
		helper.setText(text,true);//为true-->发送转义HTML
		for(File file : files){
			if(file != null) 
				helper.addAttachment(file.getName(), new FileSystemResource(file));
		}
		
		return msg;
	}
	private void sendFileTest(){
		try {
			String filePath = "E:\\111.xls";
			String address = "yongcheng.min@vtradex.net,309831731@qq.com";
			String emails[] = address.split(",");
			String title = "到货通知";
			String contextStr = "附件为到货通知，请注意查收。谢谢！";
			for(int i=0;i<emails.length;i++){
				if(emails[i].contains("@")){
					this.subject = title;
					this.sender(new String[]{emails[i]}, contextStr, new File(filePath));
				}
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}
	public void sendFileByEmail(String filePath,String emails[],String title,String contextStr){
		for(int i=0;i<emails.length;i++){
			if(emails[i].contains("@")){
				this.subject = title;
				try {
					this.sender(new String[]{emails[i]}, contextStr, new File(filePath));
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void sendFileEmail(Long taskId){
		ItmsTask task = commonDao.load(ItmsTask.class, taskId);
		if(task!=null){
			String filePath = task.getExtend1();
			File file = new File(filePath);
			if(file.exists() && file.isFile()){
//				System.out.println(file);//闵永成#173621628#D-HUB WEB接口操作说明.pdf
				String[] s = file.getName().split(MyUtils.spilt1);//应该是三段:发送人,版本号,原文件名称
				
				String renameFilePath = ItmsFileUtils.localPath+s[2];
				file.renameTo(new File(renameFilePath));
				
				//本方法存在BUG:附件中文名称过长,或导致乱码
//				sendFileByEmail(renameFilePath, task.getExtend2().split(","),s[2], 
//						"附件来自于ITMS平台,发送人:"+s[0]+",请注意查收,谢谢!");
				
				sendMultiPartEmail(renameFilePath, task.getExtend2(), task.getExtend3(), s[2],
						"附件来自于ODD-HUB,发送人:"+s[0]+",请注意查收,谢谢!");
				
				File renameFile = new File(renameFilePath);
				renameFile.delete();
			}
		}
	}
	private void sendAdvancedTimingEmail(ItmsFiles mis,ItmsInterfaceEmailRule emailRule) {
		System.out.println(mis.getId()+","+emailRule.getEmailAddr());

		String address = emailRule.getEmailAddr();//"yongcheng.min@vtradex.net,309831731@qq.com,sunxiaojing0628@126.com";
		String emails[] = address.split(",");
		this.subject = mis.getSupCode();
		try {
			// embed the image and get the content id
			//http://www.jac.com.cn/r/cms/www/red/images/logo.png
			//http://www.apache.org/images/asf_logo_wide.gif
			URL url = new URL("http://www.jac.com.cn/r/cms/www/red/images/logo.png");
//			String cid = email.embed(url, "Apache logo");
			for(int i=0;i<emails.length;i++){
				if(emails[i].contains("@")){
					
//					String contextStr = getTestForCloseASN()+"\n"+
//							"<html><img src=\"cid:"+cid+"\"></html>";
//					this.sender(new String[]{emails[i]}, contextStr, new File(filePath));
					
//					String filePath = "E:\\111.xls";
//					String address = "yongcheng.min@vtradex.net,309831731@qq.com";
//					String emails[] = address.split(",");
//					String title = "到货通知";
//					String contextStr = "附件为到货通知，请注意查收。谢谢！";
//					for(int i=0;i<emails.length;i++){
//						if(emails[i].contains("@")){
//							this.subject = title;
//							this.sender(new String[]{emails[i]}, contextStr, new File(filePath));
//						}
//					}
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	
	}
	public void sendJobException(Long id){
		ItmsJobLog log = commonDao.load(ItmsJobLog.class, id);
		String objName = log.getOperName();
		String sql = log.getOperExceptionMess();
		
		String exception = JavaTools.spiltLast(log.getOperException(), ":");
		if(StringUtils.isEmpty(exception)){
			exception = "其他异常,请查阅后台日志";
		}
		try {
			sendJobEmail(objName, exception, sql);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}
	public void createAndSendAdvancedTimingEmail() {
		/*String address = (String) commonDao.findByQueryUniqueResult("select t.emailAddr from WmsInterfaceEmailRule t where t.type=:type and rownum=1", "type", "asn_advanced_email");
		if(StringUtils.isEmpty(address))
			return;
		Connection connection = null;
		String filePath = null;
		String raqName = "";
		if((new Date()).getHours()==11)
			raqName = "advancedReportEmailAM.raq";
		else
			raqName = "advancedReportEmailPM.raq";
		try {
			BasicDataSource dataSource = (BasicDataSource)this.applicationContext.getBean("dataSource");
			connection = dataSource.getConnection();
			String license = "quieeWindowServer.lic";
			ExtCellSet.setLicenseFileName(license);
			Context context = new Context();
			context.setConnection("ds1", connection);
			context.setDefDataSourceName("ds1");
			ReportDefine rf = null;
			try {
				String raqFilePath = this.applicationContext.getResource(raqName).getFile().getAbsolutePath();
				raqFilePath = raqFilePath.replace(raqName,"reportFiles\\"+raqName);
				rf = (ReportDefine) ReportUtils.read(raqFilePath);
			} catch (Exception e) {
				throw new OriginalBusinessException("读取报表文件错误！");
			}			
			Engine engine = new Engine(rf, context);
			IReport ireport = engine.calc();

			PageBuilder pb = null;
			try {
				pb = new PageBuilder(ireport);
			} catch (Throwable e) {
				e.printStackTrace();
			}
			ExcelReport  excelReport = new ExcelReport();
			excelReport.export(pb);
			filePath = "E:\\210docs\\" + df2.format(new Date())+".xls";
			excelReport.saveTo(filePath);
		} catch (SQLException e1) {
			throw new RuntimeException(e1);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(connection != null)
				JdbcUtils.closeConnection(connection);
		}*/
//		sendFileTest();
		try {
//			sendHtml();
			sendHtml2();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (EmailException e) {
			e.printStackTrace();
		}
		
	}
	//断网邮件
	public void sendNetEmail() throws EmailException, MalformedURLException{
		
		List<String> userNames = new ArrayList<String>();
//		userNames.add("309831731@qq.com");
//		userNames.add("yongcheng.min@vtradex.com");
//		userNames.add("yongcheng_min@163.com");
		
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("itms.properties");
		Properties p = new Properties();
		try {
			p.load(inputStream);
			String mails = p.getProperty("exceptionMails");
			String[] mailstrs = mails.split(MyUtils.comma);
			for(String mail : mailstrs) {
				userNames.add(mail);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String[] emails = new String[userNames.size()];
		int k = 0;
		for(String o : userNames){
			emails[k] =o;
			k++;
		}
		if(emails!=null){
			String mesg = "当您收到这份邮件之后,很可能您的服务器之间发生了网络中断的事件,为了防止业务中断,强烈建议您远程重启相关应用服务!由此给您带来的不便,还请谅解!";
			String aHtml = getJobException("网络异常邮件","Io 异常: Socket read timed out",
					mesg,"网络故障");
			for(int i=0;i<emails.length;i++){
				if(emails[i].contains("@")){
					HtmlEmail email = new HtmlEmail();
					email.setHostName(host);
					email.addTo(emails[i]);
					email.setFrom(from);
					email.setCharset(encodeing);
					email.setAuthentication(userName, password);
					this.subject = "网络异常提示 ("+IpUtils.localIp()+")";
					email.setSubject(subject);
					// set the html message
					email.setHtmlMsg(aHtml);
					// set the alternative message
					email.setTextMsg("Your email client does not support HTML messages");
					// send the email
					email.send();
				}
			}
		}
	}
	public void sendSqlJobErrorEmail(String params) throws EmailException, MalformedURLException{
		List<String> userNames = new ArrayList<String>();
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("itms.properties");
		Properties p = new Properties();
		try {
			p.load(inputStream);
			String mails = p.getProperty("exceptionMails");
			String[] mailstrs = mails.split(MyUtils.comma);
			for(String mail : mailstrs) {
				userNames.add(mail);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String[] emails = new String[userNames.size()];
		int k = 0;
		for(String o : userNames){
			emails[k] =o;
			k++;
		}
		if(emails!=null){
			String[] values = params.split(MyUtils.spilt1);//fileName#exception#sql
			String fileName = null,exception = null,sql = null;
			fileName = values[0];
			exception = values[1];
			sql = values[2];
			
			String[] errors =exception==null?null:exception.split(":");
			if(errors!=null && errors.length>0){
				exception = errors[errors.length-1];
			}
			
			String aHtml = getJobException(fileName,exception,
					sql,"详见JOB文件");
			for(int i=0;i<emails.length;i++){
				if(emails[i].contains("@")){
					HtmlEmail email = new HtmlEmail();
					email.setHostName(host);
					email.addTo(emails[i]);
					email.setFrom(from);
					email.setCharset(encodeing);
					email.setAuthentication(userName, password);
					this.subject = "JOB异常提示 ("+IpUtils.localIp()+")";
					email.setSubject(subject);
					// set the html message
					email.setHtmlMsg(aHtml);
					// set the alternative message
					email.setTextMsg("Your email client does not support HTML messages");
					// send the email
					email.send();
				}
			}
		}
	}
	@SuppressWarnings("unchecked")
	private void sendJobEmail(String fileName,String exception,String sql) throws EmailException, MalformedURLException{
		String[] emails = null;
		List<Object[]> users = commonDao.findByQuery("select user.id,user.name,user.email from User user,Group g"
				+ " where user in elements(g.users) and g.code = 'JOB_EMAIL'");
		if(users!=null && users.size()>0){
			emails = new String[users.size()];
			int i = 0;
			for(Object[] o : users){
				emails[i] = o[2]==null?"-":o[2].toString();
				i++;
			}
		}
		if(emails!=null){
			String aHtml = getJobException(fileName,exception,sql,"详见JOB日志管理");
			for(int i=0;i<emails.length;i++){
				if(emails[i].contains("@")){
					HtmlEmail email = new HtmlEmail();
					email.setHostName(host);
					email.addTo(emails[i]);
					email.setFrom(from);
					email.setCharset(encodeing);
					email.setAuthentication(userName, password);
					this.subject = "JOB异常提示 ("+IpUtils.localIp()+")";
					email.setSubject(subject);
					// set the html message
					email.setHtmlMsg(aHtml);
					// set the alternative message
					email.setTextMsg("Your email client does not support HTML messages");
					// send the email
					email.send();
				}
			}
		}
	}
	private void sendHtml2() throws EmailException, MalformedURLException{
		String address = "yongcheng_min@163.com,309831731@qq.com";//sunxiaojing0628@126.com
		String emails[] = address.split(",");
		//http://www.jaclogistic.cn/soceo/images/bottom_pic.png
//		URL url = new URL("http://www.jac.com.cn/r/cms/www/red/images/logo.png");
		for(int i=0;i<emails.length;i++){
			if(emails[i].contains("@")){
				HtmlEmail email = new HtmlEmail();
				email.setHostName(host);
				email.addTo(emails[i]);
				email.setFrom(from);
				email.setCharset(encodeing);
				email.setAuthentication(userName, password);
				this.subject = "JOB异常提示";
				email.setSubject(subject);
//				String cid = email.embed(url, "jac logo");
				// set the html message
				email.setHtmlMsg(
//						getJobTest()
//						+"\n"+"<html><img src=\"cid:"+cid+"\"></html>"
						getJobException("SCL_常规车辆下线.sql", "未找到远程数据库的连接说明", "insert into t_mes_01@jqwlscl"+
								"(ID,STATUS,EXCEPTION_MESS,VIN,PRODUCT_NO,XXDW,ERP_CODE,erp_name,STO_TIME,insert_date,thddbm,consignee_no,gzdw)"+
								"SELECT case SUBSTR(t3.t$clot, 1,1) WHEN 'A' THEN '1' WHEN 'B' THEN '2' WHEN 'C' THEN '3' WHEN 'D' THEN '4' WHEN 'E' THEN '5'"+
								"WHEN 'F' THEN '6' WHEN 'G' THEN '7' WHEN 'H' THEN '8' END || case substr(t3.t$clot,2,1) WHEN 'A' THEN '1' WHEN 'B' THEN '2' WHEN 'C' THEN '3'"+
								"WHEN 'D' THEN '4' WHEN 'E' THEN '5' WHEN 'F' THEN '6' WHEN 'G' THEN '7' WHEN 'H' THEN '8' else substr(t3.t$clot,2,1) END"+
								"|| SUBSTR(t3.t$clot, 3,6)||case when substr(t3.t$clot,2,1)<='9' then '0' else '1' end,"+
								"1,"+
								"' ',"+
								"trim(t3.t$clot),"+
								"trim(t3.t$prdcode),"+
								"trim(t3.t$xxdw),"+
								"trim(t3.t$erpcode),"+
								"trim(t1.t$dsca),"+
								"t3.t$stotime,"+
								"sysdate,' ',t2.t$ofbp,trim(t4.t$pdno)"+
								"from"+
								"(select t$item,t$dsca from baan.ttcibd001201@fp8) t1 inner join"+
								"( select t$clot,t$prdcode,t$xxdw,t$erpcode,t$stotime from baan.ttisfc914201@fp8 a"+
								"where substr(trim(t$erpcode),13,2)<>'-1' and t$clot in"+
								"(select t$clot from baan.ttisfc914201@fp8 where t$instime>sysdate-5"+
								"minus"+
								"select vin from t_mes_01@jqwlscl)"+
								") t3 on t3.t$erpcode=trim(t1.t$item)"+
								"inner join (select t$pdno,t$clot from baan.tcprrp981201@fp8 where t$slso not like '58901%' and t$cdat>sysdate-30) t4 on t3.t$clot=t4.t$clot"+
								"left join (select a.t$ofbp,b.t$clot from baan.ttdsls400201@fp8 a,baan.twhltc100201@fp8 b where a.t$orno=b.t$sale and b.t$opno1>sysdate-10) t2"+
								"on t3.t$clot=t2.t$clot ;","详见JOB日志管理")
				);
				// set the alternative message
				email.setTextMsg("Your email client does not support HTML messages");
				
				// send the email
				email.send();
			}
		}
		
	}
	private String sendHtml() throws EmailException, MalformedURLException{
		String address = "yongcheng_min@163.com,309831731@qq.com";//sunxiaojing0628@126.com
		String emails[] = address.split(",");
		// embed the image and get the content id
		//http://www.jac.com.cn/r/cms/www/red/images/logo.png
		//http://www.apache.org/images/asf_logo_wide.gif
		URL url = new URL("http://www.jac.com.cn/r/cms/www/red/images/logo.png");
		for(int i=0;i<emails.length;i++){
			if(emails[i].contains("@")){
				HtmlEmail email = new HtmlEmail();
				email.setHostName(host);
				email.addTo(emails[i]);
				email.setFrom(from);
				email.setCharset(encodeing);
				email.setAuthentication(userName, password);
				this.subject = "车辆入库信息反馈";
				email.setSubject(subject);
				String cid = email.embed(url, "Apache logo");
				// set the html message
				email.setHtmlMsg(getTestForCloseASN()
						+"\n"+"<html><img src=\"cid:"+cid+"\"></html>"
				);
				// set the alternative message
				email.setTextMsg("Your email client does not support HTML messages");
				
				// send the email
				email.send();
			}
		}
		return null;
	}
	
	private void sendMultiPartEmail(String filePath,String emailUrl,String receiveName,String title,String contextStr){
		try {
			MultiPartEmail  email = new MultiPartEmail ();
			email.setTLS(true);
			
			//test
//			this.host = "smtp.163.com";
//			this.userName = "yongcheng_min@163.com";
//			this.password = "woshimyc***";
			
			email.setHostName(host);
			email.setAuthentication(userName, password);
			
			EmailAttachment attachment = new EmailAttachment();
	        attachment.setPath(filePath);
	        
	        attachment.setDisposition(EmailAttachment.ATTACHMENT);
	        attachment.setDescription("附件描述");
	        attachment.setName(MimeUtility.encodeText(title));
	        
	        if(emailUrl!=null && emailUrl.contains("@")){
	        	email.addTo(emailUrl, receiveName); 
	        	
	        	email.setFrom(userName, "ODD-HUB", encodeing);
		        email.setSubject(title);
		        email.setCharset(encodeing);
		        email.setMsg(contextStr);
		        
		        email.attach(attachment);//添加附件
		        email.send();//发送
		        System.out.println("email send success:"+title);
	        }
	        
		}catch (EmailException e) {
			e.printStackTrace();
		}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	private static void sendMultiPartEmail(){
		
		try {
			MultiPartEmail  email = new MultiPartEmail ();
			email.setTLS(true);
			email.setHostName("smtp.163.com"); // 这里是发送服务器的名字
			email.setAuthentication("yongcheng_min@163.com", "woshimyc***");
			
			//产生一个附件
	        EmailAttachment attachment = new EmailAttachment();
	        attachment.setPath("D:/itms数据字典.doc");//指定附件在本地的绝对路径
	        //也可以以网络的方式
	        //attachment.setURL(new URL("http://www.apache.org/images/asf_logo_wide.gif"));
	        attachment.setDisposition(EmailAttachment.ATTACHMENT);
	        attachment.setDescription("附件描述");//附件描述
	        //附件名称,如果附件是中文名会在乱码,attachment.setName(MimeUtility.encodeText("测试"));
	        attachment.setName(MimeUtility.encodeText("itms数据字典.doc"));
	        
	        email.addTo("309831731@qq.com", "yc.min"); // 接收方
	        email.addTo("yongcheng.min@vtradex.com", "闵大师"); // 接收方
	        //第2个参数是昵称，可以不要，收件人在信箱内的这个地方显示
	        //收件人： rec <lushuaiyin@yahoo.com.cn>
	        
	        email.setFrom("yongcheng_min@163.com", "itms", "UTF-8");
	        email.setSubject("Java发送邮件测试"); // 标题
	        email.setCharset("UTF-8"); // 设置Charset
	        email.setMsg("这是一封Java程序发出的测试邮件,带附件。"); // 内容
	        
	        email.attach(attachment);//添加附件
	        email.send();//发送
	        System.out.println("邮件发送成功");
		} catch (EmailException e) {
			e.printStackTrace();
		}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		sendMultiPartEmail();
	}
	/**发送JOB异常邮件*/
	private String getJobException(String fileName,String exception,String sql,String bak1){
		StringBuffer str = new StringBuffer();
		str.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
		str.append(JavaTools.enter);
		str.append("<head>");
		str.append(JavaTools.enter);
		str.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
		str.append(JavaTools.enter);
		str.append("<title>Untitled Document</title>");
		str.append(JavaTools.enter);
		str.append("</head>");
		str.append(JavaTools.enter);
		str.append("<body>");
		str.append(JavaTools.enter);
		str.append("<table width=\"600\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">");
		str.append(JavaTools.enter);
		//top
		str.append("<tr>");
		str.append(JavaTools.enter);
		str.append("<td align=\"left\" valign=\"top\" bgcolor=\"#478730\" style=\"background-color:#478730;\">");
		str.append(JavaTools.enter);
		str.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"15\">");
		str.append(JavaTools.enter);
		str.append("<tr>");
		str.append(JavaTools.enter);
		str.append("<td align=\"left\" valign=\"top\" style=\"color:#ffffff; font-family:Arial, Helvetica, sans-serif; font-size:13px;\">");
		str.append(JavaTools.enter);
		str.append("江汽物流ODD-HUB推送("+bak1+") <br>");
		str.append(JavaTools.enter);
		str.append("</td>");
		str.append(JavaTools.enter);
		str.append("</tr>");
		str.append(JavaTools.enter);
		str.append("</table>");
		str.append(JavaTools.enter);
		str.append("</td>");
		str.append(JavaTools.enter);
		str.append("</tr>");
		//fileName
		str.append("<tr>");
		str.append(JavaTools.enter);
		str.append("<td align=\"center\" valign=\"top\" bgcolor=\"#f1f69d\"");
		str.append(JavaTools.enter);
		str.append(" style=\"background-color:#f1f69d; font-family:Arial, Helvetica, sans-serif;font-size:13px; color:#000000; padding:10px;\">");
		str.append(JavaTools.enter);
		str.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"margin-top:10px;\">");
		str.append(JavaTools.enter);
		str.append("<tr>");
		str.append(JavaTools.enter);
		str.append("<td align=\"left\" valign=\"top\" style=\"font-family:Arial, Helvetica, sans-serif; font-size:13px; color:#525252;\">");
		str.append(JavaTools.enter);
		str.append("<div style=\"font-family:Georgia, 'Times New Roman', Times, serif; font-size:25px; color:#000000;\">");
		str.append(fileName);
		str.append(JavaTools.enter);
		str.append("<span style=\"color:#478730;\">*</span>");
		str.append(JavaTools.enter);
		str.append("</div>");
		str.append(JavaTools.enter);
		//exception
		str.append("<div style=\"font-size:20px;\"><br>");
		str.append(JavaTools.enter);
		str.append(exception.trim());
		str.append(JavaTools.enter);
		str.append("</div>");
		str.append(JavaTools.enter);
		//sql
		str.append("<div style=\"position:relative; height:200px; overflow-y :auto\">");
//		str.append("<textarea id=\"requestJson\" rows=\"20\" cols=\"80\">");
		str.append(JavaTools.enter);
		str.append("<span style='border-bottom:1px dashed #ccc;z-index:1' t='7' onclick='return false;'>");
		str.append(JavaTools.enter);
		str.append(sql);
		str.append(JavaTools.enter);
		str.append("</span>");
//		str.append("</textarea>");
		str.append(JavaTools.enter);
		str.append("</div>");
		str.append(JavaTools.enter);
		
		str.append("</td>");
		str.append(JavaTools.enter);
		str.append("</tr>");
		str.append(JavaTools.enter);
		str.append(" </table>");
		str.append(JavaTools.enter);
		str.append("</td>");
		str.append(JavaTools.enter);
		str.append("</tr>");
		str.append(JavaTools.enter);
		//底部
		str.append("<tr>");
		str.append(JavaTools.enter);
		str.append("<td align=\"left\" valign=\"top\" bgcolor=\"#478730\" style=\"background-color:#478730;\"><table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"15\">");
		str.append(JavaTools.enter);
		str.append("<tr>");
		str.append(JavaTools.enter);
		str.append("<td align=\"left\" valign=\"top\" style=\"color:#ffffff; font-family:Arial, Helvetica, sans-serif; font-size:13px;\">");
		str.append(JavaTools.enter);
		str.append("安徽江汽物流有限公司 <br>");
		str.append(JavaTools.enter);
		str.append("Phone: (0551)62297133 <br>");
		str.append(JavaTools.enter);
		str.append("Website: <a href=\"http://www.jaclogistic.cn/index.html\" target=\"_blank\" style=\"color:#ffffff; text-decoration:none;\">");
		str.append(JavaTools.enter);
		str.append("http://www.jaclogistic.cn/index.html</a></td>");
		str.append(JavaTools.enter);
		str.append("</tr>");
		str.append(JavaTools.enter);
		str.append("</table></td>");
		str.append(JavaTools.enter);
		str.append("</tr>");
		str.append(JavaTools.enter);
		str.append("</table>");
		str.append(JavaTools.enter);
		str.append("</body>");
		str.append(JavaTools.enter);
		str.append("</html>");
//		System.out.println(str.toString());
//		System.out.println("---------------------------------------------------------------");
		return str.toString();
	}
	private String getTestForCloseASN(){
		StringBuffer str = new StringBuffer();
		str.append("<TABLE border=1 cellspacing=0 style='border:1px solid blue'>");
		
		str.append("<TR>");
		str.append("<TD align=\"center\" colspan=7>车辆入库信息反馈表");
		str.append("</TD>");
		str.append("</TR>");
		
		str.append("<TR>");
		str.append("<TD border colspan=2>ASN号：");
		str.append("</TD>");
		str.append("<TD colspan=3>"+"JHWL-2013082100001"+"");
		str.append("</TD>");
		str.append("<TD colspan=1> 入库时间：");
		str.append("</TD>");
		try{
			str.append("<TD colspan=1>"+"2015-11-27 15:20"+"");
		}catch(Exception e){
			str.append("<TD>");
		}
		str.append("</TD>");
		
//		str.append("<TD colspan=1>库房接收时间：");
//		str.append("</TD>");
//		try{
//			str.append("<TD colspan=2>"+"yyyy-mm-dd hh24:mi:ss"+"");
//		}catch(Exception e){
//			str.append("<TD>");
//		}
		str.append("</TD>");
		str.append("</TR>");
		
		str.append("<TR>");
		str.append("<TD style='border:1px solid black'>序号");
		str.append("</TD>");
		str.append("<TD style='border:1px solid black'>VIN");
		str.append("</TD>");
		str.append("<TD style='border:1px solid black'>车型");
		str.append("</TD>");
		str.append("<TD style='border:1px solid black'>车型代码");
		str.append("</TD>");
//		str.append("<TD style='border:1px solid black'>变形号");
//		str.append("</TD>");
//		str.append("<TD style='border:1px solid black'>项目号");
//		str.append("</TD>");
//		str.append("<TD style='border:1px solid black'>物料型号");
//		str.append("</TD>");
//		str.append("<TD style='border:1px solid black'>缴库数量");
//		str.append("</TD>");
		str.append("<TD style='border:1px solid black'>收货人");
		str.append("</TD>");
		str.append("<TD style='border:1px solid black'>入库数量");
		str.append("</TD>");
//		str.append("<TD style='border:1px solid black'>是否完成缴库");
//		str.append("</TD>");
//		str.append("<TD style='border:1px solid black'>差异");
//		str.append("</TD>");
		str.append("<TD style='border:1px solid black'>备注");
		str.append("</TD>");
		str.append("</TR>");
		
		int i=1;
		str.append("<TR>");
		str.append("<TD style='border:1px solid black'>"+i+"");
		str.append("</TD>");
		str.append("<TD style='border:1px solid black'>"+"D1047499"+"");
		str.append("</TD>");
		str.append("<TD style='border:1px solid black'>"+"威38宽单CY2BZQ丙3D动蓝阔预N"+"");
		str.append("</TD>");
		str.append("<TD style='border:1px solid black'>"+"KE6080X7001G00Y0"+"");
		str.append("</TD>");
//		str.append("<TD style='border:1px solid black'>"+"---"+"");//变形号
//		str.append("</TD>");
//		str.append("<TD style='border:1px solid black'>"+"----"+"");//项目号
//		str.append("</TD>");
//		str.append("<TD style='border:1px solid black'>"+"*****"+"");//物料号
//		str.append("</TD>");
//		str.append("<TD style='border:1px solid black'>"+24+"");
//		str.append("</TD>");
		str.append("<TD style='border:1px solid black'>"+"张广荣"+"");
		str.append("</TD>");
		str.append("<TD style='border:1px solid black'>"+26+"");
		str.append("</TD>");
//		str.append("<TD style='border:1px solid black'>"+"是"+"");
//		str.append("</TD>");
//		str.append("<TD style='border:1px solid black'>"+55+"");
//		str.append("</TD>");
		str.append("<TD style='border:1px solid black'>"+"工具还未领取"+"");
		str.append("</TD>");
		str.append("</TR>");
		
		i++;
		
		str.append("</TABLE>");
		return str.toString();
	}
	private String getJobTest(){
		StringBuffer str = new StringBuffer();
		str.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
		str.append(JavaTools.enter);
		str.append("<head>");
		str.append(JavaTools.enter);
		str.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
		str.append(JavaTools.enter);
		str.append("<title>Untitled Document</title>");
		str.append(JavaTools.enter);
		str.append("</head>");
		str.append(JavaTools.enter);
		str.append("<body>");
		str.append(JavaTools.enter);
		str.append("<table width=\"600\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">");
		str.append(JavaTools.enter);
		//top
		str.append("<tr>");
		str.append(JavaTools.enter);
		str.append("<td align=\"left\" valign=\"top\" bgcolor=\"#478730\" style=\"background-color:#478730;\">");
		str.append(JavaTools.enter);
		str.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"15\">");
		str.append(JavaTools.enter);
		str.append("<tr>");
		str.append(JavaTools.enter);
		str.append("<td align=\"left\" valign=\"top\" style=\"color:#ffffff; font-family:Arial, Helvetica, sans-serif; font-size:13px;\">");
		str.append(JavaTools.enter);
		str.append("江汽物流ODD-HUB推送 <br>");
		str.append(JavaTools.enter);
		str.append("</td>");
		str.append(JavaTools.enter);
		str.append("</tr>");
		str.append(JavaTools.enter);
		str.append("</table>");
		str.append(JavaTools.enter);
		str.append("</td>");
		str.append(JavaTools.enter);
		str.append("</tr>");
		//正文
		str.append("<tr>");
		str.append(JavaTools.enter);
		str.append("<td align=\"center\" valign=\"top\" bgcolor=\"#f1f69d\"");
		str.append(JavaTools.enter);
		str.append(" style=\"background-color:#f1f69d; font-family:Arial, Helvetica, sans-serif;font-size:13px; color:#000000; padding:10px;\">");
		str.append(JavaTools.enter);
		str.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"margin-top:10px;\">");
		str.append(JavaTools.enter);
		str.append("<tr>");
		str.append(JavaTools.enter);
		str.append("<td align=\"left\" valign=\"top\" style=\"font-family:Arial, Helvetica, sans-serif; font-size:13px; color:#525252;\">");
		str.append(JavaTools.enter);
		str.append("<img src=\"http://www.jaclogistic.cn/soceo/images/bottom_pic.png\" width=\"213\" height=\"319\" align=\"right\" style=\"margin-left:10px;\">");
		str.append(JavaTools.enter);
		str.append("<div style=\"font-family:Georgia, 'Times New Roman', Times, serif; font-size:56px; color:#000000;\">Special Offer");
		str.append(JavaTools.enter);
		str.append("<span style=\"color:#478730;\">*</span>");
		str.append(JavaTools.enter);
		str.append("</div>");
		str.append(JavaTools.enter);
		str.append("<div style=\"font-size:28px;\"><br>");
		str.append(JavaTools.enter);
		str.append("Lorem Ipsum adipiscing elit. Vestibulum magna enim, volutpat nec imperdiet id</div>");
		str.append(JavaTools.enter);
		str.append("<div> <br>");
		str.append(JavaTools.enter);
		str.append("Lorem Ipsum adipiscing elit. Vestibulum magna enim, volutpat nec imperdiet id  tempor venenatis eros. Aliquam sed velit vitae");
		str.append(JavaTools.enter);
		str.append("nibh pulvinar iaculis. Aenean hendrerit, lorem eu luctus cursus, sapien justo auctor ligula, id bibendum lorem leo quis leo.");
		str.append(JavaTools.enter);
		str.append("Suspendisse sit amet aliquam orci. Aliquam erat volutpat. Aliquam erat volutpat. Nunc ac justo enim. Morbi eleifend feugiat");
		str.append(JavaTools.enter);
		str.append("turpis non placerat. Etiam sed tellus ac lectus lacinia molestie nec eu nisl. Pellentesque mattis luctus ultrices.<br>");
		str.append(JavaTools.enter);
		str.append("<br>");
		str.append(JavaTools.enter);
		str.append("</div></td>");
		str.append(JavaTools.enter);
		str.append("</tr>");
		str.append(JavaTools.enter);
		str.append(" </table></td>");
		str.append(JavaTools.enter);
		str.append("</tr>");
		str.append(JavaTools.enter);
		//底部
		str.append("<tr>");
		str.append(JavaTools.enter);
		str.append("<td align=\"left\" valign=\"top\" bgcolor=\"#478730\" style=\"background-color:#478730;\"><table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"15\">");
		str.append(JavaTools.enter);
		str.append("<tr>");
		str.append(JavaTools.enter);
		str.append("<td align=\"left\" valign=\"top\" style=\"color:#ffffff; font-family:Arial, Helvetica, sans-serif; font-size:13px;\">");
		str.append(JavaTools.enter);
		str.append("安徽江汽物流有限公司 <br>");
		str.append(JavaTools.enter);
		str.append("Phone: (0551)62297133 <br>");
		str.append(JavaTools.enter);
//		str.append("Email: <a href=\"mailto:name@yourcompanyname.com\" style=\"color:#ffffff; text-decoration:none;\">name@yourcompanyname.com </a><br>");
//		str.append(JavaTools.enter);
		str.append("Website: <a href=\"http://www.jaclogistic.cn/index.html\" target=\"_blank\" style=\"color:#ffffff; text-decoration:none;\">");
		str.append(JavaTools.enter);
		str.append("http://www.jaclogistic.cn/index.html</a></td>");
		str.append(JavaTools.enter);
		str.append("</tr>");
		str.append(JavaTools.enter);
		str.append("</table></td>");
		str.append(JavaTools.enter);
		str.append("</tr>");
		str.append(JavaTools.enter);
		str.append("</table>");
		str.append(JavaTools.enter);
		str.append("</body>");
		str.append(JavaTools.enter);
		str.append("</html>");
//		System.out.println(str.toString());
//		System.out.println("---------------------------------------------------------------");
		return str.toString();
	}
}
