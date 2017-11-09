package com.vtradex.wms.server.service.interfaceLog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.jdbc.core.JdbcTemplate;

import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.utils.JavaTools;

public class JdbcExtendDataExtImp extends DefaultBaseManager implements JdbcExtendDataExt{

	public static final String BEAN = "jdbcExtendDataExt";
	private JdbcTemplate jdbcExtendDataNo0;//odd
	private JdbcTemplate jdbcExtendDataNo1;
	private JdbcTemplate jdbcExtendDataNo2;
	private JdbcTemplate jdbcExtendDataNo3;
	private JdbcTemplate jdbcExtendDataNo4;
	private JdbcTemplate jdbcExtendDataNo5;
	private JdbcTemplate jdbcExtendDataNo6;
	public JdbcExtendDataExtImp() {
		// TODO Auto-generated constructor stub
	}
	
	/** JdbcTemplate.queryForList(String sql)
	 If you add the appropriate data source and want to perform queryForList method, then increase the number here
	 **/
	public static final String QFL = "QueryForList";
	@SuppressWarnings("rawtypes")
	public List dataNo0QueryForList(String sql){
		return jdbcExtendDataNo0.queryForList(sql);
	}
	@SuppressWarnings("rawtypes")
	public List dataNo1QueryForList(String sql){
		return jdbcExtendDataNo1.queryForList(sql);
	}
	@SuppressWarnings("rawtypes")
	public List dataNo2QueryForList(String sql){
		return jdbcExtendDataNo2.queryForList(sql);
	}
	@SuppressWarnings("rawtypes")
	public List dataNo3QueryForList(String sql){
		return jdbcExtendDataNo3.queryForList(sql);
	}
	@SuppressWarnings("rawtypes")
	public List dataNo4QueryForList(String sql){
		return jdbcExtendDataNo4.queryForList(sql);
	}
	@SuppressWarnings("rawtypes")
	public List dataNo5QueryForList(String sql){
		return jdbcExtendDataNo5.queryForList(sql);
	}
	@SuppressWarnings("rawtypes")
	public List dataNo6QueryForList(String sql){
		return jdbcExtendDataNo6.queryForList(sql);
	}
	
	/***JdbcTemplate.execute(String sql).
	 If you add the appropriate data source and want to perform execute method, then increase the number here
	 **/
	public static final String EXT = "Execute";
	public void dataNo0Execute(String sql){
		jdbcExtendDataNo0.execute(sql);
	}
	public void dataNo1Execute(String sql){
		jdbcExtendDataNo1.execute(sql);
	}
	public void dataNo2Execute(String sql){
		jdbcExtendDataNo2.execute(sql);
	}
	public void dataNo3Execute(String sql){
		jdbcExtendDataNo3.execute(sql);
	}
	public void dataNo4Execute(String sql){
		jdbcExtendDataNo4.execute(sql);
	}
	public void dataNo5Execute(String sql){
		jdbcExtendDataNo5.execute(sql);
	}
	public void dataNo6Execute(String sql){
		jdbcExtendDataNo6.execute(sql);
	}
	
	
	//******* get and set************************
	public JdbcTemplate getJdbcExtendDataNo0() {
		return jdbcExtendDataNo0;
	}
	public void setJdbcExtendDataNo0(JdbcTemplate jdbcExtendDataNo0) {
		this.jdbcExtendDataNo0 = jdbcExtendDataNo0;
	}
	public JdbcTemplate getJdbcExtendDataNo1() {
		return jdbcExtendDataNo1;
	}
	public void setJdbcExtendDataNo1(JdbcTemplate jdbcExtendDataNo1) {
		this.jdbcExtendDataNo1 = jdbcExtendDataNo1;
	}
	public JdbcTemplate getJdbcExtendDataNo2() {
		return jdbcExtendDataNo2;
	}
	public void setJdbcExtendDataNo2(JdbcTemplate jdbcExtendDataNo2) {
		this.jdbcExtendDataNo2 = jdbcExtendDataNo2;
	}
	public JdbcTemplate getJdbcExtendDataNo3() {
		return jdbcExtendDataNo3;
	}
	public void setJdbcExtendDataNo3(JdbcTemplate jdbcExtendDataNo3) {
		this.jdbcExtendDataNo3 = jdbcExtendDataNo3;
	}
	public JdbcTemplate getJdbcExtendDataNo4() {
		return jdbcExtendDataNo4;
	}
	public void setJdbcExtendDataNo4(JdbcTemplate jdbcExtendDataNo4) {
		this.jdbcExtendDataNo4 = jdbcExtendDataNo4;
	}
	public JdbcTemplate getJdbcExtendDataNo5() {
		return jdbcExtendDataNo5;
	}
	public void setJdbcExtendDataNo5(JdbcTemplate jdbcExtendDataNo5) {
		this.jdbcExtendDataNo5 = jdbcExtendDataNo5;
	}
	public JdbcTemplate getJdbcExtendDataNo6() {
		return jdbcExtendDataNo6;
	}
	public void setJdbcExtendDataNo6(JdbcTemplate jdbcExtendDataNo6) {
		this.jdbcExtendDataNo6 = jdbcExtendDataNo6;
	}
	//=========================
	public static void main(String[] args) {
//		executeEightAtNightPerDay("09:15:00");
		System.out.println(JavaTools.compare_date("08", "09", "HH"));
		System.out.println(JavaTools.format(new Date(), JavaTools.hh));
	}
	/**"20:00:00"*/
	public static void executeEightAtNightPerDay(String time) {  
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);  
		long oneDay = 24 * 60 * 60 * 1000;  
		long initDelay  = getTimeMillis(time) - System.currentTimeMillis();  
		initDelay = initDelay > 0 ? initDelay : oneDay + initDelay;  
	    executor.scheduleAtFixedRate(  
	            new EchoServer(),  
	            initDelay,  
	            oneDay,  
	            TimeUnit.MILLISECONDS);  
	}  
	/** 
	 * 获取指定时间对应的毫秒数 
	 * @param time "HH:mm:ss" 
	 * @return 
	 */  
	private static long getTimeMillis(String time) {  
	    try {  
	        DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");  
	        DateFormat dayFormat = new SimpleDateFormat("yy-MM-dd");  
	        Date curDate = dateFormat.parse(dayFormat.format(new Date()) + " " + time);  
	        return curDate.getTime();  
	    } catch (ParseException e) {  
	        e.printStackTrace();  
	    }  
	    return 0;  
	}  
}
class EchoServer implements Runnable {  
	@Override  
	public void run() {  
		try {  
            Thread.sleep(50);  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        }  
        System.out.println("This is a echo server. The current time is " +  
                System.currentTimeMillis() + ".");  
    }  
}
