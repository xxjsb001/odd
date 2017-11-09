package com.vtradex.wms.server.model.count;

public interface WmsCountLockType {
	/** 可用 */
	public static String OPEN = "OPEN";
	/** 锁定 */
	public static String LOCKED = "LOCKED";
	/** 口令到期 */
	public static String EXPIRED = "EXPIRED";
	/** 提醒修改口令 */
	public static String EXPIRED_GRACE = "EXPIRED(GRACE)";
	/** 登录失败超时 */
	public static String LOCKED_TIMED = "LOCKED(TIMED)";
	/** 到期锁定 */
	public static String EXPIRED_LOCKED = "EXPIRED & LOCKED";
	/** 当account_stutus为EXPIRED(GRACE)的时候，用户又失败的login次数超过了FAILED_LOGIN_ATTEMPTS，被系统自动锁定 */
	public static String EXPIRED_GRACE_LOCKED_TIMED = "EXPIRED(GRACE) & LOCKED(TIMED)";
	/** 当设置了account expire后，用户又失败的login次数超过了FAILED_LOGIN_ATTEMPTS，被系统自动锁定 */
	public static String EXPIRED_LOCKED_TIMED = "EXPIRED & LOCKED(TIMED)";
	/** 用户account_status为EXPIRED(GRACE)后，又被DBA 手工锁定帐户后的状态 */
	public static String EXPIRED_GRACE_LOCKED = "EXPIRED(GRACE) & LOCKED";
	
	
	
}
