SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

--
-- Table structure for table `award`
--

DROP TABLE IF EXISTS `award`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `award` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `award_type_id` bigint(20) NOT NULL,
  `rater_id` bigint(20) DEFAULT NULL,
  `time_created` datetime DEFAULT NULL,
  `version` int(11) NOT NULL,
  `expires` datetime DEFAULT NULL,
  `notes` longtext,
  `metadata` longtext,
  `time_created_mills` bigint(20) DEFAULT NULL,
  `time_created_gmt` varchar(45) DEFAULT NULL,
  `expires_mills` bigint(20) DEFAULT NULL,
  `expires_gmt` varchar(45) DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `award_type_fk` (`award_type_id`),
  KEY `rater_fk` (`rater_id`),
  KEY `FK58E7A5D7BBD5A3B` (`award_type_id`),
  KEY `FK58E7A5D7E5BCE60` (`rater_id`)
) ENGINE=MyISAM AUTO_INCREMENT=2818 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `award_awardoffer`
--

DROP TABLE IF EXISTS `award_awardoffer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `award_awardoffer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `award_id` bigint(20) DEFAULT NULL,
  `award_offer_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=603 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `award_offer`
--

DROP TABLE IF EXISTS `award_offer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `award_offer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `time_created` datetime DEFAULT NULL,
  `version` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` longtext,
  `status` varchar(45) DEFAULT NULL,
  `external_id` varchar(255) DEFAULT NULL,
  `external_source` varchar(45) DEFAULT NULL,
  `program_id` varchar(45) DEFAULT NULL,
  `program_name` varchar(128) DEFAULT NULL,
  `code` varchar(45) DEFAULT NULL,
  `url` longtext,
  `begin_millis` bigint(20) DEFAULT NULL,
  `expire_millis` bigint(20) DEFAULT NULL,
  `discount_type` varchar(45) DEFAULT NULL,
  `type` varchar(45) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `price` float DEFAULT NULL,
  `value` float DEFAULT NULL,
  `extra_details` longtext,
  `illustration_url` longtext,
  `end_millis` bigint(20) DEFAULT NULL,
  `business_id` bigint(20) DEFAULT NULL,
  `award_type_id` bigint(20) DEFAULT NULL,
  `business_location_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKBB56FDFA1B90EE54` (`business_id`),
  KEY `FKBB56FDFA7BBD5A3B` (`award_type_id`)
) ENGINE=MyISAM AUTO_INCREMENT=2690 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `award_offer_item`
--

DROP TABLE IF EXISTS `award_offer_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `award_offer_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) DEFAULT NULL,
  `time_created` bigint(20) DEFAULT NULL,
  `time_updated` bigint(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `description` longtext,
  `quantity` int(11) DEFAULT NULL,
  `award_offer_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `award_type`
--

DROP TABLE IF EXISTS `award_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `award_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  `cost` bigint(20) DEFAULT '0',
  `time_created` datetime DEFAULT NULL,
  `version` int(11) NOT NULL,
  `description` longtext,
  `keyname` varchar(45) DEFAULT NULL,
  `metadata` longtext,
  `value` int(11) DEFAULT NULL,
  `previous` varchar(45) DEFAULT NULL,
  `next` varchar(45) DEFAULT NULL,
  `category` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2201 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `business`
--

DROP TABLE IF EXISTS `business`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `status` varchar(15) DEFAULT NULL,
  `description` longtext,
  `website` longtext,
  `version` int(11) DEFAULT NULL,
  `username` varchar(16) DEFAULT NULL,
  `time_created` datetime DEFAULT NULL,
  `guid` varchar(45) DEFAULT NULL,
  `advertiser_id` varchar(255) DEFAULT NULL,
  `advertiser_source` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `business_attribute`
--

DROP TABLE IF EXISTS `business_attribute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business_attribute` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `value` longtext,
  `business_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `business_fk` (`business_id`),
  KEY `FK7390B4DD1B90EE54` (`business_id`)
) ENGINE=MyISAM AUTO_INCREMENT=888 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `business_image`
--

DROP TABLE IF EXISTS `business_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business_image` (
  `id` bigint(20) NOT NULL DEFAULT '0',
  `version` int(11) DEFAULT NULL,
  `imagevalue_id` bigint(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `time_created` datetime DEFAULT NULL,
  `business_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5ECDEFDCE45292DA` (`business_id`),
  KEY `FK5ECDEFDCFE4A8C89` (`imagevalue_id`),
  KEY `FK8B9CFC1CFE4A8C8B` (`imagevalue_id`),
  CONSTRAINT `FK8B9CFC1CFE4A8C8B` FOREIGN KEY (`imagevalue_id`) REFERENCES `image_value` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `business_location`
--

DROP TABLE IF EXISTS `business_location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business_location` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address1` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `state` varchar(2) DEFAULT NULL,
  `zip` varchar(10) DEFAULT NULL,
  `phone` varchar(16) DEFAULT NULL,
  `local_website` longtext,
  `flag` varchar(16) DEFAULT 'ACTIVE',
  `place_id` bigint(20) NOT NULL,
  `version` int(11) NOT NULL,
  `time_created` datetime DEFAULT NULL,
  `business_id` bigint(20) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `descr` longtext,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK960BC9541B90EE54` (`business_id`),
  KEY `FK960BC954DF3D4200` (`place_id`)
) ENGINE=MyISAM AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `business_location_image`
--

DROP TABLE IF EXISTS `business_location_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business_location_image` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) DEFAULT NULL,
  `imagevalue_id` bigint(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `time_created` datetime DEFAULT NULL,
  `business_location_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKBUSSINESSLOCID` (`business_location_id`),
  KEY `FK69F386B0FE4A8C8B` (`imagevalue_id`),
  CONSTRAINT `FK69F386B0FE4A8C8B` FOREIGN KEY (`imagevalue_id`) REFERENCES `image_value` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `business_metrics`
--

DROP TABLE IF EXISTS `business_metrics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business_metrics` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL,
  `yays` int(11) NOT NULL,
  `boos` int(11) NOT NULL,
  `ratings` int(11) NOT NULL,
  `rating_avg` float DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `start_time_mills` bigint(20) DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `end_time_mills` bigint(20) DEFAULT NULL,
  `business_id` bigint(20) DEFAULT NULL,
  `business_location_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=13354 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `compliment`
--

DROP TABLE IF EXISTS `compliment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `compliment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rater_id` bigint(20) NOT NULL,
  `rating_id` bigint(20) NOT NULL,
  `time_created` datetime DEFAULT NULL,
  `time_gmt` varchar(45) DEFAULT NULL,
  `note` longtext,
  `version` int(11) NOT NULL,
  `time_mills` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `rating_fk` (`rating_id`),
  KEY `rater_fk` (`rater_id`),
  KEY `FKDFF3042AF38F1314` (`rating_id`),
  KEY `FKDFF3042A7E5BCE60` (`rater_id`)
) ENGINE=MyISAM AUTO_INCREMENT=2864 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cust_order`
--

DROP TABLE IF EXISTS `cust_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cust_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) DEFAULT NULL,
  `time_created` bigint(20) DEFAULT NULL,
  `time_updated` bigint(20) DEFAULT NULL,
  `external_id` varchar(255) DEFAULT NULL,
  `external_txid` varchar(255) DEFAULT NULL,
  `channel` varchar(45) DEFAULT NULL,
  `buyer_name` varchar(128) DEFAULT NULL,
  `buyer_email` varchar(255) DEFAULT NULL,
  `shipping_name` varchar(128) DEFAULT NULL,
  `address_one` varchar(128) DEFAULT NULL,
  `address_two` varchar(128) DEFAULT NULL,
  `city` varchar(128) DEFAULT NULL,
  `state` varchar(45) DEFAULT NULL,
  `postal_code` varchar(45) DEFAULT NULL,
  `country_code` varchar(8) DEFAULT NULL,
  `external_orderitem` varchar(255) DEFAULT NULL,
  `sku` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `description` longtext,
  `price` float DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `rater_id` bigint(20) DEFAULT NULL,
  `awardoffer_id` bigint(20) DEFAULT NULL,
  `voucher_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `image_value`
--

DROP TABLE IF EXISTS `image_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `image_value` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `image` blob,
  `content_type` varchar(255) DEFAULT NULL,
  `filename` varchar(255) DEFAULT NULL,
  `type` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=633 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `place`
--

DROP TABLE IF EXISTS `place`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `place` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `address1` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `zip` varchar(45) DEFAULT NULL,
  `twitter_id` varchar(255) DEFAULT NULL,
  `sg_id` longtext,
  `phone` varchar(45) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `descr` longtext,
  `type` varchar(255) DEFAULT NULL,
  `flag` varchar(16) DEFAULT 'ACTIVE',
  `version` int(11) NOT NULL,
  `time_created` datetime DEFAULT NULL,
  `website` longtext,
  `business_location_id` bigint(20) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `business_services` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK65CD90748C99615` (`business_location_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3834 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `place_attribute`
--

DROP TABLE IF EXISTS `place_attribute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `place_attribute` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `value` longtext,
  `place_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `place_fk` (`place_id`),
  KEY `FK592B2164DF3D4200` (`place_id`)
) ENGINE=MyISAM AUTO_INCREMENT=976 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `place_rating`
--

DROP TABLE IF EXISTS `place_rating`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `place_rating` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(100) NOT NULL,
  `place_id` bigint(20) DEFAULT NULL,
  `rating_avg` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `place_fk` (`place_id`),
  KEY `FK79DAEFD5DF3D4200` (`place_id`)
) ENGINE=MyISAM AUTO_INCREMENT=1986 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rater`
--

DROP TABLE IF EXISTS `rater`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rater` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `secretkey` varchar(255) DEFAULT NULL,
  `time_created` datetime DEFAULT NULL,
  `score` bigint(20) DEFAULT '0',
  `imagevalue_id` bigint(20) DEFAULT NULL,
  `guid` varchar(45) DEFAULT NULL,
  `status` varchar(12) DEFAULT NULL,
  `auth_foursquare` varchar(12) DEFAULT 'false',
  `auth_gowalla` varchar(12) DEFAULT 'false',
  PRIMARY KEY (`id`),
  KEY `FKA471F14CFE4A8C8B` (`imagevalue_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3432 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rating`
--

DROP TABLE IF EXISTS `rating`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rating` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  `notes` longtext,
  `time_created` datetime DEFAULT NULL,
  `time_mills` bigint(20) DEFAULT NULL,
  `time_gmt` varchar(45) DEFAULT NULL,
  `rater_id` bigint(20) DEFAULT NULL,
  `twitter_status_id` bigint(20) DEFAULT NULL,
  `rater_rating` float DEFAULT NULL,
  `user_rating` float DEFAULT NULL,
  `place_id` bigint(20) DEFAULT NULL,
  `flag` varchar(16) DEFAULT 'ACTIVE',
  `referal_url` longtext,
  `referal_token` varchar(45) DEFAULT NULL,
  `checkin_foursquare` varchar(45) DEFAULT NULL,
  `checkin_gowalla` varchar(254) DEFAULT NULL,
  `txid_foursquare` varchar(45) DEFAULT NULL,
  `txid_gowalla` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKCB7F8BE62675F586` (`rater_id`),
  KEY `FKCB7F8BE6DF3D4200` (`place_id`),
  KEY `FKCB7F8BE67E5BCE60` (`rater_id`)
) ENGINE=MyISAM AUTO_INCREMENT=2408 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rating_attribute`
--

DROP TABLE IF EXISTS `rating_attribute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rating_attribute` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `rating_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK534F09834B74EBEE` (`rating_id`),
  KEY `FK534F0983F38F1314` (`rating_id`)
) ENGINE=MyISAM AUTO_INCREMENT=11943 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `version` int(11) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `role_group` varchar(255) DEFAULT NULL,
  `principal_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1602 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_principal`
--

DROP TABLE IF EXISTS `user_principal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_principal` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `expired` varchar(10) DEFAULT NULL,
  `credentials_expired` varchar(10) DEFAULT NULL,
  `locked` varchar(10) DEFAULT NULL,
  `enabled` varchar(10) DEFAULT NULL,
  `guid` varchar(45) DEFAULT NULL,
  `user_class` varchar(255) DEFAULT NULL,
  `twitter_id` bigint(20) DEFAULT NULL,
  `twitter_username` varchar(45) DEFAULT NULL,
  `twitter_token` varchar(255) DEFAULT NULL,
  `twitter_secret` varchar(255) DEFAULT NULL,
  `twitter_verify` varchar(255) DEFAULT NULL,
  `time_created_mills` bigint(20) DEFAULT NULL,
  `twitter_profile_img` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=782 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `voucher`
--

DROP TABLE IF EXISTS `voucher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `voucher` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) DEFAULT NULL,
  `time_created` bigint(20) DEFAULT NULL,
  `time_updated` bigint(20) DEFAULT NULL,
  `external_offerid` varchar(255) DEFAULT NULL,
  `external_source` varchar(45) DEFAULT NULL,
  `reservation_id` varchar(255) DEFAULT NULL,
  `barcode` varchar(255) DEFAULT NULL,
  `print_url` varchar(255) DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `time_expires` bigint(20) DEFAULT NULL,
  `time_aquired` bigint(20) DEFAULT NULL,
  `time_redeemed` bigint(20) DEFAULT NULL,
  `time_cancelled` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;