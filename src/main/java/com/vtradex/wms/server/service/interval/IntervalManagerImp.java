package com.vtradex.wms.server.service.interval;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.vtradex.engine.utils.DateUtils;
import com.vtradex.sequence.service.sequence.SequenceGenerater;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.model.itms.IntervalLog;
import com.vtradex.wms.server.model.itms.IntervalTimes;
import com.vtradex.wms.server.model.move.WmsMoveDocStatus;
import com.vtradex.wms.server.model.warehouse.ItmsTable;
import com.vtradex.wms.server.model.warehouse.ItmsTablespaces;
import com.vtradex.wms.server.model.warehouse.ItmsWarehouse;
import com.vtradex.wms.server.model.warehouse.ItmsSid;
import com.vtradex.wms.server.model.warehouse.ItmsUsers;
import com.vtradex.wms.server.utils.ItmsFileUtils;
import com.vtradex.wms.server.utils.JDBCUtils;
import com.vtradex.wms.server.utils.JavaTools;
import com.vtradex.wms.server.utils.MyUtils;

public class IntervalManagerImp extends DefaultBaseManager implements IntervalManager{
	
	
	public IntervalManagerImp(){
		
	}

	@SuppressWarnings("unchecked")
	public void storeInterval(IntervalLog iLog) {
		if(iLog.isNew()){
			String code = "INTERVAL_"+DateUtils.format(new Date(),"yyMMdd");
			SequenceGenerater ss = (SequenceGenerater) applicationContext.getBean("sequenceGenerater");
			code = ss.generateSequence(code, 6);
			iLog.setCode(code);
		}
		commonDao.store(iLog);
		
		List<IntervalTimes> times = commonDao.findByQuery("FROM IntervalTimes t WHERE t.iLog.id =:iLogId", "iLogId", iLog.getId());
		if(times!=null && times.size()>0){
			commonDao.deleteAll(times);
		}
		
		String tableName = "",tabUser = "";
		ItmsTable zone = iLog.getZone();
		ItmsUsers user = zone.getUser();
		ItmsTablespaces itmsTablespaces = user.getLocation();
		ItmsSid itmsSid = itmsTablespaces.getWarehouseArea();
		ItmsWarehouse warehouse =itmsSid.getWarehouse();
		
		tableName = zone.getCode();
		tabUser = user.getCode();
		
		String ip = warehouse.getCode();
		String sid = itmsSid.getName();
		String userName = JDBCUtils.SYS_NAME;
		String pass = StringUtils.isEmpty(itmsSid.getSysPass())?JDBCUtils.SYS_PASS:itmsSid.getSysPass();
		String sql = null;
		//登陆
		ResultSet rs = null;//结果集
		Statement stmt = null;//结果集通道
		Connection conn = null;//数据库连接
		try {
			Class.forName(JDBCUtils.CLASS_NAME); 
			String url = JDBCUtils.getUrl(ip, sid);
			conn = DriverManager.getConnection(url,userName,pass);
			stmt = conn.createStatement(); 
			
			String rangeName = iLog.getRangeName();
			if(StringUtils.isEmpty(rangeName)){
				rangeName = IntervalLog.RANGE_NAME;
			}
			sql = "select t."+IntervalLog.RANGE_NAME+" as "+IntervalLog.RANGE_NAME+" from "+
					"(select to_char(l."+rangeName+",'yyyy-MM-dd') as "+IntervalLog.RANGE_NAME+" from "+tabUser+"."+tableName+" l) "+
					"t group by t."+IntervalLog.RANGE_NAME+" order by t."+IntervalLog.RANGE_NAME;
//			//System.out.print(sql+"\n");
			IntervalTimes iTimes = null;
			Integer i = 0;
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				String created_time = rs.getString(IntervalLog.RANGE_NAME);
				iTimes = EntityFactory.getEntity(IntervalTimes.class);
				iTimes.setiLog(iLog);
				iTimes.setiTimes(created_time);
				iTimes.setLine(i);
				commonDao.store(iTimes);
				i++;
			}
		}catch(ClassNotFoundException e){
		    e.printStackTrace();
		    throw new BusinessException(e.getLocalizedMessage());
		}catch(SQLException e){
		   	e.printStackTrace();
		   	throw new BusinessException(e.getLocalizedMessage());
		}catch(Exception e){
		   	e.printStackTrace();
		   	throw new BusinessException(e.getLocalizedMessage());
		}finally{
		   JDBCUtils.free(rs, stmt, conn);
		}
	}
	private static String PART_1 = "PART1";
	@SuppressWarnings("unchecked")
	public void confirmBySerialNo(IntervalTimes iTimes) {
		IntervalLog iLog = iTimes.getiLog();
		
		String tableName = "",tabUser = "",times = "";
		ItmsTable zone = iLog.getZone();
		ItmsUsers user = zone.getUser();
		ItmsTablespaces itmsTablespaces = user.getLocation();
		ItmsSid itmsSid = itmsTablespaces.getWarehouseArea();
		ItmsWarehouse warehouse =itmsSid.getWarehouse();
		
		tableName = zone.getCode();
		tabUser = user.getCode();
		times = iTimes.getiTimes();
		
		String ip = warehouse.getCode();
		String sid = itmsSid.getName();
		String userName = JDBCUtils.SYS_NAME;
		String pass = StringUtils.isEmpty(itmsSid.getSysPass())?JDBCUtils.SYS_PASS:itmsSid.getSysPass();
		String sql = null;
		//登陆
		ResultSet rs = null;//结果集
		Statement stmt = null;//结果集通道
		Connection conn = null;//数据库连接
		try {
			Class.forName(JDBCUtils.CLASS_NAME); 
			String url = JDBCUtils.getUrl(ip, sid);
			conn = DriverManager.getConnection(url,userName,pass);
			stmt = conn.createStatement(); 
			
			String interName = iLog.getInterName();
			if(StringUtils.isEmpty(interName)){
				interName = "I_"+tableName;
			}else{
				interName = "I_"+interName;
			}
			sql = "select table_name,partition_name,high_value,tablespace_name from user_tab_partitions where "+
					"table_name='"+interName+"' order by partition_position";
//			//System.out.print(sql+"\n");
			int s = stmt.executeUpdate(sql);
			if(s>0){
				sql = "drop table "+interName+" purge";
//				//System.out.print(sql+"\n");
				stmt.executeUpdate(sql);
			}
			String rangeName = iLog.getRangeName();
			if(StringUtils.isEmpty(rangeName)){
				rangeName = IntervalLog.RANGE_NAME;
			}
			sql = "CREATE TABLE "+interName+
					" PARTITION BY RANGE ("+rangeName+")"+//将来created_time作为传参
					" INTERVAL ( NUMTOYMINTERVAL (1, 'MONTH') )"+
					" (PARTITION "+PART_1+
					" VALUES LESS THAN (TO_DATE ('"+times+"', 'yyyy-MM-dd')))"+
					" AS"+
					" SELECT * FROM "+tabUser+"."+tableName;
//			//System.out.print(sql+"\n");
			stmt.executeUpdate(sql);
			//欲擒故纵
			List<IntervalTimes> upTime = commonDao.findByQuery("FROM IntervalTimes t WHERE t.iLog.id =:iLogId"
					+ " AND t.isDrop =:isDrop", new String[]{"iLogId","isDrop"},
					new Object[]{iLog.getId(),Boolean.TRUE});
			if(upTime!=null && upTime.size()>0){
				for(IntervalTimes i : upTime){
					i.setIsDrop(Boolean.FALSE);
					commonDao.store(i);
				}
			}
			upTime = commonDao.findByQuery("FROM IntervalTimes t WHERE t.iLog.id =:iLogId"
					+ " AND t.line <=:lineNo", new String[]{"iLogId","lineNo"},
					new Object[]{iLog.getId(),iTimes.getLine()-1});
			if(upTime!=null && upTime.size()>0){
				for(IntervalTimes i : upTime){
					i.setIsDrop(Boolean.TRUE);
					commonDao.store(i);
				}
			}else{
				throw new BusinessException("所选明细前必须有值(不可选行号为0)");
			}
			String[] itms = times.split("-");
			String itm = "";
			for(String i : itms){
				itm += i;
			}
			iLog.setiTimes(itm);
			commonDao.store(iLog);
		} catch(ClassNotFoundException e){
		    e.printStackTrace();
		    throw new BusinessException(e.getLocalizedMessage());
	   }catch(SQLException e){
		   e.printStackTrace();
		   if(e.getLocalizedMessage().contains("ORA-14767")){
			   throw new BusinessException("截止点不得选择月节点最后");
		   }else{
			   throw new BusinessException(e.getLocalizedMessage());
		   }
		   
	   }catch(Exception e){
		   	e.printStackTrace();
		   	throw new BusinessException(e.getLocalizedMessage());
	   }finally{
		   JDBCUtils.free(rs, stmt, conn);
	   }
	}
	
	@SuppressWarnings("unchecked")
	public void deleteInterval(IntervalLog iLog) {
		String tableName = "";
		ItmsTable zone = iLog.getZone();
		ItmsUsers user = zone.getUser();
		ItmsTablespaces itmsTablespaces = user.getLocation();
		ItmsSid itmsSid = itmsTablespaces.getWarehouseArea();
		ItmsWarehouse warehouse =itmsSid.getWarehouse();
		
		tableName = zone.getCode();
		
		String ip = warehouse.getCode();
		String sid = itmsSid.getName();
		String userName = JDBCUtils.SYS_NAME;
		String pass = StringUtils.isEmpty(itmsSid.getSysPass())?JDBCUtils.SYS_PASS:itmsSid.getSysPass();
		String sql = null;
		//登陆
		ResultSet rs = null;//结果集
		Statement stmt = null;//结果集通道
		Connection conn = null;//数据库连接
		try {
			Class.forName(JDBCUtils.CLASS_NAME); 
			String url = JDBCUtils.getUrl(ip, sid);
			conn = DriverManager.getConnection(url,userName,pass);
			stmt = conn.createStatement(); 
			
			String interName = iLog.getInterName();
			if(StringUtils.isEmpty(interName)){
				interName = "I_"+tableName;
			}else{
				interName = "I_"+interName;
			}
			sql = "select table_name,partition_name,high_value,tablespace_name from user_tab_partitions where "+
					"table_name='"+interName+"' order by partition_position";
//			//System.out.print(sql+"\n");
			int s = stmt.executeUpdate(sql);
			if(s>0){
				sql = "drop table "+interName+" purge";
//				//System.out.print(sql+"\n");
				stmt.executeUpdate(sql);
			}
		}catch(ClassNotFoundException e){
		    e.printStackTrace();
		    throw new BusinessException(e.getLocalizedMessage());
	   }catch(SQLException e){
		   	e.printStackTrace();
		   	throw new BusinessException(e.getLocalizedMessage());
	   }catch(Exception e){
		   	e.printStackTrace();
		   	throw new BusinessException(e.getLocalizedMessage());
	   }finally{
		   JDBCUtils.free(rs, stmt, conn);
	   }
		
		List<IntervalTimes> times = commonDao.findByQuery("FROM IntervalTimes t WHERE t.iLog.id =:iLogId", "iLogId", iLog.getId());
		if(times!=null && times.size()>0){
			commonDao.deleteAll(times);
		}
		commonDao.delete(iLog);
		
	}

	public void activeInterval(IntervalLog iLog) {
		String tableName = "",tabUser = "",times = "";
		ItmsTable zone = iLog.getZone();
		ItmsUsers user = zone.getUser();
		ItmsTablespaces itmsTablespaces = user.getLocation();
		ItmsSid itmsSid = itmsTablespaces.getWarehouseArea();
		ItmsWarehouse warehouse =itmsSid.getWarehouse();
		
		tableName = zone.getCode();
		tabUser = user.getCode();
		times = iLog.getiTimes();
		
		String ip = warehouse.getCode();
		String sid = itmsSid.getName();
		String userName = JDBCUtils.SYS_NAME;
		String pass = StringUtils.isEmpty(itmsSid.getSysPass())?JDBCUtils.SYS_PASS:itmsSid.getSysPass();
		String sql = null;
		//登陆
		ResultSet rs = null;//结果集
		Statement stmt = null;//结果集通道
		Connection conn = null;//数据库连接
		try {
			Class.forName(JDBCUtils.CLASS_NAME); 
			String url = JDBCUtils.getUrl(ip, sid);
			conn = DriverManager.getConnection(url,userName,pass);
			stmt = conn.createStatement(); 
			
			String interName = iLog.getInterName();
			if(StringUtils.isEmpty(interName)){
				interName = "I_"+tableName;
			}else{
				interName = "I_"+interName;
			}
			
			sql = "select count(*) as nums from user_tab_partitions where table_name='"+interName+"' "+
					"and partition_name not in ('"+PART_1+"') order by partition_position";
//			//System.out.print(sql+"\n");
			rs = stmt.executeQuery(sql);
			Integer s = 0;
			while(rs.next()){
				s = rs.getInt("nums");
			}
			if(s>0){
				sql = "truncate table "+tabUser+"."+tableName;
//				//System.out.print(sql+"\n");
				try {
					stmt.executeUpdate(sql);
				} catch (Exception e) {
					System.out.print(e.getLocalizedMessage().trim()+"\n");
					sql = "alter table "+tabUser+"."+tableName+" disable primary key cascade";
//					//System.out.print(sql+"\n");
					stmt.executeUpdate(sql);
					sql = "truncate table "+tabUser+"."+tableName;
					stmt.executeUpdate(sql);
					sql = "alter table "+tabUser+"."+tableName+" enable primary key";
//					//System.out.print(sql+"\n");
					stmt.executeUpdate(sql);
				}
				
				sql = "alter table "+tabUser+"."+tableName+" nologging";
//				//System.out.print(sql+"\n");
				stmt.executeUpdate(sql);
				
				List<Object[]> partitions = new ArrayList<Object[]>();
				sql = "select partition_name,high_value from user_tab_partitions where table_name='"+interName+"' "+
						"and partition_name not in ('"+PART_1+"') order by partition_position";
//				//System.out.print(sql+"\n");
				rs.close();
				rs = stmt.executeQuery(sql);
				while(rs.next()){
					partitions.add(new Object[]{
							rs.getString("partition_name"),rs.getObject("high_value").toString()
					});
				}
				if(partitions!=null && partitions.size()>0){
					for(Object[] obj : partitions){
						String partition_name = obj[0].toString();
						sql = "insert /*+append*/ into "+tabUser+"."+tableName+" select * from "+interName+
								" partition("+partition_name+")";
//						//System.out.print(sql+"\n");
						stmt.executeUpdate(sql);
					}
				}
				
				if(iLog.getIsBf()){
					String bakTableName = tableName+"_LESS"+times; 
					sql = "create table "+tabUser+"."+bakTableName+" nologging as select * from "+
							""+interName+"  partition("+PART_1+")";
//					//System.out.print(sql+"\n");
					stmt.executeUpdate(sql);
					
					iLog.setBakTableName(bakTableName);
					iLog.setStatus(WmsMoveDocStatus.ACTIVE);
				}else{
					iLog.setStatus(WmsMoveDocStatus.FINISHED);
				}
				commonDao.store(iLog);
				sql = "drop table "+interName+" purge";
//				//System.out.print(sql+"\n");
				stmt.executeUpdate(sql);
			}
			
			
		} catch(ClassNotFoundException e){
		    e.printStackTrace();
		    throw new BusinessException(e.getLocalizedMessage());
	   }catch(SQLException e){
		   	e.printStackTrace();
		   	throw new BusinessException(e.getLocalizedMessage());
	   }catch(Exception e){
		   	e.printStackTrace();
		   	throw new BusinessException(e.getLocalizedMessage());
	   }finally{
		   JDBCUtils.free(rs, stmt, conn);
	   }
		
	}

	public void finishInterval(IntervalLog iLog) {
//		String tableName = "";
		String pass = "";
		ItmsTable zone = iLog.getZone();
		ItmsUsers user = zone.getUser();
		ItmsTablespaces itmsTablespaces = user.getLocation();
		ItmsSid itmsSid = itmsTablespaces.getWarehouseArea();
		ItmsWarehouse warehouse =itmsSid.getWarehouse();
		
//		tableName = zone.getCode();
		final String tabUser = user.getCode();
		pass = user.getName();
		final String bakTableName = iLog.getBakTableName();
		final String ip = warehouse.getCode();
		final String sid = itmsSid.getName();
		final String sysPass = StringUtils.isEmpty(itmsSid.getSysPass())?JDBCUtils.SYS_PASS:itmsSid.getSysPass();
		
		//路径分服务/SID/用户
		String path = MyUtils.ITMS_BACK;//D:/jac_scl/auto_oracl_bak
		ItmsFileUtils.mkdir(path);
		ItmsFileUtils.mkdir(path+"\\"+warehouse.getName());//name要必填
		ItmsFileUtils.mkdir(path+"\\"+warehouse.getName()+"\\"+sid);
		ItmsFileUtils.mkdir(path+"\\"+warehouse.getName()+"\\"+sid+"\\"+tabUser);
		path += "\\"+warehouse.getName()+"\\"+sid+"\\"+tabUser+"\\";
		
    	StringBuffer sb = new StringBuffer();
    	sb.append("@echo on").append(JavaTools.enter);
    	sb.append("set d=%DATE:~0,4%%DATE:~5,2%%DATE:~8,2%").append(JavaTools.enter);
    	//exp jac_scl/jac_scl@192.168.10.92:1521/ORCL
    	sb.append("exp ").append(tabUser).append("/").append(pass).append("@").append(ip).append("/").append(sid).append(" ");
    	//file=D:/jac_scl/auto_oracl_bak/EXCEPTION_LOG_LESS20151203.dmp 
    	sb.append("file=").append(path).append(bakTableName).append(".dmp ");
    	//log=D:\\jac_scl\\auto_oracl_bak\\EXCEPTION_LOG_LESS20151203.log 
    	sb.append("log=").append(path).append(bakTableName).append(".log ");
    	sb.append("tables=").append(bakTableName).append(JavaTools.enter);
    	sb.append("@echo on").append(JavaTools.enter);
    	//zip -m D:/jac_scl/auto_oracl_bak/EXCEPTION_LOG_LESS20151203.zip 
    	sb.append("zip -m ").append(path).append(bakTableName).append(".zip ");
    	//D:/jac_scl/auto_oracl_bak/EXCEPTION_LOG_LESS20151203.dmp
    	sb.append(path).append(bakTableName).append(".dmp");
    	
    	final String batName = path+"EXP_"+bakTableName+".bat";
    	creatBat(sb.toString(), batName);
    	new Thread(new Runnable() {
			public void run() {
				runbat(batName,new Object[]{
						ip,sid,sysPass,tabUser,bakTableName
				});
			}
		}).start();
	}
	
	public static void creatBat(String command, String batURL) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(batURL);
			fw.write(command);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(0);
				}
			}
		}
	}
	
	public void runbat(String batName,Object[] obj) {
		
        try {
            Process ps = Runtime.getRuntime().exec(batName);
            InputStream in = ps.getInputStream();
            int c;
            while ((c = in.read()) != -1) {
//                System.out.print(c);// 如果你不需要看输出，这行可以注销掉
            }
            in.close();
            ps.waitFor();
            //delete bat
            ItmsFileUtils.deleteFile(new File(batName));
            //drop bakTableName
            String ip = obj[0].toString();
    		String sid = obj[1].toString();
    		String userName = JDBCUtils.SYS_NAME;
    		String pass = obj[2].toString();
    		String tabUser = obj[3].toString();
    		String bakTableName =obj[4].toString();
    		String sql = null;
    		//登陆
    		ResultSet rs = null;//结果集
    		Statement stmt = null;//结果集通道
    		Connection conn = null;//数据库连接
            try {
            	Class.forName(JDBCUtils.CLASS_NAME); 
    			String url = JDBCUtils.getUrl(ip, sid);
    			conn = DriverManager.getConnection(url,userName,pass);
    			stmt = conn.createStatement(); 

			    sql = "drop table "+tabUser+"."+bakTableName+" purge";
//			    System.out.print(sql+"\n");
    			stmt.executeUpdate(sql);
			} catch(ClassNotFoundException e){
			    e.printStackTrace();
			    throw new BusinessException(e.getLocalizedMessage());
		   }catch(SQLException e){
			   	e.printStackTrace();
			   	throw new BusinessException(e.getLocalizedMessage());
		   }catch(Exception e){
			   	e.printStackTrace();
			   	throw new BusinessException(e.getLocalizedMessage());
		   }finally{
			   JDBCUtils.free(rs, stmt, conn);
		   }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("child thread done:"+batName);
    }


}