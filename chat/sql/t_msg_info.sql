DROP TABLE IF EXISTS `chat`.`t_msg_info`;
CREATE TABLE `chat`.`t_msg_info` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `guid` BIGINT(16) NOT NULL COMMENT '消息id',
  `to_uid` BIGINT(16) NOT NULL COMMENT '目标用户',
  `from_uid` BIGINT(16) NOT NULL COMMENT '发送者',
  `msg_type` INT NOT NULL COMMENT '消息类型',
  `msg_content_type` INT NOT NULL COMMENT '消息内容类型',
  `msg_data` BLOB NOT NULL COMMENT '消息数据',
  `read_status` INT NOT NULL DEFAULT '0' COMMENT '0未读 1已读',
  `is_delete` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '0未删除 1已删除',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `guid_UNIQUE` (`guid`),
  INDEX `to_key` (`to_uid`),
  INDEX `from_key` (`from_uid`))
  ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'NIM-消息表';