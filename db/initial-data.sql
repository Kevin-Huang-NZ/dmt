INSERT INTO `sys_page`(`page_name`,`page_title`,`memo`)VALUES('user','�û�','');
INSERT INTO `sys_page`(`page_name`,`page_title`,`memo`)VALUES('file','�ϴ��ļ�','');
INSERT INTO `sys_page`(`page_name`,`page_title`,`memo`)VALUES('syspage','ģ��','');
INSERT INTO `sys_page`(`page_name`,`page_title`,`memo`)VALUES('sysfun','����','');
INSERT INTO `sys_page`(`page_name`,`page_title`,`memo`)VALUES('sysrole','��ɫ','');
INSERT INTO `sys_page`(`page_name`,`page_title`,`memo`)VALUES('sysrolefun','��ɫ�빦��','');


INSERT INTO `sys_role`(`role_no`,`role_name`)VALUES('Z','ϵͳ����Ա');
INSERT INTO `sys_role`(`role_no`,`role_name`)VALUES('M','����Ա');
INSERT INTO `sys_role`(`role_no`,`role_name`)VALUES('A','���Ա');
INSERT INTO `sys_role`(`role_no`,`role_name`)VALUES('O','������Ա');
INSERT INTO `sys_role`(`role_no`,`role_name`)VALUES('S','�����û�');


INSERT INTO `dmt`.`sys_fun`(`fun_no`,`page_name`,`action_type`,`action_no`,`action_name`)VALUES('user/l','user','l','l','�û��б�');
INSERT INTO `dmt`.`sys_fun`(`fun_no`,`page_name`,`action_type`,`action_no`,`action_name`)VALUES('user/c','user','c','c','�����û�');
INSERT INTO `dmt`.`sys_fun`(`fun_no`,`page_name`,`action_type`,`action_no`,`action_name`)VALUES('user/r','user','r','r','�û�����');
INSERT INTO `dmt`.`sys_fun`(`fun_no`,`page_name`,`action_type`,`action_no`,`action_name`)VALUES('user/u','user','u','u','�����û�');
INSERT INTO `dmt`.`sys_fun`(`fun_no`,`page_name`,`action_type`,`action_no`,`action_name`)VALUES('user/d','user','d','d','ɾ���û�');
INSERT INTO `dmt`.`sys_fun`(`fun_no`,`page_name`,`action_type`,`action_no`,`action_name`)VALUES('file/upload','file','e','upload','�ϴ��ļ�');
INSERT INTO `dmt`.`sys_fun`(`fun_no`,`page_name`,`action_type`,`action_no`,`action_name`)VALUES('syspage/l','syspage','l','l','ģ���б�');
INSERT INTO `dmt`.`sys_fun`(`fun_no`,`page_name`,`action_type`,`action_no`,`action_name`)VALUES('syspage/c','syspage','c','c','����ģ��');
INSERT INTO `dmt`.`sys_fun`(`fun_no`,`page_name`,`action_type`,`action_no`,`action_name`)VALUES('syspage/r','syspage','r','r','ģ������');
INSERT INTO `dmt`.`sys_fun`(`fun_no`,`page_name`,`action_type`,`action_no`,`action_name`)VALUES('syspage/u','syspage','u','u','����ģ��');
INSERT INTO `dmt`.`sys_fun`(`fun_no`,`page_name`,`action_type`,`action_no`,`action_name`)VALUES('syspage/d','syspage','d','d','ɾ��ģ��');
INSERT INTO `dmt`.`sys_fun`(`fun_no`,`page_name`,`action_type`,`action_no`,`action_name`)VALUES('sysfun/l','sysfun','l','l','�����б�');
INSERT INTO `dmt`.`sys_fun`(`fun_no`,`page_name`,`action_type`,`action_no`,`action_name`)VALUES('sysfun/c','sysfun','c','c','��������');
INSERT INTO `dmt`.`sys_fun`(`fun_no`,`page_name`,`action_type`,`action_no`,`action_name`)VALUES('sysfun/r','sysfun','r','r','��������');
INSERT INTO `dmt`.`sys_fun`(`fun_no`,`page_name`,`action_type`,`action_no`,`action_name`)VALUES('sysfun/u','sysfun','u','u','���¹���');
INSERT INTO `dmt`.`sys_fun`(`fun_no`,`page_name`,`action_type`,`action_no`,`action_name`)VALUES('sysfun/d','sysfun','d','d','ɾ������');
INSERT INTO `dmt`.`sys_fun`(`fun_no`,`page_name`,`action_type`,`action_no`,`action_name`)VALUES('sysrole/l','sysrole','l','l','��ɫ�б�');
INSERT INTO `dmt`.`sys_fun`(`fun_no`,`page_name`,`action_type`,`action_no`,`action_name`)VALUES('sysrole/c','sysrole','c','c','������ɫ');
INSERT INTO `dmt`.`sys_fun`(`fun_no`,`page_name`,`action_type`,`action_no`,`action_name`)VALUES('sysrole/r','sysrole','r','r','��ɫ����');
INSERT INTO `dmt`.`sys_fun`(`fun_no`,`page_name`,`action_type`,`action_no`,`action_name`)VALUES('sysrole/u','sysrole','u','u','���½�ɫ');
INSERT INTO `dmt`.`sys_fun`(`fun_no`,`page_name`,`action_type`,`action_no`,`action_name`)VALUES('sysrole/d','sysrole','d','d','ɾ����ɫ');
INSERT INTO `dmt`.`sys_fun`(`fun_no`,`page_name`,`action_type`,`action_no`,`action_name`)VALUES('sysrole/gbuk','sysrole','e','gbuk','��ɫ����-gbuk');
INSERT INTO `dmt`.`sys_fun`(`fun_no`,`page_name`,`action_type`,`action_no`,`action_name`,`memo`)VALUES('sysrolefun/l','sysrole','l','l','��ɫȨ�޲�ѯ','��ѯĳ����ɫ���Է��ʵĹ���');
INSERT INTO `dmt`.`sys_fun`(`fun_no`,`page_name`,`action_type`,`action_no`,`action_name`,`memo`)VALUES('sysrolefun/c','sysrole','c','d','����Ȩ��','����ĳ����ɫ����ĳ�����ܵ�Ȩ��');
INSERT INTO `dmt`.`sys_fun`(`fun_no`,`page_name`,`action_type`,`action_no`,`action_name`,`memo`)VALUES('sysrolefun/d','sysrole','c','d','ɾ��Ȩ��','ȡ��ĳ����ɫ����ĳ�����ܵ�Ȩ��');


INSERT INTO `dmt`.`sys_role_fun`(`role_no`,`fun_no`)VALUES('Z','user/l');
INSERT INTO `dmt`.`sys_role_fun`(`role_no`,`fun_no`)VALUES('Z','user/c');
INSERT INTO `dmt`.`sys_role_fun`(`role_no`,`fun_no`)VALUES('Z','user/r');
INSERT INTO `dmt`.`sys_role_fun`(`role_no`,`fun_no`)VALUES('Z','user/u');
INSERT INTO `dmt`.`sys_role_fun`(`role_no`,`fun_no`)VALUES('Z','user/d');
INSERT INTO `dmt`.`sys_role_fun`(`role_no`,`fun_no`)VALUES('Z','syspage/l');
INSERT INTO `dmt`.`sys_role_fun`(`role_no`,`fun_no`)VALUES('Z','syspage/c');
INSERT INTO `dmt`.`sys_role_fun`(`role_no`,`fun_no`)VALUES('Z','syspage/r');
INSERT INTO `dmt`.`sys_role_fun`(`role_no`,`fun_no`)VALUES('Z','syspage/u');
INSERT INTO `dmt`.`sys_role_fun`(`role_no`,`fun_no`)VALUES('Z','syspage/d');
INSERT INTO `dmt`.`sys_role_fun`(`role_no`,`fun_no`)VALUES('Z','sysfun/l');
INSERT INTO `dmt`.`sys_role_fun`(`role_no`,`fun_no`)VALUES('Z','sysfun/c');
INSERT INTO `dmt`.`sys_role_fun`(`role_no`,`fun_no`)VALUES('Z','sysfun/r');
INSERT INTO `dmt`.`sys_role_fun`(`role_no`,`fun_no`)VALUES('Z','sysfun/u');
INSERT INTO `dmt`.`sys_role_fun`(`role_no`,`fun_no`)VALUES('Z','sysfun/d');
INSERT INTO `dmt`.`sys_role_fun`(`role_no`,`fun_no`)VALUES('Z','sysrole/l');
INSERT INTO `dmt`.`sys_role_fun`(`role_no`,`fun_no`)VALUES('Z','sysrole/c');
INSERT INTO `dmt`.`sys_role_fun`(`role_no`,`fun_no`)VALUES('Z','sysrole/r');
INSERT INTO `dmt`.`sys_role_fun`(`role_no`,`fun_no`)VALUES('Z','sysrole/u');
INSERT INTO `dmt`.`sys_role_fun`(`role_no`,`fun_no`)VALUES('Z','sysrole/d');
INSERT INTO `dmt`.`sys_role_fun`(`role_no`,`fun_no`)VALUES('Z','sysrole/gbuk');
INSERT INTO `dmt`.`sys_role_fun`(`role_no`,`fun_no`)VALUES('Z','sysrolefun/l');
INSERT INTO `dmt`.`sys_role_fun`(`role_no`,`fun_no`)VALUES('Z','sysrolefun/c');
INSERT INTO `dmt`.`sys_role_fun`(`role_no`,`fun_no`)VALUES('Z','sysrolefun/d');
INSERT INTO `dmt`.`sys_role_fun`(`role_no`,`fun_no`)VALUES('Z','file/upload');