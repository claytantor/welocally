SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';


-- -----------------------------------------------------
-- Table `award_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `award_type` ;

CREATE  TABLE IF NOT EXISTS `award_type` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `version` INT NOT NULL ,
  `name` VARCHAR(255) NOT NULL ,
  `class_type` VARCHAR(255) NULL DEFAULT NULL ,
  `description` TEXT NULL DEFAULT NULL ,
  `keyname` VARCHAR(45) NULL DEFAULT NULL ,
  `metadata` TEXT NULL DEFAULT NULL ,
  `points_value` INT(11) NULL DEFAULT NULL ,
  `previous` VARCHAR(45) NULL DEFAULT NULL ,
  `next` VARCHAR(45) NULL DEFAULT NULL ,
  `time_created` BIGINT NULL DEFAULT NULL ,
  `time_updated` BIGINT NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `user_principal`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user_principal` ;

CREATE  TABLE IF NOT EXISTS `user_principal` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `version` INT(11) NULL DEFAULT NULL ,
  `user_name` VARCHAR(255) NULL DEFAULT NULL ,
  `password` VARCHAR(255) NULL DEFAULT NULL ,
  `email` VARCHAR(255) NULL DEFAULT NULL ,
  `expired` TINYINT NULL DEFAULT NULL ,
  `credentials_expired` TINYINT NULL DEFAULT NULL ,
  `locked` TINYINT NULL DEFAULT NULL ,
  `enabled` TINYINT NULL DEFAULT NULL ,
  `guid` VARCHAR(45) NULL DEFAULT NULL ,
  `user_class` VARCHAR(255) NULL DEFAULT NULL ,
  `twitter_id` BIGINT(20) NULL DEFAULT NULL ,
  `twitter_username` VARCHAR(45) NULL DEFAULT NULL ,
  `twitter_token` VARCHAR(255) NULL DEFAULT NULL ,
  `twitter_secret` VARCHAR(255) NULL DEFAULT NULL ,
  `twitter_verify` VARCHAR(255) NULL DEFAULT NULL ,
  `twitter_profile_img` VARCHAR(255) NULL DEFAULT NULL ,
  `time_created` BIGINT NULL DEFAULT NULL ,
  `time_updated` BIGINT NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `business`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `business` ;

CREATE  TABLE IF NOT EXISTS `business` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `version` INT(11) NULL DEFAULT NULL ,
  `name` VARCHAR(255) NULL DEFAULT NULL ,
  `status` VARCHAR(15) NULL DEFAULT NULL ,
  `description` TEXT NULL DEFAULT NULL ,
  `url` VARCHAR(1024) NULL DEFAULT NULL ,
  `image_attachment_key` VARCHAR(255) NULL DEFAULT NULL ,
  `category_attachment_key` VARCHAR(255) NULL DEFAULT NULL ,
  `time_created` BIGINT NULL DEFAULT NULL ,
  `time_updated` BIGINT NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `offer`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `offer` ;

CREATE  TABLE IF NOT EXISTS `offer` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `version` INT(11) NOT NULL ,
  `merchant_id` BIGINT(20) NOT NULL ,
  `award_id` BIGINT(20) NOT NULL ,
  `name` VARCHAR(255) NULL DEFAULT NULL ,
  `description` TEXT NULL DEFAULT NULL ,
  `status` VARCHAR(45) NULL DEFAULT NULL ,
  `code` VARCHAR(45) NULL DEFAULT NULL ,
  `url` VARCHAR(1024) NULL DEFAULT NULL ,
  `discount_type` ENUM('DISCOUNT','COMP','PROMOCODE') NULL DEFAULT NULL ,
  `offer_type` ENUM('DEAL','VOUCHER','EVOUCHER','SMSVOUCHER','CALLVOUCHER','GIFTCARD','ADVERTISMENT') NULL DEFAULT NULL ,
  `quantity` INT(11) NULL DEFAULT NULL ,
  `price` FLOAT(11) NULL DEFAULT NULL ,
  `offer_value` FLOAT(11) NULL DEFAULT NULL ,
  `extra_details` TEXT NULL DEFAULT NULL ,
  `image_attachment_key` VARCHAR(255) NULL DEFAULT NULL ,
  `category_attachment_key` VARCHAR(255) NULL DEFAULT NULL ,
  `time_starts` BIGINT NULL DEFAULT NULL ,
  `time_ends` BIGINT NULL DEFAULT NULL ,
  `time_created` BIGINT NULL DEFAULT NULL ,
  `time_updated` BIGINT NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_offer_merchant1` (`merchant_id` ASC) ,
  INDEX `fk_offer_award1` (`award_id` ASC) ,
  CONSTRAINT `fk_offer_merchant1`
    FOREIGN KEY (`merchant_id` )
    REFERENCES `merchant` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_offer_award1`
    FOREIGN KEY (`award_id` )
    REFERENCES `award` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION  )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `award`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `award` ;

CREATE  TABLE IF NOT EXISTS `award` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `award_type_id` BIGINT(20) NOT NULL ,
  `award_offer_id` BIGINT(20) NOT NULL ,
  `patron_id` BIGINT(20) NOT NULL ,
  `version` INT(11) NOT NULL ,
  `expires` BIGINT NULL DEFAULT NULL ,
  `notes` TEXT NULL DEFAULT NULL ,
  `metadata` TEXT NULL DEFAULT NULL ,
  `status` VARCHAR(45) NULL DEFAULT NULL ,
  `time_created` BIGINT NULL DEFAULT NULL ,
  `time_updated` BIGINT NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `award_type_fk` (`award_type_id` ASC) ,
  INDEX `patron_fk` (`patron_id` ASC) ,
  INDEX `fk_award_patron` (`patron_id` ASC) ,
  INDEX `fk_award_award_offer` (`award_offer_id` ASC) ,
  CONSTRAINT `fk_award_award_type`
    FOREIGN KEY (`award_type_id` )
    REFERENCES `award_type` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_award_patron`
    FOREIGN KEY (`patron_id` )
    REFERENCES `patron` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_award_award_offer`
    FOREIGN KEY (`award_offer_id` )
    REFERENCES `offer` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `offer_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `offer_item` ;

CREATE  TABLE IF NOT EXISTS `offer_item` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `offer_id` BIGINT(20) NULL DEFAULT NULL ,
  `version` INT(11) NULL DEFAULT NULL ,
  `title` VARCHAR(255) NULL DEFAULT NULL ,
  `description` TEXT NULL DEFAULT NULL ,
  `quantity` INT(11) NULL DEFAULT NULL ,
  `time_created` BIGINT(20) NULL DEFAULT NULL ,
  `time_updated` BIGINT(20) NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_offer_item_offer`
    FOREIGN KEY (`offer_id` )
    REFERENCES `offer` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `business_attribute`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `business_attribute` ;

CREATE  TABLE IF NOT EXISTS `business_attribute` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `version` INT(11) NULL DEFAULT NULL ,
  `name` VARCHAR(255) NOT NULL ,
  `attribute_value` TEXT NULL DEFAULT NULL ,
  `business_id` BIGINT(20) NULL DEFAULT NULL ,
  `time_created` BIGINT(20) NULL DEFAULT NULL ,
  `time_updated` BIGINT(20) NULL DEFAULT NULL ,  
  PRIMARY KEY (`id`) ,
  INDEX `business_fk` (`business_id` ASC) ,
  CONSTRAINT `fk_business_attribute_business1`
    FOREIGN KEY (`business_id` )
    REFERENCES `business` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 888
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `business_metrics`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `business_metrics` ;

CREATE  TABLE IF NOT EXISTS `business_metrics` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `version` INT(11) NOT NULL ,
  `yays` INT(11) NOT NULL ,
  `boos` INT(11) NOT NULL ,
  `ratings` INT(11) NOT NULL ,
  `rating_avg` FLOAT(11) NULL DEFAULT NULL ,
  `start_time` BIGINT(20) NULL DEFAULT NULL ,
  `end_time` BIGINT(20) NULL DEFAULT NULL ,
  `business_location_id` BIGINT(20) NULL DEFAULT NULL ,
  `time_created` BIGINT(20) NULL DEFAULT NULL ,
  `time_updated` BIGINT(20) NULL DEFAULT NULL ,    
  PRIMARY KEY (`id`),
  INDEX `business_metrics_business_location_id_fk` (`business_location_id` ASC) ,
  CONSTRAINT `business_metrics_business_location_id_fk`
    FOREIGN KEY (`business_location_id` )
    REFERENCES `business_location` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION  )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `place`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `place` ;

CREATE  TABLE IF NOT EXISTS `place` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `version` INT NOT NULL ,
  `name` VARCHAR(255) NOT NULL ,
  `address1` VARCHAR(255) NULL DEFAULT NULL ,
  `city` VARCHAR(255) NULL DEFAULT NULL ,
  `state` VARCHAR(255) NULL DEFAULT NULL ,
  `zip` VARCHAR(45) NULL DEFAULT NULL ,
  `twitter_id` VARCHAR(255) NULL DEFAULT NULL ,
  `simple_geo_id` VARCHAR(255) NULL DEFAULT NULL ,
  `phone` VARCHAR(45) NULL DEFAULT NULL ,
  `latitude` DOUBLE NULL DEFAULT NULL ,
  `longitude` DOUBLE NULL DEFAULT NULL ,
  `description` TEXT NULL DEFAULT NULL ,
  `type` VARCHAR(255) NULL DEFAULT NULL ,
  `flag` VARCHAR(16) NULL DEFAULT 'ACTIVE' ,
  `url` VARCHAR(1024) NULL DEFAULT NULL ,
  `email` VARCHAR(255) NULL DEFAULT NULL ,
  `business_location_id` BIGINT(20) NULL DEFAULT NULL ,
  `image_attachment_key` VARCHAR(255) NULL DEFAULT NULL ,
  `category_attachment_key` VARCHAR(255) NULL DEFAULT NULL ,
  `time_created` BIGINT NULL DEFAULT NULL ,
  `time_updated` BIGINT NULL DEFAULT NULL ,
  PRIMARY KEY (`id`),
  INDEX `place_business_location_id_fk` (`business_location_id` ASC),
  CONSTRAINT `place_business_location_id_fk`
    FOREIGN KEY (`business_location_id` )
    REFERENCES `business_location` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
  )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `place_city_state`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `place_city_state` ;

CREATE  TABLE IF NOT EXISTS `place_city_state` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `version` INT NOT NULL ,
  `city` VARCHAR(255) NOT NULL ,
  `state` VARCHAR(255) NULL DEFAULT NULL ,
  `count` INT DEFAULT 0 ,
  `time_created` BIGINT NULL DEFAULT NULL ,
  `time_updated` BIGINT NULL DEFAULT NULL ,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;



-- -----------------------------------------------------
-- Table `rating`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `rating` ;

CREATE  TABLE IF NOT EXISTS `rating` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `version` INT(11) NOT NULL ,
  `patron_id` BIGINT(20) NOT NULL ,
  `place_id` BIGINT(20) NOT NULL ,
  `type` VARCHAR(255) NULL DEFAULT NULL ,
  `notes` TEXT NULL DEFAULT NULL ,
  `twitter_status_id` BIGINT(20) NULL DEFAULT NULL ,
  `patron_rating` FLOAT(11) NULL DEFAULT NULL ,
  `flag` VARCHAR(16) NULL DEFAULT 'ACTIVE' ,
  `referral_url` VARCHAR(1024) NULL DEFAULT NULL ,
  `referral_token` VARCHAR(255) NULL DEFAULT NULL ,
  `checkin_foursquare` VARCHAR(255) NULL DEFAULT NULL ,
  `checkin_gowalla` VARCHAR(255) NULL DEFAULT NULL ,
  `txid_foursquare` VARCHAR(45) NULL DEFAULT NULL ,
  `txid_gowalla` VARCHAR(45) NULL DEFAULT NULL ,
  `time_created` BIGINT NULL DEFAULT NULL ,
  `time_updated` BIGINT NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `rating_patron_fk` (`patron_id` ASC) ,
  INDEX `rating_place_fk` (`place_id` ASC) ,
  CONSTRAINT `fk_rating_place1`
    FOREIGN KEY (`place_id` )
    REFERENCES `place` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_rating_patron1`
    FOREIGN KEY (`patron_id` )
    REFERENCES `patron` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;

-- -----------------------------------------------------
-- Table `compliment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `compliment` ;

CREATE  TABLE IF NOT EXISTS `compliment` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `version` INT(11) NOT NULL ,
  `patron_id` BIGINT(20),
  `rating_id` BIGINT(20),
  `note` TEXT NULL DEFAULT NULL ,
  `time_created` BIGINT NULL DEFAULT NULL ,
  `time_updated` BIGINT NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `rating_fk` (`rating_id` ASC) ,
  INDEX `patron_fk` (`patron_id` ASC) ,
  CONSTRAINT `fk_compliment_rating1`
    FOREIGN KEY (`rating_id` )
    REFERENCES `rating` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_compliment_patron1`
    FOREIGN KEY (`patron_id` )
    REFERENCES `patron` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION  )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `voucher`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `voucher` ;

CREATE  TABLE IF NOT EXISTS `voucher` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `version` INT(11) NULL DEFAULT NULL ,
  `offer_id` BIGINT(20) NULL DEFAULT NULL ,
  `cust_order_id` BIGINT(20) NULL DEFAULT NULL ,
  `redemption_code` VARCHAR(255) NULL DEFAULT NULL ,
  `metadata` TEXT NULL DEFAULT NULL ,
  `notes` TEXT NULL DEFAULT NULL ,
  `image_url` VARCHAR(255) NULL DEFAULT NULL ,
  `status` VARCHAR(45) NULL DEFAULT NULL ,
  `time_expires` BIGINT(20) NULL DEFAULT NULL ,
  `time_aquired` BIGINT(20) NULL DEFAULT NULL ,
  `time_redeemed` BIGINT(20) NULL DEFAULT NULL ,
  `time_cancelled` BIGINT(20) NULL DEFAULT NULL ,
  `time_created` BIGINT(20) NULL DEFAULT NULL ,
  `time_updated` BIGINT(20) NULL DEFAULT NULL ,
  PRIMARY KEY (`id`),
  INDEX `fk_voucher_offer1` (`offer_id` ASC) ,
  INDEX `fk_voucher_order1` (`cust_order_id` ASC) ,
  CONSTRAINT `fk_voucher_offer1`
    FOREIGN KEY (`offer_id` )
    REFERENCES `offer` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_voucher_order1`
    FOREIGN KEY (`cust_order_id` )
    REFERENCES `cust_order` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `cust_order`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cust_order` ;

CREATE  TABLE IF NOT EXISTS `cust_order` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `version` INT(11) NULL DEFAULT NULL ,
  `offer_id` BIGINT(20) NOT NULL ,
  `patron_id` BIGINT(20) NOT NULL ,
  `external_id` VARCHAR(255) NULL DEFAULT NULL ,
  `external_txid` VARCHAR(255) NULL DEFAULT NULL ,
  `channel` VARCHAR(45) NULL DEFAULT NULL ,
  `buyer_name` VARCHAR(128) NULL DEFAULT NULL ,
  `buyer_email` VARCHAR(255) NULL DEFAULT NULL ,
  `shipping_name` VARCHAR(128) NULL DEFAULT NULL ,
  `address_one` VARCHAR(128) NULL DEFAULT NULL ,
  `address_two` VARCHAR(128) NULL DEFAULT NULL ,
  `city` VARCHAR(128) NULL DEFAULT NULL ,
  `state` VARCHAR(45) NULL DEFAULT NULL ,
  `postal_code` VARCHAR(45) NULL DEFAULT NULL ,
  `country_code` VARCHAR(8) NULL DEFAULT NULL ,
  `external_orderitem` VARCHAR(255) NULL DEFAULT NULL ,
  `sku` VARCHAR(255) NULL DEFAULT NULL ,
  `title` VARCHAR(255) NULL DEFAULT NULL ,
  `description` TEXT NULL DEFAULT NULL ,
  `price` FLOAT(11) NULL DEFAULT NULL ,
  `quantity` INT(11) NULL DEFAULT NULL ,
  `status` VARCHAR(45) NULL DEFAULT NULL ,
  `time_created` BIGINT(20) NULL DEFAULT NULL ,
  `time_updated` BIGINT(20) NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_cust_order_offer1` (`offer_id` ASC) ,
  INDEX `cust_order_patron_fk` (`patron_id` ASC) ,
  CONSTRAINT `fk_cust_order_offer1`
    FOREIGN KEY (`offer_id` )
    REFERENCES `offer` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_cust_order_patron1`
    FOREIGN KEY (`patron_id` )
    REFERENCES `patron` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `image_value`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `image_value` ;

CREATE  TABLE IF NOT EXISTS `image_value` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `content_type` VARCHAR(255) NULL DEFAULT NULL ,
  `url` VARCHAR(1025) NULL DEFAULT NULL ,
  `type` VARCHAR(45) NULL DEFAULT NULL ,
  `attachement_key` VARCHAR(255) NULL ,
  `description` TEXT NULL ,
  `time_created` BIGINT(20) NULL DEFAULT NULL ,
  `time_updated` BIGINT(20) NULL DEFAULT NULL ,  
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
AUTO_INCREMENT = 633
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `place_attribute`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `place_attribute` ;

CREATE  TABLE IF NOT EXISTS `place_attribute` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `version` INT,
  `name` VARCHAR(255) NOT NULL ,
  `attribute_value` TEXT NULL DEFAULT NULL ,
  `place_id` BIGINT(20) NULL DEFAULT NULL ,
  `time_created` BIGINT(20) NULL DEFAULT NULL ,
  `time_updated` BIGINT(20) NULL DEFAULT NULL ,  
  PRIMARY KEY (`id`) ,
  INDEX `place_fk` (`place_id` ASC) ,
  CONSTRAINT `fk_place_attribute_place1`
    FOREIGN KEY (`place_id` )
    REFERENCES `place` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 976
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `rating_attribute`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `rating_attribute` ;

CREATE  TABLE IF NOT EXISTS `rating_attribute` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `version` INT,
  `name` VARCHAR(255) NOT NULL ,
  `attribute_value` TEXT NULL DEFAULT NULL ,
  `rating_id` BIGINT(20) NULL DEFAULT NULL ,
  `time_created` BIGINT(20) NULL DEFAULT NULL ,
  `time_updated` BIGINT(20) NULL DEFAULT NULL , 
  PRIMARY KEY (`id`) ,
  INDEX `rating_attribute_rating_fk` (`rating_id` ASC) ,
  CONSTRAINT `fk_rating_attribute_rating1`
    FOREIGN KEY (`rating_id` )
    REFERENCES `rating` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 11943
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `role` ;

CREATE  TABLE IF NOT EXISTS `role` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT ,
  `version` INT(11) NULL DEFAULT NULL ,
  `role` VARCHAR(255) NULL DEFAULT NULL ,
  `role_group` VARCHAR(255) NULL DEFAULT NULL ,
  `principal_id` BIGINT(20) NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
AUTO_INCREMENT = 1602
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `patron`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `patron` ;

CREATE  TABLE IF NOT EXISTS `patron` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `version` INT(11) NOT NULL ,
  `user_principal_id` BIGINT(20) NOT NULL ,
  `username` VARCHAR(255) NULL DEFAULT NULL ,
  `secretkey` VARCHAR(255) NULL DEFAULT NULL ,
  `score` BIGINT(20) NULL DEFAULT '0' ,
  `profile_image_attachment_key` VARCHAR(255) NULL DEFAULT NULL ,
  `image_attachment_key` VARCHAR(255) NULL DEFAULT NULL ,
  `category_attachment_key` VARCHAR(255) NULL DEFAULT NULL ,
  `guid` VARCHAR(45) NULL DEFAULT NULL ,
  `status` VARCHAR(12) NULL DEFAULT NULL ,
  `auth_foursquare` VARCHAR(12) NULL DEFAULT 'false' ,
  `auth_gowalla` VARCHAR(12) NULL DEFAULT 'false' ,
  `time_created` BIGINT(20) NULL DEFAULT NULL ,
  `time_updated` BIGINT(20) NULL DEFAULT NULL ,   
  PRIMARY KEY (`id`) ,
  INDEX `fk_patron_user_principal1` (`user_principal_id` ASC) ,
  CONSTRAINT `fk_patron_user_principal1`
    FOREIGN KEY (`user_principal_id` )
    REFERENCES `user_principal` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `network_member`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `network_member` ;

CREATE  TABLE IF NOT EXISTS `network_member` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `version` INT(11) NOT NULL ,
  `user_principal_id` BIGINT(20) NOT NULL ,
  `name` VARCHAR(255) NULL DEFAULT NULL ,
  `primary_email` VARCHAR(255) NULL DEFAULT NULL ,
  `paypal_email` VARCHAR(255) NULL DEFAULT NULL ,  
  `member_key` VARCHAR(255) NULL DEFAULT NULL ,
  `description` TEXT NULL DEFAULT NULL ,
  `icon_url` VARCHAR(1024) NULL DEFAULT NULL ,
  `map_icon_url` VARCHAR(1024) NULL DEFAULT NULL ,
  `member_type` ENUM('PUBLISHER','AFFILIATE','MERCHANT') NULL DEFAULT NULL ,
  `time_created` BIGINT(20) NULL DEFAULT NULL ,
  `time_updated` BIGINT(20) NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `network_member_user_principal_fk` (`user_principal_id` ASC) ,
  CONSTRAINT `fk_network_member_user_principal1`
    FOREIGN KEY (`user_principal_id` )
    REFERENCES `user_principal` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `publisher`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `publisher` ;

CREATE  TABLE IF NOT EXISTS `publisher` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `version` INT(11) NOT NULL ,
  `url` VARCHAR(1024) NULL DEFAULT NULL ,
  `site_name` VARCHAR(255) NULL DEFAULT NULL ,
  `monthly_pageviews` INT NULL DEFAULT NULL ,
  `network_member_id` BIGINT(20) NOT NULL ,
  `time_created` BIGINT(20) NULL DEFAULT NULL ,
  `time_updated` BIGINT(20) NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `publisher_network_member1_fk` (`network_member_id` ASC) ,
  CONSTRAINT `fk_publisher_network_member1`
    FOREIGN KEY (`network_member_id` )
    REFERENCES `network_member` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `merchant`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `merchant` ;

CREATE  TABLE IF NOT EXISTS `merchant` (
  `id` BIGINT NOT NULL AUTO_INCREMENT ,
  `version` INT(11) NOT NULL ,
  `network_member_id` BIGINT(20) NOT NULL ,
  `business_id` BIGINT(20) NOT NULL ,
  `voucher_verification_phone` VARCHAR(255) NULL DEFAULT NULL ,
  `time_created` BIGINT(20) NULL DEFAULT NULL ,
  `time_updated` BIGINT(20) NULL DEFAULT NULL ,  
  PRIMARY KEY (`id`) ,
  INDEX `fk_merchant_network_member1` (`network_member_id` ASC) ,
  INDEX `fk_merchant_business1` (`business_id` ASC) ,
  CONSTRAINT `fk_merchant_network_member1`
    FOREIGN KEY (`network_member_id` )
    REFERENCES `network_member` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_merchant_business1`
    FOREIGN KEY (`business_id` )
    REFERENCES `business` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `event`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `event` ;

CREATE  TABLE IF NOT EXISTS `event` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `version` INT NULL ,
  `publisher_id` BIGINT(20) NOT NULL ,
  `place_id` BIGINT(20) NOT NULL ,
  `name` VARCHAR(255) NULL DEFAULT NULL ,
  `phone` VARCHAR(32) NULL DEFAULT NULL ,
  `whenText` VARCHAR(255) NULL DEFAULT NULL ,
  `url` VARCHAR(1024) NULL DEFAULT NULL ,
  `cost` FLOAT(11) NULL DEFAULT NULL ,
  `description` TEXT NULL DEFAULT NULL ,
  `category_attachment_key` VARCHAR(45) NULL DEFAULT NULL ,
  `image_attachment_key` VARCHAR(45) NULL DEFAULT NULL ,
  `time_starts` BIGINT(20) NULL DEFAULT NULL ,
  `time_ends` BIGINT(20) NULL DEFAULT NULL ,
  `alarm_data` VARCHAR(1024) NULL DEFAULT NULL ,
  `alarm_time` BIGINT(20) NULL DEFAULT NULL ,
  `recurrance_type` ENUM('DAILY','WEEKLY','MONTHLY','YEARLY','HOURLY','MINUTELY') NULL DEFAULT NULL ,
  `recurrance_interval` INT(11) NULL DEFAULT NULL ,
  `recurrance_data` VARCHAR(1024) NULL DEFAULT NULL ,
  `recurrance_end` BIGINT(20) NULL DEFAULT NULL ,
  `time_created` BIGINT(20) NULL DEFAULT NULL ,
  `time_updated` BIGINT(20) NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_event_publisher` (`publisher_id` ASC) ,
  INDEX `fk_event_place` (`place_id` ASC) ,
  CONSTRAINT `fk_event_publisher`
    FOREIGN KEY (`publisher_id` )
    REFERENCES `publisher` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_event_place`
    FOREIGN KEY (`place_id` )
    REFERENCES `place` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `business_location`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `business_location` ;

CREATE  TABLE IF NOT EXISTS `business_location` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `version` INT(11) NOT NULL ,
  `place_id` BIGINT(20) NOT NULL ,
  `business_id` BIGINT(20) NULL DEFAULT NULL ,  
  `phone` VARCHAR(16) NULL DEFAULT NULL ,
  `url` VARCHAR(1024) NULL DEFAULT NULL ,
  `flag` VARCHAR(16) NULL DEFAULT 'ACTIVE' ,
  `description` TEXT NULL DEFAULT NULL ,
  `name` VARCHAR(255) NULL DEFAULT NULL ,
  `time_created` BIGINT(20) NULL DEFAULT NULL ,
  `time_updated` BIGINT(20) NULL DEFAULT NULL ,  
  PRIMARY KEY (`id`) ,
  INDEX `fk_business` (`business_id` ASC) ,
  INDEX `fk_place` (`place_id` ASC),
  CONSTRAINT `fk_business`
    FOREIGN KEY (`business_id` )
    REFERENCES `business` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_place`
    FOREIGN KEY (`place_id` )
    REFERENCES `place` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 16
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `award_has_offer`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `award_has_offer` ;

CREATE  TABLE IF NOT EXISTS `award_has_offer` (
  `award_id` BIGINT(20) NULL DEFAULT NULL ,
  `offer_id` BIGINT(20) NULL DEFAULT NULL ,
  PRIMARY KEY (`award_id`, `offer_id`) ,
  INDEX `fk_award` (`award_id` ASC) ,
  INDEX `fk_offer` (`offer_id` ASC) ,
  CONSTRAINT `fk_award`
    FOREIGN KEY (`award_id` )
    REFERENCES `award` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_offer`
    FOREIGN KEY (`offer_id` )
    REFERENCES `offer` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)  
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `affiliate`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `affiliate` ;

CREATE  TABLE IF NOT EXISTS `affiliate` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `version` INT NOT NULL ,  
  `network_member_id` BIGINT(20) NOT NULL ,
  `time_created` BIGINT(20) NULL DEFAULT NULL ,
  `time_updated` BIGINT(20) NULL DEFAULT NULL , 
  PRIMARY KEY (`id`) ,
  INDEX `fk_affiliate_network_member1` (`network_member_id` ASC) ,
  CONSTRAINT `fk_affiliate_network_member1`
    FOREIGN KEY (`network_member_id` )
    REFERENCES `network_member` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `offer_economics`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `offer_economics` ;

CREATE  TABLE IF NOT EXISTS `offer_economics` (
	 `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
	 `version` INT NOT NULL ,
	 `publisher_id` BIGINT(20) ,
	 `merchant_id` BIGINT(20)  ,
	 `affiliate_id` BIGINT(20) ,
	 `offer_id` BIGINT(20) ,
	 `publisher_revenue_percentage` FLOAT  ,
	 `affiliate_revenue_percentage` FLOAT  ,
	 `merchant_revenue_percentage` FLOAT ,
	 `time_created` BIGINT(20) NULL DEFAULT NULL ,
	 `time_updated` BIGINT(20) NULL DEFAULT NULL ,
	 PRIMARY KEY (`id`) ,
	 INDEX `fk_offer_economics_publisher1` (`publisher_id` ASC) ,
	 INDEX `fk_offer_economics_merchant1` (`merchant_id` ASC) ,
	 INDEX `fk_offer_economics_affiliate1` (`affiliate_id` ASC) ,
	 INDEX `fk_offer_economics_offer1` (`offer_id` ASC) ,
	 CONSTRAINT `fk_offer_economics_publisher1`
		 FOREIGN KEY (`publisher_id` )
		 REFERENCES `publisher` (`id` )
		 ON DELETE NO ACTION
		 ON UPDATE NO ACTION,
	 CONSTRAINT `fk_offer_economics_merchant1`
		 FOREIGN KEY (`merchant_id` )
		 REFERENCES `merchant` (`id` )
		 ON DELETE NO ACTION
		 ON UPDATE NO ACTION,
	 CONSTRAINT `fk_offer_economics_affiliate1`
		 FOREIGN KEY (`affiliate_id` )
		 REFERENCES `affiliate` (`id` )
		 ON DELETE NO ACTION
		 ON UPDATE NO ACTION,
	 CONSTRAINT `fk_offer_economics_offer1`
		 FOREIGN KEY (`offer_id` )
		 REFERENCES `offer` (`id` )
		 ON DELETE NO ACTION
		 ON UPDATE NO ACTION)
 ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `affiliate_has_business`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `affiliate_has_business` ;

CREATE  TABLE IF NOT EXISTS `affiliate_has_business` (
  `affiliate_id` BIGINT(20) NOT NULL ,
  `business_id` BIGINT(20) NOT NULL ,
  PRIMARY KEY (`affiliate_id`, `business_id`) ,
  INDEX `fk_affiliate_has_business_business1` (`business_id` ASC) ,
  INDEX `fk_affiliate_has_business_affiliate1` (`affiliate_id` ASC) ,
  CONSTRAINT `fk_affiliate_has_business_affiliate1`
    FOREIGN KEY (`affiliate_id` )
    REFERENCES `affiliate` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_affiliate_has_business_business1`
    FOREIGN KEY (`business_id` )
    REFERENCES `business` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `article`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `article` ;

CREATE  TABLE IF NOT EXISTS `article` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `publisher_id` BIGINT(20) NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_article_publisher1` (`publisher_id` ASC) ,
  CONSTRAINT `fk_article_publisher1`
    FOREIGN KEY (`publisher_id` )
    REFERENCES `publisher` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
