package com.vtradex.wms.server.telnet.exception;

import com.vtradex.thorn.server.exception.BusinessException;

/**
 * 
 *
 * @category 
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1 $ $Date: 2015/10/22 08:03:28 $
 */
public class RFFinishException extends BusinessException {

	private static final long serialVersionUID = 6999044535327044229L;

	public RFFinishException(String message){
		super(message);
	}
}
