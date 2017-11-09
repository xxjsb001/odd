package com.vtradex.wms.client.filePage;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vtradex.thorn.client.template.BaseCustomPopupTemplate;
import com.vtradex.wms.client.ui.data.Scan_DataAccessor;

public class EditInitServiceFile extends BaseCustomPopupTemplate implements IsSerializable{
	public static final String INFO = "EditInitServiceFile";
	public static final String SAVE_MANAGER = "itmsFilesManager";
	
	public static final String width = "600px";
	public static final String height = "350px";
	
	protected transient GridGroupingSampleFiles filePanel;
	public Scan_DataAccessor getData() {
		return (Scan_DataAccessor) super.getData();
	}
	public void draw(VerticalPanel content) {
		super.draw(content);
		content.setSize(width, height);
		data = new Scan_DataAccessor(this);
		
		filePanel = new GridGroupingSampleFiles(this);
		content.add(filePanel);
		this.setFilePanel(filePanel);
	}
	public GridGroupingSampleFiles getFilePanel() {
		return filePanel;
	}
	public void setFilePanel(GridGroupingSampleFiles filePanel) {
		this.filePanel = filePanel;
	}

}
