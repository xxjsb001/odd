package com.vtradex.wms.server.model.move;

/**
 * 移位单发运状态 
 *
 * @category 枚举
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1 $Date: 2015/10/22 08:03:13 $
 */
public interface WmsMoveDocShipStatus {
	
	/** 未发运 */
	public static final String UNSHIP = "UNSHIP";
	
	/** 已发运 */
	public static final String SHIPPED = "SHIPPED";
}
