ALTER TABLE `cust_order` ADD COLUMN `publisher_id` BIGINT(20) NULL  AFTER `patron_id` ;
ALTER TABLE `publisher` CHANGE COLUMN  `simple_geo_json_token` `json_token` VARCHAR(255) NULL DEFAULT NULL  ;
