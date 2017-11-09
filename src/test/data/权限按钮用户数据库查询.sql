--查询[masterBOLProcess.viewPrintTag]流程权限对应那些角色
select * from PERMISSIONS p where p.element_id = 'masterBOLProcess.viewPrintTag'
--查询角色名称
select * from ROLES r where r.id in (1,1025,1028)
--查询角色与那些用户绑定
select * from ROLE_USER ru where ru.role_id in (1,1025,1028)
--查询哪些用户具有流程[masterBOLProcess.viewPrintTag]的使用权限
select * from users u where u.id in (select t.user_id from ROLE_USER t where t.role_id in (1,1025,1028))