INSERT INTO  global_params  VALUES ('1', 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'tempFileDir', 'P_STRING', '临时文件路径配置', 'origen', 'd:/temp/tempFile', null, '上传文件存放的临时文件夹', 'wms', null, null, null, null, null, null);
INSERT INTO  global_params  VALUES ('2', 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'imageFileDir', 'P_STRING', '临时文件路径配置', 'origen', 'd:/temp/imageFile', null, '图片存放的文件夹', 'wms', null, null, null, null, null, null);
INSERT INTO  global_params  VALUES ('3', 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'otherFileDir', 'P_STRING', '临时文件路径配置', 'origen', 'd:/temp/otherFile', null, '需要永久存放的非图片文件的文件夹', 'wms', null, null, null, null, null, null);
INSERT INTO  global_params  VALUES ('6', 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'maintainTableDecimal', 'P_INT', '页面布局设置', 'origen', '6', null, '维护屏二维表格如果是小数需要保留的小数位数', 'wms', null, null, null, null, null, null);
INSERT INTO  global_params  VALUES ('8', 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'IS_ASH', 'P_BOOLEAN', '页面布局设置', 'origen', 'true', null, '右键弹出菜单不可用时变灰或不可见(true－变灰 false－不可见)', 'wms', null, null, null, null, null, null);
INSERT INTO  global_params  VALUES ('9', 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'ACK_CLOSEWINDOW', 'P_STRING', '页面布局设置', 'origen', 'ALT-X', null, '弹出窗口关闭热键', 'wms', null, null, null, null, null, null);
INSERT INTO  global_params  VALUES ('10', 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'reportURL', 'P_STRING', '报表服务器配置', 'origen', 'reportJsp/showReport.jsp', '', '报表服务器URL地址', 'wms', null, null, null, null, null, null);
INSERT INTO  global_params  VALUES ('11', 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'clientUserInfo', 'P_BEAN', '全局变量设置', 'origen', '', 'userInfoInClient', '前台用户登陆时显示的用户信息（如：当前用户：admin）', 'wms', null, null, null, null, null, null);
INSERT INTO  global_params  VALUES ('12', 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'round', 'P_BOOLEAN', '页面布局设置', 'origen', 'true', null, '小数位数处理时是否四舍五入(默认四舍五入)', 'wms', null, null, null, null, null, null);
INSERT INTO  global_params  VALUES ('13', 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'imageSource', 'P_STRING', '临时文件路径配置', 'origen', 'd:/temp/image/source', null, '影像归档图片源文件夹', 'wms', null, null, null, null, null, null);
INSERT INTO  global_params  VALUES ('14', 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'imageTarget', 'P_STRING', '临时文件路径配置', 'origen', 'd:/temp/image/target', null, '影像归档图片目标文件夹', 'wms', null, null, null, null, null, null);
INSERT INTO  global_params  VALUES ('15', 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'welcomepage', 'P_STRING', '全局变量设置', 'origen', '', '', '默认欢迎页面', 'wms', null, null, null, null, null, null);
INSERT INTO  global_params  VALUES ('16', 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'IMPORT_REPLACE', 'P_STRING', '全局变量设置', 'origen', 'false', null, '导入业务数据时是否替换已存在业务数据(true-替换,false-不替换)', 'wms', null, null, null, null, null, null);
INSERT INTO  global_params  VALUES ('17', 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'SESSION_USERS', 'P_BEAN', '全局变量设置', 'origen', '', 'userSession', '当前用户全局变量', 'wms', null, null, null, null, null, null);
INSERT INTO  global_params  VALUES ('18', 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'SESSION_WAREHOUSE', 'P_BEAN', '全局变量设置', 'origen', '', 'warehouseSession', '记录用户当前登录的仓库全局变量', 'wms', null, null, null, null, null, null);
INSERT INTO  global_params  VALUES ('19', 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'clientOtherInfo', 'P_BEAN', '全局变量设置', 'origen', '', 'warehouseInfoInClient', '记录用户当前登录的仓库信息', 'wms', null, null, null, null, null, null);
INSERT INTO  global_params  VALUES ('20', 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'AUTO_REFRESH', 'P_INT', '全局变量设置', 'origen', '30', '', '定义页面数据刷新最小时间间隔', 'wms', null, null, null, null, null, null);
INSERT INTO  global_params  VALUES ('21', 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'SAVE_SYSTEM_LOG', 'P_STRING', '全局变量设置', 'origen', 'true', '', '是否开启保存操作日志', 'wms', null, null, null, null, null, null);
INSERT INTO  global_params  VALUES ('22', 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'EXPORT_SUPPORT', 'P_STRING', '全局变量设置', 'origen', 'true', '', '标准表格页面是否支持导出,刷新', 'wms', null, '', null, '2', 'test', null);
INSERT INTO  global_params  VALUES ('23', 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'QUERY_THROUGHPUT_RECORD', 'P_INT', '全局变量设置', 'origen', '2', '', '表格查询吞吐量统计,耗时超过此值时记录(单位秒)', 'wms', null, '', null, null, '', null);
INSERT INTO  global_params  VALUES ('24', 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'BE_SYNC_MESSAGE', 'P_STRING', '全局变量设置', 'origen', 'true', '', '异步消息同步转换处理,false异步,true同步', 'wms', null, '', null, '2', 'test', null);
INSERT INTO  global_params  VALUES ('25', 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'w_bg_image_url', 'P_STRING', '全局变量设置', 'origen', 'images/inventoryview/wms_bg.jpg', '', '异步消息同步转换处理,false异步,true同步', 'wms', null, '', null, null, '', null);
