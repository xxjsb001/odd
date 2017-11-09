package com.vtradex.wms.server.telnet.shell;

import com.vtradex.kangaroo.shell.LoginShell;
import com.vtradex.thorn.server.model.security.User;
import com.vtradex.thorn.server.security.acegi.holder.SecurityContextHolder;
import com.vtradex.thorn.server.service.security.SecurityFilter;
import com.vtradex.thorn.server.service.security.UserManager;

/**
 * @author: 李炎
 */
public class NewLoginShell extends LoginShell {
	public NewLoginShell(UserManager userManager) {
		super(userManager);
	}
	public void afterPropertySet(User user){
		SecurityFilter sf = userManager.getSecurityFilter(user,user.getReferenceModel(),user.getLocale().getLanguage());
		sf.addHql("switchWareHousePage.loadWarehouseDefault", "FROM ItmsWarehouse wh WHERE 1=1 AND wh.status = 'ENABLED'");
		SecurityContextHolder.setRFSecurityFilter(sf);
	}
}
