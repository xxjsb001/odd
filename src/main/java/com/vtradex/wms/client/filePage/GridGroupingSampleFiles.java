package com.vtradex.wms.client.filePage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.gwtext.client.widgets.grid.event.GridCellListener;
import com.gwtext.client.data.Record;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Store;
import com.gwtext.client.core.SortDir;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.GroupingStore;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SortState;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GroupingView;
import com.gwtext.client.widgets.layout.FitLayout;
import com.vtradex.thorn.client.message.IMessagePage;
import com.vtradex.thorn.client.ui.TextAreaUI;
import com.vtradex.thorn.client.ui.panel.AbstractSupportPanel;
import com.vtradex.thorn.client.ui.table.FormTable;
import com.vtradex.wms.client.businessObject.BusinessNode;
import com.vtradex.wms.client.ui.data.Scan_DataAccessor;
import com.vtradex.wms.client.ui.page.allocate.page.utils.GxtUIFactory;

@SuppressWarnings("unchecked")
public class GridGroupingSampleFiles extends AbstractSupportPanel{
	public static final String INFO = "GridGroupingSampleFiles";
	public static final String GET_FILE_DATA = "getFileData";
	public static final String READ_SQL = "readSqL";
	
	public static final String SPILT = "###___###";
	public static final String INIT_FILE = "INIT_FILE";
	public static final String READ_FILE = "READ_FILE";
	public static final String MESSAGE = "MESSAGE";
	public static final String KEY = "KEY";
	
	protected transient GridPanel grid;
	protected transient Panel panel;
	protected transient MemoryProxy proxy;
	protected transient GroupingStore store;
	protected transient Panel windowPanel;
	protected transient Window window;
//	protected transient Label label;
	protected transient TextAreaUI showMessage;
	protected transient FormTable formTable;
	
	public static final String width = "600px";
	public static final String height = "350px";
	public GridGroupingSampleFiles(IMessagePage page) {
		super(page, "iv_main_panel");
		this.initialize();
	}
	public void initialize() {
		panel = new Panel();  
        panel.setBorder(false);  
//        panel.setPaddings(15); 
        panel.setSize(EditInitServiceFile.width, EditInitServiceFile.height);
		
		remoteService(INIT_FILE);
		initView();
		
		this.add(panel);
	}
	private void initView(){
        windowPanel = new Panel();  
	    windowPanel.setShadow(true);  
	    windowPanel.setHeight(400);
	    windowPanel.setWidth(700);
	    windowPanel.setAutoScroll(true);
//	    windowPanel.setBodyStyle("background-color:#e6e6e6;font:normal 12px/15px '宋体';text-align:left;");  
	    
//	    label = new Label();
//	    windowPanel.add(label);
	    
	    formTable =	new FormTable();
	    showMessage = GxtUIFactory.createTextAreaUI("sql", false, false, 100, 100, 2);
	    showMessage.addToTable(formTable, 1, 1);
	    showMessage.getInputWidget().setStyleName("sql_panel");
	    windowPanel.add(formTable.getForm());
	
	    window = new Window();  
	    window.setIconCls("paste-icon");  
	    window.setMaximizable(true);  
	    window.setResizable(true);  
	    window.setLayout(new FitLayout());  
	    window.setWidth(700);  
	    window.setModal(false);  
	    window.setBodyStyle("packaging_panel_normal");   
	    window.add(windowPanel); 
	    
        RecordDef recordDef = new RecordDef(  
                new FieldDef[]{  
                        new StringFieldDef("fileName"),
                        new StringFieldDef("filePath")
                }  
        );
        ArrayReader reader = new ArrayReader(recordDef);  
        store = new GroupingStore();  
        store.setSortInfo(new SortState("fileName", SortDir.ASC));  
        store.setGroupField("filePath");
        store.setReader(reader);  
        
        ColumnConfig[] columns = new ColumnConfig[]{  
                //column ID is fileName which is later used in setAutoExpandColumn  
                new ColumnConfig("文件名", "fileName", 160, true, null, "fileName"),
                new ColumnConfig("文件夹", "filePath", 60, true)
        };
        ColumnModel columnModel = new ColumnModel(columns);  
        col = columns.length;
        
        grid = new GridPanel();  
        grid.setStore(store);  
        grid.setColumnModel(columnModel);  
        grid.setFrame(true);  
        grid.setStripeRows(true);  
        grid.setAutoExpandColumn("fileName");  
  
        GroupingView gridView = new GroupingView();  
        gridView.setForceFit(true);  
        gridView.setGroupTextTpl("{text} ({[values.rs.length]} {[values.rs.length > 1 ?  \"\" : \"\"]})");  
        gridView.setStartCollapsed(true);//初次显示时分组是否处于收缩状态,默认为false
  
        grid.setView(gridView);  
        grid.setSize(width, height);
        grid.setCollapsible(true);  
        grid.setAnimCollapse(false);  
        grid.setIconCls("grid-icon");  
        grid.addGridCellListener(new GridCellListener(){

			public void onCellClick(GridPanel grid, int rowIndex, int colIndex,
					EventObject e) {
				
				Store s1 = grid.getStore();
				Record r = s1.getAt(rowIndex);
				clickFile = r.getAsString("fileName")+SPILT+r.getAsString("filePath");
				clickFileName = r.getAsString("fileName")+"/"+r.getAsString("filePath");
				remoteService(READ_FILE);
			}

			public void onCellContextMenu(GridPanel grid, int rowIndex,
					int cellIndex, EventObject e) {
				// TODO Auto-generated method stub
				
			}

			public void onCellDblClick(GridPanel grid, int rowIndex,
					int colIndex, EventObject e) {
				// TODO Auto-generated method stub
				
			}
        	
        });
        panel.add(grid);
	}
	protected transient String clickFile;
	protected transient String clickFileName;
	protected transient Map params;
	private void remoteService(String message){
		params = new HashMap();
		if(INIT_FILE.equals(message)){
			getData().getFileData(params);
		}else if(READ_FILE.equals(message)){
			params.put(KEY, clickFile);
			getData().readSqL(params);
		}
	}
	protected transient Map maps;
	protected transient String sqlParameter;
	private void remoteOnSuccess(String message){
		if(INIT_FILE.equals(message)){
			initDataLine(maps);
			initData(maps);
			maps.clear();
			initStoreData();
		}else if(READ_FILE.equals(message)){
//			windowPanel.setHtml("<p>"+sqlParameter);
//			label.setText(sqlParameter);
			
			showMessage.setValue(sqlParameter);
			window.setTitle(clickFileName);
			window.show();
//			com.google.gwt.user.client.Window.alert(sqlParameter);
		}
	}
	private void initStoreData(){
		proxy = new MemoryProxy(data);//data  getCompanyData()
		store.setDataProxy(proxy);  
        store.load();
	}
	private Object[][] data = null;
	private int col = 2;
	private void initDataLine(Map<String,List<String>> maps){
		int line = 0;
		Iterator<Entry<String, List<String>>> iter = maps.entrySet().iterator();
		while(iter.hasNext()){
			Entry<String, List<String>> entry = iter.next();
			line += entry.getValue().size();
		}
		data = new Object[line][col];
	}
	private void initData(Map<String,List<String>> maps){
		int j=0;
		Iterator<Entry<String, List<String>>> iter = maps.entrySet().iterator();
		while(iter.hasNext()){
			Entry<String, List<String>> entry = iter.next();
			List<String> values = entry.getValue();
			for(int i = 0;i<values.size();i++){
				data[j][0] = values.get(i);
				data[j][1] = entry.getKey();
				j++;
			}
		}
		
	}
	public void doDispath(String message) {
		super.doDispath(message);
		if(BusinessNode.ON_SUCCESS.equals(message)){
			maps = (Map<String,List<String>>)getData().result.get(KEY);
			remoteOnSuccess(getData().result.get(MESSAGE).toString());
		}else if(READ_FILE.equals(message)){
			sqlParameter = (String) getData().result.get(KEY);
			remoteOnSuccess(getData().result.get(MESSAGE).toString());
		}
	}
	public Scan_DataAccessor getData() {
		return (Scan_DataAccessor) super.getData();
	}

}
