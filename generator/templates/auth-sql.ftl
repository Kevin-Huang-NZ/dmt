

INSERT INTO `sys_page`(`page_name`,`page_title`,`memo`)VALUES('[=lowerTargetName]','xxx','');

INSERT INTO `dmt`.`sys_fun`(`fun_no`,`page_name`,`action_type`,`action_no`,`action_name`)VALUES('[=lowerTargetName]/l','[=lowerTargetName]','l','l','xxx列表');
INSERT INTO `dmt`.`sys_fun`(`fun_no`,`page_name`,`action_type`,`action_no`,`action_name`)VALUES('[=lowerTargetName]/c','[=lowerTargetName]','c','c','创建xxx');
INSERT INTO `dmt`.`sys_fun`(`fun_no`,`page_name`,`action_type`,`action_no`,`action_name`)VALUES('[=lowerTargetName]/r','[=lowerTargetName]','r','r','xxx详情');
INSERT INTO `dmt`.`sys_fun`(`fun_no`,`page_name`,`action_type`,`action_no`,`action_name`)VALUES('[=lowerTargetName]/u','[=lowerTargetName]','u','u','更新xxx');
INSERT INTO `dmt`.`sys_fun`(`fun_no`,`page_name`,`action_type`,`action_no`,`action_name`)VALUES('[=lowerTargetName]/d','[=lowerTargetName]','d','d','删除xxx');

INSERT INTO `dmt`.`sys_role_fun`(`role_no`,`fun_no`)VALUES('Z','[=lowerTargetName]/l');
INSERT INTO `dmt`.`sys_role_fun`(`role_no`,`fun_no`)VALUES('Z','[=lowerTargetName]/c');
INSERT INTO `dmt`.`sys_role_fun`(`role_no`,`fun_no`)VALUES('Z','[=lowerTargetName]/r');
INSERT INTO `dmt`.`sys_role_fun`(`role_no`,`fun_no`)VALUES('Z','[=lowerTargetName]/u');
INSERT INTO `dmt`.`sys_role_fun`(`role_no`,`fun_no`)VALUES('Z','[=lowerTargetName]/d');