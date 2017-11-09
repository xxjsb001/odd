package com.vtradex.wms.server.service.interfaceLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.model.sencha.StPost;
import com.vtradex.wms.server.utils.JsonTools;
import com.vtradex.wms.server.utils.MyUtils;

public class AppExecuteDataImp extends DefaultBaseManager implements AppExecuteData{
	private JdbcTemplate jdbcDataSource;
	protected ItmsLogManager itmsLogManager;
	public AppExecuteDataImp(ItmsLogManager itmsLogManager) {
		this.itmsLogManager = itmsLogManager;
	}
	public String appJson(String filter){
		StringBuffer s = new StringBuffer();
		
		s.append("{");
		s.append("\"success\":true,");
		s.append("\"books\":[");
		if(MyUtils.fc.equals(filter)){
			s.append("{");
			s.append("\"id\":\"1\",");
			s.append("\"image_url\":\"resources/images/html51.jpg\",");
			s.append("\"book_name\":\"HTML 5 and CSS 3 gid\",");
			s.append("\"author\":\"xxx\",");
			s.append("\"description\":");
			s.append("\"new and full，HTML 5 and CSS3 very nice\"");
			s.append("}");
			
			s.append(",");
			s.append("{");
			s.append("\"id\":\"2\",");
			s.append("\"image_url\":\"resources/images/html52.jpg\",");
			s.append("\"book_name\":\"HTML5揭秘\",");
			s.append("\"author\":\"皮尔格林\",");
			s.append("\"description\":");
			s.append("\"本书全面而深入地对HTML5相关的技术进行详细介绍\"");
			s.append("}");
			
			s.append(",");
			s.append("{");
			s.append("\"id\":\"3\",");
			s.append("\"image_url\":\"resources/images/html53.jpg\",");
			s.append("\"book_name\":\"HTML5游戏开发\",");
			s.append("\"author\":\"(美)迈耶\",");
			s.append("\"description\":");
			s.append("\"很多从事Web前端开发的人对HTML总有些不满,比如\"");
			s.append("}");
			
			s.append(",");
			s.append("{");
			s.append("\"id\":\"4\",");
			s.append("\"image_url\":\"resources/images/html54.jpg\",");
			s.append("\"book_name\":\"HTML5高级程序设计\",");
			s.append("\"author\":\"(荷)柳伯斯,(美)阿伯斯,(美)萨姆\",");
			s.append("\"description\":");
			s.append("\"本书首先介绍了HTML5的历史背景、新的语义标签及\"");
			s.append("}");
			
			s.append(",");
			s.append("{");
			s.append("\"id\":\"5\",");
			s.append("\"image_url\":\"resources/images/html55.jpg\",");
			s.append("\"book_name\":\"HTML 5 & CSS完全手册(第5版)\",");
			s.append("\"author\":\"(美)鲍威尔\",");
			s.append("\"description\":");
			s.append("\"为了介绍HTML 5,这本由Thomas A. Powell所著\"");
			s.append("}");
		}else{
			s.append("{");
			s.append("\"id\":\"1\",");
			s.append("\"image_url\":\"resources/images/html51.jpg\",");
			s.append("\"book_name\":\"HTML 5 and CSS 3 gid\",");
			s.append("\"author\":\"xxx\",");
			s.append("\"description\":");
			s.append("\"new and full，HTML 5 and CSS3 very nice\"");
			s.append("}");
		}
		s.append("]");
		s.append("}");
		return s.toString();
	}
	@SuppressWarnings("rawtypes")
	public String List(int limit, int page,int start){
		String sql = "SELECT id,post_code,jobs_name,education_cn,experience_cn,district_cn,companyname,wage_cn"+
				",to_char(refreshtime,'yyyy-MM-dd') as refreshtime,contents,telephone,tag,click FROM "+
				"("+
				" SELECT A.*, ROWNUM RN  FROM (SELECT * FROM ST_POST) A WHERE ROWNUM <= ?"+//page*limit
				")"+
				"WHERE RN >= ? ";//start
		List l = jdbcDataSource.queryForList(sql, new Object[]{
				page*limit,start
		});
		Iterator iterator = l.iterator();
		List<StPost> posts = new ArrayList<StPost>();
		StPost post = null;
		while (iterator.hasNext()) {
			post = new StPost();
			Map m = (Map) iterator.next();
			post.setPost_id(Integer.valueOf(m.get("id").toString()));
			post.setPost_code(m.get("post_code").toString());
			post.setJobs_name((String)m.get("jobs_name"));
			post.setEducation_cn((String)m.get("education_cn"));
			post.setExperience_cn((String)m.get("experience_cn"));
			post.setDistrict_cn((String)m.get("district_cn"));
			post.setCompanyname((String)m.get("companyname"));
			post.setWage_cn((String)m.get("wage_cn"));
			post.setRefreshtime((String)m.get("refreshtime"));
			post.setContents((String)m.get("contents"));
			post.setTelephone((String)m.get("telephone"));
			post.setTag((String)m.get("tag"));
			post.setClick(m.get("click")==null?"0":m.get("click").toString());
			posts.add(post);
		}
		String strJson = JsonTools.getCreateJson("result", posts);
		return strJson;
	}
	public String click(int id){
		String sql = "update ST_POST s set s.click = s.click+1 where s.id = "+id;
		jdbcDataSource.execute(sql);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", id);
		map.put("message", "SUCCESS");
		String strJson = JsonTools.getCreateJson("result", map);
		System.out.println(strJson);//{"result":{"message":"SUCCESS","code":0}}
		return strJson;
	}
	public String vinWay(){
		String sql = "select app.status,app.order_date,app.position from app_vin_onway app where vin = 'test'";//start
		List l = jdbcDataSource.queryForList(sql);
		Iterator iterator = l.iterator();
		List<StPost> posts = new ArrayList<StPost>();
		StPost post = null;
		while (iterator.hasNext()) {
			post = new StPost();
			Map m = (Map) iterator.next();
			post.setCompanyname((String)m.get("companyname"));
			post.setJobs_name((String)m.get("jobs_name"));
			post.setPost_code(m.get("post_code").toString());
			posts.add(post);
		}
		String strJson = JsonTools.getCreateJson("result", posts);
		return strJson;
	}
	public JdbcTemplate getJdbcDataSource() {
		return jdbcDataSource;
	}
	public void setJdbcDataSource(JdbcTemplate jdbcDataSource) {
		this.jdbcDataSource = jdbcDataSource;
	}
}
