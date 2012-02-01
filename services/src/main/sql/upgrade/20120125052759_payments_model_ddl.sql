DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
	`version` INT NULL,
	`is_active` tinyint(1) NOT NULL,
	`status` varchar(32) DEFAULT NULL,
	`product_sku` varchar(64) DEFAULT NULL,
	`production_payment_code` varchar(16) DEFAULT NULL,
	`sandbox_payment_code` varchar(16) DEFAULT NULL,
	`name` varchar(64) DEFAULT NULL,
	`description` TEXT DEFAULT NULL,
	`notes` TEXT DEFAULT NULL,
	`time_created` BIGINT(20) NULL DEFAULT NULL ,
	`time_updated` BIGINT(20) NULL DEFAULT NULL,
    PRIMARY KEY (`id`) 	
);

DROP TABLE IF EXISTS `product_line`;
CREATE TABLE `product_line` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
	`version` INT NULL,
	`quantity` INT NULL,
	`status` varchar(32) DEFAULT NULL,
	`is_active` tinyint(1) NOT NULL,
	`product_id` BIGINT(20) NOT NULL,
	`item_sku_id` BIGINT(20) NOT NULL,
	`time_created` BIGINT(20) NULL DEFAULT NULL ,
	`time_updated` BIGINT(20) NULL DEFAULT NULL,
    PRIMARY KEY (`id`) 	
);

DROP TABLE IF EXISTS `item_sku`;
CREATE TABLE `item_sku` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
	`version` INT NULL,
	`item_type` varchar(32) DEFAULT NULL,
	`status` varchar(32) DEFAULT NULL,
	`sku_code` varchar(64) DEFAULT NULL,
	`price` decimal(20,8) DEFAULT NULL,
	`currency_code` varchar(3) DEFAULT NULL,
	`name` varchar(255) DEFAULT NULL,
	`description` TEXT DEFAULT NULL,
	`notes` TEXT DEFAULT NULL,
	`is_active` tinyint(1) NOT NULL,
	`period` BIGINT(20) NULL DEFAULT NULL ,	
	`time_created` BIGINT(20) NULL DEFAULT NULL ,
	`time_updated` BIGINT(20) NULL DEFAULT NULL,
    PRIMARY KEY (`id`) 
);

DROP TABLE IF EXISTS `contact`;
CREATE TABLE `contact` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
	`version` INT NULL,
	`first_name` varchar(255) NOT NULL,
	`last_name` varchar(255) NOT NULL,
	`gender` varchar(32) DEFAULT NULL,
	`title` varchar(128) DEFAULT NULL,
	`mobile` varchar(32) DEFAULT NULL,
	`phone` varchar(32) DEFAULT NULL,
	`fax` varchar(32) DEFAULT NULL,
	`email` varchar(128) DEFAULT NULL,
	`twitter` varchar(48) DEFAULT NULL,
	`linkedin` varchar(255) DEFAULT NULL,
	`is_active` tinyint(1) NOT NULL,
	`notes` TEXT DEFAULT NULL,
	`publisher_id` BIGINT(20),
	`time_created` BIGINT(20) NULL DEFAULT NULL ,
	`time_updated` BIGINT(20) NULL DEFAULT NULL ,
	PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `site`;
CREATE TABLE `site` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
	`version` INT NULL,
	`name` varchar(255)  DEFAULT NULL,
	`url` varchar(255)  DEFAULT NULL,
	`description` TEXT DEFAULT NULL,	
	`notes` TEXT DEFAULT NULL,
	`is_active` tinyint(1) NOT NULL DEFAULT 0,
	`verified` tinyint(1) NOT NULL DEFAULT 0,
	`publisher_id` BIGINT(20) DEFAULT NULL,
	`time_created` BIGINT(20) NULL DEFAULT NULL ,
	`time_updated` BIGINT(20) NULL DEFAULT NULL ,
	PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `cust_order_line`;
CREATE TABLE `cust_order_line` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
	`version` INT NULL,
	`qty_orig` INT NULL,
	`qty_used` INT NULL,
	`start_time` BIGINT(20) NULL DEFAULT NULL,
	`end_time` BIGINT(20) NULL DEFAULT NULL,
	`status` varchar(32) DEFAULT NULL,
	`cust_order_id` BIGINT(20) DEFAULT NULL ,
	`item_sku_id` BIGINT(20) DEFAULT NULL ,
	`time_created` BIGINT(20) NULL DEFAULT NULL ,
	`time_updated` BIGINT(20) NULL DEFAULT NULL,
    PRIMARY KEY (`id`) 
);	