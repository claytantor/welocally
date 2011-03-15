ALTER TABLE `award_offer` MODIFY COLUMN `external_id` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL;

ALTER TABLE `award_offer` ADD COLUMN `discount_type` VARCHAR(45) AFTER `expire_millis`,
 ADD COLUMN `type` VARCHAR(45) AFTER `discount_type`,
 ADD COLUMN `quantity` INT AFTER `type`,
 ADD COLUMN `price` FLOAT AFTER `quantity`,
 ADD COLUMN `value` FLOAT AFTER `price`,
 ADD COLUMN `extra_details` LONGTEXT AFTER `value`,
 ADD COLUMN `illustration_url` LONGTEXT AFTER `extra_details`,
 ADD COLUMN `end_millis` BIGINT(20) AFTER `illustration_url`;
 
ALTER TABLE `business` ADD COLUMN `advertiser_id` VARCHAR(255) AFTER `guid`,
 ADD COLUMN `advertiser_source` VARCHAR(45) AFTER `advertiser_id`; 
 
CREATE TABLE `cust_order` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `version` INT,
  `time_created` BIGINT(20),
  `time_updated` BIGINT(20),
  `external_id` VARCHAR(255),
  `external_txid` VARCHAR(255),
  `channel` VARCHAR(45),
  `buyer_name` VARCHAR(128),
  `buyer_email` VARCHAR(255),
  `shipping_name` VARCHAR(128),
  `address_one` VARCHAR(128),
  `address_two` VARCHAR(128),
  `city` VARCHAR(128),
  `state` VARCHAR(45),
  `postal_code` VARCHAR(45),
  `country_code` VARCHAR(8),
  `external_orderitem` VARCHAR(255),
  `sku` VARCHAR(255),
  `title` VARCHAR(255),
  `description` LONGTEXT,
  `price` FLOAT,
  `quantity` INT,
  `status` VARCHAR(45),
  `rater_id` BIGINT(20),
  `awardoffer_id` BIGINT(20),
  `voucher_id` BIGINT(20),
  PRIMARY KEY (`id`)
)
CHARACTER SET utf8;

CREATE TABLE `voucher` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `version` INT,
  `time_created` BIGINT(20),
  `time_updated` BIGINT(20),
  `external_offerid` VARCHAR(255),
  `external_source` VARCHAR(45),
  `reservation_id` VARCHAR(255),
  `barcode` VARCHAR(255),
  `print_url` VARCHAR(255),
  `status` VARCHAR(45),
  `time_expires` BIGINT(20),
  `time_aquired` BIGINT(20),
  `time_redeemed` BIGINT(20),
  `time_cancelled` BIGINT(20),
  PRIMARY KEY (`id`)
)
CHARACTER SET utf8;

CREATE TABLE `award_awardoffer` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `award_id` BIGINT(20),
  `award_offer_id` BIGINT(20),
  PRIMARY KEY (`id`)
)
CHARACTER SET utf8;

CREATE TABLE `award_offer_item` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `version` INT,
  `time_created` BIGINT(20),
  `time_updated` BIGINT(20),
  `title` VARCHAR(255),
  `description` LONGTEXT,
  `quantity` INT,
  `award_offer_id` BIGINT(20),
  PRIMARY KEY (`id`)
)
CHARACTER SET utf8;

insert into award_awardoffer(award_id,award_offer_id) SELECT id as award_id,award_offer_id FROM award;
ALTER TABLE `award` DROP COLUMN `award_offer_id`;


