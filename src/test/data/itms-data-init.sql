DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'TypeOfBill';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'TypeOfBill', 'insert');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'TypeOfBill', 'delete');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'TypeOfBill', 'update');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'TypeOfBill', 'select');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'WmsCountLockType';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsCountLockType', 'OPEN');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsCountLockType', 'LOCKED');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsCountLockType', 'EXPIRED');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsCountLockType', 'EXPIRED(GRACE)');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsCountLockType', 'LOCKED(TIMED)');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsCountLockType', 'EXPIRED & LOCKED');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsCountLockType', 'EXPIRED(GRACE) & LOCKED(TIMED)');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsCountLockType', 'EXPIRED & LOCKED(TIMED)');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsCountLockType', 'EXPIRED(GRACE) & LOCKED');
----文件类型
DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'WmsInventoryLogType';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsInventoryLogType', 'CANCEL_RECEIVING');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsInventoryLogType', 'RECEIVING');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsInventoryLogType', 'SHIPPING');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsInventoryLogType', 'MOVE');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsInventoryLogType', 'CONVERT_PACKAGEUNIT');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsInventoryLogType', 'INVENTORY_ADJUST');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsInventoryLogType', 'COUNT_ADJUST');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsInventoryLogType', 'PROCESS_DOWN');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsInventoryLogType', 'PROCESS_UP');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsInventoryLogType', 'LOCK');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsInventoryLogType', 'UNLOCK');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsInventoryLogType', 'QUALITY');

   INSERT INTO global_params
   (ID, DISCRIMINATOR, PARAM_ID, TYPE, GROUP_NAME, REF_MODEL, PARAM_VALUE, PARAM_BEAN, DESCRIPTION, MODULE, CREATOR_ID, 
CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (SEQ_ENUMERATE.NEXTVAL, 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'IP_ADDRESS', 'P_STRING', '文件下载路径', 'origen', '192.168.0.116:8086/jac_itms/', '', '
规则上线RF同步URL', 'wms', null, '', null,null , '', null);