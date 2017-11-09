<%@ page language="java" contentType="text/html; charset=UTF-8"  
    pageEncoding="UTF-8"%>  
<%@ page import="javax.naming.Context" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="javax.sql.DataSource" %>
<%@ page import="java.sql.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">  
<html>  
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">  
<title>测试JNDI连接</title>  
</head>  
<body>  
<%  
Context ctx = new InitialContext();  
DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/oracleDb"); 
Connection conns = null; 
try{
	conns = ds.getConnection(); 
	if(null!=conns){  
		out.println("数据库连接状态:<b>OK</b><hr />");  
		PreparedStatement pst = conns.prepareStatement("select u.login_name,u.name from users u where u.status = 'ACTIVE'");  
		ResultSet rs = pst.executeQuery();  
		while(rs.next()){ 
			String login_name = rs.getString("login_name");  
			String name = rs.getString("name");  
			out.println("<b>登录名:</b>"+login_name+"<br /><b>姓名:</b>"+name+"<hr />");  
		}  
	}else{  
		out.println("数据库连接状态:NO");  
	}
}catch (Exception e) {
	out.println(e.getMessage());  
}
%>  
</body>  
</html>