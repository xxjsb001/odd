package com.vtradex.wms.server.utils;
import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.ResultSet; 
import java.sql.SQLException; 
import java.sql.Statement; 
public class JDBCUtils {
	public static final String SYS_NAME = "sys as sysdba";
	public static final String SYS_PASS = "Jacjqwl123";//从实例管理去,取不到给默认值
	public static final String CLASS_NAME = "oracle.jdbc.driver.OracleDriver";
	private static JDBCUtils instance = null;
	public static synchronized JDBCUtils getJDBCUtils(){
		if(instance==null){
			instance = new JDBCUtils();
		}
		return instance;
	}
	public static String getUrl(String ip,String sid){
		String url = "jdbc:oracle:thin:@"+ip+":1521:"+sid; //jdbc:oracle:thin:@192.168.10.92:1521:orcl
		return url;
	}
	public void init(String ip,String sid,String userName,String pass){
		ResultSet rs = null;//结果集
		Statement stmt = null;//结果集通道
		Connection conn = null;//数据库连接
		try { 
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
			String url = "jdbc:oracle:thin:@"+ip+":1521:"+sid; //jdbc:oracle:thin:@192.168.10.92:1521:orcl
			conn = DriverManager.getConnection(url,userName,pass); //fdjother
			stmt = conn.createStatement(); 
			
			rs = stmt.executeQuery("SELECT a.TABLE_NAME,a.TABLESPACE_NAME,a.NUM_ROWS,a.PARTITIONED FROM ALL_TABLES a WHERE a.OWNER=upper('fdjother')");
		    while(rs.next()){
		    	int rowCount = rs.getInt("NUM_ROWS");
				System.out.println(rowCount);
		    }
			/*rs = stmt.executeQuery("drop user fdjxxx cascade");
			conn.commit();*/
	   }catch(ClassNotFoundException e){
		    e.printStackTrace();
		    System.out.println("找不到服务!");
	   }catch(SQLException e){
		   	e.printStackTrace();
	   }finally{
		   free(rs, stmt, conn);
	   }
	}
	//** 关闭资源 **//
	public static void free(ResultSet resultSet, Statement statement, Connection connection) {
		if (null != resultSet) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (null != statement) {
					try {
						statement.close();
					} catch (SQLException e) {
						e.printStackTrace();
					} finally {
						try {
							if (null != connection) {
								connection.close();
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}else{
			if (null != statement) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					try {
						if (null != connection) {
							connection.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}else{
				try {
					if (null != connection) {
						connection.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static void main(String[] args) {
		JDBCUtils ju = JDBCUtils.getJDBCUtils();
		ju.init("192.168.10.92","orcl","sys as sysdba","Jacjqwl123");
		ResultSet rs = null;//结果集

	}

}
