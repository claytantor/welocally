ALTER TABLE `cust_order` 
	DROP FOREIGN KEY `fk_cust_order_patron1` , 
	DROP FOREIGN KEY `fk_cust_order_offer1` ;

ALTER TABLE `cust_order` CHANGE 
COLUMN `price` `total` DECIMAL(20,8) NULL DEFAULT NULL  ;

ALTER TABLE `cust_order` 
	DROP COLUMN `quantity` , 
	DROP COLUMN `description` ,  
	DROP COLUMN `patron_id` , 
	DROP COLUMN `offer_id`, 
	DROP INDEX `cust_order_patron_fk` ,
	DROP INDEX `fk_cust_order_offer1` ;

ALTER TABLE `cust_order` ADD COLUMN `discount` DECIMAL(20,8) NULL  AFTER `total` ;
ALTER TABLE `cust_order` ADD COLUMN `representitive_code` VARCHAR(64) NULL  AFTER `discount` ;
ALTER TABLE `cust_order` ADD COLUMN `product_id` BIGINT(20) NULL  AFTER `representitive_code` ;

update cust_order set title='Welocally Places For Wordpress 1M' where title is null;

ALTER TABLE `publisher` CHANGE COLUMN `site_name` `name` VARCHAR(255) NULL DEFAULT NULL  ;


