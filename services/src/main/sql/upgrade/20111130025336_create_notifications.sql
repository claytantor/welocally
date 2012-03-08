/*CREATE  TABLE `payment_notification` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `version` INT NULL,
  `external_key` VARCHAR(255) NULL DEFAULT NULL ,
  `publisher_key` VARCHAR(255) NULL DEFAULT NULL ,
  `txn_type` VARCHAR(255) NULL DEFAULT NULL ,
  `notification_body` TEXT NULL DEFAULT NULL ,
  `time_created` BIGINT(20) NULL DEFAULT NULL ,
  `time_updated` BIGINT(20) NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) 
);*/
