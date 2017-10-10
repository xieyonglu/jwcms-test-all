CREATE DATABASE db_test;

CREATE TABLE `t_user` (
	`id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
	`a` varchar(20) NOT NULL DEFAULT '' COMMENT '字段a',
	`b` varchar(20) NOT NULL DEFAULT '' COMMENT '字段b',
	
	`is_delete` char(1) NOT NULL DEFAULT 'N' COMMENT '软删除标志位，N：未删除，Y：删除',
	`creator` bigint(20) NOT NULL DEFAULT '-1' COMMENT '创建人员',
	`created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`modifier` bigint(4) NOT NULL DEFAULT '-1' COMMENT '更新人员',
	`updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	`version` int(11) NOT NULL DEFAULT '1' COMMENT '版本号',

	PRIMARY KEY (`id`),
	KEY `idx_created_at` (`created_at`),
	KEY `idx_updated_at` (`updated_at`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='连通表';