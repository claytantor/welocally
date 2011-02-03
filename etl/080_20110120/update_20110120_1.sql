ALTER TABLE `ratecred_080_etl`.`rating` ADD COLUMN `referal_url` LONGTEXT AFTER `flag`,
 ADD COLUMN `referal_token` VARCHAR(45) AFTER `referal_url`;