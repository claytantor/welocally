INSERT INTO `award_type` VALUES 
(10,'VIP','all',0,'2010-11-07 00:00:00',1,'Very Important Person','vip',NULL, 0, NULL, NULL, 'place');

ALTER TABLE `place` ADD COLUMN `email` VARCHAR(255) AFTER `business_location_id`;
ALTER TABLE `place` ADD COLUMN `business_services` VARCHAR(10) AFTER `email`;
update place set business_services='false';

UPDATE award_type set name="Friends of RateCred" where id=2;


