ALTER TABLE `ratecred_080_etl`.`rating` ADD COLUMN `txid_foursquare` VARCHAR(45) AFTER `checkin_gowalla`,
 ADD COLUMN `txid_gowalla` VARCHAR(45) AFTER `txid_foursquare`;