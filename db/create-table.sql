CREATE TABLE `sys_page` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `page_name` VARCHAR(50) NOT NULL DEFAULT '',
    `page_title` VARCHAR(50) NOT NULL DEFAULT '',
    `memo` VARCHAR(500) NULL,
    PRIMARY KEY (`id`)
)  ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `sys_fun` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `fun_no` VARCHAR(80) NOT NULL DEFAULT '' COMMENT '非输入值，等于page_name/action_no',
    `page_name` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '关联sys_page表page_name',
    `action_type` CHAR(1) NOT NULL DEFAULT 'l' COMMENT 'l-列表；c-创建；r-详情；u-更新；d-删除；e-其他',
    `action_no` VARCHAR(30) NOT NULL DEFAULT '',
    `action_name` VARCHAR(50) NOT NULL DEFAULT '',
    `memo` VARCHAR(500) NULL,
    PRIMARY KEY (`id`)
)  ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `sys_role` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `role_no` CHAR(1) NOT NULL DEFAULT '',
    `role_name` VARCHAR(20) NOT NULL DEFAULT '',
    `memo` VARCHAR(500) NULL,
    PRIMARY KEY (`id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE `sys_role_fun` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `role_no` CHAR(1) NOT NULL DEFAULT '',
    `fun_no` VARCHAR(80) NOT NULL DEFAULT '',
    PRIMARY KEY (`id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE `user` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `user_name` VARCHAR(50) NOT NULL DEFAULT '',
    `avatar` VARCHAR(200) NULL,
    `gender` CHAR(1) NULL COMMENT '0-未提供；1-男；2-女',
    `birthday` VARCHAR(10) NULL COMMENT '格式：yyyy-MM-dd',
    `phone` VARCHAR(20) NOT NULL DEFAULT '',
    `password` VARCHAR(100) NOT NULL DEFAULT '',
    `roles` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '多个角色使用分号连接',
    `status` CHAR(1) NOT NULL DEFAULT '1' COMMENT '0-禁用；1-启用',
    PRIMARY KEY (`id`),
    KEY `i_staff_0` (`phone`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE `file_upload` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `table_name` VARCHAR(50) NOT NULL DEFAULT '',
    `row_id` BIGINT(20) NOT NULL DEFAULT -1,
    `full_path` VARCHAR(500) NOT NULL DEFAULT '',
    `file_name` VARCHAR(200) NOT NULL DEFAULT '',
    `delete_status` CHAR(1) NOT NULL DEFAULT '0' COMMENT '0-未删除；1-逻辑删除；2-物理删除',
    PRIMARY KEY (`id`),
    KEY `i_file_upload_0` (`table_name` , `row_id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE `country` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `country_code` VARCHAR(20) NOT NULL DEFAULT '',
    `country_name` VARCHAR(100) NOT NULL DEFAULT '',
    PRIMARY KEY (`id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE `language` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `language_code` VARCHAR(20) NOT NULL DEFAULT '',
    `language_name` VARCHAR(100) NOT NULL DEFAULT '',
    `is_roman` CHAR(1) NOT NULL DEFAULT '0' COMMENT '0-不是；1-是。不是罗马字母的，需要罗马字母转写表',
    PRIMARY KEY (`id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE `country_language` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `country_code` VARCHAR(20) NOT NULL DEFAULT '',
    `language_code` VARCHAR(20) NOT NULL DEFAULT '',
    `status` CHAR(1) NOT NULL DEFAULT '1' COMMENT '0-停用；1-启用',
    PRIMARY KEY (`id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE `ct_roman` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `country_code` VARCHAR(20) NOT NULL DEFAULT '',
    `language_code` VARCHAR(20) NOT NULL DEFAULT '',
    `original_alpha` VARCHAR(10) NOT NULL DEFAULT '',
    `roman_alpha` VARCHAR(10) NOT NULL DEFAULT '',
    PRIMARY KEY (`id`),
    KEY `i_ct_roman_0` (`country_code` , `language_code`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE `ct_common` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `country_code` VARCHAR(20) NOT NULL DEFAULT '',
    `language_code` VARCHAR(20) NOT NULL DEFAULT '',
    `original` VARCHAR(200) NOT NULL DEFAULT '',
    `original_abbr` VARCHAR(100) NULL,
    `original_type` CHAR(1) NOT NULL DEFAULT 'x' COMMENT '1-人名；2-通名；3-形容词；x-其它',
    `roman` VARCHAR(200) NULL COMMENT '罗马转写',
    `match_way` CHAR(1) NOT NULL DEFAULT '1' COMMENT '匹配方式：1-精确；2-前缀（词头）；3-后缀（词尾）；4-前置（在xxx之前）；5-后置（在xxx之后）',
    `match_params` VARCHAR(200) NULL COMMENT 'match_way为4/5时，需要xxx参数，如果存在多个，使用分号连接',
    `transliteration` VARCHAR(200) NULL COMMENT '音译。音译和意译至少有一个',
    `free_translation` VARCHAR(200) NULL COMMENT '意译。音译和意译至少有一个',
    PRIMARY KEY (`id`),
    KEY `i_ct_common_0` (`country_code` , `language_code`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE `ct_transliteration` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `country_code` VARCHAR(20) NOT NULL DEFAULT '',
    `language_code` VARCHAR(20) NOT NULL DEFAULT '',
    `original` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '辅音、元音、辅音+元音',
    `roman` VARCHAR(20) NULL COMMENT '罗马转写',
    `match_way` CHAR(1) NOT NULL DEFAULT '1' COMMENT '匹配方式：1-精确；2-前缀（词头）；3-后缀（词尾）；4-前置（在xxx之前）；5-后置（在xxx之后）',
    `match_params` VARCHAR(20) NULL COMMENT 'match_way为4/5时，需要xxx参数，如果存在多个，使用分号连接',
    `chinese` VARCHAR(10) NOT NULL DEFAULT '' COMMENT '汉字，同音异形字使用括号括起来',
    PRIMARY KEY (`id`),
    KEY `i_ct_transliteration_0` (`country_code` , `language_code`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE `project` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `project_name` VARCHAR(100) NOT NULL DEFAULT '',
    `country_code` VARCHAR(20) NOT NULL DEFAULT '',
    `start_date` VARCHAR(10) NOT NULL DEFAULT '',
    `due_date` VARCHAR(10) NOT NULL DEFAULT '',
    `status` CHAR(1) NOT NULL DEFAULT '1' COMMENT '1-执行中；9-已结束',
    `memo` VARCHAR(500) NULL,
    PRIMARY KEY (`id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE `task` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `project_id` BIGINT(20) NOT NULL DEFAULT -1,
    `task_name` VARCHAR(100) NOT NULL DEFAULT '',
    `language_code` VARCHAR(20) NOT NULL DEFAULT '',
    `need_roman` CHAR(1) NOT NULL DEFAULT '0' COMMENT '0-不需要罗马字母转写；1-需要罗马字母转写',
    `create_date` VARCHAR(10) NOT NULL DEFAULT '',
    `status` CHAR(1) NOT NULL DEFAULT '1' COMMENT '1-执行中；2-已完成；9-已取消',
    `memo` VARCHAR(500) NULL,
    PRIMARY KEY (`id`),
    KEY `i_task_0` (`project_id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE `task_detail` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `project_id` BIGINT(20) NOT NULL DEFAULT -1,
    `task_id` BIGINT(20) NOT NULL DEFAULT -1,
    `original` VARCHAR(200) NOT NULL DEFAULT '' COMMENT '导入数据-外文地名',
    `country` VARCHAR(100) NULL COMMENT '导入数据-国别',
    `language` VARCHAR(100) NULL COMMENT '导入数据-语种',
    `gec` VARCHAR(100) NULL COMMENT '导入数据-地理实体类别',
    `memo` VARCHAR(500) NULL COMMENT '导入数据-备注',
    `roman_status` CHAR(1) NOT NULL DEFAULT '0' COMMENT '罗马转写状态：0-未执行；1-已执行；9-未涉及',
    `roman` VARCHAR(200) NULL COMMENT '罗马字母转写。可以是导入数据，也可以是系统转写，也可为空',
    `trans_status` CHAR(1) NOT NULL DEFAULT '0' COMMENT '自动翻译状态：0-未执行；1-已翻译',
    `transliteration` VARCHAR(200) NULL COMMENT '音译。音译和意译至少有一个',
    `free_translation` VARCHAR(200) NULL COMMENT '意译。音译和意译至少有一个',
    `check_status` CHAR(1) NOT NULL DEFAULT '0' COMMENT '人工审核状态：0-未执行；1-已审核',
    `trans_result` VARCHAR(200) NULL COMMENT '最终翻译结果：在人工审核阶段产出，可能是自动音译、自动意译或者人工填写',
    PRIMARY KEY (`id`),
    KEY `i_task_detail_0` (`project_id` , `task_id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE `check_round` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `project_id` BIGINT(20) NOT NULL DEFAULT -1,
    `task_id` BIGINT(20) NOT NULL DEFAULT -1,
    `round_name` VARCHAR(100) NOT NULL DEFAULT '',
    `start_date` VARCHAR(10) NOT NULL DEFAULT '',
    `due_date` VARCHAR(10) NOT NULL DEFAULT '',
    `status` CHAR(1) NOT NULL DEFAULT '1' COMMENT '0-未开始；1-执行中；9-已结束',
    `memo` VARCHAR(500) NULL,
    PRIMARY KEY (`id`),
    KEY `i_check_round_0` (`project_id` , `task_id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE `check_assignment` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `round_id` BIGINT(20) NOT NULL DEFAULT -1,
    `project_id` BIGINT(20) NOT NULL DEFAULT -1,
    `task_id` BIGINT(20) NOT NULL DEFAULT -1,
    `user_id` BIGINT(20) NOT NULL DEFAULT -1,
    `amount` INT NOT NULL DEFAULT 0 COMMENT '分配的审核任务数量',
    `done` INT NOT NULL DEFAULT 0 COMMENT '已完成的审核任务数量，初始0',
    `assign_status` CHAR(1) NOT NULL DEFAULT '1' COMMENT '0-未分配；1-已分配',
    PRIMARY KEY (`id`),
    KEY `i_check_assignment_0` (`user_id` , `project_id` , `task_id`, `round_id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;


CREATE TABLE `check_assignment_detail` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `round_id` BIGINT(20) NOT NULL DEFAULT -1,
    `project_id` BIGINT(20) NOT NULL DEFAULT -1,
    `task_id` BIGINT(20) NOT NULL DEFAULT -1,
    `user_id` BIGINT(20) NOT NULL DEFAULT -1,
    `task_detail_id` BIGINT(20) NOT NULL DEFAULT -1,
    `check_status` CHAR(1) NOT NULL DEFAULT '0' COMMENT '审核状态：0-未执行；1-已审核',
    `check_result` VARCHAR(200) NULL COMMENT '人工翻译结果',
    `check_memo` VARCHAR(500) NULL,
    `adoption_status` CHAR(1) NOT NULL DEFAULT '0' COMMENT '审核结果采纳状态：0-未处理；1-已采纳；2-已拒绝',
    PRIMARY KEY (`id`),
    KEY `i_check_assignment_detail_0` (`project_id` , `task_id`, `round_id`, `user_id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;