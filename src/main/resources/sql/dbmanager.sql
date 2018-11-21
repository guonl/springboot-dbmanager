-- 数据库初始化脚本

-- 创建数据库
# CREATE DATABASE dbmanager;
# use dbmanager;

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


DROP TABLE IF EXISTS `logs_sql_record`;
CREATE TABLE `logs_sql_record` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `table_name` varchar(30) NOT NULL COMMENT '操作的表名',
  `execute_sql` varchar(255) DEFAULT NULL COMMENT '被执行的sql',
  `sql_type` varchar(50) DEFAULT NULL COMMENT 'sql类型：update insert select delete ...',
  `where_para` varchar(255) DEFAULT NULL COMMENT '查询参数',
  `update_para` varchar(255) DEFAULT NULL COMMENT '更新参数',
  `operator` varchar(50) DEFAULT NULL COMMENT '操作人',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间 ',
  `is_del` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='sql操作日志';


