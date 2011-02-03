#rater aggregator fields
ALTER TABLE `ratecred_080_etl`.`rater` ADD COLUMN `auth_foursquare` VARCHAR(12) AFTER `status`,
 ADD COLUMN `auth_gowalla` VARCHAR(12) AFTER `auth_foursquare`;
ALTER TABLE `ratecred_080_etl`.`rater` MODIFY COLUMN `auth_foursquare` VARCHAR(12) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT 'false',
 MODIFY COLUMN `auth_gowalla` VARCHAR(12) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT 'false';
 
#rating
ALTER TABLE `ratecred_080_etl`.`rating` ADD COLUMN `checkin_foursquare` VARCHAR(45) AFTER `referal_token`,
 ADD COLUMN `checkin_gowalla` VARCHAR(45) AFTER `checkin_foursquare`;
 
 

 