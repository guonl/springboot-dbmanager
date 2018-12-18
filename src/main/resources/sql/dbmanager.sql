-- 数据库初始化脚本

-- 创建数据库
# CREATE DATABASE dbmanager;
# use dbmanager;
-- 创建用户表
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(60) DEFAULT NULL,
  `mobile` varchar(60) DEFAULT NULL,
  `openid` varchar(60) DEFAULT NULL,
  `email` varchar(60) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `gender` int(11) DEFAULT NULL COMMENT '性别, 0 female 1 male',
  `birthday` date DEFAULT NULL,
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '用户状态 1 - 已激活，0 - 未激活，2 - 注销',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 黑名单表
DROP TABLE IF EXISTS `black_table_rule`;
CREATE TABLE `black_table_rule` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) DEFAULT '0' COMMENT '夫级ID，0是表级别',
  `b_table_name` varchar(50) NOT NULL COMMENT '黑名单表名',
  `is_black` int(1) DEFAULT '0' COMMENT '是否是黑名单 0不是 1是',
  `black_field` varchar(50) DEFAULT NULL COMMENT '黑字段',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间 ',
  `is_del` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='黑名单表规则';

-- sql记录表
DROP TABLE IF EXISTS `sql_log_record`;
CREATE TABLE `sql_log_record` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `table_name` varchar(50) NOT NULL COMMENT '操作的表名',
  `execute_sql` varchar(500) DEFAULT NULL COMMENT '被执行的sql',
  `sql_para` varchar(500) DEFAULT NULL COMMENT '执行参数',
  `sql_type` varchar(20) DEFAULT NULL COMMENT 'sql类型：update insert select delete ...',
  `operator_ip` varchar(30) DEFAULT NULL COMMENT '操作者IP',
  `operator_info` varchar(200) DEFAULT NULL COMMENT '操作人详情',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间 ',
  `is_del` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='sql操作日志表';












