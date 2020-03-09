CREATE TABLE `chat`.`t_user_info` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `uid` BIGINT(16) NOT NULL COMMENT '用户id',
  `u_name` VARCHAR(32) NOT NULL COMMENT '用户名字',
  `is_delete` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '0未删除 1已删除',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uid_UNIQUE` (`uid` ASC))
  ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'NIM-用户信息表' ;