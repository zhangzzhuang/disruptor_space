
##item
```mysql
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for item
-- ----------------------------
DROP TABLE IF EXISTS `item`;
CREATE TABLE `item` (
`id` bigint(8) NOT NULL,
`amount` bigint(8) NOT NULL DEFAULT '0' COMMENT '库存',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;

```

##item_order
```mysql
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for item_order
-- ----------------------------
DROP TABLE IF EXISTS `item_order`;
CREATE TABLE `item_order` (
`id` bigint(10) NOT NULL AUTO_INCREMENT,
`item_id` bigint(8) NOT NULL DEFAULT '0',
`user_id` varchar(32) NOT NULL DEFAULT '',
`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;

```
