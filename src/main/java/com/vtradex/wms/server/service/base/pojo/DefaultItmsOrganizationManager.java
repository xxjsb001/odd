package com.vtradex.wms.server.service.base.pojo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.model.itms.IntervalLog;
import com.vtradex.wms.server.model.itms.IntervalTimes;
import com.vtradex.wms.server.model.itms.ItmsSqlTaskType;
import com.vtradex.wms.server.model.itms.UserLeavel;
import com.vtradex.wms.server.model.middle.ItmsAuthority;
import com.vtradex.wms.server.model.organization.TypeOfBill;
import com.vtradex.wms.server.model.warehouse.ItmsIonizationIndex;
import com.vtradex.wms.server.model.warehouse.ItmsIonizationTables;
import com.vtradex.wms.server.model.warehouse.ItmsTable;
import com.vtradex.wms.server.model.warehouse.ItmsTablespaces;
import com.vtradex.wms.server.model.warehouse.ItmsWarehouse;
import com.vtradex.wms.server.model.warehouse.ItmsSid;
import com.vtradex.wms.server.model.warehouse.ItmsUsers;
import com.vtradex.wms.server.service.base.ItmsOrganizationManager;
import com.vtradex.wms.server.service.interfaceLog.ItmsLogManager;
import com.vtradex.wms.server.utils.ItmsUserTabs;
import com.vtradex.wms.server.utils.JDBCUtils;

public class DefaultItmsOrganizationManager extends DefaultBaseManager implements ItmsOrganizationManager {

	public static final String BEAN = "itmsOrganizationManager.";
	
	private JdbcTemplate jdbcDataSource;
	
	protected ItmsLogManager itmsLogManager;
	public DefaultItmsOrganizationManager(ItmsLogManager itmsLogManager){
		this.itmsLogManager = itmsLogManager;
	}
	
	
	public void saveUsers(ItmsUsers user){
		if(user.isNew()){
			System.out.println("new....");
		}
		commonDao.store(user);
	}
	public void insertRuleUpdate(Long workId,ItmsTable table){
		addWmsWorkerRuleUpdate(TypeOfBill.INSERT, workId, table);
	}
	public void deleteRuleUpdate(Long workId, ItmsTable table) {
		addWmsWorkerRuleUpdate(TypeOfBill.DELETE, workId, table);
	}
	public void updateRuleUpdate(Long workId, ItmsTable table) {
		addWmsWorkerRuleUpdate(TypeOfBill.UPDATE, workId, table);
	}
	public void selectRuleUpdate(Long workId, ItmsTable table) {
		addWmsWorkerRuleUpdate(TypeOfBill.SELECT, workId, table);
	}
	private void addWmsWorkerRuleUpdate(String sender,Long workId,ItmsTable table){
		ItmsAuthority mce = EntityFactory.getEntity(ItmsAuthority.class);
		mce.setSender(sender);
		mce.setTable(table);
		addWmsWorkerRule(workId, mce);
	}
	public void addWmsWorkerRule(Long workId,ItmsAuthority mce){
		mce.setWorker(commonDao.load(ItmsUsers.class, workId));
		commonDao.store(mce);
	}
	public void removeWmsWorkerRule(ItmsAuthority mce){
		deleteMiddleCompanyExtends(mce);
		commonDao.delete(mce);
	}
	@SuppressWarnings("unchecked")
	public void enableWorkUser(ItmsUsers wk){
		//查询账号是否存在
		ItmsTablespaces itmsTablespaces = wk.getLocation();//账号所在表空间
		ItmsSid itmsSid = itmsTablespaces.getWarehouseArea();//表空间所在实例SID
		ItmsWarehouse warehouse = itmsSid.getWarehouse();//服务器IP
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
			
			String user_Name = wk.getCode();
			String password = wk.getName();
			String tablespace_name = itmsTablespaces.getCode();
			
			sql = "select count(*) as rowCount from "+ItmsUserTabs.ALL_USERS+" u" +
					" where u.username = upper('"+user_Name+"')";
			rs = stmt.executeQuery(sql);
			rs.next();
			int rowCount = rs.getInt("rowCount");
			//System.out.print(sql+"\n");
		    if(rowCount==0){//new
		    	sql = "create user "+user_Name+" identified by "+password;
//		    	System.out.print(sql+"\n");
		    	rs.close();
		    	rs = stmt.executeQuery(sql);
				conn.commit();
				
				sql = "alter user "+user_Name+" default tablespace "+tablespace_name;
//		    	System.out.print(sql+"\n");
				rs.close();
		    	rs = stmt.executeQuery(sql);
				conn.commit();
				
				if(UserLeavel.OPERATION.equals(wk.getUserLeavel())
						 || UserLeavel.DBA.equals(wk.getUserLeavel())){
					sql = "grant create session,create table,create view,create sequence,unlimited tablespace to "+user_Name;
					//System.out.print(sql+"\n");
					rs.close();
					rs = stmt.executeQuery(sql);
					conn.commit();
					
					sql = "grant execute any procedure to "+user_Name;
					//System.out.print(sql+"\n");
					rs.close();
					rs = stmt.executeQuery(sql);
					conn.commit();
					
					sql = "grant create public database link,create database link to "+user_Name;
					//System.out.print(sql+"\n");
					rs.close();
					rs = stmt.executeQuery(sql);
					conn.commit();
					
					if(UserLeavel.DBA.equals(wk.getUserLeavel())){
						sql = "grant DBA to "+user_Name;
						//System.out.print(sql+"\n");
			    		rs.close();
						rs = stmt.executeQuery(sql);
						conn.commit();
					}
		    	}else{//其余默认普通用户
		    		sql = "grant create session to "+user_Name;
					//System.out.print(sql+"\n");
		    		rs.close();
					rs = stmt.executeQuery(sql);
					conn.commit();
		    	}
		    }else{//edit
		    	if(password!=null && !password.equals("-")){//更新账号维护的密码
		    		sql = "alter user "+user_Name+" identified by "+wk.getName();
		    		rs.close();
		    		rs = stmt.executeQuery(sql);
			    	//System.out.print(sql+"\n");
					conn.commit();
		    	}
		    }
		    List<ItmsAuthority> mces = commonDao.findByQuery("FROM ItmsAuthority mce WHERE mce.worker.id =:workerId", 
					new String[]{"workerId"}, new Object[]{wk.getId()});
		    //授权(验证表和同义词是否存在)
		    if(mces!=null && mces.size()>0){
				for(ItmsAuthority mce : mces){
					String sender = mce.getSender();//权限 TypeOfBill
					ItmsTable table = mce.getTable();//原始表
					ItmsUsers user = table.getUser();//原始表所属账号
			        String synonymPrefix = user.getSynonymName();//所属公司,同义词前缀
			        if(synonymPrefix==null || synonymPrefix.equals("-")){
			        	throw new BusinessException("账号["+user.getCode()+"]未设置所属公司");
			        }
			        String synonymUserName = user.getCode();//同义词表所属账号
			        String synonymTabName = table.getCode();//同义词表名称
			        
			        sql = "SELECT count(*) as rowCount FROM "+ItmsUserTabs.ALL_TABLES+"" +
			        		" a WHERE a.OWNER=upper('"+synonymUserName+"')"
							+ " AND a.TABLESPACE_NAME = upper('"+user.getLocation().getCode()+"')" +
							  " AND a.TABLE_NAME = upper('"+synonymTabName+"')";
					//System.out.print(sql+"\n");
			        rs.close();
					rs = stmt.executeQuery(sql);
					rs.next();
					rowCount = rs.getInt("rowCount");
					if(rowCount==0){
						throw new BusinessException(synonymUserName+"."+synonymTabName+"表不存在,表信息未更新");
					}
			        
			        String synonymName = table.getSynonymName().toUpperCase();//同义词名称
			        //查询当前账号下的同义词
			        sql = "SELECT count(*) as rowCount FROM "+ItmsUserTabs.ALL_SYNONYMS+"" +
			        		" WHERE table_name = upper('"+synonymTabName+"')" +
			        		" and table_owner = upper('"+synonymUserName+"') and synonym_name = upper('"+synonymName+"')" +
			        		" and owner = 'PUBLIC'";
			        rs.close();
			        rs = stmt.executeQuery(sql);
					rs.next();
					rowCount = rs.getInt("rowCount");
					if(rowCount==0){//new synonym
//						System.out.print(sql+"\n");
						throw new BusinessException(synonymUserName+"."+synonymTabName+"授权表不存在,权限表未更新");
					}
					//授权
					sql = "grant "+sender+" on "+synonymName+" to "+user_Name;
					//System.out.print(sql+"\n");
					rs.close();
					rs = stmt.executeQuery(sql);
					conn.commit();
					//将同义词信息存到权限表中
					mce.setSynonymName(synonymName);
					commonDao.store(mce);
				}
			}
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
	//删除权限,收回账号对应的权限
	public void deleteMiddleCompanyExtends(ItmsAuthority mce){
		ItmsUsers user = mce.getWorker();//账号
		String sender = mce.getSender();//权限
		String synonymName = mce.getSynonymName();//同义词表
		//参数
		ItmsTablespaces itmsTablespaces = user.getLocation();//账号所在表空间
		ItmsSid itmsSid = itmsTablespaces.getWarehouseArea();//表空间所在实例SID
		ItmsWarehouse warehouse = itmsSid.getWarehouse();//服务器IP
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
			
			String synonymUserName = mce.getTable().getUser().getCode();//同义词表所属账号
	        String synonymTabName = mce.getTable().getCode();//同义词表名称
	        
	        //查询当前账号下的同义词
	        sql = "SELECT count(*) as rowCount FROM "+ItmsUserTabs.ALL_SYNONYMS+"" +
	        		" WHERE table_name = upper('"+synonymTabName+"')" +
	        		" and table_owner = upper('"+synonymUserName+"')" +
	        		" and synonym_name = upper('"+synonymName+"')" +
	        		" and owner = 'PUBLIC'";
	        rs = stmt.executeQuery(sql);
			rs.next();
			int rowCount = rs.getInt("rowCount");
			if(rowCount>0){
				sql = "revoke "+sender+" on "+synonymName+" from "+user.getCode();
				//System.out.print(sql+"\n");
				rs.close();
				try {//如同义词存在,但该表已经不具有同义词的任何权限,简单的删除,不抛异常
					rs = stmt.executeQuery(sql);
					conn.commit();
				} catch (Exception e) {
				}
			}
		} catch(ClassNotFoundException e){
		    e.printStackTrace();
		    System.out.println("找不到服务!");
		    throw new BusinessException(e.getLocalizedMessage());
	   } catch(SQLException e){
		   	e.printStackTrace();
		   	throw new BusinessException(e.getLocalizedMessage());
	   } catch(Exception e){
		   	e.printStackTrace();
		   	throw new BusinessException(e.getLocalizedMessage());
	   } finally{
		   JDBCUtils.free(rs, stmt, conn);
	   }
	}
	@SuppressWarnings("unchecked")
	public void deleteWorkUser(ItmsUsers wk){
		List<Long> mces = commonDao.findByQuery("SELECT mce.id FROM ItmsAuthority mce WHERE mce.worker.id =:workerId", 
				new String[]{"workerId"}, new Object[]{wk.getId()});
		if(mces.size()>0){
			throw new BusinessException(wk.getCode()+":权限管理已授权,请先删除该账号数据");
		}
		List<IntervalTimes> times = commonDao.findByQuery("FROM IntervalTimes l WHERE l.iLog.zone.user.id =:workerId", 
				new String[]{"workerId"}, new Object[]{wk.getId()});
		commonDao.deleteAll(times);
		
		List<IntervalLog> logs = commonDao.findByQuery("FROM IntervalLog l WHERE l.zone.user.id =:workerId", 
				new String[]{"workerId"}, new Object[]{wk.getId()});
		commonDao.deleteAll(logs);
		
		List<ItmsTable> tabs = commonDao.findByQuery("FROM ItmsTable tab WHERE tab.user.id =:workerId", 
				new String[]{"workerId"}, new Object[]{wk.getId()});
		commonDao.deleteAll(tabs);
		
		commonDao.delete(wk);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Boolean ionizationTable(Map map){
		Long parentId = Long.parseLong(map.get("parentId").toString());
		ItmsUsers user = commonDao.load(ItmsUsers.class, parentId);
		ItmsTablespaces itmsTablespaces = user.getLocation();//账号所在表空间
		ItmsSid itmsSid = itmsTablespaces.getWarehouseArea();//表空间所在实例SID
		ItmsWarehouse warehouse = itmsSid.getWarehouse();//服务器IP
		
		String ip = warehouse.getCode();
		String sid = itmsSid.getName();
		String userName = JDBCUtils.SYS_NAME;
		String pass = StringUtils.isEmpty(itmsSid.getSysPass())?JDBCUtils.SYS_PASS:itmsSid.getSysPass();
		String sql = null;
		//登陆
		ResultSet rs = null;//结果集
		Statement stmt = null;//结果集通道
		Connection conn = null;//数据库连接	
		
		List<ItmsIonizationTables> tables = commonDao.findByQuery("FROM ItmsIonizationTables t WHERE t.users.id =:id",
				"id", parentId);
		if(tables!=null && tables.size()>0){
			return true;
		}
		
		int c = 0;
		String tableName = "",owner = "",tablespaceName = "";
		try {
			Class.forName(JDBCUtils.CLASS_NAME); 
			String url = JDBCUtils.getUrl(ip, sid);
			conn = DriverManager.getConnection(url,userName,pass);
			stmt = conn.createStatement(); 
			
			owner = user.getCode();tablespaceName = itmsTablespaces.getCode();
			//SELECT a.TABLE_NAME,a.TABLESPACE_NAME FROM ALL_TABLES a WHERE a.OWNER = upper('JAC_PCL_BMS') AND (a.TABLESPACE_NAME > upper('JAC_PCL_DATA') OR a.TABLESPACE_NAME < upper('JAC_PCL_DATA'))
			sql = "SELECT a.TABLE_NAME,a.TABLESPACE_NAME FROM ALL_TABLES a" +
					" WHERE a.OWNER = upper('"+owner+"')" +
					" AND (a.TABLESPACE_NAME > upper('"+tablespaceName+"') OR a.TABLESPACE_NAME < upper('"+tablespaceName+"'))";
//			System.out.println(sql);
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				tableName = rs.getString("TABLE_NAME");
				tablespaceName = rs.getString("TABLESPACE_NAME"); 
				ItmsIonizationTables table = new ItmsIonizationTables(user, tableName, tablespaceName);
				commonDao.store(table);
				c++;
			}
			
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
		 if(c>0){
			return true;
		 }else{
			return false;
		 }
	}
	
	@SuppressWarnings("unchecked")
	public void correctTable(ItmsUsers user){
		ItmsTablespaces itmsTablespaces = user.getLocation();//账号所在表空间
		ItmsSid itmsSid = itmsTablespaces.getWarehouseArea();//表空间所在实例SID
		ItmsWarehouse warehouse = itmsSid.getWarehouse();//服务器IP
		
		String ip = warehouse.getCode();
		String sid = itmsSid.getName();
		String userName = JDBCUtils.SYS_NAME;
		String pass = StringUtils.isEmpty(itmsSid.getSysPass())?JDBCUtils.SYS_PASS:itmsSid.getSysPass();
		String sql = null;
		//登陆
		ResultSet rs = null;//结果集
		Statement stmt = null;//结果集通道
		Connection conn = null;//数据库连接	
		
		String tableName = "",owner = "",tablespaceName = "";
		try {
			Class.forName(JDBCUtils.CLASS_NAME); 
			String url = JDBCUtils.getUrl(ip, sid);
			conn = DriverManager.getConnection(url,userName,pass);
			stmt = conn.createStatement(); 
			
			owner = user.getCode();tablespaceName = itmsTablespaces.getCode();
			//SELECT a.TABLE_NAME,a.TABLESPACE_NAME FROM ALL_TABLES a WHERE a.OWNER = upper('JAC_PCL_BMS') AND (a.TABLESPACE_NAME > upper('JAC_PCL_DATA') OR a.TABLESPACE_NAME < upper('JAC_PCL_DATA'))
			sql = "SELECT a.TABLE_NAME,a.TABLESPACE_NAME FROM ALL_TABLES a" +
					" WHERE a.OWNER = upper('"+owner+"')" +
					" AND (a.TABLESPACE_NAME > upper('"+tablespaceName+"') OR a.TABLESPACE_NAME < upper('"+tablespaceName+"'))";
//			System.out.println(sql);
			Map<String,String> ionizationTables = new HashMap<String,String>();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				tableName = rs.getString("TABLE_NAME");
				tablespaceName = rs.getString("TABLESPACE_NAME"); 
				ionizationTables.put(tableName, tablespaceName);
			}
			
			List<ItmsIonizationTables> tables = commonDao.findByQuery("FROM ItmsIonizationTables t WHERE t.users.id =:id",
					"id", user.getId());
			if(tables!=null && tables.size()>0){
				tablespaceName = itmsTablespaces.getCode();
				for(ItmsIonizationTables table : tables){
					tableName = table.getTableName();
					if(ionizationTables.containsKey(tableName)){//防止系统外人为操作和系统错误 部分数据已完成.
						//alter table jac_pcl_bms.SYSTEM_FUNCTION move tablespace JAC_PCL_DATA;
						sql = "alter table "+owner+"."+tableName+" move tablespace "+tablespaceName;
						//System.out.print(sql+"\n");
						stmt.execute(sql);
						conn.commit();
						
						ItmsIonizationIndex index = new ItmsIonizationIndex(user, tableName);
						commonDao.store(index);
						
						//new task
						itmsLogManager.saveItmsTask(new String[]{
								ItmsSqlTaskType.EDI_JOB_INDEX_REBUILD,BEAN+ALTER_INDEX_REBUILD,user.getId()+""
						});
					}
				}
				commonDao.deleteAll(tables);
			}
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
	
	public static final String ALTER_INDEX_REBUILD = "alterIndexRebuild";
	@SuppressWarnings("unchecked")
	public void alterIndexRebuild(Long id){
		List<ItmsIonizationIndex> indexs = commonDao.findByQuery("FROM ItmsIonizationIndex i WHERE i.users.id =:id", 
				new String[]{"id"}, new Object[]{id});
		if(indexs!=null && indexs.size()>0){
			ItmsUsers user = commonDao.load(ItmsUsers.class, id);
			
			ItmsTablespaces itmsTablespaces = commonDao.load(ItmsTablespaces.class,user.getLocation().getId());//账号所在表空间
			ItmsSid itmsSid = commonDao.load(ItmsSid.class,itmsTablespaces.getWarehouseArea().getId());//表空间所在实例SID
			ItmsWarehouse warehouse = commonDao.load(ItmsWarehouse.class,itmsSid.getWarehouse().getId());//服务器IP
			
			String tableName = "",owner = "";
			owner = user.getCode();
			
			String ip = warehouse.getCode();
			String sid = itmsSid.getName();
			String userName = owner;//JDBCUtils.SYS_NAME;
			String pass = user.getName();//StringUtils.isEmpty(itmsSid.getSysPass())?JDBCUtils.SYS_PASS:itmsSid.getSysPass();
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
				
				for(ItmsIonizationIndex index : indexs){
					tableName = index.getTableName();
					//select u.index_name from user_constraints u where u.index_owner = 'JAC_PCL_BMS' and u.table_name = 'BMS_BILL'
					sql = "SELECT u.index_name FROM user_constraints u WHERE u.index_owner = '"+owner+"' AND u.table_name = '"+tableName+"'";
//					System.out.print(sql+"\n");
					rs = stmt.executeQuery(sql);
					while(rs.next()){
						String indexName = rs.getString("index_name");
						//alter index jac_pcl_bms.SYS_C0016860 rebuild
						sql = "alter index "+owner+"."+indexName+" rebuild";
//						System.out.print(sql+"\n");
						stmt.execute(sql);
						conn.commit();
					}
				}
				commonDao.deleteAll(indexs);
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
	}
	public JdbcTemplate getJdbcDataSource() {
		return jdbcDataSource;
	}
	public void setJdbcDataSource(JdbcTemplate jdbcDataSource) {
		this.jdbcDataSource = jdbcDataSource;
	}
}
