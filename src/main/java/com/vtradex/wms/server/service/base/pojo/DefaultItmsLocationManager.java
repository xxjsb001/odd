package com.vtradex.wms.server.service.base.pojo;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;

import com.vtradex.thorn.client.ui.table.RowData;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.service.WorkflowManager;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.util.LocalizedMessage;
import com.vtradex.thorn.server.web.security.UserHolder;
import com.vtradex.wms.server.model.base.BaseStatus;
import com.vtradex.wms.server.model.count.ItmpDeptNumber;
import com.vtradex.wms.server.model.itms.IntervalLog;
import com.vtradex.wms.server.model.itms.IntervalTimes;
import com.vtradex.wms.server.model.itms.ItmsDbLinkOwner;
import com.vtradex.wms.server.model.itms.ItmsDbLinks;
import com.vtradex.wms.server.model.itms.ItmsSynonyms;
import com.vtradex.wms.server.model.itms.ItmsUserOnOff;
import com.vtradex.wms.server.model.warehouse.ItmsTable;
import com.vtradex.wms.server.model.warehouse.ItmsTablespaces;
import com.vtradex.wms.server.model.warehouse.ItmsWarehouse;
import com.vtradex.wms.server.model.warehouse.ItmsSid;
import com.vtradex.wms.server.model.warehouse.ItmsUsers;
import com.vtradex.wms.server.service.base.CheckInventoryType;
import com.vtradex.wms.server.service.base.ItmsLocationManager;
import com.vtradex.wms.server.service.interfaceLog.JdbcExtendDataExtImp;
import com.vtradex.wms.server.service.reflect.ReflectMethodManager;
import com.vtradex.wms.server.utils.ItmsUserTabs;
import com.vtradex.wms.server.utils.JDBCUtils;
import com.vtradex.wms.server.utils.JavaTools;
import com.vtradex.wms.server.utils.MyUtils;
import com.vtradex.wms.server.web.filter.ItmsWarehouseHolder;

public class DefaultItmsLocationManager extends DefaultBaseManager implements ItmsLocationManager {
	
	private WorkflowManager workFlowManager;
	private ReflectMethodManager reflectMethodManager;
	public DefaultItmsLocationManager( WorkflowManager workFlowManager,ReflectMethodManager reflectMethodManager) {
		this.workFlowManager = workFlowManager;
		this.reflectMethodManager = reflectMethodManager;
	}
	public void storeLocation(ItmsTablespaces location) {
		try {
			workFlowManager.doWorkflow(location, "wmsLocationBaseProcess.new");
		}catch (DataIntegrityViolationException e) {
			throw new BusinessException(e.getLocalizedMessage());
		} 
		catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		commonDao.store(location);

		
	}
	//表信息初始化
	@SuppressWarnings("unchecked")
	public void updateWorkArea(Long userId,String description){
		ItmsUsers user = commonDao.load(ItmsUsers.class, userId);
		ItmsTablespaces itmsTablespaces = user.getLocation();//账号所在表空间
		ItmsSid itmsSid = itmsTablespaces.getWarehouseArea();//表空间所在实例SID
		ItmsWarehouse warehouse = itmsSid.getWarehouse();//服务器IP
		//查询当前用户已维护表
		List<ItmsTable> tables = commonDao.findByQuery("FROM ItmsTable w WHERE w.user.id =:userId"
				+ " AND w.type =:type", 
				new String[]{"userId","type"}, new Object[]{userId,CheckInventoryType.BLIND_CHECK});
		Map<String,ItmsTable> areaMap = new HashMap<String,ItmsTable>();
		if(tables!=null && tables.size()>0){
			for(ItmsTable table : tables){
				areaMap.put(table.getCode().toUpperCase(), table);
			}
		}tables.clear();
		//查询当前用户已维护视图
		tables = commonDao.findByQuery("FROM ItmsTable w WHERE w.user.id =:userId"
				+ " AND w.type =:type", 
				new String[]{"userId","type"}, new Object[]{userId,CheckInventoryType.NORMAL_CHECK});
		Map<String,ItmsTable> viewMap = new HashMap<String,ItmsTable>();
		if(tables!=null && tables.size()>0){
			for(ItmsTable area : tables){
				viewMap.put(area.getCode().toUpperCase(), area);
			}
		}tables.clear();
		
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
			//table
			sql = "SELECT upper(a.TABLE_NAME) as TABLE_NAME,a.TABLESPACE_NAME,a.NUM_ROWS,a.PARTITIONED" +
					" FROM "+ItmsUserTabs.ALL_TABLES+" a WHERE a.OWNER=upper('"+user.getCode()+"')"
					+ " AND a.TABLESPACE_NAME = upper('"+itmsTablespaces.getCode()+"')";
			//System.out.print(sql+"\n");
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				String tableName = rs.getString("TABLE_NAME");
		    	int numRows = rs.getInt("NUM_ROWS");
		    	ItmsTable table = null;
		    	if(areaMap.containsKey(tableName)){//update
		    		table = areaMap.get(tableName);
		    	}else{//new
		    		table = EntityFactory.getEntity(ItmsTable.class);
		    	}
		    	table.setCode(tableName);
		    	table.setNumRows(numRows);
		    	table.setType(CheckInventoryType.BLIND_CHECK);
		    	table.setUser(user);
		    	table.setStatus("ENABLED");
	    		commonDao.store(table);
	    		
	    		areaMap.remove(tableName);
		    }
			/**将表所属表空间改变
	SELECT 'alter table WEI_ZHI_MIDDLE.'||a.TABLE_NAME ||' move tablespace JAC_PCL_DATA;'
  	FROM ALL_TABLES a WHERE a.OWNER = upper('WEI_ZHI_MIDDLE')  AND a.TABLESPACE_NAME <> upper('JAC_PCL_DATA')*/
			//view
			sql = "select upper(v.view_name) as view_name from "+ItmsUserTabs.ALL_VIEWS+" v" +
					" where v.owner = upper('"+user.getCode()+"')";
			//System.out.print(sql+"\n");
			rs.close();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				String tableName = rs.getString("view_name");
				ItmsTable table = null;
		    	if(viewMap.containsKey(tableName)){//update
		    		table = viewMap.get(tableName);
		    	}else{//new
		    		table = EntityFactory.getEntity(ItmsTable.class);
		    	}
		    	table.setCode(tableName);
		    	table.setNumRows(0);
		    	table.setType(CheckInventoryType.NORMAL_CHECK);
		    	table.setUser(user);
		    	table.setStatus("ENABLED");
	    		commonDao.store(table);
	    		
	    		viewMap.remove(tableName);
			}
			if(areaMap.size()>0){
				Iterator<Entry<String, ItmsTable>> integer = areaMap.entrySet().iterator();
				while(integer.hasNext()){
					Entry<String, ItmsTable> entry = integer.next();
					ItmsTable table = entry.getValue();
					table.setDescription("not exits:"+UserHolder.getUser().getName());
					table.setStatus("DISABLED");
					commonDao.store(table);
				}
			}
			if(viewMap.size()>0){
				Iterator<Entry<String, ItmsTable>> integer = viewMap.entrySet().iterator();
				while(integer.hasNext()){
					Entry<String, ItmsTable> entry = integer.next();
					ItmsTable table = entry.getValue();
					table.setDescription("not exits:"+UserHolder.getUser().getName());
					table.setStatus("DISABLED");
					commonDao.store(table);
				}
			}
			
		} catch(ClassNotFoundException e){
		    e.printStackTrace();
		    System.out.println("找不到服务!");
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
	//批量更新账号用户信息
	@SuppressWarnings("unchecked")
	public void updateWorker(Long locationId,String description,String type){
		ItmsTablespaces itmsTablespaces = commonDao.load(ItmsTablespaces.class, locationId);//表空间
		ItmsSid itmsSid = itmsTablespaces.getWarehouseArea();//表空间所在实例SID
		ItmsWarehouse warehouse = itmsSid.getWarehouse();//服务器IP
		//查询当前账号用户已维护表
		List<ItmsUsers> users = commonDao.findByQuery("FROM ItmsUsers w WHERE w.location.id =:locationId", 
				new String[]{"locationId"}, new Object[]{locationId});
		Map<String,ItmsUsers> userMap = new HashMap<String,ItmsUsers>();
		if(users!=null && users.size()>0){
			for(ItmsUsers user : users){
				userMap.put(user.getCode().toUpperCase(), user);
			}
		}
		
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
			
			sql = "select d.username,d.account_status,d.lock_date,d.expiry_date from "+ItmsUserTabs.DBA_USERS+" d"
					+ " where d.default_tablespace = upper('"+itmsTablespaces.getCode()+"')" +
					" and d.username not in ("+ItmsUserTabs.SYS_USERS+")  and d.account_status = 'OPEN'";
//			System.out.print(sql+"\n");
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				String username = rs.getString("username");
				String account_status = rs.getString("account_status");
				java.sql.Timestamp lock_date = rs.getTimestamp("lock_date");
				java.sql.Timestamp expiry_date = rs.getTimestamp("expiry_date");
//				System.out.println(username+","+account_status+","+lock_date+","+expiry_date);
				initUser(itmsTablespaces, description, type, userMap, username, account_status, lock_date, expiry_date);
				
				userMap.remove(username);
			}
			sql = "select d.username,d.account_status,d.lock_date,d.expiry_date" +
					" from "+ItmsUserTabs.DBA_USERS+" d where d.default_tablespace = 'USERS' and d.account_status = 'OPEN'" +
					" and d.username not in ("+ItmsUserTabs.SYS_USERS+")";
			//System.out.print(sql+"\n");
			rs.close();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				String username = rs.getString("username");
				String account_status = rs.getString("account_status");
				java.sql.Timestamp lock_date = rs.getTimestamp("lock_date");
				java.sql.Timestamp expiry_date = rs.getTimestamp("expiry_date");
//				System.out.println(username+","+account_status+","+lock_date+","+expiry_date);
				initUser(itmsTablespaces, description, type, userMap, username, account_status, lock_date, expiry_date);
				
				userMap.remove(username);
			}
			if(userMap.size()>0){
				Iterator<Entry<String, ItmsUsers>> integer = userMap.entrySet().iterator();
				while(integer.hasNext()){
					Entry<String, ItmsUsers> entry = integer.next();
					ItmsUsers user = entry.getValue();
					user.setDescription("not exits:"+UserHolder.getUser().getName());
					user.setStatus("DISABLED");
					commonDao.store(user);
				}
			}
		} catch(ClassNotFoundException e){
		    e.printStackTrace();
		    System.out.println("找不到服务!");
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
	private void initUser(ItmsTablespaces itmsTablespaces,String description,String type,Map<String,ItmsUsers> userMap
			,String username,String account_status,java.sql.Timestamp lock_date,java.sql.Timestamp expiry_date){
		ItmsUsers user = null;
		if(userMap.containsKey(username)){
			user = userMap.get(username);
		}else{
			user = EntityFactory.getEntity(ItmsUsers.class);
			user.setDescription(description);
		}
		user.setWarehouse(itmsTablespaces.getWarehouseArea().getWarehouse());
		user.setCode(username);//用户名
		user.setLocation(itmsTablespaces);//表空间
		user.setStatus("ENABLED");
		user.setAccountStatus(account_status);
		user.setLockDate(lock_date);
		user.setExpiryDate(expiry_date);
		user.setType(type);
//		user.setName();//密码
//		user.setSynonymName();//所属单位
		commonDao.store(user);
	}
	//表空间批量更新
	@SuppressWarnings("unchecked")
	public void updateWmsLocation(Long warehouseAreaId,String description){
		ItmsSid itmsSid = commonDao.load(ItmsSid.class, warehouseAreaId);
		ItmsWarehouse warehouse = itmsSid.getWarehouse();//服务器IP
		//查询当前表空间已维护表
		List<ItmsTablespaces> locations = commonDao.findByQuery("FROM ItmsTablespaces l WHERE l.warehouseArea.id =:sidId", 
				new String[]{"sidId"}, new Object[]{itmsSid.getId()});
		Map<String,ItmsTablespaces> userMap = new HashMap<String,ItmsTablespaces>();
		if(locations!=null && locations.size()>0){
			for(ItmsTablespaces l : locations){
				userMap.put(l.getCode().toUpperCase(), l);
			}
		}
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
			//查询表空间所在路径
			Map<String,String> tableSpacePath = new HashMap<String, String>();
			sql = "select t1.name as tablespace_name,t2.name as dbfPath" +
					" from "+ItmsUserTabs.V$TABLESPACE+" t1,"+ItmsUserTabs.V$DATAFILE+" t2 where t1.ts# = t2.ts#" +
					" and t1.name not in ("+ItmsUserTabs.SYS_TABLESPACE+")";
//			System.out.print(sql+"\n");
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				String tablespace_name = rs.getString("tablespace_name");
				String dbfPath = rs.getString("dbfPath");
				tableSpacePath.put(tablespace_name, dbfPath);
			}
			//查询表空间资源信息
			sql = "select dbf.tablespace_name as tablespace_name,"+
				" dbf.totalspace as totalspace,"+//总量(M)
				" dbf.totalblocks as totalblocks,"+//总块数
				" dfs.freespace freespace,"+//剩余总量(M)
				" dfs.freeblocks freeblocks,"+//剩余块数
				" (dfs.freespace / dbf.totalspace) * 100 as freeRate "+//空闲比例
				" from (select t.tablespace_name,"+
				" sum(t.bytes) / 1024 / 1024 totalspace,"+
				" sum(t.blocks) totalblocks"+
				" from "+ItmsUserTabs.DBA_DATA_FILES+" t"+
				" group by t.tablespace_name) dbf,"+
				" (select tt.tablespace_name,"+
				" sum(tt.bytes) / 1024 / 1024 freespace,"+
				" sum(tt.blocks) freeblocks"+
				" from "+ItmsUserTabs.DBA_FREE_SPACE+" tt"+
				" group by tt.tablespace_name) dfs"+
				" where trim(dbf.tablespace_name) = trim(dfs.tablespace_name)" +
				" and dbf.tablespace_name not in ("+ItmsUserTabs.SYS_TABLESPACE+")";
//			System.out.print(sql+"\n");
			rs.close();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				String tablespace_name = rs.getString("tablespace_name");
				double totalspace = rs.getDouble("totalspace");
				double totalblocks = rs.getDouble("totalblocks");
				double freespace = rs.getDouble("freespace");
				double freeblocks = rs.getDouble("freeblocks");
				double freeRate = rs.getDouble("freeRate");
				String dbfPath = tableSpacePath.get(tablespace_name);
				
				ItmsTablespaces l = null;
				if(userMap.containsKey(tablespace_name)){
					l = userMap.get(tablespace_name);
					l.setDescription(description);
				}else{
					l = EntityFactory.getEntity(ItmsTablespaces.class);
					l.setDescription(description);
				}
				l.setCode(tablespace_name);
				l.setWarehouseArea(itmsSid);
				l.setDbfPath(dbfPath);
				l.setZone(totalspace);
				l.setLine(totalblocks);
				l.setColumn(freespace);
				l.setLayer(freeblocks);
				l.setUsedRate(freeRate);
				l.setStatus(BaseStatus.ENABLED);
				commonDao.store(l);
				
				userMap.remove(tablespace_name);
//				System.out.println(tablespace_name+","+totalspace+","+totalblocks+","+freespace+","+freeblocks+","+freeRate);
			}
			if(userMap.size()>0){
				Iterator<Entry<String, ItmsTablespaces>> integer = userMap.entrySet().iterator();
				while(integer.hasNext()){
					Entry<String, ItmsTablespaces> entry = integer.next();
					ItmsTablespaces l = entry.getValue();
					l.setDescription("not exits:"+UserHolder.getUser().getName());
					l.setStatus(BaseStatus.DISABLED);
					commonDao.store(l);
				}
			}
		} catch(ClassNotFoundException e){
		    e.printStackTrace();
		    System.out.println("找不到服务!");
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
	public String getSessionHomeCode(Map map){
		return ItmsWarehouseHolder.getWmsWarehouse().getCode();
	}
	@SuppressWarnings("unchecked")
	public RowData[] getWarehouseArea(Map params){
		String hql = "SELECT sid.id,sid.code,sid.name,sid.warehouse.name"+
				" FROM ItmsSid sid WHERE sid.status='ENABLED' "+
				" AND sid.warehouse.id =:homeId";
		List<Object[]> objs = commonDao.findByQuery(hql, new String[]{"homeId"}, 
				new Object[]{ItmsWarehouseHolder.getWmsWarehouse().getId()});
		RowData[] rowDatas = new RowData[objs.size()];
		int index = 0;
		for(Object[] obj : objs){
			RowData rowData = new RowData();
			rowData.addColumnValue(obj[0]);
			rowData.addColumnValue(obj[1]);
			rowData.addColumnValue(obj[2]);
			rowData.addColumnValue(obj[3]);
			
			rowDatas[index++] = rowData;
		}
		return rowDatas;
		
	}
	public void addDbf(ItmsTablespaces itmsTablespaces){
		if(itmsTablespaces.getRouteNo()==null || itmsTablespaces.getRouteNo()<=0){
			LocalizedMessage.setMessage(MyUtils.font("失败!扩充基数有误!"));
		}else{
			ItmsWarehouse warehouse = itmsTablespaces.getWarehouseArea().getWarehouse();//服务器IP
			String ip = warehouse.getCode();
			String sid = itmsTablespaces.getWarehouseArea().getName();
			String userName = JDBCUtils.SYS_NAME;
			String pass = StringUtils.isEmpty(itmsTablespaces.getWarehouseArea().getSysPass())?JDBCUtils.SYS_PASS:itmsTablespaces.getWarehouseArea().getSysPass();
			String sql = null,dbfName = "",dbfExpend = "";
			StringBuffer sf = new StringBuffer();
			//登陆
			ResultSet rs = null;//结果集
			Statement stmt = null;//结果集通道
			Connection conn = null;//数据库连接
			try {
				Class.forName(JDBCUtils.CLASS_NAME); 
				String url = JDBCUtils.getUrl(ip, sid);
				conn = DriverManager.getConnection(url,userName,pass);
				stmt = conn.createStatement(); 
				sf.append("ALTER TABLESPACE ").append(itmsTablespaces.getCode());
				sf.append(" ADD DATAFILE '");
				sf.append(StringUtils.substringBeforeLast(itmsTablespaces.getDbfPath(), "\\")).append("\\");
				dbfName = StringUtils.substringAfterLast(itmsTablespaces.getDbfPath(), "\\");//JAC_SCL_001.DBF
				dbfExpend = StringUtils.substringBefore(dbfName, ".");
				sf.append(dbfExpend).append(JavaTools.numberCode(4)).append(".DBF'");
				sf.append(" SIZE ").append(itmsTablespaces.getRouteNo()).append("M");
				sql = sf.toString();
//				System.out.println(sql);
				stmt.executeUpdate(sql);
			} catch(ClassNotFoundException e){
			    e.printStackTrace();
			    System.out.println("找不到服务!");
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
			//updateWmsLocation批量更新
			updateWmsLocation(itmsTablespaces.getWarehouseArea().getId(), "扩充刷新");
		}
		
	}
	//权限表名称系统自带
	public void synonymName(ItmsTable table){
		String synonymPrefix = table.getUser().getSynonymName();//所属公司,同义词前缀
        String synonymTabName = table.getCode();//同义词表名称
        //同义词名称(前缀是为了防止不同用户表名相同无法创建问题,对不同用户前缀相同且表名也相同的,额外增加手工维护权限表功能)
		String synonymName = synonymPrefix+"_"+synonymTabName;
		synonymNameDo(table,synonymName);
	}
	private void synonymNameDo(ItmsTable table,String synonymName){
		ItmsUsers user = table.getUser();
		ItmsTablespaces itmsTablespaces = user.getLocation();//账号所在表空间
		ItmsWarehouse warehouse = itmsTablespaces.getWarehouseArea().getWarehouse();//服务器IP
		String ip = warehouse.getCode();
		String sid = itmsTablespaces.getWarehouseArea().getName();
		String userName = JDBCUtils.SYS_NAME;
		String pass = StringUtils.isEmpty(itmsTablespaces.getWarehouseArea().getSysPass())?JDBCUtils.SYS_PASS:itmsTablespaces.getWarehouseArea().getSysPass();
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
			
			String synonymUserName = user.getCode();//同义词表所属账号
	        String synonymTabName = table.getCode();//同义词表名称
	        sql = "SELECT count(*) as rowCount FROM "+ItmsUserTabs.ALL_TABLES+" a" +
	        		" WHERE a.OWNER=upper('"+synonymUserName+"')"
					+ " AND a.TABLESPACE_NAME = upper('"+user.getLocation().getCode()+"')" +
					  " AND a.TABLE_NAME = upper('"+synonymTabName+"')";
			//System.out.print(sql+"\n");
			rs = stmt.executeQuery(sql);
			rs.next();
			int rowCount = rs.getInt("rowCount");
			if(rowCount==0){
				throw new BusinessException(synonymUserName+"."+synonymTabName+"表不存在,表信息未更新");
			}
			synonymName = synonymName.toUpperCase();
	        if(synonymName.length()>=30){//oracle表名长度不能大于30,否则同义词名称会序列化
	        	throw new BusinessException(synonymUserName+"."+synonymTabName+"表太长,请手工定义权限表(长度26以内)");//要求手工维护
	        }
	        //查询当前账号下的同义词
	        sql = "SELECT count(*) as rowCount FROM "+ItmsUserTabs.ALL_SYNONYMS+"" +
	        		" WHERE table_name = upper('"+synonymTabName+"')" +
	        		" and table_owner = upper('"+synonymUserName+"')";
	        rs.close();
	        rs = stmt.executeQuery(sql);
			rs.next();
			rowCount = rs.getInt("rowCount");
			if(rowCount==0){
//				System.out.print(sql+"\n");
				//似乎存在同义词名相同的但所属用户不同的概率,要求手工定义同义词
				sql = "SELECT count(*) as rowCount FROM "+ItmsUserTabs.ALL_SYNONYMS+"" +
			        		" WHERE 1=1" +
			        		" AND synonym_name = upper('"+synonymName+"')";
//				System.out.print(sql+"\n");
				rs.close();
		        rs = stmt.executeQuery(sql);
				rs.next();
				rowCount = rs.getInt("rowCount");
				if(rowCount==0){//new synonym
					sql = "create public synonym "+synonymName+" for "+synonymUserName+"."+synonymTabName;//公有
//					System.out.print(sql+"\n");
					rs.close();
					rs = stmt.executeQuery(sql);
					conn.commit();
				}else{
					throw new BusinessException(synonymUserName+"."+synonymTabName+"权限表已存在同名,请手工定义权限表(长度26以内)");//要求手工维护
				}
			}else{
				Boolean isNone = false;
				sql = "SELECT owner,synonym_name FROM "+ItmsUserTabs.ALL_SYNONYMS+"" +
						" WHERE table_name = upper('"+synonymTabName+"')" +
						" and TABLE_OWNER = upper('"+synonymUserName+"')";
				rs.close();
				rs = stmt.executeQuery(sql);
				while(rs.next()){
					String owner = rs.getString("owner");
					String synonym_name = rs.getString("synonym_name");
					//查看是否存在公共的
					if("PUBLIC".equals(owner.toUpperCase())){
						synonymName = synonym_name;
					}else{//一旦发现之外的,要求删除同义词之后再分权限,系统原则是要保持一致(一个表一个同义词)且为PUBLIC的
						isNone = true;
						break;
					}
				}
				if(isNone){//说明该用户下的表创建的同义词该用户无法使用(私有的,比如sys账户或者别的账户),请到联系ODD管理员解决
					//drop synonym synonymName; drop public synonym synonymName
					//drop public synonym "/f7df3ed7_ITDEP_MIDDLE_DELIVER";
					//SELECT  'drop synonym ' || s.synonym_name||';' FROM SYS.ALL_SYNONYMS s WHERE TABLE_OWNER = 'MIDDLETABLE' AND owner <> 'PUBLIC'
					throw new BusinessException(synonymUserName+"."+synonymTabName+"请联系ODD管理员解决");
				}
			}
			table.setSynonymName(synonymName);
			commonDao.store(table);
		}catch(ClassNotFoundException e){
		    e.printStackTrace();
		    System.out.println("找不到服务!");
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
	public void synonymHand(ItmsTable table){
		String synonymName = table.getSynonymName().toUpperCase();
		if(synonymName.startsWith(ItmpDeptNumber.FDJ.toUpperCase())
				|| synonymName.startsWith(ItmpDeptNumber.HT.toUpperCase())
				|| synonymName.startsWith(ItmpDeptNumber.IT_DEP.toUpperCase())
				|| synonymName.startsWith(ItmpDeptNumber.PCL.toUpperCase())
				|| synonymName.startsWith(ItmpDeptNumber.SCL.toUpperCase())
				|| synonymName.startsWith(ItmpDeptNumber.PROJECT.toUpperCase())
				|| synonymName.startsWith(ItmpDeptNumber.OTHER.toUpperCase())){
			throw new BusinessException("自定义前缀后半部分,且长度26以内");
		}
		synonymNameDo(table,synonymName);
	}
	@SuppressWarnings("unchecked")
	public void deleteTable(ItmsTable table){
		List<Long> mces = commonDao.findByQuery("SELECT mce.id FROM ItmsAuthority mce WHERE mce.table.id =:tableId", 
				new String[]{"tableId"}, new Object[]{table.getId()});
		if(mces.size()>0){
			throw new BusinessException(table.getCode()+":权限管理已授权,请先删除该表数据");
		}
		List<IntervalTimes> times = commonDao.findByQuery("FROM IntervalTimes l WHERE l.iLog.zone.id =:tableId", 
				new String[]{"tableId"}, new Object[]{table.getId()});
		commonDao.deleteAll(times);
		
		List<IntervalLog> logs = commonDao.findByQuery("FROM IntervalLog l WHERE l.zone.id =:tableId", 
				new String[]{"tableId"}, new Object[]{table.getId()});
		commonDao.deleteAll(logs);
	}
	
	public void newDbLink(ItmsDbLinks link,String isPrivate,Long ownerId){
		String host = link.getHost();
		ItmsSid itmsSid = link.getSid();
		String ip = ItmsWarehouseHolder.getWmsWarehouse().getCode();
		String sidName = itmsSid.getName();
		String owner = ItmsDbLinkOwner.PUBLIC;
		String userName = JDBCUtils.SYS_NAME;
		String pass = StringUtils.isEmpty(itmsSid.getSysPass())?JDBCUtils.SYS_PASS:itmsSid.getSysPass();
		String sql = "create "+owner+" database link "+link.getDbLilnk()+" connect to "+link.getUserName()+" identified by "+link.getPassWord()+" using '"+host+"'";
		if(ItmsDbLinkOwner.PRIVATE.equals(isPrivate)){
			sql = "create database link "+link.getDbLilnk()+" connect to "+link.getUserName()+" identified by "+link.getPassWord()+" using '"+host+"'";
			
			ItmsUsers user = commonDao.load(ItmsUsers.class, ownerId);
			userName = user.getCode();
			pass = user.getName();
			owner = userName;
		}
//		System.out.print(sql+"\n");
		//grant create public database link,create database link to itms_info;
		//登陆
		ResultSet rs = null;//结果集
		Statement stmt = null;//结果集通道
		Connection conn = null;//数据库连接
		try {
			Class.forName(JDBCUtils.CLASS_NAME); 
			String url = JDBCUtils.getUrl(ip, sidName);
			conn = DriverManager.getConnection(url,userName,pass);
			stmt = conn.createStatement(); 
	    	rs = stmt.executeQuery(sql);
			conn.commit();
			
			link.setOwner(owner);
			commonDao.store(link);
		} catch(ClassNotFoundException e){
		    e.printStackTrace();
		    System.out.println("找不到服务!");
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
	
	public void deleteDblink(ItmsDbLinks link){
		ItmsSid itmsSid = link.getSid();
		String ip = ItmsWarehouseHolder.getWmsWarehouse().getCode();
		String sidName = itmsSid.getName();
		
		String userName = JDBCUtils.SYS_NAME;
		String pass = StringUtils.isEmpty(itmsSid.getSysPass())?JDBCUtils.SYS_PASS:itmsSid.getSysPass();
		String sql = null;
		if(!ItmsDbLinkOwner.PUBLIC.equals(link.getOwner().toLowerCase())){
			ItmsUsers user = (ItmsUsers) commonDao.findByQueryUniqueResult("FROM ItmsUsers u WHERE u.code =:name" +
					" and u.location.warehouseArea.name =:sidname" +
					" and u.location.warehouseArea.warehouse.code =:server", 
					new String[]{"name","sidname","server"}, 
					new Object[]{link.getOwner(),sidName,ip});
			if(user==null){
				throw new BusinessException("账号不存在:"+link.getOwner()+","+sidName+","+ip);
			}else if(user.getName()==null){
				throw new BusinessException("账号密码为空:"+link.getOwner()+","+sidName+","+ip);
			}
			userName = user.getCode();
			pass = user.getName();
		}
		//登陆
		ResultSet rs = null;//结果集
		Statement stmt = null;//结果集通道
		Connection conn = null;//数据库连接
		try {
			String dbLink = link.getDbLilnk().toUpperCase();
			Class.forName(JDBCUtils.CLASS_NAME); 
			String url = JDBCUtils.getUrl(ip, sidName);
			conn = DriverManager.getConnection(url,userName,pass);
			stmt = conn.createStatement(); 
			sql = "select count(*) as rowCount from "+ItmsUserTabs.DBA_DB_LINKS+" u" +
					" where u.db_link = '"+dbLink+"' and u.owner = '"+link.getOwner().toUpperCase()+"'";
//			System.out.print(sql+"\n");
			rs = stmt.executeQuery(sql);
			rs.next();
			int rowCount = rs.getInt("rowCount");
		    if(rowCount>0){
		    	if(!ItmsDbLinkOwner.PUBLIC.equals(link.getOwner().toLowerCase())){
		    		sql = "drop database link "+dbLink;
		    	}else{
		    		sql = "drop public database link "+dbLink;
		    	}
//		    	System.out.print(sql+"\n");
		    	rs.close();
		    	rs = stmt.executeQuery(sql);
				conn.commit();
		    }
		} catch(ClassNotFoundException e){
		    e.printStackTrace();
		    System.out.println("找不到服务!");
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
	@SuppressWarnings("unchecked")
	public void initDblink(Long sidId,String description){
		ItmsSid itmsSid = commonDao.load(ItmsSid.class, sidId);;
		String ip = ItmsWarehouseHolder.getWmsWarehouse().getCode();
		String sidName = itmsSid.getName();
		
		String userName = JDBCUtils.SYS_NAME;
		String pass = StringUtils.isEmpty(itmsSid.getSysPass())?JDBCUtils.SYS_PASS:itmsSid.getSysPass();
		String sql = null;
		//登陆
		ResultSet rs = null;//结果集
		Statement stmt = null;//结果集通道
		Connection conn = null;//数据库连接
		try {
			String key = null;
			List<ItmsDbLinks> links = commonDao.findByQuery("FROM ItmsDbLinks l WHERE l.sid.id =:sidId", 
					new String[]{"sidId"}, new Object[]{sidId});
			Map<String,Long> dblink = new HashMap<String,Long>();
			for(ItmsDbLinks l : links){
				key = l.getDbLilnk().toUpperCase()+l.getOwner().toUpperCase();
				dblink.put(key, l.getId());
			}
			
			Class.forName(JDBCUtils.CLASS_NAME); 
			String url = JDBCUtils.getUrl(ip, sidName);
			conn = DriverManager.getConnection(url,userName,pass);
			stmt = conn.createStatement(); 
			sql = "select u.owner,u.db_link,u.username,u.host from "+ItmsUserTabs.DBA_DB_LINKS+" u";
//			System.out.print(sql+"\n");
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				String owner = rs.getString("owner");
				String dbLilnk = rs.getString("db_link");
				String name = rs.getString("username");
				String host = rs.getString("host");
				key = dbLilnk+owner;
				ItmsDbLinks link = null;
				if(dblink.containsKey(key)){
					link = commonDao.load(ItmsDbLinks.class, dblink.get(key));
				}else{
					link = EntityFactory.getEntity(ItmsDbLinks.class);
					link.setSid(itmsSid);
					link.setPassWord("初始化");
					link.setDescription(description);
				}
				link.setDbLilnk(dbLilnk);
				link.setOwner(owner);
				link.setHost(host);
				link.setUserName(name);
				commonDao.store(link);
				dblink.remove(key);
			}
			if(!dblink.isEmpty()){
				Iterator<Entry<String, Long>> iter = dblink.entrySet().iterator();
				while(iter.hasNext()){
					Entry<String, Long> entry = iter.next();
					ItmsDbLinks link = commonDao.load(ItmsDbLinks.class,entry.getValue());
					commonDao.delete(link);
				}
			}
		} catch(ClassNotFoundException e){
		    e.printStackTrace();
		    System.out.println("找不到服务!");
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
	@SuppressWarnings("unchecked")
	public void initSynonyms(Long sidId){
		String hql = "FROM ItmsSynonyms syno WHERE syno.warehouse.id =:id" +
				" AND syno.itmsSid.id =:itmsSid";
		List<ItmsSynonyms> synos = commonDao.findByQuery(hql, new String[]{"id","itmsSid"}, 
				new Object[]{ItmsWarehouseHolder.getWmsWarehouse().getId(),sidId});
		if(synos!=null && synos.size()>0){
			commonDao.deleteAll(synos);
		}
		
		List<String> us = new ArrayList<String>();
		hql = "SELECT u.code FROM ItmsUsers u" +
				" LEFT JOIN u.location tablespaces" +
				" LEFT JOIN u.location.warehouseArea sid" +
				" LEFT JOIN u.location.warehouseArea.warehouse warehouse" +
				" WHERE 1=1" +
				" AND sid.warehouse.id =:id" +
				" AND u.type = 'PERSON' GROUP BY u.code";
		List<String> userNames = commonDao.findByQuery(hql, "id", ItmsWarehouseHolder.getWmsWarehouse().getId());
		if(userNames!=null && userNames.size()>0){
			for(String u : userNames){
				us.add("'"+u.trim().toUpperCase()+"'");
			}
		}
		
		ItmsSid itmsSid = commonDao.load(ItmsSid.class, sidId);;
		String ip = ItmsWarehouseHolder.getWmsWarehouse().getCode();
		String sidName = itmsSid.getName();
		
		String userName = JDBCUtils.SYS_NAME;
		String pass = StringUtils.isEmpty(itmsSid.getSysPass())?JDBCUtils.SYS_PASS:itmsSid.getSysPass();
		String sql = null;
		//登陆
		ResultSet rs = null;//结果集
		Statement stmt = null;//结果集通道
		Connection conn = null;//数据库连接
		try {
			Class.forName(JDBCUtils.CLASS_NAME); 
			String url = JDBCUtils.getUrl(ip, sidName);
			conn = DriverManager.getConnection(url,userName,pass);
			stmt = conn.createStatement(); 
			sql = "SELECT a.owner,a.synonym_name,a.table_owner,a.table_name FROM "+ItmsUserTabs.ALL_SYNONYMS+" a" +
					" WHERE 1=1 AND a.table_owner in" +
					" ("+StringUtils.substringBetween(us.toString(), "[", "]")+") ORDER BY a.table_owner";
//			System.out.print(sql+"\n");
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				String owner = rs.getString("owner");
				String synonymName = rs.getString("synonym_name");
				String tableOwner = rs.getString("table_owner");
				String tableName = rs.getString("table_name");
				ItmsSynonyms syno = new ItmsSynonyms(ItmsWarehouseHolder.getWmsWarehouse(),itmsSid, owner, synonymName, tableOwner, tableName);
				commonDao.store(syno);
			}
		} catch(ClassNotFoundException e){
		    e.printStackTrace();
		    System.out.println("找不到服务!");
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
	public void dropSynonyms(ItmsSynonyms syno){
		ItmsSid itmsSid = commonDao.load(ItmsSid.class, syno.getItmsSid().getId());
		String ip = ItmsWarehouseHolder.getWmsWarehouse().getCode();
		String sidName = itmsSid.getName();
		
		String userName = JDBCUtils.SYS_NAME;
		String pass = StringUtils.isEmpty(itmsSid.getSysPass())?JDBCUtils.SYS_PASS:itmsSid.getSysPass();
		String sql = null;
		//登陆
		ResultSet rs = null;//结果集
		Statement stmt = null;//结果集通道
		Connection conn = null;//数据库连接
		try {
			Class.forName(JDBCUtils.CLASS_NAME); 
			String url = JDBCUtils.getUrl(ip, sidName);
			conn = DriverManager.getConnection(url,userName,pass);
			stmt = conn.createStatement(); 
			
			if("PUBLIC".equals(syno.getOwner())){
				sql = "drop public synonym ["+syno.getSynonymName()+"]";
			}else{
				sql = "drop synonym ["+syno.getSynonymName()+"]";
			}
			sql = StringUtils.replace(sql, "[", "\"");
			sql = StringUtils.replace(sql, "]", "\"");
			rs = stmt.executeQuery(sql);
			conn.commit();
//			System.out.print(sql+"\n");
		} catch(ClassNotFoundException e){
		    e.printStackTrace();
		    System.out.println("找不到服务!");
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
	
	@SuppressWarnings("unchecked")
	public void initUserOnOff(Long sidId){
		String hql = "FROM ItmsUserOnOff ufo WHERE ufo.warehouse.id =:id" +
				" AND ufo.itmsSid.id =:itmsSid";
		List<ItmsUserOnOff> ufos = commonDao.findByQuery(hql, new String[]{"id","itmsSid"}, 
				new Object[]{ItmsWarehouseHolder.getWmsWarehouse().getId(),sidId});
		if(ufos!=null && ufos.size()>0){
			commonDao.deleteAll(ufos);
		}
		
		ItmsSid itmsSid = commonDao.load(ItmsSid.class, sidId);;
		String ip = ItmsWarehouseHolder.getWmsWarehouse().getCode();
		String sidName = itmsSid.getName();
		
		String userName = JDBCUtils.SYS_NAME;
		String pass = StringUtils.isEmpty(itmsSid.getSysPass())?JDBCUtils.SYS_PASS:itmsSid.getSysPass();
		String sql = null;
		//登陆
		ResultSet rs = null;//结果集
		Statement stmt = null;//结果集通道
		Connection conn = null;//数据库连接
		try {
			Class.forName(JDBCUtils.CLASS_NAME); 
			String url = JDBCUtils.getUrl(ip, sidName);
			conn = DriverManager.getConnection(url,userName,pass);
			stmt = conn.createStatement(); 
			sql = "SELECT a.id,a.username,to_char(a.log_time,'yyyy-MM-dd hh24:mi:ss') as log_time,a.onoff,a.address" +
					" FROM "+ItmsUserTabs.USER_LOG_TABLE+" a" +
					" WHERE trunc(to_date(to_char(sysdate,'yyyy-MM-dd') ,'yyyy-MM-dd') - to_date(to_char(a.log_time,'yyyy-MM-dd'),'yyyy-MM-dd'),4) <= 5" +
					" ORDER BY a.id";
//			System.out.print(sql+"\n");
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				Long userLogId = rs.getLong("id");
				String username = rs.getString("username");
				String logTime = rs.getString("log_time");
				String onOff = rs.getString("onoff");
				String address = rs.getString("address");
				ItmsUserOnOff ufo = new ItmsUserOnOff(ItmsWarehouseHolder.getWmsWarehouse(), itmsSid, userLogId, username, logTime, onOff, address);
				commonDao.store(ufo);
			}
			//清空30天之前的数据
			sql = "DELETE FROM "+ItmsUserTabs.USER_LOG_TABLE+" a WHERE trunc(to_date(to_char(sysdate,'yyyy-MM-dd') ,'yyyy-MM-dd') - to_date(to_char(a.log_time,'yyyy-MM-dd'),'yyyy-MM-dd'),4) > 30";
			stmt.execute(sql);
			
		} catch(ClassNotFoundException e){
		    e.printStackTrace();
		    System.out.println("找不到服务!");
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
	
	public void deleteUserOnOff(List<ItmsUserOnOff> ufos){
		Map<Long,Long> sids = new HashMap<Long, Long>();
		List<Long> userLogIds = new ArrayList<Long>();
		for(ItmsUserOnOff ufo : ufos){
			sids.put(ufo.getItmsSid().getId(), ufo.getItmsSid().getId());
			userLogIds.add(ufo.getUserLogId());
		}
		if(sids.size()>1){
			throw new BusinessException("不允许选中不同SID名称的数据删除!");
		}
		
		ItmsSid itmsSid = commonDao.load(ItmsSid.class, ufos.get(0).getItmsSid().getId());
		String ip = ItmsWarehouseHolder.getWmsWarehouse().getCode();
		String sidName = itmsSid.getName();
		
		String userName = JDBCUtils.SYS_NAME;
		String pass = StringUtils.isEmpty(itmsSid.getSysPass())?JDBCUtils.SYS_PASS:itmsSid.getSysPass();
		String sql = null;
		//登陆
		ResultSet rs = null;//结果集
		PreparedStatement stmt = null;//结果集通道
		Connection conn = null;//数据库连接
		try {
			Class.forName(JDBCUtils.CLASS_NAME); 
			String url = JDBCUtils.getUrl(ip, sidName);
			conn = DriverManager.getConnection(url,userName,pass);
			
			sql = "DELETE FROM "+ItmsUserTabs.USER_LOG_TABLE+" a WHERE a.id IN" +
					" ("+StringUtils.substringBetween(userLogIds.toString(), "[", "]")+")";
			stmt = conn.prepareStatement(sql);
//			stmt.setLong(1,ufo.getUserLogId());
			stmt.execute();
			conn.commit();
//			System.out.print(sql+"\n");
		} catch(ClassNotFoundException e){
		    e.printStackTrace();
		    System.out.println("找不到服务!");
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
}