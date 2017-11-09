package com.vtradex.wms.server.model.itms;

import java.util.Date;

import com.vtradex.thorn.server.model.VersionalEntity;
import com.vtradex.thorn.server.model.message.TaskStatus;
/** 任务task*/
public class ItmsTask extends VersionalEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 任务类型 */
	private String type;

	/** 任务消息的订阅者 */
	private String subscriber;

	/** 订阅者实体序号 */
	private Long messageId;

	/** 任务接收创建时间 */
	private Date createdTime = new Date();
	
	/** 任务开始执行时间 */
	private Date startTime;

	/** 结束完成时间 */
	private Date endTime;

	/** 执行次数,可作为拓展项目 */
	private Integer repeatCount = 0;

	/**
	 * 状态
	 * {@link TaskStatus.java}
	 */
	private String status = TaskStatus.STAT_READY;
	/**扩展1 路径*/
	private String extend1;
	/**扩展2 文件名*/
	private String extend2;
	
	/**扩展3 编码格式*/
	private String extend3;
	/**扩展4*/
	private String extend4;
	/**扩展5*/
	private String extend5;
	/**扩展6*/
	private String extend6;
	
	public String getExtend1() {
		return extend1;
	}

	public void setExtend1(String extend1) {
		this.extend1 = extend1;
	}

	public String getExtend2() {
		return extend2;
	}

	public void setExtend2(String extend2) {
		this.extend2 = extend2;
	}

	public String getExtend3() {
		return extend3;
	}

	public void setExtend3(String extend3) {
		this.extend3 = extend3;
	}

	public String getExtend4() {
		return extend4;
	}

	public void setExtend4(String extend4) {
		this.extend4 = extend4;
	}

	public String getExtend5() {
		return extend5;
	}

	public void setExtend5(String extend5) {
		this.extend5 = extend5;
	}

	public String getExtend6() {
		return extend6;
	}

	public void setExtend6(String extend6) {
		this.extend6 = extend6;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(String subscriber) {
		this.subscriber = subscriber;
	}

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getRepeatCount() {
		return repeatCount;
	}

	public void setRepeatCount(Integer repeatCount) {
		this.repeatCount = repeatCount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	public void setFinshStatus() {
		status = TaskStatus.STAT_FINISH;
		endTime = new Date();
		this.repeatCount += 1;
	}
	public void setFailStatus() {
		status = TaskStatus.STAT_FAIL;
		endTime = new Date();
		this.repeatCount += 1;
	}
	/**
	 * 唯一构造函数, 所有参数必填
	 * 
	 * @param type 任务类型
	 * @param subscriber 任务订阅者
	 * @param messageId 实体ID
	 */
	public ItmsTask(String type, String subscriber, Long messageId) {
		this.type = type;
		this.subscriber = subscriber;
		this.messageId = messageId;
		
		status = TaskStatus.STAT_READY;
		createdTime = new Date();
	}
	public ItmsTask(){
		// 供Hibernate使用，开发者不允许调用
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
