package com.vtradex.wms.server.web.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.vtradex.wms.server.service.files.ItmsFilesManager;


import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/***
 * 
 * @author yc
 * 文件下载
 */
public class DownloadFileServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	protected final String CHARACTER = "GBK";
	protected static ApplicationContext ac;
	public void init(ServletConfig sc) throws ServletException {
		super.init(sc);
		ac = WebApplicationContextUtils.getRequiredWebApplicationContext(sc.getServletContext());
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req , res);
	}
	public void doPost(final HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
		req.setCharacterEncoding(CHARACTER);//UTF-8
		String id = req.getParameter("id");
		String type = req.getParameter("type");
		final ItmsFilesManager itmsFilesManager = (ItmsFilesManager)ac.getBean("itmsFilesManager");
		String filePath = itmsFilesManager.exportFile(Long.parseLong(id),type);
		response.setCharacterEncoding("GBK");
		File file = new File(filePath);
		if(file.exists()){
			//下载
			String filename = new String((file.getName()).getBytes("GBK"),"8859_1");
			response.setHeader("content-disposition",
		           "attachment; filename=" + filename);
			FileInputStream in = new FileInputStream(file);
		    FileCopyUtils.copy(in,response.getOutputStream());
		    //回写次数,并删除文件
		    itmsFilesManager.updateExportTimes(Long.parseLong(id),type);
		}else{
			response.getWriter().print("文档尚未产生,请稍候再试"+filePath);
		}
	}
}
